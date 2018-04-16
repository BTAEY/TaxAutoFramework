package com.ey.tax.workflow.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * Created by zhuji on 4/16/2018.
 */
public class TaskServiceImpl implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        delegateTask.setAssignee("submitter");
    }
}
