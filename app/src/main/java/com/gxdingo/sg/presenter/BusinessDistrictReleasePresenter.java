package com.gxdingo.sg.presenter;

import android.app.Activity;

import com.gxdingo.sg.bean.BusinessDistrictListBean;
import com.gxdingo.sg.bean.UpLoadBean;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.StoreBusinessDistrictReleaseContract;
import com.gxdingo.sg.biz.UpLoadImageListener;
import com.gxdingo.sg.model.BusinessDistrictModel;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.model.StoreNetworkModel;
import com.gxdingo.sg.utils.GlideEngine;
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
import com.zhouyou.http.subsciber.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static com.luck.picture.lib.config.PictureMimeType.ofImage;

/**
 * 商家端发布商圈信息Presenter
 *
 * @author JM
 */
public class BusinessDistrictReleasePresenter extends BaseMvpPresenter<BasicsListener, StoreBusinessDistrictReleaseContract.StoreBusinessDistrictReleaseListener>
        implements StoreBusinessDistrictReleaseContract.StoreBusinessDistrictReleasePresenter, NetWorkListener {

    private NetworkModel networkModel;
    private BusinessDistrictModel businessDistrictModel;

    private List<BusinessDistrictListBean.Labels> labels;

    public BusinessDistrictReleasePresenter() {
        labels = new ArrayList<>();
        networkModel = new NetworkModel(this);
        businessDistrictModel = new BusinessDistrictModel(this);
    }

    @Override
    public void onSucceed(int type) {
        if (type == 100) {
            getV().releaseBusinessDistrictSuccess("发布成功");
        }
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
     * 添加图片
     */
    @Override
    public void addPhoto(int type, int num) {
        if (!isViewAttached())
            return;

        boolean gallery = type == 0;//0表示相册，1表示相机
        PictureSelector selector = PictureSelector.create((Activity) getContext());
        PictureSelectionModel model = gallery ? selector.openGallery(ofImage()) : selector.openCamera(ofImage());
        model.selectionMode(PictureConfig.MULTIPLE).
                loadImageEngine(GlideEngine.createGlideEngine())// 图片加载引擎 需要 implements ImageEngine接口
                .compress(true)//是否压缩
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)//是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .rotateEnabled(false)//裁剪是否可旋转图片
                .scaleEnabled(true)//裁剪是否可放大缩小图片
                .maxSelectNum(num)
                .minimumCompressSize(500)//小于1024kb不压缩
                .synOrAsy(true)//开启同步or异步压缩
                .forResult(new OnResultCallbackListener<LocalMedia>() {

                    @Override
                    public void onResult(List<LocalMedia> result) {
                        if (networkModel != null && isBViewAttached() && isViewAttached()) {
                            networkModel.upLoadImages(getContext(), result, new UpLoadImageListener() {
                                @Override
                                public void loadSucceed(String path) {
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

    /**
     * 提交商圈信息
     *
     * @param content
     * @param images
     * @param images
     */
    @Override
    public void releaseBusinessDistrict(String content, List<String> images) {

        businessDistrictModel.storeReleaseBusinessDistrict(getContext(), content, images,labels);
    }

    @Override
    public void getLabelList() {
        if (networkModel != null)
            networkModel.getLabelList(getContext(), o -> {
                if (isViewAttached()) {
                    getV().onLabelResult((List<BusinessDistrictListBean.Labels>) o);
                }

            });

    }

    @Override
    public void labelCheck(int position) {
        if (!isViewAttached() || getV().getLabelsData() == null)
            return;

        RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {
            //选中的数量
            int checkNum = 0;
            for (BusinessDistrictListBean.Labels datas : getV().getLabelsData()) {
                if (datas.isCheck)
                    checkNum++;
            }
            if (checkNum >= 2 && !getV().getLabelsData().get(position).isCheck)
                e.onNext("最多只能选2个标签哦~");
            else {
                getV().getLabelsData().get(position).isCheck = !getV().getLabelsData().get(position).isCheck;

                if (getV().getLabelsData().get(position).isCheck)
                    labels.add(getV().getLabelsData().get(position));
                else {
                    for (int i = 0; i < labels.size(); i++) {
                        if (getV().getLabelsData().get(position).getId() == labels.get(i).getId()) {
                            labels.remove(i);
                            break;
                        }
                    }
                }
            }


            e.onNext(0);
            e.onComplete();
        }), (BaseActivity) getContext()).subscribe(o -> {
            if (o instanceof String) {
                onMessage((String) o);
            } else {
                getV().refreshLabelAdapter(position);
            }

        });

    }
}
