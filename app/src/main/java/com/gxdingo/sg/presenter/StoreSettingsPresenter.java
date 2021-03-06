package com.gxdingo.sg.presenter;

import android.app.Activity;

import com.gxdingo.sg.bean.DistanceListBean;
import com.gxdingo.sg.bean.StoreDetail;
import com.gxdingo.sg.bean.StoreDetailBean;
import com.gxdingo.sg.bean.StoreQRCodeBean;
import com.gxdingo.sg.bean.UpLoadBean;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.StoreSettingsContract;
import com.gxdingo.sg.biz.UpLoadImageListener;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.model.StoreNetworkModel;
import com.gxdingo.sg.utils.GlideEngine;
import com.gxdingo.sg.utils.UserInfoUtils;
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
import static com.luck.picture.lib.config.PictureMimeType.s;

/**
 * @author: Weaving
 * @date: 2021/11/10
 * @page:
 */
public class StoreSettingsPresenter extends BaseMvpPresenter<BasicsListener, StoreSettingsContract.StoreSettingsListener>
        implements OnResultCallbackListener<LocalMedia>, StoreSettingsContract.StoreSettingsPresenter, NetWorkListener {

    private NetworkModel networkModel;

    private StoreNetworkModel storeNetworkModel;

    public StoreSettingsPresenter() {

        networkModel = new NetworkModel(this);
        storeNetworkModel = new StoreNetworkModel(this);
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
        if (o instanceof StoreQRCodeBean)
            getV().onQRResult((StoreQRCodeBean) o);
        else if (o instanceof DistanceListBean) {
            DistanceListBean distanceListBean = (DistanceListBean) o;
            if (isViewAttached() && distanceListBean.getList() != null)
                getV().onDistanceResult(distanceListBean.getList());
        } else if (o instanceof StoreDetail) {
            if (isViewAttached())
                getV().onInfoResult((StoreDetail) o);
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
    public void getStoreInfo(String id) {
        if (storeNetworkModel != null)
            storeNetworkModel.getStoreDetails(getContext(), id);
    }

    @Override
    public void photoItemClick(int pos) {
        boolean gallery = pos == 0;

        PictureSelector selector = PictureSelector.create((Activity) getContext());

        PictureSelectionModel model = gallery ? selector.openGallery(ofImage()) : selector.openCamera(ofImage());

        model.selectionMode(PictureConfig.SINGLE).
                loadImageEngine(GlideEngine.createGlideEngine())// ?????????????????? ?????? implements ImageEngine??????
                .enableCrop(true)//????????????
                .compress(true)//????????????
                .enableCrop(true)
                .withAspectRatio(1, 1)
                .circleDimmedLayer(true)
                .showCropFrame(false)// ?????????????????????????????? ???????????????????????????false
                .showCropGrid(false)//?????????????????????????????? ???????????????????????????false
                .rotateEnabled(false)//???????????????????????????
                .scaleEnabled(true)//?????????????????????????????????
                .minimumCompressSize(200)//??????1024kb?????????
                .synOrAsy(true)//????????????or????????????
                .forResult(this);
    }

    @Override
    public void getDistanceList() {
        if (storeNetworkModel != null)
            storeNetworkModel.getDeliveryScope(getContext());
    }

    @Override
    public void getQrCode() {
        if (storeNetworkModel != null)
            storeNetworkModel.getExclusiveQRcode(getContext());
    }


    @Override
    public void updateStoreName(String name) {
        if (storeNetworkModel != null)
            storeNetworkModel.updateStoreName(getContext(), name);
    }

    @Override
    public void changMobile(String mobile) {
        if (storeNetworkModel != null)
            storeNetworkModel.updateContractNumber(getContext(), mobile);
    }

    @Override
    public void businessTime(String startTime, String endTime) {
        if (storeNetworkModel != null)
            storeNetworkModel.updateBusinessTime(getContext(), startTime, endTime);
    }

    @Override
    public void deliveryScope(String scope) {
        if (storeNetworkModel != null)
            storeNetworkModel.updateMaxDistance(getContext(), scope);
    }

    @Override
    public void getAuthInfo() {
        if (storeNetworkModel != null)
            storeNetworkModel.getAuthInfo(getContext(), UserInfoUtils.getInstance().getIdentifier());
    }

    @Override
    public void updateBusinessStatus(int status) {

        if (storeNetworkModel != null)
            storeNetworkModel.updateBusinessStatus(getContext(), status, o -> {


                if (isViewAttached())
                    getV().changeBusinessStatus(status);
            });

    }


    @Override
    public void onResult(List<LocalMedia> result) {
        String url = getPhotoUrl(result.get(0));

        if (isViewAttached() && result != null && networkModel != null) {
            networkModel.upLoadImage(getContext(), url, new UpLoadImageListener() {
                @Override
                public void loadSucceed(String path) {
                    if (storeNetworkModel != null) {
                        storeNetworkModel.updateStoreAvatar(getContext(), path);
                    }
//                    getV().changeAvatar(getPhotoUrl(result.get(0)));
                }

                @Override
                public void loadSucceed(UpLoadBean upLoadBean) {

                }
            }, 0);
        }
    }

    @Override
    public void onCancel() {

    }
}
