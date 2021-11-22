package com.gxdingo.sg.bean;

import com.google.gson.annotations.SerializedName;

public class OauthEventBean {

    @SerializedName("isChecked")
    private boolean isChecked;

    public boolean isIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
