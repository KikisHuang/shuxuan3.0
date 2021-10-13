package com.kikis.commnlibrary.bean;

/**
 * @author: Kikis
 * @date: 2020/11/18
 * @page:
 */
public class NewMessage {


    /**
     * subscribeId : 2
     * fromId : 8
     * fromName : 很好
     * fromAvatar : http://oss.dgkjmm.com/upload/20200730/643fe7fed3d7438eb767c86f75471dce.jpg
     * fromType : 10
     * content : 消息内容
     * msgType : 0
     * msgTime : 2020-08-26 17:32:29
     */

    private int subscribeId;
    private int fromId;
    private String fromName;
    private String fromAvatar;
    private int fromType;
    private String content;
    private int msgType;
    private String msgTime;

    private OrderBean order;
    private RefundBean refund;

    private ProductBean product;

    private ButtonBean buttons;

    public ButtonBean getButtons() {
        return buttons;
    }

    public void setButtons(ButtonBean buttons) {
        this.buttons = buttons;
    }

    public RefundBean getRefund() {
        return refund;
    }

    public void setRefund(RefundBean refund) {
        this.refund = refund;
    }

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public int getSubscribeId() {
        return subscribeId;
    }

    public void setSubscribeId(int subscribeId) {
        this.subscribeId = subscribeId;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromAvatar() {
        return fromAvatar;
    }

    public void setFromAvatar(String fromAvatar) {
        this.fromAvatar = fromAvatar;
    }

    public int getFromType() {
        return fromType;
    }

    public void setFromType(int fromType) {
        this.fromType = fromType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }


   public class ButtonBean {


        /**
         * showPay : 1
         * showCancel : 1
         * showChoose : 1
         * showRandom : 1
         */

        private String showPay;
        private String showCancel;
        private String showChoose;
        private String showRandom;

        public String getShowPay() {
            return showPay;
        }

        public void setShowPay(String showPay) {
            this.showPay = showPay;
        }

        public String getShowCancel() {
            return showCancel;
        }

        public void setShowCancel(String showCancel) {
            this.showCancel = showCancel;
        }

        public String getShowChoose() {
            return showChoose;
        }

        public void setShowChoose(String showChoose) {
            this.showChoose = showChoose;
        }

        public String getShowRandom() {
            return showRandom;
        }

        public void setShowRandom(String showRandom) {
            this.showRandom = showRandom;
        }
    }

}