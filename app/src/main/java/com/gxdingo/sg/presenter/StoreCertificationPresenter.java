package com.gxdingo.sg.presenter;

import android.app.Activity;
import android.content.Context;

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
import com.gxdingo.sg.model.StoreNetworkModel;
import com.gxdingo.sg.utils.GlideEngine;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.biz.BasicsListener;
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
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
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
        //提交审核成功刷新状态
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
                if (businessScopeBean.getList() != null && businessScopeBean.getList().size() > 0)
                    getV().onBusinessScopeResult(businessScopeBean.getList());
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

    }

    @Override
    public void photoItemClick(int type, int pos, boolean isCrop) {
        boolean gallery = pos == 0;

        PictureSelector selector = PictureSelector.create((Activity) getContext());

        PictureSelectionModel model = gallery ? selector.openGallery(ofImage()) : selector.openCamera(ofImage());

        if (isCrop) {
            model.withAspectRatio(1, 1)
                    .circleDimmedLayer(type == 1);//圆形变暗层
        }
        model.selectionMode(PictureConfig.SINGLE).
                loadImageEngine(GlideEngine.createGlideEngine())// 图片加载引擎 需要 implements ImageEngine接口
                .isEnableCrop(isCrop)
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
                            });

                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }

    @Override
    public void getCategory() {
        storeNetworkModel.businessScope(getContext());
    }

    @Override
    public void confirmBusinessScope(List<StoreBusinessScopeBean.ListBean> businessScopeBeans) {
        if (isViewAttached())
            RxUtil.observe(Schedulers.newThread(), Observable.create(new ObservableOnSubscribe<Object>() {
                @Override
                public void subscribe(ObservableEmitter<Object> e) throws Exception {
                    List<StoreCategoryBean> data = new ArrayList<>();
                    String scopeName = "";
                    for (StoreBusinessScopeBean.ListBean scb : businessScopeBeans) {
                        if (scb.isSelect()) {
                            StoreCategoryBean bsb = new StoreCategoryBean(scb.getId(), scb.getPath());
                            data.add(bsb);
                            if (!isEmpty(scopeName)) {
                                scopeName += "," + scb.getName();
                            } else {
                                scopeName = scb.getName();
                            }

                        }
                    }
                    e.onNext(new BusinessScopeEvent(data, scopeName));
                    e.onComplete();
                }
            }), (LifecycleProvider) getContext()).subscribe(new Consumer<Object>() {
                @Override
                public void accept(Object o) throws Exception {
                    BusinessScopeEvent data = (BusinessScopeEvent) o;
                    if (data.data.size() <= 0)
                        onMessage("请最少选择一个品类");
                    else {
                        getV().closeBusinessScope(data);
                    }
                }

            });

    }

    @Override
    public void submitCertification(Context context, String avatar, String name, List<StoreCategoryBean> storeCategory, String regionPath, String address, String businessLicence, double longitude, double latitude) {
        if (storeNetworkModel != null) {
            storeNetworkModel.settle(context, avatar, name, storeCategory, regionPath, address, businessLicence, longitude, latitude);
        }
    }

    @Override
    public void getLoginInfoStatus() {
        if (storeNetworkModel != null)
            storeNetworkModel.refreshLoginStauts(getContext(), o -> {
                UserBean data = (UserBean) o;
                int status = data.getStore().getStatus();
                int storeId = data.getStore().getId();

                UserBean userBean = UserInfoUtils.getInstance().getUserInfo();

                userBean.getStore().setId(storeId);

                userBean.getStore().setStatus(status);

                UserInfoUtils.getInstance().saveLoginUserInfo(userBean);

                if (isViewAttached()) {

                    if (data.getStore().getId() == 0) {
                        //未提交认证流程
                    } else if (data.getStore().getStatus() == 20) {
                        //回调显示被驳回
                        getV().rejected();
                    } else if (data.getStore().getStatus() == 10) {
                        //回调显示已认证通过
                        getV().certificationPassed();
                    } else if (data.getStore().getStatus() == 0) {
                        //回调显示在审核
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
}
