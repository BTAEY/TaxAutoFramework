package com.ey.tax.web.controller.workflow;

import com.ey.tax.common.AttachmentEnums;
import com.ey.tax.core.service.IAttachmentStoreService;
import com.ey.tax.entity.AttachmentStore;
import com.ey.tax.security.SecurityUser;
import com.ey.tax.service.IAttachmentStoreDelegate;
import com.ey.tax.service.TaskPropertyResolverAdapter;
import com.ey.tax.service.UploadService;
import com.ey.tax.service.WorkflowFacadeService;
import com.ey.tax.utils.PropertiesUtil;
import com.ey.tax.utils.StringUtil;
import com.ey.tax.vo.HistoryCommentVo;
import com.ey.tax.vo.WorkflowInfo;
import com.ey.tax.vo.taxpayment.RegisterInfo;
import com.ey.tax.vo.taxpayment.SubmitterInfo;
import com.ey.tax.workflow.FormPageMapping;
import org.activiti.engine.TaskService;
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

/**
 * Created by zhuji on 4/9/2018.
 */
@Controller
public class TaxPaymentController implements IAttachmentStoreDelegate {
    private Logger logger = LogManager.getLogger();

    @Autowired
    private IAttachmentStoreService attachmentStoreService;

    @Autowired
    private WorkflowFacadeService workflowFacadeService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private TaskService taskService;

    @RequestMapping(value="/taxPayment/forward/submitForm/{taskId}")
    public ModelAndView forwardSubmitForm(@PathVariable String taskId,Authentication authentication, String comment){
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        WorkflowInfo workflowInfo = workflowFacadeService.getWorkFlowInfoById(task.getProcessDefinitionId());

        SubmitterInfo submitterInfo = new SubmitterInfo();
        submitterInfo.setApplier(user.getUsername());
        submitterInfo.setStation("代扣代缴业务经办岗");
        submitterInfo.setOrganization("浙商总行");

        ModelAndView mav = new ModelAndView(PropertiesUtil.getString(FormPageMapping.TAX_PAYMENT_SUBMIT_FORM));
        mav.addObject("submitter",submitterInfo);
        mav.addObject("wfname",workflowInfo.getName());
        mav.addObject("taskId",taskId);
        return mav;
    }

    @RequestMapping(value = "/taxPayment/processTask/submit", method = RequestMethod.POST, headers = "Content-Type=multipart/form-data")
    public String submit(MultipartHttpServletRequest request,Authentication authentication,SubmitterInfo submitterInfo,String comment){
        String taskId = request.getParameter("taskId");
        List<MultipartFile> multipartFiles = new ArrayList<MultipartFile>();
        Optional.ofNullable(request.getMultiFileMap()).ifPresent(m -> {
            m.values().forEach((t) -> {
                multipartFiles.addAll(t);
            });
        });
        List<AttachmentStore> attachmentStores = uploadService.upload(multipartFiles,this);

        submitterInfo.setTaskId(taskId);
        final Map<String,Object> variables = new HashMap<>();
        variables.put("submitter",submitterInfo);
        variables.put("to","general");

        workflowFacadeService.completeTask(taskId,new TaskPropertyResolverAdapter(){
            @Override
            public String[] comments() {
                return new String[]{comment};
            }

            @Override
            public Map<String, Object> variables() {
                return variables;
            }

            @Override
            public List<AttachmentStore> attachments() {
                return attachmentStores;
            }
        });
        return "redirect:/workflowlist";
    }

