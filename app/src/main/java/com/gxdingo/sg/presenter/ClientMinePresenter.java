package com.gxdingo.sg.presenter;

import android.app.Activity;
import android.widget.EditText;

import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientMineBean;
import com.gxdingo.sg.bean.UpLoadBean;
import com.gxdingo.sg.biz.ClientMineContract;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.UpLoadImageListener;
import com.gxdingo.sg.model.ClientMainModel;
import com.gxdingo.sg.model.ClientMineModel;
import com.gxdingo.sg.model.ClientNetworkModel;
import com.gxdingo.sg.model.CommonModel;
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

import static android.text.TextUtils.isEmpty;
import static com.gxdingo.sg.utils.PhotoUtils.getPhotoUrl;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.luck.picture.lib.config.PictureMimeType.ofImage;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientMinePresenter extends BaseMvpPresenter<BasicsListener, ClientMineContract.ClientMineListener> implements OnResultCallbackListener<LocalMedia>, ClientMineContract.ClientMinePresenter, NetWorkListener {

    private ClientNetworkModel clientNetworkModel;

    private NetworkModel networkModel;

    private CommonModel mClientCommonModel;

    private ClientMineModel mineModel;


    public ClientMinePresenter() {
        clientNetworkModel = new ClientNetworkModel(this);
        networkModel = new NetworkModel(this);
        mClientCommonModel = new CommonModel();
        mineModel = new ClientMineModel();
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
        if (isViewAttached()){
            if (o instanceof ClientMineBean)
                getV().onMineDataResult((ClientMineBean) o);
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


    @Override
    public void editsetInit(EditText nick_name_edt, int limit) {
        if (isViewAttached()) {
            CommonModel clientCommonModel = new CommonModel();
            clientCommonModel.editTextFilters(nick_name_edt, limit);
            mineModel.edittextFocusChangeListener(nick_name_edt, (v, hasFocus) -> {
                // 获得焦点
//                if (isViewAttached())
//                    getV().showSaveBtn(hasFocus);
            });
        }
    }

    @Override
    public void photoItemClick(int pos) {
        boolean gallery = pos == 0;

        PictureSelector selector = PictureSelector.create((Activity) getContext());

        PictureSelectionModel model = gallery ? selector.openGallery(ofImage()) : selector.openCamera(ofImage());

        model.selectionMode(PictureConfig.SINGLE).
                loadImageEngine(GlideEngine.createGlideEngine())// 图片加载引擎 需要 implements ImageEngine接口
                .enableCrop(true)//是否裁剪
                .compress(true)//是否压缩
                .enableCrop(true)
                .withAspectRatio(1, 1)
                .circleDimmedLayer(true)
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)//是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .rotateEnabled(false)//裁剪是否可旋转图片
                .scaleEnabled(true)//裁剪是否可放大缩小图片
                .minimumCompressSize(200)//小于1024kb不压缩
                .synOrAsy(true)//开启同步or异步压缩
                .forResult(this);
    }

    @Override
    public void getUserInfo() {
        if (clientNetworkModel!=null)
            clientNetworkModel.getMineData(getContext());
    }

    @Override
    public void modityNickName(String name) {
        if (!isEmpty(name))
            clientNetworkModel.userEdit(getContext(), "", name);
        else
            onMessage(gets(R.string.nickname_cannot_null));
    }

    @Override
    public void logout() {
        if (networkModel!=null)
            networkModel.logOut(getContext());
    }

    @Override
    public void onResult(List<LocalMedia> result) {
        String url = getPhotoUrl(result.get(0));

        if (isViewAttached() && result != null && networkModel != null) {
            networkModel.upLoadImage(getContext(), url, new UpLoadImageListener() {
                @Override
                public void loadSucceed(String path) {
                    if (clientNetworkModel != null) {
                        clientNetworkModel.userEdit(getContext(), path, "");
                    }
                    getV().changeAvatar(getPhotoUrl(result.get(0)));
                }

                @Override
                public void loadSucceed(UpLoadBean upLoadBean) {

                }
            });
        }
    }

    @Override
    public void onCancel() {

    }
}
