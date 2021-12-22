package com.kikis.commnlibrary.utils;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;

import static com.kikis.commnlibrary.utils.Constant.isDebug;

public class BadgerManger {

    /**
     * 添加角标
     *
     * @param context
     */
    public static void addBadgerNum(Context context) {
        MessageCountManager.count++;

        if (isDebug)
            LogUtils.i("badgerNum === " + MessageCountManager.count);

        if (RomUtils.isHuawei()) {
            BadgerUtils.setHuaweiBadge(context, MessageCountManager.count);

        } else if (RomUtils.isVivo()) {
            BadgerUtils.setVivoBadge(context, MessageCountManager.count);

        } else if (RomUtils.isXiaomi() || RomUtils.isOppo()) {
            BadgerUtils.setNotificationBadge(context, MessageCountManager.count);

        } else if (RomUtils.isSamsung()) {
            BadgerUtils.setSamsungBadge(context, MessageCountManager.count);

        } else if (RomUtils.isLenovo()) {
            BadgerUtils.setZukBadge(context, MessageCountManager.count);

        } else if (RomUtils.isHtc()) {
            BadgerUtils.setHTCBadge(context, MessageCountManager.count);

        } else if (RomUtils.isSony()) {
            BadgerUtils.setSonyBadge(context, MessageCountManager.count);

        }
    }

    /**
     * 重设桌面角标
     *
     * @param context
     */
    public static void resetBadger(Context context) {

        if (isDebug)
            LogUtils.i("badgerNum === " + MessageCountManager.count);

        if (RomUtils.isHuawei()) {
            BadgerUtils.setHuaweiBadge(context, MessageCountManager.count);

        } else if (RomUtils.isVivo()) {
            BadgerUtils.setVivoBadge(context, MessageCountManager.count);

        } else if (RomUtils.isXiaomi() || RomUtils.isOppo()) {
            BadgerUtils.setNotificationBadge(context, MessageCountManager.count);

        } else if (RomUtils.isSamsung()) {
            BadgerUtils.setSamsungBadge(context, MessageCountManager.count);

        } else if (RomUtils.isLenovo()) {
            BadgerUtils.setZukBadge(context, MessageCountManager.count);

        } else if (RomUtils.isHtc()) {
            BadgerUtils.setHTCBadge(context, MessageCountManager.count);

        } else if (RomUtils.isSony()) {
            BadgerUtils.setSonyBadge(context, MessageCountManager.count);

        }
    }

    /**
     * 清除全部角标
     *
     * @param context
     */
    public static void badgerRemoveAll(Context context) {
        if (RomUtils.isHuawei()) {
            BadgerUtils.setHuaweiBadge(context, 0);

        } else if (RomUtils.isVivo()) {
            BadgerUtils.setVivoBadge(context, 0);

        } else if (RomUtils.isXiaomi() || RomUtils.isOppo()) {
            BadgerUtils.setNotificationBadge(context, 0);

        } else if (RomUtils.isSamsung()) {
            BadgerUtils.setSamsungBadge(context, 0);

        } else if (RomUtils.isLenovo()) {
            BadgerUtils.setZukBadge(context, 0);

        } else if (RomUtils.isHtc()) {
            BadgerUtils.setHTCBadge(context, 0);

        } else if (RomUtils.isSony()) {
            BadgerUtils.setSonyBadge(context, 0);

        }
    }
}
