package com.gxdingo.sg.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Weaving
 * @date: 2021/11/11
 * @page:
 */
public class StoreWalletBean implements Serializable {


    /**
     * isShowWechat : 1
     * isShowAlipay : 0
     * isShowBank : 0
     * explain : 1、每3天可提现1次
     * 2、提现不收取服务费
     * 3、。
     * alipay :
     * wechat :
     * balance : 9108.01
     * bankList : [{"number":"***************0235","cardName":"储蓄卡","name":"中国建设银行","icon":"https://dgkjmm.oss-cn-shenzbank_icon/CCB.png","id":2,"type":"DC"}]
     * transactionList : [{"id":270,"type":31,"amount":3,"balance":9108.01,"description":"核销新注册用户二维码","createTime":"2021-10-14T05:45:29.000+00:00"},{"id":269,"type":30,"amount":2,"balance":9105.01,"description":"邀请新用户注册奖励","createTime":"2021-10-14T05:10:31.000+00:00"}]
     */

    private Integer isShowWechat;
    private Integer isShowAlipay;
    private Integer isShowBank;
    private String explain;
    private String alipay;
    public int authStatus;
    public String authImage;
    private String wechat;
    private Double balance;
    private List<BankcardBean> bankList;
    private List<TransactionBean> transactionList;

    public Integer getIsShowWechat() {
        return isShowWechat;
    }

    public void setIsShowWechat(Integer isShowWechat) {
        this.isShowWechat = isShowWechat;
    }

    public Integer getIsShowAlipay() {
        return isShowAlipay;
    }

    public void setIsShowAlipay(Integer isShowAlipay) {
        this.isShowAlipay = isShowAlipay;
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

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public List<BankcardBean> getBankList() {
        return bankList;
    }

    public void setBankList(List<BankcardBean> bankList) {
        this.bankList = bankList;
    }

    public List<TransactionBean> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<TransactionBean> transactionList) {
        this.transactionList = transactionList;
    }


}
