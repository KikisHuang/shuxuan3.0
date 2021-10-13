package com.gxdingo.sg.utils.pay;

/**
 * @author: Kikis
 * @date: 2021/1/4
 * @page:
 */
public interface AliPayAuthResultListener {

    void onAuthSucceed(String authCode);

    void onAuthFailed();

}
