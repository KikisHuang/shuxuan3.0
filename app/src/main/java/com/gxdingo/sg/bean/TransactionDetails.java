package com.gxdingo.sg.bean;

/**
 * @author: Weaving
 * @date: 2021/11/12
 * @page:
 */
public class TransactionDetails {


    /**
     * avatar : https://oss.gxdingo.com/log/shuxuan.jpg
     * title : 提现
     * explain : 提现
     * payType : 中国农业银行提现（4476）
     * amount : 100.0
     * type : 2
     * statusText : 待处理
     * createTime : 2021-10-25T08:13:04.000+00:00
     * payTime :
     * receiveTime :
     * paymentTime :
     */

    private String avatar;
    private String title;
    private String explain;
    private String payType;
    private Double amount;
    private Integer type;
    private String statusText;
    private String createTime;
    private String payTime;
    private String receiveTime;
    private String paymentTime;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }
}
