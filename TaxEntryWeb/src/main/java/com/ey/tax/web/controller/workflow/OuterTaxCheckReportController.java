package com.ey.tax.web.controller.workflow;

import com.ey.tax.core.service.IOuterTaxCheckReporterService;
import com.ey.tax.core.service.impl.OuterTaxCheckReporterServiceImpl;
import com.ey.tax.security.SecurityUser;
import com.ey.tax.utils.PropertiesUtil;
import com.ey.tax.workflow.FormPageMapping;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zhuji on 5/8/2018.
 */
@Controller
public class OuterTaxCheckReportController {
    @Autowired
    private IOuterTaxCheckReporterService outerTaxCheckReporterService;

    @Autowired
    private TaskService taskService;

    @GetMapping(value="/workflow/outertaxcheckreport/listpage")
    public ModelAndView listpage(Authentication authentication){
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        ModelAndView mav = new ModelAndView(PropertiesUtil.getString(FormPageMapping.OUTER_TAX_CHECK_REPORT_LISTPATE));
        return mav;
    }

    @GetMapping(value = "/workflow/outertaxcheckreport/step1/{taskId}")
    public ModelAndView step1(@PathVariable String taskId){
        ModelAndView mav = new ModelAndView("/workflow/outertaxcheckreport/step1");
        mav.addObject("taskId",taskId);
        return mav;
    }

    @GetMapping(value = "/workflow/outertaxcheckreport/step2/{taskId}")
    public ModelAndView step2(@PathVariable String taskId){
        ModelAndView mav = new ModelAndView("/workflow/outertaxcheckreport/step2");
        mav.addObject("taskId",taskId);
        return mav;
    }

    @PostMapping(value = "/workflow/outertaxcheckreport/audit")
    public String audit(String taskId){
        taskService.complete(taskId);
        return "redirect:/workflowlist/outertaxcheck/listpage";
    }

}
