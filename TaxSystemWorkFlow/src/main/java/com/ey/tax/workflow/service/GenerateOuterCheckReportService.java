package com.ey.tax.workflow.service;

import com.ey.tax.bs.ReportFrequency;
import com.ey.tax.core.service.IOuterTaxCheckReporterService;
import com.ey.tax.core.service.IOuterTaxCheckService;
import com.ey.tax.entity.OuterTaxCheck;
import com.ey.tax.entity.OuterTaxCheckReporter;
import com.ey.tax.service.ReportFrequencyHelper;
import com.ey.tax.utils.ApplicationContextCoreProvider;
import com.ey.tax.utils.DateUtil;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by zhuji on 5/8/2018.
 */
@Component
public class GenerateOuterCheckReportService implements JavaDelegate {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ReportFrequencyHelper reportFrequencyHelper;

    @Autowired
    private IOuterTaxCheckReporterService outerTaxCheckReporterService;

    @Autowired
    private IOuterTaxCheckService outerTaxCheckService;

    public GenerateOuterCheckReportService(){
        runtimeService = ApplicationContextCoreProvider.getBean(RuntimeService.class);
        reportFrequencyHelper = ApplicationContextCoreProvider.getBean(ReportFrequencyHelper.class);
        outerTaxCheckReporterService = ApplicationContextCoreProvider.getBean(IOuterTaxCheckReporterService.class);
        outerTaxCheckService = ApplicationContextCoreProvider.getBean(IOuterTaxCheckService.class);
    }

    @Override
    public void execute(DelegateExecution execution) {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(execution.getProcessInstanceId()).singleResult();
        String bk = pi.getBusinessKey();
        OuterTaxCheck outerTaxCheck = outerTaxCheckService.findById(Long.valueOf(bk));

        //生成子流程业务数据
//        ReportFrequency reportFrequency = ReportFrequency.getFrequency(outerTaxCheck.getReportfrequency());
        ReportFrequency reportFrequency = ReportFrequency.Everyday;
        List<Date> activeDateList = reportFrequencyHelper.getSpecifyDate(outerTaxCheck.getCheckstarttime(),
                outerTaxCheck.getCheckendtime(),
                reportFrequency);
        if(CollectionUtils.isNotEmpty(activeDateList)){
            int index = 0;
            for(Date activeDate:activeDateList){
                OuterTaxCheckReporter outerTaxCheckReport = new OuterTaxCheckReporter();
                outerTaxCheckReport.setOuterTaxCheckId(outerTaxCheck.getId());
                outerTaxCheckReport.setActiveDate(DateUtil.dateToTimestamp(activeDate));
                outerTaxCheckReport.setReporterName("外部税务检查流程-第"+(++index)+"期汇报");
                outerTaxCheckReporterService.saveOrUpdate(outerTaxCheckReport);
            }
        }
        runtimeService.setVariable(execution.getId(),"activeDateList",activeDateList);
        runtimeService.setVariable(execution.getId(),"loopNum",activeDateList.size());
    }
}
