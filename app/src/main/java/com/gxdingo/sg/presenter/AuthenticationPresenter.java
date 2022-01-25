package com.gxdingo.sg.presenter;

import android.app.Activity;

import com.gxdingo.sg.bean.IdCardOCRBean;
import com.gxdingo.sg.bean.UpLoadBean;
import com.gxdingo.sg.biz.AuthenticationContract;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.UpLoadImageListener;
import com.gxdingo.sg.model.ClientNetworkModel;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.utils.PhotoUtils;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.zhouyou.http.subsciber.BaseSubscriber;

import java.io.File;
import java.util.List;

import static com.blankj.utilcode.util.FileUtils.createOrExistsDir;
import static com.gxdingo.sg.utils.PhotoUtils.getPhotoUrl;
import static com.kikis.commnlibrary.utils.CommonUtils.getPath;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * @author: Kikis
 * @date: 2021/12/30
 * @page:
 */
public class AuthenticationPresenter extends BaseMvpPresenter<BasicsListener, AuthenticationContract.AuthenticationListener> implements AuthenticationContract.AuthenticationPresenter, NetWorkListener, OnResultCallbackListener<LocalMedia> {

    private NetworkModel mNetworkModel;


    public AuthenticationPresenter() {
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
    public void photoItemClick(int pos) {

    }

    /**
     * 提交认证信息
     */
    @Override
    public void submitAuthenticationInfo() {
        if (!isViewAttached())
            return;

        else if (isEmpty(getV().getIdCardName()))
            getBV().onMessage("请填写身份证名称");
        else if (isEmpty(getV().getIdCardNumber()))
            getBV().onMessage("请填写身份证号码");


        if (mNetworkModel != null)
            mNetworkModel.certification(getContext(), getV().getIdCardName(), getV().getIdCardNumber(), "frontImg", "backImg", new CustomResultListener() {
                @Override
                public void onResult(Object o) {
                    if (isViewAttached())
                        getBV().onSucceed(0);
                }
            });

    }

    @Override
    public void onResult(List<LocalMedia> result) {

        if (mNetworkModel != null && isBViewAttached() && isViewAttached()) {
            String url = getPhotoUrl(result.get(0));

            mNetworkModel.upLoadImage(getContext(), url, new UpLoadImageListener() {
                @Override
                public void loadSucceed(String path) {


                }

                @Override
                public void loadSucceed(UpLoadBean upLoadBean) {

                }
            }, 1);

        }

    }

    @Override
    public void onCancel() {

    }
}
