package com.gxdingo.sg.model;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DistanceItem;
import com.amap.api.services.route.DistanceSearch;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.esandinfo.livingdetection.bean.EsLivingDetectResult;
import com.google.gson.reflect.TypeToken;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.BindingPhoneActivity;
import com.gxdingo.sg.activity.ClientActivity;
import com.gxdingo.sg.activity.StoreActivity;
import com.gxdingo.sg.bean.AliVerifyBean;
import com.gxdingo.sg.bean.AuthenticationBean;
import com.gxdingo.sg.bean.CommonlyUsedStoreBean;
import com.gxdingo.sg.bean.IdCardOCRBean;
import com.gxdingo.sg.bean.ItemDistanceBean;
import com.gxdingo.sg.bean.LabelListBean;
import com.gxdingo.sg.bean.NormalBean;
import com.gxdingo.sg.bean.UpLoadBean;
import com.gxdingo.sg.bean.UserBean;
import com.gxdingo.sg.biz.GridPhotoListener;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.UpLoadImageListener;
import com.gxdingo.sg.http.HttpClient;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.StoreLocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.view.MyBaseSubscriber;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.utils.BaseLogUtils;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.utils.GsonUtil;
import com.kikis.commnlibrary.utils.RxUtil;
import com.luck.picture.lib.entity.LocalMedia;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.callback.CallClazzProxy;
import com.zhouyou.http.callback.DownloadProgressCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.ApiResult;
import com.zhouyou.http.model.HttpHeaders;
import com.zhouyou.http.request.DownloadRequest;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.RegexUtils.isMobileSimple;
import static com.blankj.utilcode.util.TimeUtils.getNowMills;
import static com.gxdingo.sg.http.Api.ALICLOUDAPI_VERIFY;
import static com.gxdingo.sg.http.Api.AUTHENTICATION_INIT;
import static com.gxdingo.sg.http.Api.AUTHENTICATION_VERIFY;
import static com.gxdingo.sg.http.Api.AUTHENTICATION_VERIFY2;
import static com.gxdingo.sg.http.Api.CHECK_CODE_SMS;
import static com.gxdingo.sg.http.Api.CIRCLE_LABEL;
import static com.gxdingo.sg.http.Api.COMPLAINT_MSG;
import static com.gxdingo.sg.http.Api.EXTRA_CERTIFICATION;
import static com.gxdingo.sg.http.Api.EXTRA_IDCARDOCR;
import static com.gxdingo.sg.http.Api.INVITATIONCODE;
import static com.gxdingo.sg.http.Api.ONE_CLICK_LOGIN;
import static com.gxdingo.sg.http.Api.OTHER_DISTANCE;
import static com.gxdingo.sg.http.Api.PAYMENT_ALIPAY_AUTHINFO;
import static com.gxdingo.sg.http.Api.SEND_SMS;
import static com.gxdingo.sg.http.Api.USER_LOGIN;
import static com.gxdingo.sg.http.Api.USER_LOGOFF;
import static com.gxdingo.sg.http.Api.USER_LOGOUT;
import static com.gxdingo.sg.http.Api.USER_OPEN_LOGIN;
import static com.gxdingo.sg.http.Api.getBatchUpLoadImage;
import static com.gxdingo.sg.http.Api.getUpLoadImage;
import static com.gxdingo.sg.utils.LocalConstant.ADD;
import static com.gxdingo.sg.utils.LocalConstant.COMPLAINT_SUCCEED;
import static com.gxdingo.sg.utils.PhotoUtils.getPhotoUrl;
import static com.gxdingo.sg.utils.StoreLocalConstant.INVITATION_CODE;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.CommonUtils.oneDecimal;
import static com.kikis.commnlibrary.utils.GsonUtil.getJsonMap;
import static com.kikis.commnlibrary.utils.GsonUtil.getObjMap;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;
import static com.kikis.commnlibrary.utils.MyToastUtils.customToast;

//import com.gxdingo.sg.activity.BindingPhoneActivity;

