package com.gxdingo.sg.bean;

import com.google.gson.annotations.SerializedName;

public class PushBean {

    @SerializedName("shareUuid")
    private String shareUuid;
    @SerializedName("type")
    private int type;
    @SerializedName("role")
    private int role;

    public String getShareUuid() {
        return shareUuid;
    }

    public void setShareUuid(String shareUuid) {
        this.shareUuid = shareUuid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
