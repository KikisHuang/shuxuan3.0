package com.kikis.commnlibrary.bean;

/**
 * @author: Kikis
 * @date: 2021/3/10
 * @page:
 */
public class OrderBean {

    /**
     * id : 3541
     * productId : 350
     * productImage : http://xxxxxxx.com/image.jpg
     * productTitle : 您购买的小金砖反重力唇釉...
     * storeAvatar : http://xxxxxxx.com/image.jpg
     * storeId : 35
     * storeName : 店铺名称
     * tradeNo : 210235413333
     */

    private long id;
    private int productId;
    private String productImage;
    private String productTitle;
    private String storeAvatar;
    private int storeId;
    private String storeName;
    private long tradeNo;

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getStoreAvatar() {
        return storeAvatar;
    }

    public void setStoreAvatar(String storeAvatar) {
        this.storeAvatar = storeAvatar;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public long getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(long tradeNo) {
        this.tradeNo = tradeNo;
    }
}
