package com.kikis.commnlibrary.bean;

/**
 * @author: Kikis
 * @date: 2021/2/23
 * @page:
 */
public class ProductBean {
    /**
     * id : 3541
     * image : http://xxxxxxx.com/image.jpg
     * price : ￥88.00
     * title : xxxxxxxxxxxxxxx
     */

    private int id;
    private String image;
    private String price;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
