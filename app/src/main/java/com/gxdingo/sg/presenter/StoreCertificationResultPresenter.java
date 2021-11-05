package com.gxdingo.sg.presenter;

import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.StoreCertificationResultContract;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.zhouyou.http.subsciber.BaseSubscriber;

public class StoreCertificationResultPresenter extends BaseMvpPresenter<BasicsListener, StoreCertificationResultContract.StoreCertificationResultListener> implements StoreCertificationResultContract.StoreCertificationResultPresenter, NetWorkListener {
    @Override
    public void onSucceed(int type) {

    }

    @Override
    public void onMessage(String msg) {

    }

    @Override
    public void noData() {

    }

    @Override
    public void onData(boolean refresh, Object o) {

    }

    @Override
    public void haveData() {

    }

    @Override
    public void finishLoadmoreWithNoMoreData() {

    }

    @Override
    public void finishRefreshWithNoMoreData() {

    }

    @Override
    public void onRequestComplete() {

    }

    @Override
    public void resetNoMoreData() {

    }

    @Override
    public void finishRefresh(boolean success) {

    }

    @Override
    public void finishLoadmore(boolean success) {

    }

    @Override
    public void onAfters() {

    }

    @Override
    public void onStarts() {

    }

    @Override
    public void onDisposable(BaseSubscriber subscriber) {

    }
}