/**
 * @author: Kikis
 * @date: 2021/3/16
 * @page: ????????????model
 */
public class NetworkModel {

    protected int mPage = 1;

    protected int mPageSize = 10;

    public int getPage() {
        return mPage;
    }

    private NetWorkListener netWorkListener;


    //??????AMapLocationClient?????????
    public AMapLocationClient mLocationClient = null;

    //??????AMapLocationClientOption??????
    public AMapLocationClientOption mLocationOption = null;


    /**
     * ??????
     */
    public void nextPage() {
        mPage++;
    }


    public NetworkModel(NetWorkListener netWorkListener) {
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

    //????????????????????????
    public void location(Context context, AMapLocationListener mLocationListener) {
        //???????????????
        try {
            mLocationClient = new AMapLocationClient(context);
        } catch (Exception e) {
            e.printStackTrace();
            BaseLogUtils.e("AMapLocationClient == " + e);
        }

        mLocationClient.setLocationListener(mLocationListener);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setOnceLocation(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    /**
     * ??????????????????
     */
    public void deactivateLocation() {

        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    /**
     * ???????????????
     *
     * @param phone
     */
    public void sendSmsCode(Context context, String phone) {

        if (isEmpty(phone) || !isMobileSimple(phone)) {

            if (isEmpty(phone))
                netWorkListener.onMessage(gets(R.string.phone_number_can_not_null));
            else if (!isMobileSimple(phone))
                netWorkListener.onMessage(gets(R.string.please_input_correct_phone_number));

            return;
        }

        netWorkListener.onStarts();

        Map<String, String> map = getJsonMap();

        map.put(Constant.MOBILE, phone);


        Observable<NormalBean> observable = HttpClient.post(SEND_SMS, map)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);
                netWorkListener.onMessage(e.getMessage());
                netWorkListener.onAfters();

            }

            @Override
            public void onNext(NormalBean findBean) {

                if (netWorkListener != null) {
                    netWorkListener.onSucceed(LocalConstant.CODE_SEND);

                    netWorkListener.onAfters();
                    netWorkListener.onMessage(gets(R.string.captcha_code_sent));
                }
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }


    /**
     * ????????????????????????
     *
     * @param phone
     */
    public void sendSmsCodeResult(Context context, String phone, CustomResultListener resultListener) {

        if (isEmpty(phone) || !isMobileSimple(phone)) {

            if (isEmpty(phone))
                netWorkListener.onMessage(gets(R.string.phone_number_can_not_null));
            else if (!isMobileSimple(phone))
                netWorkListener.onMessage(gets(R.string.please_input_correct_phone_number));

            return;
        }

        if (netWorkListener != null)
            netWorkListener.onStarts();

        Map<String, String> map = getJsonMap();

        map.put(Constant.MOBILE, phone);

        Observable<NormalBean> observable = HttpClient.post(SEND_SMS, map)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);
                netWorkListener.onMessage(e.getMessage());
                netWorkListener.onAfters();

            }

            @Override
            public void onNext(NormalBean findBean) {

                SPUtils.getInstance().put(Constant.SMS_CODE_KEY, getNowMills());

                resultListener.onResult("");

                netWorkListener.onAfters();
                netWorkListener.onMessage(gets(R.string.captcha_code_sent));


            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ?????????????????????
     *
     * @param phone
     */
    public void checkSMSCode(Context context, String phone, String code, CustomResultListener customResultListener) {


        if (isEmpty(phone)) {
            netWorkListener.onMessage(gets(R.string.phone_number_can_not_null));
            return;
        } else if (!isMobileSimple(phone)) {
            netWorkListener.onMessage(gets(R.string.please_input_correct_phone_number));
            return;
        } else if (isEmpty(code)) {
            netWorkListener.onMessage(gets(R.string.verification_code_can_not_null));
            return;
        }

        netWorkListener.onStarts();

        Map<String, String> map = getJsonMap();

        map.put(Constant.MOBILE, phone);

//        if (SPUtils.getInstance().getBoolean(LocalConstant.LOGIN_WAY))
        map.put(Constant.CODE, code);
//        else
//            map.put(StoreLocalConstant.CAPTCHA, code);

        Observable<NormalBean> observable = HttpClient.post(CHECK_CODE_SMS, map)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);
                netWorkListener.onMessage(e.getMessage());
                netWorkListener.onAfters();

            }

            @Override
            public void onNext(NormalBean findBean) {

                if (customResultListener != null)
                    customResultListener.onResult("");

                netWorkListener.onAfters();
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ?????????????????????
     *
     * @param phone
     */
    public void storeCheckSMSCode(Context context, String phone, String code, CustomResultListener customResultListener) {


        if (isEmpty(phone)) {
            netWorkListener.onMessage(gets(R.string.phone_number_can_not_null));
            return;
        } else if (!isMobileSimple(phone)) {
            netWorkListener.onMessage(gets(R.string.please_input_correct_phone_number));
            return;
        } else if (isEmpty(code)) {
            netWorkListener.onMessage(gets(R.string.verification_code_can_not_null));
            return;
        }

        netWorkListener.onStarts();

        Map<String, String> map = getJsonMap();

        map.put(Constant.MOBILE, phone);
        map.put(StoreLocalConstant.CAPTCHA, code);


        Observable<NormalBean> observable = HttpClient.post(CHECK_CODE_SMS, map)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);
                netWorkListener.onMessage(e.getMessage());
                netWorkListener.onAfters();

            }

            @Override
            public void onNext(NormalBean findBean) {

                if (customResultListener != null)
                    customResultListener.onResult("");

                netWorkListener.onAfters();
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ????????????
     *
     * @param context
     * @param
     * @param
     */
    public void oneClickLogin(Context context, String accessToken) {

        if (netWorkListener != null)
            netWorkListener.onStarts();

        Map<String, String> map = getJsonMap();

        map.put("accessToken", accessToken);

        Observable<UserBean> observable = HttpClient.post(ONE_CLICK_LOGIN, map)
                .execute(new CallClazzProxy<ApiResult<UserBean>, UserBean>(new TypeToken<UserBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<UserBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onMessage(e.getMessage());
                }
                OneKeyModel.hideLoginLoading();
            }

            @Override
            public void onNext(UserBean userBean) {

                UserInfoUtils.getInstance().saveLoginUserInfo(userBean);

                if (netWorkListener != null)
                    netWorkListener.onAfters();

                if (netWorkListener != null) {
                    netWorkListener.onSucceed(LocalConstant.LOGIN_SUCCEED);
                    netWorkListener.onMessage(gets(R.string.login_succeed));
                    EventBus.getDefault().post(LocalConstant.LOGIN_SUCCEED);

//                    SPUtils.getInstance().put(LOGIN_WAY, isUse);//??????????????????

                    //?????????
                    if (ClientActivity.getInstance() == null)
                        goToPage(context, ClientActivity.class, null);
                }
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);

    }

    /**
     * ??????
     *
     * @param context
     * @param mobile
     * @param code
     */
    public void login(Context context, String mobile, String code) {

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

        Observable<UserBean> observable = HttpClient.post(USER_LOGIN, map)
                .execute(new CallClazzProxy<ApiResult<UserBean>, UserBean>(new TypeToken<UserBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<UserBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onMessage(e.getMessage());
                }
            }

            @Override
            public void onNext(UserBean userBean) {
                UserInfoUtils.getInstance().saveLoginUserInfo(userBean);

                if (netWorkListener != null)
                    netWorkListener.onAfters();

                if (netWorkListener != null) {
                    netWorkListener.onSucceed(LocalConstant.LOGIN_SUCCEED);
                    EventBus.getDefault().post(LocalConstant.LOGIN_SUCCEED);
                }

            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);

    }

    /**
     * ???????????????infoSt
     */
    public void getAliyPayAuthinfo(Context context, CustomResultListener customResultListener) {

        Observable<NormalBean> observable = HttpClient.post(PAYMENT_ALIPAY_AUTHINFO)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onMessage(e.getMessage());
                }
            }

            @Override
            public void onNext(NormalBean normalBean) {

                if (customResultListener != null)
                    customResultListener.onResult(normalBean.auth);
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener == null)
            netWorkListener.onDisposable(subscriber);
    }


    /**
     * ???????????????
     *
     * @param code
     * @param type
     */
    public void thirdPartyLogin(Context context, String code, String type) {

        if (isEmpty(code)) {
            if (netWorkListener != null)
                netWorkListener.onMessage(gets(R.string.phone_number_can_not_null));
            return;
        } else if (isEmpty(type)) {
            if (netWorkListener != null)
                netWorkListener.onMessage(gets(R.string.verification_code_can_not_null));
            return;
        }

        if (netWorkListener != null)
            netWorkListener.onStarts();

        Map<String, String> map = getJsonMap();

        map.put(LocalConstant.APPNAME, type);
        map.put(Constant.CODE, code);

        Observable<UserBean> observable = HttpClient.post(USER_OPEN_LOGIN, map)
                .execute(new CallClazzProxy<ApiResult<UserBean>, UserBean>(new TypeToken<UserBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<UserBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onMessage(e.getMessage());
                }
            }

            @Override
            public void onNext(UserBean userBean) {
                if (netWorkListener == null)
                    return;
                netWorkListener.onAfters();
                //0???????????????
                if (userBean.getIsBindMobile() == 0) {

                    netWorkListener.onMessage(gets(R.string.please_bind_phone));
                    netWorkListener.onSucceed(LocalConstant.BIND_PHONE);
                    goToPagePutSerializable(context, BindingPhoneActivity.class, getIntentEntityMap(new Object[]{userBean.getOpenid(), type}));
                } else {
                    UserInfoUtils.getInstance().saveLoginUserInfo(userBean);
                    netWorkListener.onSucceed(LocalConstant.LOGIN_SUCCEED);
                    EventBus.getDefault().post(LocalConstant.LOGIN_SUCCEED);
                }
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener == null)
            netWorkListener.onDisposable(subscriber);
    }

    /**
     * ????????????
     *
     * @param context
     * @param mobile
     * @param code
     * @param openid
     * @param appname
     */
    public void bind(Context context, String mobile, String code, String openid, String appname) {


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
        map.put(LocalConstant.APPNAME, appname);
        map.put(Constant.OPENID, openid);

        Observable<UserBean> observable = HttpClient.post(USER_LOGIN, map)
                .execute(new CallClazzProxy<ApiResult<UserBean>, UserBean>(new TypeToken<UserBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<UserBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onMessage(e.getMessage());
                }
            }

            @Override
            public void onNext(UserBean userBean) {

                UserInfoUtils.getInstance().saveLoginUserInfo(userBean);

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onMessage(gets(R.string.bind_succeed));

                    netWorkListener.onSucceed(LocalConstant.LOGIN_SUCCEED);
                    EventBus.getDefault().post(LocalConstant.LOGIN_SUCCEED);

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
     */
    public void logOut(Context context) {

        if (netWorkListener != null)
            netWorkListener.onStarts();

        Observable<NormalBean> observable = HttpClient.post(USER_LOGOUT)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onMessage(e.getMessage());
                }
            }

            @Override
            public void onNext(NormalBean normalBean) {
                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onSucceed(LocalConstant.LOGOUT_SUCCEED);
                    EventBus.getDefault().post(LocalConstant.LOGOUT_SUCCEED);
                }
                UserInfoUtils.getInstance().clearLoginStatus();

      /*          new OneKeyModel().getKey(context, netWorkListener, (CustomResultListener<OneKeyLoginEvent>) event -> {
                    new NetworkModel(netWorkListener).oneClickLogin(context, event.code, event.isUser);
                });*/
                UserInfoUtils.getInstance().goToOauthPage(context);

            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);

    }

