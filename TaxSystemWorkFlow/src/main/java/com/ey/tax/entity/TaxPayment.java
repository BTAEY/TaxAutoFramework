package com.ey.tax.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by zhuji on 4/9/2018.
 */
@Entity
@Table(name = "t_tax_payment")
public class TaxPayment extends BaseEntity{

    @Column(name="applier")
    private String applier;

    @Temporal(TemporalType.DATE)
    @Column(name="apply_date")
    private Date applyDate;

    @Column(name="applier_station")
    private String applierStation;

    @Column(name="applier_organize")
    private String applierOrganize;

    @Temporal(TemporalType.DATE)
    @Column(name="report_date")
    private Date reportDate;

    @Column(name="payer_id")
    private String payerId;

    @Column(name="payer_name")
    private String payerName;

    @Column(name="process_id")
    private String processId;

    @Column(name="attachment_ids")
    private String attachmentIds;

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

    public String getApplierStation() {
        return applierStation;
    }

    public void setApplierStation(String applierStation) {
        this.applierStation = applierStation;
    }

    public String getApplierOrganize() {
        return applierOrganize;
    }

    public void setApplierOrganize(String applierOrganize) {
        this.applierOrganize = applierOrganize;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getAttachmentIds() {
        return attachmentIds;
    }

    public void setAttachmentIds(String attachmentIds) {
        this.attachmentIds = attachmentIds;
    }
}
