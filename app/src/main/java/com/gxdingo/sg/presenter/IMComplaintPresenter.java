package com.gxdingo.sg.presenter;

import android.app.Activity;

import com.gxdingo.sg.bean.ArticleListBean;
import com.gxdingo.sg.bean.UpLoadBean;
import com.gxdingo.sg.bean.WebBean;
import com.gxdingo.sg.biz.IMComplaintContract;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.UpLoadImageListener;
import com.gxdingo.sg.model.ClientNetworkModel;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.utils.PhotoUtils;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.kikis.commnlibrary.view.GridPictureEditing;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.zhouyou.http.subsciber.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.isEmpty;

public class IMComplaintPresenter extends BaseMvpPresenter<BasicsListener, IMComplaintContract.IMComplaintListener> implements IMComplaintContract.IMComplaintPresenter, NetWorkListener {

    private NetworkModel networkModel;

    private ClientNetworkModel mClientNetworkModel;

    public IMComplaintPresenter() {
        networkModel = new NetworkModel(this);
        mClientNetworkModel = new ClientNetworkModel(this);
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

        if (isViewAttached()) {
            if (o instanceof ArticleListBean) {
                getV().onArticleListResult(((ArticleListBean) o).getList());
            }
        }
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

    @Override
    public void getDataList(String identifier) {
        if (mClientNetworkModel != null)
            mClientNetworkModel.getArticleList(getContext(), 0, identifier);
    }

    @Override
    public void complaint(String reason, String content, ArrayList<GridPictureEditing.PictureValue> values, String sendIdentifier) {
        if (networkModel != null) {
            List<String> list = new ArrayList<>();
            for (GridPictureEditing.PictureValue value : values) {
                list.add(isEmpty(value.thumbnailUrl) ? value.url : value.thumbnailUrl);
            }
            networkModel.complaintMessage(getContext(), reason, content, list, sendIdentifier);
        }
    }
}
