package com.longway.framework.util;

import android.text.TextUtils;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by longway on 15/8/17.
 */
public class DateUtils {
    public static final String YMD_HMS = "yyyy-MM-dd HH:mm:ss";
    public static final String DUMP_YMD_HMS = "yyyy-MM-dd-HH:mm:ss";
    public static final String MD_HM = "MM-dd HH:mm";
    public static final String YYYY_MM_DD = "yyyy年MM月dd日";
    private static final long SECOND = 1000;
    private static final long MIN = SECOND * 60;
    private static final long HOUR = MIN * 60;
    private static final long DAY = HOUR * 24;
    private static final Calendar CALENDAR = Calendar.getInstance(Locale.getDefault());

    private DateUtils() {

    }

    private static ThreadLocal<SimpleDateFormat> sThreadLocal = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat();
        }
    };

    /**
     * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
     *
     * @return SimpleDateFormat
     */
    private static SimpleDateFormat getSdf() {
        return sThreadLocal.get();
    }

    /**
     * 是用ThreadLocal<SimpleDateFormat>来获取SimpleDateFormat,这样每个线程只会有一个SimpleDateFormat
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = getSdf();
        simpleDateFormat.applyPattern(pattern);
        return simpleDateFormat.format(date);
    }

    public static String format(long mill, String pattern) {
        return format(new Date(mill), pattern);
    }

    public static Date parse(String dateStr, String pattern) {
        try {
            SimpleDateFormat simpleDateFormat = getSdf();
            simpleDateFormat.applyPattern(pattern);
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date(System.currentTimeMillis());
    }

    /**
     * 根据创建时间获取与当前时间的差值
     * 返回结果如下:
     * 1.刚刚 小于1s的情况下
     * 2.xx秒 小于1分钟的情况下
     * 3.xx时:xx分 小于1天的情况下
     * 4.xx天 大于等于1天的情况下
     *
     * @param mills
     * @return
     */
    public static String getSpanTime(long mills) {
        long currentTime = System.currentTimeMillis();
        long diff = currentTime - mills;
        if (diff < SECOND) {
            return "刚刚";
        }
        if (diff < MIN) {
            return (diff / SECOND) + "秒";
        }
        if (diff < HOUR) {
            long min = diff / MIN;
            long second = diff / SECOND % 60;
            return min + "分" + second + "秒";
        }

        if (diff < DAY) {
            long hour = diff / HOUR;
            long min = diff / MIN % 60;
            return hour + "时:" + min + "分";

        }

        return diff / DAY + "天前";

    }

    public static synchronized int[] getYMD(Date date) {
        CALENDAR.clear();
        CALENDAR.setTime(date);
        return new int[]{
                CALENDAR.get(Calendar.YEAR), CALENDAR.get(Calendar.MONTH) + 1, CALENDAR.get(Calendar.DAY_OF_MONTH)
        };
    }

    public static boolean isExpired(String issued, long validate) {
        if (TextUtils.isEmpty(issued)) {
            return true;
        }
        String template = "EEE, d MMM yyyy HH:mm:ss Z";
        SimpleDateFormat dateFormat = getSdf();
        dateFormat.applyPattern(template);
        dateFormat.setDateFormatSymbols(new DateFormatSymbols(Locale.US));
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date d = dateFormat.parse(issued);
            long current = System.currentTimeMillis();
            return current - d.getTime() > validate * 1000;

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    public static String formatTime(String time, String pattern, String hopePattern) {
        SimpleDateFormat simpleDateFormat = getSdf();
        simpleDateFormat.applyPattern(pattern);
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        try {
            Date d = simpleDateFormat.parse(time);
            simpleDateFormat.applyPattern(hopePattern);
            return simpleDateFormat.format(d);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";

    }

}
