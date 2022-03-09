package com.gxdingo.sg.model;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.reflect.TypeToken;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.BindingPhoneActivity;
import com.gxdingo.sg.activity.ClientActivity;
import com.gxdingo.sg.activity.LoginActivity;
import com.gxdingo.sg.bean.AuthResult;
import com.gxdingo.sg.bean.NormalBean;
import com.gxdingo.sg.bean.OauthEventBean;
import com.gxdingo.sg.bean.OneKeyLoginEvent;
import com.gxdingo.sg.bean.UserBean;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.http.Api;
import com.gxdingo.sg.http.HttpClient;
import com.gxdingo.sg.utils.ClientLocalConstant;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.utils.WechatUtils;
import com.gxdingo.sg.view.MyBaseSubscriber;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.utils.GsonUtil;
import com.mobile.auth.gatewayauth.AuthRegisterXmlConfig;
import com.mobile.auth.gatewayauth.AuthUIConfig;
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper;
import com.mobile.auth.gatewayauth.ResultCode;
import com.mobile.auth.gatewayauth.TokenResultListener;
import com.mobile.auth.gatewayauth.model.TokenRet;
import com.mobile.auth.gatewayauth.ui.AbstractPnsViewDelegate;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.zhouyou.http.callback.CallClazzProxy;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.ApiResult;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import io.reactivex.Observable;

import static com.blankj.utilcode.util.StringUtils.getString;
import static com.gxdingo.sg.http.Api.GET_MOBILE_KEY;
import static com.gxdingo.sg.http.Api.PAYMENT_ALIPAY_AUTHINFO;
import static com.gxdingo.sg.http.Api.SM;
import static com.gxdingo.sg.http.Api.STORE_PRIVACY_AGREEMENT_KEY;
import static com.gxdingo.sg.http.Api.USER_OPEN_LOGIN;
import static com.gxdingo.sg.http.Api.CLIENT_PRIVACY_AGREEMENT_KEY;
import static com.gxdingo.sg.http.Api.CLIENT_SERVICE_AGREEMENT_KEY;
import static com.gxdingo.sg.http.Api.HTML;
import static com.gxdingo.sg.utils.LocalConstant.SDK_AUTH_FLAG;
import static com.gxdingo.sg.utils.pay.AlipayTool.auth;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.CommonUtils.getd;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.CommonUtils.isWeixinAvilible;
import static com.kikis.commnlibrary.utils.Constant.isDebug;
import static com.kikis.commnlibrary.utils.GsonUtil.getJsonMap;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;
import static com.kikis.commnlibrary.utils.KikisUitls.getContext;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;
import static com.mobile.auth.gatewayauth.ResultCode.CODE_ERROR_USER_CHECKBOX;

/**
 * @author: Weaving
 * @date: 2021/10/28
 * @page:
 */
public class OneKeyModel {


    private static PhoneNumberAuthHelper mAuthHelper;

    private TokenResultListener mTokenResultListener;

    private NetWorkListener netWorkListener;

    //协议是否勾选
    private boolean isCheck;


    public OneKeyModel() {

    }

    public OneKeyModel(NetWorkListener netWorkListener) {
        this.netWorkListener = netWorkListener;
    }

