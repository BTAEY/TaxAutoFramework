package com.ey.tax.service.strategy;

import com.ey.tax.bs.ReportFrequency;

import java.util.Date;
import java.util.List;

/**
 * Created by zhuji on 5/8/2018.
 */
public interface ReportFrequencyStrategy {
    boolean support(ReportFrequency reportFrequency);

    List<Date> getSpecifyDate(Date startDate, Date endDate);
}
