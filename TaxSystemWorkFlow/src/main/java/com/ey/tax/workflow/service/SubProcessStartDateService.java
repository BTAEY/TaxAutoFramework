package com.ey.tax.workflow.service;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
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
public class SubProcessStartDateService implements ExecutionListener {
    @Autowired
    private RuntimeService runtimeService;

    @Override
    public void notify(DelegateExecution execution) {
        List<Date> activeList = (List<Date>) execution.getVariable("activeDateList");

        Integer index = (Integer) execution.getVariable("loopCounter");

        if(CollectionUtils.isNotEmpty(activeList)){
            execution.setVariable("startDate",activeList.get(index));
        }

    }
}
