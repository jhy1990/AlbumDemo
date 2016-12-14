package com.example.humax.albumdemo.utils;



import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static String time;

    public static String getTodayMillisecondFromZero(){
        Date date = new Date();
        long l = 24*60*60*1000; //每天的毫秒数
        long now = date.getTime()%l;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        time = sdf.format(now);
        return time;
    }

    public static String getAMorPM(String time){
        String[] split = time.split(":");
        Integer hour = Integer.valueOf(split[0]);

        if(hour>=12){
            return "PM";
        }
        return "AM";
    }

    public static boolean isSameDay(long t1, long t2) {
        return DateFormat.format("yyyy/MM/dd", new Date(t1)).toString().equals(DateFormat.format("yyyy/MM/dd", new Date(t2)).toString());
    }

    public static boolean isToday(long t) {
        return DateFormat.format("yyyy/MM/dd", new Date()).toString().equals(DateFormat.format("yyyy/MM/dd", new Date(t)).toString());
    }

    public static String formatChatTime(long t) {
        if(isToday(t)) {
            return format(new Date(t), "HH:mm");
        }
        else {
            return format(new Date(t), "MM-dd HH:mm");
        }
    }

    public static String format(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }
}
