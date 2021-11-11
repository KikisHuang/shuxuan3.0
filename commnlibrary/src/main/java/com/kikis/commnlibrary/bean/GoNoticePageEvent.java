package com.kikis.commnlibrary.bean;

import android.util.Log;

/**
 * @author: Kikis
 * @date: 2021/3/4
 * @page:
 */
public class GoNoticePageEvent {

    public String id = "";

    public int type = 0;

    public GoNoticePageEvent(String id, int type) {
        this.id = id;
        this.type = type;
    }
}
