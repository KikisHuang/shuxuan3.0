package com.kikis.commnlibrary.view.recycler_view;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 配置参数
 * Created by Pang JM on 2019/11/21.
 */
class ConfigData {
    private Context context;
    private boolean enablePullRefresh;//开启下拉刷新
    private boolean enablePullLoad;//开启加载更多
    private RecyclerView.LayoutParams headParams;
    private RecyclerView.LayoutParams footerParams;
    private View headView;
    private View footerView;


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Boolean getEnablePullRefresh() {
        return enablePullRefresh;
    }

    public void setEnablePullRefresh(Boolean enablePullRefresh) {
        this.enablePullRefresh = enablePullRefresh;
    }

    public Boolean getEnablePullLoad() {
        return enablePullLoad;
    }

    public void setEnablePullLoad(Boolean enablePullLoad) {
        this.enablePullLoad = enablePullLoad;
    }

    public RecyclerView.LayoutParams getHeadParams() {
        return headParams;
    }

    public void setHeadParams(RecyclerView.LayoutParams headParams) {
        this.headParams = headParams;
    }


    public RecyclerView.LayoutParams getFooterParams() {
        return footerParams;
    }

    public void setFooterParams(RecyclerView.LayoutParams footerParams) {
        this.footerParams = footerParams;
    }

    public View getHeadView() {
        return headView;
    }

    public void setHeadView(View headView) {
        this.headView = headView;
    }

    public View getFooterView() {
        return footerView;
    }

    public void setFooterView(View footerView) {
        this.footerView = footerView;
    }


}
