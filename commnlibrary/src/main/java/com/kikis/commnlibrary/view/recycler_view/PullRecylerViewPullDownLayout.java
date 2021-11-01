package com.kikis.commnlibrary.view.recycler_view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kikis.commnlibrary.R;


/**
 * 下拉布局
 * Created by JM on 2019/6/17.
 */

public class PullRecylerViewPullDownLayout extends RelativeLayout {
    private Context mContext;
    private Activity activity;
    private View viewPullDown;
    private ProgressBar pullDownProgress;
    private TextView pullDownContent;
    private boolean isShowLoadingStatic = false;//是否显示静态不动的加载进度图片


    public PullRecylerViewPullDownLayout(Context context) {
        super(context);
        init(context);
    }

    public PullRecylerViewPullDownLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullRecylerViewPullDownLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PullRecylerViewPullDownLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        activity = (Activity) mContext;

        viewPullDown = LayoutInflater.from(mContext).inflate(R.layout.pull_recycler_view_head_pull_down_layout, null);
        pullDownProgress = viewPullDown.findViewById(R.id.pull_down_progress);
        pullDownContent = viewPullDown.findViewById(R.id.pull_down_content);
        addView(viewPullDown, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    /**
     * 设置下拉视图位置
     */
    public void setRelativeViewLocation(int left, int top, int right, int bottom) {
        LayoutParams params = new LayoutParams(right - left, bottom - top);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params.setMargins(left, top, 0, 0);
        setLayoutParams(params);
    }

    /**
     * 改变下拉视图状态
     *
     * @param percentage 百分比 （百分比值是0至最大下拉值范围）
     */
    public void changePullDownViewStatus(int percentage) {
        if (!isShowLoadingStatic) {
            isShowLoadingStatic = true;
            setRotationDrawable(R.drawable.pull_recycler_view_loading_static);
        }

        if (percentage >= 0 && percentage < 50) {
            pullDownContent.setText("下拉刷新内容");
        } else if (percentage >= 50 && percentage < 100) {
            pullDownContent.setText("继续下拉");
        } else if (percentage >= 100) {
            pullDownContent.setText("松开刷新内容");
        } else if (percentage == -1) {
            isShowLoadingStatic = false;
            setRotationDrawable(R.drawable.pull_recycler_view_progress);
            pullDownContent.setText("正在加载中，请稍后");
        }
    }

    private void setRotationDrawable(int resId) {
        Drawable drawable = mContext.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, dpTopx(25), dpTopx(25));
        pullDownProgress.setIndeterminateDrawable(drawable);
    }

    private int dpTopx(int dp) {
        DisplayMetrics dm = mContext.getApplicationContext().getResources().getDisplayMetrics();
        return (int) (dp * dm.density + 0.5f);
    }
}
