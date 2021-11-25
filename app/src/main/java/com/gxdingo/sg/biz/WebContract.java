package com.gxdingo.sg.biz;


import com.gxdingo.sg.bean.WebBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

import java.util.List;

/**
 * Created by Kikis on 2020/4/6.
 */

public class WebContract {

    public interface WebPresenter extends MvpPresenter<BasicsListener, WebListener> {

        void getArticleDetail(int articleId, String identifier);

        void loadData(int articleId, String identifier);

        void sharePayment(String url, String title, String description, String thumb);
    }

    public interface WebListener {

        void loadWebUrl(WebBean webBean);

        void onArticleListResult(List<WebBean> webBeans);
    }

}