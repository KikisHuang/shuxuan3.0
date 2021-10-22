package com.gxdingo.sg.bean;

import java.util.List;

/**
 * @author: Weaving
 * @date: 2021/10/19
 * @page:
 */
public class ClientMineBean {

    /**
     * balance : 0
     * couponList : [{"couponName":"商家邀请新用户注册活动","couponAmount":2,"expireTime":"2021-10-27T05:10:31.000+00:00","id":20,"storeId":1,"useAmount":0}]
     * adsList : [{"image":"http://oss.dgkjmm.com/upload/20.png","id":9,"page":"http://192./userIntroduction","title":"","type":2}]
     * nickname : 18376632822
     * avatar : http://oss.gxdingo.com/uplo64cb7a2a81cacff.png
     */

    private Integer balance;
    private List<CouponListBean> couponList;
    private List<AdsListBean> adsList;
    private String nickname;
    private String avatar;

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public List<CouponListBean> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<CouponListBean> couponList) {
        this.couponList = couponList;
    }

    public List<AdsListBean> getAdsList() {
        return adsList;
    }

    public void setAdsList(List<AdsListBean> adsList) {
        this.adsList = adsList;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public static class CouponListBean {
        /**
         * couponName : 商家邀请新用户注册活动
         * couponAmount : 2.0
         * expireTime : 2021-10-27T05:10:31.000+00:00
         * id : 20
         * storeId : 1
         * useAmount : 0.0
         */

        private String couponName;
        private Double couponAmount;
        private String expireTime;
        private Integer id;
        private Integer storeId;
        private Double useAmount;

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

        public String getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(String expireTime) {
            this.expireTime = expireTime;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getStoreId() {
            return storeId;
        }

        public void setStoreId(Integer storeId) {
            this.storeId = storeId;
        }

        public Double getUseAmount() {
            return useAmount;
        }

        public void setUseAmount(Double useAmount) {
            this.useAmount = useAmount;
        }
    }

    public static class AdsListBean {
        /**
         * image : http://oss.dgkjmm.com/upload/20.png
         * id : 9
         * page : http://192./userIntroduction
         * title :
         * type : 2
         */

        private String image;
        private Integer id;
        private String page;
        private String title;
        private Integer type;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }
    }
}
