package com.gxdingo.sg.utils;

import android.media.AudioManager;
import android.media.SoundPool;
import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gxdingo.sg.bean.ExitChatEvent;
import com.gxdingo.sg.bean.SocketLoginEvent;
import com.gxdingo.sg.http.HttpClient;
import com.gxdingo.sg.websocket.BaseWebSocket;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.kikis.commnlibrary.utils.BaseLogUtils;
import com.kikis.commnlibrary.utils.GsonUtil;
import com.kikis.commnlibrary.utils.KikisUitls;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.gxdingo.sg.http.Api.isUat;
import static com.gxdingo.sg.utils.LocalConstant.CHAT_IDENTIFIER;
import static com.gxdingo.sg.utils.LocalConstant.IM_OFFICIAL_HTTP_KEY;
import static com.gxdingo.sg.utils.LocalConstant.IM_SIGN;
import static com.gxdingo.sg.utils.LocalConstant.IM_UAT_HTTP_KEY;
import static com.gxdingo.sg.utils.LocalConstant.WEB_SOCKET_KEY;
import static com.gxdingo.sg.utils.LocalConstant.isBackground;
import static com.kikis.commnlibrary.utils.Constant.WEB_SOCKET_URL;
import static com.kikis.commnlibrary.utils.Constant.isDebug;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

public class ImMessageUtils {

    private final String TAG = ImMessageUtils.class.toString();

    private BaseWebSocket mBaseWebSocket;

    private String mUrl = "";//web socket接入url

    private Timer mWebsocketStatusTimer;//websocket连接状态定时器

    private Timer mHeartbeatTimer;//心跳检测定时器

    private SoundPool mSoundPool;

    private int streamID;

    private static ImMessageUtils instance;

    public static ImMessageUtils getInstance() {
        if (instance == null) {
            synchronized (ImMessageUtils.class) {
                instance = new ImMessageUtils();
            }
        }
        return instance;
    }

    /**
     * im web socket是否运行中
     *
     * @return
     */
    public boolean isRunning() {
        return mBaseWebSocket != null && mBaseWebSocket.isOpen();
    }

