package com.gxdingo.sg.bean;

/**
 * @author: Weaving
 * @date: 2021/12/2
 * @page:
 */
public class HelpBean {

    /**
     * buttonToUrl : https://gimg2.bai48d1e724
     * image : https://dgkj_box.png
     * title : 我在参与助力活动\n快来助我一臂之力吧
     * lotteryCount : 1
     */

    private String buttonToUrl;
    private String image;
    private String title;
    private String subtitle;
    private Integer lotteryCount;

    public String getButtonToUrl() {
        return buttonToUrl;
    }

    public void setButtonToUrl(String buttonToUrl) {
        this.buttonToUrl = buttonToUrl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Integer getLotteryCount() {
        return lotteryCount;
    }

    public void setLotteryCount(Integer lotteryCount) {
        this.lotteryCount = lotteryCount;
    }
}
