package com.ey.tax.bs;

import java.util.EnumSet;

/**
 * Created by zhuji on 5/8/2018.
 */
public enum ReportFrequency {
    FirstDayOfMonth("FirstDayOfMonth"),
    FirstDayOfWeek("FirstDayOfWeek"),
    FirstDayOfDoubleWeek("FirstDayofDoubleWeek"),
    Everyday("Everyday");

    private String code;

    ReportFrequency(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static ReportFrequency getFrequency(String code){
        EnumSet<ReportFrequency> enumSet =EnumSet.allOf(ReportFrequency.class);
        for(ReportFrequency reportFrequency: enumSet){
            if(reportFrequency.getCode().equalsIgnoreCase(code)){
                return reportFrequency;
            }
        }
        throw new IllegalArgumentException("系统找不到对应的报告频率");
    }
}
