package com.gxdingo.sg.biz;

import android.app.Activity;

import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

/**
 * @author: Weaving
 * @date: 2021/10/12
 * @page:
 */
public class LoginContract {

    public interface LoginPresenter extends MvpPresenter<BasicsListener,LoginListener>{

        void switchUrl(boolean isUserId);

        void getWechatAuth();

        void alipayAuth();

        void sendVerificationCode();

        void getVerificationCodeTime();

        void bindPhone(String mOpenId, String mAppName);

        void weChatLogin(String code);

        void login();

        void oauth();
    }

    public interface LoginListener{
        String getCode();

        String getMobile();

        boolean isClient();

        void setVerificationCodeTime(int time);

        void showIdButton();
    }
}
