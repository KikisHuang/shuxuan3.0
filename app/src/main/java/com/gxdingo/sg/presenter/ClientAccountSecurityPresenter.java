package com.gxdingo.sg.presenter;

import com.gxdingo.sg.biz.ClientAccountSecurityContract;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.zhouyou.http.subsciber.BaseSubscriber;

import static android.text.TextUtils.isEmpty;

/**
 * @author: Weaving
 * @date: 2021/10/15
 * @page:
 */
public class ClientAccountSecurityPresenter extends BaseMvpPresenter<BasicsListener, ClientAccountSecurityContract.ClientAccountSecurityListener>
        implements ClientAccountSecurityContract.ClientAccountSecurityPresenter, NetWorkListener {

    private NetworkModel mNetworkModel;

//    private String phone  =  UserInfoUtils.getInstance().getUserInfo().getMobile();
    private String phone = "18878759765";

    public ClientAccountSecurityPresenter() {
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
    public void sendVerificationCode() {
        if (mNetworkModel != null && isViewAttached()) {
            mNetworkModel.sendSmsCode(getContext(), phone);
        }
    }

    @Override
    public void certify() {
        if (mNetworkModel != null && isViewAttached()) {
            mNetworkModel.checkSMSCode(getContext(),phone, getV().getCode(), o -> getV().next());
        }
    }

    @Override
    public void certifyPwd() {

    }

    @Override
    public void checkPayPsw() {

    }

    @Override
    public void updatePsw() {

    }

    @Override
    public void saveStatus() {

    }

    @Override
    public void getUserPhone() {
        if (isViewAttached() && !isEmpty(phone))
            getV().setUserPhone(phone);
    }
}
