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
    private Integer role;
    private Boolean isSetPassword;
    private Object store;
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

    public Object getStore() {
        return store;
    }

    public void setStore(Object store) {
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


//    private long userId; // 用户id
//    private String nickname;// 用户名
//    private String avatar; // 头像地址
//    private String mobile;// 手机号码
//    private int isSeller; // 是否商家 0=否 1=是
//    private int isBindMobile; // 是否已绑定手机 1=已绑定，0未绑定
//    private int gender;  // 性别：0未知, 1男, 2女
//    private String createTime;// 创建时间
//    private String loginTime;// 最后登录时间
//    private String token;  // token
//    private String wallpaper;  // 壁纸字段
//    private String identifier;  // 用户id标识
//    private String crossToken;  // crossToken
//    private String openid;  // 点三方登录用户标识
//    private int status;//账号状态  0=禁用 1=启用
//    private int isSetPassword;//是否已设置过提现密码  false=否 true=是
//    private StoreBean store;
//
//    public String getIdentifier() {
//        return identifier;
//    }
//
//    public void setIdentifier(String identifier) {
//        this.identifier = identifier;
//    }
//
//    public String getWallpaper() {
//        return wallpaper;
//    }
//
//    public void setWallpaper(String wallpaper) {
//        this.wallpaper = wallpaper;
//    }
//
//    public String getCrossToken() {
//        return crossToken;
//    }
//
//    public void setCrossToken(String crossToken) {
//        this.crossToken = crossToken;
//    }
//
//    public int getIsBindMobile() {
//        return isBindMobile;
//    }
//
//    public void setIsBindMobile(int isBindMobile) {
//        this.isBindMobile = isBindMobile;
//    }
//
//    public String getOpenid() {
//        return openid;
//    }
//
//    public void setOpenid(String openid) {
//        this.openid = openid;
//    }
//
//    public long getUserId() {
//        return userId;
//    }
//
//    public void setUserId(long userId) {
//        this.userId = userId;
//    }
//
//    public String getNickname() {
//        return nickname;
//    }
//
//    public void setNickname(String nickname) {
//        this.nickname = nickname;
//    }
//
//    public String getAvatar() {
//        return avatar;
//    }
//
//    public void setAvatar(String avatar) {
//        this.avatar = avatar;
//    }
//
//    public String getMobile() {
//        return mobile;
//    }
//
//    public void setMobile(String mobile) {
//        this.mobile = mobile;
//    }
//
//    public int getIsSeller() {
//        return isSeller;
//    }
//
//    public void setIsSeller(int isSeller) {
//        this.isSeller = isSeller;
//    }
//
//    public int getGender() {
//        return gender;
//    }
//
//    public void setGender(int gender) {
//        this.gender = gender;
//    }
//
//    public String getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(String createTime) {
//        this.createTime = createTime;
//    }
//
//    public String getLoginTime() {
//        return loginTime;
//    }
//
//    public void setLoginTime(String loginTime) {
//        this.loginTime = loginTime;
//    }
//
//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }
//
//    public int getStatus() {
//        return status;
//    }
//
//    public void setStatus(int status) {
//        this.status = status;
//    }
//
//    public boolean isSetPassword() {
//        return isSetPassword == 1;
//    }
//
//    public void setSetPassword(int setPassword) {
//        isSetPassword = setPassword;
//    }
//
//    public StoreBean getStore() {
//        return store;
//    }
//
//    public void setStore(StoreBean store) {
//        this.store = store;
//    }
//
//    public class StoreBean {
//        private List<String> images;//店铺页面图
//        private String address;//店铺地址
//        private String name;//店铺名称
//        private String avatar;//店铺头像
//        private int id;//店铺id
//        private int rating; //评分
//        private int status; //状态 0=待审核 10=已认证 20=已驳回 40=已禁用 90=永久关闭
//        private int businessStatus; //经营状态  0=未营业  1=营业中
//
//        public List<String> getImages() {
//            return images;
//        }
//
//        public void setImages(List<String> images) {
//            this.images = images;
//        }
//
//        public String getAddress() {
//            return address;
//        }
//
//        public void setAddress(String address) {
//            this.address = address;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getAvatar() {
//            return avatar;
//        }
//
//        public void setAvatar(String avatar) {
//            this.avatar = avatar;
//        }
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public int getRating() {
//            return rating;
//        }
//
//        public void setRating(int rating) {
//            this.rating = rating;
//        }
//
//        public int getStatus() {
//            return status;
//        }
//
//        public void setStatus(int status) {
//            this.status = status;
//        }
//
//        public int getBusinessStatus() {
//            return businessStatus;
//        }
//
//        public void setBusinessStatus(int businessStatus) {
//            this.businessStatus = businessStatus;
//        }
//    }
}
