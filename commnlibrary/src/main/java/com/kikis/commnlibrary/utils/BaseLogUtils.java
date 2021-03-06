package com.kikis.commnlibrary.utils;

import com.blankj.utilcode.util.LogUtils;

import static com.kikis.commnlibrary.utils.Constant.isDebug;

/**
 * 自定义log打印工具类
 */
public class BaseLogUtils {

        private static boolean D = isDebug;
//    private static boolean D = true;

    public static void w(final Object... contents) {
        if (contents == null)
            return;
        if (D)
            LogUtils.w(contents);
    }

    public static void i(final Object... contents) {
        if (contents == null)
            return;
        if (D)
            LogUtils.i(contents);
    }

    public static void e(final Object... contents) {
        if (contents == null)
            return;
        if (D)
            LogUtils.e(contents);
    }

    public static void d(final Object... contents) {
        if (contents == null)
            return;
        if (D)
            LogUtils.d(contents);
    }

}
