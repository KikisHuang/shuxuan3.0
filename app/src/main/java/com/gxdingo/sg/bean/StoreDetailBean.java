package com.gxdingo.sg.bean;

import java.util.ArrayList;

/**
 * @author: Weaving
 * @date: 2021/11/11
 * @page:
 */
public class StoreDetailBean {

    /**
     * id : 12
     * avatar : http://oss.gxdingo.com/upload/20201029/9c589a0d5e594531964cb7a2a81cacff.png
     * name : 测试开店
     * regionPath : /450924/450924/450924
     * address : 广西南宁秦秀区
     * rating : 0
     * status : 10
     * latitude : 39.63577992975916
     * longitude : 115.92471589062498
     * maxDistance : 1000
     * introduction :
     * businessStatus : 1
     * openTime : 1970-01-01T01:00:00.000+00:00
     * closeTime : 1970-01-01T13:00:00.000+00:00
     * businessScope : 百货
     * salesAmount : 2220.0
     * productSales : 2
     * followCount : 0
     * contactNumber :
     * createTime : 2021-04-13T06:13:21.000+00:00
     */

    private Integer id;
    private String avatar;
    private String name;
    private String regionPath;
    private String address;
    private Integer rating;
    private Integer status;
    private Double latitude;
    private Double longitude;
    private String maxDistance;
    private String introduction;
    private Integer businessStatus;
    private String openTime;
    private String closeTime;
    private ArrayList<BusinessScopeBean> businessScope;
    private Double salesAmount;
    private Integer productSales;
    private Integer followCount;
    private String contactNumber;
    private String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegionPath() {
        return regionPath;
    }

    public void setRegionPath(String regionPath) {
        this.regionPath = regionPath;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(String maxDistance) {
        this.maxDistance = maxDistance;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Integer getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(Integer businessStatus) {
        this.businessStatus = businessStatus;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public ArrayList<BusinessScopeBean> getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(ArrayList<BusinessScopeBean> businessScope) {
        this.businessScope = businessScope;
    }

    public Double getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(Double salesAmount) {
        this.salesAmount = salesAmount;
    }

    public Integer getProductSales() {
        return productSales;
    }

    public void setProductSales(Integer productSales) {
        this.productSales = productSales;
    }

    public Integer getFollowCount() {
        return followCount;
    }

    public void setFollowCount(Integer followCount) {
        this.followCount = followCount;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
