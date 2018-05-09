package com.ey.tax.service;

import com.ey.tax.bs.ReportFrequency;
import com.ey.tax.service.strategy.ReportFrequencyStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhuji on 5/8/2018.
 */
@Component
public class ReportFrequencyHelper {
    @Autowired
    private List<ReportFrequencyStrategy> reportFrequencyStrategies;

    public List<Date> getSpecifyDate(Date startDate, Date endDate, ReportFrequency reportFrequency){
        List<Date> dateList = new ArrayList<>();
        for(ReportFrequencyStrategy reportFrequencyStrategy:reportFrequencyStrategies){
            if(reportFrequencyStrategy.support(reportFrequency)){
                dateList = reportFrequencyStrategy.getSpecifyDate(startDate,endDate);
                break;
            }
        }
        return dateList;
    }
}
