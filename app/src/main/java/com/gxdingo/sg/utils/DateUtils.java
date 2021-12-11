package com.gxdingo.sg.utils;

import com.blankj.utilcode.util.LogUtils;
import com.kikis.commnlibrary.utils.BaseLogUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.blankj.utilcode.util.TimeUtils.getNowString;

/**
 * @author: Kikis
 * @date: 2021/5/11
 * @page:
 */
public class DateUtils {

    /**
     * 将毫秒转化为 分钟：秒 的格式
     *
     * @param millisecond 毫秒
     * @return
     */
    public static String formatTime(long millisecond) {
        int minute;//分钟
        int second;//秒数
        minute = (int) ((millisecond / 1000) / 60);
        second = (int) ((millisecond / 1000) % 60);
        if (minute < 10) {
            if (second < 10) {
                return "0" + minute + ":" + "0" + second;
            } else {
                return "0" + minute + ":" + second;
            }
        } else {
            if (second < 10) {
                return minute + ":" + "0" + second;
            } else {
                return minute + ":" + second;
            }
        }
    }

    /**
     * GMT(格林威治标准时间)转换当前北京时间
     * 比如：1526217409 -->2018/5/13 21:16:49 与北京时间相差8个小时，调用下面的方法，是在1526217409加上8*3600秒
     *
     * @param GMT 秒单部位
     * @return
     */
    public static String stampToDate(String GMT) {

        long lt = Long.parseLong(GMT) + 8 * 3600;
        String res = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            res = simpleDateFormat.format(lt * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }


    /**
     * 日期格式转换yyyy-MM-dd'T'HH:mm:ss.SSSXXX  (yyyy-MM-dd'T'HH:mm:ss.SSSZ) TO  yyyy-MM-dd HH:mm:ss
     * 2020-04-09T23:00:00.000+08:00 TO 2020-04-09 23:00:00
     *
     * @throws ParseException
     */
    public static String dealDateFormat(String oldDateStr) {
        try {
            if (oldDateStr == null)
                return getNowString();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");  //yyyy-MM-dd'T'HH:mm:ss.SSSZ
            Date date = df.parse(oldDateStr);
            SimpleDateFormat df1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
            Date date1 = df1.parse(date.toString());
            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return df2.format(date1);
        } catch (ParseException e) {
            BaseLogUtils.e("ParseException === " + e);
            return "";
        }
    }

    /**
     * 日期格式转换yyyy-MM-dd'T'HH:mm:ss.SSSXXX  (yyyy-MM-dd'T'HH:mm:ss.SSSZ) TO  yyyy-MM-dd HH:mm:ss
     * 2020-04-09T23:00:00.000+08:00 TO 2020-04-09 23:00:00
     *
     * @throws ParseException
     */
    public static String dealDateFormat(String oldDateStr, String pattern) {
        try {
            if (oldDateStr == null)
                return getNowString();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");  //yyyy-MM-dd'T'HH:mm:ss.SSSZ
            Date date = df.parse(oldDateStr);
            SimpleDateFormat df1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
            Date date1 = df1.parse(date.toString());
            DateFormat df2 = new SimpleDateFormat(pattern);
            return df2.format(date1);
        } catch (ParseException e) {
            BaseLogUtils.e("ParseException === " + e);
            return "";
        }
    }

    /**
     * 日期格式转换yyyy-MM-dd'T'HH:mm:ss.SSSXXX  (yyyy-MM-dd'T'HH:mm:ss.SSSZ) TO  yyyy-MM-dd HH:mm:ss
     * 2020-04-09T23:00:00.000+08:00 TO 2020-04-09 23:00:00
     *
     * @throws ParseException
     */
    public static String dealDateFormatHm(String oldDateStr) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");  //yyyy-MM-dd'T'HH:mm:ss.SSSZ
            Date date = df.parse(oldDateStr);
            SimpleDateFormat df1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
            Date date1 = df1.parse(date.toString());
            DateFormat df2 = new SimpleDateFormat("HH:mm");
            return df2.format(date1);
        } catch (ParseException e) {
            BaseLogUtils.e("ParseException === " + e);
            return "";
        }
    }

    /**
     * 日期格式转换 yyyy-MM-dd HH:mm:ss  TO yyyy-MM-dd'T'HH:mm:ss.SSSXXX  (yyyy-MM-dd'T'HH:mm:ss.SSSZ)
     * 2020-04-09 23:00:00 TO 2020-04-09T23:00:00.000+08:00
     *
     * @throws ParseException
     */
    public static String dealDateFormatReverse(String oldDateStr) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = df2.parse(oldDateStr);
        return df.format(date1);
    }

    /**
     * 至今到多少天前的开始时间
     *
     * @param day
     * @return
     */
    public static String getQueryDate(int day) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(getNowString());
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(myDate);
        calendar.add(Calendar.DAY_OF_YEAR, -day);
        Date newDate = calendar.getTime();
        String date = dateFormat.format(newDate);
        return date;

    }

    /**
     * 判断是否为今天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean IsToday(String day) {

        try {
            Calendar pre = Calendar.getInstance();
            Date predate = new Date(System.currentTimeMillis());
            pre.setTime(predate);
            Calendar cal = Calendar.getInstance();
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(day);
            cal.setTime(date);
            if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
                int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                        - pre.get(Calendar.DAY_OF_YEAR);

                if (diffDay == 0) {
                    return true;
                }
            }
        } catch (ParseException e) {
            BaseLogUtils.e("ParseException === " + e);
        }
        return false;
    }

    /**
     * 判断是否为昨天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean IsYesterday(String day) {
        try {
            Calendar pre = Calendar.getInstance();
            Date predate = new Date(System.currentTimeMillis());
            pre.setTime(predate);

            Calendar cal = Calendar.getInstance();
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(day);
            cal.setTime(date);

            if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
                int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                        - pre.get(Calendar.DAY_OF_YEAR);

                if (diffDay == -1) {
                    return true;
                }
            }
        } catch (ParseException e) {
            BaseLogUtils.e("ParseException === " + e);
        }
        return false;
    }


}
