package com.ey.tax.web.utils;

import com.ey.tax.utils.DateUtil;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zhuji on 2/6/2018.
 */
public class TaxDateUtil {
    public static String getMonthByQuarter(Integer year,String quarter){
        StringBuilder time = new StringBuilder("");
        if("Q1".equals(quarter)){
            time.append(year-1).append("-06");
        }else if("Q2".equals(quarter)){
            time.append(year-1).append("-09");
        }else if("Q3".equals(quarter)){
            time.append(year-1).append("-12");
        }else if("Q4".equals(quarter)){
            time.append(year).append("-03");
        }
        return time.toString();
    }

    public static List<String> getPeriodByQuarter(Integer year, String quarter){
        List<String> result = new ArrayList<String>();
        StringBuilder time1 = new StringBuilder("");
        StringBuilder time2 = new StringBuilder("");
        StringBuilder time3 = new StringBuilder("");
        if("Q1".equals(quarter)){
            time1.append(year-1).append("-04");
            time2.append(year-1).append("-05");
            time3.append(year-1).append("-06");
        }else if("Q2".equals(quarter)){
            time1.append(year-1).append("-07");
            time2.append(year-1).append("-08");
            time3.append(year-1).append("-09");
        }else if("Q3".equals(quarter)){
            time1.append(year-1).append("-10");
            time2.append(year-1).append("-11");
            time3.append(year-1).append("-12");
        }else if("Q4".equals(quarter)){
            time1.append(year).append("-01");
            time2.append(year).append("-02");
            time3.append(year).append("-03");
        }
        result.add(time1.toString());
        result.add(time2.toString());
        result.add(time3.toString());
        return result;
    }

    public static Pair<Date,Date> getTBPeriodByYearQuarter(Integer year, String quarter){
        if("Q1".equals(quarter)){
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year-1);
            c.set(Calendar.MONTH, 5);
            Date startDate = DateUtil.getFirstDayOfMonth(c.getTime());
            Date endDate = DateUtil.getEndDayOfMonth(DateUtil.addMonths(c.getTime(),-1));
            return Pair.with(startDate, endDate);
        }
        if("Q2".equals(quarter)){
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year-1);
            c.set(Calendar.MONTH, 8);
            Date startDate = DateUtil.getFirstDayOfMonth(c.getTime());
            Date endDate = DateUtil.getEndDayOfMonth(DateUtil.addMonths(c.getTime(),-1));
            return Pair.with(startDate, endDate);
        }
        if("Q3".equals(quarter)){
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year-1);
            c.set(Calendar.MONTH, 11);
            Date startDate = DateUtil.getFirstDayOfMonth(c.getTime());
            Date endDate = DateUtil.getEndDayOfMonth(DateUtil.addMonths(c.getTime(),-1));
            return Pair.with(startDate, endDate);
        }
        if("Q4".equals(quarter)){
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, 2);
            Date startDate = DateUtil.getFirstDayOfMonth(c.getTime());
            Date endDate = DateUtil.getEndDayOfMonth(DateUtil.addMonths(c.getTime(),-1));
            return Pair.with(startDate, endDate);
        }
        return null;
    }

    public static Pair<Date,Date> getTBDPeriodByYearQuarter(Integer year,String quarter){
        if("Q1".equals(quarter)){
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year-1);
            c.set(Calendar.MONTH, 3);
            Date startDate = DateUtil.getFirstDayOfMonth(c.getTime());
            c.set(Calendar.MONTH, 5);
            Date endDate = DateUtil.getEndDayOfMonth(DateUtil.addMonths(c.getTime(),-1));
            return Pair.with(startDate, endDate);
        }
        if("Q2".equals(quarter)){
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year-1);
            c.set(Calendar.MONTH, 6);
            Date startDate = DateUtil.getFirstDayOfMonth(c.getTime());
            c.set(Calendar.MONTH, 8);
            Date endDate = DateUtil.getEndDayOfMonth(DateUtil.addMonths(c.getTime(),-1));
            return Pair.with(startDate, endDate);
        }
        if("Q3".equals(quarter)){
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year-1);
            c.set(Calendar.MONTH, 9);
            Date startDate = DateUtil.getFirstDayOfMonth(c.getTime());
            c.set(Calendar.MONTH, 11);
            Date endDate = DateUtil.getEndDayOfMonth(DateUtil.addMonths(c.getTime(),-1));
            return Pair.with(startDate, endDate);
        }
        if("Q4".equals(quarter)){
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, 0);
            Date startDate = DateUtil.getFirstDayOfMonth(c.getTime());
            c.set(Calendar.MONTH, 2);
            Date endDate = DateUtil.getEndDayOfMonth(DateUtil.addMonths(c.getTime(),-1));
            return Pair.with(startDate, endDate);
        }
        return null;
    }
}
