package com.gxdingo.sg.model;

import android.content.Context;

import com.amap.api.services.core.LatLonPoint;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.reflect.TypeToken;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.AddressBean;
import com.gxdingo.sg.bean.AddressListBean;
import com.gxdingo.sg.bean.CategoryListBean;
import com.gxdingo.sg.bean.ClientAccountTransactionBean;
import com.gxdingo.sg.bean.ClientCashInfoBean;
import com.gxdingo.sg.bean.ClientMineBean;
import com.gxdingo.sg.bean.NormalBean;
import com.gxdingo.sg.bean.StoreDetail;
import com.gxdingo.sg.bean.StoreListBean;
import com.gxdingo.sg.bean.UserBean;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.http.HttpClient;
import com.gxdingo.sg.utils.ClientLocalConstant;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.StoreLocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.view.MyBaseSubscriber;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.utils.Constant;
import com.zhouyou.http.callback.CallClazzProxy;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.ApiResult;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.RegexUtils.isMobileSimple;
import static com.gxdingo.sg.http.ClientApi.ADDRESS_ADD;
import static com.gxdingo.sg.http.ClientApi.ADDRESS_ADDRESSES;
import static com.gxdingo.sg.http.ClientApi.ADDRESS_DEFAULT;
import static com.gxdingo.sg.http.ClientApi.ADDRESS_DELETE;
import static com.gxdingo.sg.http.ClientApi.ADDRESS_UPDATE;
import static com.gxdingo.sg.http.ClientApi.CATEGORY_CATEGORIES;
import static com.gxdingo.sg.http.ClientApi.Cash_ACCOUNT_INFO;
import static com.gxdingo.sg.http.ClientApi.MINE_HOME;
import static com.gxdingo.sg.http.ClientApi.STORE_DETAIL;
import static com.gxdingo.sg.http.ClientApi.STORE_LIST;
import static com.gxdingo.sg.http.ClientApi.TRANSACTION_RECORD;
import static com.gxdingo.sg.http.ClientApi.USER_EDIT;
import static com.gxdingo.sg.http.ClientApi.USER_MOBILE_CHANGE;
import static com.gxdingo.sg.utils.ClientLocalConstant.COMPILEADDRESS_SUCCEED;
import static com.gxdingo.sg.utils.LocalConstant.LOGIN_WAY;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.GsonUtil.getJsonMap;

/**
 * @author: Weaving
 * @date: 2021/10/15
 * @page:
 */
public class ClientNetworkModel {

    protected int mPage = 1;

    protected int mPageSize = 15;

    public int getPage() {
        return mPage;
    }

    private NetWorkListener netWorkListener;

    /**
     * 翻页
     */
    public void nextPage() {
        mPage++;
    }


    public ClientNetworkModel(NetWorkListener netWorkListener) {
        this.netWorkListener = netWorkListener;
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
     * 绑定新手机
     *
     * @param context
     * @param mobile
     * @param code
     */
    public void bindNewPhone(Context context, String mobile, String code) {

        if (isEmpty(mobile)) {
            netWorkListener.onMessage(gets(R.string.phone_number_can_not_null));
            return;
        } else if (isEmpty(code)) {
            netWorkListener.onMessage(gets(R.string.verification_code_can_not_null));
            return;
        }

        if (netWorkListener != null)
            netWorkListener.onStarts();

        boolean isUser = SPUtils.getInstance().getBoolean(LOGIN_WAY);

        Map<String, String> map = getJsonMap();

        if (isUser) {
            map.put(Constant.MOBILE, mobile);
        } else {
//            map.put(StoreLocalConstant.OLD_MOBILE, UserInfoUtils.getInstance().getUserPhone());
//            map.put(StoreLocalConstant.NEW_MOBILE, mobile);
        }
        map.put(Constant.CODE, code);


        Observable<NormalBean> observable = HttpClient.post(isUser ? USER_MOBILE_CHANGE : "USER_UPDATE_MOBILE", map)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
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
            public void onNext(NormalBean normalBean) {

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onMessage(gets(R.string.bind_succeed));
                    netWorkListener.onSucceed(LocalConstant.BIND_NEW_PHONE);
                }

            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);

    }

