package com.gxdingo.sg.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.LogUtils;
import com.gxdingo.sg.service.IMMessageReceivingService;

import java.util.List;

import static com.kikis.commnlibrary.utils.Constant.isDebug;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

public class ImServiceUtils {

    /**
     * 启动im服务
     *
     * @param context
     */
    public static void startImService(Context context) {

        if (!isServiceRunning(context, IMMessageReceivingService.class.getName()))
            context.startService(new Intent(context, IMMessageReceivingService.class));
        else {
            if (isDebug)
                LogUtils.i("Im 服务运行中，无需启动");
        }

    }

    /**
     * 判断服务是否运行
     *
     * @param context
     * @param className
     * @return
     */
    public static boolean isServiceRunning(Context context, String className) {
        if (isEmpty(className)) {
            return false;
        }
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);

        if (serviceList.isEmpty()) {
            return false;
        }

        for (ActivityManager.RunningServiceInfo item : serviceList) {
            if (item.service.getClassName().equals(className) ) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

}
