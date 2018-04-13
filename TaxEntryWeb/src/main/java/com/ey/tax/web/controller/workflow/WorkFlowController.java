package com.ey.tax.web.controller.workflow;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HtmlUtil;
import com.ey.tax.security.SecurityUser;
import com.ey.tax.service.WorkflowService;
import com.ey.tax.utils.PropertiesUtil;
import com.ey.tax.vo.ActTaskVo;
import com.ey.tax.vo.WorkflowInfo;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by zhuji on 4/9/2018.
 */
@Controller
public class WorkFlowController {
    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private FormService formService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @RequestMapping(value = "/workflow/deploy/{wfKey}")
    @ResponseBody
    public String deploy(@PathVariable String wfKey){
        String resourcePath = PropertiesUtil.getString(wfKey);
        repositoryService.createDeployment().addClasspathResource(resourcePath).deploy();
        return "流程【"+wfKey+"】部署成功";
    }

    @RequestMapping(value="/workflow/viewImage/{deploymentId}")
    @ResponseBody
    public String viewImage(@PathVariable String deploymentId){
        Optional<String> imageNameOptional = repositoryService.getDeploymentResourceNames(deploymentId).stream().filter(s -> s.indexOf(".png") >= 0).findFirst();
        InputStream in = repositoryService.getResourceAsStream(deploymentId,imageNameOptional.get());

//        ProcessInstance pi = runtimeService.createProcessInstanceQuery().deploymentId(deploymentId).singleResult();
//        BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
//        List<String> activeIds = runtimeService.getActiveActivityIds(pi.getId());
//        ProcessDiagramGenerator p = new DefaultProcessDiagramGenerator();
//        InputStream is = p.generateDiagram(bpmnModel,"png",activeIds, Collections.emptyList(),"","宋体","宋体",null,1.0);

        byte[] bytes = IoUtil.readBytes(in);
        String valueStr = Base64.encode(bytes, Charset.forName("UTF-8"));
        return valueStr;
    }

    @RequestMapping(value="/workflow/viewResource/{deploymentId}")
    @ResponseBody
    public String viewResource(@PathVariable String deploymentId){
        Optional<String> bpmnNameOptional = repositoryService.getDeploymentResourceNames(deploymentId).stream().filter(s -> s.indexOf(".xml") >= 0).findFirst();
        InputStream in = repositoryService.getResourceAsStream(deploymentId,bpmnNameOptional.get());
        String content = HtmlUtil.escape(IoUtil.read(in,Charset.forName("UTF-8")));
        return content;
    }

    @RequestMapping(value="/workflow/start/{wfKey}")
    @ResponseBody
    public String startWf(@PathVariable String wfKey){
        ProcessDefinition processDefinition =  repositoryService.createProcessDefinitionQuery().processDefinitionKey(wfKey).singleResult();
        try {
            workflowService.startProcess(wfKey);
            return "流程【"+processDefinition.getName()+"】启动成功";
        } catch (Exception e) {
            logger.error("流程【"+processDefinition.getName()+"】启动失败",e);
            return "流程【"+processDefinition.getName()+"】启动失败";
        }
    }

    @RequestMapping(value="/workflowlist" , method = RequestMethod.GET)
    public ModelAndView workflow(){
        List<WorkflowInfo> workflowInfoList = workflowService.findAllDefinedWorkflowList();
        ModelAndView mav = new ModelAndView("workflow/workflow_list");
        mav.addObject("workflows",workflowInfoList);
        return mav;
    }

    @RequestMapping(value = "/workflow/pending" , method = RequestMethod.GET)
    public ModelAndView pendingTask(Authentication authentication){
        SecurityUser currentUser = (SecurityUser) authentication.getPrincipal();
        List<Task> taskList = workflowService.findTaskByUserId(currentUser.getUsername());
        List<ActTaskVo> taskVoList = taskList.stream().map(t -> {
           ActTaskVo taskVo = new ActTaskVo();
            taskVo.setProcId(t.getProcessInstanceId());
            taskVo.setTaskId(t.getId());
            taskVo.setTaskName(t.getName());
            taskVo.setAssignee(t.getAssignee());
            taskVo.setCreateDate(t.getCreateTime());
            TaskFormData taskFormData = formService.getTaskFormData(t.getId());
            String url = taskFormData.getFormKey()+"/"+t.getId();
            taskVo.setRedirectUrl(url);
            return taskVo;
        }).collect(Collectors.toList());

        ModelAndView mav = new ModelAndView("/workflow/pendingTaskList");
        mav.addObject("pendingTasks",taskVoList);
        return mav;
    }

    @RequestMapping(value = "/workflow/completed" , method = RequestMethod.GET)
    public ModelAndView completedWorkflows(){
        List<HistoricProcessInstance> processInstances = historyService.createHistoricProcessInstanceQuery()
                .finished().orderByProcessInstanceId().asc().list();

        List<WorkflowInfo> workflowInfoList = processInstances.stream().map(i -> {
            WorkflowInfo workflowInfo = new WorkflowInfo();
            workflowInfo.setName(i.getName());
            workflowInfo.setDescription(i.getDescription());
            workflowInfo.setDuration(i.getDurationInMillis());
            workflowInfo.setStartTime(i.getStartTime());
            workflowInfo.setEndTime(i.getEndTime());
            return workflowInfo;
        }).collect(Collectors.toList());
        ModelAndView mav = new ModelAndView("workflow/completed_workflow_list");
        mav.addObject("workflows",workflowInfoList);
        return mav;
    }
}
