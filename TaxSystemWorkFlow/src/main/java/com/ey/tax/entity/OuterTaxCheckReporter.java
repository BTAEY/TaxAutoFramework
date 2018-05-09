package com.ey.tax.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by zhuji on 5/8/2018.
 */
@Entity
@Table(name = "t_tax_outer_check_reporter")
public class OuterTaxCheckReporter extends BaseEntity {
    @Column(name = "reporter_name")
    private String reporterName;

    @Column(name = "pid")
    private Long outerTaxCheckId;

    @Column(name = "active_date")
    private Date activeDate;

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public Long getOuterTaxCheckId() {
        return outerTaxCheckId;
    }

    public void setOuterTaxCheckId(Long outerTaxCheckId) {
        this.outerTaxCheckId = outerTaxCheckId;
    }

    public Date getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(Date activeDate) {
        this.activeDate = activeDate;
    }
}
