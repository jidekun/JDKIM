package com.jidekun.jdk.jdkim.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JDK on 2016/5/26.
 */
public class TimeFormater {
    public static String format(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //格式化后的时间
        String formatTime = sdf.format(date);
        return formatTime;
    }
}
