package com.gxdingo.sg.bean;

import java.util.List;

/**
 * @author: Weaving
 * @date: 2021/10/19
 * @page:
 */
public class StoreListBean {

    private List<StoreBean> stores;

    public List<StoreBean> getList() {
        return stores;
    }

    public void setList(List<StoreBean> stores) {
        this.stores = stores;
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
        private String number;
        private String distance;
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
            return number;
        }

        public void setContactNumber(String contactNumber) {
            this.number = contactNumber;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public List<String> getClassNameList() {
            return classNameList;
        }

        public void setClassNameList(List<String> classNameList) {
            this.classNameList = classNameList;
        }
    }
}
