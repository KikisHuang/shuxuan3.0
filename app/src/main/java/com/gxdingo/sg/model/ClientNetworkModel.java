package com.gxdingo.sg.model;

import android.content.Context;

import com.amap.api.services.core.LatLonPoint;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.reflect.TypeToken;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.ClientSettingPayPwd1Activity;
import com.gxdingo.sg.bean.ArticleImage;
import com.gxdingo.sg.bean.HelpBean;
import com.gxdingo.sg.bean.RankListBean;
import com.gxdingo.sg.bean.ShareBean;
import com.kikis.commnlibrary.bean.AddressBean;
import com.gxdingo.sg.bean.AddressListBean;
import com.gxdingo.sg.bean.ArticleListBean;
import com.gxdingo.sg.bean.BankcardListBean;
import com.gxdingo.sg.bean.CategoryListBean;
import com.gxdingo.sg.bean.CheckPayPwdBean;
import com.gxdingo.sg.bean.ClientAccountTransactionBean;
import com.gxdingo.sg.bean.ClientCashInfoBean;
import com.gxdingo.sg.bean.ClientCouponsBean;
import com.gxdingo.sg.bean.ClientMineBean;
import com.gxdingo.sg.bean.NormalBean;
import com.gxdingo.sg.bean.StoreDetail;
import com.gxdingo.sg.bean.StoreListBean;
import com.gxdingo.sg.bean.ThirdPartyBean;
import com.gxdingo.sg.bean.UserBean;
import com.gxdingo.sg.bean.WebBean;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.http.HttpClient;
import com.gxdingo.sg.utils.ClientLocalConstant;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.view.MyBaseSubscriber;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.utils.Constant;
import com.zhouyou.http.callback.CallClazzProxy;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.ApiResult;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.ClipboardUtils.copyText;
import static com.blankj.utilcode.util.RegexUtils.isIDCard18;
import static com.blankj.utilcode.util.RegexUtils.isMobileSimple;
import static com.gxdingo.sg.http.Api.INVITE_HELP;
import static com.gxdingo.sg.http.Api.RANKING_LIST;
import static com.gxdingo.sg.http.Api.UPLOAD_INVITATIONCODE;
import static com.gxdingo.sg.http.Api.ADDRESS_ADD;
import static com.gxdingo.sg.http.Api.ADDRESS_ADDRESSES;
import static com.gxdingo.sg.http.Api.ADDRESS_DEFAULT;
import static com.gxdingo.sg.http.Api.ADDRESS_DELETE;
import static com.gxdingo.sg.http.Api.ADDRESS_UPDATE;
import static com.gxdingo.sg.http.Api.ARTICLE_DETAIL;
import static com.gxdingo.sg.http.Api.ARTICLE_IMAGE;
import static com.gxdingo.sg.http.Api.ARTICLE_LIST;
import static com.gxdingo.sg.http.Api.CATEGORY_CATEGORIES;
import static com.gxdingo.sg.http.Api.CHECK_PAY_PASSWORD;
import static com.gxdingo.sg.http.Api.COUPON_LIST;
import static com.gxdingo.sg.http.Api.COUPON_RECEIVE;
import static com.gxdingo.sg.http.Api.Cash_ACCOUNT_INFO;
import static com.gxdingo.sg.http.Api.HELP_AFTER;
import static com.gxdingo.sg.http.Api.INVITESELLER;
import static com.gxdingo.sg.http.Api.MINE_HOME;
import static com.gxdingo.sg.http.Api.STORE_DETAIL;
import static com.gxdingo.sg.http.Api.STORE_LIST;
import static com.gxdingo.sg.http.Api.TASK_COMPLETE;
import static com.gxdingo.sg.http.Api.TRANSACTION_RECORD;
import static com.gxdingo.sg.http.Api.USER_EDIT;
import static com.gxdingo.sg.http.Api.USER_MOBILE_CHANGE;
import static com.gxdingo.sg.http.Api.VOICE_TOKEN;
import static com.gxdingo.sg.http.Api.WALLET_BINDING;
import static com.gxdingo.sg.http.Api.WALLET_UNBINDING;
import static com.gxdingo.sg.http.Api.ADD_CARD;
import static com.gxdingo.sg.http.Api.BALANCE_CASH;
import static com.gxdingo.sg.http.Api.STORE_ACCOUNT;
import static com.gxdingo.sg.http.Api.SUPPORT_CARD_LIST;
import static com.gxdingo.sg.http.Api.UNBIND_BANK_CARD;
import static com.gxdingo.sg.http.Api.UPDATE_WITHDRAWAL_PASSWORD;
import static com.gxdingo.sg.utils.ClientLocalConstant.ALIPAY;
import static com.gxdingo.sg.utils.ClientLocalConstant.BANK;
import static com.gxdingo.sg.utils.ClientLocalConstant.COMPILEADDRESS_SUCCEED;
import static com.gxdingo.sg.utils.ClientLocalConstant.WECHAT;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.GsonUtil.getJsonMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;

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
     * ??????
     */
    public void nextPage() {
        mPage++;
    }


    public ClientNetworkModel(NetWorkListener netWorkListener) {
        this.netWorkListener = netWorkListener;
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
            //?????????????????????0?????????????????????????????????
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
     * ???????????????
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


        Map<String, String> map = getJsonMap();

        map.put(Constant.MOBILE, mobile);
        map.put(Constant.CODE, code);


        Observable<NormalBean> observable = HttpClient.post(USER_MOBILE_CHANGE, map)
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
     * ??????????????????
     *
     * @param context
     */
    public void getCategories(Context context) {

        if (netWorkListener != null)
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
                    netWorkListener.onData(true, categoryListBean);
                    netWorkListener.onAfters();
                }


            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }

    /**
     * ???????????? \ ???????????? \ ??????????????????
     *
     * @param context
     * @param lon
     * @param lat
     */
    public void getStoreList(Context context, boolean refresh, double lon, double lat, int categoryId, String key) {


        if (refresh)
            resetPage();

        Map<String, String> map = getJsonMap();

        if (UserInfoUtils.getInstance().isLogin())
            map.put(LocalConstant.IDENTIFIER, UserInfoUtils.getInstance().getIdentifier());

        if (categoryId > 0)
            map.put(ClientLocalConstant.CATEGORYID, String.valueOf(categoryId));

        if (!isEmpty(key))
            map.put("keyword", String.valueOf(key));


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
            }

            @Override
            public void onNext(StoreListBean storeListBean) {

                if (netWorkListener != null) {
                    if (storeListBean != null && storeListBean.getList() != null) {
                        pageNext(refresh, storeListBean.getList().size());
                        netWorkListener.onData(refresh, storeListBean);
                    }
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
     * @param context
     * @param mStoreId
     */
    public void getStoreDetail(Context context, String mStoreId, double la, double lon, CustomResultListener customResultListener) {

        if (netWorkListener != null)
            netWorkListener.onStarts();

        Map<String, String> map = getJsonMap();

        map.put(LocalConstant.IDENTIFIER, String.valueOf(mStoreId));

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
     * ????????????
     *
     * @param context
     * @param imageName
     */
    public void getArticleImage(Context context, String imageName) {

        if (netWorkListener != null)
            netWorkListener.onStarts();

        Map<String, String> map = getJsonMap();

        map.put(LocalConstant.IMAGE_NAME, imageName);

        Observable<ArticleImage> observable = HttpClient.post(ARTICLE_IMAGE, map)
                .execute(new CallClazzProxy<ApiResult<ArticleImage>, ArticleImage>(new TypeToken<ArticleImage>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<ArticleImage>(context) {
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
            public void onNext(ArticleImage articleImage) {

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    EventBus.getDefault().post(articleImage);
                }
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);


    }


    /**
     * ??????????????????
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
                }

            }

            @Override
            public void onNext(ClientMineBean mineBean) {

                if (netWorkListener != null) {
                    netWorkListener.onData(true, mineBean);
                    netWorkListener.onAfters();
                }


            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }

    /**
     * ??????????????????
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
//                    UserBean userInfo = UserInfoUtils.getInstance().getUserInfo();
//                    userInfo.setAvatar(userBean.getAvatar());
//                    userInfo.setNickname(userBean.getNickname());
                    UserInfoUtils.getInstance().saveUserAvatar(userBean.getAvatar());
                    UserInfoUtils.getInstance().saveUserNickName(userBean.getNickname());
                    netWorkListener.onMessage(gets(R.string.modify_succeed));
                    EventBus.getDefault().post(ClientLocalConstant.MODIFY_PERSONAL_SUCCESS);
                }

            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);

    }

    /**
     * ??????????????????
     *
     * @param context
     */
    public void getAccountTransaction(Context context, boolean refresh, int status, String date) {

        if (netWorkListener != null)
            netWorkListener.onStarts();

        if (refresh)
            resetPage();

        Map<String, String> map = getJsonMap();

        map.put(ClientLocalConstant.STATUS, String.valueOf(status));

        if (!isEmpty(date))
            map.put(ClientLocalConstant.DATE, date);

        map.put(LocalConstant.CURRENT, String.valueOf(getPage()));

        Observable<ClientAccountTransactionBean> observable = HttpClient.post(TRANSACTION_RECORD, map)
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
                    netWorkListener.onData(true, transactionBean);
                    netWorkListener.onAfters();
                    pageNext(refresh, transactionBean.getList().size());
                }

            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }


    /**
     * ??????????????????
     *
     * @param context
     */
    public void getCashInfo(Context context) {
        if (netWorkListener != null)
            netWorkListener.onStarts();

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
                }

            }

            @Override
            public void onNext(ClientCashInfoBean cashInfoBean) {

                if (netWorkListener != null) {
                    netWorkListener.onData(true, cashInfoBean);
                    netWorkListener.onAfters();
                }


            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }


    /**
     * ???????????????
     *
     * @param context
     */
    public void receiveCoupon(Context context, String activeCode, CustomResultListener customResultListener) {
        Map<String, String> map = getJsonMap();

        map.put(ClientLocalConstant.ACTIVE_CODE, activeCode);

        Observable<NormalBean> observable = HttpClient.post(COUPON_RECEIVE, map)
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
                    resetPage();
                }

            }

            @Override
            public void onNext(NormalBean normalBean) {
                if (customResultListener != null)
                    customResultListener.onResult(normalBean.successPrompt);
                 else if (netWorkListener != null) {
                    netWorkListener.onSucceed(ClientLocalConstant.FILL_SUCCESS);
                    EventBus.getDefault().post(ClientLocalConstant.FILL_SUCCESS);
                    netWorkListener.onAfters();
                }

            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }

    /**
     * ???????????????
     *
     * @param context
     */
    public void getCoupons(Context context, boolean refresh, String id) {

        if (refresh)
            resetPage();

        Map<String, String> map = getJsonMap();

        map.put(LocalConstant.CURRENT, String.valueOf(getPage()));

        if (!isEmpty(id))
            map.put("otherIdentifier", id);


        Observable<ClientCouponsBean> observable = HttpClient.post(COUPON_LIST, map)
                .execute(new CallClazzProxy<ApiResult<ClientCouponsBean>, ClientCouponsBean>(new TypeToken<ClientCouponsBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<ClientCouponsBean>(context) {
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
            public void onNext(ClientCouponsBean couponsBean) {

                if (netWorkListener != null) {
                    netWorkListener.onData(refresh, couponsBean);
                    netWorkListener.onAfters();
                    pageNext(refresh, couponsBean.getList().size());
                }

            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }

    /**
     * ??????????????????
     *
     * @param context
     */
    public void getBankList(Context context, boolean refresh, CustomResultListener customResultListener) {

        Map<String, String> map = getJsonMap();


        if (refresh)
            resetPage();

        map.put(LocalConstant.CURRENT, String.valueOf(getPage()));


        Observable<ClientCashInfoBean> observable = HttpClient.post(Cash_ACCOUNT_INFO, map)
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

                if (customResultListener != null)
                    customResultListener.onResult(cashInfoBean.getBankList());
                else {
                    if (netWorkListener != null)
                        netWorkListener.onData(refresh, cashInfoBean);
                }

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    pageNext(refresh, cashInfoBean.getBankList().size());
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
    public void balanceCash(Context context, int type, String amount, String withdrawalPassword, long bankCardId) {

        Map<String, String> map = getJsonMap();
        //0????????? 10?????? 20?????????
        switch (type) {
            case 0:
                map.put(LocalConstant.TYPE, BANK);
                break;
            case 10:
                map.put(LocalConstant.TYPE, WECHAT);
                break;
            case 20:
                map.put(LocalConstant.TYPE, ALIPAY);
                break;
        }

        map.put(ClientLocalConstant.WITHDRAWAL_PASSWORD, withdrawalPassword);

        map.put(ClientLocalConstant.AMOUNT, amount);

        if (type == 0 && bankCardId > 0)
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

                if (e.getCode() == 601)
                    goToPage(context, ClientSettingPayPwd1Activity.class, null);

                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
                    netWorkListener.onAfters();
                }
            }

            @Override
            public void onNext(NormalBean normalBean) {
                if (netWorkListener != null) {
                    netWorkListener.onSucceed(1);
                    netWorkListener.onAfters();
                    netWorkListener.onMessage("?????????????????????");
                }
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ??????????????????
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
                    netWorkListener.onData(refresh, addressListBean);
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
     * ??????????????????
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
     * ???????????????????????????
     *
     * @param addressDetail
     * @param contact
     * @param mobile
     * @param point
     * @param regionPath
     */
    public void addAddressInfo(Context context, boolean isAdd, int id, String doorplate, String addressDetail, String contact, String mobile, LatLonPoint point, String regionPath, String locationImage) {

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

        //map.put(Constant.GENDER, String.valueOf(gender));

        map.put(ClientLocalConstant.REGIONPATH, regionPath);

        map.put(ClientLocalConstant.STREET, addressDetail);

        map.put(ClientLocalConstant.DOORPLATE, doorplate);

        if (!isEmpty(locationImage))
            map.put(ClientLocalConstant.LOCATIONIMAGE, locationImage);

        //map.put(LocalConstant.TAG, labelString);

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
     * ????????????
     *
     * @param id
     */
    public void deleteAddress(Context context, int id) {


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
                    netWorkListener.onSucceed(ClientLocalConstant.DELADDRESS_SUCCEED);
                    EventBus.getDefault().post(ClientLocalConstant.REFRESH_ADDRESS_LIST);
                }

            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);

    }


    /**
     * ???????????????
     *
     * @param
     */
    public void checkPayPwd(Context context, String payPassword) {
        Map<String, String> map = new HashMap<>();
//        if (!isEmpty(oldPassword))
//            map.put(StoreLocalConstant.OLD_PASSWORD, oldPassword);
        map.put("oldPassword", payPassword);
        if (netWorkListener != null)
            netWorkListener.onStarts();
        Observable<CheckPayPwdBean> observable = HttpClient.post(CHECK_PAY_PASSWORD, map)
                .execute(new CallClazzProxy<ApiResult<CheckPayPwdBean>, CheckPayPwdBean>(new TypeToken<CheckPayPwdBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<CheckPayPwdBean>(context) {
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
            public void onNext(CheckPayPwdBean checkPayPwdBean) {
                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onSucceed(checkPayPwdBean.getAuthentication());
                }
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }

    /**
     * ??????????????????
     */
    public void updatePayPassword(Context context, String newPassword, String oldPassword, String code) {
        Map<String, String> map = new HashMap<>();


        if (!isEmpty(oldPassword) && (isEmpty(code)))
            map.put("oldPassword", oldPassword);

        map.put("newPassword", newPassword);
        netWorkListener.onStarts();
        Observable<NormalBean> observable = HttpClient.post(UPDATE_WITHDRAWAL_PASSWORD, map)
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
     * ???????????????
     *
     * @param
     */
    public void addBankCard(Context context, String bankType, String personOfCard, String idCard, String name, String number, String mobile, String smsCode) {

        if (isEmpty(bankType) || isEmpty(name)) {
            netWorkListener.onMessage("?????????????????????");
            return;
        } else if (isEmpty(personOfCard)) {
            netWorkListener.onMessage("???????????????????????????");
            return;
        } else if (!isIDCard18(idCard)) {
            netWorkListener.onMessage("????????????????????????????????????");
            return;
        } else if (isEmpty(number)) {
            netWorkListener.onMessage("???????????????????????????");
            return;
        } else if (mobile.length() < 11) {
            netWorkListener.onMessage("??????????????????????????????");
            return;
        } else if (smsCode.length() != 6) {
            netWorkListener.onMessage("?????????6?????????????????????");
            return;
        }
        Map<String, String> map = getJsonMap();

        map.put(LocalConstant.BANK_TYPE, bankType);
        map.put(LocalConstant.PERSON_OF_CARD, personOfCard);
        map.put(LocalConstant.ID_CARD, idCard);
        map.put(Constant.NAME, name);
        map.put(LocalConstant.NUMBER, number);
        map.put(Constant.MOBILE, mobile);
        map.put(LocalConstant.SMS_CODE, smsCode);

        netWorkListener.onStarts();

        Observable<NormalBean> observable = HttpClient.post(ADD_CARD, map)
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
                    netWorkListener.onSucceed(1);
                    netWorkListener.onAfters();
                }
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
    public void supportBank(Context context) {


        netWorkListener.onStarts();

        Observable<BankcardListBean> observable = HttpClient.post(SUPPORT_CARD_LIST)
                .execute(new CallClazzProxy<ApiResult<BankcardListBean>, BankcardListBean>(new TypeToken<BankcardListBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<BankcardListBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
                netWorkListener.onMessage(e.getMessage());
                netWorkListener.onAfters();

            }

            @Override
            public void onNext(BankcardListBean supportBankBean) {
                netWorkListener.onSucceed(1);
                netWorkListener.onAfters();
                netWorkListener.onData(true, supportBankBean);
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
    public void unbindBankCard(Context context, long id) {

        Map<String, String> map = new HashMap<>();
        map.put("bankCardId", String.valueOf(id));

        netWorkListener.onStarts();

        Observable<NormalBean> observable = HttpClient.post(UNBIND_BANK_CARD, map)
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
                netWorkListener.onSucceed(1);
                netWorkListener.onAfters();
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ??????????????????
     *
     * @param context
     * @param articleId
     * @param identifier
     */
    public void getArticleDetail(Context context, int articleId, String identifier) {
        Map<String, String> map = getJsonMap();

        if (netWorkListener != null) {
            netWorkListener.onStarts();
        }

        if (articleId > 0)
            map.put(LocalConstant.ARTICLEID, String.valueOf(articleId));

        if (!isEmpty(identifier))
            map.put(LocalConstant.IDENTIFIER, String.valueOf(identifier));

        Observable<WebBean> observable = HttpClient.post(ARTICLE_DETAIL, map)
                .execute(new CallClazzProxy<ApiResult<WebBean>, WebBean>(new TypeToken<WebBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<WebBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                }
            }

            @Override
            public void onNext(WebBean webBean) {

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onData(true, webBean);
                }
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);

    }

    /**
     * ??????????????????
     *
     * @param context
     * @param articleId
     * @param identifier
     */
    public void getArticleList(Context context, int articleId, String identifier) {
        Map<String, String> map = getJsonMap();

        if (netWorkListener != null) {
            netWorkListener.onStarts();
        }

        if (articleId > 0)
            map.put(LocalConstant.ARTICLEID, String.valueOf(articleId));

        if (!isEmpty(identifier))
            map.put(LocalConstant.IDENTIFIER, String.valueOf(identifier));

        Observable<ArticleListBean> observable = HttpClient.post(ARTICLE_LIST, map)
                .execute(new CallClazzProxy<ApiResult<ArticleListBean>, ArticleListBean>(new TypeToken<ArticleListBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<ArticleListBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                }
            }

            @Override
            public void onNext(ArticleListBean articleListBean) {

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onData(true, articleListBean);
                }
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
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
     * ???????????????????????????
     *
     * @param context
     * @param type
     */
    public void unbindThirdParty(Context context, int type) {

        Map<String, String> map = getJsonMap();

        if (netWorkListener != null) {
            netWorkListener.onStarts();
        }

//        map.put(Constant.CODE, code);

        if (type == 0)
            map.put(LocalConstant.APPNAME, ClientLocalConstant.ALIPAY);
        else if (type == 1)
            map.put(LocalConstant.APPNAME, ClientLocalConstant.WECHAT);
        Observable<NormalBean> observable = HttpClient.post(WALLET_UNBINDING, map)
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
                    netWorkListener.onSucceed(3);
                }
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);

    }

    /**
     * ??????????????????????????????
     *
     * @param context
     * @param helpCode
     */
    public void inviteHelp(Context context, String helpCode) {
        Map<String, String> map = getJsonMap();

        if (netWorkListener != null) {
            netWorkListener.onStarts();
        }

        map.put("helpCode", helpCode);

        Observable<HelpBean> observable = HttpClient.post(INVITE_HELP, map)
                .execute(new CallClazzProxy<ApiResult<HelpBean>, HelpBean>(new TypeToken<HelpBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<HelpBean>(context) {
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
            public void onNext(HelpBean helpBean) {

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    helpBean.setType(0);
                    netWorkListener.onData(true, helpBean);

                }
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);

    }

    /**
     * ??????????????????????????????
     *
     * @param context
     * @param helpCode
     */
    public void helpAfter(Context context, String helpCode) {
        Map<String, String> map = getJsonMap();

        if (netWorkListener != null) {
            netWorkListener.onStarts();
        }

        map.put("helpCode", helpCode);

        Observable<HelpBean> observable = HttpClient.post(HELP_AFTER, map)
                .execute(new CallClazzProxy<ApiResult<HelpBean>, HelpBean>(new TypeToken<HelpBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<HelpBean>(context) {
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
            public void onNext(HelpBean helpBean) {

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    helpBean.setType(1);
                    netWorkListener.onData(true, helpBean);

                }
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);

    }

    /**
     * ?????????????????????????????????
     *
     * @param context
     * @param identifier
     * @param completeType
     */
    public void completeTask(Context context, String identifier, int completeType) {
        Map<String, String> map = getJsonMap();

        if (netWorkListener != null) {
            netWorkListener.onStarts();
        }

        if (!isEmpty(identifier))
            map.put("identifier", identifier);

        //map.put(Constant.CODE, code);
        map.put("completeType", String.valueOf(completeType));

        Observable<NormalBean> observable = HttpClient.post(TASK_COMPLETE, map)
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
                    netWorkListener.onMessage("???????????????");
                }
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);

    }

    /**
     * ??????????????????
     *
     * @param context
     * @param customResultListener
     */
    public void getShareUrl(Context context, CustomResultListener customResultListener) {
        Map<String, String> map = getJsonMap();

//        map.put("identifier", String.valueOf());

        Observable<ShareBean> observable = HttpClient.post(INVITESELLER, map)
                .execute(new CallClazzProxy<ApiResult<ShareBean>, ShareBean>(new TypeToken<ShareBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<ShareBean>(context) {
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
            public void onNext(ShareBean normalBean) {

                if (netWorkListener != null) {
                    netWorkListener.onAfters();

                    if (customResultListener != null)
                        customResultListener.onResult(normalBean);
                }
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }

    /**
     * ??????????????????
     *
     * @param context
     * @param adCode
     */
    public void upLoadRegionCode(Context context, String adCode) {
        Map<String, String> map = getJsonMap();

        map.put("regionCode", String.valueOf(adCode));

        Observable<ShareBean> observable = HttpClient.post(UPLOAD_INVITATIONCODE, map)
                .execute(new CallClazzProxy<ApiResult<ShareBean>, ShareBean>(new TypeToken<ShareBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<ShareBean>(context) {
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
            public void onNext(ShareBean normalBean) {

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                }
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);

    }

    /**
     * ????????????token
     *
     * @param context
     */
    public void getVoiceToken(Context context, CustomResultListener customResultListener) {

//        if (netWorkListener != null)
//            netWorkListener.onStarts();


        Observable<UserBean> observable = HttpClient.post(VOICE_TOKEN)
                .execute(new CallClazzProxy<ApiResult<UserBean>, UserBean>(new TypeToken<UserBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<UserBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);

                if (netWorkListener != null) {
//                    netWorkListener.onAfters();
                    netWorkListener.onMessage(e.getMessage());
                }
                if (customResultListener != null)
                    customResultListener.onResult(null);

            }

            @Override
            public void onNext(UserBean userBean) {

                if (netWorkListener != null) {
//                    netWorkListener.onAfters();

                    if (customResultListener != null)
                        customResultListener.onResult(userBean.getToken());
                }
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }

    public void getRankingList(Context context, String cycle) {

        Map<String, String> map = getJsonMap();

        map.put("cycle", cycle);
//        map.put("activityIdentifier",);

        Observable<RankListBean> observable = HttpClient.post(RANKING_LIST, map)
                .execute(new CallClazzProxy<ApiResult<RankListBean>, RankListBean>(new TypeToken<RankListBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<RankListBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);

                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
                }
            }

            @Override
            public void onNext(RankListBean rankListBean) {

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onData(true, rankListBean);
                }


            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);

    }

}