    @RequestMapping(value="/taxPayment/forward/registerForm/{taskId}")
    public ModelAndView forwardRegisterForm(@PathVariable String taskId,Authentication authentication){
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        WorkflowInfo workflowInfo = workflowFacadeService.getWorkFlowInfoById(task.getProcessDefinitionId());

        RegisterInfo registerInfo = new RegisterInfo();
        registerInfo.setApplier(user.getUsername());
        registerInfo.setStation("总行其他税费纳税管理岗");
        registerInfo.setOrganization("浙商总行");

        List<HistoryCommentVo> commentVos = workflowFacadeService.getProcessInstanceComments(task.getProcessInstanceId());
        taskService.claim(taskId,user.getUsername());

        ModelAndView mav = new ModelAndView(PropertiesUtil.getString(FormPageMapping.TAX_PAYMENT_REGISTER_FORM));
        mav.addObject("register",registerInfo);
        mav.addObject("wfname",workflowInfo.getName());
        mav.addObject("taskId",taskId);
        mav.addObject("auditHistory",commentVos);
        return mav;
    }

    @RequestMapping(value = "/taxPayment/processTask/register", method = RequestMethod.POST, headers = "Content-Type=multipart/form-data")
    public String register(MultipartHttpServletRequest request, Authentication authentication, RegisterInfo registerInfo, String comment){
        String taskId = request.getParameter("taskId");
        SecurityUser currentUser = (SecurityUser) authentication.getPrincipal();
        taskService.claim(taskId,currentUser.getUsername());

        List<MultipartFile> multipartFiles = new ArrayList<MultipartFile>();
        Optional.ofNullable(request.getMultiFileMap()).ifPresent(m -> {
            m.values().forEach((t) -> {
                multipartFiles.addAll(t);
            });
        });
        List<AttachmentStore> attachmentStores = uploadService.upload(multipartFiles,this);

        registerInfo.setTaskId(taskId);

        final Map<String,Object> variables = new HashMap<>();
        variables.put("register",registerInfo);

        workflowFacadeService.completeTask(taskId,new TaskPropertyResolverAdapter(){
            @Override
            public String[] comments() {
                return new String[]{comment};
            }

            @Override
            public Map<String, Object> variables() {
                return variables;
            }

            @Override
            public List<AttachmentStore> attachments() {
                return attachmentStores;
            }
        });
        return "redirect:/workflowlist";
    }

    @RequestMapping(value = "/taxPayment/forward/auditForm/{taskId}")
    public ModelAndView forwardAuditForm(@PathVariable String taskId,Authentication authentication){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        List<HistoryCommentVo> commentVos = workflowFacadeService.getProcessInstanceComments(task.getProcessInstanceId());

        RegisterInfo register = workflowFacadeService.queryProcessVariable(task.getExecutionId(),"register");
        ModelAndView mav = new ModelAndView(PropertiesUtil.getString(FormPageMapping.TAX_PAYMENT_APPROVE_FORM));
        mav.addObject("register",register);
        mav.addObject("auditHistory",commentVos);
        mav.addObject("taskId",taskId);
        return mav;

    }

    @RequestMapping(value = "/taxPayment/processTask/audit", method = RequestMethod.POST)
    public String audit(HttpServletRequest request,String result,String comment,Authentication authentication){
        String taskId = request.getParameter("taskId");
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        taskService.claim(taskId,user.getUsername());

        //使用流程变量指定完成任务后下一个连线
        final Map<String,Object> variables = new HashMap<>();
        variables.put("approve",Boolean.valueOf(result));

        workflowFacadeService.completeTask(taskId,new TaskPropertyResolverAdapter(){
            @Override
            public String[] comments() {
                return new String[]{comment};
            }

            @Override
            public Map<String, Object> variables() {
                return variables;
            }

        });

        return "redirect:/workflowlist";
    }


    @Override
    public AttachmentStore saveAttachmentStore(String absolutePath) {
        AttachmentStore attachmentStore = new AttachmentStore();
        attachmentStore.setStoreType(AttachmentEnums.StoreType.Local);
        attachmentStore.setAttachmentName(attachmentName());
        attachmentStore.setPhysicalPath(absolutePath);
        attachmentStore.setAttachmentType(AttachmentEnums.AttachmentType.valueOf(StringUtil.getFilenameExtension(absolutePath).toUpperCase()));
        attachmentStore = attachmentStoreService.save(attachmentStore);
        return attachmentStore;
    }

    @Override
    public String attachmentName() {
        return "test";
    }
}
