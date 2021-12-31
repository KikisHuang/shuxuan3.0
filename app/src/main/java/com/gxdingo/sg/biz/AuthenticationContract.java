package com.gxdingo.sg.biz;

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

        void photoItemClick(int pos, int type);

        void submitAuthenticationInfo();
    }

    public interface AuthenticationListener {

        void upLoadSucceed(String path, int selectedType);

        /**
         * 识别信息返回
         *
         * @param data
         */
        void onOCRInfoResult(IdCardOCRBean data);

        String getIdCardName();

        String getIdCardNumber();

        void changeButtonStatus();

        void onOCRFailed(int type);
    }
}
