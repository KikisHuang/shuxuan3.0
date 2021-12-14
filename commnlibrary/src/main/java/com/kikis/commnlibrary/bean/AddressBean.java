package com.kikis.commnlibrary.bean;

import java.io.Serializable;

public class AddressBean implements Serializable {

    /**
     * id : 2
     * mobile : 12345678901
     * name : 安大花
     * gender : 1
     * regionPath : 广西 南宁
     * street : 老鼠街 老鼠洞
     * tag : 家
     * isDefault : 1
     * longitude : 0.0
     * latitude : 0.0
     */

    private int id = 0;
    private String mobile;
    private String name;
    private int gender;

    //自定义参数0不返回 1 首页 2聊天
    public int selectType;
    private String regionPath;
    private String regionPathStr;
    public String identifier;
    private String street;

    public String getDoorplate() {
        return doorplate;
    }

    public void setDoorplate(String doorplate) {
        this.doorplate = doorplate;
    }

    private String doorplate = "";
    private String tag;
    private int isDefault;
    public int result;
    private double longitude;
    private double latitude;

    public String defaultCacheTime;

    public String getRegionPathStr() {
        return regionPathStr;
    }

    public void setRegionPathStr(String regionPathStr) {
        this.regionPathStr = regionPathStr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getRegionPath() {
        return regionPath;
    }

    public void setRegionPath(String regionPath) {
        this.regionPath = regionPath;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
