package com.gxdingo.sg.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ArticleListBean;
import com.gxdingo.sg.bean.WebBean;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.WebContract;
import com.gxdingo.sg.model.ClientNetworkModel;
import com.gxdingo.sg.utils.WechatUtils;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.zhouyou.http.subsciber.BaseSubscriber;

import static com.blankj.utilcode.util.StringUtils.getString;
import static com.blankj.utilcode.util.StringUtils.isEmpty;
import static com.kikis.commnlibrary.utils.CommonUtils.getTAG;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.CommonUtils.isWeixinAvilible;

/**
 * Created by Kikis on 2021/5/21
 */

public class WebPresenter extends BaseMvpPresenter<BasicsListener, WebContract.WebListener> implements WebContract.WebPresenter, NetWorkListener {

    private static final String TAG = getTAG(WebPresenter.class);

    private ClientNetworkModel mClientNetworkModel;


    public WebPresenter() {

        mClientNetworkModel = new ClientNetworkModel(this);

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
            if (o instanceof ArticleListBean){
                getV().onArticleListResult(((ArticleListBean)o).getList());
            }else if(o instanceof WebBean) {
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

    @Override
    public void completeTask() {
        if (mClientNetworkModel!=null)
            mClientNetworkModel.completeTask(getContext(),10);
    }
}

