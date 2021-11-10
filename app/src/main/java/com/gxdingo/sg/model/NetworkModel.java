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
import com.google.gson.reflect.TypeToken;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.BindingPhoneActivity;
import com.gxdingo.sg.bean.CommonlyUsedStoreBean;
import com.gxdingo.sg.bean.ItemDistanceBean;
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
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.utils.RxUtil;
import com.luck.picture.lib.entity.LocalMedia;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.zhouyou.http.callback.CallClazzProxy;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.ApiResult;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.RegexUtils.isMobileSimple;
import static com.blankj.utilcode.util.TimeUtils.getNowMills;
import static com.gxdingo.sg.http.Api.CHECK_CODE_SMS;
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
import static com.gxdingo.sg.utils.LocalConstant.STORE_LOGIN_SUCCEED;
import static com.gxdingo.sg.utils.PhotoUtils.getPhotoUrl;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.CommonUtils.oneDecimal;
import static com.kikis.commnlibrary.utils.GsonUtil.getJsonMap;
import static com.kikis.commnlibrary.utils.GsonUtil.getObjMap;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;
import static com.kikis.commnlibrary.utils.MyToastUtils.customToast;

//import com.gxdingo.sg.activity.BindingPhoneActivity;

/**
 * @author: Kikis
 * @date: 2021/3/16
 * @page: 网络请求model
 */
public class NetworkModel {

    protected int mPage = 1;

    protected int mPageSize = 10;

    public int getPage() {
        return mPage;
    }

    private NetWorkListener netWorkListener;


    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;


    /**
     * 翻页
     */
    public void nextPage() {
        mPage++;
    }


    public NetworkModel(NetWorkListener netWorkListener) {
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

    //获取一次定位方法
    public void location(Context context, AMapLocationListener mLocationListener) {
        //初始化定位
        mLocationClient = new AMapLocationClient(context);

        mLocationClient.setLocationListener(mLocationListener);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setOnceLocation(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    /**
     * 销毁定位方法
     */
    public void deactivateLocation() {

        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    /**
     * 发送验证码
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
                LogUtils.e(e);
                netWorkListener.onMessage(e.getMessage());
                netWorkListener.onAfters();

            }

            @Override
            public void onNext(NormalBean findBean) {

                SPUtils.getInstance().put(Constant.SMS_CODE_KEY, getNowMills());

                netWorkListener.onSucceed(LocalConstant.CODE_SEND);

                netWorkListener.onAfters();
                netWorkListener.onMessage(gets(R.string.captcha_code_sent));


            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }


    /**
     * 发送验证码带回调
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
                LogUtils.e(e);
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
     * 短信验证码校验
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

        if (SPUtils.getInstance().getBoolean(LocalConstant.LOGIN_WAY))
            map.put(Constant.CODE, code);
        else
            map.put(StoreLocalConstant.CAPTCHA, code);

        Observable<NormalBean> observable = HttpClient.post(CHECK_CODE_SMS, map)
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
     * 短信验证码校验
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
                LogUtils.e(e);
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
     * 一键登录
     *
     * @param context
     * @param
     * @param
     */
    public void oneClickLogin(Context context, String accessToken, boolean isUse) {


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
                LogUtils.e(e);

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onMessage(e.getMessage());
                    OneKeyModel.hideLoginLoading();
                }
            }

            @Override
            public void onNext(UserBean userBean) {
                OneKeyModel.quitLoginPage();
                UserInfoUtils.getInstance().saveLoginUserInfo(userBean);

                if (netWorkListener != null)
                    netWorkListener.onAfters();

                if (netWorkListener != null) {
                    netWorkListener.onSucceed(isUse ? LocalConstant.CLIENT_LOGIN_SUCCEED : STORE_LOGIN_SUCCEED);
                    EventBus.getDefault().post(isUse ? LocalConstant.CLIENT_LOGIN_SUCCEED : STORE_LOGIN_SUCCEED);
                }

            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);

    }

    /**
     * 登录
     *
     * @param context
     * @param mobile
     * @param code
     */
    public void login(Context context, String mobile, String code, boolean isUse) {

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
                LogUtils.e(e);

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onMessage(e.getMessage());
                }
            }

            @Override
            public void onNext(UserBean userBean) {
                OneKeyModel.quitLoginPage();
                UserInfoUtils.getInstance().saveLoginUserInfo(userBean);

                if (netWorkListener != null)
                    netWorkListener.onAfters();

                if (netWorkListener != null) {
                    netWorkListener.onSucceed(isUse ? LocalConstant.CLIENT_LOGIN_SUCCEED : STORE_LOGIN_SUCCEED);
                    EventBus.getDefault().post(isUse ? LocalConstant.CLIENT_LOGIN_SUCCEED : STORE_LOGIN_SUCCEED);
                }

            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);

    }

