package com.kikis.commnlibrary.bean;

public class ReLoginBean {
    public String errormsg;
    public int code;


    public ReLoginBean() {
    }

    public ReLoginBean(int code,String errormsg) {
        this.errormsg = errormsg;
        this.code = code;
    }
}
