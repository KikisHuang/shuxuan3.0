package com.gxdingo.sg.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author: Weaving
 * @date: 2021/10/20
 * @page:
 */
public class StoreDetail {


    @SerializedName("address")
    private String address;
    @SerializedName("authStatus")
    private int authStatus;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("businessStatus")
    private int businessStatus;
    @SerializedName("classNameList")
    private List<String> classNameList;
    @SerializedName("closeTime")
    private String closeTime;
    @SerializedName("contactNumber")
    private String contactNumber;
    @SerializedName("createTime")
    private long createTime;
    @SerializedName("distance")
    private String distance;
    @SerializedName("followCount")
    private int followCount;
    @SerializedName("iconUrl")
    private String iconUrl;
    @SerializedName("id")
    private int id;
    @SerializedName("introduction")
    private String introduction;
    @SerializedName("isAuthentication")
    private int isAuthentication;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("licence")
    private LicenceDTO licence;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("maxDistance")
    private int maxDistance;
    @SerializedName("name")
    private String name;
    @SerializedName("openTime")
    private String openTime;
    @SerializedName("productSales")
    private int productSales;
    @SerializedName("rating")
    private int rating;
    @SerializedName("regionPath")
    private String regionPath;
    @SerializedName("releaseUserType")
    private int releaseUserType;
    @SerializedName("salesAmount")
    private double salesAmount;
    @SerializedName("status")
    private int status;
    @SerializedName("type")
    private int type;
    @SerializedName("updateTime")
    private long updateTime;
    @SerializedName("userId")
    private int userId;

    private List<String> images;

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(int businessStatus) {
        this.businessStatus = businessStatus;
    }

    public List<String> getClassNameList() {
        return classNameList;
    }

    public void setClassNameList(List<String> classNameList) {
        this.classNameList = classNameList;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getIsAuthentication() {
        return isAuthentication;
    }

    public void setIsAuthentication(int isAuthentication) {
        this.isAuthentication = isAuthentication;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public LicenceDTO getLicence() {
        return licence;
    }

    public void setLicence(LicenceDTO licence) {
        this.licence = licence;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public int getProductSales() {
        return productSales;
    }

    public void setProductSales(int productSales) {
        this.productSales = productSales;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getRegionPath() {
        return regionPath;
    }

    public void setRegionPath(String regionPath) {
        this.regionPath = regionPath;
    }

    public int getReleaseUserType() {
        return releaseUserType;
    }

    public void setReleaseUserType(int releaseUserType) {
        this.releaseUserType = releaseUserType;
    }

    public double getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(double salesAmount) {
        this.salesAmount = salesAmount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public static class LicenceDTO {
        @SerializedName("businessLicence")
        private String businessLicence;
        @SerializedName("licenceName")
        private String licenceName;
        @SerializedName("updateTime")
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