    /**
     * 支付宝获取infoSt
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
                LogUtils.e(e);

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
     * 第三方登录
     *
     * @param code
     * @param type
     */
    public void thirdPartyLogin(Context context, String code, String type, boolean isUse) {

        if (isEmpty(code)) {
            netWorkListener.onMessage(gets(R.string.phone_number_can_not_null));
            return;
        } else if (isEmpty(type)) {
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
                LogUtils.e(e);

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onMessage(e.getMessage());
                }
            }

            @Override
            public void onNext(UserBean userBean) {
                OneKeyModel.quitLoginPage();
                if (netWorkListener == null)
                    return;
                netWorkListener.onAfters();
                //0未绑定手机
                if (userBean.getIsBindMobile() == 0) {

                    netWorkListener.onMessage(gets(R.string.please_bind_phone));
                    netWorkListener.onSucceed(LocalConstant.BIND_PHONE);
                    goToPagePutSerializable(context, BindingPhoneActivity.class, getIntentEntityMap(new Object[]{userBean.getOpenid(), type, isUse}));
                } else {
                    UserInfoUtils.getInstance().saveLoginUserInfo(userBean);
                    netWorkListener.onSucceed(isUse ? LocalConstant.CLIENT_LOGIN_SUCCEED : STORE_LOGIN_SUCCEED);
                    EventBus.getDefault().post(isUse ? LocalConstant.CLIENT_LOGIN_SUCCEED : STORE_LOGIN_SUCCEED);
                }
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener == null)
            netWorkListener.onDisposable(subscriber);
    }

    /**
     * 绑定手机
     *
     * @param context
     * @param mobile
     * @param code
     * @param openid
     * @param appname
     */
    public void bind(Context context, String mobile, String code, String openid, String appname, boolean isUse) {


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
                LogUtils.e(e);

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

                    netWorkListener.onSucceed(isUse ? LocalConstant.CLIENT_LOGIN_SUCCEED : STORE_LOGIN_SUCCEED);
                    EventBus.getDefault().post(isUse ? LocalConstant.CLIENT_LOGIN_SUCCEED : STORE_LOGIN_SUCCEED);

                }

            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);

    }


    /**
     * 退出登录
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
                    netWorkListener.onSucceed(LocalConstant.LOGOUT_SUCCEED);
                }
                UserInfoUtils.getInstance().clearLoginStatus();
                UserInfoUtils.getInstance().goToLoginPage(context, "");

            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);

    }

    /**
     * 注销
     *
     * @param context
     */
    public void logOff(Context context) {

        if (netWorkListener != null)
            netWorkListener.onStarts();

        Observable<NormalBean> observable = HttpClient.post(USER_LOGOFF)
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
                    netWorkListener.onSucceed(LocalConstant.LOGOUT_SUCCEED);
                }
                UserInfoUtils.getInstance().clearLoginStatus();
                UserInfoUtils.getInstance().goToLoginPage(context, "");

            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);

    }


    /**
     * 上传图片
     *
     * @param context
     * @param path
     */
    public void upLoadImage(Context context, String path, UpLoadImageListener loadImageListener) {

        if (netWorkListener != null)
            netWorkListener.onStarts();

        Map<String, Object> map = getObjMap();

        map.put(Constant.FILE, new File(path));

        Observable<NormalBean> observable = HttpClient.postUpLoad(getUpLoadImage(), map, null)
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
     * 数据处理
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
                //添加图片
                LocalMedia localMedia = new LocalMedia();
                localMedia.setId(ADD);
                data.add(localMedia);
            }

            listener.onSucceed();
        });


    }

    /**
     * 批量上传图片
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
            customToast("获取图片路径出错");
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
                LogUtils.e(e);
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
     * 高德地图距离测量
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

            //设置各个店铺为起点
            distanceQuery.setOrigins(latLonPoints);
            //设置当前位置为终点
            distanceQuery.setDestination(dest);

            //设置测量方式，支持直线和驾车
            distanceQuery.setType(DistanceSearch.TYPE_DISTANCE);

            distanceSearch.calculateRouteDistanceAsyn(distanceQuery);
        } catch (Exception e) {
            LogUtils.e(e);
        }

    }

    /**
     * 按下标设置距离
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
     * 距离测量
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
                LogUtils.e(e);

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
}
