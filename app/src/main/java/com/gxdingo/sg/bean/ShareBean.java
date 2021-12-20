package com.gxdingo.sg.bean;

import com.google.gson.annotations.SerializedName;

public class ShareBean {

    @SerializedName("type")
    private int type;
    @SerializedName("inviteCode")
    private String inviteCode;
    @SerializedName("title")
    private String title;
    @SerializedName("describe")
    private String describe;
    @SerializedName("image")
    private String image;
    @SerializedName("url")
    private String url;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
