package com.gxdingo.sg.model;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.reflect.TypeToken;
import com.gxdingo.sg.bean.BusinessDistrictListBean;
import com.gxdingo.sg.bean.NormalBean;
import com.gxdingo.sg.bean.StoreBusinessScopeBean;
import com.gxdingo.sg.bean.StoreCategoryBean;
import com.gxdingo.sg.bean.UserBean;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.http.HttpClient;
import com.gxdingo.sg.utils.StoreLocalConstant;
import com.gxdingo.sg.view.MyBaseSubscriber;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.utils.GsonUtil;
import com.zhouyou.http.callback.CallClazzProxy;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.ApiResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

import static android.text.TextUtils.isEmpty;
import static com.gxdingo.sg.http.StoreApi.BUSINESS_DISTRICT_LIST;
import static com.gxdingo.sg.http.StoreApi.CATEGORY_LIST;
import static com.gxdingo.sg.http.StoreApi.RELEASE_BUSINESS_DISTRICT_INFO;
import static com.gxdingo.sg.http.StoreApi.SETTLE;
import static com.gxdingo.sg.http.StoreApi.USER_STATUS;

/**
 * @author: Weaving
 * @date: 2021/4/28
 * @page:
 */
public class StoreNetworkModel {

    protected int mPage = 1;

    protected int mPageSize = 15;

    private NetWorkListener netWorkListener;

    public int getPage() {
        return mPage;
    }


    public StoreNetworkModel(NetWorkListener netWorkListener) {
        this.netWorkListener = netWorkListener;
    }


    /**
     * 翻页
     */
    public void nextPage() {
        mPage++;
    }

    /**
     * 重置
     */
    public void resetPage() {
        if (mPage > 1)
            setPage(1);
    }

    protected void setPage(int mPage) {
        this.mPage = mPage;
    }

    protected int getSize() {
        return mPageSize;
    }

    protected void setSize(int mSize) {
        this.mPageSize = mSize;
    }

    /**
     * 页数自动计算
     *
     * @param refresh
     * @param size
     */
    public void pageNext(boolean refresh, int size) {
        if (netWorkListener == null)
            return;

        //刷新
        if (refresh) {
            /**
             * 控制列表下拉刷新和上拉加载更多视图
             */
            if (size < mPageSize)
                netWorkListener.finishRefreshWithNoMoreData();//完成刷新并标记没有更多数据
            else {
                netWorkListener.resetNoMoreData();//重置没有更多数据
                resetPage();//重置页码
            }

            /**
             * 控制是否有数据显示的视图
             */
            if (size <= 0)
                netWorkListener.noData();//没有数据（显示传入的没有数据布局）
            else {
                nextPage();//有数据，页码累加1
                netWorkListener.haveData();//有数据（隐藏没有数据布局）
            }

            netWorkListener.finishRefresh(true);//完成刷新
        }
        //加载更多
        else {
            //请求的长度小于0，显示没有更多数据布局
            if (size < mPageSize)
                netWorkListener.finishLoadmoreWithNoMoreData();//完成加载并标记没有更多数据
            else {
                nextPage();//有数据，页码累加1
                netWorkListener.haveData();//有数据
            }


            netWorkListener.finishLoadmore(true);//完成加载更多
        }
        netWorkListener.onRequestComplete();//完成请求
    }

    /**
     * 页面重置
     *
     * @param refresh
     * @param msg
     */
    public void pageReset(boolean refresh, String msg) {

        if (netWorkListener == null)
            return;

        if (refresh) {
            resetPage();
            netWorkListener.resetNoMoreData();
            netWorkListener.finishRefresh(false);
        } else
            netWorkListener.finishLoadmore(false);

        if (!isEmpty(msg))
            netWorkListener.onMessage(msg);

        netWorkListener.onRequestComplete();
    }

