package com.kikis.commnlibrary.utils;

import static com.kikis.commnlibrary.utils.BadgerManger.resetBadger;

/**
 * 未读消息统计util
 * kikis
 */
public class MessageCountManager {

    private static MessageCountManager instance;

    private static final String TAG = MessageCountManager.class.toString();

    public static int count = 0;

    public static MessageCountManager getInstance() {
        if (instance == null) {
            synchronized (MessageCountManager.class) {
                if (instance == null) {
                    instance = new MessageCountManager();
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
