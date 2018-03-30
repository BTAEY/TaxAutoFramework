package com.ey.tax.utils;


import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.javatuples.Pair;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhuji on 2/6/2018.
 */
public final class DateUtil extends DateUtils {
    /**
     *
     * @comment:  转时间戳
     * @createDate:2017-11-08
     * @param date YYYY-MM-dd 时间类型
     */
    public static Timestamp stringToTimestamp(String date) {
        if(date != null){
            String tsStr = date+" 00:00:00";
            Timestamp ts = Timestamp.valueOf(tsStr);  // 2011-05-09 11:49:45.0
            return ts;
        }else{
            return null;
        }
    }

    /**
     * 得到某年某月的第一天
     * @createDate:2017-11-08
     * @param year
     * @param month
     * @return
     */
    public static Date getFirstDayOfMonth(int year, int month) {

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, year);

        cal.set(Calendar.MONTH, month - 1);

        cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));

        return cal.getTime();
    }

    /**
     * @comment:获取某年某月的最后一天
     * @createDate:2017-11-08
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDayOfMonth(int year, int month) {

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, year);

        cal.set(Calendar.MONTH, month - 1);

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        return cal.getTime();
    }

    /**
     * @comment:根据年 月 获取对应的月份 天数
     * @createDate:2017-11-08
     * */
    public static int getDaysByYearMonth(int year, int month) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, 1);
        cal.roll(Calendar.DATE, -1);
        return cal.get(Calendar.DATE);
    }
    /**
     * @comment:查询当前月份
     * @return
     */
    public static int getCurrMonth(){
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH)+1;
    }
    /**
     * @comment:返回给定固定格式的当前时间
     * @param formatStr YYYY-MM-dd
     * @return
     */
    public static String getCurrDatetime(String formatStr){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format =  new SimpleDateFormat(formatStr);
        return format.format(cal.getTime());
    }
    /**
     * @comment:获取当前时间
     * @return
     */
    public static Timestamp getNowTimestamp(){
        return new Timestamp(Calendar.getInstance().getTimeInMillis());
    }
    /**
     * @comment 格式化时间 date To String
     * @param date 时间
     * @param formatType 形式（eg:yyyy-MM-dd HH:mm:ss ）
     * @return
     */
    public static String dateFormatToString(Date date,String formatType){
        DateFormat format = new SimpleDateFormat(formatType);
        return format.format(date);
    }
    /**
     * @comment string转date
     * @param date 时间
     * @param formatType 形式（eg:yyyy-MM-dd HH:mm:ss ）
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String date,String formatType) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat(formatType);
        Date res = sdf.parse(date);
        return res;
    }
    /**
     * @comment string 转 sqlDate
     * @param strDate 时间
     * @param formatType 形式（eg:yyyy-MM-dd HH:mm:ss ）
     * @return
     */
    public static java.sql.Date stringToSqlDate(String strDate,String formatType) {
        SimpleDateFormat format = new SimpleDateFormat(formatType);
        try {
            java.util.Date d = stringToDate(strDate, formatType);
            d = format.parse(strDate);
            java.sql.Date date = new java.sql.Date(d.getTime());
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * @comment 时间向后推 几个月
     * @param date 转换时间
     * @param month 推后月份
     * @history 2017-12-06
     * @return 转换结果
     */
    public static Date getMonthLater(Date date,int month){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }
    /**
     * @comment timestamp to date
     * @param time 转换时间
     * @history 2017-12-06
     * @return
     */
    public static Date timeStampToDate(Timestamp time){
        return new Date(time.getTime());
    }
    /**
     * @comment date to timestamp
     * @param date 转换时间
     * @history 2017-12-06
     * @return
     */
    public static Timestamp dateToTimestamp(Date date){
        return new Timestamp(date.getTime());
    }

    public static Date getTimeOfDay(Date date,int hour,int min,int sec,int millisec){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, min);
        c.set(Calendar.SECOND, sec);
        c.set(Calendar.MILLISECOND, millisec);
        return c.getTime();
    }

    public static Date getFirstDayOfMonth(Date date) {
        return addMonths(ceiling(date,Calendar.MONTH), -1);
    }

    public static Date getEndDayOfMonth(Date date){
        return addDays(addMonths(ceiling(date,Calendar.MONTH), 1),-1);
    }

    public static Date getEndTimeOfDay(Date date) {
        return getTimeOfDay(date,23,59,59,0);
    }

    public static Pair<Date,Date> getFinancialYearPeriod(Integer year){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        Date startDate = getFirstDayOfMonth(addMonths(c.getTime(), -9));
        Date endDate = getEndDayOfMonth(addMonths(c.getTime(),1));
        return Pair.with(startDate, endDate);
    }


    enum DateFormats{
        CHINESE_FORMAT("yyyy-MM-dd hh:mm:ss"),
        SHORT_CHINESE_FORMAT("yyyy-MM-dd"),
        US_FORMAT("MM/dd/yyyy hh:mm:ss"),
        SHORT_US_FORMAT("MM/dd/yyyy");

        String format;

        DateFormats(String format){
            this.format = format;
        }

        public String getFormatString(){
            return format;
        }
    }

    public static boolean isDateFormat(String dateStr){
        String[] parsePatterns = FluentIterable.from(EnumUtils.getEnumList(DateFormats.class)).transform(new Function<DateFormats, String>() {
            @Override
            public String apply(DateFormats input) {
                return input.getFormatString();
            }
        }).toArray(String.class);
        return parseFormat(dateStr,parsePatterns);

    }

    private static boolean parseFormat(String str,String[] parsePatterns){
        boolean matched = false;
        if(str == null || parsePatterns == null){
            throw new IllegalArgumentException("Date and Patterns must not be null");
        }
        SimpleDateFormat parser = new SimpleDateFormat();
        for (final String parsePattern : parsePatterns) {
            parser.applyPattern(parsePattern);
            parser.setLenient(false);
            try {
                parser.parse(str);
                matched = true;
                break;
            } catch (ParseException e) {
                continue;
            }
        }
        return matched;
    }

    public static Date parseDate(String dateStr) throws Exception{
        String[] parsePatterns = FluentIterable.from(EnumUtils.getEnumList(DateFormats.class)).transform(new Function<DateFormats, String>() {
            @Override
            public String apply(DateFormats input) {
                return input.getFormatString();
            }
        }).toArray(String.class);
        return parseDate(dateStr,parsePatterns);
    }
}
