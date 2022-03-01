package com.gxdingo.sg.bean;

/**
 * @author: Kikis
 * @date: 2021/5/10
 * @page:
 */
public class DialogParam {
    public String file_path;
    public NlsConfig nls_config;

    public DialogParam(String file_path, NlsConfig nls_config) {
        this.file_path = file_path;
        this.nls_config = nls_config;
    }
}
