package com.gxdingo.sg.model;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.LoginActivity;
import com.gxdingo.sg.bean.OneKeyLoginEvent;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.mobile.auth.gatewayauth.AuthRegisterXmlConfig;
import com.mobile.auth.gatewayauth.AuthUIConfig;
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper;
import com.mobile.auth.gatewayauth.ResultCode;
import com.mobile.auth.gatewayauth.TokenResultListener;
import com.mobile.auth.gatewayauth.model.TokenRet;
import com.mobile.auth.gatewayauth.ui.AbstractPnsViewDelegate;

import org.greenrobot.eventbus.EventBus;

import static com.gxdingo.sg.http.HttpClient.switchGlobalUrl;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.CommonUtils.getd;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;

/**
 * @author: Weaving
 * @date: 2021/10/28
 * @page:
 */
public class OneKeyModel {


    private static PhoneNumberAuthHelper mAuthHelper;

    private TokenResultListener mTokenResultListener;

    private boolean isUser = true;

    public OneKeyModel() {
    }

    public void sdkInit(Context context) {
        mTokenResultListener = new TokenResultListener() {
            @Override
            public void onTokenSuccess(String s) {

                TokenRet tokenRet = null;
                try {
                    tokenRet = TokenRet.fromJson(s);
                    if (ResultCode.CODE_START_AUTHPAGE_SUCCESS.equals(tokenRet.getCode())) {
                        Log.i("oneKey-login", "唤起授权页成功：" + s);
                    }

                    if (ResultCode.CODE_SUCCESS.equals(tokenRet.getCode())) {
                        Log.i("oneKey-login", "获取token成功：" + s);
                        EventBus.getDefault().post(new OneKeyLoginEvent(tokenRet.getToken()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTokenFailed(String s) {
                Log.e("oneKey-login", "获取token失败：" + s);

//                goToPage(context, LoginActivity.class,null);
                UserInfoUtils.getInstance().goToLoginPage(context,"");
                TokenRet tokenRet = null;
                try {
                    tokenRet = TokenRet.fromJson(s);
                    if (ResultCode.CODE_ERROR_USER_CANCEL.equals(tokenRet.getCode())) {
                        //模拟的是必须登录 否则直接退出app的场景

                    } else {
//                        Toast.makeText(getApplicationContext(), "一键登录失败切换到其他登录方式", Toast.LENGTH_SHORT).show();
//                        Intent pIntent = new Intent(OneKeyLoginActivity.this, MessageActivity.class);
//                        startActivityForResult(pIntent, 1002);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mAuthHelper.setAuthListener(null);
            }
        };
        mAuthHelper = PhoneNumberAuthHelper.getInstance(context, mTokenResultListener);
        mAuthHelper.getReporter().setLoggerEnable(true);
        mAuthHelper.setAuthSDKInfo(LocalConstant.AUTH_SECRET);
        mAuthHelper.checkEnvAvailable();
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
                        findViewById(R.id.switch_tv).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                isUser = !isUser;
                                ((TextView)findViewById(R.id.switch_tv)).setText(isUser?"商家身份登录":"用户身份登陆");
                                ((TextView)findViewById(R.id.role_tv)).setText(isUser?"树选客户端":"树选商家端");
                                switchGlobalUrl(isUser);
                            }
                        });
                        findViewById(R.id.tv_other).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                goToPage(context,LoginActivity.class,null);
                            }
                        });
                        findViewById(R.id.alipay_login).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EventBus.getDefault().post(LocalConstant.ALIPAY_LOGIN_EVENT);
                            }
                        });
                        findViewById(R.id.wechat_login).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EventBus.getDefault().post(LocalConstant.WECHAT_LOGIN_EVENT);
                            }
                        });

                    }
                })
                .build());
        mAuthHelper.setAuthUIConfig(new AuthUIConfig.Builder()
                .setAppPrivacyOne("《服务协议》","")
                .setAppPrivacyTwo("《隐私协议》","")
                .setNavColor(getc(R.color.white))
                .setNavTextColor(getc(R.color.white))
                .setLogoHidden(true)
                .setSloganHidden(true)
                .setSwitchAccHidden(true)
                .setPrivacyState(false)
                .setPrivacyTextSize(12)
                .setPrivacyOffsetY(340)
                .setCheckboxHidden(true)
                .setLogBtnText("本机号码一键登陆")
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
        mAuthHelper.getLoginToken(context,2000);
    }


    public static void quitLoginPage(){
        if (mAuthHelper!=null)
            mAuthHelper.quitLoginPage();
    }

    public static void hideLoginLoading(){
        if (mAuthHelper!=null)
            mAuthHelper.hideLoginLoading();
    }
}
