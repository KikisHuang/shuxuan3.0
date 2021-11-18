package com.gxdingo.sg.biz;

import com.gxdingo.sg.bean.DistanceBean;
import com.gxdingo.sg.bean.StoreDetailBean;
import com.gxdingo.sg.bean.StoreQRCodeBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

import java.util.List;

/**
 * @author: Weaving
 * @date: 2021/11/10
 * @page:
 */
public class StoreSettingsContract {

    public interface StoreSettingsPresenter extends MvpPresenter<BasicsListener,StoreSettingsListener>{

        void getStoreInfo();

        void photoItemClick(int pos);

        void getQrCode();

        void updateStoreName(String name);

        void changMobile(String mobile);

        void businessTime(String startTime,String endTime);

        void getDistanceList();

        void deliveryScope(String scope);

        void getAuthInfo();
    }

    public interface StoreSettingsListener{

        void onInfoResult(StoreDetailBean storeDetailBean);

        void onQRResult(StoreQRCodeBean qrCodeBean);

        void onDistanceResult(List<DistanceBean> distanceBeans);
    }
}
