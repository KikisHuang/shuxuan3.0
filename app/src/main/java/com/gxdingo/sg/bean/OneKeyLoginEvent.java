package com.gxdingo.sg.bean;

/**
 * @author: Weaving
 * @date: 2021/10/28
 * @page:
 */
public class OneKeyLoginEvent {

    public String code;

    public boolean isUser;

    //默认0一键登录 1支付宝 2微信
    public int type ;

    public OneKeyLoginEvent(String code,boolean isUser) {
        this.code = code;
        this.isUser=isUser;
    }

    public OneKeyLoginEvent(String code, boolean isUser, int type) {
        this.code = code;
        this.isUser = isUser;
        this.type = type;
    }
}
