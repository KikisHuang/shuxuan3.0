package com.gxdingo.sg.biz;

import com.gxdingo.sg.bean.BannerBean;
import com.gxdingo.sg.bean.BusinessDistrictListBean;
import com.gxdingo.sg.bean.BusinessDistrictUnfoldCommentListBean;
import com.gxdingo.sg.bean.NumberUnreadCommentsBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

/**
 * 商家端商圈契约类
 *
 * @author JM
 */
public class StoreBusinessDistrictContract {

    public interface StoreBusinessDistrictPresenter extends MvpPresenter<BasicsListener, StoreBusinessDistrictListener> {
        /**
         * 获取商圈列表
         *
         * @param refresh true 表示刷新，false表示加载更多
         */
        void getBusinessDistrictList(boolean refresh, String id);

        /**
         * 分享跳转获取商圈列表
         *
         * @param circleCode 商圈分享口令
         */
        void shareGetBusinessDistrictList(String id, String circleCode);

        /**
         * 提交评论/回复
         *
         * @param businessDistrict 评论/回复的商圈
         * @param circleId         商圈ID
         * @param parentId         回复谁的消息id
         * @param content          内容
         */
        void submitCommentOrReply(BusinessDistrictListBean.BusinessDistrict businessDistrict, long circleId, long parentId, String content);

        /**
         * 获取评论列表（展开评论）
         *
         * @param businessDistrict 商圈
         * @param circleId         商圈ID
         * @param current          页面
         * @param size             每页数量
         */
        void getUnfoldCommentList(BusinessDistrictListBean.BusinessDistrict businessDistrict, long circleId, int current, int size);

        /**
         * 去重合并评论
         *
         * @param commentList       原来的评论数据
         * @param unfoldCommentList 展开评论获取的评论数据
         * @param businessDistrict
         * @param total
         */
        void onDuplicateRemovalMerge(ArrayList<BusinessDistrictListBean.Comment> commentList
                , ArrayList<BusinessDistrictUnfoldCommentListBean.UnfoldComment> unfoldCommentList, BusinessDistrictListBean.BusinessDistrict businessDistrict, int total);

        /**
         * 获取商圈评论未读数量
         */
        void getNumberUnreadComments();

        /**
         * 商家删除商圈动态
         *
         * @param id 商圈ID
         */
        void deleteBusinessDistrictDynamics(long id);

        void PhotoViewer(ArrayList<String> images, int position);

        //完成浏览商圈
        void complete(String identifier);

        //点赞of 取消点赞
        void likedOrUnliked(int status, long id, int position);

        //分享连接
        void shareLink(String content, String imgUrl, String url);

        void checkLocationPermission(RxPermissions rxPermissions, String mcircleUserIdentifier);

        void getBannerDataInfo();
        //刷新认证状态
        void refreshUserStatus();
    }

    public interface StoreBusinessDistrictListener {

        /**
         * 商圈数据
         */
        void onBusinessDistrictData(boolean refresh, BusinessDistrictListBean bean);

        /**
         * 提交评论/回复的结果
         */
        void onSubmitCommentOrReplyResult();

        /**
         * 返回评论列表结果（展开评论）
         *
         * @param businessDistrict 展开评论所属商圈
         * @param commentListBean  展开的评论
         */
        void onReturnCommentListResult(BusinessDistrictListBean.BusinessDistrict businessDistrict, BusinessDistrictUnfoldCommentListBean commentListBean);

        /**
         * 返回商圈评论未读数量
         */
        void onNumberUnreadComments(NumberUnreadCommentsBean unreadCommentsBean);

        void onCommentListRefresh(ArrayList<BusinessDistrictListBean.Comment> commentList, ArrayList<BusinessDistrictUnfoldCommentListBean.UnfoldComment> unfoldCommentList, BusinessDistrictListBean.BusinessDistrict businessDistrict, int total);

        /**
         * 刷新点赞数量
         *
         * @param postition
         * @param status
         */
        void refreshLikeNum(String o, int postition, int status);

        /**
         * 头部广告数据回调
         *
         * @param data
         */
        void onBannerResult(BannerBean data);

        /**
         * 显示实名认证弹窗
         */
        void showAuthenticationDialog();

    }
}
