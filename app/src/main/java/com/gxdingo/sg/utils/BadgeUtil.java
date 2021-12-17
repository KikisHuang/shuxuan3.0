package com.gxdingo.sg.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;

import com.blankj.utilcode.util.DeviceUtils;
import com.gxdingo.sg.activity.ClientActivity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author: Weaving
 * @date: 2021/12/17
 * @page:
 */
public class BadgeUtil {

    /**
     * 手机品牌
     */
    // 小米
    public static final String PHONE_XIAOMI = "xiaomi";
    // 华为
    public static final String PHONE_HUAWEI1 = "Huawei";
    // 华为
    public static final String PHONE_HUAWEI2 = "HONOR";
    // VIVO
    public static final String PHONE_VIVO = "vivo";
    // OPPO
    public static final String PHONE_OPPO = "OPPO";

    private static String getLauncherClassName(Context context) {
        ComponentName launchComponent = getLauncherComponentName(context);
        if (launchComponent == null) {
            return "";
        } else {
            return launchComponent.getClassName();
        }
    }

    private static ComponentName getLauncherComponentName(Context context) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context
                .getPackageName());
        if (launchIntent != null) {
            return launchIntent.getComponent();
        } else {
            return null;
        }
    }

    public static void  setBadge(int count,Context context){
        String brand = Build.BRAND;
        switch (brand){
            case PHONE_XIAOMI:
                setNotificationBadge(count,context);
                break;
            case PHONE_HUAWEI1:
            case PHONE_HUAWEI2:
                setHuaweiBadge(count,context);
                break;
            case PHONE_VIVO:
                setVivoBadge(count,context);
                break;
            case PHONE_OPPO:
                break;
        }
    }

    //小米
    private static boolean setNotificationBadge(int count, Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService
                (Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 8.0之后添加角标需要NotificationChannel
            NotificationChannel channel = new NotificationChannel("badge", "badge",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(true);
            notificationManager.createNotificationChannel(channel);
        }
        Intent intent = new Intent(context, ClientActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(context, "badge")
//                .setContentTitle("应用角标")
//                .setContentText("您有" + count + "条未读消息")
//                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap
//                        .ic_launcher))
//                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setChannelId("badge")
                .setNumber(count)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL).build();
        // 小米
        try {
            Field field = notification.getClass().getDeclaredField("extraNotification");
            Object extraNotification = field.get(notification);
            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int
                    .class);
            method.invoke(extraNotification, count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        notificationManager.notify(count++, notification);
        return true;
    }

    private static boolean setHuaweiBadge(int count, Context context) {
        try {
            String launchClassName = getLauncherClassName(context);
            if (TextUtils.isEmpty(launchClassName)) {
                return false;
            }
            Bundle bundle = new Bundle();
            bundle.putString("package", context.getPackageName());
            bundle.putString("class", launchClassName);
            bundle.putInt("badgenumber", count);
            context.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher" +
                    ".settings/badge/"), "change_badge", null, bundle);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private static boolean setVivoBadge(int count, Context context) {
        try {
            String launcherClassName = getLauncherClassName(context);
            if (TextUtils.isEmpty(launcherClassName)) {
                return false;
            }
            Intent intent = new Intent("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM");
            intent.putExtra("packageName", context.getPackageName());
            intent.putExtra("className", launcherClassName);
            intent.putExtra("notificationNum", count);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//                Intent.FLAG_rECEI;
//            }
            context.sendBroadcast(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
