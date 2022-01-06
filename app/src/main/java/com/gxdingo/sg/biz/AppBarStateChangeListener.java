package com.gxdingo.sg.biz;

import android.annotation.SuppressLint;

import androidx.annotation.IntDef;

import com.google.android.material.appbar.AppBarLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {


    /*常量*/
    public final static int EXPANDED = 1;
    public final static int COLLAPSED = 2;
    public final static int IDLE = 3;

    @IntDef({EXPANDED, COLLAPSED, IDLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {

    }

    int mCurrentState = EXPANDED;

    @SuppressLint("WrongConstant")
    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {

        onStateOffset(appBarLayout, i);

        if (i == 0) {
            //展开状态
            if (mCurrentState != EXPANDED) {
                onStateChanged(appBarLayout, EXPANDED, i);
            }
            mCurrentState = EXPANDED;
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            //折叠状态
            if (mCurrentState != COLLAPSED) {
                onStateChanged(appBarLayout, COLLAPSED, i);
            }
            mCurrentState = COLLAPSED;
        } else {
            //中间状态
            if (mCurrentState != IDLE) {
                onStateChanged(appBarLayout, IDLE, i);
            }
            mCurrentState = IDLE;
        }
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, @Type int state, int verticalOffset);

    public abstract void onStateOffset(AppBarLayout appBarLayout, int verticalOffset);


}