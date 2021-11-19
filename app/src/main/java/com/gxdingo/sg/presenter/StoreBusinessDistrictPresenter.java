package com.gxdingo.sg.presenter;

import android.app.Activity;

import com.gxdingo.sg.bean.BusinessDistrictListBean;
import com.gxdingo.sg.bean.BusinessDistrictCommentOrReplyBean;
import com.gxdingo.sg.bean.BusinessDistrictUnfoldCommentListBean;
import com.gxdingo.sg.bean.NumberUnreadCommentsBean;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.StoreBusinessDistrictContract;
import com.gxdingo.sg.model.BusinessDistrictModel;
import com.kikis.commnlibrary.activitiy.BaseActivity;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MultiParameterCallbackListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.kikis.commnlibrary.utils.RxUtil;
import com.zhouyou.http.subsciber.BaseSubscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static cc.shinichi.library.ImagePreview.LoadStrategy.NetworkAuto;
import static com.kikis.commnlibrary.utils.IntentUtils.getImagePreviewInstance;

/**
 * 商家端商圈Presenter
 *
 * @author JM
 */
public class StoreBusinessDistrictPresenter extends BaseMvpPresenter<BasicsListener, StoreBusinessDistrictContract.StoreBusinessDistrictListener>
        implements StoreBusinessDistrictContract.StoreBusinessDistrictPresenter, NetWorkListener, MultiParameterCallbackListener {


    private BusinessDistrictModel businessDistrictModel;

    public StoreBusinessDistrictPresenter() {
        businessDistrictModel = new BusinessDistrictModel(this);
    }

    @Override
    public void onSucceed(int type) {
        //商家删除商圈动态
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
            //商圈列表回调
            if (o instanceof BusinessDistrictListBean) {
                getV().onBusinessDistrictData(refresh, (BusinessDistrictListBean) o);
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
     * 获取商圈列表
     */
    @Override
    public void getBusinessDistrictList(boolean refresh, int storeId) {
        businessDistrictModel.getBusinessDistrict(getContext(), refresh, storeId);
    }

    /**
     * 提交评论/回复
     *
     * @param businessDistrict 评论/回复的商圈
     * @param circleId         商圈ID
     * @param parentId         回复谁的消息id
     * @param content          内容
     */
    @Override
    public void submitCommentOrReply(BusinessDistrictListBean.BusinessDistrict businessDistrict, long circleId, long parentId, String content) {
        businessDistrictModel.commentOrReply(getContext(), businessDistrict, circleId, parentId, content, this);
    }

    /**
     * 获取评论列表（展开评论）
     *
     * @param businessDistrict 商圈
     * @param circleId         商圈ID
     * @param current          页面
     * @param size             每页数量
     */
    @Override
    public void getUnfoldCommentList(BusinessDistrictListBean.BusinessDistrict businessDistrict, long circleId, int current, int size) {
        businessDistrictModel.getCommentList(getContext(), businessDistrict, circleId, current, size, this);
    }

    /**
     * 去重合并评论数据
     * @param commentList       原来的评论数据
     * @param unfoldCommentList 展开评论获取的评论数据
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
                    //使用迭代器删除commentList重复的评论
                    Iterator<BusinessDistrictListBean.Comment> iterator = commentList.iterator();
                    while (iterator.hasNext()) {
                        BusinessDistrictListBean.Comment c = iterator.next();
                        if (uc.getId() == c.getId()) {
                            iterator.remove();
                        }
                    }
                }
                //合并数据
                commentList.addAll(unfoldCommentList);
                //根据ID排序，最早的在前面
                Collections.sort(commentList);
            }
            e.onNext(0);
            e.onComplete();
        }), (BaseActivity) getContext()).subscribe(o -> {
            if (isViewAttached())
                getV().onCommentListRefresh(commentList,unfoldCommentList,businessDistrict,total);
        });

    }

    /**
     * 获取商圈评论未读数量
     */
    @Override
    public void getNumberUnreadComments() {
        businessDistrictModel.getNumberUnreadComments(getContext(), this);
    }

    /**
     * 商家删除商圈动态
     *
     * @param id 商圈ID
     */
    @Override
    public void deleteBusinessDistrictDynamics(long id) {
        businessDistrictModel.storeDeleteBusinessDistrictDynamics(getContext(), id);
    }

    @Override
    public void PhotoViewer(ArrayList<String> images, int position) {
        getImagePreviewInstance((Activity) getContext(), NetworkAuto, position, true).setImageList(images).start();
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
                    BusinessDistrictListBean.BusinessDistrict businessDistrict = (BusinessDistrictListBean.BusinessDistrict) objects[1];
                    //得到该商圈评论集合
                    ArrayList<BusinessDistrictListBean.Comment> commentList = businessDistrict.getCommentList();
                    if (commentList != null) {
                        //将提交返回的评论/回复添加到集合中，并在UI刷新适配器显示
                        commentList.add((BusinessDistrictCommentOrReplyBean) objects[0]);
                        //更新评论数
                        businessDistrict.setComments(businessDistrict.getComments() + 1);
                    }
                    getV().onSubmitCommentOrReplyResult();
                } else if (objects[0] instanceof BusinessDistrictUnfoldCommentListBean) {
                    /**
                     * 返回评论列表结果（展开评论）
                     */
                    BusinessDistrictListBean.BusinessDistrict businessDistrict = (BusinessDistrictListBean.BusinessDistrict) objects[1];
                    BusinessDistrictUnfoldCommentListBean commentListBean = (BusinessDistrictUnfoldCommentListBean) objects[0];
                    getV().onReturnCommentListResult(businessDistrict, commentListBean);
                } else if (objects[0] instanceof NumberUnreadCommentsBean) {
                    /**
                     * 返回商圈评论未读数
                     */
                    NumberUnreadCommentsBean unreadCommentsBean = (NumberUnreadCommentsBean) objects[0];
                    getV().onNumberUnreadComments(unreadCommentsBean);
                }
            }
        }
    }
}
