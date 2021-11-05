package com.gxdingo.sg.presenter;

import android.app.Activity;

import com.gxdingo.sg.bean.UpLoadBean;
import com.gxdingo.sg.biz.IMChatContract;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.UpLoadImageListener;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.utils.GlideEngine;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.zhouyou.http.subsciber.BaseSubscriber;

import java.util.List;

import static com.gxdingo.sg.utils.PhotoUtils.getPhotoUrl;
import static com.luck.picture.lib.config.PictureMimeType.ofImage;

public class IMChatPresenter extends BaseMvpPresenter<BasicsListener, IMChatContract.IMChatListener> implements IMChatContract.IMChatPresenter, NetWorkListener {
    private NetworkModel networkModel;

    public IMChatPresenter() {
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

    /**
     * 照片来源点击
     *
     * @param pos 0 相册，1 相机
     */
    @Override
    public void photoSourceClick(int pos) {
        boolean gallery = pos == 0;
        PictureSelector selector = PictureSelector.create((Activity) getContext());
        PictureSelectionModel model = gallery ? selector.openGallery(ofImage()) : selector.openCamera(ofImage());
        model.selectionMode(PictureConfig.SINGLE).
                loadImageEngine(GlideEngine.createGlideEngine())// 图片加载引擎 需要 implements ImageEngine接口
                .compress(true)//是否压缩
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)//是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .rotateEnabled(false)//裁剪是否可旋转图片
                .scaleEnabled(true)//裁剪是否可放大缩小图片
                .minimumCompressSize(500)//小于1024kb不压缩
                .synOrAsy(true)//开启同步or异步压缩
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        getBV().onStarts();
                        String url = getPhotoUrl(result.get(0));
                        networkModel.upLoadImage(getContext(), url, new UpLoadImageListener() {
                            @Override
                            public void loadSucceed(String path) {
                                getV().uploadImage(path);
                                getBV().onAfters();
                            }

                            @Override
                            public void loadSucceed(UpLoadBean upLoadBean) {

                            }
                        });
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }
}
