package com.ey.tax.web.controller.workflow;

import com.ey.tax.bs.ReportFrequency;
import com.ey.tax.core.service.IOuterTaxCheckReporterService;
import com.ey.tax.core.service.IOuterTaxCheckService;
import com.ey.tax.entity.OuterTaxCheck;
import com.ey.tax.entity.OuterTaxCheckReporter;
import com.ey.tax.security.SecurityUser;
import com.ey.tax.service.ReportFrequencyHelper;
import com.ey.tax.service.TaskPropertyResolver;
import com.ey.tax.service.TaskPropertyResolverAdapter;
import com.ey.tax.service.WorkflowFacadeService;
import com.ey.tax.utils.DateUtil;
import com.ey.tax.utils.PropertiesUtil;
import com.ey.tax.utils.converter.SqlTimestampPropertyEditor;
import com.ey.tax.vo.WorkflowInfo;
import com.ey.tax.workflow.FormPageMapping;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by zhuji on 5/8/2018.
 */
@Controller
public class OuterTaxCheckController {
    @Autowired
    private IOuterTaxCheckService outerTaxCheckService;

    @Autowired
    private WorkflowFacadeService workflowFacadeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @InitBinder("outerTaxCheck")
    public void customizeBinding (WebDataBinder binder) {
        binder.registerCustomEditor(Timestamp.class, "checkstarttime",
                new SqlTimestampPropertyEditor());
        binder.registerCustomEditor(Timestamp.class, "checkendtime",
                new SqlTimestampPropertyEditor());
    }

    @GetMapping(value="/workflow/outertaxcheck/listpage")
    public ModelAndView listpage(Authentication authentication){
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        List<OuterTaxCheck> outerTaxCheckList = outerTaxCheckService.findByUser(user.getUsername());
        List<WorkflowInfo> workflowInfoList = workflowFacadeService.findAllDefinedWorkflowList();
        ModelAndView mav = new ModelAndView(PropertiesUtil.getString(FormPageMapping.OUTER_TAX_CHECK_LISTPATE));
        mav.addObject("resultset",outerTaxCheckList);
        mav.addObject("workflows",workflowInfoList);
        return mav;
    }

    @PostMapping(value = "/workflow/outertaxcheck/submit")
    public String submit(@ModelAttribute("outerTaxCheck") OuterTaxCheck outerTaxCheck, @RequestParam String workflowkey){
        //保存数据
        outerTaxCheck.setWorkflowKey(workflowkey);
        outerTaxCheck = outerTaxCheckService.saveOrUpdate(outerTaxCheck);
        //启动流程
        runtimeService.startProcessInstanceByKey(workflowkey,outerTaxCheck.getId().toString());
        return "redirect:/workflow/outertaxcheck/listpage";
    }

    @GetMapping(value = "/workflow/outertaxcheck/step1/{taskId}")
    public ModelAndView step1(@PathVariable String taskId){
        ModelAndView mav = new ModelAndView("/workflow/outertaxcheck/step1");
        Task task = workflowFacadeService.findTask(taskId);
        ProcessInstance pi = workflowFacadeService.findProcessInstance(task.getProcessInstanceId());
        String bk = pi.getBusinessKey();
        OuterTaxCheck outerTaxCheck = outerTaxCheckService.findById(Long.valueOf(bk));
        List<WorkflowInfo> workflowInfoList = workflowFacadeService.findAllDefinedWorkflowList();
        mav.addObject("outertaxcheck",outerTaxCheck);
        mav.addObject("workflows",workflowInfoList);
        mav.addObject("workflowkey",pi.getProcessDefinitionKey());
        mav.addObject("taskId",taskId);
        return mav;
    }

    @GetMapping(value = "/workflow/outertaxcheck/step2/{taskId}")
    public ModelAndView step2(@PathVariable String taskId){
        ModelAndView mav = new ModelAndView("/workflow/outertaxcheck/step2");
        mav.addObject("taskId",taskId);
        return mav;
    }

    @PostMapping(value = "/workflow/outertaxcheck/audit")
    public String audit(String taskId,Authentication authentication){
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        workflowFacadeService.completeTask(taskId,new TaskPropertyResolverAdapter());
        return "redirect:/workflowlist";
    }

}
