package com.ey.tax.web.controller.workflow;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HtmlUtil;
import com.ey.tax.core.service.impl.TaxPaymentServiceImpl;
import com.ey.tax.security.SecurityUser;
import com.ey.tax.service.WorkflowService;
import com.ey.tax.utils.PropertiesUtil;
import com.ey.tax.vo.ActTaskVo;
import com.ey.tax.vo.WorkflowInfo;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Created by zhuji on 4/9/2018.
 */
@Controller
public class WorkFlowController {
    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private RepositoryService repositoryService;

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
            return taskVo;
        }).collect(Collectors.toList());

        ModelAndView mav = new ModelAndView("/workflow/pendingTaskList");
        mav.addObject("pendingTasks",taskVoList);
        return mav;
    }
}
