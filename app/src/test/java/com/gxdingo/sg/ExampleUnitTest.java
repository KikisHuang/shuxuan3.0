package com.gxdingo.sg;


import org.junit.Test;

import static com.blankj.utilcode.util.RegexUtils.isMobileExact;
import static com.blankj.utilcode.util.TimeUtils.string2Millis;
import static com.kikis.commnlibrary.utils.DateUtils.getCustomDate;

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


        System.out.println(getCustomDate(string2Millis("2021-11-10 20:45:45"), string2Millis("2021-11-16 20:45:45")));


        System.out.println(isMobileExact("19195753176"));
    }
}