package com.gxdingo.sg.bean;

public class SendMessageBean {

    public String content;

    //0 private 1 room
    public int message_type = 0;

    // 新消息类型 消息类型 0=文本 1=表情 10=图片 11=语音 12=视频 20=转账 21=收款 30=定位位置信息
    //事件类型
    public String type = "0";

    //聊天id
    public String chatId;

    public SendMessageBean() {
    }

    public SendMessageBean(String chatId, String msg) {
        this.content = msg;
        this.chatId = chatId;
    }

    public SendMessageBean(String content, String type, String chatId) {
        this.content = content;
        this.type = type;
        this.chatId = chatId;
    }

}
