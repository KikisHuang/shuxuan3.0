package com.gxdingo.sg.utils;

import android.content.Context;

import com.kikis.commnlibrary.utils.BaseLogUtils;

/**
 * im操作类
 */
public class ImServiceUtils {

    /**
     * 启动im服务
     *
     */
    public static void startImService() {
        if (!ImMessageUtils.getInstance().isRunning())
            new Thread(() -> ImMessageUtils.getInstance().start()).start();
         else
            BaseLogUtils.i("Im 服务运行中，无需启动");
    }

    /**
     * 停止im服务
     */
    public static void stopImService() {
        if (ImMessageUtils.getInstance().isRunning())
            ImMessageUtils.getInstance().stop();
    }

    /**
     * 重置im服务
     */
    public static void resetImService() {
        if (ImMessageUtils.getInstance().isRunning())
            new Thread(() -> ImMessageUtils.getInstance().reSet()).start();
    }
}
