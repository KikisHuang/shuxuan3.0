package com.gxdingo.sg.view;



import com.gxdingo.sg.biz.WebViewLoadingListener;
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
}
