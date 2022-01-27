package com.gxdingo.sg.presenter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.esandinfo.livingdetection.EsLivingDetectionManager;
import com.esandinfo.livingdetection.bean.EsLivingDetectResult;
import com.esandinfo.livingdetection.biz.EsLivingDetectCallback;
import com.esandinfo.livingdetection.constants.EsLivingDetectErrorCode;
import com.esandinfo.livingdetection.util.AppExecutors;
import com.esandinfo.livingdetection.util.GsonUtil;
import com.esandinfo.livingdetection.util.MyLog;
import com.gxdingo.sg.bean.AliVerifyBean;
import com.gxdingo.sg.bean.AuthenticationBean;
import com.gxdingo.sg.bean.UpLoadBean;
import com.gxdingo.sg.biz.AuthenticationContract;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.UpLoadImageListener;
import com.gxdingo.sg.http.Api;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.SignatureUtils;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.kikis.commnlibrary.utils.BaseLogUtils;
import com.kikis.commnlibrary.utils.Constant;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.zhouyou.http.subsciber.BaseSubscriber;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.blankj.utilcode.util.FileUtils.createOrExistsDir;
import static com.blankj.utilcode.util.RegexUtils.isIDCard18Exact;
import static com.gxdingo.sg.http.Api.AUTHENTICATION_INIT;
import static com.gxdingo.sg.http.Api.AUTHENTICATION_VERIFY;
import static com.gxdingo.sg.http.Api.AUTHENTICATION_VERIFY2;
import static com.gxdingo.sg.http.Api.URL;
import static com.gxdingo.sg.http.HttpClient.getCurrentTimeUTCM;
import static com.gxdingo.sg.utils.LocalConstant.GLOBAL_SIGN;
import static com.gxdingo.sg.utils.PhotoUtils.getPhotoUrl;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * @author: Kikis
 * @date: 2021/12/30
 * @page:
 */
public class AuthenticationPresenter extends BaseMvpPresenter<BasicsListener, AuthenticationContract.AuthenticationListener> implements AuthenticationContract.AuthenticationPresenter, NetWorkListener, OnResultCallbackListener<LocalMedia> {

    private NetworkModel mNetworkModel;


    // @param livingType 认证类型  1：远近，2：眨眼，3：摇头，4: 点头，5:张嘴，6：炫彩活体
    //  支持多动作，如传入12表示先做远近活体，后做眨眼活体，一次最多支持4组动作
    //2眨眼、3摇头、4点头、5张嘴
    private int livingType = 23;

    private EsLivingDetectionManager manager;

    private Handler handler;

    public AuthenticationPresenter() {
        mNetworkModel = new NetworkModel(this);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {

                    if (msg.obj instanceof AuthenticationBean) {
                        AuthenticationBean data2 = (AuthenticationBean) msg.obj;
                        if (isViewAttached())
                            getV().onShowAuthenticationStatusDialog(data2);
                    } else if (msg.obj instanceof String)
                        onMessage((String) msg.obj);

                }
            }
        };
    }

    @Override
    public void onMvpAttachView(Context context, BasicsListener bview, AuthenticationContract.AuthenticationListener view) {
        super.onMvpAttachView(context, bview, view);
        manager = new EsLivingDetectionManager(getContext());
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
    }

    @Override
    public void haveData() {
        if (isBViewAttached())
            getBV().haveData();
    }

    @Override
    public void finishLoadmoreWithNoMoreData() {
        if (isBViewAttached())
            getBV().finishRefreshWithNoMoreData();
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


    @Override
    public void photoItemClick(int pos) {

    }

    /**
     * 检测初始化
     */
    @Override
    public void verifyInit() {

        if (!isViewAttached())
            return;

        if (isEmpty(getV().getIdCardName())) {
            onMessage("请填写姓名");
            return;
        } else if (!isIDCard18Exact(getV().getIdCardNumber())) {
            onMessage("请填写正确的身份证号");
            return;
        }

        AppExecutors.getInstance().networkIO().execute(() -> {
            EsLivingDetectResult result = manager.verifyInit(livingType);
            if (EsLivingDetectErrorCode.ELD_SUCCESS == result.getCode()) {
                if (mNetworkModel != null) {
                    mNetworkModel.RPauthInit(getContext(), result.getData(), getV().getIdCardName(), getV().getIdCardNumber(), new CustomResultListener() {
                        @Override
                        public void onResult(Object o) {
                            AuthenticationBean data1 = (AuthenticationBean) o;
                            LogUtils.w("get esFace token === " + data1.getToken());


                            if (data1.getCode().equals("0000")) {
                                //初始化成功过 访问服务端进行初始化
                                manager.startLivingDetect(data1.getToken(), result1 -> {
                                    if (result1.getCode() == EsLivingDetectErrorCode.ELD_SUCCESS) {
                                        if (mNetworkModel != null) {

                                            mNetworkModel.AliRPauth(getContext(), result1, o1 -> {
                                                AliVerifyBean data2 = (AliVerifyBean) o1;
                                                mNetworkModel.RPauth(getContext(), data2, o2 -> {
                                                    AuthenticationBean data3 = (AuthenticationBean) o2;
                                                    updateUi(data3);
                                                });

                                            });

                                        }
                                    } else {
                                        updateUi(result1.getMsg());
                                    }
                                });
                            } else
                                updateUi(data1.getMsg());
                        }
                    });
                }
            } else {
                updateUi(result.getMsg());
            }
        });

    }


    // 更新 UI 进程界面
    private void updateUi(Object messageStr) {
        Message message = Message.obtain();
        message.obj = messageStr;
        message.what = 0;
        handler.sendMessage(message);
    }

    @Override
    public void onResult(List<LocalMedia> result) {

        if (mNetworkModel != null && isBViewAttached() && isViewAttached()) {
            String url = getPhotoUrl(result.get(0));

            mNetworkModel.upLoadImage(getContext(), url, new UpLoadImageListener() {
                @Override
                public void loadSucceed(String path) {


                }

                @Override
                public void loadSucceed(UpLoadBean upLoadBean) {

                }
            }, 1);

        }

    }

    @Override
    public void onCancel() {

    }
}
