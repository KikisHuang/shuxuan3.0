package com.gxdingo.sg.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserReward {

    @SerializedName("couponList")
    private List<CouponListDTO> couponList;

    public List<CouponListDTO> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<CouponListDTO> couponList) {
        this.couponList = couponList;
    }

    public static class CouponListDTO {
        @SerializedName("name")
        private String name;
        @SerializedName("couponIdentifier")
        private String couponIdentifier;
        @SerializedName("remark")
        private String remark;
        @SerializedName("amount")
        private double amount;
        @SerializedName("useAmount")
        private double useAmount;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCouponIdentifier() {
            return couponIdentifier;
        }

        public void setCouponIdentifier(String couponIdentifier) {
            this.couponIdentifier = couponIdentifier;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getUseAmount() {
            return useAmount;
        }

        public void setUseAmount(double useAmount) {
            this.useAmount = useAmount;
        }
    }
}
