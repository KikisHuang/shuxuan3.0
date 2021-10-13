package com.kikis.commnlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;


import java.lang.reflect.Method;

import static com.kikis.commnlibrary.utils.KikisUitls.getContext;


/**
 * Created by Forrest on 16/5/4.
 */
public class DeviceUtils {
    public static final int DEVICE_SCALE = 640;
    private static final String TAG = "DeviceUtils";
    public static int param = 0;

    /**
     * 获取屏幕宽和高
     *
     * @param context
     * @return
     */
    public static int[] getScreenHW(Context context) {
        int[] hw = new int[3];
        try {
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(dm);
            hw[0] = dm.widthPixels;//屏幕宽带(像素)
            hw[1] = dm.heightPixels;//屏幕高度(像素)
            hw[2] = dm.densityDpi;//屏幕密度(120/160/240)
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hw;
    }

    /**
     * 手机屏幕宽度
     *
     * @return
     */
    public static int getWindowWidth() {
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * getRealMetrics - 屏幕的原始尺寸，即包含状态栏。
     * version >= 4.2.2
     */
    public static int getRatio(Context context, boolean wid) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        if (wid)
            return width;
        else
            return height;
    }

    /**
     * 手机屏幕高度
     *
     * @return
     */
    public static int getWindowHeight() {

        Display display = ((WindowManager) getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        return metrics.heightPixels;
    }

    /**
     * 获取顶部statusBar高度
     *
     * @return
     */
    public static int getStatusBarHeight() {
        Resources resources = getContext().getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * 获取底部navigationBar高度
     *
     * @return
     */
    public static int getNavigationBarHeight() {
        if (checkDeviceHasNavigationBar(getContext())) {
            Resources resources = getContext().getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            int height = resources.getDimensionPixelSize(resourceId);
            return height;
        }
        return 0;
    }

    /**
     * 获取设备是否存在NavigationBar
     *
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            //do something
        }
        return hasNavigationBar;
    }


    /**
     * 单次测量view控件宽高方法;
     *
     * @param flag 0高,1宽;
     * @param view 需要测量的view控件;
     * @return
     */
    public static int getViewHeightOfWidth(final int flag, final View view) {
        ViewTreeObserver vto2 = view.getViewTreeObserver();
        param = 0;
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (flag == 0)
                    param = view.getHeight();
                else
                    param = view.getWidth();
            }
        });
        return param;
    }

}
