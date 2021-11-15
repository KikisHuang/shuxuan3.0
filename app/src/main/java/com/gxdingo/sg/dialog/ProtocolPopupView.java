package com.gxdingo.sg.dialog;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.gxdingo.sg.MyApplication;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.WebActivity;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.view.PartTextClickSpan;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.lxj.xpopup.core.CenterPopupView;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.model.HttpHeaders;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.blankj.utilcode.util.AppUtils.getAppName;
import static com.blankj.utilcode.util.DeviceUtils.getUniqueDeviceId;
import static com.gxdingo.sg.http.ClientApi.CLIENT_PRIVACY_AGREEMENT_KEY;
import static com.gxdingo.sg.http.ClientApi.CLIENT_SERVICE_AGREEMENT_KEY;
import static com.gxdingo.sg.utils.ClientLocalConstant.APP;
import static com.gxdingo.sg.utils.ClientLocalConstant.DEVICE;
import static com.gxdingo.sg.utils.ClientLocalConstant.YI_TARGET;
import static com.gxdingo.sg.utils.ClientLocalConstant.YI_VERSION;
import static com.gxdingo.sg.utils.ClientLocalConstant.YI_VERSION_NUMBER;
import static com.gxdingo.sg.utils.LocalConstant.FIRST_LOGIN_KEY;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * @author: Weaving
 * @date: 2021/8/6
 * @page:
 */
public class ProtocolPopupView extends CenterPopupView implements View.OnClickListener {

    private TextView protocol_tv;

    private TextView cancel_tv;

    private TextView consent_tv;

    private CustomResultListener listener;

    public ProtocolPopupView(@NonNull Context context, CustomResultListener listener) {
        super(context);
        addInnerContent();
        this.listener = listener;
    }

    @Override
    protected void initPopupContent() {
        protocol_tv = findViewById(R.id.protocol_tv);
        cancel_tv = findViewById(R.id.cancel_tv);
        consent_tv = findViewById(R.id.consent_tv);
        cancel_tv.setOnClickListener(this);
        consent_tv.setOnClickListener(this);
        setTextHighLightWithClick(protocol_tv.getText().toString(), new String[]{"《服务协议》", "《隐私政策》"});
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_protocol;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_tv:
                listener.onResult(0);
                this.dismiss();
                break;
            case R.id.consent_tv:
                SPUtils.getInstance().put(FIRST_LOGIN_KEY, false);
                listener.onResult(1);
                this.dismiss();
                break;
        }
    }


    /**
     * 设置文字高亮及点击事件
     *
     * @param text
     * @param keyWord
     */
    private void setTextHighLightWithClick(String text, String[] keyWord) {

        protocol_tv.setClickable(true);

        protocol_tv.setHighlightColor(Color.TRANSPARENT);

        protocol_tv.setMovementMethod(LinkMovementMethod.getInstance());


        SpannableString s = new SpannableString(text);

        for (int i = 0; i < keyWord.length; i++) {
            Pattern p = Pattern.compile(keyWord[i]);
            Matcher m = p.matcher(s);

            while (m.find()) {

                int start = m.start();

                int end = m.end();

                int finalI = i;

                s.setSpan(new PartTextClickSpan(getc(R.color.deepskyblue), false, new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (finalI == 0)
                            goToPagePutSerializable(getContext(), WebActivity.class, getIntentEntityMap(new Object[]{true, 0, CLIENT_SERVICE_AGREEMENT_KEY}));
                        else
                            goToPagePutSerializable(getContext(), WebActivity.class, getIntentEntityMap(new Object[]{true, 0, (CLIENT_PRIVACY_AGREEMENT_KEY)}));
                    }
                }), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        protocol_tv.setText(s);

    }

    /**
     * 初始化云推送通道
     *
     * @param applicationContext
     */
    private void initCloudChannel(MyApplication applicationContext) {

        try {

            createNotificationChannel();

            PushServiceFactory.init(applicationContext);
            CloudPushService pushService = PushServiceFactory.getCloudPushService();

            if (pushService == null)
                return;

            pushService.register(applicationContext, new CommonCallback() {
                @Override
                public void onSuccess(String response) {
//                    String deviceId = PushServiceFactory.getCloudPushService().getDeviceId();
//                    LogUtils.w("init cloudchannel success deviceId ==== " + deviceId);

                }

                @Override
                public void onFailed(String errorCode, String errorMessage) {
                    LogUtils.w("init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
                }
            });

            auxiliaryChannelInit();

        } catch (Exception e) {
            LogUtils.e("initCloudChannel error  == " + e);
        }
    }

    /**
     * 8.0以上弹窗
     */
    private void createNotificationChannel() {

        //8.0及其以上的设配设置NotificaitonChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) MyApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
            // 通知渠道的id
            String id = "1";//这个是与后台约定好的，要不收不到，该方法主要是适配Android 8.0以上，避免接收不到通知
            // 用户可以看到的通知渠道的名字.
            CharSequence name = getAppName();
            // 用户可以看到的通知渠道的描述
            String description = "通知";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // 配置通知渠道的属性
            mChannel.setDescription(description);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            mChannel.enableLights(false);
            mChannel.setLightColor(Color.RED);

            //取消震动
            mChannel.enableVibration(false);
            mChannel.setVibrationPattern(new long[]{0});
            // 设置通知出现时的震动（如果 android 设备支持的话）
//            mChannel.enableVibration(true);
//            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            //最后在notificationmanager中创建该通知渠道
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }


    /**
     * 辅助通道初始化
     */
    private void auxiliaryChannelInit() {

    /*    //小米辅助推送通道注册（如不支持会跳过）
        MiPushRegister.register(this, MI_APPID, MI_APP_KEY);
        //华为辅助推送通道注册（如不支持会跳过）
        HuaWeiRegister.register(this);
        // OPPO辅助通道注册
        OppoRegister.register(this, OPPO_APPKEY, OPPO_MASTERSECRET); // appKey/appSecret在OPPO开发者平台获取*/

    }


}
