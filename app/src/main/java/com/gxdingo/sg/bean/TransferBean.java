package com.gxdingo.sg.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author: Weaving
 * @date: 2021/6/21
 * @page:
 */
public class TransferBean {


    @SerializedName("code")
    private int code;
    @SerializedName("data")
    private DataDTO data;
    @SerializedName("info")
    private String info;
    @SerializedName("msg")
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataDTO {
        @SerializedName("payNo")
        private long payNo;
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
            @SerializedName("id")
            private int id;
            @SerializedName("tradeNo")
            private long tradeNo;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public long getTradeNo() {
                return tradeNo;
            }

            public void setTradeNo(long tradeNo) {
                this.tradeNo = tradeNo;
            }
        }
    }
}