    /**
     * ??????
     *
     * @param context
     * @param cancel  1=?????????????????? 0=????????????
     */
    public void logOff(Context context, int cancel) {

        if (netWorkListener != null)
            netWorkListener.onStarts();

        Map<String, String> map = getJsonMap();

        map.put("cancel", String.valueOf(cancel));

        Observable<NormalBean> observable = HttpClient.post(USER_LOGOFF, map)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onMessage(e.getMessage());
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


    /**
     * ????????????
     *
     * @param context
     * @param path
     * @param certFlag ??????????????????????????? 0??? 1??? :??????0
     */
    public void upLoadImage(Context context, String path, UpLoadImageListener loadImageListener, int certFlag) {

        if (netWorkListener != null)
            netWorkListener.onStarts();

        Map<String, Object> map = getObjMap();

        map.put(Constant.FILE, new File(path));

        if (certFlag > 0)
            map.put(LocalConstant.CERTFLAG, certFlag);

        Observable<NormalBean> observable = HttpClient.postUpLoad(getUpLoadImage(), map, null)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onMessage(e.getMessage());
                }
            }

            @Override
            public void onNext(NormalBean normalBean) {

                if (netWorkListener != null)
                    netWorkListener.onAfters();

                if (loadImageListener != null)
                    loadImageListener.loadSucceed(normalBean.url);
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);

    }


    /**
     * ????????????
     *
     * @param path
     * @param callBack
     * @param mDownloadDisp
     * @param filePath
     * @param fileName
     */
    public void downloadFile(String path, DownloadProgressCallBack callBack, Disposable mDownloadDisp, String filePath, String fileName) {

        DownloadRequest request = EasyHttp.downLoad(path)
                .savePath(filePath)
                .saveName(fileName);//??????????????????????????????????????????

        mDownloadDisp = request.execute(callBack);
    }

    /**
     * ????????????
     */
    public void gridPhotodataManager(List<LocalMedia> data, LifecycleProvider lifecycleProvider, int limit, GridPhotoListener listener) {

        RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getId() == ADD && i != data.size() - 1)
                    data.remove(i);
            }
            e.onNext(0);
            e.onComplete();
        }), lifecycleProvider).subscribe(o -> {
            if (data.size() < limit && data.get(data.size() - 1).getId() != ADD) {
                //????????????
                LocalMedia localMedia = new LocalMedia();
                localMedia.setId(ADD);
                data.add(localMedia);
            }

            listener.onSucceed();
        });


    }

    /**
     * ??????????????????
     *
     * @param context
     * @param newData
     * @param loadImageListener
     */
    public void upLoadImages(Context context, List<LocalMedia> newData, UpLoadImageListener loadImageListener) {

        List<File> files = new ArrayList<>();

        try {
            for (LocalMedia lm : newData) {
                files.add(new File(getPhotoUrl(lm)));
            }
        } catch (Exception e) {
            customToast("????????????????????????");
            return;
        }
        if (netWorkListener != null)
            netWorkListener.onStarts();

        Map<String, Object> map = getObjMap();

        map.put(Constant.FILES, files);

        Observable<UpLoadBean> observable = HttpClient.postUpLoad(getBatchUpLoadImage(), map, (done, progress) -> {

        })
                .execute(new CallClazzProxy<ApiResult<UpLoadBean>, UpLoadBean>(new TypeToken<UpLoadBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<UpLoadBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);
                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onMessage(e.getMessage());
                }


            }

            @Override
            public void onNext(UpLoadBean upLoadBean) {
                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                }
                if (loadImageListener != null)
                    loadImageListener.loadSucceed(upLoadBean);
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);

    }

    /**
     * ????????????????????????
     *
     * @param context
     * @param longitude
     * @param latitude
     * @param list
     */
    public void aMapdistanceSearch(Context context, double longitude, double latitude, List<CommonlyUsedStoreBean> list, DistanceSearch.OnDistanceSearchListener distanceSearchListener) {
        try {
            DistanceSearch distanceSearch = new DistanceSearch(context);

            distanceSearch.setDistanceSearchListener(distanceSearchListener);

            DistanceSearch.DistanceQuery distanceQuery = new DistanceSearch.DistanceQuery();

            LatLonPoint dest = new LatLonPoint(latitude, longitude);

            List<LatLonPoint> latLonPoints = new ArrayList<LatLonPoint>();

            for (CommonlyUsedStoreBean commonlyUsedStoreBean : list) {
                LatLonPoint start = new LatLonPoint(commonlyUsedStoreBean.getLatitude(), commonlyUsedStoreBean.getLongitude());
                latLonPoints.add(start);
            }

            //???????????????????????????
            distanceQuery.setOrigins(latLonPoints);
            //???????????????????????????
            distanceQuery.setDestination(dest);

            //??????????????????????????????????????????
            distanceQuery.setType(DistanceSearch.TYPE_DISTANCE);

            distanceSearch.calculateRouteDistanceAsyn(distanceQuery);
        } catch (Exception e) {
            BaseLogUtils.e(e);
        }

    }

    /**
     * ?????????????????????
     *
     * @param distanceResults
     * @param listener
     */
    public void aMapSettingDistance(LifecycleProvider lifecycleProvider, List<DistanceItem> distanceResults, CustomResultListener listener) {
        if (distanceResults == null || distanceResults.size() <= 0)
            return;

        RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {
            for (int i = 0; i < distanceResults.size(); i++) {

                e.onNext(new ItemDistanceBean(i, oneDecimal(distanceResults.get(i).getDistance() / 1000)));
            }
            e.onComplete();
        }), lifecycleProvider).subscribe(o -> {
            if (listener != null)
                listener.onResult(o);
        });
    }

    /**
     * ????????????
     *
     * @param context
     * @param longitude
     * @param latitude
     * @param storelat
     * @param storelon
     * @param customResultListener
     */
    public void distanceSearch(Context context, double longitude, double latitude, double storelat, double storelon, CustomResultListener customResultListener) {

        Map<String, String> map = getJsonMap();


        map.put(LocalConstant.LONGITUDE1, String.valueOf(longitude));
        map.put(LocalConstant.LONGITUDE2, String.valueOf(storelon));
        map.put(LocalConstant.LATITUDE1, String.valueOf(latitude));
        map.put(LocalConstant.LATITUDE2, String.valueOf(storelat));

        Observable<NormalBean> observable = HttpClient.post(OTHER_DISTANCE, map)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);

            }

            @Override
            public void onNext(NormalBean normalBean) {
                if (customResultListener != null) {
                    ItemDistanceBean itemDistanceBean = new ItemDistanceBean(-1, normalBean.distance);
                    customResultListener.onResult(itemDistanceBean);
                }
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }


    /**
     * ??????
     *
     * @param context
     * @param reason
     * @param content
     * @param list
     * @param sendIdentifier
     */
    public void complaintMessage(Context context, String reason, String content, List<String> list, String sendIdentifier) {

        Map<String, String> map = getJsonMap();

        map.put(LocalConstant.REASON, reason);
        map.put("content", content);

        if (list.size() > 0)
            map.put("images", GsonUtil.gsonToStr(list));

        if (!isEmpty(sendIdentifier))
            map.put("sendIdentifier", sendIdentifier);

        Observable<NormalBean> observable = HttpClient.post(COMPLAINT_MSG, map)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);
                if (netWorkListener != null)
                    netWorkListener.onMessage(e.getMessage());
            }

            @Override
            public void onNext(NormalBean normalBean) {
                if (netWorkListener != null)
                    netWorkListener.onMessage("????????????");

                EventBus.getDefault().post(COMPLAINT_SUCCEED);

            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }


    /**
     * ????????????????????????????????????
     *
     * @param context
     * @param invitationCode
     */
    public void getInvitationCode(Context context, String invitationCode, CustomResultListener customResultListener) {

        Map<String, String> map = getJsonMap();

        if (!isEmpty(invitationCode))
            map.put(INVITATION_CODE, String.valueOf(invitationCode));

        Observable<NormalBean> observable = HttpClient.post(INVITATIONCODE, map)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);
            }

            @Override
            public void onNext(NormalBean normalBean) {
                if (!isEmpty(normalBean.invitationCode) && customResultListener != null)
                    customResultListener.onResult("ok");
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }

    /**
     * ????????????????????????
     */
    public void idCardOCR(Context context, String side, String imgUrl, CustomResultListener customResultListener) {

        Map<String, String> map = getJsonMap();

        map.put("side", side);
        map.put("imageUrl", imgUrl);

        if (netWorkListener != null)
            netWorkListener.onStarts();

        Observable<IdCardOCRBean> observable = HttpClient.post(EXTRA_IDCARDOCR, map)
                .execute(new CallClazzProxy<ApiResult<IdCardOCRBean>, IdCardOCRBean>(new TypeToken<IdCardOCRBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<IdCardOCRBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);
                customResultListener.onResult(null);

                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
                    netWorkListener.onAfters();
                }


            }

            @Override
            public void onNext(IdCardOCRBean idCardOCRBean) {

                if (netWorkListener != null)
                    netWorkListener.onAfters();

                if (customResultListener != null)
                    customResultListener.onResult(idCardOCRBean);


            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }

    /**
     * ????????????
     */
    public void certification(Context context, String name, String cardNumber, String frontUrl, String backUrl, CustomResultListener customResultListener) {

        Map<String, String> map = getJsonMap();

        map.put("name", name);

        map.put("cardNumber", cardNumber);

        map.put("frontUrl", frontUrl);

        map.put("backUrl", backUrl);

        Observable<NormalBean> observable = HttpClient.post(EXTRA_CERTIFICATION, map)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);
                if (netWorkListener != null)
                    netWorkListener.onMessage(e.getMessage());
            }

            @Override
            public void onNext(NormalBean normalBean) {

                if (customResultListener != null) {
                    customResultListener.onResult(normalBean.msg);
                }
                if (netWorkListener != null)
                    netWorkListener.onMessage(normalBean.msg);
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }


    /**
     * ?????????????????????????????????
     *
     * @param data
     * @param idCardName
     * @param idCardNumber
     */
    public void RPauthInit(Context context, String data, String idCardName, String idCardNumber, CustomResultListener customResultListener) {

        Map<String, String> map = getJsonMap();

        map.put("initMsg", data);

        map.put("certName", idCardName);
        map.put("certNo", idCardNumber);

        Observable<AuthenticationBean> observable = HttpClient.post(AUTHENTICATION_INIT, map)
                .execute(new CallClazzProxy<ApiResult<AuthenticationBean>, AuthenticationBean>(new TypeToken<AuthenticationBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<AuthenticationBean>(context) {
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
            public void onNext(AuthenticationBean authenticationBean) {

                if (customResultListener != null) {
                    customResultListener.onResult(authenticationBean);
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
    public void AliRPauth(Context context, EsLivingDetectResult result1, CustomResultListener customResultListener) {
        if (netWorkListener != null)
            netWorkListener.onStarts();

        Map<String, String> map = getJsonMap();

        map.put("token", result1.getToken());

        map.put("verifyMsg", result1.getData());

/*        FormBody body;
        FormBody.Builder bodyBuilder = new FormBody.Builder()
                .add("token", result1.getToken())
                .add("verifyMsg", result1.getData());

        body = bodyBuilder.build();*/

        HttpHeaders headers = new HttpHeaders();

        //?????????header????????????(?????????????????????)???Authorization:APPCODE
//        headers.put("Authorization", "APPCODE " + "a0b80eedd699448e82a1f1f7250deb31");
        headers.put("Authorization", "APPCODE " + LocalConstant.APPCODE);
        //??????API??????????????????????????????Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        //?????????X-Ca-Nonce??????????????????????????????????????????????????????
        headers.put("X-Ca-Nonce", UUID.randomUUID().toString());

        Observable<String> observable = HttpClient.post(ALICLOUDAPI_VERIFY, map).headers(headers)
                .execute(String.class);

        MyBaseSubscriber subscriber = new MyBaseSubscriber<String>(context) {
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
            public void onNext(String s) {

                AliVerifyBean data = GsonUtil.GsonToBean(s, AliVerifyBean.class);
                if (customResultListener != null) {
                    customResultListener.onResult(data);
                }

                if (netWorkListener != null)
                    netWorkListener.onAfters();
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);


    }

    /**
     * ??????????????????
     */
    public void RPauth(Context context, AliVerifyBean result1, CustomResultListener customResultListener) {

        Map<String, String> map = getJsonMap();

        if (!isEmpty(result1.getCode()))
            map.put("code", result1.getCode());
        if (!isEmpty(result1.getMsg()))
            map.put("msg", result1.getMsg());
        if (!isEmpty(result1.getBizId()))
            map.put("bizId", result1.getBizId());
        if (!isEmpty(result1.getRequestId()))
            map.put("requestId", result1.getRequestId());
        if (!isEmpty(result1.getLivingType()))
            map.put("livingType", result1.getLivingType());
        if (!isEmpty(result1.getCertName()))
            map.put("certName", result1.getCertName());
        if (!isEmpty(result1.getCertNo()))
            map.put("certNo", result1.getCertNo());
        if (!isEmpty(result1.getBestImg()))
            map.put("bestImg", result1.getBestImg());
        if (!isEmpty(result1.getPass()))
            map.put("pass", result1.getPass());
//        map.put("rxfs", result1.getRxfs());

        Observable<AuthenticationBean> observable = HttpClient.post(AUTHENTICATION_VERIFY, map)
                .execute(new CallClazzProxy<ApiResult<AuthenticationBean>, AuthenticationBean>(new TypeToken<AuthenticationBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<AuthenticationBean>(context) {
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
            public void onNext(AuthenticationBean s) {

                if (customResultListener != null) {
                    customResultListener.onResult(s);
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
    public void RPauth2(Context context, EsLivingDetectResult result1, CustomResultListener customResultListener) {

/*        Map<String, String> map = getJsonMap();

        map.put("token", result1.getToken());

        map.put("verifyMsg", result1.getData());*/

        FormBody body;
        FormBody.Builder bodyBuilder = new FormBody.Builder()
                .add("token", result1.getToken())
                .add("verifyMsg", result1.getData());

        body = bodyBuilder.build();

        HttpHeaders headers = new HttpHeaders();

        //?????????header????????????(?????????????????????)???Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + LocalConstant.APPCODE);
        //??????API??????????????????????????????Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        //?????????X-Ca-Nonce??????????????????????????????????????????????????????
        headers.put("X-Ca-Nonce", UUID.randomUUID().toString());

        Observable<AuthenticationBean> observable = HttpClient.post(AUTHENTICATION_VERIFY2).requestBody(body)
                .execute(new CallClazzProxy<ApiResult<AuthenticationBean>, AuthenticationBean>(new TypeToken<AuthenticationBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<AuthenticationBean>(context) {
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
            public void onNext(AuthenticationBean s) {

                if (customResultListener != null) {
                    customResultListener.onResult(s);
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
     * @param customResultListener
     */
    public void getLabelList(Context context, CustomResultListener customResultListener) {

        Observable<LabelListBean> observable = HttpClient.post(CIRCLE_LABEL)
                .execute(new CallClazzProxy<ApiResult<LabelListBean>, LabelListBean>(new TypeToken<LabelListBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<LabelListBean>(context) {
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
            public void onNext(LabelListBean data) {

                if (customResultListener != null) {
                    customResultListener.onResult(data.list);
                }
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }
}
