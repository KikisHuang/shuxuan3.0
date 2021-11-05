package com.gxdingo.sg.biz;

import com.gxdingo.sg.bean.BusinessDistrictListBean;
import com.gxdingo.sg.bean.BusinessDistrictMessageCommentListBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

/**
 * 商圈消息契约类
 *
 * @author JM
 */
public class BusinessDistrictMessageContract {

    public interface BusinessDistrictMessagePresenter extends MvpPresenter<BasicsListener, BusinessDistrictMessageListener> {

        /**
         * 获取商圈消息评论列表
         *
         * @param refresh
         */
        void getMessageCommentList(boolean refresh);

        /**
         * 提交评论/回复
         *
         * @param parentId 回复谁的消息id
         * @param content  内容
         */
        void submitCommentOrReply(long parentId, String content);

        /**
         * 删除我自己的评论
         *
         * @param id 评论ID
         */
        void deleteMyOwnComment(long id);
    }

    public interface BusinessDistrictMessageListener {

        /**
         * 商圈消息评论数据
         *
         * @param refresh
         */
        void onMessageCommentData(boolean refresh, BusinessDistrictMessageCommentListBean commentListBean);

        /**
         * 提交评论/回复的结果
         */
        void onSubmitCommentOrReplyResult();
    }
}
