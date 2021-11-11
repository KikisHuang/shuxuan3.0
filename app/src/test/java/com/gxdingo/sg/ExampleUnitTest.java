package com.gxdingo.sg;

import com.gxdingo.sg.bean.NormalBean;
import com.kikis.commnlibrary.utils.GsonUtil;

import org.junit.Test;

import static com.gxdingo.sg.utils.DateUtils.GetUTCTime;
import static org.junit.Assert.*;

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

            System.out.println(GetUTCTime());
    }
}