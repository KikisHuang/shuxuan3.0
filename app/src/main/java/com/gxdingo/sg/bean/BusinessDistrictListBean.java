package com.gxdingo.sg.bean;


import com.lzy.ninegrid.ImageInfo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 商圈列表
 *
 * @author JM
 */
public class BusinessDistrictListBean implements Serializable {

    private int unread;//未读数
    private ArrayList<BusinessDistrict> list;

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public ArrayList<BusinessDistrict> getList() {
        return list;
    }

    public void setList(ArrayList<BusinessDistrict> list) {
        this.list = list;
    }

    public static class BusinessDistrict implements Serializable {
        private int currentPage = 1;//商圈评论页码
        private int pageSize = 10;//商圈评论每页数量

        private long id;
        private long storeId;
        private String content;
        private int comments;
        private String createTime;
        private String identifier;
        private String storeName;
        private String stareAvatar;
        private ArrayList<String> images;//商圈图片（需要判空null/[]处理）
        public ArrayList<ImageInfo> imageInfos = new ArrayList<>();
        private ArrayList<Comment> commentList = new ArrayList<>();//**注意**：当comments < 10时返回，否则返回空数组 []
        private int commentOpen;//展开评论状态 0 表示初始化没有点过，1 表示打开，2 表示关闭（辅助字段，非接口字段）

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getStoreId() {
            return storeId;
        }

        public void setStoreId(long storeId) {
            this.storeId = storeId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getComments() {
            return comments;
        }

        public void setComments(int comments) {
            this.comments = comments;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getStareAvatar() {
            return stareAvatar;
        }

        public void setStareAvatar(String stareAvatar) {
            this.stareAvatar = stareAvatar;
        }

        public ArrayList<String> getImages() {
            return images;
        }

        public void setImages(ArrayList<String> images) {
            this.images = images;
        }

        public ArrayList<Comment> getCommentList() {
            if (commentList == null) {
                setCommentList(new ArrayList<>());
            }
            return commentList;
        }

        public void setCommentList(ArrayList<Comment> commentList) {
            this.commentList = commentList;
        }

        public int getCommentOpen() {
            return commentOpen;
        }

        public void setCommentOpen(int commentOpen) {
            this.commentOpen = commentOpen;
        }
    }

    public static class Comment implements Serializable, Comparable<Comment> {
        private String identifier;
        private String createTime;
        private String replyNickname;
        private long id;
        private String parentNickname;
        private String content;
        private int status;
        private String parentRead;


        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getReplyNickname() {
            return replyNickname;
        }

        public void setReplyNickname(String replyNickname) {
            this.replyNickname = replyNickname;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getParentNickname() {
            return parentNickname;
        }

        public void setParentNickname(String parentNickname) {
            this.parentNickname = parentNickname;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getParentRead() {
            return parentRead;
        }

        public void setParentRead(String parentRead) {
            this.parentRead = parentRead;
        }


        @Override
        public int compareTo(Comment o) {
            if (this.id > o.id) {
                return 1;
            } else if (this.id < o.id) {
                return -1;
            }
            return 0;
        }
    }
}
