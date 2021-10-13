package com.kikis.commnlibrary.biz;

/**
 * Created by Kikis on 2019/3/6.
 * // 返回给调用者屏幕状态信息
 */

public interface ScreenStateListener {
    // 开屏
    void onScreenOn();
    //锁屏
    void onScreenOff();
    //解锁
    void onUserPresent();
}
