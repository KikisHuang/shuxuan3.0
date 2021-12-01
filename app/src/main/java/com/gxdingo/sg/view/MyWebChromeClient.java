package com.gxdingo.sg.view;


import android.net.Uri;

import com.blankj.utilcode.util.LogUtils;
import com.gxdingo.sg.biz.WebViewLoadingListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;


/**
 * @author: Kikis
 * @date: 2020/11/19
 * @page:
 */
public class MyWebChromeClient extends WebChromeClient {

    private WebViewLoadingListener listener;


    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (listener != null)
            listener.onLoading(newProgress);
    }

    public void setListener(WebViewLoadingListener listener) {
        this.listener = listener;
    }

    @Override
    public void openFileChooser(ValueCallback<Uri> valueCallback, String s, String s1) {
        LogUtils.w("openFileChooser");
        LogUtils.w("s === " + s);
        LogUtils.w("s1 === " + s1);
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {

        if (listener != null && fileChooserParams != null)
            listener.onShowFileChooser(valueCallback, fileChooserParams.getMode());

        //fileChooserParams mode 1 相册 0 视频
        return true;

    }
}
