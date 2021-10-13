package com.gxdingo.sg.bean;

public class SocketPingEvent {


    public String exec;

    public String timestamp;

    public String sign;


    public SocketPingEvent(String exec, String timestamp, String sign) {
        this.exec = exec;
        this.timestamp = timestamp;
        this.sign = sign;
    }
}
