package com.gxdingo.sg.bean;

public class SocketLoginEvent {


    public String exec;

    public String timestamp;

    public String sign;

    public String crossToken;


    public SocketLoginEvent(String exec, String timestamp, String sign, String crossToken) {
        this.exec = exec;
        this.timestamp = timestamp;
        this.sign = sign;
        this.crossToken = crossToken;
    }
    public SocketLoginEvent(String exec, String timestamp, String sign) {
        this.exec = exec;
        this.timestamp = timestamp;
        this.sign = sign;
    }
}
