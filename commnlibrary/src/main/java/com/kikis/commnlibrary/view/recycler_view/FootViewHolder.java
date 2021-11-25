package com.kikis.commnlibrary.view.recycler_view;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kikis.commnlibrary.R;


/**
 * 自定义的尾ViewHolder
 * Created by JM on 2019/11/29.
 */

class FootViewHolder extends RecyclerView.ViewHolder {
    private View mFootView;
    private LinearLayout mFooterContent;
    private RelativeLayout mLoadMoreLayout;//加载更多布局
    private LinearLayout mFooterLayout;//页脚布局（可添加视图进去）
    private LinearLayout mLoadMoreProgressBarLayout;
    private ProgressBar mProgressbar;
    private TextView mHintTextview;
    private ConfigData mConfigData;

    public FootViewHolder(View view, ConfigData configData) {
        super(view);
        mFootView = view;
        mConfigData = configData;

        mFootView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        mFooterContent = mFootView.findViewById(R.id.footer_content);
        mFooterLayout = mFootView.findViewById(R.id.footer_layout);
        mLoadMoreLayout = mFootView.findViewById(R.id.load_more_layout);

        mLoadMoreProgressBarLayout = mFootView.findViewById(R.id.load_more_progress_bar_layout);
        mProgressbar = mFootView.findViewById(R.id.progressbar);
        mHintTextview = mFootView.findViewById(R.id.hint_textview);

        if (mConfigData.getFooterView() != null) {
            addFootView(mConfigData.getFooterView(), mConfigData.getFooterParams());
        }

        setPullLoadEnable(mConfigData.getEnablePullLoad());
    }

    /**
     * 根据方向改变UI
     *
     * @param orientation
     */
    @SuppressLint("WrongConstant")
    public void changeUIBasedOnOrientation(int orientation) {
        mLoadMoreLayout.removeAllViews();
        if (orientation == RecyclerView.HORIZONTAL) {
            mLoadMoreLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
            mFooterLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
            mFooterContent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
            mFooterContent.setOrientation(RecyclerView.HORIZONTAL);

            View footerVertical = LayoutInflater.from(mConfigData.getContext()).inflate(R.layout.pull_recycler_view_footer_horizontal, null);

            mLoadMoreProgressBarLayout = footerVertical.findViewById(R.id.load_more_progress_bar_layout);
            mProgressbar = footerVertical.findViewById(R.id.progressbar);
            mHintTextview = footerVertical.findViewById(R.id.hint_textview);

            mLoadMoreLayout.addView(footerVertical, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        } else {
            mLoadMoreLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            mFooterLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            mFooterContent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            mFooterContent.setOrientation(RecyclerView.VERTICAL);

            View footerVertical = LayoutInflater.from(mConfigData.getContext()).inflate(R.layout.pull_recycler_view_footer_vertical, null);

            mLoadMoreProgressBarLayout = footerVertical.findViewById(R.id.load_more_progress_bar_layout);
            mProgressbar = footerVertical.findViewById(R.id.progressbar);
            mHintTextview = footerVertical.findViewById(R.id.hint_textview);

            mLoadMoreLayout.addView(footerVertical, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    public void addFootView(View footerView, RecyclerView.LayoutParams params) {
        mFooterLayout.removeAllViews();
        if (params != null) {
            mFooterLayout.addView(footerView, params);
        } else {
            mFooterLayout.addView(footerView);
        }
    }

    public void setPullLoadEnable(boolean enable) {
        mLoadMoreLayout.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    public void removeFooterLayout() {
        mFooterLayout.removeAllViews();
    }

    public RelativeLayout getLoadMoreLayout() {
        return mLoadMoreLayout;
    }

    public LinearLayout getFooterLayout() {
        return mFooterLayout;
    }

    public LinearLayout getLoadMoreProgressBarLayout() {
        return mLoadMoreProgressBarLayout;
    }

    public ProgressBar getProgressBar() {
        return mProgressbar;
    }

    public TextView getHintTextview() {
        return mHintTextview;
    }

    public View getFootView() {
        return mFootView;
    }
}
