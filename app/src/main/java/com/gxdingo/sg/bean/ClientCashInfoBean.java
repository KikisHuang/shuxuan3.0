package com.gxdingo.sg.bean;

import java.util.List;

/**
 * @author: Weaving
 * @date: 2021/10/19
 * @page:
 */
public class ClientCashInfoBean {

    /**
     * mobile : 183****2826
     * withdrawalPassword : 1
     * version : 1.0
     * isShowAlipay : 1
     * isShowWechat : 1
     * isShowBank : 1
     * explain : 1、每3天可提现1次
     2、提现收取5%的服务费
     3、提现到账时间为1~3个工作日，如遇周六日及节假日则顺延
     4、提现声请提交后不可取消
     * alipay : 张三
     * wechat : null
     * bankList : [{"number":"***************0235","cardName":"储蓄卡","name":"工商银行","icon":"https://apimg.alipay.com/combo.png?d=cashier&t=ICBC","id":1,"type":"DC"}]
     */

    private String mobile;
    private Integer withdrawalPassword;
    private String version;
    private Integer isShowAlipay;
    private Integer isShowWechat;
    private Integer isShowBank;
    private String explain;
    private String alipay;
    private String wechat;
    private String balance;
    public int authStatus;
    public String authImage;
    public String rejectReason;
    private List<BankcardBean> bankList;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getWithdrawalPassword() {
        return withdrawalPassword;
    }

    public void setWithdrawalPassword(Integer withdrawalPassword) {
        this.withdrawalPassword = withdrawalPassword;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getIsShowAlipay() {
        return isShowAlipay;
    }

    public void setIsShowAlipay(Integer isShowAlipay) {
        this.isShowAlipay = isShowAlipay;
    }

    public Integer getIsShowWechat() {
        return isShowWechat;
    }

    public void setIsShowWechat(Integer isShowWechat) {
        this.isShowWechat = isShowWechat;
    }

    public Integer getIsShowBank() {
        return isShowBank;
    }

    public void setIsShowBank(Integer isShowBank) {
        this.isShowBank = isShowBank;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public List<BankcardBean> getBankList() {
        return bankList;
    }

    public void setBankList(List<BankcardBean> bankList) {
        this.bankList = bankList;
    }

}
