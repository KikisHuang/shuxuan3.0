package com.gxdingo.sg.bean;

import java.util.List;

/**
 * @author: Weaving
 * @date: 2021/6/2
 * @page:
 */
public class StoreBusinessScopeBean {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 1
         * path : 1/
         * name : 一级分类
         * type : 0
         * typeDescr :
         */

        private Integer id;
        private String path;
        private String name;
        private Integer type;
        private String typeDescr;
        private boolean isSelect;
        public String typeDescribe;
        public String licenceName;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getTypeDescr() {
            return typeDescr;
        }

        public void setTypeDescr(String typeDescr) {
            this.typeDescr = typeDescr;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }
    }
}
