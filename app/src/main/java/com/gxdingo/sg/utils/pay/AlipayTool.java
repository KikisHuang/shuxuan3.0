
package com.gxdingo.sg.utils.pay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.OpenAuthTask;
import com.alipay.sdk.app.PayTask;
import com.gxdingo.sg.bean.AuthResult;

import java.util.HashMap;
import java.util.Map;

import static com.gxdingo.sg.utils.LocalConstant.SDK_AUTH_FLAG;
import static com.gxdingo.sg.utils.LocalConstant.SDK_PAY_FLAG;

/**
 * Created by yy on 2015/7/20.
 */

public class AlipayTool {


    /**
     * 支付宝支付
     *
     * @param mActivity
     * @param info
     * @param mHandler
     */
    public static void pay(Activity mActivity, final String info, Handler mHandler) {

        Runnable  runnable =  new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mActivity);
                // 调用支付接口，获取支付结果
                Map<String, String> result = alipay.payV2(info, true);
                PayResult payResult = new PayResult(result);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = payResult;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread authThread = new Thread(runnable);
        authThread.start();

        Log.e("payInfo=======", info);

    }

    /**
     * 支付宝授权
     *
     * @param activity
     * @param authInfo
     * @param mHandler
     */
    public static void auth(Activity activity, String authInfo, Handler mHandler) {

        Runnable runnable = () -> {
            // 构造AuthTask 对象
            AuthTask authTask = new AuthTask(activity);
            // 调用授权接口，获取授权结果
            Map<String, String> result = authTask.authV2(authInfo, true);
            AuthResult authResult = new AuthResult(result, true);

            Message msg = new Message();
            msg.what = SDK_AUTH_FLAG;
            msg.obj = authResult;
            mHandler.sendMessage(msg);
        };
        // 必须异步调用
        Thread authThread = new Thread(runnable);
        authThread.start();

    }
    /**
     * 支付宝授权极简版
     *
     * @param activity
     * @param authInfo
     * @param callback
     */
    public static void simpleAuth(Activity activity, String authInfo, OpenAuthTask.Callback callback) {

        // 商家对接业务所约定的参数
        Map<String, String> bizParams = new HashMap<>();
        bizParams.put("url", authInfo);   // key为url，value是上面的url值

        // 授权完成后回跳的scheme
        // 需与@{link com.alipay.sdk.app.AlipayResultActivity}的配置保持一致
        String scheme = "sg_alipay_result";

        OpenAuthTask task = new OpenAuthTask(activity);

        // 支付宝sdk目前已支持的钱包内业务
        OpenAuthTask.BizType bizType = OpenAuthTask.BizType.AccountAuth;
        task.execute(scheme, bizType, bizParams, callback, true);

    }
}
