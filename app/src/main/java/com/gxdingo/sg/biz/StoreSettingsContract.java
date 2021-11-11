package com.gxdingo.sg.biz;

import com.gxdingo.sg.bean.StoreQRCodeBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

/**
 * @author: Weaving
 * @date: 2021/11/10
 * @page:
 */
public class StoreSettingsContract {

    public interface StoreSettingsPresenter extends MvpPresenter<BasicsListener,StoreSettingsListener>{
        void getQrCode();
    }

    public interface StoreSettingsListener{
        void onQRResult(StoreQRCodeBean qrCodeBean);
    }
}
