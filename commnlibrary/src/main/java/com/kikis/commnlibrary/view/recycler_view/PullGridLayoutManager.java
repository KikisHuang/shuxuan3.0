package com.kikis.commnlibrary.view.recycler_view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * 自定义网格布局管理器
 * Created by JM on 2019/5/25.
 */

public class PullGridLayoutManager extends GridLayoutManager {
    private boolean isScrollEnabled = true;

    public PullGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public PullGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public PullGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setScrollEnabled(boolean flag) {
        isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }

    @Override
    public boolean canScrollHorizontally() {
        return isScrollEnabled && super.canScrollHorizontally();
    }

    @Override
    public void setSpanSizeLookup(final GridLayoutManager.SpanSizeLookup spanSizeLookup) {
        //设置头（下拉）尾（下拉）布局列数
        super.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                //垂直布局才会有下拉刷新和加载更多操作，则第一个item表示下拉刷新，最后一个item是加载更多。他们占满整列。
                if (getOrientation() == VERTICAL) {
                    //第一个item（下拉布局）和最后一个item（下拉加载更多）列跨度占满一行（例如spanCount是2，则占2个位置）
                    if (i == 0 || (i + 1) == getItemCount()) {
                        return getSpanCount();
                    }

                    //其它item占多少个位置根据自定义getSpanSize()方法返回数来定
                    return spanSizeLookup.getSpanSize(i - 1);
                } else if (getOrientation() == HORIZONTAL) {
                    //最后一个item（下拉加载更多）列跨度占满一行（例如spanCount是2，则占2个位置）
                    if ((i + 1) == getItemCount()) {
                        return getSpanCount();
                    }

                    //其它item占多少个位置根据自定义getSpanSize()方法返回数来定
                    return spanSizeLookup.getSpanSize(i);
                }
                return spanSizeLookup.getSpanSize(i);
            }
        });
    }
}
