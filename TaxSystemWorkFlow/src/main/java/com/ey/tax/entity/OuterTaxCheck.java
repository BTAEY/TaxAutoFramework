package com.ey.tax.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by zhuji on 5/8/2018.
 */
@Entity
@Table(name = "t_tax_outer_check")
public class OuterTaxCheck extends Auditable {
    @Column(name="outertaxcheck_name")
    private String outertaxcheckname;

    @Column(name="reportfrequency")
    private String reportfrequency;

    @Column(name="checkstart_time")
    private Timestamp checkstarttime;

    @Column(name="checkend_time")
    private Timestamp checkendtime;

    @Column(name="description")
    private String description;

    @Column(name="workflow_key")
    private String workflowKey;

    public String getOutertaxcheckname() {
        return outertaxcheckname;
    }

    public void setOutertaxcheckname(String outertaxcheckname) {
        this.outertaxcheckname = outertaxcheckname;
    }

    public String getReportfrequency() {
        return reportfrequency;
    }

    public void setReportfrequency(String reportfrequency) {
        this.reportfrequency = reportfrequency;
    }

    public Timestamp getCheckstarttime() {
        return checkstarttime;
    }

    public void setCheckstarttime(Timestamp checkstarttime) {
        this.checkstarttime = checkstarttime;
    }

    public Timestamp getCheckendtime() {
        return checkendtime;
    }

    public void setCheckendtime(Timestamp checkendtime) {
        this.checkendtime = checkendtime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWorkflowKey() {
        return workflowKey;
    }

    public void setWorkflowKey(String workflowKey) {
        this.workflowKey = workflowKey;
    }
}
