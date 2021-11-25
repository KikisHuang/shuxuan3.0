package com.kikis.commnlibrary.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 接收IM消息
 *
 * @author JM
 */
public class ReceiveIMMessageBean implements Serializable {

    private long id;//消息id
    private String sendIdentifier;//发送方用户id标识，与登录的identifier判断是否是自己发送
    private int type;//消息类型 0=文本 1=表情 10=图片 11=语音 12=视频 20=转账 21=收款 30=定位位置信息
    private int status;//状态。0=正常；1=撤回
    private String content;//消息内容
    private int voiceDuration;//语音时长
    public int recipientRead;//语音是否未读
    private String createTime;//创建时间 (发送时间)
    private SubscribeListVO subscribeListVO;//订阅详情
    private AddressBean msgAddress;//地址类型详情（type == 30 返回，否则返回 null）
    private MsgAccounts msgAccounts;//转账类型详情（type == 20 返回，否则返回 null）
    //图片类型上传进度
    public int upload_progress;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSendIdentifier() {
        return sendIdentifier;
    }

    public void setSendIdentifier(String sendIdentifier) {
        this.sendIdentifier = sendIdentifier;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public SubscribeListVO getSubscribeListVO() {
        return subscribeListVO;
    }

    public void setSubscribeListVO(SubscribeListVO subscribeListVO) {
        this.subscribeListVO = subscribeListVO;
    }

    public AddressBean getMsgAddress() {
        return msgAddress;
    }

    public void setMsgAddress(AddressBean msgAddress) {
        this.msgAddress = msgAddress;
    }

    public MsgAccounts getMsgAccounts() {
        return msgAccounts;
    }

    public void setMsgAccounts(MsgAccounts msgAccounts) {
        this.msgAccounts = msgAccounts;
    }


    /**
     * 订阅详情
     */
    public static class SubscribeListVO extends SubscribesListBean.SubscribesMessage implements Serializable {
        private long sendUserId;//消息发布者ID

        public long getSendUserId() {
            return sendUserId;
        }

        public void setSendUserId(long sendUserId) {
            this.sendUserId = sendUserId;
        }
    }


    /**
     * 转账类型详情
     */
    public static class MsgAccounts {
        private String id;//转账id
        private String tradeNo;//系统内部展示转账流水号
        private int status;//0=未付款；1=待领取；2=已收款；3=拒绝收款；4=过期退回
        private int payType;//转账方支付类型。10=微信,20=支付宝
        private BigDecimal amount;//":9.00    //转账金额


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getPayType() {
            return payType;
        }

        public void setPayType(int payType) {
            this.payType = payType;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }


    }
}
