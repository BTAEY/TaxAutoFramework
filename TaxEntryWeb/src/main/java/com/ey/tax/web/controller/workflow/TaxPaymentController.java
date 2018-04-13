package com.ey.tax.web.controller.workflow;

import com.ey.tax.common.AttachmentEnums;
import com.ey.tax.core.service.IAttachmentStoreService;
import com.ey.tax.core.service.ITaxPaymentService;
import com.ey.tax.entity.AttachmentStore;
import com.ey.tax.entity.TaxPayment;
import com.ey.tax.security.SecurityUser;
import com.ey.tax.service.IAttachmentStoreDelegate;
import com.ey.tax.service.UploadService;
import com.ey.tax.service.WorkflowService;
import com.ey.tax.utils.DateUtil;
import com.ey.tax.utils.StringUtil;
import com.ey.tax.vo.AuditHistoryVo;
import com.ey.tax.vo.taxpayment.AuditorInfo;
import com.ey.tax.vo.taxpayment.RegisterInfo;
import com.ey.tax.vo.taxpayment.SubmitterInfo;
import com.google.common.base.Joiner;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by zhuji on 4/9/2018.
 */
@Controller
public class TaxPaymentController implements IAttachmentStoreDelegate {
    private Logger logger = LogManager.getLogger();

    @Autowired
    private IAttachmentStoreService attachmentStoreService;

    @Autowired
    private ITaxPaymentService taxPaymentService;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @RequestMapping(value="/taxPayment/forward/submitForm/{taskId}")
    public ModelAndView forwardSubmitForm(@PathVariable String taskId,Authentication authentication, String comment){
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String procId = task.getProcessDefinitionId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(procId).singleResult();

        SubmitterInfo submitterInfo = new SubmitterInfo();
        submitterInfo.setApplier(user.getUsername());
        submitterInfo.setStation("代扣代缴业务经办岗");
        submitterInfo.setOrganization("浙商总行");

        ModelAndView mav = new ModelAndView("workflow/taxPayment_1");
        mav.addObject("submitter",submitterInfo);
        mav.addObject("wfname",processDefinition.getName());
        mav.addObject("taskId",taskId);
        return mav;
    }

    @RequestMapping(value = "/taxPayment/processTask/submit", method = RequestMethod.POST, headers = "Content-Type=multipart/form-data")
    public String submit(MultipartHttpServletRequest request,Authentication authentication,SubmitterInfo submitterInfo,String comment){
        String taskId = request.getParameter("taskId");
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getExecutionId();

        List<MultipartFile> multipartFiles = new ArrayList<MultipartFile>();
        Optional.ofNullable(request.getMultiFileMap()).ifPresent(m -> {
            m.values().forEach((t) -> {
                multipartFiles.addAll(t);
            });
        });
        uploadService.upload(multipartFiles,this);

        if(StringUtil.isNotEmpty(comment)){
            taskService.addComment(taskId,processInstanceId,comment);
        }

        submitterInfo.setTaskId(taskId);

        Map<String,Object> variables = new HashMap<>();
        variables.put("submitter",submitterInfo);
        taskService.setVariables(taskId,variables);
        taskService.complete(taskId);

        return "redirect:/workflowlist";
    }

    @RequestMapping(value="/taxPayment/forward/registerForm/{taskId}")
    public ModelAndView forwardRegisterForm(@PathVariable String taskId,Authentication authentication){
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String procId = task.getProcessDefinitionId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(procId).singleResult();

        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .finished()
                .orderByHistoricTaskInstanceStartTime()
                .asc()
                .list();

        RegisterInfo registerInfo = new RegisterInfo();
        registerInfo.setApplier(user.getUsername());
        registerInfo.setStation("总行其他税费纳税管理岗");
        registerInfo.setOrganization("浙商总行");

        List<AuditHistoryVo> historyList = new ArrayList<>();
//        historicTaskInstanceList.stream().forEach((e) -> {
//            AuditHistoryVo vo = new AuditHistoryVo();
//            vo.setAction(e.getName());
//            List<Comment> comments = taskService.getTaskComments(e.getId());
//            vo.setComment(Joiner.on(",").join(comments));
//            SubmitterInfo submitterInfo = (SubmitterInfo) taskService.getVariable(e.getId(),"submitter");
//            vo.setTaskName(submitterInfo.getStation());
//            historyList.add(vo);
//        });

        for(HistoricTaskInstance historicTaskInstance: historicTaskInstanceList){
            AuditHistoryVo vo = new AuditHistoryVo();
            vo.setAction(historicTaskInstance.getName());
            if(runtimeService.getVariable(task.getExecutionId(),"submitter") != null){
                SubmitterInfo submitterInfo = (SubmitterInfo) runtimeService.getVariable(task.getExecutionId(),"submitter");
                if(historicTaskInstance.getId().equals(submitterInfo.getTaskId())){
                    vo.setTaskName(submitterInfo.getStation());
                }
            }
            if(runtimeService.getVariable(task.getExecutionId(),"register") != null){
                RegisterInfo registerInfo2 = (RegisterInfo) runtimeService.getVariable(task.getExecutionId(),"register");
                if(historicTaskInstance.getId().equals(registerInfo2.getTaskId())){
                    vo.setTaskName(registerInfo2.getStation());
                }
            }
            if(runtimeService.getVariable(task.getExecutionId(),"auditor") != null){
                AuditorInfo auditor = (AuditorInfo) runtimeService.getVariable(task.getExecutionId(),"auditor");
                if(historicTaskInstance.getId().equals(auditor.getTaskId())){
                    vo.setTaskName(auditor.getStation());
                }
            }
            List<Comment> comments = taskService.getTaskComments(historicTaskInstance.getId());
            String commentStr = comments.stream().map(c -> c.getFullMessage()).collect(Collectors.joining(","));
            vo.setComment(commentStr);
            historyList.add(vo);
        }

        ModelAndView mav = new ModelAndView("workflow/taxPayment_2");
        mav.addObject("register",registerInfo);
        mav.addObject("wfname",processDefinition.getName());
        mav.addObject("taskId",taskId);
        mav.addObject("auditHistory",historyList);
        return mav;
    }

