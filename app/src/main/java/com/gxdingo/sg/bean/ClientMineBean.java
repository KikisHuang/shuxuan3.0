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

    private String balance;
    public String integral;
    public String integralLink;
    public String openTime;
    public String closeTime;
    private List<ClientCouponBean> couponList;
    public List<String> iconList;
    private List<AdsListBean> adsList;
    public List<StoreAuthInfoBean.CategoryListBean> categoryList;
    private String nickname;
    private String avatar;
    //用户类型。0=商家；1=用户
    public int releaseUserType;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public List<ClientCouponBean> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<ClientCouponBean> couponList) {
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
        private String remark;
        private Integer type;

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

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
