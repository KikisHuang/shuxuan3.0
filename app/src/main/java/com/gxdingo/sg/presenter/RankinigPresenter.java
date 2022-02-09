package com.gxdingo.sg.presenter;

import android.app.Activity;
import android.widget.EditText;

import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientMineBean;
import com.gxdingo.sg.bean.NormalBean;
import com.gxdingo.sg.bean.UpLoadBean;
import com.gxdingo.sg.biz.ClientMineContract;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.PermissionsListener;
import com.gxdingo.sg.biz.RankingContract;
import com.gxdingo.sg.biz.UpLoadImageListener;
import com.gxdingo.sg.model.ClientMineModel;
import com.gxdingo.sg.model.ClientNetworkModel;
import com.gxdingo.sg.model.CommonModel;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.model.StoreNetworkModel;
import com.gxdingo.sg.utils.GlideEngine;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhouyou.http.subsciber.BaseSubscriber;

import java.util.List;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.VIBRATE;
import static android.text.TextUtils.isEmpty;
import static com.gxdingo.sg.utils.PhotoUtils.getPhotoUrl;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.luck.picture.lib.config.PictureMimeType.ofImage;

/**
 * @author: Kikis
 * @date: 2022/2/9
 * @page:
 */
public class RankinigPresenter extends BaseMvpPresenter<BasicsListener, RankingContract.RankingListener> implements RankingContract.RankingPresenter, NetWorkListener {

    private ClientNetworkModel clientNetworkModel;

    private CommonModel mClientCommonModel;


    public RankinigPresenter() {
        clientNetworkModel = new ClientNetworkModel(this);
        mClientCommonModel = new CommonModel();
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
        if (isViewAttached()) {


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