    @RequestMapping(value = "/taxPayment/processTask/register", method = RequestMethod.POST)
    public String register(MultipartHttpServletRequest request, Authentication authentication, RegisterInfo registerInfo, String comment){
        String taskId = request.getParameter("taskId");
        SecurityUser currentUser = (SecurityUser) authentication.getPrincipal();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getExecutionId();

        taskService.claim(taskId,currentUser.getUsername());

        List<MultipartFile> multipartFiles = new ArrayList<MultipartFile>();
        Optional.ofNullable(request.getMultiFileMap()).ifPresent(m -> {
            m.values().forEach((t) -> {
                multipartFiles.addAll(t);
            });
        });
        uploadService.upload(multipartFiles,this);

        if(StringUtil.isNotEmpty(comment)){
            taskService.addComment(taskId,processInstanceId,comment);
        }
        registerInfo.setTaskId(taskId);

        Map<String,Object> variables = new HashMap<>();
        variables.put("register",registerInfo);
        taskService.setVariables(taskId,variables);
        taskService.complete(taskId);
        return "redirect:/workflowlist";
    }

    @RequestMapping(value = "/taxPayment/forward/auditForm/{taskId}")
    public ModelAndView forwardAuditForm(@PathVariable String taskId,Authentication authentication){
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .finished()
                .orderByHistoricTaskInstanceStartTime()
                .asc()
                .list();

        List<AuditHistoryVo> historyList = new ArrayList<>();
        for(HistoricTaskInstance historicTaskInstance: historicTaskInstanceList){
            AuditHistoryVo vo = new AuditHistoryVo();
            vo.setAction(historicTaskInstance.getName());

            if(runtimeService.getVariable(task.getExecutionId(),"submitter") != null){
                SubmitterInfo submitterInfo = (SubmitterInfo) runtimeService.getVariable(task.getExecutionId(),"submitter");
                if(historicTaskInstance.getId().equals(submitterInfo.getTaskId())){
                    vo.setTaskName(submitterInfo.getStation());
                }
            }
            if(runtimeService.getVariable(task.getExecutionId(),"register") != null){
                RegisterInfo registerInfo = (RegisterInfo) runtimeService.getVariable(task.getExecutionId(),"register");
                if(historicTaskInstance.getId().equals(registerInfo.getTaskId())){
                    vo.setTaskName(registerInfo.getStation());
                }
            }
            if(runtimeService.getVariable(task.getExecutionId(),"auditor") != null){
                AuditorInfo auditor = (AuditorInfo) runtimeService.getVariable(task.getExecutionId(),"auditor");
                if(historicTaskInstance.getId().equals(auditor.getTaskId())){
                    vo.setTaskName(auditor.getStation());
                }
            }

            List<Comment> comments = taskService.getTaskComments(historicTaskInstance.getId());
            String commentStr = comments.stream().map(c -> c.getFullMessage()).collect(Collectors.joining(","));
            vo.setComment(commentStr);
            historyList.add(vo);
        }
        RegisterInfo register = (RegisterInfo) runtimeService.getVariable(task.getExecutionId(),"register");

        ModelAndView mav = new ModelAndView("/workflow/taxPayment_3");
        mav.addObject("register",register);
        mav.addObject("auditHistory",historyList);
        mav.addObject("taskId",taskId);
        return mav;

    }

    @RequestMapping(value = "/taxPayment/processTask/audit", method = RequestMethod.POST)
    public String audit(HttpServletRequest request,String result,String comment,Authentication authentication){
        String taskId = request.getParameter("taskId");
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getExecutionId();

        taskService.claim(taskId,user.getUsername());
        if(StringUtil.isNotEmpty(comment)){
            taskService.addComment(taskId,processInstanceId,comment);
        }

        AuditorInfo auditorInfo = new AuditorInfo();
        auditorInfo.setStation("总行其他税费纳税复核岗");
        auditorInfo.setTaskId(taskId);
        Map<String,Object> auditorVariable = new HashMap<>();
        auditorVariable.put("auditor",auditorInfo);
        runtimeService.setVariables(processInstanceId,auditorVariable);

        //使用流程变量指定完成任务后下一个连线
        Map<String,Object> variables = new HashMap<>();
        variables.put("approve",Boolean.valueOf(result));
        taskService.complete(taskId,variables);
        return "redirect:/workflowlist";
    }


    @Override
    public void saveAttachmentStore(String filename, String absolutePath) {
        AttachmentStore attachmentStore = new AttachmentStore();
        attachmentStore.setStoreType(AttachmentEnums.StoreType.Remote);
        attachmentStore.setAttachmentName(filename);
        attachmentStore.setPhysicalPath(absolutePath);
        attachmentStoreService.save(attachmentStore);
    }

    @Override
    public String attachmentName() {
        return "test";
    }
}
