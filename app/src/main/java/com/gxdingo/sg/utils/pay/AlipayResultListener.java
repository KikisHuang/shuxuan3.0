package com.gxdingo.sg.utils.pay;

/**
 * Created by Kikis on 2020/4/2
 */

public interface AlipayResultListener {
    void AliPayResult(int result);

    void AliPayfailure(int resultStatus);

}
