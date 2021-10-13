package com.kikis.commnlibrary.bean;

import android.util.Log;

/**
 * @author: Kikis
 * @date: 2021/3/4
 * @page:
 */
public class GoNoticePageEvent {

    public long orderId = 0;
    public long id = 0;

    public int type = 0;

    public GoNoticePageEvent(long orderId, long id,int type) {
        this.orderId = orderId;
        this.id = id;
        this.type= type;
    }
}
