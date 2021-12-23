package com.gxdingo.sg.presenter;

import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.StoreDetail;
import com.gxdingo.sg.biz.ClientStoreContract;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.PermissionsListener;
import com.gxdingo.sg.model.ClientHomeModel;
import com.gxdingo.sg.model.ClientNetworkModel;
import com.gxdingo.sg.model.CommonModel;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhouyou.http.subsciber.BaseSubscriber;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.blankj.utilcode.util.StringUtils.getString;
import static com.blankj.utilcode.util.StringUtils.isEmpty;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.PN_BAIDU_MAP;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.PN_GAODE_MAP;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.PN_TENCENT_MAP;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.goToBaiduActivity;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.goToGaoDeMap;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.goToTencentMap;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.isAvilible;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;

/**
 * @author: Weaving
 * @date: 2021/10/31
 * @page:
 */
public class ClientStorePresenter extends BaseMvpPresenter<BasicsListener, ClientStoreContract.ClientStoreListener> implements ClientStoreContract.ClientStorePresenter, NetWorkListener {

    private CommonModel commonModel;

    private ClientHomeModel model;

    private ClientNetworkModel clientNetworkModel;

    private double lon, lat;

    private StoreDetail mStoreDetail;

    public ClientStorePresenter() {
        commonModel = new CommonModel();
        model = new ClientHomeModel();
        clientNetworkModel = new ClientNetworkModel(this);
    }

    @Override
    public void onSucceed(int type) {
        if (isBViewAttached())
            getBV().onSucceed(type);
    }

    @Override
    public void onMessage(String msg) {
        if (isBViewAttached())
            getBV().onMessage(msg);
    }

    @Override
    public void noData() {
        if (isBViewAttached())
            getBV().noData();
    }

    @Override
    public void onData(boolean refresh, Object o) {

    }

    @Override
    public void haveData() {
        if (isBViewAttached())
            getBV().haveData();
    }

    @Override
    public void finishLoadmoreWithNoMoreData() {
        if (isBViewAttached())
            getBV().finishLoadmoreWithNoMoreData();
    }

    @Override
    public void finishRefreshWithNoMoreData() {
        if (isBViewAttached())
            getBV().finishRefreshWithNoMoreData();
    }

    @Override
    public void onRequestComplete() {
        if (isBViewAttached())
            getBV().onRequestComplete();
    }

    @Override
    public void resetNoMoreData() {
        if (isBViewAttached())
            getBV().resetNoMoreData();
    }

    @Override
    public void finishRefresh(boolean success) {
        if (isBViewAttached())
            getBV().finishRefresh(success);
    }

    @Override
    public void finishLoadmore(boolean success) {
        if (isBViewAttached())
            getBV().finishLoadmore(success);
    }

    @Override
    public void onAfters() {
        if (isBViewAttached())
            getBV().onAfters();
    }

    @Override
    public void onStarts() {
        if (isBViewAttached())
            getBV().onStarts();
    }

    @Override
    public void onDisposable(BaseSubscriber subscriber) {
        addDisposable(subscriber);
    }

    @Override
    public void getStoreDetail(RxPermissions rxPermissions, long storeId) {
        if (commonModel != null && isViewAttached())
            commonModel.checkPermission(rxPermissions, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, new PermissionsListener() {
                @Override
                public void onNext(boolean value) {
                    if (!value) {
                        if (isBViewAttached())
                            getBV().onFailed();
                    } else {
                        model.location(getContext(), aMapLocation -> {

                            if (aMapLocation.getErrorCode() == 0) {
                                lat = aMapLocation.getLatitude();
                                lon = aMapLocation.getLongitude();

                                if (clientNetworkModel != null) {
                                    clientNetworkModel.getStoreDetail(getContext(), String.valueOf(storeId), lat, lon, o -> {
                                        StoreDetail storeDetail = (StoreDetail) o;
                                        mStoreDetail = storeDetail;
                                        mapInit(storeDetail.getLatitude(), storeDetail.getLongitude());
                                        getV().onStoreDetailResult(storeDetail);
                                    });
                                }
                            }
                        });
                    }
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
    }

    @Override
    public void mapInit(double latitude, double longitude) {
        if (model != null)
            model.mapInit(getV().getMap(), latitude, longitude);
    }

    @Override
    public void goOutSideNavigation(int pos) {
        if (!isBViewAttached() || mStoreDetail == null)
            return;

        switch (pos) {
            case 0:
                if (isAvilible(getContext(), PN_GAODE_MAP))
                    goToGaoDeMap(getContext(), mStoreDetail.getAddress(), mStoreDetail.getLongitude(), mStoreDetail.getLatitude());
                else
                    getBV().onMessage(String.format(getString(R.string.uninstall_app), gets(R.string.gaode_map)));
                break;
            case 1:
                if (isAvilible(getContext(), PN_BAIDU_MAP))
                    goToBaiduActivity(getContext(), mStoreDetail.getAddress(), mStoreDetail.getLongitude(), mStoreDetail.getLatitude());
                else
                    getBV().onMessage(String.format(getString(R.string.uninstall_app), gets(R.string.baidu_map)));
                break;
            case 2:
                if (isAvilible(getContext(), PN_TENCENT_MAP))
                    goToTencentMap(getContext(), mStoreDetail.getAddress(), mStoreDetail.getLongitude(), mStoreDetail.getLatitude());
                else
                    getBV().onMessage(String.format(getString(R.string.uninstall_app), gets(R.string.tencent_map)));
                break;

        }
    }

    @Override
    public void callStore(String s) {
        if (commonModel != null)
            if (!isEmpty(s))
                commonModel.goCallPage(getContext(), s);
            else
                onMessage(gets(R.string.no_get_store_mobile_phone_number));
    }
}
