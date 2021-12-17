package com.gxdingo.sg.receiver;

import android.content.Context;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.gxdingo.sg.activity.ChatActivity;
import com.gxdingo.sg.bean.PushBean;
import com.gxdingo.sg.utils.BadgeUtil;
import com.gxdingo.sg.utils.LocalConstant;
import com.kikis.commnlibrary.utils.BaseLogUtils;
import com.kikis.commnlibrary.utils.GsonUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import static com.blankj.utilcode.util.ScreenUtils.isScreenLock;
import static com.blankj.utilcode.util.StringUtils.isEmpty;
import static com.gxdingo.sg.utils.ImServiceUtils.startImService;
import static com.gxdingo.sg.utils.LocalConstant.LOGIN_WAY;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;
import static com.kikis.commnlibrary.utils.KikisUitls.getContext;


/**
 * Created by Kikis on 2020/12/16.
 * 推送receiver
 */
public class AliPushMessageReceiver extends MessageReceiver {

    public static final String TAG = "Shuxiang_User_Push";


    private int count = 0;
    /**
     * 当通知准确到达用户的时候触发
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {

        BaseLogUtils.d(TAG, "Receive notification, title: " + title + ", summary: " + summary + ", extraMap: " + extraMap);

        String orderId = extraMap.get("orderId");

/*
        if (!isEmpty(orderId))
            EventBus.getDefault().post(new OrderRefreshBean(Long.valueOf(orderId)));
*/

    /*    //非锁屏状态
        if (!isScreenLock()) {
            //应用不是后台状态，不显示通知栏
            if (!LocalConstant.isBackground)
                PushServiceFactory.getCloudPushService().clearNotifications();
        }*/
        count ++ ;
        BadgeUtil.setBadge(count,context);

    }


    /**
     * 当消息准确到达用户的时候触发，只有用户在使用的时候才能接收到消息
     *
     * @param context
     * @param cPushMessage
     */
    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
        BaseLogUtils.d(TAG, "onMessage, messageId: " + cPushMessage.getMessageId() + ", title: " + cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());
    }

    /**
     * 当通知打开的时候触发的操作
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        BaseLogUtils.d(TAG, "onNotificationOpened, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
        try {

            PushBean pushBean = GsonUtil.GsonToBean(extraMap, PushBean.class);

            if (pushBean.getType() == 0)
                goToPagePutSerializable(context, ChatActivity.class, getIntentEntityMap(new Object[]{pushBean.getShareUuid(), pushBean.getRole()}));


        } catch (Exception e) {
            BaseLogUtils.e("ali push paser error == " + e);
        }
    }

    /**
     * 当通知被点击了触发的操作，并且没有配置跳转路径
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        BaseLogUtils.d(TAG, "onNotificationClickedWithNoAction, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);


    }

    /**
     * 当用户使用时接收到通知
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     * @param openType
     * @param openActivity
     * @param openUrl
     */
    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        BaseLogUtils.d(TAG, "onNotificationReceivedInApp, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap + ", openType:" + openType + ", openActivity:" + openActivity + ", openUrl:" + openUrl);
    }

    /**
     * 通知被移除
     *
     * @param context
     * @param messageId
     */
    @Override
    protected void onNotificationRemoved(Context context, String messageId) {
        BaseLogUtils.d(TAG, "onNotificationRemoved");

    }
}