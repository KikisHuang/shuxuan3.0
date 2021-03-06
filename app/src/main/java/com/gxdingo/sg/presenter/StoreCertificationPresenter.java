package com.gxdingo.sg.presenter;

import android.app.Activity;
import android.content.Context;

import com.blankj.utilcode.util.ClipboardUtils;
import com.gxdingo.sg.bean.BusinessScopeEvent;
import com.gxdingo.sg.bean.StoreBusinessScopeBean;
import com.gxdingo.sg.bean.StoreCategoryBean;
import com.gxdingo.sg.bean.UpLoadBean;
import com.gxdingo.sg.bean.UserBean;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.StoreCertificationContract;
import com.gxdingo.sg.biz.UpLoadImageListener;
import com.gxdingo.sg.model.CommonModel;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.model.ShibbolethModel;
import com.gxdingo.sg.model.StoreNetworkModel;
import com.gxdingo.sg.utils.GlideEngine;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.activitiy.BaseActivity;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.kikis.commnlibrary.utils.RxUtil;
import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.zhouyou.http.subsciber.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static com.blankj.utilcode.util.StringUtils.isEmpty;
import static com.gxdingo.sg.utils.PhotoUtils.getPhotoUrl;
import static com.luck.picture.lib.config.PictureMimeType.ofImage;

public class StoreCertificationPresenter extends BaseMvpPresenter<BasicsListener, StoreCertificationContract.StoreCertificationListener> implements StoreCertificationContract.StoreCertificationPresenter, NetWorkListener {

    private NetworkModel networkModel;

    private CommonModel mCommonModel;

    private StoreNetworkModel storeNetworkModel;

    public StoreCertificationPresenter() {

        networkModel = new NetworkModel(this);
        mCommonModel = new CommonModel();
        storeNetworkModel = new StoreNetworkModel(this);
    }

    @Override
    public void onSucceed(int type) {
        //??????????????????????????????
        if (type == 100)
            getLoginInfoStatus();
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

    }

