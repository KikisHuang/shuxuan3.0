package com.gxdingo.sg.utils;

import com.blankj.utilcode.util.LogUtils;
import com.kikis.commnlibrary.utils.BaseLogUtils;

/**
 * 未读消息统计util
 * kikis
 */
public class MessageCountUtils {

    private static MessageCountUtils instance;
    private static final String TAG = MessageCountUtils.class.toString();


    private int count = 0;

    public static MessageCountUtils getInstance() {
        if (instance == null) {
            synchronized (MessageCountUtils.class) {
                if (instance == null) {
                    instance = new MessageCountUtils();
                }
            }
        }
        return instance;
    }


    /**
     * 设置未读消息数
     *
     * @param num
     */
    public void setUnreadMessageNum(int num) {
        BaseLogUtils.i(TAG, "setUnreadMessageNum === " + count);
        count = num;
    }

    /**
     * 减少未读消息数
     *
     * @param num
     */
    public int reduceUnreadMessageNum(int num) {
        BaseLogUtils.i(TAG, "reduceUnreadMessageNum === " + count);
        return count -= num;
    }


    /**
     * 增加未读消息数
     */
    public int addNewMessage() {
        BaseLogUtils.i(TAG, "addNewMessage === " + count);
        return count++;
    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    public int getUnreadMessageNum() {
        BaseLogUtils.i(TAG, "getUnreadMessageNum === " + count);
        return count;
    }

}
