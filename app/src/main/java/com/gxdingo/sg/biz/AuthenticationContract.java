package com.gxdingo.sg.biz;

import com.esandinfo.livingdetection.bean.EsLivingDetectResult;
import com.gxdingo.sg.bean.AuthenticationBean;
import com.gxdingo.sg.bean.IdCardOCRBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

/**
 * @author: Kikis
 * @date: 2021/12/30
 * @page:
 */
public class AuthenticationContract {

    public interface AuthenticationPresenter extends MvpPresenter<BasicsListener, AuthenticationListener> {

        void photoItemClick(int pos);

        void verifyInit();

    }

    public interface AuthenticationListener {


        String getIdCardName();

        String getIdCardNumber();


        void onShowAuthenticationStatusDialog(AuthenticationBean data);
    }
}
