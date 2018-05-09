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
        SHORT_US_FORMAT("MM/dd/yyyy"),
        UK_FORMAT("dd/MM/yyyy hh:mm:ss"),
        SHORT_UK_FORMAT("dd/MM/yyyy");

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

    ////////////////////////////////////////////////////////////////
    enum DateField {

        /**
         * 年
         * @see Calendar#YEAR
         */
        YEAR(Calendar.YEAR),
        /**
         * 月
         * @see Calendar#MONTH
         */
        MONTH(Calendar.MONTH),
        /**
         * 一年中第几周
         * @see Calendar#WEEK_OF_YEAR
         */
        WEEK_OF_YEAR(Calendar.WEEK_OF_YEAR),
        /**
         * 一月中第几周
         * @see Calendar#WEEK_OF_MONTH
         */
        WEEK_OF_MONTH(Calendar.WEEK_OF_MONTH),
        /**
         * 一月中的第几天
         * @see Calendar#DAY_OF_MONTH
         */
        DAY_OF_MONTH(Calendar.DAY_OF_MONTH),
        /**
         *一年中的第几天
         * @see Calendar#DAY_OF_YEAR
         */
        DAY_OF_YEAR(Calendar.DAY_OF_YEAR),
        /**
         *周几，1表示周日，2表示周一
         * @see Calendar#DAY_OF_WEEK
         */
        DAY_OF_WEEK(Calendar.DAY_OF_WEEK),
        /**
         * 天所在的周是这个月的第几周
         * @see Calendar#DAY_OF_WEEK_IN_MONTH
         */
        DAY_OF_WEEK_IN_MONTH(Calendar.DAY_OF_WEEK_IN_MONTH),
        /**
         * 上午或者下午
         * @see Calendar#AM_PM
         */
        AM_PM(Calendar.AM_PM),
        /**
         * 小时，用于12小时制
         * @see Calendar#HOUR
         */
        HOUR(Calendar.HOUR),
        /**
         * 小时，用于24小时制
         * @see Calendar#HOUR
         */
        HOUR_OF_DAY(Calendar.HOUR_OF_DAY),
        /**
         * 分钟
         * @see Calendar#MINUTE
         */
        MINUTE(Calendar.MINUTE),
        /**
         * 秒
         * @see Calendar#SECOND
         */
        SECOND(Calendar.SECOND),
        /**
         * 毫秒
         * @see Calendar#MILLISECOND
         */
        MILLISECOND(Calendar.MILLISECOND);

        // ---------------------------------------------------------------
        private int value;

        private DateField(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        /**
         * 将 {@link Calendar}相关值转换为DatePart枚举对象<br>
         *
         * @param calendarPartIntValue Calendar中关于Week的int值
         */
        public static DateField of(int calendarPartIntValue) {
            switch (calendarPartIntValue) {
                case Calendar.YEAR:
                    return YEAR;
                case Calendar.MONTH:
                    return MONTH;
                case Calendar.WEEK_OF_YEAR:
                    return WEEK_OF_YEAR;
                case Calendar.WEEK_OF_MONTH:
                    return WEEK_OF_MONTH;
                case Calendar.DAY_OF_MONTH:
                    return DAY_OF_MONTH;
                case Calendar.DAY_OF_YEAR:
                    return DAY_OF_YEAR;
                case Calendar.DAY_OF_WEEK:
                    return DAY_OF_WEEK;
                case Calendar.DAY_OF_WEEK_IN_MONTH:
                    return DAY_OF_WEEK_IN_MONTH;
                case Calendar.MINUTE:
                    return MINUTE;
                case Calendar.SECOND:
                    return SECOND;
                case Calendar.MILLISECOND:
                    return MILLISECOND;
                default:
                    return null;
            }
        }
    }

    enum DateUnit {
        /** 一毫秒 */
        MS(1),
        /** 一秒的毫秒数 */
        SECOND(1000),
        /**一分钟的毫秒数 */
        MINUTE(SECOND.getMillis() * 60),
        /**一小时的毫秒数 */
        HOUR(MINUTE.getMillis() * 60),
        /**一天的毫秒数 */
        DAY(HOUR.getMillis() * 24),
        /**一周的毫秒数 */
        WEEK(DAY.getMillis() * 7);

        private long millis;
        DateUnit(long millis){
            this.millis = millis;
        }

        /**
         * @return 单位对应的毫秒数
         */
        public long getMillis(){
            return this.millis;
        }
    }
    
    /**
     * 转换为Calendar对象
     *
     * @param date 日期对象
     * @return Calendar对象
     */
    public static Calendar calendar(Date date) {
        return calendar(date.getTime());
    }

    /**
     * 转换为Calendar对象
     *
     * @param millis 时间戳
     * @return Calendar对象
     */
    public static Calendar calendar(long millis) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return cal;
    }

    public static Calendar getTimeOfDay(Calendar c,int hour,int min,int sec,int millisec){
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, min);
        c.set(Calendar.SECOND, sec);
        c.set(Calendar.MILLISECOND, millisec);
        return c;
    }

    /**
     * 获取一天开始时间
     * @param c
     * @return
     */
    public static Date beginOfDay(Calendar c){
        return getTimeOfDay(c,0,0,0,0).getTime();
    }

    public static Date beginOfDay(Date date){
        return getTimeOfDay(calendar(date),0,0,0,0).getTime();
    }

    public static Date offset(Date date, DateField dateField, int offset) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(dateField.getValue(), offset);
        return cal.getTime();
    }

    public static Date tomorrow(Date currentDate){
        return offset(currentDate,DateField.DAY_OF_YEAR,1);
    }

    /**
     * 相差天数
     * @param begin
     * @param end
     * @return
     */
    public static long betweenDay(Date begin,Date end){
        begin = beginOfDay(begin);
        end = beginOfDay(end);
        long diff = end.getTime() - begin.getTime();
        return diff / DateUnit.DAY.getMillis();
    }

}
