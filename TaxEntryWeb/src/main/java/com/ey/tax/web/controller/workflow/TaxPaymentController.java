package com.ey.tax.web.controller.workflow;

import cn.hutool.core.util.StrUtil;
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
import com.ey.tax.utils.FileNameFormatUtil;
import com.ey.tax.utils.StringUtil;
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
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

    @RequestMapping(method = RequestMethod.POST, value = "/startTaxPayment_workflow",headers = "Content-Type=multipart/form-data")
    String upload(MultipartHttpServletRequest request,Authentication authentication){
        SecurityUser currentUser = (SecurityUser) authentication.getPrincipal();

        List<MultipartFile> multipartFiles = new ArrayList<MultipartFile>();
        Optional.ofNullable(request.getMultiFileMap()).ifPresent(m -> {
            m.values().forEach((t) -> {
                multipartFiles.addAll(t);
            });
        });

        uploadService.upload(multipartFiles,this);

        String processName = request.getParameter("wfname");

        TaxPayment taxPayment = new TaxPayment();
        taxPayment.setApplier(currentUser.getUsername());
        taxPayment.setApplyDate(DateUtil.getNowTimestamp());
        taxPayment = taxPaymentService.save(taxPayment);

        Map<String,Object> variables = new HashMap<>();
        variables.put("taxPaymentId",taxPayment.getId());
        workflowService.startProcess(processName,variables);

        return "redirect:/workflowlist";
    }


    @RequestMapping(method = RequestMethod.GET, value = "/taxPayment/step_1/{defineId}")
    ModelAndView step1(@PathVariable String defineId, Authentication authentication){
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        ModelAndView mav = new ModelAndView();
        mav.setViewName("workflow/taxPayment_1");
        mav.addObject("applier",user.getUsername());
        mav.addObject("wfname",workflowService.getProcessName(defineId));
        return mav;
    }

    @RequestMapping(value = "/taxPayment/processTask/{procId}", method = RequestMethod.GET)
    public ModelAndView processTask(@PathVariable String procId){
        TaxPayment taxPayment = taxPaymentService.findTaxPaymentByProcessId(procId);
        ModelAndView mav = new ModelAndView("workflow/taxPayment_2");
        mav.addObject("taxPayment",taxPayment);
        return mav;
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
