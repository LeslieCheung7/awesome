package com.yoimiya.awesome.network.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static final String STANDARD_TIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取标准时间
     * @return eg: 2023-10-19 15:57:31
     */
    public static String getDataTime() {
        return new SimpleDateFormat(STANDARD_TIME, Locale.CHINESE).format(new Date());
    }
}
