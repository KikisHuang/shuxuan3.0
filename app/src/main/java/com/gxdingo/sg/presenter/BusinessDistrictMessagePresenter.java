package com.gxdingo.sg.presenter;

import com.gxdingo.sg.bean.BusinessDistrictCommentOrReplyBean;
import com.gxdingo.sg.bean.BusinessDistrictListBean;
import com.gxdingo.sg.bean.BusinessDistrictMessageCommentListBean;
import com.gxdingo.sg.bean.BusinessDistrictUnfoldCommentListBean;
import com.gxdingo.sg.biz.BusinessDistrictMessageContract;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.model.BusinessDistrictModel;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MultiParameterCallbackListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.zhouyou.http.subsciber.BaseSubscriber;

import java.util.ArrayList;

/**
 * 商圈消息评论Presenter
 *
 * @author JM
 */
public class BusinessDistrictMessagePresenter extends BaseMvpPresenter<BasicsListener, BusinessDistrictMessageContract.BusinessDistrictMessageListener>
        implements BusinessDistrictMessageContract.BusinessDistrictMessagePresenter, NetWorkListener, MultiParameterCallbackListener {


    private BusinessDistrictModel businessDistrictModel;

    public BusinessDistrictMessagePresenter() {
        businessDistrictModel = new BusinessDistrictModel(this);
    }

    @Override
    public void onSucceed(int type) {
        //删除我自己的评论
        if (type == 300) {
            getBV().onSucceed(type);
        }
    }

    @Override
    public void onMessage(String msg) {

    }

    @Override
    public void noData() {
        if (isBViewAttached())
            getBV().noData();
    }

    @Override
    public void onData(boolean refresh, Object o) {
        if (isBViewAttached()) {
            if (o instanceof BusinessDistrictMessageCommentListBean) {
                getV().onMessageCommentData(refresh, (BusinessDistrictMessageCommentListBean) o);
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

    }

    /**
     * 获取商圈消息评论列表
     *
     * @param refresh
     */
    @Override
    public void getMessageCommentList(boolean refresh) {
        businessDistrictModel.getMessageCommentList(getContext(), refresh);
    }

    /**
     * 提交评论/回复
     *
     * @param parentId 回复谁的消息id
     * @param content  内容
     */
    @Override
    public void submitCommentOrReply(long parentId, String content) {
        businessDistrictModel.commentOrReply(getContext(), null, 0, parentId, content, this);
    }

    /**
     * 删除我自己的评论
     *
     * @param id 评论ID
     */
    @Override
    public void deleteMyOwnComment(long id) {
        businessDistrictModel.deleteMyOwnComment(getContext(), id);
    }

    /**
     * Model层多数据回调
     *
     * @param objects 数组
     */
    @Override
    public void multipleDataResult(Object... objects) {
        if (isBViewAttached()) {
            if (objects != null && objects.length > 0) {
                if (objects[0] instanceof BusinessDistrictCommentOrReplyBean) {
                    /**
                     * 提交返回评论/回复
                     */
                    getV().onSubmitCommentOrReplyResult();
                }
            }
        }
    }

}
