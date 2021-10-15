package com.gxdingo.sg.biz;

/**
 * @author: Kikis
 * @date: 2021/4/20
 * @page:
 */
public interface PermissionsListener {

    void onNext(boolean value);

    void onError(Throwable e);

    void onComplete();
}
