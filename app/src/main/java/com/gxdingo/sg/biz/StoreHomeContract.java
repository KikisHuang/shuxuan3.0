package com.gxdingo.sg.biz;

import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

/**
 * 商家主页契约类
 * @author JM
 */
public class StoreHomeContract {

    public interface StoreHomePresenter extends MvpPresenter<BasicsListener, StoreHomeListener> {
        /**
         * 截取营业时间
         * @param time 时间
         * @return
         */
        String onInterceptionBusinessHours(String time);
    }

    public interface StoreHomeListener {

    }
}
