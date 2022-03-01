package com.gxdingo.sg.bean;

import com.google.gson.annotations.SerializedName;

public class AliVerifyBean {

    @SerializedName("code")
    private String code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("bizId")
    private String bizId;
    @SerializedName("requestId")
    private String requestId;
    @SerializedName("livingType")
    private String livingType;
    @SerializedName("certName")
    private String certName;
    @SerializedName("certNo")
    private String certNo;
    @SerializedName("bestImg")
    private String bestImg;
    @SerializedName("pass")
    private String pass;
    @SerializedName("rxfs")
    private String rxfs;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getLivingType() {
        return livingType;
    }

    public void setLivingType(String livingType) {
        this.livingType = livingType;
    }

    public String getCertName() {
        return certName;
    }

    public void setCertName(String certName) {
        this.certName = certName;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getBestImg() {
        return bestImg;
    }

    public void setBestImg(String bestImg) {
        this.bestImg = bestImg;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getRxfs() {
        return rxfs;
    }

    public void setRxfs(String rxfs) {
        this.rxfs = rxfs;
    }
}
