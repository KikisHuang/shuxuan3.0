package com.gxdingo.sg.bean;

import java.io.Serializable;

public class UserBean implements Serializable {
    /**
     * isBindMobile : 1
     * openid : null
     * identifier : S51J2V4T98
     * mobile : 18376632826
     * nickname : 18376632826
     * avatar : https://gimg2.baidu.com/image_seardc3ded5089700b28548d1e724
     * gender : 1
     * status : 1
     * role : 11
     * isSetPassword : true
     * store : null
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjM0Mjc0NzA3fQ.fqxcfp5Wz5fmuGPhTl9hQe2DCOJ3ucwrTShq2t7vbMP_N1oecDbN5irAQ42tvyvSWrCs1Ultjj6neNYOO6_zFQ
     * crossToken : S51J2V4T98.9ZHd1qzDmxH99AR.uMTVXw0CFcTuut3.9ZHq2YA1AZH1xqR
     */

    private Integer isBindMobile;
    private String openid;
    private String identifier;
    private String mobile;
    private String nickname;
    private String avatar;
    private Integer gender;
    private Integer status;
    private Integer inviterId;
    private Integer role;
    private Boolean isSetPassword;
    private StoreBean store;
    private String token;
    private String crossToken;

    public Integer getIsBindMobile() {
        return isBindMobile;
    }

    public void setIsBindMobile(Integer isBindMobile) {
        this.isBindMobile = isBindMobile;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getInviterId() {
        return inviterId;
    }

    public void setInviterId(Integer inviterId) {
        this.inviterId = inviterId;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Boolean getIsSetPassword() {
        return isSetPassword;
    }

    public void setIsSetPassword(Boolean isSetPassword) {
        this.isSetPassword = isSetPassword;
    }

    public StoreBean getStore() {
        return store;
    }

    public void setStore(StoreBean store) {
        this.store = store;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCrossToken() {
        return crossToken;
    }

    public void setCrossToken(String crossToken) {
        this.crossToken = crossToken;
    }


    public class StoreBean {
        private String address;//店铺地址
        private String name;//店铺名称
        private int rating;//评分
        private String openTime = "";//营业时间
        private String closeTime = "";//闭业时间
        private int businessStatus;//经营状态  0=未营业  1=营业中
        private int id;//店铺id
        private String avatar;//店铺头像
        private int status;//状态 0=待审核 10=已认证 20=已驳回 40=已禁用 90=永久关闭
        private String statusText;//店铺状态对应的文本


        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
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

        public int getBusinessStatus() {
            return businessStatus;
        }

        public void setBusinessStatus(int businessStatus) {
            this.businessStatus = businessStatus;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getStatusText() {
            return statusText;
        }

        public void setStatusText(String statusText) {
            this.statusText = statusText;
        }


    }
}
