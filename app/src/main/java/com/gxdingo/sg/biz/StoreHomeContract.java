package com.gxdingo.sg.biz;

import com.gxdingo.sg.bean.SubscribesListBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

/**
 * 商家主页契约类
 * @author JM
 */
public class StoreHomeContract {

    public interface StoreHomePresenter extends MvpPresenter<BasicsListener, StoreHomeListener> {

        /**
         * 获取IM订阅信息列表
         */
        void getIMSubscribesList(boolean refresh);

        /**
         * 截取营业时间
         * @param time 时间
         * @return
         */
        String onInterceptionBusinessHours(String time);
    }

    public interface StoreHomeListener {
        /**
         * 返回IM订阅信息列表(包含有请求web socket接入url)
         */
        void onIMSubscribesInfo(boolean refresh, SubscribesListBean subscribesListBean);
    }
}
