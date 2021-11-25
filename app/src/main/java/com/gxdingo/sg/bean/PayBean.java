package com.gxdingo.sg.bean;

import com.google.gson.annotations.SerializedName;

public class PayBean {


    @SerializedName("payNo")
    private long payNo;
    @SerializedName("wxpayInfo")
    private WxpayInfo wxpayInfo;
    @SerializedName("alipayInfo")
    private String alipayInfo;
    @SerializedName("payStatus")
    private String payStatus;
    @SerializedName("transferAccounts")
    private TransferAccountsDTO transferAccounts;

    public long getPayNo() {
        return payNo;
    }

    public void setPayNo(long payNo) {
        this.payNo = payNo;
    }

    public WxpayInfo getWxpayInfo() {
        return wxpayInfo;
    }

    public void setWxpayInfo(WxpayInfo wxpayInfo) {
        this.wxpayInfo = wxpayInfo;
    }

    public String getAlipayInfo() {
        return alipayInfo;
    }

    public void setAlipayInfo(String alipayInfo) {
        this.alipayInfo = alipayInfo;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public TransferAccountsDTO getTransferAccounts() {
        return transferAccounts;
    }

    public void setTransferAccounts(TransferAccountsDTO transferAccounts) {
        this.transferAccounts = transferAccounts;
    }


    public static class TransferAccountsDTO {
        @SerializedName("tradeNo")
        private long tradeNo;
        @SerializedName("id")
        private int id;

        public long getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(long tradeNo) {
            this.tradeNo = tradeNo;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
