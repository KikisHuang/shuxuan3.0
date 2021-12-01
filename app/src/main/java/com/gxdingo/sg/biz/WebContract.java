package com.gxdingo.sg.biz;


import android.net.Uri;

import com.gxdingo.sg.bean.WebBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;
import com.tencent.smtt.sdk.ValueCallback;

import java.util.List;

/**
 * Created by Kikis on 2020/4/6.
 */

public class WebContract {

    public interface WebPresenter extends MvpPresenter<BasicsListener, WebListener> {

        void getArticleDetail(int articleId, String identifier);

        void loadData(int articleId, String identifier);

        void sharePayment(String url, String title, String description, String thumb);

        void openPhoto(ValueCallback<Uri[]> valueCallback, int mode);
    }

    public interface WebListener {

        void loadWebUrl(WebBean webBean);

        void onArticleListResult(List<WebBean> webBeans);

        void uploadImage(ValueCallback<Uri[]> valueCallback, Uri parse);
    }

}