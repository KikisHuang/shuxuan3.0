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
    private MsgAddress msgAddress;//地址类型详情（type == 30 返回，否则返回 null）
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

    public MsgAddress getMsgAddress() {
        return msgAddress;
    }

    public void setMsgAddress(MsgAddress msgAddress) {
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
     * 地址类型详情
     */
    public static class MsgAddress implements Serializable {
        private long id;//用户收货地址id
        private String mobile;//收件人手机号
        private String name;//收件人名称
        private int gender;//性别：1先生，2女士
        private String regionPath;//地区路径
        private String street;//收件人地址
        private String doorplate;//门牌号
        private double longitude;//坐标经度
        private double latitude;//坐标纬度


        public long getId() {
            return id;
        }

        public void setId(long id) {
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

        public String getDoorplate() {
            return doorplate;
        }

        public void setDoorplate(String doorplate) {
            this.doorplate = doorplate;
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
