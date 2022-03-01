package com.kikis.commnlibrary.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 获取订阅列表
 *
 * @author: JM
 */
public class SubscribesListBean {

    private String websocketUrl;//web socket接入url
    private ArrayList<SubscribesMessage> list;

    public String getWebsocketUrl() {
        return websocketUrl;
    }

    public void setWebsocketUrl(String websocketUrl) {
        this.websocketUrl = websocketUrl;
    }

    public ArrayList<SubscribesMessage> getList() {
        return list;
    }

    public void setList(ArrayList<SubscribesMessage> list) {
        this.list = list;
    }

    public static class SubscribesMessage implements Serializable {
        private String sendAvatar;//发送方头像
        private String sendNickname;//发送方昵称
        private int sendUserRole;//*发s送方角色。10=用户；11=商家；12=客服；13=系统
        private String shareUuid; //订阅UUID，使用该值查出聊天记录
        private String sendIdentifier;//*消息发布者id标识
        private int category;//订阅类别 10=消息  20=转账通知  99=系统通知
        private String categoryText;//category对应的文本
        private int unreadNum;//未读消息数
        private String lastMsg;//最后一条消息内容
        private int lastMsgType;//最后一条消息类型 0=文本 1=表情 10=图片 11=语音 12=视频 20=转账 21=收款 30=定位位置信息
        private String updateTime;//最后发送时间
        public int sort; //置顶值，值越大越靠前。0=未置顶；>0 大于零已置顶
        public String id; //订阅列表id
        public int avatarIcon; //头像图标。0不显示；1 = 显示商家图标

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

        public int getSendUserRole() {
            return sendUserRole;
        }

        public void setSendUserRole(int sendUserRole) {
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

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public String getCategoryText() {
            return categoryText;
        }

        public void setCategoryText(String categoryText) {
            this.categoryText = categoryText;
        }

        public int getUnreadNum() {
            return unreadNum;
        }

        public void setUnreadNum(int unreadNum) {
            this.unreadNum = unreadNum;
        }

        public String getLastMsg() {
            return lastMsg;
        }

        public void setLastMsg(String lastMsg) {
            this.lastMsg = lastMsg;
        }

        public int getLastMsgType() {
            return lastMsgType;
        }

        public void setLastMsgType(int lastMsgType) {
            this.lastMsgType = lastMsgType;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }
}
