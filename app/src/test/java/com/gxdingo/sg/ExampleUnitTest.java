package com.gxdingo.sg;


import com.kikis.commnlibrary.utils.BaseLogUtils;

import org.junit.Test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.blankj.utilcode.util.RegexUtils.isMobileExact;
import static com.blankj.utilcode.util.TimeUtils.date2String;
import static com.blankj.utilcode.util.TimeUtils.getNowMills;
import static com.blankj.utilcode.util.TimeUtils.getNowString;
import static com.blankj.utilcode.util.TimeUtils.string2Date;
import static com.kikis.commnlibrary.utils.BigDecimalUtils.div;
import static com.kikis.commnlibrary.utils.BigDecimalUtils.sub;
import static com.kikis.commnlibrary.utils.CommonUtils.URLRequest;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    @Test
    public void addition_isCorrect() {
//        assertEquals(4, 2 + 2);

//        String json = "{\"msg\":\"请求成功\",\"code\":0,\"data\":{}}";
//        NormalBean normalBean = GsonUtil.GsonToBean(json, NormalBean.class);

        //dWKULklfNvKfWdp
        System.out.println(" content  === " + div("2000", String.valueOf(1000), 1)+"公里");

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
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");  //yyyy-MM-dd'T'HH:mm:ss.SSSZ
            Date date = df.parse(oldDateStr);
            SimpleDateFormat df1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
            Date date1 = df1.parse(date.toString());
            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return df2.format(date1);
        } catch (ParseException e) {
            System.out.println(e);

            return "";
        }
    }
}