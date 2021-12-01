package com.gxdingo.sg.presenter;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.blankj.utilcode.util.LogUtils;
import com.gxdingo.sg.R;
import com.kikis.commnlibrary.bean.AddressBean;
import com.gxdingo.sg.bean.AddressListBean;
import com.gxdingo.sg.biz.AddressContract;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.PermissionsListener;
import com.gxdingo.sg.model.ClientNetworkModel;
import com.gxdingo.sg.model.CommonModel;
import com.gxdingo.sg.model.SelectAddressModel;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.kikis.commnlibrary.utils.BaseLogUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhouyou.http.subsciber.BaseSubscriber;

import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.text.TextUtils.isEmpty;
import static com.gxdingo.sg.utils.ClientLocalConstant.ADDADDRESS_SUCCEED;
import static com.gxdingo.sg.utils.ClientLocalConstant.COMPILEADDRESS_SUCCEED;
import static com.gxdingo.sg.utils.ClientLocalConstant.DELADDRESS_SUCCEED;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.Constant.isDebug;

/**
 * @author: Weaving
 * @date: 2021/10/15
 * @page:
 */
public class AddressPresenter extends BaseMvpPresenter<BasicsListener, AddressContract.AddressListener> implements AddressContract.AddressPresenter, NetWorkListener {

    private SelectAddressModel model;

    private CommonModel mClientCommonModel;

    private ClientNetworkModel clientNetworkModel;

    private AMapLocation mMapLocation;

    private long delId = 0;

    private String cityCode;

    private CameraPosition mCameraPosition;


    private boolean isPOIAsyn = false;

    public AddressPresenter() {
        model = new SelectAddressModel();
        mClientCommonModel = new CommonModel();
        clientNetworkModel = new ClientNetworkModel(this);
    }

    @Override
    public void getAddressList(boolean refresh) {
        if (clientNetworkModel != null)
            clientNetworkModel.getAddressList(getContext(), refresh, this);
    }

    @Override
    public void compileOrAdd(boolean isAdd) {
        if (!isViewAttached() || clientNetworkModel == null)
            return;

        clientNetworkModel.addAddressInfo(getContext(), isAdd, getV().getAddressId(), getV().getDoorplate(), getV().getAddressDetail(), getV().getContact(), getV().getMobile(), getV().getLabelString(), getV().getPoint(), getV().getGender(), getV().getRegionPath());

    }

    @Override
    public void checkCompileInfo() {
        if (model != null && isViewAttached())
            model.checkStatus(getV().getAddress(), getV().getAddressDetail(), getV().getContact(), getV().getMobile(), getV().getDoorplate(), b -> getV().saveBtnEnable(b));
    }

    @Override
    public void delAddress(int id) {
        if (!isViewAttached() || clientNetworkModel == null)
            return;

        delId = id;
        clientNetworkModel.deleteAddress(getContext(), id);
    }

