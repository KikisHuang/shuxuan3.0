package com.gxdingo.sg.presenter;

import com.gxdingo.sg.bean.SendIMMessageBean;
import com.gxdingo.sg.biz.ClientMessageContract;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.model.WebSocketModel;
import com.kikis.commnlibrary.bean.SubscribesListBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.kikis.commnlibrary.utils.MessageCountManager;
import com.zhouyou.http.subsciber.BaseSubscriber;

import java.util.Map;

import static com.kikis.commnlibrary.utils.BadgerManger.resetBadger;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientMessagePresenter extends BaseMvpPresenter<BasicsListener, ClientMessageContract.ClientMessageListener>
        implements ClientMessageContract.ClientMessagePresenter, NetWorkListener {

    private NetworkModel networkModel;

    private WebSocketModel mWebSocketModel;

    public ClientMessagePresenter() {
        networkModel = new NetworkModel(this);

        mWebSocketModel = new WebSocketModel(this);
    }

    @Override
    public void getSubscribesMessage(boolean refresh) {
        if (networkModel != null)
            mWebSocketModel.getMessageSubscribesList(getContext(), refresh, "");
//        networkModel.getMessageSubscribesList(getContext(),refresh);
    }

    @Override
    public void clearUnreadMsg(String id) {
        if (mWebSocketModel != null)
            mWebSocketModel.clearUnreadMessage(getContext(), id, o -> {
                getV().clearMessageUnreadItem(id);

            });
    }

    @Override
    public void refreshList() {
        if (mWebSocketModel != null) {
            mWebSocketModel.refreshMessageList(getContext());
        }
    }

    /**
     * 获取未读消息数
     */
    @Override
    public void getUnreadMessageNum() {

        if (mWebSocketModel != null) {
            mWebSocketModel.getUnreadMessageNumber(getContext(), data -> {
                MessageCountManager.getInstance().setUnreadMessageNum((Integer) data);
                resetBadger(getContext());

                if (isViewAttached())
                    getV().setUnreadMsgNum((Integer) data);
            });
        }


    }

    /**
     * 发送消息
     *
     * @param shareUuid
     * @param type
     * @param content
     * @param voiceDuration
     * @param params
     */
    @Override
    public void sendMessage(String shareUuid, int type, String content, int voiceDuration, Map<String, Object> params) {
        if (mWebSocketModel != null) {
            SendIMMessageBean sendIMMessageBean = new SendIMMessageBean(shareUuid, type, content, voiceDuration, params);
            mWebSocketModel.sendMessage(getContext(), sendIMMessageBean, receiveIMMessageBean -> {
                if (isBViewAttached()) {
                    onMessage("转发成功");
                    getBV().onSucceed(100);
                }
            });
        }
    }

    /**
     * 置顶
     *
     * @param shareUuid
     * @param sort
     * @param pos
     */
    @Override
    public void setTop(String shareUuid, int sort, int pos) {

        if (mWebSocketModel != null) {
            mWebSocketModel.chatSetTop(getContext(), shareUuid, sort, result -> {
                if (isViewAttached())
                    getV().onSetTopResult(pos,sort);

            });
        }

    }

    /**
     * 订阅列表删除
     * @param shareUuid
     * @param position
     */
    @Override
    public void listChatDel(String shareUuid, int position) {

        if (mWebSocketModel != null) {
            mWebSocketModel.chatSubDel(getContext(), shareUuid, result -> {
                if (isViewAttached())
                    getV().onSubDel(position);

            });
        }
    }

    @Override
    public void onSucceed(int type) {
        if (isBViewAttached())
            getBV().onSucceed(type);
    }

    @Override
    public void onMessage(String msg) {
        if (isBViewAttached())
            getBV().onMessage(msg);
    }

    @Override
    public void noData() {
        if (isBViewAttached())
            getBV().noData();
    }

    @Override
    public void onData(boolean refresh, Object o) {
        if (o instanceof SubscribesListBean)
            getV().onSubscribes(refresh, (SubscribesListBean) o);
    }

    @Override
    public void haveData() {
        if (isBViewAttached())
            getBV().haveData();
    }

    @Override
    public void finishLoadmoreWithNoMoreData() {
        if (isBViewAttached())
            getBV().finishLoadmoreWithNoMoreData();
    }

    @Override
    public void finishRefreshWithNoMoreData() {
        if (isBViewAttached())
            getBV().finishRefreshWithNoMoreData();
    }

    @Override
    public void onRequestComplete() {
        if (isBViewAttached())
            getBV().onRequestComplete();
    }

    @Override
    public void resetNoMoreData() {
        if (isBViewAttached())
            getBV().resetNoMoreData();
    }

    @Override
    public void finishRefresh(boolean success) {
        if (isBViewAttached())
            getBV().finishRefresh(success);
    }

    @Override
    public void finishLoadmore(boolean success) {
        if (isBViewAttached())
            getBV().finishLoadmore(success);
    }

    @Override
    public void onAfters() {
        if (isBViewAttached())
            getBV().onAfters();
    }

    @Override
    public void onStarts() {
        if (isBViewAttached())
            getBV().onStarts();
    }

    @Override
    public void onDisposable(BaseSubscriber subscriber) {
        addDisposable(subscriber);
    }
}
