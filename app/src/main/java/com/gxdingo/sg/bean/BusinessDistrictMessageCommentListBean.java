package com.gxdingo.sg.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 商圈消息-评论列表
 *
 * @author JM
 */
public class BusinessDistrictMessageCommentListBean implements Serializable {

    private ArrayList<Comment> list;

    public ArrayList<Comment> getList() {
        return list;
    }

    public void setList(ArrayList<Comment> list) {
        this.list = list;
    }

    public class Comment implements Serializable {
        private long id;//评论id
        private String userAvatar;//对方的头像
        private String content;//对方评论内容
        private String replyNickname;//对方的昵称
        private String createTime;//对方评论的时间
        private int parentRead;//我是否已读。0=否；1=是（已根据未读先查出）
        private String circleImage;//商圈第一张图片

        private ArrayList<Reply> myReplyList;//我的回复内容列表


        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getUserAvatar() {
            return userAvatar;
        }

        public void setUserAvatar(String userAvatar) {
            this.userAvatar = userAvatar;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getReplyNickname() {
            return replyNickname;
        }

        public void setReplyNickname(String replyNickname) {
            this.replyNickname = replyNickname;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getParentRead() {
            return parentRead;
        }

        public void setParentRead(int parentRead) {
            this.parentRead = parentRead;
        }

        public String getCircleImage() {
            return circleImage;
        }

        public void setCircleImage(String circleImage) {
            this.circleImage = circleImage;
        }

        public ArrayList<Reply> getMyReplyList() {
            return myReplyList;
        }

        public void setMyReplyList(ArrayList<Reply> myReplyList) {
            this.myReplyList = myReplyList;
        }


    }

    public class Reply implements Serializable {
        private int id;//评论id
        private String content;//评论内容

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }


    }
}
