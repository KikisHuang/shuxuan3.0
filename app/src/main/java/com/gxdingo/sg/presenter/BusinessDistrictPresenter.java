package com.gxdingo.sg.presenter;

import android.app.Activity;
import android.content.Intent;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.gxdingo.sg.activity.StoreBusinessDistrictReleaseActivity;
import com.gxdingo.sg.activity.StoreCertificationActivity;
import com.gxdingo.sg.bean.BannerBean;
import com.gxdingo.sg.bean.BusinessDistrictListBean;
import com.gxdingo.sg.bean.BusinessDistrictCommentOrReplyBean;
import com.gxdingo.sg.bean.BusinessDistrictUnfoldCommentListBean;
import com.gxdingo.sg.bean.NumberUnreadCommentsBean;
import com.gxdingo.sg.bean.UserBean;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.PermissionsListener;
import com.gxdingo.sg.biz.StoreBusinessDistrictContract;
import com.gxdingo.sg.model.BusinessDistrictModel;
import com.gxdingo.sg.model.ClientNetworkModel;
import com.gxdingo.sg.model.CommonModel;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.model.StoreNetworkModel;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.ShareUtils;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.activitiy.BaseActivity;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.biz.MultiParameterCallbackListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.kikis.commnlibrary.utils.RxUtil;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.ninegrid.ImageInfo;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhouyou.http.subsciber.BaseSubscriber;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static cc.shinichi.library.ImagePreview.LoadStrategy.NetworkAuto;
import static com.blankj.utilcode.util.ActivityUtils.startActivity;
import static com.blankj.utilcode.util.PermissionUtils.isGranted;
import static com.blankj.utilcode.util.TimeUtils.getNowMills;
import static com.gxdingo.sg.utils.LocalConstant.ADD;
import static com.kikis.commnlibrary.utils.IntentUtils.getImagePreviewInstance;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * ???????????????Presenter
 *
 * @author JM
 */
