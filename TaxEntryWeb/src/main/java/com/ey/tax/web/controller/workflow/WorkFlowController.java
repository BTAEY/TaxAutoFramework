package com.ey.tax.web.controller.workflow;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;
import com.ey.tax.security.SecurityUser;
import com.ey.tax.service.WorkflowFacadeService;
import com.ey.tax.vo.ActTaskVo;
import com.ey.tax.vo.WorkflowInfo;
import com.ey.tax.web.core.ResponseData;
import org.activiti.engine.FormService;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhuji on 4/9/2018.
 */
@Controller
public class WorkFlowController {
    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private WorkflowFacadeService workflowFacadeService;

    @Autowired
    private FormService formService;

    @RequestMapping(value = "/workflow/deploy/{wfKey}")
    @ResponseBody
    public String deploy(@PathVariable String wfKey){
        try {
            workflowFacadeService.deploy(wfKey);
            ResponseData responseData = new ResponseData(ResponseData.Status.SUCCESS);
            responseData.setMessage("流程【"+wfKey+"】部署成功");
            return JSONUtil.toJsonStr(responseData);
        } catch (Exception e) {
            logger.error("流程部署失败",e);
            ResponseData responseData = new ResponseData(ResponseData.Status.FAILED);
            responseData.setMessage("流程【"+wfKey+"】部署失败");
            return JSONUtil.toJsonStr(responseData);
        }
    }

    @RequestMapping(value="/workflow/viewImage/{deploymentId}")
    @ResponseBody
    public String viewImage(@PathVariable String deploymentId){
        InputStream in = workflowFacadeService.getWorkFLowImage(deploymentId);
        byte[] bytes = IoUtil.readBytes(in);
        String valueStr = Base64.encode(bytes, Charset.forName("UTF-8"));
        return valueStr;
    }

    @RequestMapping(value = "/workflow/viewImageAttachment/{attachmentId}")
    @ResponseBody
    public String viewAttachment(@PathVariable String attachmentId){
        Attachment attachment = workflowFacadeService.getAttachmentById(attachmentId);
        InputStream in = FileUtil.getInputStream(attachment.getUrl());
        byte[] bytes = IoUtil.readBytes(in);
        String valueStr = Base64.encode(bytes, Charset.forName("UTF-8"));
        return valueStr;
    }

    @RequestMapping(value="/workflow/viewResource/{deploymentId}")
    @ResponseBody
    public String viewResource(@PathVariable String deploymentId){
        String content = workflowFacadeService.getWorkFlowResource(deploymentId);
        return content;
    }

    @RequestMapping(value="/workflow/start/{wfKey}")
    @ResponseBody
    public String startWf(@PathVariable String wfKey){
        WorkflowInfo workflowInfo = workflowFacadeService.getWorkFlowInfoByKey(wfKey);
        try {
            workflowFacadeService.startProcess(wfKey);
            ResponseData responseData = new ResponseData(ResponseData.Status.SUCCESS);
            responseData.setMessage("流程【"+workflowInfo.getName()+"】启动成功");
            return JSONUtil.toJsonStr(responseData);
        } catch (Exception e) {
            logger.error("流程【"+workflowInfo.getName()+"】启动失败",e);
            ResponseData responseData = new ResponseData(ResponseData.Status.FAILED);
            responseData.setMessage("流程【"+workflowInfo.getName()+"】启动失败");
            return JSONUtil.toJsonStr(responseData);
        }
    }

    @RequestMapping(value="/workflowlist" , method = RequestMethod.GET)
    public ModelAndView workflow(){
        List<WorkflowInfo> workflowInfoList = workflowFacadeService.findAllDefinedWorkflowList();
        ModelAndView mav = new ModelAndView("workflow/workflow_list");
        mav.addObject("workflows",workflowInfoList);
        return mav;
    }

    @RequestMapping(value = "/workflow/pending" , method = RequestMethod.GET)
    public ModelAndView pendingTask(Authentication authentication){
        SecurityUser currentUser = (SecurityUser) authentication.getPrincipal();
        List<Task> taskList = workflowFacadeService.findTaskByUserId(currentUser.getUsername());
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
    public ModelAndView completedWorkFlows(){
        List<WorkflowInfo> workflowInfoList = workflowFacadeService.getHistoricWorkFlows(true);
        ModelAndView mav = new ModelAndView("workflow/completed_workflow_list");
        mav.addObject("workflows",workflowInfoList);
        return mav;
    }

    /**
     * 查询未完成的工作流
     * @return
     */
    @RequestMapping(value = "/workflow/undone", method = RequestMethod.GET)
    public ModelAndView undoneWorkFlows(){
        List<WorkflowInfo> workflowInfoList = workflowFacadeService.getHistoricWorkFlows(false);
        ModelAndView mav = new ModelAndView("workflow/undone_workflow_list");
        mav.addObject("workflows",workflowInfoList);
        return mav;
    }

    /**
     * 查看任务办理进度
     * @return
     */
    @RequestMapping(value="/workflow/viewProgress",method = RequestMethod.GET)
    public String viewProgress(ModelMap map){
        return "";
    }

    @RequestMapping(value="/workflow/delProc",method = RequestMethod.POST)
    @ResponseBody
    public String deleteProcessInstance(HttpServletRequest request){
        String procInstId = request.getParameter("procInstId");
        String reason = request.getParameter("reason");
        workflowFacadeService.delProcessInstance(procInstId,reason);
        return "/workflow/undone";
    }

    /**
     * 获取流程图像，已执行节点和流程线高亮显示
     */
    @RequestMapping(value="/workflow/viewProgressImage/{procInstId}")
    @ResponseBody
    public String viewProgressImage(@PathVariable String procInstId){
        InputStream in = workflowFacadeService.getWorkFlowProgressImage(procInstId);
        byte[] bytes = IoUtil.readBytes(in);
        String valueStr = Base64.encode(bytes, Charset.forName("UTF-8"));
        return valueStr;
    }
}
