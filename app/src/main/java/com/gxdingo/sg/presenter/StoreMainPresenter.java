package com.gxdingo.sg.presenter;


import android.media.MediaPlayer;

import androidx.fragment.app.FragmentTransaction;

import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.NumberUnreadCommentsBean;
import com.gxdingo.sg.bean.OneKeyLoginEvent;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.StoreMainContract;
import com.gxdingo.sg.model.BusinessDistrictModel;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.model.OneKeyModel;
import com.gxdingo.sg.model.StoreMainModel;
import com.gxdingo.sg.model.WebSocketModel;
import com.gxdingo.sg.utils.MessageCountUtils;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.biz.MultiParameterCallbackListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.zhouyou.http.subsciber.BaseSubscriber;

import java.io.IOException;

import static com.gxdingo.sg.biz.StoreMainContract.StoreMainListener;
import static com.kikis.commnlibrary.utils.CommonUtils.getTAG;


public class StoreMainPresenter extends BaseMvpPresenter<BasicsListener, StoreMainListener> implements StoreMainContract.StoreMainPresenter, NetWorkListener {

    private static final String TAG = getTAG(StoreMainPresenter.class);

    private StoreMainModel model;

    private NetworkModel networkModel;

    private MediaPlayer mediaPlayer;

    private WebSocketModel mWebSocketModel;

    private BusinessDistrictModel businessDistrictModel;

    public StoreMainPresenter() {
        model = new StoreMainModel();
        mWebSocketModel = new WebSocketModel(this);
        networkModel = new NetworkModel(this);
        businessDistrictModel = new BusinessDistrictModel(this);
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
    public void play() {
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.new_order);
        try {
            mediaPlayer.prepare();
        } catch (IllegalStateException e) {
            e.fillInStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();
        mediaPlayer.setLooping(false);
    }

    @Override
    public void release() {
        if (mediaPlayer != null)
            mediaPlayer.release();
    }

    @Override
    public void logout() {
        UserInfoUtils.getInstance().clearLoginStatus();
        UserInfoUtils.getInstance().goToOauthPage(getContext());
    /*    new OneKeyModel().getKey(getContext(), this, (CustomResultListener<OneKeyLoginEvent>) event -> {
            new NetworkModel(this).oneClickLogin(getContext(), event.code, event.isUser);
        });*/
    }

    @Override
    public void login() {
        UserInfoUtils.getInstance().goToOauthPage(getContext());
        /* new OneKeyModel().getKey(getContext(), this, (CustomResultListener<OneKeyLoginEvent>) event -> {
            new NetworkModel(this).oneClickLogin(getContext(), event.code, event.isUser);
        });*/
    }

    @Override
    public void getAliKey() {
        if (UserInfoUtils.getInstance().isLogin())
            new OneKeyModel().getKey(getContext());
    }


    /**
     * 获取未读消息数
     */
    @Override
    public void getUnreadMessageNum() {

        if (mWebSocketModel != null) {
            mWebSocketModel.getUnreadMessageNumber(getContext(), data -> {
                MessageCountUtils.getInstance().setUnreadMessageNum((Integer) data);
                if (isViewAttached())
                    getV().setUnreadMsgNum((Integer) data);
            });
        }
        if (businessDistrictModel != null){
            businessDistrictModel.getNumberUnreadComments(getContext(), objects -> {
                if (objects[0] instanceof NumberUnreadCommentsBean) {
                    /**
                     * 返回商圈评论未读数
                     */
                    NumberUnreadCommentsBean unreadCommentsBean = (NumberUnreadCommentsBean) objects[0];
                    if (isViewAttached())
                        getV().setBusinessUnreadMsgNum(unreadCommentsBean.getUnread());
                }
            });
        }

    }

    @Override
    public void onMvpDestroy() {
        super.onMvpDestroy();
        if (model != null)
            model = null;
    }

    @Override
    public void onSucceed(int type) {

    }

    @Override
    public void onMessage(String msg) {
        if (isBViewAttached())
            getBV().onMessage(msg);

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
        addDisposable(subscriber);
    }
}

