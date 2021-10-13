package com.gxdingo.sg.model;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;

import com.alipay.sdk.app.AuthTask;
import com.blankj.utilcode.util.SPUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.AuthResult;
import com.gxdingo.sg.utils.WechatUtils;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gxdingo.sg.http.ClientApi.CLIENT_PRIVACY_AGREEMENT_KEY;
import static com.gxdingo.sg.http.ClientApi.CLIENT_SERVICE_AGREEMENT_KEY;
import static com.gxdingo.sg.http.StoreApi.STORE_SHOP_AGREEMENT_KEY;
import static com.gxdingo.sg.utils.LocalConstant.LOGIN_WAY;
import static com.gxdingo.sg.utils.LocalConstant.SDK_AUTH_FLAG;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * @author: Kikis
 * @date: 2021/3/31
 * @page:
 */
public class LoginModel {

    private int mOldTab = 0;

    public LoginModel() {

    }


    /**
     * 微信第三方登录
     */
    public void wxLogin() {

        final SendAuth.Req req = new SendAuth.Req();

        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo";

        WechatUtils.getInstance().getWxApi().sendReq(req);
    }

    /**
     * 支付宝授权完整版
     *
     * @param activity
     * @param authInfo
     * @param mHandler
     */
    public void aliPayAuth(Activity activity, String authInfo, Handler mHandler) {


        Runnable runnable = () -> {
            // 构造AuthTask 对象
            AuthTask authTask = new AuthTask(activity);
            // 调用授权接口，获取授权结果
            Map<String, String> result = authTask.authV2(authInfo, true);
            AuthResult authResult = new AuthResult(result, true);

            Message msg = new Message();
            msg.what = SDK_AUTH_FLAG;
            msg.obj = authResult;
            mHandler.sendMessage(msg);
        };
        // 必须异步调用
        Thread authThread = new Thread(runnable);
        authThread.start();

    }

//
//    public void setTextColor(Context context, String string, String[] keyword, CustomResultListener<SpannableString> customResultListener) {
//
//        SpannableString s = new SpannableString(string);
//
//        for (int i = 0; i < keyword.length; i++) {
//            Pattern p = Pattern.compile(keyword[i]);
//            Matcher m = p.matcher(s);
//
//            while (m.find()) {
//
//                int start = m.start();
//
//                int end = m.end();
//
//                final int finalI = i;
//                s.setSpan(new PartTextClickSpan(getc(R.color.blue_dominant_tone), false, view -> {
//                    boolean isUser = SPUtils.getInstance().getBoolean(LOGIN_WAY, true);
//
//                    if (finalI == 0)
//                        goToPagePutSerializable(context, WebActivity.class, getIntentEntityMap(new Object[]{true, 0,CLIENT_SERVICE_AGREEMENT_KEY}));
//                    else
//                        goToPagePutSerializable(context, WebActivity.class, getIntentEntityMap(new Object[]{true, 0, (isUser ? CLIENT_PRIVACY_AGREEMENT_KEY : STORE_SHOP_AGREEMENT_KEY)}));
//
//                }), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//        }
//        if (customResultListener != null)
//            customResultListener.onResult(s);
//    }
}
