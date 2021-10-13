package com.kikis.commnlibrary.biz;

import android.view.View;

/**
 * Created by Kikis on 2018/3/6.
 */

public interface BasicsListener {

    void onSucceed(int type);

    void onFailed();

    void onMessage(String msg);

    /**
     * 开始执行方法回调;
     */
    void onStarts();

    /**
     * 开始执行并禁止view点击方法回调;
     */
    void onStartsBanClick(View view);

    /**
     * 结束方法回调;
     */
    void onAfters();

    /**
     * 结束方法回调 恢复view点击
     *
     * @param view
     */
    void onAftersResumeClick(View view);

    /**
     * 无数据
     */
    void noData();

    /**
     * 有数据
     */
    void haveData();

    /**
     * 完成加载并标记没有更多数据
     */
    void finishLoadmoreWithNoMoreData();

    /**
     * 完成刷新并标记没有更多数据
     */
    void finishRefreshWithNoMoreData();

    /**
     * 完成请求
     */
    void onRequestComplete();

    /**
     * 重置没有更多
     */
    void resetNoMoreData();

    void finishRefresh(boolean success);

    void finishLoadmore(boolean success);
}
