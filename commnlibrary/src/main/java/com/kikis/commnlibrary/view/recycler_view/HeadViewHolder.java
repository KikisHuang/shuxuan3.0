package com.kikis.commnlibrary.view.recycler_view;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.kikis.commnlibrary.R;


/**
 * 自定义的头ViewHolder
 * Created by JM on 2019/11/29.
 */
class HeadViewHolder extends RecyclerView.ViewHolder {
    private Context mContext;
    private View mHeadView;
    private PullRecylerViewPullDownLayout mPullDownRefreshLayout;//下拉刷新布局
    private RelativeLayout mHeadLayout;//头部布局（可添加视图进去）
    private View headLine;
    private ConfigData mConfigData;

    public HeadViewHolder(Context context, View view, ConfigData configData) {
        super(view);
        mContext = context;
        mHeadView = view;
        mConfigData = configData;

        mHeadView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        mPullDownRefreshLayout = mHeadView.findViewById(R.id.pull_down_refresh_layout);
        mHeadLayout = mHeadView.findViewById(R.id.head_layout);
        headLine = mHeadView.findViewById(R.id.head_line);

        final int mMaxPullDownTop = (int) mContext.getResources().getDimension(R.dimen.max_pull_down_top);//下拉触发刷新的最大下拉值
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, mMaxPullDownTop);
        params.setMargins(0, -mMaxPullDownTop, 0, 0);
        mPullDownRefreshLayout.setLayoutParams(params);

        if (mConfigData.getHeadView() != null) {
            addHeadView(mConfigData.getHeadView(), mConfigData.getHeadParams());
        }
        setPullRefreshEnable(mConfigData.getEnablePullRefresh());
    }

    public void addHeadView(View headView, RecyclerView.LayoutParams params) {
        mHeadLayout.removeAllViews();
        if (params != null) {
            mHeadLayout.addView(headView, params);
        } else {
            mHeadLayout.addView(headView);
        }
    }

    public void setPullRefreshEnable(boolean enable) {
        mPullDownRefreshLayout.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
    }

    public void removeHeadViewLayout() {
        mHeadLayout.removeAllViews();
    }

    public PullRecylerViewPullDownLayout getPullDownRefreshLayout() {
        return mPullDownRefreshLayout;
    }

    public RelativeLayout getHeadLayout() {
        return mHeadLayout;
    }

    public void setHeadLineVisibility(boolean visibility) {
        headLine.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    public View getHeadView(){
        return mHeadView;
    }
}
