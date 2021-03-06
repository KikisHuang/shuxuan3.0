package com.gxdingo.sg.biz;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.gxdingo.sg.bean.ItemDistanceBean;
import com.kikis.commnlibrary.bean.AddressBean;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

/**
 * @author: Weaving
 * @date: 2021/10/15
 * @page:
 */
public class AddressContract {

    public interface AddressPresenter extends MvpPresenter<BasicsListener,AddressListener>{

        void getAddressList(boolean refresh);

        void compileOrAdd(boolean isAdd);

        void checkCompileInfo();

        void delAddress(int id);

        void checkPermissions(RxPermissions rxPermissions);

        void searchPOIAsyn(boolean refresh, String keyword, String cityCode);


        void loadmoreData(String key) ;

        void searchBound(boolean refresh, String keyword, String cityCode);


        void searchBound(boolean refresh, LatLng latLng, String cityCode) throws AMapException;

        void mapInit();

        void moveCamera(LatLng latLng);

        void cacheAddress(AddressBean item);

        void getLocationInfo(RxPermissions rxPermissions, boolean b);

        void goOutSideNavigation(int pos, ReceiveIMMessageBean.DataByType mDataByType);

        void callPhone(ReceiveIMMessageBean.DataByType mDataByType);

        void getDistance(double latitude, double longitude);

        void upLoadLocationImage(String fliepath);
    }

    public interface AddressListener {
        void onDataResult(boolean refresh, List<AddressBean> addressBeans);

        void setAddressData(AddressBean bean);

        void saveBtnEnable(boolean en);

        int getAddressId();

        String getAddress();

        String getAddressDetail();

        String getContact();

        String getMobile();

        String getRegionPath();

        LatLonPoint getPoint();

        void showDelDialog();

        String getDoorplate();

        void setCityName(String cityName);

        void searchResult(boolean refresh, List<PoiItem> poiItems,boolean isSearch);

        AMap getAMap();

        void onDistanceResult(ItemDistanceBean bean);

    }

    public interface AddressCompileModelListener {

        void isEnable(boolean en);

    }
}
