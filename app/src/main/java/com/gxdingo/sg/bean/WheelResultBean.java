package com.gxdingo.sg.bean;

/**
 * @author: Weaving
 * @date: 2021/12/6
 * @page:
 */
public class WheelResultBean {
    public int jumpType;
    public int type;
    public String helpCode;
    public String url;
    public String image;
    public String title;
    public String describe;

    public WheelResultBean(int jumpType, int type, String helpCode, String url, String image, String title, String describe) {
        this.jumpType = jumpType;
        this.type = type;
        this.helpCode = helpCode;
        this.url = url;
        this.image = image;
        this.title = title;
        this.describe = describe;
    }
}
