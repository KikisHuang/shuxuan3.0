package com.gxdingo.sg.presenter;

import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.AddressBean;
import com.gxdingo.sg.bean.CategoriesBean;
import com.gxdingo.sg.bean.CategoryListBean;
import com.gxdingo.sg.bean.StoreDetail;
import com.gxdingo.sg.bean.StoreListBean;
import com.gxdingo.sg.biz.ClientHomeContract;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.PermissionsListener;
import com.gxdingo.sg.model.ClientHomeModel;
import com.gxdingo.sg.model.ClientNearbyShopsModel;
import com.gxdingo.sg.model.ClientNetworkModel;
import com.gxdingo.sg.model.CommonModel;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.model.SelectAddressModel;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.bugly.proguard.C;
import com.zhouyou.http.subsciber.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.blankj.utilcode.util.StringUtils.getString;
import static com.blankj.utilcode.util.StringUtils.isEmpty;
import static com.gxdingo.sg.model.CommonModel.isLocServiceEnable;
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
 * @date: 2021/10/13
 * @page:
 */
public class ClientHomePresenter extends BaseMvpPresenter<BasicsListener, ClientHomeContract.ClientHomeListener> implements ClientHomeContract.ClientHomePresenter, NetWorkListener {

    private ClientHomeModel model;

    private ClientNetworkModel clientNetworkModel;

//    private NetworkModel mNetworkModel;

    private CommonModel commonModel;

    private List<String> searchHistory;

    private Gson gson;

    private double lon, lat;

    public ClientHomePresenter() {
        clientNetworkModel = new ClientNetworkModel(this);
//        mNetworkModel = new NetworkModel(this);
        commonModel = new CommonModel();
        model = new ClientHomeModel();
//        clientNearbyShopsModel = new ClientNearbyShopsModel();
    }

    @Override
    public void checkPermissions(RxPermissions rxPermissions) {
        if (commonModel!=null){
            commonModel.checkPermission(rxPermissions, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, new PermissionsListener() {
                @Override
                public void onNext(boolean value) {
                    if (isViewAttached() && isBViewAttached()) {
                        if (!value)
                            getBV().onFailed();
                        else
                            model.location(getContext(), aMapLocation -> {

                                if (aMapLocation.getErrorCode() == 0) {

                                    getV().setDistrict(aMapLocation.getDistrict());

                                    lat = aMapLocation.getLatitude();
                                    lon=aMapLocation.getLongitude();
                                    getNearbyStore(true,0);
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
    public void getCategory() {
        if (clientNetworkModel!=null)
            clientNetworkModel.getCategories(getContext());
    }

    @Override
    public void getNearbyStore(boolean refresh,int categoryId) {
        if (clientNetworkModel!=null)
            clientNetworkModel.getStoreList(getContext(),refresh,lon,lat,categoryId,"");
    }

    @Override
    public void getStoreDetails(int storeId) {
        if (clientNetworkModel!=null)
            clientNetworkModel.getStoreDetail(getContext(), String.valueOf(storeId), lon, lat, new CustomResultListener() {
                @Override
                public void onResult(Object o) {
                    StoreDetail storeDetail=(StoreDetail) o;
                    mapInit(storeDetail.getLatitude(),storeDetail.getLongitude());
                    getV().onStoreDetailResult(storeDetail);
                }
            });

    }

    @Override
    public void mapInit(double latitude, double longitude) {
        if (model!=null)
            model.mapInit(getV().getMap(),latitude,longitude);
    }

    @Override
    public void callStore(String s) {
        if (commonModel!=null)
            if (!isEmpty(s))
                commonModel.goCallPage(getContext(), s);
            else
                onMessage(gets(R.string.no_get_store_mobile_phone_number));
    }

    @Override
    public void goOutSideNavigation(int pos) {
        if (!isBViewAttached())
            return;

        switch (pos) {
            case 0:
                if (isAvilible(getContext(), PN_GAODE_MAP)) {
                    goToGaoDeMap(getContext(), "addressName", lon, lat);
                } else
                    getBV().onMessage(String.format(getString(R.string.uninstall_app), gets(R.string.gaode_map)));
                break;
            case 1:
                if (isAvilible(getContext(), PN_BAIDU_MAP))
                    goToBaiduActivity(getContext(), "addressName", lon, lat);
                else
                    getBV().onMessage(String.format(getString(R.string.uninstall_app), gets(R.string.baidu_map)));

                break;
            case 2:
                if (isAvilible(getContext(), PN_TENCENT_MAP)) {
                    goToTencentMap(getContext(), "addressName", lon, lat);
                } else
                    getBV().onMessage(String.format(getString(R.string.uninstall_app), gets(R.string.tencent_map)));
                break;

        }
    }

    @Override
    public void search(boolean refresh,int categoryId,String content) {
        if (clientNetworkModel!=null){
            saveSearch(content);
            clientNetworkModel.getStoreList(getContext(),refresh,lon,lat,categoryId,content);
        }

    }

    /**
     * 保存成历史搜索
     */
    public void saveSearch(String keyword) {
        if (!TextUtils.isEmpty(keyword)) {
            if (searchHistory == null) {
                searchHistory = getSearchHistory();
            }
            if (searchHistory != null) {
                if (searchHistory.size() > 0) {
                    searchHistory.remove(keyword);
                }
                searchHistory.add(0, keyword);
                if (searchHistory.size() > 12) {
                    searchHistory.remove(searchHistory.size() - 1);
                }
            }
            if (gson == null) {
                gson = new Gson();
            }
//            SPUtils.getInstance.(SEARCH_HISTORY, gson.toJson(searchHistory));
            SPUtils.getInstance().put("SEARCH_HISTORY",gson.toJson(searchHistory));

        }
    }

    public List<String> getSearchHistory() {
        try {
            String details = SPUtils.getInstance().getString("SEARCH_HISTORY");
            if (!TextUtils.isEmpty(details)) {
                if (gson == null) {
                    gson = new Gson();
                }
                return gson.fromJson(details, new TypeToken<List<String>>() {
                }.getType());
            } else {
                return new ArrayList<String>();
            }
        } catch (Exception e) {
            return new ArrayList<String>();
        }
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
        if (isViewAttached()){
            if (o instanceof CategoryListBean)
                getV().onCategoryResult(((CategoryListBean) o).getCategories());
            else if (o instanceof StoreListBean)
                getV().onStoresResult(refresh,((StoreListBean) o).getList());

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