    /**
     * 获取地址列表
     *
     * @param context
     */
    public void getCategories(Context context) {
        netWorkListener.onStarts();

        Observable<CategoryListBean> observable = HttpClient.post(CATEGORY_CATEGORIES)
                .execute(new CallClazzProxy<ApiResult<CategoryListBean>, CategoryListBean>(new TypeToken<CategoryListBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<CategoryListBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
                    netWorkListener.onAfters();
                    resetPage();
                }

            }

            @Override
            public void onNext(CategoryListBean categoryListBean) {

                if (netWorkListener != null) {
                    netWorkListener.onData(true,categoryListBean);
                    netWorkListener.onAfters();
                }


            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }

    /**
     * 商家列表\附近商家
     *
     * @param context
     * @param lon
     * @param lat
     */
    public void getStoreList(Context context, boolean refresh, double lon, double lat, int categoryId, String key) {

        netWorkListener.onStarts();
        if (refresh)
            resetPage();

        Map<String, String> map = getJsonMap();

        if (UserInfoUtils.getInstance().isLogin())
            map.put(LocalConstant.IDENTIFIER, UserInfoUtils.getInstance().getIdentifier());

        if (categoryId > 0)
            map.put(ClientLocalConstant.CATEGORYID, String.valueOf(categoryId));

        if (!isEmpty(key))
            map.put(ClientLocalConstant.KEY, String.valueOf(key));


        map.put(LocalConstant.CURRENT, String.valueOf(getPage()));

//        map.put(LocalConstant.SIZE, String.valueOf(50));

        map.put(LocalConstant.LATITUDE, String.valueOf(lat));

        map.put(LocalConstant.LONGITUDE, String.valueOf(lon));


        Observable<StoreListBean> observable = HttpClient.post(STORE_LIST, map)
                .execute(new CallClazzProxy<ApiResult<StoreListBean>, StoreListBean>(new TypeToken<StoreListBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<StoreListBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);

                if (netWorkListener != null) {

                    netWorkListener.onMessage(e.getMessage());
                    resetPage();
                }
                netWorkListener.onAfters();
            }

            @Override
            public void onNext(StoreListBean storeListBean) {

                if (netWorkListener != null) {
                    if (storeListBean!=null&&storeListBean.getList()!=null){
                        netWorkListener.onData(refresh, storeListBean);
                        netWorkListener.onAfters();
                        pageNext(refresh, storeListBean.getList().size());
                    }
                }

                netWorkListener.onAfters();

            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }


    /**
     * 商家详情
     *
     * @param context
     * @param mStoreId
     */
    public void getStoreDetail(Context context, String mStoreId, double la, double lon, CustomResultListener customResultListener) {

        if (netWorkListener != null)
            netWorkListener.onStarts();

        Map<String, String> map = getJsonMap();

        map.put(LocalConstant.STOREID, String.valueOf(mStoreId));

        if (la != 0)
            map.put(LocalConstant.LATITUDE, String.valueOf(la));
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
                    netWorkListener.onAfters();
                    netWorkListener.onMessage(e.getMessage());
                }
            }

            @Override
            public void onNext(StoreDetail storeDetail) {

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    if (customResultListener != null) {
                        if (storeDetail.getLicence() != null && !isEmpty(storeDetail.getLicence().getBusinessLicence())) {
                            List<String> data = new ArrayList<>();
                            data.add(storeDetail.getLicence().getBusinessLicence());
//                            storeDetail.setInfo(data);
                        }
                        if (customResultListener != null)
                            customResultListener.onResult(storeDetail);
                    }
                }
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);


    }



    /**
     * 获取地址列表
     *
     * @param context
     */
    public void getMineData(Context context) {


        Observable<ClientMineBean> observable = HttpClient.post(MINE_HOME)
                .execute(new CallClazzProxy<ApiResult<ClientMineBean>, ClientMineBean>(new TypeToken<ClientMineBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<ClientMineBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
                    netWorkListener.onAfters();
                    resetPage();
                }

            }

            @Override
            public void onNext(ClientMineBean mineBean) {

                if (netWorkListener != null) {
                    netWorkListener.onData(true,mineBean);
                    netWorkListener.onAfters();
                }


            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }

    /**
     * 修改用户信息
     *
     * @param context
     * @param avatar
     * @param nickname
     */
    public void userEdit(Context context, String avatar, String nickname) {

        if (netWorkListener != null)
            netWorkListener.onStarts();

        Map<String, String> map = getJsonMap();

        if (!isEmpty(avatar))
            map.put(Constant.AVATAR, avatar);

        if (!isEmpty(nickname))
            map.put(Constant.NICKNAME, nickname);

        Observable<UserBean> observable = HttpClient.post(USER_EDIT, map)
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

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onMessage(gets(R.string.modify_succeed));
                }

            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);

    }

    /**
     * 获取账户记录
     *
     * @param context
     */
    public void getAccountTransaction(Context context,boolean refresh,int status,String date) {

        if (netWorkListener!=null)
            netWorkListener.onStarts();
        Map<String, String> map = getJsonMap();
        map.put(ClientLocalConstant.STATUS,String.valueOf(status));
        if (!isEmpty(date))
            map.put(ClientLocalConstant.DATE,String.valueOf(status));

        Observable<ClientAccountTransactionBean> observable = HttpClient.post(TRANSACTION_RECORD,map)
                .execute(new CallClazzProxy<ApiResult<ClientAccountTransactionBean>, ClientAccountTransactionBean>(new TypeToken<ClientAccountTransactionBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<ClientAccountTransactionBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
                    netWorkListener.onAfters();
                    resetPage();
                }

            }

            @Override
            public void onNext(ClientAccountTransactionBean transactionBean) {

                if (netWorkListener != null) {
                    netWorkListener.onData(true,transactionBean);
                    netWorkListener.onAfters();
                }


            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }


    /**
     * 获取提现信息
     *
     * @param context
     */
    public void getCashInfo(Context context) {


        Observable<ClientCashInfoBean> observable = HttpClient.post(Cash_ACCOUNT_INFO)
                .execute(new CallClazzProxy<ApiResult<ClientCashInfoBean>, ClientCashInfoBean>(new TypeToken<ClientCashInfoBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<ClientCashInfoBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
                    netWorkListener.onAfters();
                    resetPage();
                }

            }

            @Override
            public void onNext(ClientCashInfoBean cashInfoBean) {

                if (netWorkListener != null) {
                    netWorkListener.onData(true,cashInfoBean);
                    netWorkListener.onAfters();
                }


            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }

    /**
     * 获取地址列表
     *
     * @param context
     * @param refresh
     * @param netWorkListener
     */
    public void getAddressList(Context context, boolean refresh, NetWorkListener netWorkListener) {


        Observable<AddressListBean> observable = HttpClient.post(ADDRESS_ADDRESSES)
                .execute(new CallClazzProxy<ApiResult<AddressListBean>, AddressListBean>(new TypeToken<AddressListBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<AddressListBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
                    netWorkListener.onAfters();
                    resetPage();
                }

            }

            @Override
            public void onNext(AddressListBean addressListBean) {

                if (netWorkListener != null) {
                    netWorkListener.onData(refresh, addressListBean.getList());
                    netWorkListener.onAfters();
                    pageNext(refresh, addressListBean.getList().size());
                }


            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }

    /**
     * 获取默认地址
     *
     * @param context
     */
    public void getDefaultAddress(Context context, double lat, double lon, CustomResultListener customResultListener) {


        if (netWorkListener != null)
            netWorkListener.onStarts();

        Map<String, String> map = getJsonMap();

        map.put(LocalConstant.LATITUDE, String.valueOf(lat));

        map.put(LocalConstant.LONGITUDE, String.valueOf(lon));

        Observable<AddressBean> observable = HttpClient.post(ADDRESS_DEFAULT, map)
                .execute(new CallClazzProxy<ApiResult<AddressBean>, AddressBean>(new TypeToken<AddressBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<AddressBean>(context) {
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
            public void onNext(AddressBean addressBean) {

                if (netWorkListener != null) {
                    netWorkListener.onAfters();


                    if (customResultListener != null)
                        customResultListener.onResult(addressBean);
                }
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }

    /**
     * 新增或编辑收货地址
     *
     * @param addressDetail
     * @param contact
     * @param mobile
     * @param labelString
     * @param point
     * @param gender
     * @param regionPath
     */
    public void addAddressInfo(Context context, boolean isAdd, int id, String doorplate, String addressDetail, String contact, String mobile, String labelString, LatLonPoint point, int gender, String regionPath) {

        if (isEmpty(mobile) || !isMobileSimple(mobile)) {
            if (!isMobileSimple(mobile))
                netWorkListener.onMessage(gets(R.string.please_input_correct_phone_number));
            else
                netWorkListener.onMessage(gets(R.string.phone_number_can_not_null));
            return;
        } else if (isEmpty(contact)) {
            netWorkListener.onMessage(gets(R.string.contact_name_can_not_null));
            return;
        } else if (isEmpty(regionPath) || point == null) {
            netWorkListener.onMessage(gets(R.string.shipping_address_can_not_null));
            return;
        } else if (isEmpty(addressDetail)) {
            netWorkListener.onMessage(gets(R.string.address_detal_can_not_null));
            return;
        }

        if (netWorkListener != null)
            netWorkListener.onStarts();

        Map<String, String> map = getJsonMap();

        if (id > 0)
            map.put(Constant.ID, String.valueOf(id));

        map.put(Constant.MOBILE, mobile);

        map.put(Constant.NAME, contact);

        map.put(Constant.GENDER, String.valueOf(gender));

        map.put(ClientLocalConstant.REGIONPATH, regionPath);

        map.put(ClientLocalConstant.STREET, addressDetail);

        map.put(ClientLocalConstant.DOORPLATE, doorplate);

        map.put(LocalConstant.TAG, labelString);

        map.put(LocalConstant.LATITUDE, String.valueOf(point.getLatitude()));

        map.put(LocalConstant.LONGITUDE, String.valueOf(point.getLongitude()));

        Observable<NormalBean> observable = HttpClient.post(isAdd ? ADDRESS_ADD : ADDRESS_UPDATE, map)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
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
            public void onNext(NormalBean normalBean) {

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onSucceed(isAdd ? ClientLocalConstant.ADDADDRESS_SUCCEED : COMPILEADDRESS_SUCCEED);
                    EventBus.getDefault().post(ClientLocalConstant.REFRESH_ADDRESS_LIST);
                }

            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);

    }

    /**
     * 删除地址
     *
     * @param id
     */
    public void deleteAddress(Context context,  int id) {


        if (netWorkListener != null)
            netWorkListener.onStarts();

        Map<String, String> map = getJsonMap();


        map.put(ClientLocalConstant.ADDRESSID, String.valueOf(id));


        Observable<NormalBean> observable = HttpClient.post(ADDRESS_DELETE, map)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
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
            public void onNext(NormalBean normalBean) {

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onSucceed(ClientLocalConstant.DELADDRESS_SUCCEED );
                    EventBus.getDefault().post(ClientLocalConstant.REFRESH_ADDRESS_LIST);
                }

            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);

    }
}
