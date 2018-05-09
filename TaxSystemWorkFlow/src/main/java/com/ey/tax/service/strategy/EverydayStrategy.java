package com.ey.tax.service.strategy;

import com.ey.tax.bs.ReportFrequency;
import com.ey.tax.utils.DateUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhuji on 5/8/2018.
 */
@Component
public class EverydayStrategy implements ReportFrequencyStrategy {
    @Override
    public boolean support(ReportFrequency reportFrequency) {
        return ReportFrequency.Everyday.equals(reportFrequency);
    }

    @Override
    public List<Date> getSpecifyDate(Date startDate, Date endDate) {
        List<Date> result = new ArrayList<>();
        if(DateUtil.isSameDay(startDate,endDate)){
            result.add(DateUtil.beginOfDay(startDate));
            return result;
        }
        long daycount = DateUtil.betweenDay(startDate,endDate);
        int i = Long.valueOf(daycount).intValue()+1;
        while(i >= 0){
            result.add(startDate);
            startDate = DateUtil.tomorrow(startDate);
            i--;
        }
        return result;
    }
}
