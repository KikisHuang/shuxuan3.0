package com.gxdingo.sg.bean;

import java.io.Serializable;

/**
 * 商圈评论未读数量
 * @author JM
 */
public class NumberUnreadCommentsBean implements Serializable {
    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    private int unread;

}
