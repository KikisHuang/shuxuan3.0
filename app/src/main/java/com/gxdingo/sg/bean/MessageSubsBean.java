package com.gxdingo.sg.bean;

import java.util.List;

/**
 * @author: Kikis
 * @date: 2021/5/19
 * @page:
 */
public class MessageSubsBean {

    private String websocketUrl;

    private List<SubscribesBean> list;

    public String getWebsocketUrl() {
        return websocketUrl;
    }

    public void setWebsocketUrl(String websocketUrl) {
        this.websocketUrl = websocketUrl;
    }

    public List<SubscribesBean> getList() {
        return list;
    }

    public void setList(List<SubscribesBean> list) {
        this.list = list;
    }
}
