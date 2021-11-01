package com.gxdingo.sg.bean;

/**
 * @author: Kikis
 * @date: 2021/4/20
 * @page:
 */
public class WeChatLoginEvent {

    public String code;

    //区分登录和绑定
    public boolean login;

    public WeChatLoginEvent(String code) {
        this.code = code;
    }

    public WeChatLoginEvent(String code, boolean login) {
        this.code = code;
        this.login = login;
    }
}
