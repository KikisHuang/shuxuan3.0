package com.gxdingo.sg.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author: Weaving
 * @date: 2021/10/19
 * @page:
 */
public class ClientMineBean {

    @SerializedName("adsList")
    private List<AdsListDTO> adsList;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("balance")
    private double balance;
    @SerializedName("categoryList")
    private List<StoreAuthInfoBean.CategoryListBean> categoryList;
    @SerializedName("closeTime")
    private String closeTime;
    @SerializedName("couponList")
    private List<ClientCouponBean> couponList;
    @SerializedName("iconList")
    private List<String> iconList;
    @SerializedName("integral")
    private int integral;
    @SerializedName("integralLink")
    private String integralLink;
    @SerializedName("maxDistance")
    private String maxDistance;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("openTime")
    private String openTime;
    @SerializedName("releaseUserType")
    private int releaseUserType;

    public List<AdsListDTO> getAdsList() {
        return adsList;
    }

    public void setAdsList(List<AdsListDTO> adsList) {
        this.adsList = adsList;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBalance() {
        return String.valueOf(balance);
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<StoreAuthInfoBean.CategoryListBean> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<StoreAuthInfoBean.CategoryListBean> categoryList) {
        this.categoryList = categoryList;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public List<?> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<ClientCouponBean> couponList) {
        this.couponList = couponList;
    }

    public List<String> getIconList() {
        return iconList;
    }

    public void setIconList(List<String> iconList) {
        this.iconList = iconList;
    }

    public String getIntegral() {
        return String.valueOf(integral);
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public String getIntegralLink() {
        return integralLink;
    }

    public void setIntegralLink(String integralLink) {
        this.integralLink = integralLink;
    }

    public String getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(String maxDistance) {
        this.maxDistance = maxDistance;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public int getReleaseUserType() {
        return releaseUserType;
    }

    public void setReleaseUserType(int releaseUserType) {
        this.releaseUserType = releaseUserType;
    }

    public static class AdsListDTO {
        @SerializedName("id")
        private int id;
        @SerializedName("image")
        private String image;
        @SerializedName("page")
        private String page;
        @SerializedName("remark")
        private String remark;
        @SerializedName("title")
        private String title;
        @SerializedName("type")
        private int type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public static class CategoryListDTO {
        @SerializedName("name")
        private String name;
        @SerializedName("prove")
        private String prove;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProve() {
            return prove;
        }

        public void setProve(String prove) {
            this.prove = prove;
        }
    }
}
