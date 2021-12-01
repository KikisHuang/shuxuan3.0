package com.gxdingo.sg.biz;

import android.net.Uri;

import com.tencent.smtt.sdk.ValueCallback;

/**
 * @author: Kikis
 * @date: 2020/11/30
 * @page:
 */
public interface WebViewLoadingListener {

    void onLoading(int progress);

    void onShowFileChooser(ValueCallback<Uri[]> valueCallback, int mode);
}