    /**
     * 启动
     */
    public void start() {

        //设置最多可容纳1个音频流，音频的品质为5
        mSoundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        try {
            streamID = mSoundPool.load(KikisUitls.getContext().getAssets().openFd("beep/beep.mp3"), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        synchronized (this) {
            //避免多次实例化BaseWebSocket
            if (mBaseWebSocket == null) {
                connectWs();//连接websocket
            }
        }
    }

    /**
     * 连接
     */
    private void connectWs() {

        BaseLogUtils.i(TAG, "WebSocketService connect");

        mUrl = SPUtils.getInstance().getString(WEB_SOCKET_URL);

        if (!TextUtils.isEmpty(mUrl)) {
            URI uri = URI.create(mUrl);

            mBaseWebSocket = new BaseWebSocket(uri) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {

                    BaseLogUtils.i(TAG, "onOpen：连接成功");
                    if (mBaseWebSocket.isOpen()) {
                        String info = getWebSocketPassParameters(LocalConstant.PING);
                        //连接成功后，发送登录信息
                        mBaseWebSocket.send(info);

                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    super.onClose(code, reason, remote);
                }

                @Override
                public void onError(Exception ex) {
                    super.onError(ex);


                    BaseLogUtils.i(TAG, "onError：" + ex);

                    if (mBaseWebSocket != null && !mBaseWebSocket.isOpen()) {
                        mBaseWebSocket.close();
                        mBaseWebSocket = null;
                    }
                }

                @Override
                public void onMessage(String message) {

                    //接收到服务器传来的消息
                    BaseLogUtils.i(TAG, "onMessage：" + message);

                    if (!isEmpty(message)) {
                        ReceiveIMMessageBean messageBean = GsonUtil.GsonToBean(message, ReceiveIMMessageBean.class);

                        //消息接收
                        if (messageBean != null && messageBean.getId() > 0) {

                            if (!CHAT_IDENTIFIER.equals(messageBean.getSendIdentifier()))
                                MessageCountUtils.getInstance().addNewMessage();
                            else {
                                if (!isEmpty(LocalConstant.CHAT_UUID))
                                    EventBus.getDefault().post(new ExitChatEvent(LocalConstant.CHAT_UUID));
                            }
                            playBeep();
                            passMessage(messageBean);
                        }
                    }

                }
            };
            try {
                mBaseWebSocket.connectBlocking();
                startTimerTask();//启动定时器
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else {
            mBaseWebSocket = null;
        }
    }


    /**
     * 获取WebSocket传递参数
     *
     * @param exec
     * @return
     */
    private String getWebSocketPassParameters(String exec) {
        String timesTemp = HttpClient.getCurrentTimeUTCM();
        String crossToken = "";

        if (UserInfoUtils.getInstance().isLogin()) {
            crossToken = UserInfoUtils.getInstance().getUserInfo().getCrossToken();
        }

        Map<String, String> signMap = new HashMap<>();
        signMap.put(LocalConstant.EXEC, exec);
        signMap.put(LocalConstant.TIMESTAMP, timesTemp);

        if ((exec == LocalConstant.PING) && !isEmpty(crossToken)) {
            signMap.put(LocalConstant.CROSSTOKEN, crossToken);
        }

        String sign = SignatureUtils.generate(signMap, IM_SIGN, SignatureUtils.SignType.MD5);

        String loginParameter = "";
        if (exec == LocalConstant.LOGIN) {
            SocketLoginEvent event = new SocketLoginEvent(exec, timesTemp, sign, crossToken);
            loginParameter = GsonUtil.gsonToStr(event);
        } else if (exec == LocalConstant.PING) {
            SocketLoginEvent event = new SocketLoginEvent(exec, timesTemp, sign, crossToken);
            loginParameter = GsonUtil.gsonToStr(event);
        }
        return loginParameter;
    }


    /**
     * 将服务器发送的聊天消息传递
     *
     * @param receiveIMMessageBean
     */
    private void passMessage(ReceiveIMMessageBean receiveIMMessageBean) {
        //发送接受到的聊天消息
        EventBus.getDefault().post(receiveIMMessageBean);
    }

    /**
     * 播放提示音
     */
    private void playBeep() {
        if (mSoundPool != null && !isBackground)
            mSoundPool.play(streamID, 1, 1, 1, 0, 1.0f);
    }

    /**
     * 重新连接
     */
    private void reconnectWs() {
        if (mBaseWebSocket != null) {
            try {
                //重连
                mBaseWebSocket.reconnectBlocking();
                startTimerTask();//启动定时器
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 启动定时器任务
     */
    private void startTimerTask() {
        if (mWebsocketStatusTimer != null) {
            mWebsocketStatusTimer.cancel();
            mWebsocketStatusTimer = null;
        }
        if (mHeartbeatTimer != null) {
            mHeartbeatTimer.cancel();
            mHeartbeatTimer = null;
        }

        //websocket连接状态定时任务
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (mBaseWebSocket != null) {
                    //连接服务器是否失败
                    if (mBaseWebSocket.getReadyState() != ReadyState.OPEN) {
                        if (mWebsocketStatusTimer != null) {
                            mWebsocketStatusTimer.cancel();
                        }
                        if (mHeartbeatTimer != null) {
                            mHeartbeatTimer.cancel();
                        }
                        reconnectWs();//重新连接
                    }
                }
            }
        };
        mWebsocketStatusTimer = new Timer();
        mWebsocketStatusTimer.schedule(task, 5000, 5000);//5秒检测一次

        //心跳检测定时任务
        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                try {

                    if (mBaseWebSocket != null && mBaseWebSocket.isOpen()) {
                        if (mBaseWebSocket.getReadyState() == ReadyState.OPEN && !isBackground) {
                            mBaseWebSocket.send(getWebSocketPassParameters(LocalConstant.PING));
                        }
                    }

                } catch (Exception e) {
                    BaseLogUtils.e("WebSocket Error === " + e);
                }
            }
        };
        mHeartbeatTimer = new Timer();
        mHeartbeatTimer.schedule(task2, 45 * 1000, 45 * 1000);//45秒进行一次心跳检测
    }

    /**
     * 重置
     */
    public void reSet() {

        BaseLogUtils.i(TAG, "WebSocketService reset");

        if (mBaseWebSocket != null)
            mBaseWebSocket = null;

        start();
    }

    /**
     * 关闭
     */
    public void stop() {

        BaseLogUtils.i(TAG, "WebSocketService stop");

        if (mSoundPool != null) {
            mSoundPool.release();
            mSoundPool = null;
        }

        if (mWebsocketStatusTimer != null) {
            mWebsocketStatusTimer.cancel();
            mWebsocketStatusTimer = null;
        }

        if (mHeartbeatTimer != null) {
            mHeartbeatTimer.cancel();
            mHeartbeatTimer = null;
        }

        try {
            if (mBaseWebSocket != null)
                mBaseWebSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mBaseWebSocket = null;
        }
    }

}
