package com.gxdingo.sg.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RankBean {

    @SerializedName("userIdentifier")
    private String userIdentifier;
    @SerializedName("categoryList")
    private List<String> categoryList;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("count")
    private int count;
    @SerializedName("avatar")
    private String avatar;

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public List<String> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<String> categoryList) {
        this.categoryList = categoryList;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
