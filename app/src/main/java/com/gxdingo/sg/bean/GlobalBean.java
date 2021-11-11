package com.gxdingo.sg.bean;

public class GlobalBean {
    //0删除、
    public int event = 0;

    public String flagPage = "";
    public int emotion_map_type = 0;
    public String emotionName = "";


    public GlobalBean(int event, String emotionName, int emotion_map_type, String flagPage) {
        this.event = event;
        this.emotionName = emotionName;
        this.flagPage = flagPage;
        this.emotion_map_type = emotion_map_type;
    }
}
