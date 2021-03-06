package com.gxdingo.sg.model;

import android.content.Context;

import com.amap.api.services.core.PoiItem;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.reflect.TypeToken;
import com.gxdingo.sg.bean.DistanceListBean;
import com.gxdingo.sg.bean.NormalBean;
import com.gxdingo.sg.bean.StoreAuthInfoBean;
import com.gxdingo.sg.bean.StoreBusinessScopeBean;
import com.gxdingo.sg.bean.StoreCategoryBean;
import com.gxdingo.sg.bean.StoreDetail;
import com.gxdingo.sg.bean.StoreDetailBean;
import com.gxdingo.sg.bean.StoreMineBean;
import com.gxdingo.sg.bean.StoreQRCodeBean;
import com.gxdingo.sg.bean.StoreWalletBean;
import com.gxdingo.sg.bean.ThirdPartyBean;
import com.gxdingo.sg.bean.TransactionDetails;
import com.gxdingo.sg.bean.UserBean;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.http.HttpClient;
import com.gxdingo.sg.utils.ClientLocalConstant;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.SignatureUtils;
import com.gxdingo.sg.utils.StoreLocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.view.MyBaseSubscriber;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.utils.GsonUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallClazzProxy;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.ApiResult;
import com.zhouyou.http.model.HttpParams;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

