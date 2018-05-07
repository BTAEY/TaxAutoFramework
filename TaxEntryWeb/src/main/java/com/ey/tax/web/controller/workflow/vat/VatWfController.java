package com.ey.tax.web.controller.workflow.vat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zhuji on 4/24/2018.
 */
@Controller
public class VatWfController {

    @GetMapping("/vat/forward/submitForm/{taskId}")
    public ModelAndView forwardSubmitForm(@PathVariable String taskId){
        ModelAndView mav = new ModelAndView("workflow/vat/submitForm");
        return mav;
    }

    @PostMapping("/vat/process/submit")
    public String submit(MultipartHttpServletRequest request){
        return "redirect:/workflowlist";
    }
}
