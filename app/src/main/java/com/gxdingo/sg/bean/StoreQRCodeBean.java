package com.gxdingo.sg.bean;

/**
 * @author: Weaving
 * @date: 2021/11/10
 * @page:
 */
public class StoreQRCodeBean {

    /**
     * couponIdIdentifier : invite-new-register
     * explain : <p>使用规则</p><p>1.</p><p>2.</p>
     * storeAvatar : http://oss.b22356.png
     * storeName : 兴宁区Geiger
     * url : https://192.168.110.236:818te-new-register
     * activeCode : S51J2V4TAL
     */

    private String couponIdIdentifier;
    private String explain;
    private String storeAvatar;
    private String storeName;
    private String url;
    private String activeCode;

    public String getCouponIdIdentifier() {
        return couponIdIdentifier;
    }

    public void setCouponIdIdentifier(String couponIdIdentifier) {
        this.couponIdIdentifier = couponIdIdentifier;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getActiveCode() {
        return activeCode;
    }

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }
}
