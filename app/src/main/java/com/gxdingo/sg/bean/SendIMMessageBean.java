package com.gxdingo.sg.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * 发送IM消息
 *
 * @author JM
 */
public class SendIMMessageBean implements Serializable {
    private String shareUuid;//发布者与订阅者的共享唯一id，使用该值查出聊天记录
    private int type;//消息类型 0=文本 1=表情 10=图片 11=语音 12=视频 20=转账 21=收款 30=定位位置信息
    private String content;//内容（type < 20 必传）
    private int voiceDuration;//语音时长（秒）（type = 11 必传）
    private Map<String, Object> params;// key=id,value=地址id、转账id（type >= 20 必传）


    public SendIMMessageBean() {
    }

    public SendIMMessageBean(String shareUuid, int type, String content, int voiceDuration, Map<String, Object> params) {
        this.shareUuid = shareUuid;
        this.type = type;
        this.content = content;
        this.voiceDuration = voiceDuration;
        this.params = params;
    }

    public String getShareUuid() {
        return shareUuid;
    }

    public void setShareUuid(String shareUuid) {
        this.shareUuid = shareUuid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getVoiceDuration() {
        return voiceDuration;
    }

    public void setVoiceDuration(int voiceDuration) {
        this.voiceDuration = voiceDuration;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
