package com.gxdingo.sg.presenter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.alipay.sdk.app.OpenAuthTask;
import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.LoginContract;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.model.LoginModel;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.utils.ClientLocalConstant;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.zhouyou.http.subsciber.BaseSubscriber;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.StringUtils.getString;
import static com.gxdingo.sg.http.HttpClient.switchGlobalUrl;
import static com.gxdingo.sg.utils.pay.AlipayTool.simpleAuth;
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

    public LoginPresenter() {

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
    public void switchUrl(boolean isUserId) {
        getV().showIdButton();
        switchGlobalUrl(isUserId);
    }

    @Override
    public void switchPanel(boolean showBack, boolean oneClick) {
        if (isViewAttached())
            getV().setPanel(showBack? View.VISIBLE:View.GONE,oneClick?View.VISIBLE:View.GONE,oneClick?View.GONE:View.VISIBLE);
    }

    @Override
    public void getWechatAuth() {
        if (!isViewAttached() || mModdel == null)
            return;
        if (isWeixinAvilible(getContext()))
            mModdel.wxLogin();
        else {
            if (isBViewAttached())
                getBV().onMessage(String.format(getString(R.string.uninstall_app), gets(R.string.wechat)));
        }
    }

    @Override
    public void alipayAuth() {
        mNetworkModel.getAliyPayAuthinfo(getContext(), str -> {
            simpleAuth((Activity) getContext(), (String) str, callback);
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
    public void weChatLogin(String code) {
        if (mNetworkModel != null && isViewAttached()) {
            mNetworkModel.thirdPartyLogin(getContext(), code, ClientLocalConstant.WECHAT, getV().isClient());
        }
    }

    @Override
    public void login() {
        if (mNetworkModel != null && isViewAttached()) {
            mNetworkModel.login(getContext(), getV().getMobile(), getV().getCode(), getV().isClient());
        }
    }

    /**
     * 支付宝sdk结果回调，主线程中执行
     * 为了避免内存泄漏，支付宝sdk内不强引用该回调
     * 请持有该callback引用，以免callback被回收导致无法获取业务结果
     */
    private OpenAuthTask.Callback callback = new OpenAuthTask.Callback() {
        @Override
        public void onResult(int i, String s, Bundle bundle) {
            if (i == OpenAuthTask.OK) {

                String authCode = bundle.getString("auth_code");
                if (mNetworkModel != null) {
                    if (!isEmpty(authCode)) {
                        mNetworkModel.thirdPartyLogin(getContext(), authCode, ClientLocalConstant.ALIPAY, getV().isClient());
                    } else
                        onMessage("没有获取到authCode");
                }

                // 执行原有处理逻辑
            }
        }
    };

}
