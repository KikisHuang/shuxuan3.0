package com.gxdingo.sg.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.gxdingo.sg.biz.onSwipeGestureListener;

public class NoScrollViewPager extends ViewPager {
    private boolean isScroll = false;

    private onSwipeGestureListener onSwipeGestureListener;

    private float mDownX, mDownY, mTempX, totalMoveX;

    private float mTouchSlop = ScreenUtils.getAppScreenWidth() / 7;

    private boolean isSilding;

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public void setOnSwipeGestureListener(onSwipeGestureListener listener) {
        this.onSwipeGestureListener = listener;
    }

    /**
     * 1.dispatchTouchEvent一般情况不做处理
     * ,如果修改了默认的返回值,子孩子都无法收到事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);   // return true;不行
    }

    /**
     * 是否拦截
     * 拦截:会走到自己的onTouchEvent方法里面来
     * 不拦截:事件传递给子孩子
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        // return false;//可行,不拦截事件,
        // return true;//不行,孩子无法处理事件
        //return super.onInterceptTouchEvent(ev);//不行,会有细微移动
        if (isScroll) {
            return super.onInterceptTouchEvent(ev);
        } else {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDownX = mTempX = (int) ev.getRawX();
                    mDownY = (int) ev.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int moveX = (int) ev.getRawX();
                    // 满足此条件屏蔽SildingFinishLayout里面子类的touch事件
                    if (Math.abs(moveX - mDownX) > mTouchSlop
                            && Math.abs((int) ev.getRawY() - mDownY) < mTouchSlop) {
                        return true;
                    }
                    break;

            }
            return false;
        }
    }

    /**
     * 是否消费事件
     * 消费:事件就结束
     * 不消费:往父控件传
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {


        //return false;// 可行,不消费,传给父控件
        //return true;// 可行,消费,拦截事件
        //super.onTouchEvent(ev); //不行,
        //虽然onInterceptTouchEvent中拦截了,
        //但是如果viewpage里面子控件不是viewgroup,还是会调用这个方法.
        if (isScroll) {
            return super.onTouchEvent(event);
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    int moveX = (int) event.getRawX();
                    int deltaX = (int) (mTempX - moveX);
//			Log.i("debug", "deltaX:" + deltaX + "mTouchSlop:" + mTouchSlop);
                    mTempX = moveX;
                    if (Math.abs(moveX - mDownX) > mTouchSlop
                            && Math.abs((int) event.getRawY() - mDownY) < mTouchSlop) {
                        isSilding = true;
                    }

                    if (Math.abs(moveX - mDownX) >= 0 && isSilding) {
//				mContentView.scrollBy(deltaX, 0);
                        totalMoveX += deltaX;
                    }
                    break;
                case MotionEvent.ACTION_UP:
//			Log.i("debug", "TotoalMoveX:" + totalMoveX + "viewVidth:" + viewWidth);
                    if (Math.abs(totalMoveX) >= mTouchSlop) {
                        if (totalMoveX > 0) {
                            if (onSwipeGestureListener != null)
                                onSwipeGestureListener.onLeftSwipe();
                        } else {
                            onSwipeGestureListener.onRightSwipe();
                        }
                    }
                    totalMoveX = 0;
                    break;
            }
            return true;// 可行,消费,拦截事件
        }
    }

    public void setScroll(boolean scroll) {
        isScroll = scroll;
    }
}