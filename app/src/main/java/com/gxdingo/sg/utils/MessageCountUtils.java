package com.gxdingo.sg.utils;

/**
 * 未读消息统计util
 * kikis
 */
public class MessageCountUtils {

    private static MessageCountUtils instance;

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
        count = num;
    }

    /**
     * 减少未读消息数
     *
     * @param num
     */
    public int reduceUnreadMessageNum(int num) {
        return count -= num;
    }


    /**
     * 增加未读消息数
     */
    public int addNewMessage() {
       return count++;
    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    public int getUnreadMessageNum() {
        return count;
    }

}
