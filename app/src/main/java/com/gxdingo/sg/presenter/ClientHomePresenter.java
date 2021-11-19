package com.gxdingo.sg.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.StoreCertificationActivity;
import com.gxdingo.sg.bean.OneKeyLoginEvent;
import com.gxdingo.sg.bean.UserBean;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.model.OneKeyModel;
import com.gxdingo.sg.model.StoreNetworkModel;
import com.gxdingo.sg.utils.StoreLocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.bean.AddressBean;
import com.gxdingo.sg.bean.CategoryListBean;
import com.gxdingo.sg.bean.StoreListBean;
import com.gxdingo.sg.biz.ClientHomeContract;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.PermissionsListener;
import com.gxdingo.sg.model.ClientHomeModel;
import com.gxdingo.sg.model.ClientNetworkModel;
import com.gxdingo.sg.model.CommonModel;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.kikis.commnlibrary.utils.GsonUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhouyou.http.subsciber.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.blankj.utilcode.util.StringUtils.getString;
import static com.blankj.utilcode.util.StringUtils.isEmpty;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientHomePresenter extends BaseMvpPresenter<BasicsListener, ClientHomeContract.ClientHomeListener> implements ClientHomeContract.ClientHomePresenter, NetWorkListener {

    private ClientHomeModel model;

    private ClientNetworkModel clientNetworkModel;

    private StoreNetworkModel storeNetworkModel;

    private CommonModel commonModel;

    private List<String> searchHistory;

    private double lon, lat;

    private boolean searchModel;

    public ClientHomePresenter() {
        clientNetworkModel = new ClientNetworkModel(this);

        storeNetworkModel = new StoreNetworkModel(this);

        commonModel = new CommonModel();

        model = new ClientHomeModel();

    }

    @Override
    public void checkPermissions(RxPermissions rxPermissions,boolean search) {
        if (commonModel!=null){
            commonModel.checkPermission(rxPermissions, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, new PermissionsListener() {
                @Override
                public void onNext(boolean value) {
                    if (isViewAttached() && isBViewAttached()) {
                        if (!value)
                            getBV().onFailed();
                        else{
                            model.location(getContext(), aMapLocation -> {

                                if (aMapLocation.getErrorCode() == 0) {

                                    getV().setDistrict(aMapLocation.getDistrict());

                                    lat = aMapLocation.getLatitude();
                                    lon=aMapLocation.getLongitude();
                                    getNearbyStore(true,search,0);
                                } else {
                                    getBV().onMessage(aMapLocation.getLocationDetail());
                                    getBV().onFailed();
                                }
                            });
                        }

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
    public void getNearbyStore(boolean refresh,boolean search,int categoryId) {
        searchModel = search;
        if (clientNetworkModel!=null)
            clientNetworkModel.getStoreList(getContext(),refresh,lon,lat,categoryId,"");
    }

    @Override
    public void getNearbyStore(AddressBean addressBean, int categoryId) {
        lon = addressBean.getLongitude();
        lat = addressBean.getLatitude();
        if (isViewAttached())
            getV().setDistrict(addressBean.getStreet());
        getNearbyStore(true,true,categoryId);
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
    public void convertStore() {
        if (storeNetworkModel!=null)
            storeNetworkModel.refreshLoginStauts(getContext(), 0,new CustomResultListener() {
                @Override
                public void onResult(Object o) {
                    UserBean userBean = (UserBean) o;
                    if (userBean.getStore()!=null ){
                        int status = userBean.getStore().getStatus();
                        if (userBean.getStore().getId()<=0 || status == 20){
                            goToPagePutSerializable(getContext(), StoreCertificationActivity.class,getIntentEntityMap(new Object[]{true}));
                        }else if (status==0){
                            onMessage("店铺审核中！");
                        }else if (status>0)
                            onMessage("该账户已有店铺！");
//                        if (status>0){
//                            onMessage("店铺审核中！");
//                        }else {
//                            onMessage("该账户已有店铺！");
//                        }
                    }
                }
            });
    }

    @Override
    public void search(boolean refresh,boolean search,String content) {
        searchModel = search;
        if (clientNetworkModel!=null){
            if (isEmpty(content)){
                onMessage("请输入搜索内容！");
                return;
            }
            saveSearch(content);
            clientNetworkModel.getStoreList(getContext(),refresh,lon,lat,0,content);
        }

    }

    @Override
    public void oauth(Context context) {
      /*  new OneKeyModel().getKey(getContext(), this, (CustomResultListener<OneKeyLoginEvent>) event -> {
            new NetworkModel(this).oneClickLogin(getContext(), event.code, event.isUser);
        });*/
        UserInfoUtils.getInstance().goToOauthPage(context);
    }

    @Override
    public void search(AddressBean addressBean, String content) {
        lon = addressBean.getLongitude();
        lat = addressBean.getLatitude();
        if (isViewAttached())
            getV().setDistrict(addressBean.getStreet());
        search(true,true,content);
    }

    @Override
    public void getSettleImage() {
        if (clientNetworkModel!=null){
            clientNetworkModel.getArticleImage(getContext(),"sxyg_invite_in_seller");
        }

    }

    /**
     * 保存成历史搜索
     */
    public void saveSearch(String keyword) {
        if (!TextUtils.isEmpty(keyword)) {
            if (searchHistory == null) {
                searchHistory = getSearch();
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

            SPUtils.getInstance().put("SEARCH_HISTORY",GsonUtil.gsonToStr(searchHistory));

        }
    }

    @Override
    public void getSearchHistory() {
        if (isViewAttached())
            try{
                String details = SPUtils.getInstance().getString("SEARCH_HISTORY","");
                if (!isEmpty(details))
                    getV().onHistoryResult(GsonUtil.jsonToList(details,String.class));
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    public List<String> getSearch() {
        try {
            String details = SPUtils.getInstance().getString("SEARCH_HISTORY","");
            if (!isEmpty(details)) {
                return GsonUtil.jsonToList(details,String.class);
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
            else if (o instanceof StoreListBean){
                StoreListBean storeListBean=(StoreListBean) o;
                if (refresh && !searchModel){
                    if (storeListBean.getList()!=null && storeListBean.getList().size()>0){
                        storeListBean.getList().get(0).setShowTop(true);
                        getV().onStoresResult(true,searchModel,storeListBean.getList());
                    }
                } else {
                    getV().onStoresResult(refresh,searchModel,storeListBean.getList());
                }
            }


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
