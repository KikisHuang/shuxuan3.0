package com.gxdingo.sg.bean;

import java.util.List;

/**
 * @author: Weaving
 * @date: 2021/10/18
 * @page:
 */
public class CategoriesBean {


    /**
     * image : http://oss.dgkjmm.com/upload/20210419/352f2c98c8d94bc5882f4e975c8a4159.png
     * name : 分类11.11
     * id : 13
     * keyword : [""]
     * status : 1
     */

    private String image;
    private String name;
    private Integer id;
    private List<String> keyword;
    private Integer status;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getKeyword() {
        return keyword;
    }

    public void setKeyword(List<String> keyword) {
        this.keyword = keyword;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


}
