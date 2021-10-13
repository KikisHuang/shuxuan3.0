package com.kikis.commnlibrary.view;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;

import android.util.AttributeSet;
import android.view.MotionEvent;

import com.kikis.commnlibrary.biz.CollectListener;


/**
 * Created by  on 2018/3/5.
 * 解决  photoview 与viewpager 组合时 图片缩放的错误 ；异常：.IllegalArgumentException: pointerIndex out of range
 */
public class BigPhotoViewPager extends ViewPager {
    /**
     * 上一次x坐标
     */
    private float beforeX;
    private boolean isCanScroll = true;
    private CollectListener clistener;

    float startX;
    float endX;

    public BigPhotoViewPager(Context context) {
        super(context);
    }

    public BigPhotoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            return super.dispatchTouchEvent(ev);
        } else {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN://按下如果‘仅’作为‘上次坐标’，不妥，因为可能存在左滑，motionValue大于0的情况（来回滑，只要停止坐标在按下坐标的右边，左滑仍然能滑过去）
                    beforeX = ev.getX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float motionValue = ev.getX() - beforeX;
                    if (motionValue < 0) {//禁止左滑
                        if (clistener != null)
                            clistener.ShowVip();
                        return true;
                    }
                    beforeX = ev.getX();//手指移动时，再把当前的坐标作为下一次的‘上次坐标’，解决上述问题

                    break;
                default:
                    break;
            }
            return super.dispatchTouchEvent(ev);
        }
    }


    public boolean isScrollble() {
        return isCanScroll;
    }

    /**
     * 设置 是否可以滑动
     *
     * @param isCanScroll
     * @param clistener
     */
    public void setScrollble(boolean isCanScroll, CollectListener clistener) {
        this.isCanScroll = isCanScroll;
        this.clistener = clistener;
    }
}