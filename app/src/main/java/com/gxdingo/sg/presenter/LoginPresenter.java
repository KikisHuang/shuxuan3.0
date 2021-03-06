package com.gxdingo.sg.presenter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.alipay.sdk.app.OpenAuthTask;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.AuthResult;
import com.gxdingo.sg.bean.OneKeyLoginEvent;
import com.gxdingo.sg.biz.LoginContract;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.model.LoginModel;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.model.OneKeyModel;
import com.gxdingo.sg.utils.ClientLocalConstant;
import com.gxdingo.sg.utils.LocalConstant;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.zhouyou.http.subsciber.BaseSubscriber;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.StringUtils.getString;
import static com.gxdingo.sg.utils.LocalConstant.SDK_AUTH_FLAG;
import static com.gxdingo.sg.utils.pay.AlipayTool.auth;
import static com.kikis.commnlibrary.utils.CommonUtils.getSmsCodeTime;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.CommonUtils.isWeixinAvilible;

/**
 * @author: Weaving
 * @date: 2021/10/12
 * @page:
 */
public class LoginPresenter extends BaseMvpPresenter<BasicsListener, LoginContract.LoginListener> implements LoginContract.LoginPresenter, NetWorkListener {

    private NetworkModel mNetworkModel;

    private LoginModel mModdel;

    private OneKeyModel oneKeyModel;

    public LoginPresenter() {
        oneKeyModel = new OneKeyModel(this);
        mModdel = new LoginModel();
        mNetworkModel = new NetworkModel(this);
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


    @Override
    public void getWechatAuth() {

        if (!isViewAttached() || mModdel == null)
            return;
        if (isWeixinAvilible(getContext())) {
            if (getV().getCheckState() == false) {
                onMessage(gets(R.string.please_agree_to_the_terms_of_service));
                return;
            }
            //??????????????????????????????????????????
            LocalConstant.isLogin = false;
            mModdel.wxLogin();
        } else {
            if (isBViewAttached())
                getBV().onMessage(String.format(getString(R.string.uninstall_app), gets(R.string.wechat)));
        }
    }

    @Override
    public void alipayAuth() {
        if (getV().getCheckState() == false) {
            onMessage(gets(R.string.please_agree_to_the_terms_of_service));
            return;
        }
        mNetworkModel.getAliyPayAuthinfo(getContext(), str -> {
            auth((Activity) getContext(), (String) str, handler);
//            simpleAuth((Activity) getContext(), (String) str, callback);
        });
    }

    @Override
    public void sendVerificationCode() {
        if (mNetworkModel != null && isViewAttached()) {
            mNetworkModel.sendSmsCode(getContext(), getV().getMobile());
        }
    }

    @Override
    public void getVerificationCodeTime() {
        int time = getSmsCodeTime();

        if (time <= 0)
            return;

        if (isBViewAttached())
            getV().setVerificationCodeTime(time);
    }

    @Override
    public void bindPhone(String mOpenId, String mAppName) {
        if (mNetworkModel != null && isViewAttached()) {
            mNetworkModel.bind(getContext(), getV().getMobile(), getV().getCode(), mOpenId, mAppName);
        }

    }

    @Override
    public void weChatLogin(String code) {
        if (mNetworkModel != null && isViewAttached()) {
            mNetworkModel.thirdPartyLogin(getContext(), code, ClientLocalConstant.WECHAT);
        }
    }

    @Override
    public void login() {

        if (isEmpty(getV().getMobile()) || isEmpty(getV().getCode())) {
            onMessage(gets(R.string.phone_number_of_verification_code_cannot_null));
            return;

        }

        if (getV().getCheckState() == false) {
            onMessage(gets(R.string.please_agree_to_the_terms_of_service));
            return;
        }
        if (mNetworkModel != null && isViewAttached()) {
            mNetworkModel.login(getContext(), getV().getMobile(), getV().getCode());
        }
    }

    /**
     * ????????????
     */
    @Override
    public void oauth() {
        if (oneKeyModel != null) {
            oneKeyModel.getKey(getContext(), this, (CustomResultListener<OneKeyLoginEvent>) event -> {
                new NetworkModel(this).oneClickLogin(getContext(), event.code);
            });
        }
    }


    @Override
    public void oauthWeChatLogin(String code) {
        if (oneKeyModel != null)
            oneKeyModel.thirdPartyLogin(getContext(), code, ClientLocalConstant.WECHAT,this);

    }

    @Override
    public void quitlogin() {
        if (oneKeyModel != null)
            oneKeyModel.quitLoginPage();
    }


    @Override
    public void onMvpDestroy() {
        super.onMvpDestroy();
        if (oneKeyModel != null)
            oneKeyModel.destroy();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
            switch (msg.what) {
                case SDK_AUTH_FLAG:
                    AuthResult authResult = (AuthResult) msg.obj;
                    if (mNetworkModel != null) {
                        if (!isEmpty(authResult.getAuthCode()) && isViewAttached()) {
                            mNetworkModel.thirdPartyLogin(getContext(), authResult.getAuthCode(), ClientLocalConstant.ALIPAY);
                        } else
                            onMessage("???????????????authCode");
                    }
                    break;
            }
        }
    };

    /**
     * ?????????sdk?????????????????????????????????
     * ????????????????????????????????????sdk????????????????????????
     * ????????????callback???????????????callback???????????????????????????????????????
     */
    private OpenAuthTask.Callback callback = new OpenAuthTask.Callback() {
        @Override
        public void onResult(int i, String s, Bundle bundle) {
            if (i == OpenAuthTask.OK) {

                String authCode = bundle.getString("auth_code");
                if (mNetworkModel != null) {
                    if (!isEmpty(authCode)) {
                        mNetworkModel.thirdPartyLogin(getContext(), authCode, ClientLocalConstant.ALIPAY);
                    } else
                        onMessage("???????????????authCode");
                }

                // ????????????????????????
            }
        }
    };

}
