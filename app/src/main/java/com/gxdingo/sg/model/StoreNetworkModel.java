package com.gxdingo.sg.model;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.reflect.TypeToken;
import com.gxdingo.sg.bean.StoreBusinessScopeBean;
import com.gxdingo.sg.bean.UserBean;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.http.HttpClient;
import com.gxdingo.sg.view.MyBaseSubscriber;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.zhouyou.http.callback.CallClazzProxy;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.ApiResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

import static android.text.TextUtils.isEmpty;
import static com.gxdingo.sg.http.StoreApi.CATEGORY_LIST;
import static com.gxdingo.sg.http.StoreApi.USER_STATUS;

/**
 * @author: Weaving
 * @date: 2021/4/28
 * @page:
 */
public class StoreNetworkModel {

    protected int mPage = 1;

    protected int mPageSize = 10;

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

        if (refresh) {

            if (size < mPageSize)
                netWorkListener.finishRefreshWithNoMoreData();
            else {
                netWorkListener.resetNoMoreData();
                resetPage();
            }

            if (size <= 0)
                netWorkListener.noData();
            else {
                nextPage();
                netWorkListener.haveData();
            }

            netWorkListener.finishRefresh(true);
        } else {
            //请求的长度小于0，显示没有更多数据布局
            if (size < mPageSize)
                netWorkListener.finishLoadmoreWithNoMoreData();
            else {
                nextPage();
                netWorkListener.haveData();
            }


            netWorkListener.finishLoadmore(true);
        }
        netWorkListener.onRequestComplete();
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
//    public void settle(Context context, String avatar, List<StoreCategoryBean> storeCategory, String name, List<String> images, String regionPath,
////                       String address, String cardName, String idCard, String cardFront, String cardBack, String cardHands,
////                       String businessLicence, String licenceCode, String licenceName, String contactNumber, Double longitude,
////                       Double latitude) {
////
////        Map<String, String> map = new HashMap<>();
////        map.put(StoreLocalConstant.AVATAR, avatar);
////
////        if (images != null && images.size() > 0) {
////            map.put(LocalConstant.IMAGES, GsonUtil.gsonToStr(images));
////        }
////
////        map.put(StoreLocalConstant.STORE_CATEGORY, GsonUtil.gsonToStr(storeCategory));
////
////        map.put(NAME, name);
////
////        map.put(StoreLocalConstant.REGION_PATH, regionPath);
////        map.put(StoreLocalConstant.ADDRESS, address);
////        map.put(StoreLocalConstant.CARD_NAME, cardName);
////        map.put(ID_CARD, idCard);
//////        map.put(StoreLocalConstant.CARD_FRONT, cardFront);
//////        map.put(StoreLocalConstant.CARD_BACK, cardBack);
//////        map.put(StoreLocalConstant.CARD_HANDS, cardHands);
////        map.put(StoreLocalConstant.BUSINESS_LICENSE, businessLicence);
////        map.put(StoreLocalConstant.LICENSE_CODE, licenceCode);
////        map.put(StoreLocalConstant.LICENSE_NAME, licenceName);
////        map.put(StoreLocalConstant.CONTACT_NUMBER, contactNumber);
////        map.put(StoreLocalConstant.LONGITUDE, String.valueOf(longitude));
////        map.put(StoreLocalConstant.LATITUDE, String.valueOf(latitude));
////
////        Observable<NormalBean> observable = HttpClient.post(SETTLE, map)
////                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
////                }.getType()) {
////                });
////        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
////            @Override
////            public void onError(ApiException e) {
////                super.onError(e);
////                LogUtils.e(e);
////                netWorkListener.onMessage(e.getMessage());
////                netWorkListener.onAfters();
////            }
////
////            @Override
////            public void onNext(NormalBean normalBean) {
////                netWorkListener.onAfters();
////                netWorkListener.onSucceed(100);
////            }
////        };
////
////        observable.subscribe(subscriber);
////        netWorkListener.onDisposable(subscriber);
////    }



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
