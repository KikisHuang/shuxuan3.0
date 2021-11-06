package com.kikis.commnlibrary.utils;

import android.annotation.SuppressLint;

import com.blankj.utilcode.constant.TimeConstants;
import com.kikis.commnlibrary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.blankj.utilcode.util.TimeUtils.getNowDate;
import static com.blankj.utilcode.util.TimeUtils.isToday;
import static com.blankj.utilcode.util.TimeUtils.millis2String;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;

/**
 * Created by Kikis on 2018/5/15.
 */
@SuppressLint("WrongConstant")
public class DateUtils {


    public static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String HMSS = "HH:mm:ss.SSS";
    public static final String MSS = "mm:ss.SSS";
    public static final String MDHM = "MM-dd HH:mm";
    public static final String YMDHM = "yyyy-MM-dd HH:mm";
    public static final String YMD = "yyyy-MM-dd";
    public static final String CMD = "MM月dd日";
    public static final String CMDHM = "MM月dd日 HH:mm";
    public static final String CYMD = "yyyy年MM月dd日";
    public static final String MD = "MM-dd";
    public static final String HMS = "HH:mm:ss";
    public static final String HM = "HH:mm";
    public static final String D = "d";

    /**
     * 获取当前时间;
     *
     * @return
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间;
     *
     * @return
     */
    public static String getDateYMD() {
        Calendar c = Calendar.getInstance();//
        int mYear = c.get(Calendar.YEAR); // 获取当前年份
        int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期

        return mYear + gets(R.string.year) + mMonth + gets(R.string.month) + mDay;
    }

    /**
     * 获取当前时间;
     *
     * @return
     */
    public static String getDateymd() {
        Calendar c = Calendar.getInstance();//
        int mYear = c.get(Calendar.YEAR); // 获取当前年份
        int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期

        return mYear + "-" + mMonth + "-" + mDay;
    }

    /**
     * 获取当前年;
     *
     * @return
     */
    public static int getDateYear() {

        Calendar c = Calendar.getInstance();//
        int mYear = c.get(Calendar.YEAR); // 获取当前年份

        return mYear;
    }

    /**
     * 获取当前月;
     *
     * @return
     */
    public static int getDateMonth() {

        Calendar c = Calendar.getInstance();//
        int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份

        return mMonth;
    }

    /**
     * 获取当前日;
     *
     * @return
     */
    public static int getDateDayh() {

        Calendar c = Calendar.getInstance();//
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期

        return mDay;
    }

