package com.myitsolver.baseandroidapp.logic;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Patrik on 2017. 01. 21..
 */
public class DateHelper {
    public static String getFormattedDate(Date creationDate) {
        return new SimpleDateFormat("yyyy-MM-dd").format(creationDate);
    }

    private static final long DAY_MILLIS = 1000 * 60 * 60 * 24;
    private static final long HOUR_MILLIS = 1000 * 60 * 60;
    private static final long MIN_MILLIS = 1000 * 60;


    public static int getDays(long time) {
        int days = (int) Math.floor(time / DAY_MILLIS);
        return days;
    }

    public static int getMins(long timeLeft) {
        int hours = (int) Math.floor((timeLeft - DAY_MILLIS * getDays(timeLeft) - getHours(timeLeft) * HOUR_MILLIS) / MIN_MILLIS);
        return hours;
    }

    public static int getHours(long timeLeft) {
        int hours = (int) Math.floor((timeLeft - DAY_MILLIS * getDays(timeLeft)) / DAY_MILLIS);
        return hours;
    }
}
