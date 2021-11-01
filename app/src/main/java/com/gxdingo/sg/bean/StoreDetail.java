package com.gxdingo.sg.bean;

import java.util.List;

/**
 * @author: Weaving
 * @date: 2021/10/20
 * @page:
 */
public class StoreDetail {

    /**
     * id : 1
     * user_id : 1
     * avatar : http://xxxxx.png
     * name : NAME
     * images : ["http://xxxxx.png","http://xxxxx.png"]
     * regionPath : 123456/123456
     * address : 广西南宁秦秀区
     * rating : 3.2
     * longitude : 115.92471589062498
     * latitude : 115.92471589062498
     * distance : 1.5km
     * introduction : 店铺简介店铺简介
     * businessStatus : 1
     * openTime : 09:00:00
     * closeTime : 21:00:00
     * salesAmount : 50
     * productSales : 70
     * followCount : 15
     * createTime : 2021-04-20 10:00:00
     * commentCount : 23
     * licence : {"businessLicence":"http://xxxx.png","licenceName":"安全小卖部营业执照","updateTime":"2021-05-13T06:21:06.000+00:00"}
     */

    private Integer id;
    private Integer user_id;
    private String avatar;
    private String name;
    private List<String> images;
    private String regionPath;
    private String address;
    private Double rating;
    private Double longitude;
    private Double latitude;
    private String distance;
    private String introduction;
    private Integer businessStatus;
    private String openTime;
    private String closeTime;
    private Double salesAmount;
    private Integer productSales;
    private Integer followCount;
    private String contactNumber;
    private String createTime;
    private Integer commentCount;
    private LicenceBean licence;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
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

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public LicenceBean getLicence() {
        return licence;
    }

    public void setLicence(LicenceBean licence) {
        this.licence = licence;
    }

    public static class LicenceBean {
        /**
         * businessLicence : http://xxxx.png
         * licenceName : 安全小卖部营业执照
         * updateTime : 2021-05-13T06:21:06.000+00:00
         */

        private String businessLicence;
        private String licenceName;
        private String updateTime;

        public String getBusinessLicence() {
            return businessLicence;
        }

        public void setBusinessLicence(String businessLicence) {
            this.businessLicence = businessLicence;
        }

        public String getLicenceName() {
            return licenceName;
        }

        public void setLicenceName(String licenceName) {
            this.licenceName = licenceName;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }
}
