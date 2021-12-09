package com.gxdingo.sg.bean;

/**
 * @author: Weaving
 * @date: 2021/12/2
 * @page:
 */
public class HomeBannerBean {

    /**
     * id : 1
     * position : app-home-middle
     * identifier :
     * title :
     * type : 2
     * image : https://dg8c5154.png
     * page : http://1table/lottery
     * remark : 大转盘活动
     */

    private Integer id;
    private String position;
    private String identifier;
    private String title;
    private Integer type;
    private String image;
    private String page;
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
