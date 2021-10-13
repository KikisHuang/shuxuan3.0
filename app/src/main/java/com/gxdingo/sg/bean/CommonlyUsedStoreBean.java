package com.gxdingo.sg.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import static com.gxdingo.sg.utils.ClientLocalConstant.COMMONLY_USED_STORE_HORIZONTAL;

/**
 * @author: Kikis
 * @date: 2021/4/13
 * @page:
 */
public class CommonlyUsedStoreBean implements MultiItemEntity {

    private int itemType = COMMONLY_USED_STORE_HORIZONTAL;


    public CommonlyUsedStoreBean() {
    }

    public CommonlyUsedStoreBean(int itemType) {
        this.itemType = itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    /**
     * avatar : http://xxxxx.png
     * id : 1
     * latitude : 115.92471589062498
     * longitude : 115.92471589062498
     * name : NAME
     * rating : 3.2
     */

    private String avatar;
    private String id;
    private double latitude;
    private double longitude;
    private String name;
    private String distance;
    private double rating;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
