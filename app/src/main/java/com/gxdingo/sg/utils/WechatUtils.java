package com.gxdingo.sg.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.blankj.utilcode.util.ToastUtils;
import com.gxdingo.sg.R;
import com.kikis.commnlibrary.utils.KikisUitls;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import static android.graphics.Bitmap.CompressFormat.JPEG;
import static com.blankj.utilcode.util.ConvertUtils.bitmap2Bytes;
import static com.kikis.commnlibrary.utils.Constant.WECHAT_APPID;

/**
 * @author: Kikis
 * @date: 2020/12/10
 * @page:
 */

public class WechatUtils {

    public static final int WECHAT_SHARE_TYPE_TALK = SendMessageToWX.Req.WXSceneSession;  //会话

    public static final int WECHAT_SHARE_TYPE_FRENDS = SendMessageToWX.Req.WXSceneTimeline; //朋友圈

    // IWXAPI 是第三方app和微信通信的openApi接口
    private IWXAPI api;

    //0 一键登录页面微信登录  1 手动登录页面微信登录
    public static int weChatLoginType =0;

    private static WechatUtils wechatUtils;

    public WechatUtils() {
        regToWx();
    }

    public static WechatUtils getInstance() {
        if (wechatUtils == null) {
            synchronized (WechatUtils.class) {
                if (wechatUtils == null) {
                    wechatUtils = new WechatUtils();
                }
            }
        }
        return wechatUtils;
    }


    public void regToWx() {
        if (api == null) {
            // 通过WXAPIFactory工厂，获取IWXAPI的实例
            api = WXAPIFactory.createWXAPI(KikisUitls.getContext(), WECHAT_APPID);

            // 将应用的appId注册到微信
            api.registerApp(WECHAT_APPID);

       /*     //建议动态监听微信启动广播进行注册到微信
            KikisUitls.getContext().registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    // 将该app注册到微信
                    api.registerApp(WECHAT_APPID);
                }
            }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));*/
        }

    }

    public IWXAPI getWxApi() {
        regToWx();
        return api;
    }

    public IWXAPI getWxApi(int t) {
        weChatLoginType = t;
        regToWx();
        return api;
    }


    public void wxDetach() {
        if (api != null) {
            api.detach();
            api = null;
        }
    }


    /*
     * 分享链接
     */
    public void shareWebPage(String url, String title, String content, Bitmap bitmap, int shareType) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = content;

        Bitmap thumb;

        if (bitmap != null)
            thumb = bitmap;
        else
            thumb = BitmapFactory.decodeResource(KikisUitls.getContext().getResources(), R.drawable.default_bg);
        if (thumb == null) {
            ToastUtils.showShort("图片不能为空");
        } else {
            //Bitmap转换成byte[]
            msg.thumbData = bitmap2Bytes(thumb, JPEG, 50);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = shareType;
        getWxApi().sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

}
