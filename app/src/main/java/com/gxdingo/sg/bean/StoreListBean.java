package com.gxdingo.sg.bean;

import com.kikis.commnlibrary.bean.AddressBean;

import java.util.List;

/**
 * @author: Weaving
 * @date: 2021/10/19
 * @page:
 */
public class StoreListBean {

    private AddressBean userAddress;

    private List<StoreBean> list;

    public AddressBean getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(AddressBean userAddress) {
        this.userAddress = userAddress;
    }

    public List<StoreBean> getList() {
        return list;
    }

    public void setList(List<StoreBean> list) {
        this.list = list;
    }

    public static class StoreBean {
        /**
         * id : 24
         * avatar : /storage/emulOP_20210618_09373637.png
         * name : 便利店
         * contactNumber : 13557466632
         * distance : 3593.0
         * classNameList : ["煤气","送水","便利店"]
         */

        private Integer id;
        private String avatar;
        private String name;
        private String contactNumber;
        private String distance;
        //是否显示附近商家布局
        private boolean showTop;
        private List<String> classNameList;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public boolean isShowTop() {
            return showTop;
        }

        public void setShowTop(boolean showTop) {
            this.showTop = showTop;
        }

        public List<String> getClassNameList() {
            return classNameList;
        }

        public void setClassNameList(List<String> classNameList) {
            this.classNameList = classNameList;
        }
    }
}