    @Override
    public void checkPermissions(RxPermissions rxPermissions) {
        if (mClientCommonModel != null) {
            mClientCommonModel.checkPermission(rxPermissions, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, new PermissionsListener() {
                @Override
                public void onNext(boolean value) {
                    if (isViewAttached() && isBViewAttached()) {
                        if (!value)
                            getBV().onFailed();
                        else
                            model.location(getContext(), aMapLocation -> {

                                if (aMapLocation.getErrorCode() == 0) {
                                    mMapLocation = aMapLocation;
                                    getV().setCityName(aMapLocation.getCity());
                                    cityCode = aMapLocation.getCityCode();
                                    model.setCityCode(cityCode);
                                    mapInit();
                                /*    getBV().onStarts();
                                    searchBound(true, "");*/
                                } else {
                                    getBV().onMessage(aMapLocation.getLocationDetail());
                                    getBV().onFailed();
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
    }

    @Override
    public void searchPOIAsyn(boolean refresh, String keyword, String cityCode) {
        isPOIAsyn = true;

        if (refresh)
            clientNetworkModel.resetPage();

//        LogUtils.d("=========城市码"+cityCode);
//        if (!isEmpty(cityCode)) {

        BaseLogUtils.i("mNetworkModel.getPage() === " + clientNetworkModel.getPage());

        model.retrievalPOI(keyword, isEmpty(cityCode) ? cityCode : cityCode, new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int errorCode) {

                if (errorCode == 1000) {
                    if (isViewAttached()) {

                        getV().searchResult(refresh, poiResult.getPois());

                        clientNetworkModel.pageNext(refresh, poiResult.getPois().size());
                    }
                } else
                    clientNetworkModel.pageReset(refresh, "请求失败");
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
//        }
    }

    @Override
    public void loadmoreData(String key) {
        if (isPOIAsyn)
            searchPOIAsyn(false, key, cityCode);
        else
            searchBound(false, mCameraPosition.target, cityCode);
    }

    @Override
    public void searchBound(boolean refresh, String keyword, String cityCode) {
        if (mMapLocation != null) {
            searchBound(refresh, keyword, mMapLocation.getCityCode());
        }
    }

    @Override
    public void searchBound(boolean refresh, LatLng latLng, String cityCode) {
        BaseLogUtils.i("mNetworkModel.getPage() === " + clientNetworkModel.getPage());

        isPOIAsyn = false;
        model.retrievalBoundPOI("", cityCode, latLng.latitude, latLng.longitude, clientNetworkModel.getPage(), new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int errorCode) {
                if (errorCode == 1000) {
                    if (isViewAttached()) {
                        if (isBViewAttached())
                            getBV().onAfters();

                        getV().searchResult(refresh, poiResult.getPois());

                        clientNetworkModel.pageNext(refresh, poiResult.getPois().size());
                    }
                } else
                    clientNetworkModel.pageReset(refresh, "请求失败");
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
    }

    @Override
    public void mapInit() {
        if (model != null && isViewAttached()) {
            model.mapInit(getV().getAMap(), new AMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {

                }

                @Override
                public void onCameraChangeFinish(CameraPosition cameraPosition) {

                    mCameraPosition = cameraPosition;

                    BaseLogUtils.i("CameraPosition  latitude === " + cameraPosition.target.latitude);
                    BaseLogUtils.i("CameraPosition  longitude === " + cameraPosition.target.longitude);

                    if (model != null)
                        clientNetworkModel.resetPage();

                    if (isBViewAttached())
                        getBV().onStarts();


                    searchBound(true, mCameraPosition.target, cityCode);

                }
            });
        }
    }

    @Override
    public void moveCamera() {
        if (model != null && model.getMyLocation() != null)
            model.moveCamera(new LatLng(model.getMyLocation().getLatitude(), model.getMyLocation().getLongitude()));

    }

    @Override
    public void cacheAddress(AddressBean item) {
        if (mClientCommonModel != null)
            mClientCommonModel.cacheDefaultAddress(item);
    }

    @Override
    public void onSucceed(int type) {

        if (type == ADDADDRESS_SUCCEED)
            onMessage(gets(R.string.add_address_succeed));
        else if (type == DELADDRESS_SUCCEED) {
            //清除缓存地址
            if (mClientCommonModel != null && mClientCommonModel.getCacheDefaultAddress() != null && mClientCommonModel.getCacheDefaultAddress().getId() == delId)
                mClientCommonModel.clearCacheDefaultAddress();

            onMessage(gets(R.string.del_address_succeed));
        } else if (type == COMPILEADDRESS_SUCCEED)
            onMessage(gets(R.string.compile_address_succeed));

        if (isViewAttached())
            getBV().onSucceed(type);
//        if (isBViewAttached())
//            getBV().onSucceed(type);
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

        if (o instanceof AddressListBean) {
            if (isViewAttached())
                getV().onDataResult(refresh, (List<AddressBean>) ((AddressListBean) o).getList());
        }
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
}