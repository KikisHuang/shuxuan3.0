package com.gxdingo.sg.bean;

/**
 * @author: Weaving
 * @date: 2021/10/28
 * @page:
 */
public class OneKeyLoginEvent {

    public String code;

    public boolean isUser;

    public OneKeyLoginEvent(String code,boolean isUser) {
        this.code = code;
        this.isUser=isUser;
    }
}
