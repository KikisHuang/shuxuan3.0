package com.gxdingo.sg.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.kikis.commnlibrary.utils.BaseLogUtils;
import com.kikis.commnlibrary.utils.Constant;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by Kikis on 2021/4/12.
 * 微信支付回调Activity
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {


    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constant.WECHAT_APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case 0:
                try {
//                    EventBus.getDefault().post(Constant.WECHAT_PAYMENT_SUCCESS);
                    EventBus.getDefault().post(Constant.PAYMENT_SUCCESS);
//                    ToastUtils.showShort("支付成功");
                    BaseLogUtils.i("支付成功");
                } catch (Exception e) {
                    BaseLogUtils.i("Error ==== " + e);
                }
                break;
            case -1:
//                EventBus.getDefault().post(Constant.WECHAT_PAYMENT_FAILED);
                EventBus.getDefault().post(Constant.PAYMENT_FAILED);
//              ToastUtils.showShort("-1支付失败");
                BaseLogUtils.i("支付失败");
                break;
            case -2:
                //USER_CANCEL
                BaseLogUtils.i("用户取消了微信支付 ");
                break;
        }
        finish();
    }

    @Override
    public void onReq(BaseReq req) {
        EventBus.getDefault().post(Constant.PAYMENT_FAILED);
        BaseLogUtils.i(" onReq 支付失败");
    }

}