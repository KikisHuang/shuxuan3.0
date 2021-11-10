package com.gxdingo.sg.bean;

/**
 * @author: Kikis
 * @date: 2021/5/19
 * @page:
 */
public class SubscribesBean {


    /**
     * sendAvatar : http://o0.png
     * sendNickname : 江南区狗子请进
     * sendUserRole : 11
     * shareUuid : f0872838-3f6f-4358-8930-23d71a0c543c
     * sendIdentifier : S51J2V4T98
     * category : 10
     * categoryText : 消息
     * unreadNum : 0
     * lastMsg : hello
     * lastMsgType : 0
     * updateTime : 2021-09-30T06:22:02.000+00:00
     */

    private String sendAvatar;
    private String sendNickname;
    private Integer sendUserRole;
    private String shareUuid;
    private String sendIdentifier;
    private Integer category;
    private String categoryText;
    private Integer unreadNum;
    private String lastMsg;
    private Integer lastMsgType;
    private String updateTime;

    public String getSendAvatar() {
        return sendAvatar;
    }

    public void setSendAvatar(String sendAvatar) {
        this.sendAvatar = sendAvatar;
    }

    public String getSendNickname() {
        return sendNickname;
    }

    public void setSendNickname(String sendNickname) {
        this.sendNickname = sendNickname;
    }

    public Integer getSendUserRole() {
        return sendUserRole;
    }

    public void setSendUserRole(Integer sendUserRole) {
        this.sendUserRole = sendUserRole;
    }

    public String getShareUuid() {
        return shareUuid;
    }

    public void setShareUuid(String shareUuid) {
        this.shareUuid = shareUuid;
    }

    public String getSendIdentifier() {
        return sendIdentifier;
    }

    public void setSendIdentifier(String sendIdentifier) {
        this.sendIdentifier = sendIdentifier;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getCategoryText() {
        return categoryText;
    }

    public void setCategoryText(String categoryText) {
        this.categoryText = categoryText;
    }

    public Integer getUnreadNum() {
        return unreadNum;
    }

    public void setUnreadNum(Integer unreadNum) {
        this.unreadNum = unreadNum;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public Integer getLastMsgType() {
        return lastMsgType;
    }

    public void setLastMsgType(Integer lastMsgType) {
        this.lastMsgType = lastMsgType;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
