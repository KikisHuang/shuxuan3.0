package com.gxdingo.sg.utils.pay;

import com.gxdingo.sg.bean.WxpayInfo;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;


/**
 * Created by yy on 2018/5/9.
 */
public class WeChatTool {

    private PayReq req;

    final IWXAPI msgApi;


    public WeChatTool() {
        req = new PayReq();
        msgApi = WechatUtils.getInstance().getWxApi();
    }

    public void sendPayReq(WxpayInfo data) {

        req.appId = data.getAppid();
        req.partnerId = data.getPartnerid();
        req.prepayId = data.getPrepayid();
        req.packageValue = data.getPackageX();
        req.nonceStr = data.getNoncestr();
        req.timeStamp = data.getTimeStamp();
        req.sign = data.getSign();

        msgApi.registerApp(req.appId);
        msgApi.sendReq(req);

    }
}
