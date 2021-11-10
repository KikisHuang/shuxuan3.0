package com.gxdingo.sg.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * IM聊天记录列表
 *
 * @author JM
 */
public class IMChatHistoryListBean implements Serializable {
    private String websocketUrl;//socket接入url
    private MyAvatarInfo myAvatarInfo;//我的头像信息
    private Address address;//收货地址
    private OtherAvatarInfo otherAvatarInfo;//对方头像信息
    private boolean isShowAddressModule;//是否显示没有收货地址弹窗（用户端没有收货地址时才会显示true）
    private String shareUuid;//双向唯一id，发送消息以及查看聊天列表信息需要用到
    private ArrayList<ReceiveIMMessageBean> list;


    public String getWebsocketUrl() {
        return websocketUrl;
    }

    public void setWebsocketUrl(String websocketUrl) {
        this.websocketUrl = websocketUrl;
    }

    public MyAvatarInfo getMyAvatarInfo() {
        return myAvatarInfo;
    }

    public void setMyAvatarInfo(MyAvatarInfo myAvatarInfo) {
        this.myAvatarInfo = myAvatarInfo;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public OtherAvatarInfo getOtherAvatarInfo() {
        return otherAvatarInfo;
    }

    public void setOtherAvatarInfo(OtherAvatarInfo otherAvatarInfo) {
        this.otherAvatarInfo = otherAvatarInfo;
    }

    public boolean isShowAddressModule() {
        return isShowAddressModule;
    }

    public void setShowAddressModule(boolean showAddressModule) {
        isShowAddressModule = showAddressModule;
    }

    public String getShareUuid() {
        return shareUuid;
    }

    public void setShareUuid(String shareUuid) {
        this.shareUuid = shareUuid;
    }

    public ArrayList<ReceiveIMMessageBean> getList() {
        return list;
    }

    public void setList(ArrayList<ReceiveIMMessageBean> list) {
        this.list = list;
    }


    /**
     * 我的头像信息
     */
    public static class MyAvatarInfo implements Serializable {
        private int sendRole;//角色：10=用户；11=商家；12=客服
        private String sendIdentifier;//我的id标识（与登录的id标识一致）
        private String sendAvatar;//我的头像
        private String sendNickname;//我的昵称
        private int id;//店铺id（role == 11时返回，默认返回0）
        private String mobile;//联系电话号码（默认值空字符串=""）

        public int getSendRole() {
            return sendRole;
        }

        public void setSendRole(int sendRole) {
            this.sendRole = sendRole;
        }

        public String getSendIdentifier() {
            return sendIdentifier;
        }

        public void setSendIdentifier(String sendIdentifier) {
            this.sendIdentifier = sendIdentifier;
        }

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


    }

    /**
     * 收货地址
     */
    public static class Address implements Serializable {
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
     * 对方头像信息
     */
    public static class OtherAvatarInfo implements Serializable {
        private int sendRole; //角色：10=用户；11=商家；12=客服
        private String sendIdentifier;//对方的id标识
        private String sendAvatar;//对方的头像
        private String sendNickname;//对方的昵称
        private long id;//店铺id（role == 11时返回，默认返回0）
        private String mobile;//联系电话号码（默认值空字符串=""）
        
        public int getSendRole() {
            return sendRole;
        }

        public void setSendRole(int sendRole) {
            this.sendRole = sendRole;
        }

        public String getSendIdentifier() {
            return sendIdentifier;
        }

        public void setSendIdentifier(String sendIdentifier) {
            this.sendIdentifier = sendIdentifier;
        }

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


    }
}
