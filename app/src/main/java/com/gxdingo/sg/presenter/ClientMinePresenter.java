package com.gxdingo.sg.presenter;

import android.app.Activity;
import android.widget.EditText;

import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientMineBean;
import com.gxdingo.sg.bean.NormalBean;
import com.gxdingo.sg.bean.UpLoadBean;
import com.gxdingo.sg.bean.UserBean;
import com.gxdingo.sg.biz.ClientMineContract;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.PermissionsListener;
import com.gxdingo.sg.biz.UpLoadImageListener;
import com.gxdingo.sg.model.ClientMainModel;
import com.gxdingo.sg.model.ClientMineModel;
import com.gxdingo.sg.model.ClientNetworkModel;
import com.gxdingo.sg.model.CommonModel;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.model.StoreNetworkModel;
import com.gxdingo.sg.utils.GlideEngine;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.CustomResultListener;
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
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientMinePresenter extends BaseMvpPresenter<BasicsListener, ClientMineContract.ClientMineListener> implements OnResultCallbackListener<LocalMedia>, ClientMineContract.ClientMinePresenter, NetWorkListener {

    private ClientNetworkModel clientNetworkModel;

    private StoreNetworkModel storeNetworkModel;

    private NetworkModel networkModel;

    private CommonModel mClientCommonModel;

    private ClientMineModel mineModel;


    public ClientMinePresenter() {
        storeNetworkModel = new StoreNetworkModel(this);
        clientNetworkModel = new ClientNetworkModel(this);
        networkModel = new NetworkModel(this);
        mClientCommonModel = new CommonModel();
        mineModel = new ClientMineModel();
    }

    @Override
    public void onSucceed(int type) {
        if (isBViewAttached()) {
            if (type == 100)
                refreshStatus();
            else
                getBV().onSucceed(type);
        }

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
            if (o instanceof ClientMineBean) {
                ClientMineBean cmb = (ClientMineBean) o;
                getV().onMineDataResult(cmb);
                if (mineModel != null&& UserInfoUtils.getInstance().getUserInfo().getRole()==11){
                    mineModel.getQualification(getContext(), cmb.getCategoryList(), v -> {
                        if (isViewAttached())
                            getV().onQualification(v);

                    });
                }

            } else if (o instanceof NormalBean)
                //?????????????????????????????????????????????
                getV().onRemindResult(((NormalBean) o).remindValue);
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
                // ????????????
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
    public void getUserInfo() {
        if (clientNetworkModel != null)
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
        if (networkModel != null)
            networkModel.logOut(getContext());
    }

    @Override
    public void loginOff(int c) {
        if (networkModel != null)
            networkModel.logOff(getContext(), c);
    }

    @Override
    public void scan(RxPermissions rxPermissions) {
        if (mClientCommonModel != null)
            mClientCommonModel.checkPermission(rxPermissions, new String[]{CAMERA, VIBRATE}, new PermissionsListener() {
                @Override
                public void onNext(boolean value) {
                    if (!value) {
                        getBV().onFailed();
                    } else {
                        getBV().onSucceed(1);
                    }
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
    }

    @Override
    public void scanCode(String content) {
        if (clientNetworkModel != null)
            clientNetworkModel.receiveCoupon(getContext(), content, null);
    }

    @Override
    public void getNoRemindContent() {
        if (storeNetworkModel != null)
            storeNetworkModel.getScanningInfo(getContext());
    }

    @Override
    public void storeScanCode(String scanContent) {
        if (storeNetworkModel != null)
            storeNetworkModel.scanCode(getContext(), scanContent);
    }

    /**
     * ????????????
     */
    @Override
    public void refreshStatus() {
        if (storeNetworkModel != null)
            storeNetworkModel.refreshLoginStauts(getContext(), o -> {
                UserBean userBean = (UserBean) o;

                if (isViewAttached())
                    getV().onStatusResult(userBean);

            });
    }

    /**
     * ???????????????
     *
     * @param article
     */
    @Override
    public void getArticleImg(String article) {
        if (clientNetworkModel != null)
            clientNetworkModel.getArticleImage(getContext(), article);
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
            }, 0);
        }
    }


    @Override
    public void onCancel() {

    }
}
