package com.gxdingo.sg.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.StoreCertificationActivity;
import com.gxdingo.sg.bean.HelpBean;
import com.gxdingo.sg.bean.OneKeyLoginEvent;
import com.gxdingo.sg.bean.ShareBean;
import com.gxdingo.sg.bean.UserBean;
import com.gxdingo.sg.bean.changeLocationEvent;
import com.gxdingo.sg.biz.OnCodeListener;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.model.OneKeyModel;
import com.gxdingo.sg.model.ShibbolethModel;
import com.gxdingo.sg.model.StoreNetworkModel;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.StoreLocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.activitiy.BaseActivity;
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
import com.kikis.commnlibrary.utils.RxUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.bugly.proguard.C;
import com.zhouyou.http.subsciber.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.blankj.utilcode.util.StringUtils.getString;
import static com.blankj.utilcode.util.StringUtils.isEmpty;
import static com.blankj.utilcode.util.StringUtils.null2Length0;
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

    private String helpCode;

    private List<StoreListBean.StoreBean> mHistoryList;

    //是否首页
    private boolean isHome = false;


    public ClientHomePresenter(boolean home) {
        isHome = home;
        clientNetworkModel = new ClientNetworkModel(this);

        storeNetworkModel = new StoreNetworkModel(this);

        commonModel = new CommonModel();

        model = new ClientHomeModel();

        mHistoryList = new ArrayList<>();
    }

    @Override
    public void checkPermissions(RxPermissions rxPermissions, boolean search) {
        if (commonModel != null) {
            commonModel.checkPermission(rxPermissions, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, new PermissionsListener() {
                @Override
                public void onNext(boolean value) {
                    if (isViewAttached() && isBViewAttached()) {
                        if (!value)
                            getBV().onFailed();
                        else {
                            model.location(getContext(), aMapLocation -> {

                                if (aMapLocation.getErrorCode() == 0) {
                                    if (search)
                                        getV().setDistrict(aMapLocation.getPoiName());

                                    lat = aMapLocation.getLatitude();
                                    lon = aMapLocation.getLongitude();

                                    LocalConstant.AdCode = aMapLocation.getAdCode();

                                    if (UserInfoUtils.getInstance().isLogin()) {
                                        UserBean userBean = UserInfoUtils.getInstance().getUserInfo();
                                        if (userBean.getRole() == 11) {
                                            //商家端先监测是否填写入驻信息
                                            if (userBean.getStore().getId() != 0 && userBean.getStore().getStatus() != 0 && userBean.getStore().getStatus() != 20)
                                                getNearbyStore(true, search, 0);
                                        } else
                                            getNearbyStore(true, search, 0);
                                    } else
                                        getNearbyStore(true, search, 0);

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
        if (clientNetworkModel != null)
            clientNetworkModel.getCategories(getContext());
    }

    @Override
    public void getNearbyStore(boolean refresh, boolean search, int categoryId) {
        searchModel = search;
        if (clientNetworkModel != null)
            clientNetworkModel.getStoreList(getContext(), refresh, lon, lat, categoryId, "");
    }

    @Override
    public void getNearbyStore(Object object, int categoryId) {

        if (object instanceof AddressBean) {
            AddressBean addressBean = (AddressBean) object;
            lon = addressBean.getLongitude();
            lat = addressBean.getLatitude();
            if (isViewAttached())
                getV().setDistrict(addressBean.getStreet());
        } else if (object instanceof changeLocationEvent) {
            changeLocationEvent changeLocationEvent = (com.gxdingo.sg.bean.changeLocationEvent) object;

            lon = changeLocationEvent.longitude;
            lat = changeLocationEvent.latitude;
            if (isViewAttached())
                getV().setDistrict(changeLocationEvent.name);
        }
        getNearbyStore(true, true, categoryId);
        if (!isHome)
            searchModel = false;
    }


    @Override
    public void fllInvitationCode(String code) {
        if (isEmpty(code)) {
            onMessage("请填写商家邀请码");
            return;
        }
        if (clientNetworkModel != null)
            clientNetworkModel.receiveCoupon(getContext(), code);
    }


    @Override
    public void callStore(String s) {
        if (commonModel != null)
            if (!isEmpty(s))
                commonModel.goCallPage(getContext(), s);
            else
                onMessage(gets(R.string.no_get_store_mobile_phone_number));
    }

    @Override
    public void convertStore() {
        if (storeNetworkModel != null)
            storeNetworkModel.refreshLoginStauts(getContext(), 0, new CustomResultListener() {
                @Override
                public void onResult(Object o) {
                    UserBean userBean = (UserBean) o;
                    if (userBean.getStore() != null) {
                        int status = userBean.getStore().getStatus();
                        if (userBean.getStore().getId() <= 0 || status == 20) {
                            goToPagePutSerializable(getContext(), StoreCertificationActivity.class, getIntentEntityMap(new Object[]{true}));
                        } else if (status == 0) {
                            onMessage("店铺审核中！");
                        } else if (status > 0)
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
    public void search(boolean refresh, boolean search, String content) {
        searchModel = search;
        if (clientNetworkModel != null) {
            if (isEmpty(content)) {
                onMessage("请输入搜索内容！");
                return;
            }
            saveSearch(content);
            clientNetworkModel.getStoreList(getContext(), refresh, lon, lat, 0, content);
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
    public void checkHelpCode() {
        if (UserInfoUtils.getInstance().isLogin()) {
            ShibbolethModel.checkShibboleth((type, code) -> {
                //30口令类型为邀请商家活动
                if (type != 30) {
                    helpCode = code;
                    clientNetworkModel.inviteHelp(getContext(), code);
                }

            }, 1000);
        }
//        ShibbolethModel.checkShibboleth(code -> {
//            if (clientNetworkModel!=null)
//                clientNetworkModel.inviteHelp(getContext(),code);
//        });

    }

    @Override
    public void help() {
        if (clientNetworkModel != null)
            clientNetworkModel.helpAfter(getContext(), helpCode);
    }

    /**
     * 获取分享连接
     */
    @Override
    public void getShareUrl() {
        if (clientNetworkModel != null)
            clientNetworkModel.getShareUrl(getContext(), o -> {
                if (o instanceof ShareBean)
                    getV().onShareUrlResult((ShareBean) o);
            });
    }

    /**
     * 重置页码 （搜索的数据已经没有了，重置页码继续加载推荐的附近商家列表数据）
     */
    @Override
    public void resetPage() {
        if (clientNetworkModel != null) {
            clientNetworkModel.resetPage();
            if (isBViewAttached())
                getBV().resetNoMoreData();
        }

    }

    @Override
    public void search(AddressBean addressBean, String content) {
        lon = addressBean.getLongitude();
        lat = addressBean.getLatitude();
        if (isViewAttached())
            getV().setDistrict(addressBean.getStreet());
        search(true, true, content);
    }

    @Override
    public void getSettleImage() {
        if (clientNetworkModel != null) {
            clientNetworkModel.getArticleImage(getContext(), "sxyg_invite_in_seller");
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

            SPUtils.getInstance().put("SEARCH_HISTORY", GsonUtil.gsonToStr(searchHistory));

        }
    }

    @Override
    public void getSearchHistory() {
        if (isViewAttached())
            try {
                String details = SPUtils.getInstance().getString("SEARCH_HISTORY", "");
                if (!isEmpty(details))
                    getV().onHistoryResult(GsonUtil.jsonToList(details, String.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public List<String> getSearch() {
        try {
            String details = SPUtils.getInstance().getString("SEARCH_HISTORY", "");
            if (!isEmpty(details)) {
                return GsonUtil.jsonToList(details, String.class);
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
        if (isViewAttached()) {
            if (o instanceof CategoryListBean)
                getV().onCategoryResult(((CategoryListBean) o).getCategories());
            else if (o instanceof StoreListBean) {
                StoreListBean storeListBean = (StoreListBean) o;

                //是否首页
                if (!isHome) {
                    if (refresh)
                        mHistoryList.clear();
                    //搜索页面历史记录
                    if (storeListBean.getList() != null && storeListBean.getList().size() > 0)
                        mHistoryList.addAll(storeListBean.getList());

                    if (!searchModel) {
                        //ClientSearchAcvtiity 附近商家列表返回
                        if (isViewAttached()) {
                            RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {
                                //判断是否第一次添加附近商家列表,如果是添加一个头部标识符
                                boolean isExist = false;

                                for (StoreListBean.StoreBean sb : mHistoryList) {
                                    if (sb.isShowTop() == true) {
                                        isExist = true;
                                        break;
                                    }
                                }
                                e.onNext(isExist);
                                e.onComplete();
                            }), (BaseActivity) getContext()).subscribe(data -> {

                                boolean flag = (boolean) data;
                                if (storeListBean.getList() != null && storeListBean.getList().size() > 0) {
                                    if (!flag)
                                        storeListBean.getList().get(0).setShowTop(true);
                                    getV().onStoresResult(refresh, searchModel, storeListBean.getList());
                                }
                            });
                        }
                    } else {
                        //ClientSearchAcvtiity 搜索列表返回
                        getV().onStoresResult(refresh, searchModel, storeListBean.getList());
                    }

                } else {
                    //Home首页 数据返回
                    getV().onStoresResult(refresh, searchModel, storeListBean.getList());

                    //首页banner
                    if (storeListBean.getAppHomeMiddle() != null)
                        getV().onBannerResult(storeListBean.getAppHomeMiddle());
                }

            } else if (o instanceof HelpBean) {
                getV().onHelpDataResult((HelpBean) o);
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
