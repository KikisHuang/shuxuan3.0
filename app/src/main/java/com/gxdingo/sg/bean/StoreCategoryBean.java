package com.gxdingo.sg.bean;

/**
 * @author: Weaving
 * @date: 2021/6/3
 * @page:
 */
public class StoreCategoryBean {

    private long categoryId;
    private String prove;

    public StoreCategoryBean(long categoryId, String prove) {
        this.categoryId = categoryId;
        this.prove = prove;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getProve() {
        return prove;
    }

    public void setProve(String prove) {
        this.prove = prove;
    }
}
