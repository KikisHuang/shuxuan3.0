package com.gxdingo.sg.view;

import android.content.Context;
import android.graphics.Typeface;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

/**
 * 带颜色渐变和缩放的指示器标题 并且选中字体加粗。
 * Created by Kikis on 2021/12/10.
 */
public class ScaleTransitionPagerTitleView extends ColorTransitionPagerTitleView {

    private float mMinScale = 0.78f;

    private int SelectedStyle = Typeface.BOLD;
    private int DeselectedStyle = Typeface.NORMAL;

    public ScaleTransitionPagerTitleView(Context context) {
        super(context);
    }

    @Override
    public void onSelected(int index, int totalCount) {
        setTypeface(Typeface.defaultFromStyle(SelectedStyle));
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        setTypeface(Typeface.defaultFromStyle(SelectedStyle));
    }


    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        super.onEnter(index, totalCount, enterPercent, leftToRight);    // 实现颜色渐变
        setScaleX(mMinScale + (1.2f - mMinScale) * enterPercent);
        setScaleY(mMinScale + (1.2f - mMinScale) * enterPercent);
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        super.onLeave(index, totalCount, leavePercent, leftToRight);    // 实现颜色渐变
        setScaleX(1.2f + (mMinScale - 1.2f) * leavePercent);
        setScaleY(1.2f + (mMinScale - 1.2f) * leavePercent);
    }

    public float getMinScale() {
        return mMinScale;
    }

    public void setMinScale(float minScale) {
        mMinScale = minScale;
    }

    public void setSelectedStyle(int style) {
        SelectedStyle = style;
    }

    public void setDeselectedStyle(int style) {
        DeselectedStyle = style;
    }
}
