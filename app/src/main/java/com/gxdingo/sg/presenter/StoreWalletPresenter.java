package com.gxdingo.sg.presenter;

import android.app.Activity;
import android.os.Bundle;

import com.alipay.sdk.app.OpenAuthTask;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.StoreCashActivity;
import com.gxdingo.sg.bean.StoreWalletBean;
import com.gxdingo.sg.bean.ThirdPartyBean;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.StoreWalletContract;
import com.gxdingo.sg.model.LoginModel;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.model.StoreNetworkModel;
import com.gxdingo.sg.utils.ClientLocalConstant;
import com.gxdingo.sg.utils.LocalConstant;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.kikis.commnlibrary.utils.Constant;
import com.zhouyou.http.subsciber.BaseSubscriber;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.StringUtils.getString;
import static com.gxdingo.sg.utils.pay.AlipayTool.simpleAuth;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.CommonUtils.isWeixinAvilible;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * 商家钱包控制器
 * @author JM
 */
public class StoreWalletPresenter extends BaseMvpPresenter<BasicsListener, StoreWalletContract.StoreWalletListener> implements StoreWalletContract
        .StoreWalletPresenter, NetWorkListener {

    private NetworkModel mNetworkModel;

    private StoreNetworkModel storeNetworkModel;

    private LoginModel mModdel;

    private StoreWalletBean walletBean;

    private String mType;

    public StoreWalletPresenter() {
        mNetworkModel = new NetworkModel(this);
        storeNetworkModel = new StoreNetworkModel(this);
        mModdel = new LoginModel();
    }

    @Override
    public void getWalletHome(boolean refresh) {
        if (storeNetworkModel!=null)
            storeNetworkModel.getWalletHome(getContext(),refresh);
    }

    @Override
    public void cash(String password) {
        if (storeNetworkModel!=null)
            storeNetworkModel.balanceCash(getContext(),mType,"",password,getV().getBackCardId());

    }

    @Override
    public void bind(String code, int type) {


        if (storeNetworkModel!=null)
            storeNetworkModel.bindThirdParty(getContext(),code,type);

    }

    @Override
    public void bindAli() {
        mNetworkModel.getAliyPayAuthinfo(getContext(), str -> {
            simpleAuth((Activity) getContext(), (String) str, callback);
        });
    }

    @Override
    public void bindWechat() {
        if (!isViewAttached() || mModdel == null)
            return;
        if (isWeixinAvilible(getContext())){
            LocalConstant.isLogin = false;
            mModdel.wxLogin();
        }else {
            if (isBViewAttached())
                getBV().onMessage(String.format(getString(R.string.uninstall_app), gets(R.string.wechat)));
        }
    }

    @Override
    public void goCashPage(int type) {
        if (walletBean ==null)return;

        switch (type){
            case 0:
                mType = ClientLocalConstant.ALIPAY;
                break;
            case 1:
                mType = ClientLocalConstant.WECHAT;
                break;
            case 2:
                mType = ClientLocalConstant.BANK;
                break;
        }

        if (type == 0 && isEmpty(walletBean.getAlipay())){
            mType = ClientLocalConstant.ALIPAY;
            bindAli();
            return;
        }

        if (type == 1 && isEmpty(walletBean.getWechat())){
            mType = ClientLocalConstant.WECHAT;
            bindWechat();
            return;
        }


        goToPagePutSerializable(getContext(), StoreCashActivity.class,getIntentEntityMap(new Object[]{type,walletBean}));
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
        if (o instanceof StoreWalletBean){
            walletBean = (StoreWalletBean) o;
            if (isViewAttached())
                getV().onWalletHomeResult(refresh,(StoreWalletBean) o);
        }else if (o instanceof ThirdPartyBean){
            ThirdPartyBean thirdPartyBean = (ThirdPartyBean) o;
            if (thirdPartyBean.type == 0)
                walletBean.setAlipay(thirdPartyBean.getNickname());
            else if (thirdPartyBean.type == 1)
                walletBean.setWechat(thirdPartyBean.getNickname());
            goToPagePutSerializable(getContext(), StoreCashActivity.class,getIntentEntityMap(new Object[]{thirdPartyBean.type,walletBean}));
        }
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
            getBV().onAfters();
    }

    @Override
    public void onDisposable(BaseSubscriber subscriber) {
        addDisposable(subscriber);
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
                if (storeNetworkModel != null) {
                    if (!isEmpty(authCode)) {
                        storeNetworkModel.bindThirdParty(getContext(),authCode,0);
                    } else
                        onMessage("没有获取到authCode");
                }

                // 执行原有处理逻辑
            }
        }
    };
}
