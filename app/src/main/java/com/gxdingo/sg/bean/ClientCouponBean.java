package com.gxdingo.sg.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Weaving
 * @date: 2021/10/25
 * @page:
 */
public class ClientCouponBean implements Serializable {

    @SerializedName("couponName")
    private String couponName;
    @SerializedName("couponAmount")
    private double couponAmount;
    @SerializedName("expireTime")
    private String expireTime;
    @SerializedName("storeAvatar")
    private String storeAvatar;
    @SerializedName("storeName")
    private String storeName;
    @SerializedName("id")
    private int id;
    @SerializedName("status")
    private int status;
    @SerializedName("storeId")
    private int storeId;
    @SerializedName("useAmount")
    private double useAmount;
    @SerializedName("couponIdentifier")
    private String couponIdentifier;
    @SerializedName("writeOff")
    private int writeOff;
    @SerializedName("isNeedWriteOff")
    private int isNeedWriteOff;
    @SerializedName("instructions")
    private List<String> instructions;
    @SerializedName("precautions")
    private List<String> precautions;

    public String userIdentifier;

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getCouponAmount() {
        return String.valueOf(couponAmount);
    }

    public void setCouponAmount(double couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getStoreAvatar() {
        return storeAvatar;
    }

    public void setStoreAvatar(String storeAvatar) {
        this.storeAvatar = storeAvatar;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getUseAmount() {
        return String.valueOf(useAmount);
    }

    public void setUseAmount(double useAmount) {
        this.useAmount = useAmount;
    }

    public String getCouponIdentifier() {
        return couponIdentifier;
    }

    public void setCouponIdentifier(String couponIdentifier) {
        this.couponIdentifier = couponIdentifier;
    }

    public int getWriteOff() {
        return writeOff;
    }

    public void setWriteOff(int writeOff) {
        this.writeOff = writeOff;
    }

    public int getIsNeedWriteOff() {
        return isNeedWriteOff;
    }

    public void setIsNeedWriteOff(int isNeedWriteOff) {
        this.isNeedWriteOff = isNeedWriteOff;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    public List<String> getPrecautions() {
        return precautions;
    }

    public void setPrecautions(List<String> precautions) {
        this.precautions = precautions;
    }
}
