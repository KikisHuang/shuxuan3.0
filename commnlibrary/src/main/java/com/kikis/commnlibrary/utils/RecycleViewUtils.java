package com.kikis.commnlibrary.utils;


import android.os.SystemClock;
import android.view.MotionEvent;

import com.blankj.utilcode.util.LogUtils;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Created by Kikis on 2018/7/26.
 */

public class RecycleViewUtils {

    /**
     * 判断是否显示到底部 -3的item数
     * *
     *
     * @param recyclerView
     * @return
     */
    public static boolean isVisBottom(RecyclerView recyclerView) {
        if (recyclerView==null)
            return false;

        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //屏幕中最后一个可见子项的position
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        //当前屏幕所看到的子项个数
        int visibleItemCount = layoutManager.getChildCount();
        //当前RecyclerView的所有子项个数
        int totalItemCount = layoutManager.getItemCount();
        //RecyclerView的滑动状态
        int state = recyclerView.getScrollState();
        LogUtils.i("lastVisibleItemPosition === " + lastVisibleItemPosition + " totalItemCount === " + totalItemCount);
        if (visibleItemCount > 0 && lastVisibleItemPosition >= totalItemCount - 4) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * RecyclerView 移动到当前位置，
     *
     * @param manager       设置RecyclerView对应的manager
     * @param mRecyclerView 当前的RecyclerView
     * @param n             要跳转的位置
     */
    public static void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {

        if (mRecyclerView==null)
            return;

        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }
    }

    /**
     * 滑动到指定位置
     */
    public static void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {

        if (mRecyclerView==null)
            return;

        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后
            mRecyclerView.smoothScrollToPosition(position);
        }
    }
    //强制停止RecyclerView滑动方法
    public static void forceStopRecyclerViewScroll(RecyclerView mRecyclerView) {
        mRecyclerView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0, 0, 0));
    }

    /**
     * 滑动到指定位置并在顶部
     */
    public static void MoveToPositionTop(RecyclerView mRecyclerView, final int position) {
        if (mRecyclerView==null)
            return;

        if (position != -1) {
            mRecyclerView.scrollToPosition(position);

            if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager mLayoutManager =
                        (LinearLayoutManager) mRecyclerView.getLayoutManager();
                mLayoutManager.scrollToPositionWithOffset(position, Integer.MIN_VALUE);
            }

            if (mRecyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager mLayoutManager =
                        (StaggeredGridLayoutManager) mRecyclerView.getLayoutManager();
                mLayoutManager.scrollToPositionWithOffset(position, Integer.MIN_VALUE);
            }

            if (mRecyclerView.getLayoutManager() instanceof GridLayoutManager) {
                GridLayoutManager mLayoutManager =
                        (GridLayoutManager) mRecyclerView.getLayoutManager();
                mLayoutManager.scrollToPositionWithOffset(position, Integer.MIN_VALUE);
            }

        }
    }


}