    @Override
    public void onData(boolean refresh, Object o) {
        if (isViewAttached()) {
            if (o instanceof StoreBusinessScopeBean) {
                StoreBusinessScopeBean businessScopeBean = (StoreBusinessScopeBean) o;

                if (businessScopeBean.getList() != null && businessScopeBean.getList().size() > 0) {
                    //??????item????????????
                    RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {
                        for (int i = 0; i < businessScopeBean.getList().size(); i++) {
                            if (businessScopeBean.getList().get(i).getId() == 0) {
                                businessScopeBean.getList().remove(i);
                                break;
                            }
                        }
                        e.onNext(0);
                        e.onComplete();
                    }), ((BaseActivity) getContext())).subscribe(data -> {
                        getV().onBusinessScopeResult(businessScopeBean.getList());
                    });
                }
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
    public void photoItemClick(int type, int pos, boolean isCrop) {
        boolean gallery = pos == 0;

        PictureSelector selector = PictureSelector.create((Activity) getContext());

        PictureSelectionModel model = gallery ? selector.openGallery(ofImage()) : selector.openCamera(ofImage());

        if (isCrop) {
            model.withAspectRatio(1, 1)
                    .circleDimmedLayer(type == 1);//???????????????
        }
        model.selectionMode(PictureConfig.SINGLE).
                loadImageEngine(GlideEngine.createGlideEngine())// ?????????????????? ?????? implements ImageEngine??????
                .isEnableCrop(isCrop)
                .compress(true)//????????????
                .showCropFrame(false)// ?????????????????????????????? ???????????????????????????false
                .showCropGrid(false)//?????????????????????????????? ???????????????????????????false
                .rotateEnabled(false)//???????????????????????????
                .scaleEnabled(true)//?????????????????????????????????
                .minimumCompressSize(500)//??????1024kb?????????
                .synOrAsy(true)//????????????or????????????
                .forResult(new OnResultCallbackListener<LocalMedia>() {

                    @Override
                    public void onResult(List<LocalMedia> result) {
                        if (networkModel != null && isBViewAttached() && isViewAttached()) {
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
                            }, 0);

                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }

    @Override
    public void getCategory() {
        if (storeNetworkModel != null)
            storeNetworkModel.businessScope(getContext());
    }

    @Override
    public void confirmBusinessScope(List<StoreBusinessScopeBean.ListBean> businessScopeBeans, List<StoreCategoryBean> licenceUrl) {
        if (isViewAttached())
            RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {
                String scopeName = "";
                //?????????????????????????????????
                for (StoreBusinessScopeBean.ListBean scb : businessScopeBeans) {
                    if (scb.isSelect()) {
                        if (!isEmpty(scopeName)) {
                            scopeName += "," + scb.getName();
                        } else {
                            scopeName = scb.getName();
                        }
                    }
                }
                e.onNext(new BusinessScopeEvent(licenceUrl, scopeName));
                e.onComplete();
            }), (LifecycleProvider) getContext()).subscribe(o -> {
                BusinessScopeEvent data = (BusinessScopeEvent) o;

                if (data.data.size() <= 0)
                    onMessage("???????????????????????????");
                else {
                    data.data = licenceUrl;
                    getV().closeBusinessScope(data);
                }
            });

    }

    @Override
    public void submitCertification(Context context, String avatar, String name, List<StoreCategoryBean> storeCategory, String regionPath, String address, String businessLicence, String storeLicence, double longitude, double latitude) {
        if (storeNetworkModel != null) {
            storeNetworkModel.settle(context, avatar, name, storeCategory, regionPath, address, businessLicence, storeLicence, longitude, latitude);
        }
    }

    @Override
    public void getLoginInfoStatus() {
        if (!UserInfoUtils.getInstance().isLogin())
            return;

        if (storeNetworkModel != null)
            storeNetworkModel.refreshLoginStauts(getContext(), o -> {
                UserBean data = (UserBean) o;

                if (isViewAttached()) {

                    if (data.getStore().getId() == 0) {
                        //?????????????????????
                    } else if (data.getStore().getStatus() == 20) {
                        onMessage("?????????????????????");
                        //?????????????????????
                        getV().rejected(data.getStore().rejectReason);
                    } else if (data.getStore().getStatus() == 10) {

                        UserBean locatUserBean = UserInfoUtils.getInstance().getUserInfo();
                        locatUserBean.setRole(11);
                        locatUserBean.setStore(data.getStore());
                        UserInfoUtils.getInstance().saveUserInfo(locatUserBean);

                        //???????????????????????????
                        getV().certificationPassed();

                    } else if (data.getStore().getStatus() == 0) {
                        onMessage("?????????????????????");
                        //?????????????????????
                        getV().onReview();
                    }
                }

            });
    }

    @Override
    public void logout() {
        if (networkModel != null)
            networkModel.logOut(getContext());
    }


    /**
     * ????????????????????????????????????
     */
    @Override
    public void getInvitationCode() {

        String copyContent = ClipboardUtils.getText().toString();
        //????????????????????????????????????
        if (!isEmpty(copyContent)) {
            ShibbolethModel.checkShibboleth((type, code) -> {
                if (networkModel != null) {
                    if (type == 30) {
                        networkModel.getInvitationCode(getContext(), type == 30 ? code : "", result -> {
                            //????????????????????????????????????????????????
                            if (isViewAttached()) getV().showActivityTypeLayout(type);
                        });
                    }
                }
            }, 50, true);
        }/* else {
            if (networkModel != null) {
                networkModel.getInvitationCode(getContext(), "", result -> {
                    if (isViewAttached()) getV().showActivityTypeLayout(30);
                });
            }
        }*/
    }

    /**
     * ???????????????
     *
     * @param customResultListener
     */
    @Override
    public void selectedLicence(CustomResultListener customResultListener) {

        PictureSelector selector = PictureSelector.create((Activity) getContext());

        PictureSelectionModel model = selector.openGallery(ofImage());

        model.selectionMode(PictureConfig.SINGLE).
                loadImageEngine(GlideEngine.createGlideEngine())// ?????????????????? ?????? implements ImageEngine??????
                .isEnableCrop(false)
                .compress(true)//????????????
                .minimumCompressSize(2048)//??????2048kb?????????
                .synOrAsy(true)//????????????or????????????
                .forResult(new OnResultCallbackListener<LocalMedia>() {

                    @Override
                    public void onResult(List<LocalMedia> result) {

                        if (customResultListener != null)
                            customResultListener.onResult(result.get(0));

                    }

                    @Override
                    public void onCancel() {


                    }
                });

    }


    @Override
    public void uploadOss(int position, String photoUrl) {

        if (isViewAttached() && !isEmpty(photoUrl) && networkModel != null) {
            networkModel.upLoadImage(getContext(), photoUrl, new UpLoadImageListener() {
                @Override
                public void loadSucceed(String path) {

                    getV().setOssSpecialQualificationsImg(position, path);
                }

                @Override
                public void loadSucceed(UpLoadBean upLoadBean) {

                }
            }, 0);

        }
    }
}