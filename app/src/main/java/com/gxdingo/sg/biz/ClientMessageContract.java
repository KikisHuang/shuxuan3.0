package com.gxdingo.sg.biz;

import com.gxdingo.sg.bean.SubscribesBean;
import com.kikis.commnlibrary.bean.SubscribesListBean;
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

        void clearUnreadMsg(String id);

        void refreshList();

        void getUnreadMessageNum();
    }

    public interface ClientMessageListener{
        void onSubscribes(boolean refresh, SubscribesListBean subscribesListBean);

        void clearMessageUnreadItem(String id);

        void setUnreadMsgNum(Integer data);
    }
}
