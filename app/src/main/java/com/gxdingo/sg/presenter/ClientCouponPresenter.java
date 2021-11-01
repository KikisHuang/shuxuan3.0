package com.gxdingo.sg.presenter;

import com.gxdingo.sg.bean.ClientCouponsBean;
import com.gxdingo.sg.biz.ClientCouponContract;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.model.ClientNetworkModel;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.zhouyou.http.subsciber.BaseSubscriber;

import static android.text.TextUtils.isEmpty;


/**
 * @author: Weaving
 * @date: 2021/10/29
 * @page:
 */
public class ClientCouponPresenter extends BaseMvpPresenter<BasicsListener, ClientCouponContract.ClientCouponListener> implements ClientCouponContract.ClientCouponPresenter, NetWorkListener {

    private ClientNetworkModel clientNetworkModel;

    public ClientCouponPresenter() {
        clientNetworkModel = new ClientNetworkModel(this);
    }

    @Override
    public void receive() {
        if (isEmpty(getV().getCode())){
            onMessage("请输入商家邀请码");
            return;
        }
        clientNetworkModel.receiveCoupon(getContext(),getV().getCode());
    }

    @Override
    public void getCoupons(boolean refresh) {
        if (clientNetworkModel!=null)
            clientNetworkModel.getCoupons(getContext(),refresh);
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
        if (o instanceof ClientCouponsBean){
            ClientCouponsBean couponsBean = (ClientCouponsBean) o;
            if (couponsBean.getList()!=null)
                getV().onCouponsResult(refresh,couponsBean.getList());
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
            getBV().onStarts();
    }

    @Override
    public void onDisposable(BaseSubscriber subscriber) {
        addDisposable(subscriber);
    }
}
