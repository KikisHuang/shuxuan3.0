package com.kikis.commnlibrary.bean;

/**
 * @author: Kikis
 * @date: 2021/2/24
 * @page:
 */
public class RefundBean {

    /**
     * id : 3541
     * storeId : 35
     * storeName : 店铺名称
     * storeAvatar : http://xxxxxxx.com/image.jpg
     * productId : 350
     * productTitle : 您购买的小金砖反重力唇釉...
     * productImage : http://xxxxxxx.com/image.jpg
     */

    private int id;
    private int storeId;
    private String storeName;
    private String storeAvatar;
    private int productId;
    private String productTitle;
    private String productImage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getStoreAvatar() {
        return storeAvatar;
    }

    public void setStoreAvatar(String storeAvatar) {
        this.storeAvatar = storeAvatar;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}
