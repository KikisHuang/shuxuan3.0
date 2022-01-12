package com.gxdingo.sg.http;


import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
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

import static com.gxdingo.sg.http.Api.HTTP;
import static com.gxdingo.sg.http.Api.HTTPS;
import static com.gxdingo.sg.http.Api.L;
import static com.gxdingo.sg.http.Api.OFFICIAL_OSS_UPLOAD_URL;
import static com.gxdingo.sg.http.Api.OSS_URL;
import static com.gxdingo.sg.http.Api.SM;
import static com.gxdingo.sg.http.Api.TEST_OSS_UPLOAD_URL;
import static com.gxdingo.sg.http.Api.isUat;
import static com.gxdingo.sg.http.ClientApi.CLIENT_PORT;
import static com.gxdingo.sg.http.ClientApi.UAT_URL;
import static com.gxdingo.sg.http.StoreApi.STORE_PORT;
import static com.gxdingo.sg.utils.LocalConstant.CLIENT_OFFICIAL_HTTP_KEY;
import static com.gxdingo.sg.utils.LocalConstant.CLIENT_UAT_HTTP_KEY;
import static com.gxdingo.sg.utils.LocalConstant.GLOBAL_SIGN;
import static com.gxdingo.sg.utils.LocalConstant.IM_SIGN;
import static com.gxdingo.sg.utils.LocalConstant.LOGIN_WAY;
import static com.gxdingo.sg.utils.LocalConstant.OSS_SIGN_KEY;
import static com.gxdingo.sg.utils.LocalConstant.STORE_OFFICIAL_HTTP_KEY;
import static com.gxdingo.sg.utils.LocalConstant.STORE_UAT_HTTP_KEY;
import static com.gxdingo.sg.utils.LocalConstant.TEST_HTTP_KEY;
import static com.gxdingo.sg.utils.LocalConstant.TEST_OSS_KEY;
import static com.gxdingo.sg.utils.LocalConstant.UAT_OSS_KEY;
import static com.kikis.commnlibrary.utils.Constant.isDebug;
import static com.kikis.commnlibrary.utils.MyToastUtils.customToast;

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

    /**
     * 切换全局url
     *
     * @param isUser
     */
    public static void switchGlobalUrl(boolean isUser) {

        //全局url初始化
        if (isUser) {
            //客户端
            Api.URL = isUat ? HTTP + UAT_URL : !isDebug ? HTTPS + ClientApi.OFFICIAL_URL : HTTP + ClientApi.TEST_URL + SM + CLIENT_PORT + L;

            Api.OSS_URL = isUat ? HTTP + UAT_URL : !isDebug ? HTTPS + OFFICIAL_OSS_UPLOAD_URL : HTTP + TEST_OSS_UPLOAD_URL;

            //客户端 key
            LocalConstant.GLOBAL_SIGN = isUat ? CLIENT_UAT_HTTP_KEY : !isDebug ? CLIENT_OFFICIAL_HTTP_KEY : TEST_HTTP_KEY;
        } else {
            //商家端
            Api.URL = isUat ? HTTP + StoreApi.UAT_URL : !isDebug ? HTTPS + StoreApi.OFFICIAL_URL : HTTP + StoreApi.TEST_URL + SM + STORE_PORT + L;

            Api.OSS_URL = isUat ? HTTP + ClientApi.UAT_URL : !isDebug ? HTTPS + OFFICIAL_OSS_UPLOAD_URL : HTTP + TEST_OSS_UPLOAD_URL;
            //商家端 key
            LocalConstant.GLOBAL_SIGN = isUat ? STORE_UAT_HTTP_KEY : !isDebug ? STORE_OFFICIAL_HTTP_KEY : TEST_HTTP_KEY;
        }

        SPUtils.getInstance().put(LOGIN_WAY, isUser);
/*

        //正式环境测试
        if (isUser) {
            //客户端
            Api.URL = HTTPS + ClientApi.OFFICIAL_URL;
            Api.OSS_URL = HTTPS + OFFICIAL_OSS_UPLOAD_URL;
                //客户端
                LocalConstant.GLOBAL_SIGN = CLIENT_OFFICIAL_HTTP_KEY;
        } else {
            //商家端
            Api.URL = HTTPS + StoreApi.OFFICIAL_URL;
            Api.OSS_URL = HTTPS + OFFICIAL_OSS_UPLOAD_URL;
                //商家端
                LocalConstant.GLOBAL_SIGN = STORE_OFFICIAL_HTTP_KEY;
        }
*/

        //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
        EasyHttp.getInstance()
                //可以全局统一设置全局URL
                .setBaseUrl(Api.URL);//设置全局URL  url只能是域名 或者域名+端口号

    }


    public static String getCurrentTimeUTCM() {
        Calendar cal = Calendar.getInstance();
        // 返回当前系统的UTC时间，具体实现可参看JDK源码
        long currentTimeUTC = cal.getTimeInMillis();
        String s = String.valueOf(currentTimeUTC);
        return s.substring(0, 10);
    }

}