public class BusinessDistrictPresenter extends BaseMvpPresenter<BasicsListener, StoreBusinessDistrictContract.StoreBusinessDistrictListener>
        implements StoreBusinessDistrictContract.StoreBusinessDistrictPresenter, NetWorkListener, MultiParameterCallbackListener {

    private ClientNetworkModel clientNetworkModel;
    private StoreNetworkModel storeNetworkModel;

    private BusinessDistrictModel businessDistrictModel;
    private CommonModel commonModel;
    private NetworkModel mNetWorkModel;


    public BusinessDistrictPresenter() {
        businessDistrictModel = new BusinessDistrictModel(this);
        commonModel = new CommonModel();
        mNetWorkModel = new NetworkModel(this);
        storeNetworkModel = new StoreNetworkModel(this);
        clientNetworkModel = new ClientNetworkModel(this);
    }

    @Override
    public void onSucceed(int type) {
        //????????????????????????
        if (type == 200) {
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
        if (isBViewAttached()) {
            //??????????????????
            if (o instanceof BusinessDistrictListBean) {
                RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {
                    BusinessDistrictListBean districtListBean = (BusinessDistrictListBean) o;

                    for (BusinessDistrictListBean.BusinessDistrict businessDistrict : districtListBean.getList()) {
                        if (businessDistrict.getImages() != null && businessDistrict.getImages().size() > 0) {
                            for (String img : businessDistrict.getImages()) {
                                ImageInfo info = new ImageInfo();
                                info.setThumbnailUrl(img);
                                info.setBigImageUrl(img);
                                businessDistrict.imageInfos.add(info);
                            }
                        }
                    }
                    e.onNext(districtListBean);
                    e.onComplete();
                }), (BaseActivity) getContext()).subscribe(data -> {
                    getV().onBusinessDistrictData(refresh, (BusinessDistrictListBean) data);
                });

            }
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

    /**
     * ??????????????????
     */
    @Override
    public void getBusinessDistrictList(boolean refresh, String circleUserIdentifier) {
        new Thread(() -> SPUtils.getInstance().put(LocalConstant.LAST_VIEW_TIME,getNowMills())).start();

        businessDistrictModel.getBusinessDistrict(getContext(), refresh, circleUserIdentifier, "");

    }

    @Override
    public void shareGetBusinessDistrictList(String id, String circleCode) {
        businessDistrictModel.getBusinessDistrict(getContext(), true, id, circleCode);
    }

    /**
     * ????????????/??????
     *
     * @param businessDistrict ??????/???????????????
     * @param circleId         ??????ID
     * @param parentId         ??????????????????id
     * @param content          ??????
     */
    @Override
    public void submitCommentOrReply(BusinessDistrictListBean.BusinessDistrict businessDistrict, long circleId, long parentId, String content) {
        businessDistrictModel.commentOrReply(getContext(), businessDistrict, circleId, parentId, content, this);
    }

    /**
     * ????????????????????????????????????
     *
     * @param businessDistrict ??????
     * @param circleId         ??????ID
     * @param current          ??????
     * @param size             ????????????
     */
    @Override
    public void getUnfoldCommentList(BusinessDistrictListBean.BusinessDistrict businessDistrict, long circleId, int current, int size) {
        businessDistrictModel.getCommentList(getContext(), businessDistrict, circleId, current, size, this);
    }

    /**
     * ????????????????????????
     *
     * @param commentList       ?????????????????????
     * @param unfoldCommentList ?????????????????????????????????
     * @param businessDistrict
     * @param total
     */
    @Override
    public void onDuplicateRemovalMerge(ArrayList<BusinessDistrictListBean.Comment> commentList
            , ArrayList<BusinessDistrictUnfoldCommentListBean.UnfoldComment> unfoldCommentList, BusinessDistrictListBean.BusinessDistrict businessDistrict, int total) {

        RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {
            if (commentList != null && unfoldCommentList != null) {
                for (int i = 0; i < unfoldCommentList.size(); i++) {
                    BusinessDistrictUnfoldCommentListBean.UnfoldComment uc = unfoldCommentList.get(i);
                    //?????????????????????commentList???????????????
                    Iterator<BusinessDistrictListBean.Comment> iterator = commentList.iterator();
                    while (iterator.hasNext()) {
                        BusinessDistrictListBean.Comment c = iterator.next();
                        if (uc.getId() == c.getId()) {
                            iterator.remove();
                        }
                    }
                }
                //????????????
                commentList.addAll(unfoldCommentList);
            /*    //??????ID???????????????????????????
                Collections.sort(commentList);*/
            }
            e.onNext(0);
            e.onComplete();
        }), (BaseActivity) getContext()).subscribe(o -> {
            if (isViewAttached())
                getV().onCommentListRefresh(commentList, unfoldCommentList, businessDistrict, total);
        });

    }

    /**
     * ??????????????????????????????
     */
    @Override
    public void getNumberUnreadComments() {
        if (businessDistrictModel != null)
            businessDistrictModel.getNumberUnreadComments(getContext(), this);
    }

    /**
     * ????????????????????????
     *
     * @param id ??????ID
     */
    @Override
    public void deleteBusinessDistrictDynamics(long id) {
        if (businessDistrictModel != null)
            businessDistrictModel.storeDeleteBusinessDistrictDynamics(getContext(), id);
    }

    @Override
    public void PhotoViewer(ArrayList<String> images, int position) {
        if (images != null && images.size() > 0)
            getImagePreviewInstance((Activity) getContext(), NetworkAuto, position, true).setImageList(images).start();
    }

    @Override
    public void complete(String identifier) {
        if (clientNetworkModel != null)
            clientNetworkModel.completeTask(getContext(), identifier, 10);
    }

    /**
     * ?????? or ????????????
     *
     * @param status
     * @param id
     * @param position
     */
    @Override
    public void likedOrUnliked(int status, long id, int position) {

        if (businessDistrictModel != null)
            businessDistrictModel.likedOrUnliked(getContext(), status, id, (CustomResultListener<String>) o -> {

                if (isViewAttached())
                    getV().refreshLikeNum(o, position, status);

            });

    }

    /**
     * ??????
     *
     * @param content
     * @param imgUrl
     * @param url
     */
    @Override
    public void shareLink(String content, String imgUrl, String url) {


        ShareUtils.UmShare(getContext(), new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {

            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {

            }
        }, url, content, content, imgUrl, SHARE_MEDIA.WEIXIN);


    }

    @Override
    public void checkLocationPermission(RxPermissions rxPermissions, String mcircleUserIdentifier) {
        //????????????????????????????????????
        if (!isGranted(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)) {
            getBusinessDistrictList(true, mcircleUserIdentifier);
            return;
        }

        if (commonModel != null) {
            commonModel.checkPermission(rxPermissions, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, new PermissionsListener() {
                @Override
                public void onNext(boolean value) {
                    if (isViewAttached() && isBViewAttached()) {
                        //??????????????????????????????????????????????????????????????????
                        if (value) {
                            mNetWorkModel.location(getContext(), aMapLocation -> {
                                if (aMapLocation.getErrorCode() == 0) {

                                    LocalConstant.AdCode = aMapLocation.getAdCode();

                                    LocalConstant.lat = aMapLocation.getLatitude();

                                    LocalConstant.lon = aMapLocation.getLongitude();
                                    LocalConstant.AoiName = aMapLocation.getPoiName();
                                    //??????????????????
                                    getBusinessDistrictList(true, mcircleUserIdentifier);
                                } else
                                    LogUtils.e("(aMapLocation.getErrorCode() == " + aMapLocation.getErrorCode());
                            });
                        }
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
    }

    /**
     * ????????????/??????/??????
     */
    @Override
    public void getBannerDataInfo() {

        if (businessDistrictModel != null)
            businessDistrictModel.getBannerDataInfo(getContext(), o -> {
                BannerBean data = (BannerBean) o;

                RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {
                    List<String> notices = new ArrayList<>();
                    for (BannerBean.NoticeListDTO d : data.getNoticeList()) {
                        if (!isEmpty(d.getTitle()))
                            notices.add(d.getTitle());

                    }
                    e.onNext(notices);
                    e.onComplete();
                }), (BaseActivity) getContext()).subscribe(o1 -> {

                    List<String> data1 = (List<String>) o1;
                    data.noticeStringList = data1;
                    if (isViewAttached())
                        getV().onBannerResult(data);
                });

            });

    }


    @Override
    public void refreshUserStatus() {
        if (storeNetworkModel != null) {
            storeNetworkModel.refreshLoginStauts(getContext(), o -> {
                UserBean userBean = (UserBean) o;
                if (userBean.getStore() != null) {

                    int status = userBean.getStore().getStatus();

                    if (status == 10) {
                        UserBean locatUserBean = UserInfoUtils.getInstance().getUserInfo();
                        if (locatUserBean.getRole() == 10) {
                            locatUserBean.setRole(11);
                            locatUserBean.setStore(userBean.getStore());
                            UserInfoUtils.getInstance().saveUserInfo(locatUserBean);
                            startActivity(new Intent(getContext(), StoreBusinessDistrictReleaseActivity.class));
                        }
                    } else
                        getV().showAuthenticationDialog();
                }
            });
        }
    }

    /**
     * Model??????????????????
     *
     * @param objects ??????
     */
    @Override
    public void multipleDataResult(Object... objects) {
        if (isBViewAttached()) {
            if (objects != null && objects.length > 0) {
                if (objects[0] instanceof BusinessDistrictCommentOrReplyBean) {
                    /**
                     * ??????????????????/??????
                     */
                    BusinessDistrictListBean.BusinessDistrict businessDistrict = (BusinessDistrictListBean.BusinessDistrict) objects[1];
                    //???????????????????????????
                    ArrayList<BusinessDistrictListBean.Comment> commentList = businessDistrict.getCommentList();
                    if (commentList != null) {

                        //????????????????????????/?????????????????????????????????UI?????????????????????
                        if (commentList.size() > 0)
                            commentList.add(0, (BusinessDistrictCommentOrReplyBean) objects[0]);
                        else
                            commentList.add((BusinessDistrictCommentOrReplyBean) objects[0]);
                        //???????????????
                        businessDistrict.setComments(businessDistrict.getComments() + 1);
                    }
                    getV().onSubmitCommentOrReplyResult();
                } else if (objects[0] instanceof BusinessDistrictUnfoldCommentListBean) {
                    /**
                     * ??????????????????????????????????????????
                     */
                    BusinessDistrictListBean.BusinessDistrict businessDistrict = (BusinessDistrictListBean.BusinessDistrict) objects[1];
                    BusinessDistrictUnfoldCommentListBean commentListBean = (BusinessDistrictUnfoldCommentListBean) objects[0];
                    getV().onReturnCommentListResult(businessDistrict, commentListBean);
                } else if (objects[0] instanceof NumberUnreadCommentsBean) {
                    /**
                     * ???????????????????????????
                     */
                    NumberUnreadCommentsBean unreadCommentsBean = (NumberUnreadCommentsBean) objects[0];
                    getV().onNumberUnreadComments(unreadCommentsBean);

                }
            }
        }
    }
}