import static android.text.TextUtils.isEmpty;
import static com.gxdingo.sg.http.Api.BALANCE_CASH;
import static com.gxdingo.sg.http.Api.CATEGORY_LIST;
import static com.gxdingo.sg.http.Api.CHECK_QUALIFICATION;
import static com.gxdingo.sg.http.Api.CONFIG_SCANNING_REMIND;
import static com.gxdingo.sg.http.Api.DELIVERY_SCOPE;
import static com.gxdingo.sg.http.Api.HTTP;
import static com.gxdingo.sg.http.Api.HTTPS;
import static com.gxdingo.sg.http.Api.L;
import static com.gxdingo.sg.http.Api.MINE_INFO;
import static com.gxdingo.sg.http.Api.SETTLE;
import static com.gxdingo.sg.http.Api.SM;
import static com.gxdingo.sg.http.Api.STORE_DETAIL;
import static com.gxdingo.sg.http.Api.STORE_QR_CODE;
import static com.gxdingo.sg.http.Api.STORE_SCAN_CODE;
import static com.gxdingo.sg.http.Api.STORE_UPDATE;
import static com.gxdingo.sg.http.Api.TRANSACTION_DETAILS;
import static com.gxdingo.sg.http.Api.UPDATE_QUALIFICATION;
import static com.gxdingo.sg.http.Api.USER_STATUS;
import static com.gxdingo.sg.http.Api.WALLET_BINDING;
import static com.gxdingo.sg.http.Api.WALLET_HOME;
import static com.gxdingo.sg.http.Api.isUat;
import static com.gxdingo.sg.http.HttpClient.getCurrentTimeUTCM;
import static com.gxdingo.sg.utils.LocalConstant.IDENTIFIER;
import static com.gxdingo.sg.utils.LocalConstant.TEST_HTTP_KEY;
import static com.gxdingo.sg.utils.LocalConstant.lat;
import static com.gxdingo.sg.utils.LocalConstant.lon;
import static com.kikis.commnlibrary.utils.Constant.isDebug;
import static com.kikis.commnlibrary.utils.GsonUtil.getJsonMap;

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
     * ??????
     */
    public void nextPage() {
        mPage++;
    }

    /**
     * ??????
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
     * ??????????????????
     *
     * @param refresh
     * @param size
     */
    public void pageNext(boolean refresh, int size) {
        if (netWorkListener == null)
            return;

        //??????
        if (refresh) {
            /**
             * ???????????????????????????????????????????????????
             */
            if (size < mPageSize)
                netWorkListener.finishRefreshWithNoMoreData();//???????????????????????????????????????
            else {
                netWorkListener.resetNoMoreData();//????????????????????????
                resetPage();//????????????
            }

            /**
             * ????????????????????????????????????
             */
            if (size <= 0)
                netWorkListener.noData();//???????????????????????????????????????????????????
            else {
                nextPage();//????????????????????????1
                netWorkListener.haveData();//???????????????????????????????????????
            }

            netWorkListener.finishRefresh(true);//????????????
        }
        //????????????
        else {
            //?????????????????????0?????????????????????????????????
            if (size < mPageSize)
                netWorkListener.finishLoadmoreWithNoMoreData();//???????????????????????????????????????
            else {
                nextPage();//????????????????????????1
                netWorkListener.haveData();//?????????
            }


            netWorkListener.finishLoadmore(true);//??????????????????
        }
        netWorkListener.onRequestComplete();//????????????
    }

    /**
     * ????????????
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
     * ????????????
     *
     * @param
     */
    public void businessScope(Context context) {
        if (netWorkListener != null)
            netWorkListener.onStarts();

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
                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
                    netWorkListener.onAfters();
                }
            }

            @Override
            public void onNext(StoreBusinessScopeBean businessScopeBean) {
                netWorkListener.onAfters();
                if (businessScopeBean != null) {
                    if (netWorkListener != null) {
                        netWorkListener.onData(true, businessScopeBean);
                    }
                }
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ????????????
     *
     * @param
     */
    public void settle(Context context, String avatar, String name, List<StoreCategoryBean> storeCategory
            , String regionPath, String address, String businessLicence, String storeLicence, double longitude, double latitude) {

        if (netWorkListener != null)
            netWorkListener.onStarts();

        Map<String, String> map = new HashMap<>();
        map.put(StoreLocalConstant.AVATAR, avatar);
        map.put(StoreLocalConstant.NAME, name);

        if (storeCategory != null)
            map.put(StoreLocalConstant.STORE_CATEGORY, GsonUtil.gsonToStr(storeCategory));

        map.put(StoreLocalConstant.REGION_PATH, regionPath);
        map.put(StoreLocalConstant.ADDRESS, address);
        map.put(StoreLocalConstant.BUSINESS_LICENCE, businessLicence);

        if (!isEmpty(storeLicence))
            map.put(StoreLocalConstant.STOREDOOR_PHOTO, storeLicence);

        map.put(StoreLocalConstant.LONGITUDE, String.valueOf(longitude));
        map.put(StoreLocalConstant.LATITUDE, String.valueOf(latitude));

        if (UserInfoUtils.getInstance().isLogin())
            map.put(IDENTIFIER, UserInfoUtils.getInstance().getIdentifier());


        Observable<NormalBean> observable = HttpClient.post(SETTLE, map)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
                    netWorkListener.onAfters();
                }
            }

            @Override
            public void onNext(NormalBean normalBean) {
                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onSucceed(100);
                }
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ????????????
     *
     * @param
     */
    public void getWalletHome(Context context, boolean refresh) {
        if (netWorkListener != null)
            netWorkListener.onStarts();

        Observable<StoreWalletBean> observable = HttpClient.post(WALLET_HOME)
                .execute(new CallClazzProxy<ApiResult<StoreWalletBean>, StoreWalletBean>(new TypeToken<StoreWalletBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<StoreWalletBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
                    netWorkListener.onAfters();
                    pageReset(refresh, e.getMessage());
                }
            }

            @Override
            public void onNext(StoreWalletBean storeWalletBean) {
                netWorkListener.onAfters();
                if (netWorkListener != null) {
                    netWorkListener.onData(true, storeWalletBean);
                    if (storeWalletBean.getTransactionList() != null)
                        pageNext(refresh, storeWalletBean.getTransactionList().size());
                }
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ?????????????????????
     *
     * @param
     */
    public void scanCode(Context context, String couponIdentifier) {
        if (netWorkListener != null)
            netWorkListener.onStarts();


        Map<String, String> map = new HashMap<>();
        map.put("couponIdentifier", couponIdentifier);

        Observable<NormalBean> observable = HttpClient.post(STORE_SCAN_CODE, map)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
                    netWorkListener.onAfters();
                }
            }

            @Override
            public void onNext(NormalBean normalBean) {
                netWorkListener.onAfters();
                if (netWorkListener != null) {
                    netWorkListener.onMessage("????????????????????????");
                }
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }


    /**
     * ????????????
     *
     * @param
     */
    public void balanceCash(Context context, String type, String amount, String withdrawalPassword, long bankCardId) {

        Map<String, String> map = getJsonMap();

        map.put(LocalConstant.TYPE, type);
        map.put(ClientLocalConstant.WITHDRAWAL_PASSWORD, withdrawalPassword);
        map.put(ClientLocalConstant.AMOUNT, amount);
        if (type.equals(ClientLocalConstant.BANK) && bankCardId > 0)
            map.put(ClientLocalConstant.BANK_CARD_ID, String.valueOf(bankCardId));

        netWorkListener.onStarts();

        Observable<NormalBean> observable = HttpClient.post(BALANCE_CASH, map)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
                    netWorkListener.onAfters();
                }
            }

            @Override
            public void onNext(NormalBean normalBean) {
                netWorkListener.onSucceed(1);
                netWorkListener.onAfters();
                EventBus.getDefault().post(LocalConstant.CASH_SUCCESSS);
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ?????????????????????????????????
     *
     * @param context
     * @param code
     * @param type
     */
    public void bindThirdParty(Context context, String code, int type) {
        Map<String, String> map = getJsonMap();

        if (netWorkListener != null) {
            netWorkListener.onStarts();
        }

        map.put(Constant.CODE, code);

        if (type == 0)
            map.put(LocalConstant.APPNAME, ClientLocalConstant.ALIPAY);
        else if (type == 1)
            map.put(LocalConstant.APPNAME, ClientLocalConstant.WECHAT);
        Observable<ThirdPartyBean> observable = HttpClient.post(WALLET_BINDING, map)
                .execute(new CallClazzProxy<ApiResult<ThirdPartyBean>, ThirdPartyBean>(new TypeToken<ThirdPartyBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<ThirdPartyBean>(context) {
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
            public void onNext(ThirdPartyBean thirdPartyBean) {

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
//                    netWorkListener.onData(true, thirdPartyBean);
                    thirdPartyBean.type = type;
                    EventBus.getDefault().post(thirdPartyBean);
                }
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);

    }

    /**
     * ????????????
     *
     * @param
     */
    public void getTransactionDetail(Context context, long moneyLogId) {
        if (netWorkListener != null)
            netWorkListener.onStarts();

        Map<String, String> map = getJsonMap();
        map.put("moneyLogId", String.valueOf(moneyLogId));

        Observable<TransactionDetails> observable = HttpClient.post(TRANSACTION_DETAILS, map)
                .execute(new CallClazzProxy<ApiResult<TransactionDetails>, TransactionDetails>(new TypeToken<TransactionDetails>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<TransactionDetails>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
                    netWorkListener.onAfters();
                }
            }

            @Override
            public void onNext(TransactionDetails transactionDetails) {
                netWorkListener.onAfters();
                if (netWorkListener != null) {
                    netWorkListener.onData(true, transactionDetails);

                }
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ????????????
     *
     * @param
     */
    public void getMine(Context context) {
        if (netWorkListener != null)
            netWorkListener.onStarts();

        Observable<StoreMineBean> observable = HttpClient.post(MINE_INFO)
                .execute(new CallClazzProxy<ApiResult<StoreMineBean>, StoreMineBean>(new TypeToken<StoreMineBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<StoreMineBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
                    netWorkListener.onAfters();
                }
            }

            @Override
            public void onNext(StoreMineBean storeMineBean) {
                netWorkListener.onAfters();
                if (netWorkListener != null) {
                    netWorkListener.onData(true, storeMineBean);
                }
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ?????????????????????
     *
     * @param
     */
    public void getExclusiveQRcode(Context context) {
        if (netWorkListener != null)
            netWorkListener.onStarts();

        Observable<StoreQRCodeBean> observable = HttpClient.post(STORE_QR_CODE)
                .execute(new CallClazzProxy<ApiResult<StoreQRCodeBean>, StoreQRCodeBean>(new TypeToken<StoreQRCodeBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<StoreQRCodeBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
                    netWorkListener.onAfters();
                }
            }

            @Override
            public void onNext(StoreQRCodeBean storeQRCodeBean) {
                netWorkListener.onAfters();
                if (netWorkListener != null) {
                    netWorkListener.onData(true, storeQRCodeBean);
                }
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ??????????????????
     *
     * @param
     */
    public void getStoreDetails(Context context, String id) {
        if (netWorkListener != null)
            netWorkListener.onStarts();

        Map<String, String> map = getJsonMap();

        map.put(LocalConstant.IDENTIFIER, id);

        if (lat != 0)
            map.put(LocalConstant.LATITUDE, String.valueOf(lat));
        if (lon != 0)
            map.put(LocalConstant.LONGITUDE, String.valueOf(lon));

        Observable<StoreDetail> observable = HttpClient.post(STORE_DETAIL, map)
                .execute(new CallClazzProxy<ApiResult<StoreDetail>, StoreDetail>(new TypeToken<StoreDetail>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<StoreDetail>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
                    netWorkListener.onAfters();
                }
            }

            @Override
            public void onNext(StoreDetail storeDetailBean) {
                netWorkListener.onAfters();
                if (netWorkListener != null) {
                    netWorkListener.onData(true, storeDetailBean);
                }
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ????????????
     *
     * @param
     */
    public void getDeliveryScope(Context context) {
        if (netWorkListener != null)
            netWorkListener.onStarts();

        Observable<DistanceListBean> observable = HttpClient.post(DELIVERY_SCOPE)
                .execute(new CallClazzProxy<ApiResult<DistanceListBean>, DistanceListBean>(new TypeToken<DistanceListBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<DistanceListBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
                    netWorkListener.onAfters();
                }
            }

            @Override
            public void onNext(DistanceListBean distanceListBean) {
                netWorkListener.onAfters();
                if (netWorkListener != null) {
                    netWorkListener.onData(true, distanceListBean);
                }
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ??????????????????
     *
     * @param
     */
    public void updateStoreAvatar(Context context, String avatar) {
        Map<String, String> map = new HashMap<>();
        map.put(StoreLocalConstant.AVATAR, avatar);

        Observable<NormalBean> observable = HttpClient.post(STORE_UPDATE, map)
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
                netWorkListener.onSucceed(1);
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ??????????????????
     *
     * @param
     */
    public void updateStoreName(Context context, String name) {
        Map<String, String> map = new HashMap<>();
        map.put(Constant.NAME, name);

        Observable<NormalBean> observable = HttpClient.post(STORE_UPDATE, map)
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
                netWorkListener.onSucceed(1);
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }


    /**
     * ??????????????????
     *
     * @param
     */
    public void updateShopAddress(Context context, PoiItem poiItem) {
        Map<String, String> map = new HashMap<>();
        map.put(StoreLocalConstant.REGION_PATH, poiItem.getCityCode());
        map.put(StoreLocalConstant.ADDRESS, poiItem.getAdName());
        map.put(StoreLocalConstant.LONGITUDE, String.valueOf(poiItem.getLatLonPoint().getLongitude()));
        map.put(StoreLocalConstant.LATITUDE, String.valueOf(poiItem.getLatLonPoint().getLatitude()));

        Observable<NormalBean> observable = HttpClient.post(STORE_UPDATE, map)
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
                netWorkListener.onSucceed(1);
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ????????????????????????
     *
     * @param
     */
    public void updateBusinessScope(Context context, String scope) {

        Map<String, String> map = new HashMap<>();
        map.put(StoreLocalConstant.BUSINESS_SCOPE, scope);

        Observable<NormalBean> observable = HttpClient.post(STORE_UPDATE, map)
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
                netWorkListener.onSucceed(1);
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ????????????????????????
     *
     * @param
     */
    public void updateMaxDistance(Context context, String scope) {

        Map<String, String> map = new HashMap<>();
        map.put(StoreLocalConstant.MAX_DISTANCE, scope);

        Observable<NormalBean> observable = HttpClient.post(STORE_UPDATE, map)
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
                netWorkListener.onSucceed(1);
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ????????????????????????
     *
     * @param
     */
    public void updateContractNumber(Context context, String mobile) {

        Map<String, String> map = new HashMap<>();
        map.put(StoreLocalConstant.CONTACT_NUMBER, mobile);

        Observable<NormalBean> observable = HttpClient.post(STORE_UPDATE, map)
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
                netWorkListener.onSucceed(1);
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ????????????????????????
     *
     * @param
     */
    public void updateBusinessTime(Context context, String startTime, String endTime) {

        Map<String, String> map = new HashMap<>();
        map.put(StoreLocalConstant.OPEN_TIME, startTime);
        map.put(StoreLocalConstant.CLOSE_TIME, endTime);

        Observable<NormalBean> observable = HttpClient.post(STORE_UPDATE, map)
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
                netWorkListener.onSucceed(1);
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ????????????????????????
     *
     * @param
     */
    public void updateContactPhone(Context context, String contactNumber) {

        Map<String, String> map = new HashMap<>();
        map.put(StoreLocalConstant.CONTACT_NUMBER, contactNumber);

        Observable<NormalBean> observable = HttpClient.post(STORE_UPDATE, map)
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
                netWorkListener.onSucceed(1);
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }


    /**
     * ??????????????????
     *
     * @param status
     */
    public void updateBusinessStatus(Context context, int status, CustomResultListener customResultListener) {

        Map<String, String> map = new HashMap<>();
        map.put(StoreLocalConstant.BUSINESSSTATUS, String.valueOf(status));
        netWorkListener.onStarts();
        Observable<NormalBean> observable = HttpClient.post(STORE_UPDATE, map)
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
                if (customResultListener != null)
                    customResultListener.onResult(status);

                if (UserInfoUtils.getInstance().getUserInfo() != null && UserInfoUtils.getInstance().getUserInfo().getStore() != null) {
                    UserBean userBean = UserInfoUtils.getInstance().getUserInfo();
                    userBean.getStore().setBusinessStatus(status);
                    UserInfoUtils.getInstance().saveUserInfo(userBean);
                }
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ????????????????????????
     */
    public void getAuthInfo(Context context,String identifier) {

        Map<String, String> map = new HashMap<>();
        map.put("identifier", identifier);

        Observable<StoreAuthInfoBean> observable = HttpClient.post(CHECK_QUALIFICATION,map)
                .execute(new CallClazzProxy<ApiResult<StoreAuthInfoBean>, StoreAuthInfoBean>(new TypeToken<StoreAuthInfoBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<StoreAuthInfoBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
                if (netWorkListener != null)
                    netWorkListener.onMessage(e.getMessage());
            }

            @Override
            public void onNext(StoreAuthInfoBean authInfoBean) {
                if (authInfoBean != null)
                    EventBus.getDefault().post(authInfoBean);
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ????????????????????????
     */
    public void getAuthInfo(Context context,String identifier,CustomResultListener customResultListener) {

        Map<String, String> map = new HashMap<>();
        map.put("identifier", identifier);

        Observable<StoreAuthInfoBean> observable = HttpClient.post(CHECK_QUALIFICATION,map)
                .execute(new CallClazzProxy<ApiResult<StoreAuthInfoBean>, StoreAuthInfoBean>(new TypeToken<StoreAuthInfoBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<StoreAuthInfoBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
                if (netWorkListener != null)
                    netWorkListener.onMessage(e.getMessage());
            }

            @Override
            public void onNext(StoreAuthInfoBean authInfoBean) {
                if (customResultListener != null)
                    customResultListener.onResult(authInfoBean);
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }
    /**
     * ????????????????????????
     *
     * @param context
     * @param customResultListener
     */
    public void refreshLoginStauts(Context context, CustomResultListener customResultListener) {

        if (netWorkListener != null)
            netWorkListener.onStarts();

        Map<String, String> map = new HashMap<>();

        if (UserInfoUtils.getInstance().getUserInfo() != null && UserInfoUtils.getInstance().isLogin())
            map.put(IDENTIFIER, UserInfoUtils.getInstance().getIdentifier());

        Observable<UserBean> observable = HttpClient.post(USER_STATUS, map)
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

    /**
     * ???????????????????????????????????????
     */
    public void getScanningInfo(Context context) {

        if (netWorkListener != null)
            netWorkListener.onStarts();

        Observable<NormalBean> observable = HttpClient.post(CONFIG_SCANNING_REMIND)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
                    netWorkListener.onAfters();
                }
            }

            @Override
            public void onNext(NormalBean normalBean) {
                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onData(true, normalBean);
                }
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }

    /**
     * ????????????????????????
     * @param context
     * @param list
     */
    public void upDateQulification(Context context,List<StoreAuthInfoBean.CategoryListBean> list) {

        Map<String, String> map = new HashMap<>();

        if (netWorkListener != null)
            netWorkListener.onStarts();

        map.put("storeCategory",GsonUtil.gsonToStr(list));

        Observable<NormalBean> observable = HttpClient.post(UPDATE_QUALIFICATION,map)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
                    netWorkListener.onAfters();
                }
            }

            @Override
            public void onNext(NormalBean normalBean) {
                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onSucceed(100);
                }
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }
}
