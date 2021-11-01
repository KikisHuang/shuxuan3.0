package com.gxdingo.sg.utils;

import android.app.Activity;
import android.content.Context;

import com.gyf.immersionbar.ImmersionBar;

/**
 * @author: Weaving
 * @date: 2021/10/21
 * @page:
 */
public class StatusBarUtils {

    public static void setStatusBarColor(Activity context,int color){
        ImmersionBar mImmersionBar = ImmersionBar.with(context).fitsSystemWindows(true);
        mImmersionBar.statusBarDarkFont(true, 0.2f).statusBarColor(color);
        mImmersionBar.init();
    }
}
