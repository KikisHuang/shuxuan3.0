package com.gxdingo.sg.bean;

/**
 * @author: Weaving
 * @date: 2021/10/28
 * @page:
 */
public class OneKeyLoginEvent {

    public String code;


    //默认0一键登录 1支付宝 2微信
    public int type ;

    public OneKeyLoginEvent(String code ) {
        this.code = code;
    }

    public OneKeyLoginEvent(String code, int type) {
        this.code = code;
        this.type = type;
    }
}