    /**
     * 经营范围
     *
     * @param
     */
    public void businessScope(Context context) {

        Map<String, String> map = new HashMap<>();
        map.put("isStoreSelect", String.valueOf(1));

        Observable<StoreBusinessScopeBean> observable = HttpClient.post(CATEGORY_LIST, map)
                .execute(new CallClazzProxy<ApiResult<StoreBusinessScopeBean>, StoreBusinessScopeBean>(new TypeToken<StoreBusinessScopeBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<StoreBusinessScopeBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
                netWorkListener.onMessage(e.getMessage());
                netWorkListener.onAfters();
            }

            @Override
            public void onNext(StoreBusinessScopeBean businessScopeBean) {
                netWorkListener.onAfters();
                if (businessScopeBean != null)
                    netWorkListener.onData(true, businessScopeBean);
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * 商家入驻
     *
     * @param
     */
    public void settle(Context context, String avatar, String name, List<StoreCategoryBean> storeCategory
            , String regionPath, String address, String businessLicence, double longitude, double latitude) {

        Map<String, String> map = new HashMap<>();
        map.put(StoreLocalConstant.AVATAR, avatar);
        map.put(StoreLocalConstant.NAME, name);
        map.put(StoreLocalConstant.STORE_CATEGORY, GsonUtil.gsonToStr(storeCategory));
        map.put(StoreLocalConstant.REGION_PATH, regionPath);
        map.put(StoreLocalConstant.ADDRESS, address);
        map.put(StoreLocalConstant.BUSINESS_LICENCE, businessLicence);
        map.put(StoreLocalConstant.LONGITUDE, String.valueOf(longitude));
        map.put(StoreLocalConstant.LATITUDE, String.valueOf(latitude));


        Observable<NormalBean> observable = HttpClient.post(SETTLE, map)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
                netWorkListener.onMessage(e.getMessage());
                netWorkListener.onAfters();
            }

            @Override
            public void onNext(NormalBean normalBean) {
                netWorkListener.onAfters();
                netWorkListener.onSucceed(100);
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }


    /**
     * 店铺地址编辑
     *
     * @param
     */
//    public void updateShopAddress(Context context, PoiItem poiItem) {
//        Map<String, String> map = new HashMap<>();
//        map.put(StoreLocalConstant.REGION_PATH, poiItem.getCityCode());
//        map.put(StoreLocalConstant.ADDRESS, poiItem.getAdName());
//        map.put(StoreLocalConstant.LONGITUDE, String.valueOf(poiItem.getLatLonPoint().getLongitude()));
//        map.put(StoreLocalConstant.LATITUDE, String.valueOf(poiItem.getLatLonPoint().getLatitude()));
//
//        Observable<NormalBean> observable = HttpClient.post(STORE_UPDATE, map)
//                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
//                }.getType()) {
//                });
//        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
//            @Override
//            public void onError(ApiException e) {
//                super.onError(e);
//                LogUtils.e(e);
//                netWorkListener.onMessage(e.getMessage());
//                netWorkListener.onAfters();
//            }
//
//            @Override
//            public void onNext(NormalBean normalBean) {
//                netWorkListener.onAfters();
//                netWorkListener.onSucceed(1);
//            }
//        };
//
//        observable.subscribe(subscriber);
//        netWorkListener.onDisposable(subscriber);
//    }

    /**
     * 店铺经营范围设置
     *
     * @param
     */
//    public void updateBusinessScope(Context context, String scope) {
//
//        Map<String, String> map = new HashMap<>();
//        map.put(StoreLocalConstant.BUSINESS_SCOPE, scope);
//
//        Observable<NormalBean> observable = HttpClient.post(STORE_UPDATE, map)
//                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
//                }.getType()) {
//                });
//        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
//            @Override
//            public void onError(ApiException e) {
//                super.onError(e);
//                LogUtils.e(e);
//                netWorkListener.onMessage(e.getMessage());
//                netWorkListener.onAfters();
//            }
//
//            @Override
//            public void onNext(NormalBean normalBean) {
//                netWorkListener.onAfters();
//                netWorkListener.onSucceed(1);
//            }
//        };
//
//        observable.subscribe(subscriber);
//        netWorkListener.onDisposable(subscriber);
//    }

    /**
     * 店铺经营范围设置
     *
     * @param
     */
//    public void updateMaxDistance(Context context, String scope) {
//
//        Map<String, String> map = new HashMap<>();
//        map.put(StoreLocalConstant.MAX_DISTANCE, scope);
//
//        Observable<NormalBean> observable = HttpClient.post(STORE_UPDATE, map)
//                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
//                }.getType()) {
//                });
//        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
//            @Override
//            public void onError(ApiException e) {
//                super.onError(e);
//                LogUtils.e(e);
//                netWorkListener.onMessage(e.getMessage());
//                netWorkListener.onAfters();
//            }
//
//            @Override
//            public void onNext(NormalBean normalBean) {
//                netWorkListener.onAfters();
//                netWorkListener.onSucceed(1);
//            }
//        };
//
//        observable.subscribe(subscriber);
//        netWorkListener.onDisposable(subscriber);
//    }

    /**
     * 店铺营业时间修改
     *
     * @param
     */
//    public void updateBusinessTime(Context context, String startTime, String endTime) {
//
//        Map<String, String> map = new HashMap<>();
//        map.put(StoreLocalConstant.OPEN_TIME, startTime);
//        map.put(StoreLocalConstant.CLOSE_TIME, endTime);
//
//        Observable<NormalBean> observable = HttpClient.post(STORE_UPDATE, map)
//                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
//                }.getType()) {
//                });
//        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
//            @Override
//            public void onError(ApiException e) {
//                super.onError(e);
//                LogUtils.e(e);
//                netWorkListener.onMessage(e.getMessage());
//                netWorkListener.onAfters();
//            }
//
//            @Override
//            public void onNext(NormalBean normalBean) {
//                netWorkListener.onAfters();
//                netWorkListener.onSucceed(1);
//            }
//        };
//
//        observable.subscribe(subscriber);
//        netWorkListener.onDisposable(subscriber);
//    }

    /**
     * 店铺手机号码编辑
     *
     * @param
     */
//    public void updateContactPhone(Context context, String contactNumber) {
//
//        Map<String, String> map = new HashMap<>();
//        map.put(StoreLocalConstant.CONTACT_NUMBER, contactNumber);
//
//        Observable<NormalBean> observable = HttpClient.post(STORE_UPDATE, map)
//                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
//                }.getType()) {
//                });
//        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
//            @Override
//            public void onError(ApiException e) {
//                super.onError(e);
//                LogUtils.e(e);
//                netWorkListener.onMessage(e.getMessage());
//                netWorkListener.onAfters();
//            }
//
//            @Override
//            public void onNext(NormalBean normalBean) {
//                netWorkListener.onAfters();
//                netWorkListener.onSucceed(1);
//            }
//        };
//
//        observable.subscribe(subscriber);
//        netWorkListener.onDisposable(subscriber);
//    }


    /**
     * 绑定第三方提现账号
     *
     * @param
     */
//    public void walletBinding(Context context,String code,boolean isAli) {
//
//        Map<String, String> map = getJsonMap();
//        map.put(Constant.CODE,code);
//        if (isAli)
//            map.put("appName","alipay");
//        else
//            map.put("appName","wechat");
//        netWorkListener.onStarts();
//
//        Observable<NormalBean> observable = HttpClient.post(WALLET_BINDING,map)
//                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
//                }.getType()) {
//                });
//        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
//            @Override
//            public void onError(ApiException e) {
//                super.onError(e);
//                LogUtils.e(e);
//                netWorkListener.onMessage(e.getMessage());
//                netWorkListener.onAfters();
//
//            }
//
//            @Override
//            public void onNext(NormalBean normalBean) {
//                netWorkListener.onSucceed(isAli?0:1);
//                netWorkListener.onAfters();
//            }
//        };
//
//        observable.subscribe(subscriber);
//        netWorkListener.onDisposable(subscriber);
//    }


    /**
     * 刷新登录信息状态
     *
     * @param context
     * @param customResultListener
     */
    public void refreshLoginStauts(Context context, CustomResultListener customResultListener) {

        if (netWorkListener != null)
            netWorkListener.onStarts();

        Observable<UserBean> observable = HttpClient.post(USER_STATUS)
                .execute(new CallClazzProxy<ApiResult<UserBean>, UserBean>(new TypeToken<UserBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<UserBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onMessage(e.getMessage());
                }


            }

            @Override
            public void onNext(UserBean userBean) {
                if (customResultListener != null)
                    customResultListener.onResult(userBean);

                if (netWorkListener != null)
                    netWorkListener.onAfters();

            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }



}
