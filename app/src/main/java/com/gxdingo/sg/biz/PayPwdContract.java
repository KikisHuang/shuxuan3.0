package com.gxdingo.sg.biz;

import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

/**
 * @author: Weaving
 * @date: 2021/10/24
 * @page:
 */
public class PayPwdContract {

    public interface PayPwdPresenter extends MvpPresenter<BasicsListener,PayPwdListener>{
        void getUserPhone();

        void sendVerificationCode();

        void certify();

        void certifyPwd(String pwd);

        void changePayPwd(String code, String oldPasswd);
    }

    public interface PayPwdListener{

        void setUserPhone(String phone);

        String getFirstPwd();

        String getCode();

        void next();
    }
}
