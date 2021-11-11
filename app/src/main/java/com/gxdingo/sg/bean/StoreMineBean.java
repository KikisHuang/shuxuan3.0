package com.gxdingo.sg.bean;

import java.util.List;

/**
 * @author: Weaving
 * @date: 2021/11/10
 * @page:
 */
public class StoreMineBean {

    /**
     * adsList : [{"image":"http://oss.dgkjmm.com/upload/20.png","id":9,"page":"http://192./userIntroduction","title":"","type":2}]
     * name : 兴宁区Geiger
     * closeTime : 1969-12-31T16:00:00.000+00:00
     * categoryList : [{"prove":"http://oss.029/5154aa4613036a754b.png","name":"煤气"}]
     * avatar : http://oss.dgkjmm.56.png
     * openTime : 1970-01-01T00:30:00.000+00:00
     */

    private List<AdsListBean> adsList;
    private String name;
    private String closeTime;
    private List<CategoryListBean> categoryList;
    private String avatar;
    private String openTime;

    public List<AdsListBean> getAdsList() {
        return adsList;
    }

    public void setAdsList(List<AdsListBean> adsList) {
        this.adsList = adsList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public List<CategoryListBean> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryListBean> categoryList) {
        this.categoryList = categoryList;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public static class AdsListBean {
        /**
         * image : http://oss.dgkjmm.com/upload/20.png
         * id : 9
         * page : http://192./userIntroduction
         * title :
         * type : 2
         */

        private String image;
        private Integer id;
        private String page;
        private String title;
        private Integer type;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }
    }

    public static class CategoryListBean {
        /**
         * prove : http://oss.029/5154aa4613036a754b.png
         * name : 煤气
         */

        private String prove;
        private String name;

        public String getProve() {
            return prove;
        }

        public void setProve(String prove) {
            this.prove = prove;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
