package com.gxdingo.sg.http;


import com.gxdingo.sg.biz.ProgressListener;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.SignatureUtils;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.utils.BaseLogUtils;
import com.kikis.commnlibrary.utils.Constant;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.body.UIProgressResponseCallBack;
import com.zhouyou.http.request.PostRequest;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gxdingo.sg.http.Api.OSS_URL;
import static com.gxdingo.sg.http.Api.isUat;
import static com.gxdingo.sg.utils.LocalConstant.GLOBAL_SIGN;
import static com.gxdingo.sg.utils.LocalConstant.IM_SIGN;
import static com.gxdingo.sg.utils.LocalConstant.OSS_SIGN_KEY;
import static com.gxdingo.sg.utils.LocalConstant.TEST_OSS_KEY;
import static com.gxdingo.sg.utils.LocalConstant.UAT_OSS_KEY;
import static com.kikis.commnlibrary.utils.Constant.isDebug;

/**
 * @author: Kikis
 * @date: 2020/12/1
 * @page: http请求加密统一封装方法
 */
public class HttpClient {

    public static PostRequest post(String url) {
        return post(url, new HashMap<>());
    }

    /**
     * post通用方法
     *
     * @param url
     * @param map
     * @return
     */
    public static PostRequest post(String url, Map<String, String> map) {

        PostRequest request = EasyHttp.post(url);
        String timeStamp = getCurrentTimeUTCM();

        Map<String, String> signMap = new HashMap<>();
        signMap.putAll(map);

        signMap.put(LocalConstant.TIMESTAMP, timeStamp);

        String sign = SignatureUtils.generate(signMap, GLOBAL_SIGN, SignatureUtils.SignType.MD5);

        BaseLogUtils.d(" SIGN === " + sign);
        BaseLogUtils.d(" timeStamp === " + timeStamp);

        request.headers(LocalConstant.TIMESTAMP, timeStamp)
                .headers(LocalConstant.SIGN, sign);
        if (UserInfoUtils.getInstance().isLogin()) {
            request.headers(Constant.TOKEN, UserInfoUtils.getInstance().getUserToken());
            request.headers(Constant.EMASID, UserInfoUtils.getInstance().getEMAS());

            BaseLogUtils.d(" EMASID === " + UserInfoUtils.getInstance().getEMAS());
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                request.params(entry.getKey(), entry.getValue());
            } catch (Exception e) {
                BaseLogUtils.e("http error === " + e);
            }
        }

        return request;
    }

    /**
     * post通用方法
     *
     * @param url
     * @param map
     * @return
     */
    public static PostRequest imPost(String url, Map<String, String> map) {

        PostRequest request = EasyHttp.post(url);

        String timeStamp = getCurrentTimeUTCM();

        Map<String, String> signMap = new HashMap<>();
        signMap.putAll(map);

        signMap.put(LocalConstant.TIMESTAMP, timeStamp);

        String sign = SignatureUtils.generate(signMap, IM_SIGN, SignatureUtils.SignType.MD5);

        BaseLogUtils.d(" SIGN === " + sign);
        BaseLogUtils.d(" timeStamp === " + timeStamp);

        request.headers(LocalConstant.TIMESTAMP, timeStamp)
                .headers(LocalConstant.SIGN, sign);

        if (UserInfoUtils.getInstance().isLogin()) {
            request.headers(Constant.TOKEN, UserInfoUtils.getInstance().getUserToken());

            BaseLogUtils.d(" USERIDENTIFIER === " + timeStamp);
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                request.params(entry.getKey(), entry.getValue());
            } catch (Exception e) {
                BaseLogUtils.e("http error === " + e);
            }
        }

        return request;
    }

    /**
     * 聊天通用post方法
     *
     * @param url
     * @param map
     * @return
     */
    public static PostRequest chatKeyPost(String url, Map<String, String> map) {

        PostRequest request = EasyHttp.post(OSS_URL + url);

        String timeStamp = getCurrentTimeUTCM();

        Map<String, String> signMap = new HashMap<>();
        signMap.putAll(map);

        signMap.put(LocalConstant.TIMESTAMP, timeStamp);

        String sign = SignatureUtils.generate(signMap, isDebug ? isUat ? UAT_OSS_KEY : TEST_OSS_KEY : OSS_SIGN_KEY, SignatureUtils.SignType.MD5);

        request.headers(LocalConstant.TIMESTAMP, timeStamp)
                .headers(LocalConstant.SIGN, sign);

        if (UserInfoUtils.getInstance().isLogin()) {
   /*         if (!isEmpty(UserInfoUtils.getInstance().getIdentifier()))
                request.headers(LocalConstant.USERIDENTIFIER, UserInfoUtils.getInstance().getIdentifier());*/
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                request.params(entry.getKey(), entry.getValue());
            } catch (Exception e) {
                BaseLogUtils.e("http error === " + e);
            }
        }

        return request;
    }

    /**
     * 上传通用post方法
     *
     * @param url
     * @param map
     * @param progressListener
     * @return
     */
    public static PostRequest postUpLoad(String url, Map<String, Object> map, ProgressListener progressListener) {

        String timeStamp = getCurrentTimeUTCM();
        Map<String, String> signMap = new HashMap<>();
        signMap.put(LocalConstant.TIMESTAMP, timeStamp);

        PostRequest request = EasyHttp.post(url)
                .headers(LocalConstant.TIMESTAMP, timeStamp)
                .headers(LocalConstant.SIGN, SignatureUtils.generate(signMap, OSS_SIGN_KEY, SignatureUtils.SignType.MD5));

        if (UserInfoUtils.getInstance().isLogin())
            request.headers(Constant.TOKEN, UserInfoUtils.getInstance().getUserToken());

        for (Map.Entry<String, Object> entry : map.entrySet()) {

            if (entry.getValue() instanceof File) {
                request.params(entry.getKey(), (File) entry.getValue(), new UIProgressResponseCallBack() {
                    @Override
                    public void onUIResponseProgress(long bytesRead, long contentLength, boolean done) {

                        if (progressListener != null) {
                            int progress = (int) (bytesRead * 100 / contentLength);
                            progressListener.onProgress(done, progress);
                        }

                    }
                });
            } else if (entry.getValue() instanceof List) {
                request.addFileParams(entry.getKey(), (List<File>) entry.getValue(), new UIProgressResponseCallBack() {
                    @Override
                    public void onUIResponseProgress(long bytesRead, long contentLength, boolean done) {

                        if (progressListener != null) {
                            int progress = (int) (bytesRead * 100 / contentLength);
                            progressListener.onProgress(done, progress);
                        }

                    }
                });
            }  else {
                request.params(entry.getKey(), String.valueOf(entry.getValue()));
            }

        }
        return request;
    }


    public static String getCurrentTimeUTCM() {
        Calendar cal = Calendar.getInstance();
        // 返回当前系统的UTC时间，具体实现可参看JDK源码
        long currentTimeUTC = cal.getTimeInMillis();
        String s = String.valueOf(currentTimeUTC);
        return s.substring(0, 10);
    }

}
