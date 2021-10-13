package com.kikis.commnlibrary.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kikis.commnlibrary.R;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;

import androidx.annotation.NonNull;

import static com.kikis.commnlibrary.utils.CommonUtils.gets;

/**
 * Created by solo on 2018/2/1.
 * SmartRefreshLayout的自定义Footer
 */

public class CustomRefreshFooter extends LinearLayout implements RefreshFooter {

    private TextView foot_tv;

    public CustomRefreshFooter(Context context, String str) {
        super(context);
        View view = View.inflate(context, R.layout.module_view_widget_custom_refresh_footer, this);
        foot_tv = (TextView) findViewById(R.id.tv_refresh_footer);
        if (str != null && !str.isEmpty())
            foot_tv.setText(str);
        else
            foot_tv.setText(gets(R.string.loading));
    }

    public CustomRefreshFooter(Context context) {
        super(context);
        View view = View.inflate(context, R.layout.module_view_widget_custom_refresh_footer, this);
        foot_tv = (TextView) findViewById(R.id.tv_refresh_footer);
    }


    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }


    @Override
    public void onReleased(RefreshLayout refreshLayout, int height, int extendHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int height, int extendHeight) {

    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        return 0;
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {

    }

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        return true;
    }
}
