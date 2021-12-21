package com.gxdingo.sg.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.UtilsTransActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ArticleListBean;
import com.gxdingo.sg.bean.UserBean;
import com.gxdingo.sg.bean.WebBean;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.PermissionsListener;
import com.gxdingo.sg.biz.WebContract;
import com.gxdingo.sg.model.ClientNetworkModel;
import com.gxdingo.sg.model.CommonModel;
import com.gxdingo.sg.model.WebModel;
import com.gxdingo.sg.utils.GlideEngine;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.utils.WechatUtils;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;
import com.zhouyou.http.subsciber.BaseSubscriber;

import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.blankj.utilcode.util.StringUtils.getString;
import static com.blankj.utilcode.util.StringUtils.isEmpty;
import static com.kikis.commnlibrary.utils.CommonUtils.getTAG;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.CommonUtils.isWeixinAvilible;
import static com.luck.picture.lib.config.PictureMimeType.ofImage;
import static com.luck.picture.lib.config.PictureMimeType.ofVideo;

/**
 * Created by Kikis on 2021/5/21
 */

public class WebPresenter extends BaseMvpPresenter<BasicsListener, WebContract.WebListener> implements WebContract.WebPresenter, NetWorkListener {

    private static final String TAG = getTAG(WebPresenter.class);

    private ClientNetworkModel mClientNetworkModel;
    private WebModel mWebModel;

    private CommonModel commonModel;

    public WebPresenter() {

        mClientNetworkModel = new ClientNetworkModel(this);
        mWebModel = new WebModel();
        commonModel = new CommonModel();
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
            if (o instanceof ArticleListBean) {
                getV().onArticleListResult(((ArticleListBean) o).getList());
            } else if (o instanceof WebBean) {
                WebBean webBean = (WebBean) o;
                getV().loadWebUrl(webBean);
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


    @Override
    public void onMvpDestroy() {
        super.onMvpDestroy();
    }

    /**
     * 获取文章详情
     *
     * @param articleId
     * @param identifier
     */
    @Override
    public void getArticleDetail(int articleId, String identifier) {

        if (mClientNetworkModel != null)
            mClientNetworkModel.getArticleDetail(getContext(), articleId, identifier);

    }

    @Override
    public void loadData(int articleId, String identifier) {
        if (mClientNetworkModel != null)
            mClientNetworkModel.getArticleList(getContext(), articleId, identifier);
    }

    /**
     * 分享支付
     *
     * @param url
     * @param title
     * @param description
     * @param thumb
     */
    @Override
    public void sharePayment(String url, String title, String description, String thumb) {

        if (!isWeixinAvilible(getContext())) {
            onMessage(String.format(getString(R.string.uninstall_app), gets(R.string.wechat)));
            return;
        }

        Glide.with(getContext()).asBitmap().load(!isEmpty(thumb) ? thumb : R.drawable.default_bg).into(new SimpleTarget<Bitmap>(200, 200) {

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                onMessage(getString(R.string.get_share_pictures_failed));
                onAfters();
            }

            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                WechatUtils.getInstance().shareWebPage(url, title, description, resource, SendMessageToWX.Req.WXSceneSession);
                onAfters();
            }
        });

    }

    /**
     * 打开相册 或 视频
     *
     * @param valueCallback
     * @param mode          //fileChooserParams mode 1 相册 0 视频
     */
    @Override
    public void openPhoto(ValueCallback<Uri[]> valueCallback, int mode) {


        PictureSelector selector = PictureSelector.create((Activity) getContext());

        PictureSelectionModel model = selector.openGallery(mode == 1 ? ofImage() : ofVideo());

        model.selectionMode(PictureConfig.SINGLE).
                loadImageEngine(GlideEngine.createGlideEngine())// 图片加载引擎 需要 implements ImageEngine接口
                .enableCrop(false)//是否裁剪
                .compress(true)//是否压缩
                .minimumCompressSize(400)//小于1024kb不压缩
                .synOrAsy(true)//开启同步or异步压缩
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        if (isViewAttached()) {
                            String url = !isEmpty(result.get(0).getCompressPath()) ? result.get(0).getCompressPath() : result.get(0).getPath();
                            getV().uploadImage(valueCallback, Uri.parse((String) url));
                        }
                    }

                    @Override
                    public void onCancel() {
                        getV().uploadImage(valueCallback, null);
                    }
                });

    }

    @Override
    public void completeTask() {
        if (mClientNetworkModel != null)
            mClientNetworkModel.completeTask(getContext(), 30);
    }

    /**
     * 清除缓存
     */
    @Override
    public void clearCache() {
        if (mWebModel != null && isViewAttached())
            mWebModel.clearCache(getContext());

    }

    /**
     * 获取用户定位信息
     *
     * @return
     */
    @Override
    public String getUserLocationInfo() {
        return  LocalConstant.AdCode;
    }
}

