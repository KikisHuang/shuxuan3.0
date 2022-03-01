package com.gxdingo.sg.bean;

/**
 * @author: Kikis
 * @date: 2021/5/10
 * @page:
 */
public class ParamsBean {

    public String app_key;
    public String token;
    public String url;
    public String device_id;
    public String workspace;
    public String debug_path;

    public ParamsBean(String app_key, String token, String url, String device_id, String workspace, String debug_path) {
        this.app_key = app_key;
        this.token = token;
        this.url = url;
        this.device_id = device_id;
        this.workspace = workspace;
        this.debug_path = debug_path;
    }
}
