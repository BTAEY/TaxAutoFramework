package com.ey.tax.vo;

import java.util.List;

/**
 * Created by zhuji on 4/13/2018.
 */
public class AuditHistoryVo {
    private String taskName;

    private String action;

    private String comment;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