    /**
     * string中获取dd;
     *
     * @return
     */
    public static int getStringDateDay(String str) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(str);
            Calendar c = Calendar.getInstance();//
            c.setTime(date);
            int day = c.get(Calendar.DAY_OF_MONTH);// 获取当前月份
            return day;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * string中获取MM;
     *
     * @return
     */
    public static int getStringDateMonth(String str) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(str);
            Calendar c = Calendar.getInstance();//
            c.setTime(date);
            int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
            return mMonth;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * string中获取yyyy;
     *
     * @return
     */
    public static int getStringDateYear(String str) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(str);
            Calendar c = Calendar.getInstance();//
            c.setTime(date);
            int year = c.get(Calendar.YEAR);// 获取当前年份
            return year;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 根据年 月 获取对应的月份 天数
     */
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }


    /**
     * 获取今年日期;
     *
     * @return
     */
    public static int getThisYear() {
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType, Locale.getDefault());
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType, Locale.getDefault()).format(data);
    }


    public static boolean ThisYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.get(Calendar.YEAR) == getThisYear())
            return true;
        return false;
    }


    /**
     * 计算时间差
     */
    public static final int CAL_MINUTES = 1000 * 60;
    public static final int CAL_HOURS = 1000 * 60 * 60;
    public static final int CAL_DAYS = 1000 * 60 * 60 * 24;

    /**
     * 获取当前时间格式化后的值
     *
     * @param pattern
     * @return
     */
    public static String getNowDateText(String pattern) {
        SimpleDateFormat sdf = getSimpleDateFormat(pattern);
        return sdf.format(new Date());
    }

    /**
     * 获取日期格式化后的值
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String getDateText(Date date, String pattern) {
        SimpleDateFormat sdf = getSimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 字符串时间转换成Date格式
     *
     * @param date
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date getDate(String date, String pattern) throws ParseException {
        SimpleDateFormat sdf = getSimpleDateFormat(pattern);
        return sdf.parse(date);
    }

    private static SimpleDateFormat getSimpleDateFormat(String pattern) {
        return new SimpleDateFormat(pattern, Locale.getDefault());
    }

    /**
     * 获取时间戳
     *
     * @param date
     * @return
     */
    public static Long getTime(Date date) {
        return date.getTime();
    }

    /**
     * 计算时间差值以某种约定形式显示
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static String timeDiffText(Date startDate, Date endDate) {

        Long start = DateUtils.getTime(startDate);
        Long end = DateUtils.getTime(endDate);
        long diff = (end - start);
        if (diff < 0)
            return "未知";

        int days = (int) ((diff) / (CAL_DAYS));
        int hours = (int) ((diff) / (CAL_HOURS));
        int minutes = (int) ((diff) / (CAL_MINUTES));

        if (days <= 0 && hours <= 0 && minutes <= 60)
            return minutes + "分钟前";
        if (days <= 0 && hours < 24)
            return hours + "小时前";
        if (days > 0 && days <= 15)
            return days + "天前";
        if (days >= 30 && days <= 365)
            return String.valueOf(Math.round(Float.valueOf(days / 30))) + "个月前";
        if (days > 365)
            return DateUtils.getDateText(startDate, DateUtils.YMD);

        return DateUtils.getDateText(startDate, DateUtils.CMD);
    }

    /**
     * 计算时间差值以某种约定形式显示
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static String timeDiffText(Long startDate, Long endDate) {

        Long start = startDate;
        Long end = endDate;
        long diff = (end - start);
        if (diff < 0)
            return "未知";

        int days = (int) ((diff) / (CAL_DAYS));
        int hours = (int) ((diff) / (CAL_HOURS));
        int minutes = (int) ((diff) / (CAL_MINUTES));

        if (days <= 0 && hours <= 0 && minutes <= 10)
            return "刚刚";
        if (days <= 0 && hours <= 0 && minutes <= 60)
            return minutes + "分钟前";
        if (days <= 0 && hours < 24)
            return hours + "小时前";
        if (days > 0 && days <= 15)
            return days + "天前";
        if (days >= 30 && days <= 365)
            return String.valueOf(Math.round(Float.valueOf(days / 30))) + "个月前";
        if (days > 365)
            return DateUtils.getDateText(new Date(startDate), DateUtils.YMD);

        return DateUtils.getDateText(new Date(startDate), DateUtils.CMD);
    }

    /**
     * 计算时间差值以某种约定形式显示
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static String timeDiffDayText(Long startDate, Long endDate) {

        Long start = startDate;
        Long end = endDate;
        long diff = (end - start);
        if (diff < 0)
            return "第1天";

        int days = (int) ((diff) / (CAL_DAYS));
        int hours = (int) ((diff) / (CAL_HOURS));
        int minutes = (int) ((diff) / (CAL_MINUTES));

        if (days <= 0 && hours <= 24)
            return "第1天";
        if (days > 0)
            return "第" + days + "天";

        return "第" + days + "天";
    }


    /**
     * 计算时间差值以某种约定形式显示
     *
     * @param start
     * @param end
     * @return
     */
    public static String getMessagetimeDiffText(Long start, Long end) {

        long diff = (end - start);
        if (diff < 0)
            return "未知";

        int days = (int) ((diff) / (CAL_DAYS));

        long wee = getWeeOfToday();

        if (start >= wee) {
            //今天
            return String.format("%tR", start);
        }

        if (start >= wee - TimeConstants.DAY) {
            return String.format("昨天 %tR", start);
        }
    /*    if (days <= 0)
            return DateUtils.getDateText(new Date(start), DateUtils.HM);*/
        if (days > 1 && days <= 365)
            return DateUtils.getDateText(new Date(start), DateUtils.CMD);
        if (days > 365)
            return DateUtils.getDateText(new Date(start), DateUtils.YMD);

        return DateUtils.getDateText(new Date(start), DateUtils.CMD);
    }


    /**
     * 计算时间差值以某种约定形式显示
     *
     * @param start
     * @param end
     * @return
     */
    public static String getEventtimeDiffText(Long start, Long end) {

        long diff = (end - start);
        if (diff < 0)
            return "今天";

        int days = (int) ((diff) / (CAL_DAYS));

        long wee = getWeeOfToday();

        int year = Integer.valueOf(getDateText(new Date(start), "y"));

        //这个时间戳月份的天数
        int month = getMonthLastDay(year, Integer.valueOf(getDateText(new Date(start), "M")));

        if (start >= wee) {
            //今天
            return "今天";
        } else if (start >= wee - TimeConstants.DAY) {
            return "昨天";
        } else if (days > 1 && days <= month)
            return DateUtils.getDateText(new Date(start), DateUtils.CMD);
        else if (days > 365)
            return year + "年前";
        else if (days > month)
            return month + "月前";


        return DateUtils.getDateText(new Date(start), DateUtils.CMD);
    }

    /**
     * 计算时间差值以某种约定形式显示
     *
     * @param start
     * @param end
     * @return
     */
    public static String getFincaEventtimeDiffText(Long start, Long end) {

        long diff = (end - start);
        if (diff < 0)
            return "今天";

        int days = (int) ((diff) / (CAL_DAYS));

        long wee = getWeeOfToday();

        int month = Integer.valueOf(getDateText(new Date(start), "MM"));
        int day = Integer.valueOf(getDateText(new Date(start), "dd"));

        if (start >= wee) {
            //今天
            return "今天";
        } else if (start >= wee - TimeConstants.DAY) {
            return "昨天";
        } else if (days > 1)
            return day + "\n/\n" + month + "月";

        return DateUtils.getDateText(new Date(start), DateUtils.CMD);
    }

    /**
     * 计算时间差值以某种约定形式显示
     *
     * @param start
     * @param end
     * @return
     */
    public static String getMessageDetailtimeDiffText(Long start, Long end) {

        long diff = (end - start);
        if (diff < 0)
            return "";

        int days = (int) ((diff) / (CAL_DAYS));

        long wee = getWeeOfToday();

        if (start >= wee) {
            //今天
            return String.format("%tR", start);
        }
        if (start >= wee - TimeConstants.DAY) {
            return String.format("昨天 %tR", start);
        }

        if (days > 1 && days <= 365)
            return DateUtils.getDateText(new Date(start), DateUtils.MDHM);
        if (days > 365)
            return DateUtils.getDateText(new Date(start), DateUtils.YMDHM);

        return DateUtils.getDateText(new Date(start), DateUtils.MDHM);
    }

    /**
     * 计算时间戳以天数显示
     *
     * @param start
     * @param end
     * @return
     */
    public static String timeofDay(Long start, Long end) {

        if (isToday(start)) {
            return "第1天";
        }

        long diff = (end - start);
        if (diff < 0)
            return "第1天";

        int days = (int) ((diff) / (CAL_DAYS));

        if (days > 0)
            return "第" + days + "天";

        return "第1天";
    }

    /**
     * 显示某种约定后的时间值,类似微信朋友圈发布说说显示的时间那种
     *
     * @param date
     * @return
     */
    public static String showTimeText(Date date) {
        return DateUtils.timeDiffText(date, new Date());
    }

    /**
     * 判断2个时间大小
     * yyyy-MM-dd 格式（自己可以修改成想要的时间格式）
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static int getTimeCompareSize(String startTime, String endTime) {

        int i = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//年-月-日 时-分
        try {
            Date date1 = dateFormat.parse(startTime);//开始时间
            Date date2 = dateFormat.parse(endTime);//结束时间
            // 1 开始时间大于结束时间 2 开始时间与结束时间相同 3 开始时间小于结束时间
            if (date2.getTime() < date1.getTime()) {
                i = 1;
            } else if (date2.getTime() == date1.getTime()) {
                i = 2;
            } else if (date2.getTime() > date1.getTime()) {
                //正常情况下的逻辑操作.
                i = 3;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * 获取当前时间戳跟该时间戳的差
     *
     * @param ns (纳秒单位的时间戳)
     * @return
     */
    public static String getFriendlyTimeSpanByNows(long nowtime, final long ns, String format) {

        //将纳秒转为毫秒
        long millis = ns / 1000000;

        long now = nowtime == 0 ? System.currentTimeMillis() : nowtime;
        if (now < millis)
            now = millis;

        long span = now - millis;

        // 需要解析的日期字符串
        // 解析格式，yyyy表示年，MM(大写M)表示月,dd表示天，HH表示小时24小时制，小写的话是12小时制
        // mm，小写，表示分钟，ss表示秒
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            // 用parse方法，可能会异常，所以要try-catch
            Date date = sdf.parse(millis2String(millis));
            // 获取日期实例
            Calendar calendar = Calendar.getInstance();
            // 将日历设置为指定的时间
            calendar.setTime(date);
            // 获取年
            int year = calendar.get(Calendar.YEAR);
            // 这里要注意，月份是从0开始。
            int month = calendar.get(Calendar.MONTH);
            // 获取天
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            int hour = calendar.get(Calendar.HOUR_OF_DAY);

            int minute = calendar.get(Calendar.MINUTE);


            Calendar nowCalendar = Calendar.getInstance();
            nowCalendar.setTime(getNowDate());
            int nowYear = nowCalendar.get(Calendar.YEAR);

            // 获取当天 00:00
            long wee = getWeeOfToday();
            if (millis >= wee) {
                //今天
                return String.format("%tR", millis);
            } else if (millis >= wee - TimeConstants.DAY) {
                return String.format("昨天 %tR", millis);
            } else {

                if (format.equals(MD))
                    return month + "-" + day;

                if (format.equals(YMD))
                    return year + "-" + month + "-" + day;

                if (format.equals(YMDHM))
                    return nowYear == year ? month + "-" + day + " " + hour + ":" + minute : year + "-" + month + "-" + day + " " + hour + ":" + minute;

                return year + "-" + month + "-" + day;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "未知";
    }


    public static long getWeeOfToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }


    /**
     * 2个时间戳是否同一天
     *
     * @param millis1
     * @param millis2
     * @param timeZone
     * @return
     */
    public static boolean isSameDay(long millis1, long millis2, TimeZone timeZone) {
        long interval = millis1 - millis2;
        return interval < 86400000 && interval > -86400000 && millis2Days(millis1, timeZone) == millis2Days(millis2, timeZone);
    }

    private static long millis2Days(long millis, TimeZone timeZone) {
        return (((long) timeZone.getOffset(millis)) + millis) / 86400000;
    }


    /**
     * 得到指定月的天数
     */
    public static int getMonthLastDay(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }


    /**
     * 将毫秒转为 dd HH:mm:ss
     *
     * @param
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     * @author fy.zhang
     */
    public static String getMdhm(long mss) {

        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;

        return days + " days " + hours + " hours " + minutes + " minutes "
                + seconds + " seconds ";
    }

    /**
     * 毫秒获取天数
     *
     * @param
     * @return 该毫秒数转换为 天
     * @author fy.zhang
     */
    public static String getDay(long mss) {

        long days = mss / (1000 * 60 * 60 * 24);

        return String.valueOf(days);
    }

    /**
     * 毫秒获取时
     *
     * @param
     * @return 该毫秒数转换为 天
     * @author fy.zhang
     */
    public static String getHour(long mss) {

        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);

        return String.valueOf(hours);
    }

    /**
     * 毫秒获取分钟
     *
     * @param
     * @return 该毫秒数转换为 天
     * @author fy.zhang
     */
    public static String getMinute(long mss) {

        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);

        return String.valueOf(minutes);
    }


    /**
     * 毫秒获取秒
     *
     * @param
     * @return 该毫秒数转换为 天
     * @author fy.zhang
     */
    public static String getSeconds(long mss) {

        long seconds = (mss % (1000 * 60)) / 1000;

        return String.valueOf(seconds);
    }

    /**
     * 获取时间戳
     *
     * @param time
     * @return
     */
    public static long getTimeStamp(SimpleDateFormat simpleDateFormat, String time) {
        long mTimeStamp = 0;
        if (time != null && time.length() > 0) {
            Date date;
            String times = null;
            try {
                date = simpleDateFormat.parse(time);
                long l = date.getTime();
                String stf = String.valueOf(l);
                times = stf.substring(0, 10);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mTimeStamp = Long.parseLong(times);
        }
        return mTimeStamp;
    }
}
