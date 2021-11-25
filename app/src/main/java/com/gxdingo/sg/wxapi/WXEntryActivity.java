package com.gxdingo.sg.wxapi;


import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.gxdingo.sg.bean.WeChatLoginEvent;
import com.gxdingo.sg.utils.LocalConstant;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by Kikis on 2021/4/12.
 */

public class WXEntryActivity extends WXCallbackActivity {

/*    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Constant.WECHAT_APPID, false);
        // 将该app注册到微信
        api.registerApp(Constant.WECHAT_APPID);

        api.handleIntent(getIntent(), this);
        super.onCreate(savedInstanceState);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
    }

    @Override
    protected void onNewIntent(Intent paramIntent) {
        super.onNewIntent(paramIntent);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        super.onReq(req);
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                LogUtils.i("onReq  ---  COMMAND_GETMESSAGE_FROM_WX");
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                // 小程序 跳转app
                openApp((ShowMessageFromWX.Req) req);
                break;
            default:
                break;
        }
    }

    private void openApp(ShowMessageFromWX.Req req) {

        WXMediaMessage wxMsg = req.message;
        WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;

        LogUtils.i("obj.extInfo === " + obj.extInfo);


    }


    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp baseResp) {
        super.onResp(baseResp);
        switch (baseResp.errCode) {
            case 0:
                String code = ((SendAuth.Resp) baseResp).code;
                EventBus.getDefault().post(new WeChatLoginEvent(code, LocalConstant.isLogin));
                finish();
                break;

        }
    }


}
