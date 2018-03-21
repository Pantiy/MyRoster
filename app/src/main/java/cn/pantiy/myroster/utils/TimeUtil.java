package cn.pantiy.myroster.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Pantiy on 2018/3/21.
 * Copyright © 2016 All rights Reserved by Pantiy
 */

public class TimeUtil {

    private static final String DEFAULT_TIME_FORMAT = "EE  |  HH:mm  |  yyyy-MM-dd";

    public static String format(Date time) {
        return new SimpleDateFormat(DEFAULT_TIME_FORMAT, Locale.CHINA).format(time);
    }

    public static String format(String format, Date time) {
        return new SimpleDateFormat(format, Locale.CHINA).format(time);
    }
}
