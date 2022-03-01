package com.gxdingo.sg.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BannerBean {


    @SerializedName("appHomeHeader")
    private List<HomeBannerBean> appHomeHeader;
    @SerializedName("iconList")
    private List<IconListDTO> iconList;
    @SerializedName("noticeList")
    private List<NoticeListDTO> noticeList;
    public List<String> noticeStringList;

    public List<HomeBannerBean> getAppHomeHeader() {
        return appHomeHeader;
    }

    public void setAppHomeHeader(List<HomeBannerBean> appHomeHeader) {
        this.appHomeHeader = appHomeHeader;
    }

    public List<IconListDTO> getIconList() {
        return iconList;
    }

    public void setIconList(List<IconListDTO> iconList) {
        this.iconList = iconList;
    }

    public List<NoticeListDTO> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<NoticeListDTO> noticeList) {
        this.noticeList = noticeList;
    }


    public static class IconListDTO {
        @SerializedName("iconUrl")
        private String iconUrl;
        @SerializedName("name")
        private String name;

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class NoticeListDTO {
        @SerializedName("content")
        private String content;
        @SerializedName("createTime")
        private String createTime;
        @SerializedName("id")
        private int id;
        @SerializedName("noticeTime")
        private String noticeTime;
        @SerializedName("status")
        private int status;
        @SerializedName("target")
        private int target;
        @SerializedName("title")
        private String title;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNoticeTime() {
            return noticeTime;
        }

        public void setNoticeTime(String noticeTime) {
            this.noticeTime = noticeTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getTarget() {
            return target;
        }

        public void setTarget(int target) {
            this.target = target;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
