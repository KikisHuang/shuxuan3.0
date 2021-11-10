package com.gxdingo.sg.biz;

import com.gxdingo.sg.bean.BusinessDistrictListBean;
import com.gxdingo.sg.bean.BusinessDistrictUnfoldCommentListBean;
import com.gxdingo.sg.bean.NumberUnreadCommentsBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

import java.util.ArrayList;

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
        void getBusinessDistrictList(boolean refresh);

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
         */
        void onDuplicateRemovalMerge(ArrayList<BusinessDistrictListBean.Comment> commentList
                , ArrayList<BusinessDistrictUnfoldCommentListBean.UnfoldComment> unfoldCommentList);

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
    }
}
