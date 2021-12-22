package com.gxdingo.sg.model;

import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;

import com.blankj.utilcode.util.LogUtils;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebView;

import java.io.File;

public class WebModel {

    public WebModel() {
    }


    /**
     * 清除缓存
     *
     * @param context 上下文
     */
    public static void clearCache(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 清除cookie
                CookieManager.getInstance().removeAllCookies(null);
            } else {
                CookieSyncManager.createInstance(context);
                CookieManager.getInstance().removeAllCookie();
                CookieSyncManager.getInstance().sync();
            }
            new WebView(context).clearCache(true);

            File cacheFile = new File(context.getCacheDir().getParent() + "/app_webview");
            clearCacheFolder(cacheFile, System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除缓存文件
     *
     * @param dir
     * @param time
     * @return
     */
    private static int clearCacheFolder(File dir, long time) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, time);
                    }
                    if (child.lastModified() < time) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }
}
