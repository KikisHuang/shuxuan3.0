package com.gxdingo.sg.presenter;

import com.gxdingo.sg.bean.BankcardListBean;
import com.gxdingo.sg.bean.ClientCashInfoBean;
import com.gxdingo.sg.biz.BankcardContract;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.model.ClientNetworkModel;
import com.gxdingo.sg.model.NetworkModel;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.zhouyou.http.subsciber.BaseSubscriber;

import anetwork.channel.NetworkListener;

import static android.text.TextUtils.isEmpty;

/**
 * @author: Weaving
 * @date: 2021/10/22
 * @page:
 */
public class BankcardPresenter extends BaseMvpPresenter<BasicsListener, BankcardContract.BankcardListener> implements BankcardContract.BankcardPresenter , NetWorkListener {

    private ClientNetworkModel clientNetworkModel;

    private NetworkModel mNetworkModel;

    public BankcardPresenter() {
        clientNetworkModel = new ClientNetworkModel(this);
        mNetworkModel = new NetworkModel(this);
    }

    @Override
    public void bindCard() {
        if (clientNetworkModel!=null)
            clientNetworkModel.addBankCard(getContext(),getV().getBankType(),getV().getPersonOfCard(),getV().getIdCard(),getV().getName(),getV().getNumber(),getV().getMobile(),getV().getCode());
    }

    @Override
    public void getCardList(boolean refresh) {
        if (clientNetworkModel!=null)
            clientNetworkModel.getBankList(getContext(),refresh);
    }


    @Override
    public void getSupportCardList() {
        if (clientNetworkModel!=null)
            clientNetworkModel.supportBank(getContext());
    }

    @Override
    public void sendVerificationCode() {
        if (getV().getMobile().length()<11){
            onMessage("请输入正确手机号码");
            return;
        }

        if (mNetworkModel!=null)
            mNetworkModel.sendSmsCode(getContext(),getV().getMobile());
    }

    @Override
    public void delete(long bankCardId) {
        if (clientNetworkModel!=null)
            clientNetworkModel.unbindBankCard(getContext(),bankCardId);
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
        if (o instanceof ClientCashInfoBean){
            getV().onDataResult(((ClientCashInfoBean)o).getBankList());
        }else if (o instanceof BankcardListBean)
            getV().onDataResult(((BankcardListBean)o).getList());
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
}
