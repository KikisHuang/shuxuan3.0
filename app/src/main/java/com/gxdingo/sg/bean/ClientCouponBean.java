package com.gxdingo.sg.bean;

import java.io.Serializable;

/**
 * @author: Weaving
 * @date: 2021/10/25
 * @page:
 */
public class ClientCouponBean implements Serializable {

    /**
     * id : 11
     * couponId : 2
     * couponName : 天天神卷
     * couponAmount : 5.0
     * orderAmount : 0.0
     * storeId : 26
     * expireTime : 2021-08-28T09:22:36.000+00:00
     * couponIdentifier : S51J2V4NGZ
     */

    private Integer id;
    private Integer couponId;
    private String couponName;
    private Double couponAmount;
    private Double orderAmount;
    private Integer storeId;
    private String expireTime;
    public String storeAvatar;
    private String couponIdentifier;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public Double getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(Double couponAmount) {
        this.couponAmount = couponAmount;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getCouponIdentifier() {
        return couponIdentifier;
    }

    public void setCouponIdentifier(String couponIdentifier) {
        this.couponIdentifier = couponIdentifier;
    }
}
