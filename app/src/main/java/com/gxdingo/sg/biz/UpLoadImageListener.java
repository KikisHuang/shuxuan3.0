package com.gxdingo.sg.biz;

import com.gxdingo.sg.bean.UpLoadBean;

/**
 * @author: Kikis
 * @date: 2021/5/12
 * @page:
 */
public interface UpLoadImageListener {

    void loadSucceed(String path);

    void loadSucceed(UpLoadBean upLoadBean);

}
