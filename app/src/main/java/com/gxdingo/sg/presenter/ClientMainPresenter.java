package com.gxdingo.sg.presenter;

import androidx.fragment.app.FragmentTransaction;

import com.gxdingo.sg.biz.ClientMainContract;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.model.ClientMainModel;
import com.gxdingo.sg.model.NetworkModel;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.zhouyou.http.subsciber.BaseSubscriber;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientMainPresenter extends BaseMvpPresenter<BasicsListener, ClientMainContract.ClientMainListener> implements ClientMainContract.ClientMainPresenter, NetWorkListener {

    private ClientMainModel model;

    private NetworkModel networkModel;

    public ClientMainPresenter() {
        model = new ClientMainModel();
        networkModel = new NetworkModel(this);
    }

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
        if (model != null) {
            if (isViewAttached() && tab != model.getOldTab())
                getV().onSeleted(tab, model.getOldTab());

            showFm(tab);

            model.recordTab(tab);
        }
    }

    @Override
    public void getSocketUrl() {

    }

    @Override
    public void destroySocket() {

    }
}
