package com.gxdingo.sg.bean;

import com.google.gson.annotations.SerializedName;
import com.kikis.commnlibrary.biz.FooAnnotation;

import java.util.List;

/**
 * @author: Weaving
 * @date: 2021/11/17
 * @page:
 */
public class StoreAuthInfoBean {

    /**
     * address : xxx
     * businessLicence : http://oss.gxdingo.com.png
     * licenceCode : 435413245343213243436
     * licenceName : 就行了氪金大佬市解放路
     * categoryList : [{"name":"特殊品类名称","prove":"http://oss.gxdingo.com.png"}]
     */

    private String address;
    private String businessLicence;
    private String licenceCode;
    private String licenceName;
    public int authStatus;
    private List<CategoryListBean> categoryList;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusinessLicence() {
        return businessLicence;
    }

    public void setBusinessLicence(String businessLicence) {
        this.businessLicence = businessLicence;
    }

    public String getLicenceCode() {
        return licenceCode;
    }

    public void setLicenceCode(String licenceCode) {
        this.licenceCode = licenceCode;
    }

    public String getLicenceName() {
        return licenceName;
    }

    public void setLicenceName(String licenceName) {
        this.licenceName = licenceName;
    }

    public List<CategoryListBean> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryListBean> categoryList) {
        this.categoryList = categoryList;
    }

    public static class CategoryListBean {
        /**
         * name : 特殊品类名称
         * prove : http://oss.gxdingo.com.png
         */
        @FooAnnotation
        private  String name;

        private String prove;
        @FooAnnotation
        public  String licenceName;

        public String categoryId;

        @FooAnnotation
        @SerializedName("rejectReason")
        private String rejectReason;

        @FooAnnotation
        @SerializedName("type")
        private  int type;

        @FooAnnotation
        @SerializedName("proveStatus")
        private int proveStatus;

        public transient boolean unUpload;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProve() {
            return prove;
        }

        public void setProve(String prove) {
            this.prove = prove;
        }

        public String getRejectReason() {
            return rejectReason;
        }

        public void setRejectReason(String rejectReason) {
            this.rejectReason = rejectReason;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getProveStatus() {
            return proveStatus;
        }

        public void setProveStatus(int proveStatus) {
            this.proveStatus = proveStatus;
        }
    }
}
