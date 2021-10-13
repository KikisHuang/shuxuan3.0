package com.gxdingo.sg.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import static com.gxdingo.sg.utils.ClientLocalConstant.ITEMS1;

/**
 * @author: Kikis
 * @date: 2021/4/13
 * @page:
 */
public class MessageBean implements MultiItemEntity {

    private int itemType = ITEMS1;


    public MessageBean() {

    }

    public MessageBean(int itemType) {
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
     * content : 消息内容
     * fromId : 8
     * msgTime : 2020-08-26 17:32:29
     * type : 21
     * url : http://xxxxxxx.com/image.jpg
     */

    private String content;

    private int fromId;

    private String msgTime;

    private int type;

    private String url;

//    public OrderBean order;

    //图片类型上传进度
    public int upload_progress;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
