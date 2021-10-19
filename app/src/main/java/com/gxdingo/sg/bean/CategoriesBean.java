package com.gxdingo.sg.bean;

/**
 * @author: Weaving
 * @date: 2021/10/18
 * @page:
 */
public class CategoriesBean {

    private int id;
    private String name;
    private String image;

    public boolean isSelected = false;

    public CategoriesBean() {
    }

    public CategoriesBean(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