    /**
     * 动态获取一键登录key
     *
     * @param context
     * @param netWorkListener
     */
    public void getKey(Context context, NetWorkListener netWorkListener, CustomResultListener customResultListener) {

        if (!isEmpty(LocalConstant.AUTH_SECRET))
            sdkInit(context, customResultListener);
        else {

            Map<String, String> map = getJsonMap();

            map.put("os", "android");


            if (netWorkListener != null)
                netWorkListener.onStarts();

            Observable<NormalBean> observable = HttpClient.post(GET_MOBILE_KEY, map)
                    .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                    }.getType()) {
                    });

            MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
                @Override
                public void onError(ApiException e) {
                    super.onError(e);
                    LogUtils.e(e);
                    quitLoginPage();

                    if (netWorkListener != null) {
                        netWorkListener.onAfters();
                        netWorkListener.onMessage(e.getMessage());
                    } else
                        ToastUtils.showShort(e.getMessage());

                }

                @Override
                public void onNext(NormalBean normalBean) {
                    if (!isEmpty(normalBean.key)) {
                        LocalConstant.AUTH_SECRET = normalBean.key;
                        sdkInit(context, customResultListener);
                    } else {
                        if (netWorkListener != null) {
                            netWorkListener.onAfters();
                            netWorkListener.onMessage("没有获取到一键登录认证key");
                        } else
                            ToastUtils.showShort("没有获取到一键登录认证key");
                    }

                }
            };
            observable.subscribe(subscriber);
            if (netWorkListener != null)
                netWorkListener.onDisposable(subscriber);
        }
    }

    /**
     * 动态获取一键登录key
     *
     * @param context
     */
    public void getKey(Context context) {

        Map<String, String> map = getJsonMap();

        map.put("os", "android");


        Observable<NormalBean> observable = HttpClient.post(GET_MOBILE_KEY, map)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);

                LogUtils.i("获取阿里一键登录key 异常 === " + e.getMessage());

            }

            @Override
            public void onNext(NormalBean normalBean) {
                LocalConstant.AUTH_SECRET = normalBean.key;

            }
        };
        observable.subscribe(subscriber);
    }

    /**
     * 支付宝获取infoStr
     *
     * @param context
     */
    public void getAliAuthInfo(Context context) {
        Observable<NormalBean> observable = HttpClient.post(PAYMENT_ALIPAY_AUTHINFO)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
            }

            @Override
            public void onNext(NormalBean normalBean) {

//                if (customResultListener != null)
//                    customResultListener.onResult(normalBean.auth);
                auth((Activity) context, normalBean.auth, new Handler() {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
                        switch (msg.what) {
                            case SDK_AUTH_FLAG:
                                AuthResult authResult = (AuthResult) msg.obj;
                                if (!TextUtils.isEmpty(authResult.getAuthCode())) {
                                    thirdPartyLogin(context, authResult.getAuthCode(), ClientLocalConstant.ALIPAY);
                                } else
                                    ToastUtils.showLong("没有获取到authCode");
                                break;
                        }
                    }
                });
            }
        };

        observable.subscribe(subscriber);
    }

    /**
     * 第三方登录
     *
     * @param code
     * @param type
     */
    public void thirdPartyLogin(Context context, String code, String type) {
        if (netWorkListener != null)
            netWorkListener.onStarts();

        if (TextUtils.isEmpty(code)) {
            ToastUtils.showLong(gets(R.string.phone_number_can_not_null));
            return;
        } else if (TextUtils.isEmpty(type)) {
            ToastUtils.showLong(gets(R.string.verification_code_can_not_null));
            return;
        }

        Map<String, String> map = getJsonMap();

        map.put(LocalConstant.APPNAME, type);
        map.put(Constant.CODE, code);

        Observable<UserBean> observable = HttpClient.post(USER_OPEN_LOGIN, map)
                .execute(new CallClazzProxy<ApiResult<UserBean>, UserBean>(new TypeToken<UserBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<UserBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
                if (netWorkListener != null)
                    netWorkListener.onAfters();
            }

            @Override
            public void onNext(UserBean userBean) {
                if (netWorkListener != null)
                    netWorkListener.onAfters();
                //0未绑定手机
                if (userBean.getIsBindMobile() == 0) {

                    ToastUtils.showLong(gets(R.string.please_bind_phone));
                    goToPagePutSerializable(context, BindingPhoneActivity.class, getIntentEntityMap(new Object[]{userBean.getOpenid(), type}));
                } else {
                    UserInfoUtils.getInstance().saveLoginUserInfo(userBean);
                    goToPage(getContext(), ClientActivity.class, null);
                    EventBus.getDefault().post(LocalConstant.LOGIN_SUCCEED);
                }
            }
        };

        observable.subscribe(subscriber);
    }

    public void sdkInit(Context context, CustomResultListener customResultListener) {

        if (mAuthHelper != null) {
            mAuthHelper.setAuthListener(null);
            mAuthHelper.quitLoginPage();
            mAuthHelper = null;
            LogUtils.i("已启动阿里一键登录页");
            return;
        }

        mTokenResultListener = new TokenResultListener() {
            @Override
            public void onTokenSuccess(String s) {
                hideLoginLoading();
                TokenRet tokenRet = null;
                try {
                    tokenRet = TokenRet.fromJson(s);
                    if (ResultCode.CODE_START_AUTHPAGE_SUCCESS.equals(tokenRet.getCode())) {
                        Log.i("oneKey-login", "唤起授权页成功：" + s);
                    }

                    if (ResultCode.CODE_SUCCESS.equals(tokenRet.getCode())) {
                        Log.i("oneKey-login", "获取token成功：" + s);
//                        EventBus.getDefault().post(new OneKeyLoginEvent(tokenRet.getToken(), isUser));

                        if (customResultListener != null)
                            customResultListener.onResult(new OneKeyLoginEvent(tokenRet.getToken()));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTokenFailed(String s) {

                hideLoginLoading();

                Log.e("oneKey-login", "获取token失败： " + s);

                TokenRet tokenRet = null;
                try {
                    tokenRet = TokenRet.fromJson(s);
                    //除了用户取消操作的事件，其他都跳转登录页面
                    if (!ResultCode.CODE_ERROR_USER_CANCEL.equals(tokenRet.getCode()))
                        UserInfoUtils.getInstance().goToLoginPage(context, "");

                    quitLoginPage();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        mAuthHelper = PhoneNumberAuthHelper.getInstance(context, mTokenResultListener);

        mAuthHelper.getReporter().setLoggerEnable(true);
        mAuthHelper.setAuthSDKInfo(LocalConstant.AUTH_SECRET);
        mAuthHelper.checkEnvAvailable(1);
        mAuthHelper.removeAuthRegisterXmlConfig();
        mAuthHelper.removeAuthRegisterViewConfig();
        int authPageOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
        if (Build.VERSION.SDK_INT == 26) {
            authPageOrientation = ActivityInfo.SCREEN_ORIENTATION_BEHIND;
        }

        mAuthHelper.addAuthRegisterXmlConfig(new AuthRegisterXmlConfig.Builder()
                .setLayout(R.layout.module_activity_one_key_login, new AbstractPnsViewDelegate() {
                    @Override
                    public void onViewCreated(View view) {

                        findViewById(R.id.tv_other).setOnClickListener(v -> {
                            goToPage(context, LoginActivity.class, null);
                        });
                        findViewById(R.id.alipay_login).setOnClickListener(v -> {
                            if (isCheck)
                                getAliAuthInfo(context);
                            else
                                ToastUtils.showShort(R.string.please_agree_to_the_terms_of_service);
//                                EventBus.getDefault().post(LocalConstant.ALIPAY_LOGIN_EVENT);
                        });
                        findViewById(R.id.wechat_login).setOnClickListener(v -> {

                            if (isCheck) {
                                if (isWeixinAvilible(getContext())) {

                                    final SendAuth.Req req = new SendAuth.Req();
                                    req.scope = "snsapi_userinfo";
                                    req.state = "wechat_sdk_demo";
                                    WechatUtils.getInstance().getWxApi(0).sendReq(req);
                                } else {
                                    ToastUtils.showLong(String.format(getString(R.string.uninstall_app), gets(R.string.wechat)));
                                }
                            } else
                                ToastUtils.showShort(gets(R.string.please_agree_to_the_terms_of_service));
//                                customResultListener.onResult(new OneKeyLoginEvent("",isUser,2));
                        });

                    }
                })
                .build());

        mAuthHelper.setUIClickListener((code, context1, jsonObj) -> {
            if (isDebug)
                Log.e("xxxxxx", "OnUIControlClick:code=" + code + ", jsonObj=" + (jsonObj == null ? "" : jsonObj));


            if (code.equals(CODE_ERROR_USER_CHECKBOX))
                isCheck = GsonUtil.GsonToBean(jsonObj, OauthEventBean.class).isIsChecked();

        });

        String c_privacy_agreementUrl = Api.URL + HTML + "identifier=" + CLIENT_PRIVACY_AGREEMENT_KEY;
        String service_agreementUrl = Api.URL + HTML + "identifier=" + CLIENT_SERVICE_AGREEMENT_KEY;
        mAuthHelper.setAuthUIConfig(new AuthUIConfig.Builder()
                .setPrivacyBefore("")
                .setAppPrivacyOne("《服务协议》", service_agreementUrl)
                .setAppPrivacyTwo("《隐私协议》", c_privacy_agreementUrl)
                .setNavColor(getc(R.color.white))
                .setNavTextColor(getc(R.color.white))
                .setLogoHidden(false)
                .setSloganHidden(true)
                .setSwitchAccHidden(true)
                .setPrivacyState(false)
                .setPrivacyTextSize(12)
                .setPrivacyOffsetY(340)
                .setCheckboxHidden(false)
                .setLogBtnText("本机号码一键登录")
                .setLogBtnBackgroundDrawable(getd(R.drawable.module_bg_main_color_round6))
                .setLightColor(true)
                .setWebViewStatusBarColor(Color.TRANSPARENT)
                .setStatusBarColor(Color.TRANSPARENT)
                .setStatusBarUIFlag(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
                .setWebNavTextSizeDp(20)
                .setNumberSizeDp(24)
                .setNumberColor(getc(R.color.blue_dark_tone))
                .setNumFieldOffsetY(220)
                .setAuthPageActIn("in_activity", "out_activity")
                .setAuthPageActOut("in_activity", "out_activity")
                .setVendorPrivacyPrefix("《")
                .setVendorPrivacySuffix("》")
                .setPageBackgroundPath("page_background_color")
                .setLogoImgPath("mytel_app_launcher")
                .setLogBtnBackgroundPath("login_btn_bg")
                .setScreenOrientation(authPageOrientation)
                .create());
        mAuthHelper.getLoginToken(context, 2000);
    }


    public void quitLoginPage() {
        destroy();
        EventBus.getDefault().post(LocalConstant.QUITLOGINPAGE);
    }

    public void destroy() {
        ToastUtils.cancel();
        if (mAuthHelper != null) {
            mAuthHelper.setAuthListener(null);
            mAuthHelper.quitLoginPage();
            mAuthHelper = null;

        }
    }

    public static void hideLoginLoading() {
        if (mAuthHelper != null)
            mAuthHelper.hideLoginLoading();
    }
}
