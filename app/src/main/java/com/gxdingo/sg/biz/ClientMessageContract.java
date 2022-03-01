package com.gxdingo.sg.biz;

import com.kikis.commnlibrary.bean.SubscribesListBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

import java.util.Map;

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

        void sendMessage(String shareUuid, int type, String content, int voiceDuration, Map<String, Object> params);

        void setTop(String shareUuid, int sort, int pos);

        void listChatDel(String shareUuid, int position);
    }

    public interface ClientMessageListener{
        void onSubscribes(boolean refresh, SubscribesListBean subscribesListBean);

        void clearMessageUnreadItem(String id);

        void setUnreadMsgNum(Integer data);

        void onSetTopResult(int pos, int sort);

        void onSubDel(int position);
    }
}
