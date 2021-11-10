package com.gxdingo.sg.model;

import android.content.Context;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.gxdingo.sg.bean.SocketLoginEvent;
import com.gxdingo.sg.bean.SocketPingEvent;
import com.gxdingo.sg.http.HttpClient;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.SignatureUtils;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.view.BaseWebSocket;

import org.java_websocket.enums.ReadyState;
import org.java_websocket.exceptions.WebsocketNotConnectedException;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static com.gxdingo.sg.http.Api.isUat;
import static com.gxdingo.sg.utils.LocalConstant.WEB_SOCKET_KEY;
import static com.gxdingo.sg.utils.LocalConstant.WEB_SOCKET_TEST_KEY;
import static com.gxdingo.sg.utils.LocalConstant.WEB_SOCKET_UAT_KEY;
import static com.kikis.commnlibrary.utils.Constant.isDebug;
import static org.java_websocket.enums.ReadyState.OPEN;

/**
 * @author: Kikis
 * @date: 2021/5/19
 * @page:websocket model类
 */
public class WebSocketModel {

    public static WebSocketModel instance;

    private Disposable mDisposable;

    private BaseWebSocket mWebSocket;

    private Context context;

    private String wsServerUrl;

    private int unReadMsgNum = 0;

    private int mChatTingId = 0;


    public static WebSocketModel getInstance(Context context) {
        if (instance == null) {
            synchronized (WebSocketModel.class) {
                if (instance == null) {
                    instance = new WebSocketModel(context);
                }
            }
        }
        return instance;

    }

    public WebSocketModel(Context context) {
        this.context = context;
    }

    /**
     * 聊天webSocket初始化
     *
     * @param wsServerUrl
     */
    public void webSocketInit(String wsServerUrl) {
        this.wsServerUrl = wsServerUrl;
        URI uri = URI.create(wsServerUrl);

        mWebSocket = BaseWebSocket.getInstance(uri);

        new Thread(() -> {
            try {
                mWebSocket.connectBlocking();

                if (mWebSocket.isOpen())
                    mWebSocket.send(getSendJson(LocalConstant.LOGIN));

                startSocketHeartTimer();
            } catch (Exception e) {
                LogUtils.e("socket error == " + e);
            }
        }).start();


    }

    public void setWsServerUrl(String wsServerUrl) {
        this.wsServerUrl = wsServerUrl;
    }

    /**
     * ping
     */
    public void sendPing() {

        if (mWebSocket == null)
            return;
        //如果已经断开连接，进行重连操作
        if (mWebSocket.getReadyState() != OPEN)
            reconnectWs();
        else {
            if (mWebSocket.isOpen()) {

                if (!mWebSocket.getReadyState().equals(ReadyState.OPEN)) {
                    reconnectWs();
                    return;
                } else {
                    try {
                        mWebSocket.sendPing();
                        mWebSocket.send(getSendJson(LocalConstant.PING));
                    } catch (WebsocketNotConnectedException e) {
                        LogUtils.e("WebsocketNotConnectedException === " + e);
                        reconnectWs();
                    }
                }
            }
        }
    }

    /**
     * 重连
     */
    public void reconnectWs() {
        if (mWebSocket != null) {
            mWebSocket.close();
            mWebSocket = null;
        }
        //onMessage("与聊天服务器断开，正在尝试重新连接。。。");
        webSocketInit(wsServerUrl);
    }

    /**
     * 获取发送参数
     *
     * @param exec
     * @return
     */
    private String getSendJson(String exec) {

        String timesTemp = HttpClient.getCurrentTimeUTCM();

        Map<String, String> signMap = new HashMap<>();
        //signMap.putAll(new HashMap<>());

        signMap.put(LocalConstant.EXEC, exec);

        signMap.put(LocalConstant.TIMESTAMP, timesTemp);

        if (exec == LocalConstant.LOGIN)
            signMap.put(LocalConstant.CROSSTOKEN, UserInfoUtils.getInstance().getUserInfo().getCrossToken());

        String sign = SignatureUtils.generate(signMap, isUat ? WEB_SOCKET_UAT_KEY : isDebug ? WEB_SOCKET_TEST_KEY : WEB_SOCKET_KEY, SignatureUtils.SignType.MD5);

        Object jsonBean;

        if (exec == LocalConstant.LOGIN)
            jsonBean = new SocketLoginEvent(exec, timesTemp, sign, UserInfoUtils.getInstance().getUserInfo().getCrossToken());
        else
            jsonBean = new SocketPingEvent(exec, timesTemp, sign);

        String json = GsonUtils.toJson(jsonBean);

        return json;
    }


    public void destroySocket() {

        if (mWebSocket != null) {
            mWebSocket.close();
            mWebSocket = null;
        }
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    /**
     * socket 心跳计时器
     */
    private void startSocketHeartTimer() {

        Observable.interval(LocalConstant.SOCKET_HEART_TIME, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        mDisposable = disposable;
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
                        if (UserInfoUtils.getInstance().isLogin())
                            sendPing();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (mDisposable != null) {
                            mDisposable.dispose();
                            mDisposable = null;
                        }
                    }

                    @Override
                    public void onComplete() {

                        if (mDisposable != null) {
                            mDisposable.dispose();
                            mDisposable = null;
                        }

                    }
                });
    }

    /**
     * 设置未读消息数
     *
     * @param num
     */
    public void setUnReadMessageNum(int num) {
        this.unReadMsgNum = num;
    }

    /**
     * 减去num未读消息数
     *
     * @param num
     */
    public void reduceUnReadMessageNum(int num) {
        this.unReadMsgNum -= num;
    }

    /**
     * 增加未读消息数+1
     */
    public void addUnReadMessageNum() {
        unReadMsgNum++;
    }

    /**
     * 获取未读消息树
     *
     * @return
     */
    public int getUnReadMessageNum() {
        return unReadMsgNum;
    }


    /**
     * 设置当前聊天id
     *
     * @param id
     */
    public void setChatTingId(int id) {
        mChatTingId = id;
    }

    /**
     * 获取当前聊天id
     */
    public int getChatTingId() {
        return mChatTingId;
    }

    /**
     * 清除当前聊天id
     */
    public void clearChatTingId() {
        if (mChatTingId > 0)
            mChatTingId = 0;
    }


}
