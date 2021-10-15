package com.gxdingo.sg.biz;

import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

/**
 * @author: Weaving
 * @date: 2021/10/15
 * @page:
 */
public class ClientAccountSecurityContract {

    public interface ClientAccountSecurityPresenter extends MvpPresenter<BasicsListener,ClientAccountSecurityListener>{

        void sendVerificationCode();
        void certify();
        void certifyPwd();
        void checkPayPsw();
        void updatePsw();
        void saveStatus();
        void getUserPhone();

        void sendOldPhoneVerificationCode();

        void sendNewPhoneVerificationCode(String phone);

        void nextStep(String edttcontent);

        void lastStep();
    }

    public interface ClientAccountSecurityListener{
        void setUserPhone(String phone);
        String getCode();
        void next();
//        String getOldPhoneNum();

        void oldPhoneNumberCountDown();

        void newPhoneNumberCountDown();

        void changeTitle(String title);

        void changeHint(String hint);

        void changeNextBtnText(String text);

        void bottomHintVisibility(int visib);

        void oldPhoneCodeCountdownVisibility(int visib);

        void newPhoneCodeCountdownVisibility(int visib);

        void countryCodeShow(boolean show);

        void setEdittextInputType(int type);

        void setEdittextContent(String content);

        void setEdittextHint(String hint);

        int getNumberCountDown();
    }
}
