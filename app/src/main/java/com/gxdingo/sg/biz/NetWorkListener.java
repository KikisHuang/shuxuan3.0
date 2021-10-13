package com.gxdingo.sg.biz;

import com.zhouyou.http.subsciber.BaseSubscriber;

/**
 * Created by Kikis on 2021/3/16.
 */

public interface NetWorkListener<T> {


    void onSucceed(int type);


    void onMessage(String msg);

    /**
     * 无数据
     */
    void noData();

    /**
     * 数据回调
     *
     * @param t
     */
    void onData(boolean refresh, T t);

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


    void onAfters();

    void onStarts();

    void onDisposable(BaseSubscriber<T> subscriber);
}
