package com.gxdingo.sg.biz;

import com.gxdingo.sg.bean.SubscribesBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

import java.util.List;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientMessageContract {

    public interface ClientMessagePresenter extends MvpPresenter<BasicsListener,ClientMessageListener>{

        void getSubscribesMessage(boolean refresh);
    }

    public interface ClientMessageListener{
        void onSubscribes(List<SubscribesBean> subscribesBeans);
    }
}
