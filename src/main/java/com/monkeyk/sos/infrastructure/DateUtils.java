package com.monkeyk.sos.infrastructure;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期处理工具类
 *
 * @author Shengzhao Li
 */
public abstract class DateUtils {

    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Private constructor
     */
    private DateUtils(){
    }

    public static Date now() {
        return new Date();
    }

    public static String toDateTime(Date date) {
        return toDateTime(date, DEFAULT_DATE_TIME_FORMAT);
    }

    public static String toDateTime(Date dateTime, String pattern) {
        return new SimpleDateFormat(pattern).format(dateTime);
    }

    public static String toDateText(Date date, String pattern) {
        if (date == null || pattern == null) {
            return null;
        }
        return new SimpleDateFormat(pattern).format(date);
    }

}
