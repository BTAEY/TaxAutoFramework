package com.ey.tax.vo.taxpayment;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhuji on 4/13/2018.
 */
public class SubmitterInfo implements Serializable{

    private static final long serialVersionUID = 3112733446461569107L;

    private String applier;

    private Date applyDate;

    private String station;

    private String organization;

    private String taskId;

    public String getApplier() {
        return applier;
    }

    public void setApplier(String applier) {
        this.applier = applier;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
