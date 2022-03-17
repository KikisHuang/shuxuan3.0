package com.gxdingo.sg.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 商圈评论未读数量
 * @author JM
 */
public class NumberUnreadCommentsBean implements Serializable {
    @SerializedName("popupCouponList")
    private List<UserReward.CouponListDTO> popupCouponList;

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    private int unread;
    private int circleUnread;

    public int getCircleUnread() {
        return circleUnread;
    }

    public void setCircleUnread(int circleUnread) {
        this.circleUnread = circleUnread;
    }

    public List<UserReward.CouponListDTO> getPopupCouponList() {
        return popupCouponList;
    }

    public void setPopupCouponList(List<UserReward.CouponListDTO> popupCouponList) {
        this.popupCouponList = popupCouponList;
    }
}
