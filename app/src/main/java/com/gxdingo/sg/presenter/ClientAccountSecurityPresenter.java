package com.gxdingo.sg.presenter;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import com.alipay.sdk.app.OpenAuthTask;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientAccountTransactionBean;
import com.gxdingo.sg.bean.ClientCashInfoBean;
import com.gxdingo.sg.biz.ClientAccountSecurityContract;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.model.ChangePhoneModel;
import com.gxdingo.sg.model.ClientNetworkModel;
import com.gxdingo.sg.model.LoginModel;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.utils.ClientLocalConstant;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.utils.pay.AlipayTool;
import com.gxdingo.sg.utils.pay.WechatUtils;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.kikis.commnlibrary.utils.BigDecimalUtils;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.zhouyou.http.subsciber.BaseSubscriber;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.RegexUtils.isMobileSimple;
import static com.blankj.utilcode.util.StringUtils.getString;
import static com.gxdingo.sg.utils.pay.AlipayTool.simpleAuth;
import static com.kikis.commnlibrary.utils.CommonUtils.HideMobile;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.CommonUtils.isWeixinAvilible;

/**
 * @author: Weaving
 * @date: 2021/10/15
 * @page:
 */
public class ClientAccountSecurityPresenter extends BaseMvpPresenter<BasicsListener, ClientAccountSecurityContract.ClientAccountSecurityListener>
        implements ClientAccountSecurityContract.ClientAccountSecurityPresenter, NetWorkListener {

    private NetworkModel mNetworkModel;

    private ClientNetworkModel clientNetworkModel;

    private LoginModel mModdel;

    private String phone  =  UserInfoUtils.getInstance().getUserInfo().getMobile();
//    private String phone = "18878759765";

    public ClientAccountSecurityPresenter() {
        mNetworkModel = new NetworkModel(this);
        clientNetworkModel = new ClientNetworkModel(this);
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
        if (o instanceof ClientAccountTransactionBean){
            ClientAccountTransactionBean transactionBean = (ClientAccountTransactionBean) o;
            if (transactionBean.getList() != null)
                getV().onTransactionResult(refresh,transactionBean.getList());
        }else if (o instanceof ClientCashInfoBean)
            getV().onCashInfoResult((ClientCashInfoBean) o);


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
    public void getAccountRecord(boolean refresh, int status, String date) {
        if (clientNetworkModel!=null)
            clientNetworkModel.getAccountTransaction(getContext(),refresh,status,date);
    }


    @Override
    public void sendVerificationCode() {
        if (mNetworkModel != null && isViewAttached()) {
            mNetworkModel.sendSmsCode(getContext(), phone);
        }
    }

    @Override
    public void getCashInfo() {
        if (clientNetworkModel != null){
            clientNetworkModel.getCashInfo(getContext());
        }
    }

    @Override
    public void bind(String code, int type) {
        if (clientNetworkModel!=null)
            clientNetworkModel.bindThirdParty(getContext(),code,type);
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
    public void unbindThirdParty(int type) {
        if (clientNetworkModel!=null)
            clientNetworkModel.unbindThirdParty(getContext(),type);
    }

    @Override
    public void cash(String pwd) {
        if (getV().getType()<0){
            onMessage(gets(R.string.please_select_cash_account));
        }
        if (!BigDecimalUtils.compare(getV().getCashAmount(),"0")){
            onMessage(gets(R.string.please_input_valid_amount));
        }
        clientNetworkModel.balanceCash(getContext(),getV().getType(),getV().getCashAmount(),pwd,getV().getBackCardId());
    }

    @Override
    public void loginOff() {
        if (mNetworkModel!=null)
            mNetworkModel.logOff(getContext());
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
                if (clientNetworkModel != null) {
                    if (!isEmpty(authCode)) {
                        clientNetworkModel.bindThirdParty(getContext(),authCode,0);
                    } else
                        onMessage("没有获取到authCode");
                }

                // 执行原有处理逻辑
            }
        }
    };
}
