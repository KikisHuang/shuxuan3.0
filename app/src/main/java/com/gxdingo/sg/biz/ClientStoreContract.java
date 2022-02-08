package com.gxdingo.sg.biz;

import com.amap.api.maps.AMap;
import com.gxdingo.sg.bean.StoreDetail;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;
import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * @author: Weaving
 * @date: 2021/10/31
 * @page:
 */
public class ClientStoreContract {
    public interface ClientStorePresenter extends MvpPresenter<BasicsListener,ClientStoreListener>{

        void getStoreDetail(RxPermissions rxPermissions, String storeId);

        void mapInit(double latitude,double longitude);

        void goOutSideNavigation(int pos);

        void callStore(String s);
    }

    public interface ClientStoreListener {

        void onStoreDetailResult(StoreDetail storeDetail);

        AMap getMap();
    }
}
