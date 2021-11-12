package com.gxdingo.sg.presenter;

import android.app.Activity;

import com.gxdingo.sg.bean.UpLoadBean;
import com.gxdingo.sg.biz.IMComplaintContract;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.UpLoadImageListener;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.utils.PhotoUtils;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.zhouyou.http.subsciber.BaseSubscriber;

import java.util.List;

public class IMComplaintPresenter extends BaseMvpPresenter<BasicsListener, IMComplaintContract.IMComplaintListener> implements IMComplaintContract.IMComplaintPresenter, NetWorkListener {

    private NetworkModel networkModel;

    public IMComplaintPresenter() {
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
        addDisposable(subscriber);
    }

    /**
     * 添加投诉图片
     */
    @Override
    public void addPhoto(int num) {
        if (!isViewAttached())
            return;

        PhotoUtils.PhotoMultiple((Activity) getContext()
                , num
                , new OnResultCallbackListener<LocalMedia>() {

                    @Override
                    public void onResult(List<LocalMedia> result) {
                        if (networkModel != null && isBViewAttached() && isViewAttached()) {
                            getBV().onStarts();
                            networkModel.upLoadImages(getContext(), result, new UpLoadImageListener() {
                                @Override
                                public void loadSucceed(String path) {
                                    System.out.println("");
                                }

                                @Override
                                public void loadSucceed(UpLoadBean upLoadBean) {
                                    getBV().onAfters();
                                    getV().getPhotoDataList(upLoadBean);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }
}
