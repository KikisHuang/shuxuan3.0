package com.gxdingo.sg;


import android.content.Context;
import android.content.res.AssetManager;
import android.util.Pair;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.LogUtils;
import com.gxdingo.sg.utils.DateUtils;
import com.kikis.commnlibrary.utils.BaseLogUtils;
import com.taobao.android.tlog.protocol.utils.RSAUtils;

import org.junit.Test;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
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

import static com.blankj.utilcode.util.EncryptUtils.decryptHexStringRSA;
import static com.blankj.utilcode.util.RegexUtils.isMobileExact;
import static com.blankj.utilcode.util.TimeUtils.date2String;
import static com.blankj.utilcode.util.TimeUtils.getNowMills;
import static com.blankj.utilcode.util.TimeUtils.getNowString;
import static com.blankj.utilcode.util.TimeUtils.string2Date;
import static com.blankj.utilcode.util.TimeUtils.string2Millis;
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

    }

}