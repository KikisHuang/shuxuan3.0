package com.gxdingo.sg.presenter;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import com.alipay.sdk.app.OpenAuthTask;
import com.blankj.utilcode.util.SPUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.ClientMainContract;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.model.ClientMainModel;
import com.gxdingo.sg.model.LoginModel;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.model.OneKeyModel;
import com.gxdingo.sg.utils.ClientLocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.zhouyou.http.subsciber.BaseSubscriber;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.StringUtils.getString;
import static com.gxdingo.sg.utils.LocalConstant.LOGIN_WAY;
import static com.gxdingo.sg.utils.pay.AlipayTool.simpleAuth;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.CommonUtils.isWeixinAvilible;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientMainPresenter extends BaseMvpPresenter<BasicsListener, ClientMainContract.ClientMainListener> implements ClientMainContract.ClientMainPresenter, NetWorkListener {

    private ClientMainModel model;

    private NetworkModel networkModel;

    private OneKeyModel oneKeyModel;

    private LoginModel mModdel;

    public ClientMainPresenter() {
        model = new ClientMainModel();
        networkModel = new NetworkModel(this);
        oneKeyModel = new OneKeyModel();
        mModdel = new LoginModel();
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
    public void onMvpDestroy() {
        super.onMvpDestroy();
        if (model != null)
            model = null;
    }

    @Override
    public void persenterInit() {
        if (model != null && isViewAttached()) {

            FragmentTransaction fragmentTransaction = getV().getFragmentTransaction();

            model.fragmentInit(fragmentTransaction, getV().getFragmentList());

            if (isViewAttached())
                getV().showFragment(fragmentTransaction, 0);
        }
    }

    private void showFm(int tab) {
        model.hideAllFragment(getV().getFragmentList().size(), getContext(), index -> {
            if (isViewAttached())
                getV().hideFragment(index);
        });
        getV().showFragment(getV().getFragmentTransaction(), tab);

    }

    @Override
    public void checkTab(int tab) {
        if (!UserInfoUtils.getInstance().isLogin())
            if (tab==1 || tab==3){
                oneKeyModel.sdkInit(getContext());
                return;
            }

        if (model != null) {
            if (isViewAttached() && tab != model.getOldTab())
                getV().onSeleted(tab, model.getOldTab());

            showFm(tab);

            model.recordTab(tab);
        }
    }

    @Override
    public void oneKeyLogin(String code) {
        if (networkModel!=null)
            networkModel.oneClickLogin(getContext(),code, SPUtils.getInstance().getBoolean(LOGIN_WAY));
    }

    @Override
    public void aliLogin() {
        if (networkModel!=null)

            networkModel.getAliyPayAuthinfo(getContext(), str -> simpleAuth((Activity) getContext(), (String) str, callback));
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
    public void wechatLogin(String code) {
        if (networkModel != null && isViewAttached()) {
            networkModel.thirdPartyLogin(getContext(), code, ClientLocalConstant.WECHAT, SPUtils.getInstance().getBoolean(LOGIN_WAY));
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
                if (networkModel != null) {
                    if (!isEmpty(authCode)) {
                        networkModel.thirdPartyLogin(getContext(), authCode, ClientLocalConstant.ALIPAY, SPUtils.getInstance().getBoolean(LOGIN_WAY));
                    } else
                        onMessage("没有获取到authCode");
                }

                // 执行原有处理逻辑
            }
        }
    };

    @Override
    public void getSocketUrl() {

    }

    @Override
    public void destroySocket() {

    }
}
