package com.gxdingo.sg.utils;

import android.media.AudioManager;
import android.media.SoundPool;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gxdingo.sg.MyApplication;
import com.gxdingo.sg.bean.CouponVerificationEvent;
import com.gxdingo.sg.bean.ExitChatEvent;
import com.gxdingo.sg.bean.SocketLoginEvent;
import com.gxdingo.sg.http.HttpClient;
import com.gxdingo.sg.websocket.BaseWebSocket;
import com.kikis.commnlibrary.bean.ReLoginBean;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.kikis.commnlibrary.utils.BaseLogUtils;
import com.kikis.commnlibrary.utils.GsonUtil;
import com.kikis.commnlibrary.utils.KikisUitls;
import com.kikis.commnlibrary.utils.MessageCountManager;
import com.zhouyou.http.model.ApiResult;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static com.gxdingo.sg.utils.LocalConstant.IM_SIGN;
import static com.gxdingo.sg.utils.LocalConstant.isBackground;
import static com.kikis.commnlibrary.utils.BadgerManger.resetBadger;
import static com.kikis.commnlibrary.utils.Constant.CHAT_IDENTIFIER;
import static com.kikis.commnlibrary.utils.Constant.WEB_SOCKET_URL;
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

                    LogUtils.i(TAG, "onOpen：连接成功");
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
                    LogUtils.e(TAG, "onError：" + ex);

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
                        //普通的返回类型
                        ApiResult result = GsonUtil.GsonToBean(message, ApiResult.class);

                        if (result != null && result.getCode() == 408 && !LocalConstant.IS_CONTEACT_SERVER) {
                            //账号其他地方登录被顶下线返回：
                            UserInfoUtils.getInstance().clearLoginStatus();
                            EventBus.getDefault().post(new ReLoginBean(0, ""));
                            ToastUtils.showShort(result.getMsg());
                        }

                        //接收新消息的返回类型
                        ReceiveIMMessageBean messageBean = GsonUtil.GsonToBean(message, ReceiveIMMessageBean.class);

                        if (messageBean != null && messageBean.getType() == 1001 || messageBean.getType() == 1002) {
                            //商圈未读通知、商圈被评论通知
                            EventBus.getDefault().post(messageBean.getType() == 1001 ? LocalConstant.SHOW_BUSINESS_DISTRICT_UN_READ_DOT : messageBean.getDataByType());
                            return;
                        }else if (messageBean != null && messageBean.getType() == 2001){
                            //2001=优惠券核销通知
                            EventBus.getDefault().post(new CouponVerificationEvent(messageBean.getContent()));
                            return;
                        }
                        //消息接收
                        if (messageBean != null && messageBean.getId() > 0) {
                            //如果不是自己发送的消息，加入新消息未读
                            if (!CHAT_IDENTIFIER.equals(messageBean.getSendIdentifier())) {
                                playBeep();
                                MessageCountManager.getInstance().addNewMessage();
                            } else {
                                //自己发送的消息要手动调用服务端接口清除
                                if (!isEmpty(LocalConstant.CHAT_UUID))
                                    EventBus.getDefault().post(new ExitChatEvent(LocalConstant.CHAT_UUID));
                            }
                            passMessage(messageBean);
                        }
                    }
                }

            };
            try {
                if (!isEmpty(mUrl) && mUrl.contains("wss"))
                    sslInit();
                LogUtils.w(TAG, "afterTextChanged：" + mUrl);
                mBaseWebSocket.connectBlocking();
                startTimerTask();//启动定时器
            } catch (Exception e) {
                LogUtils.e(TAG, "onError：" + e);
            }

        } else {
            mBaseWebSocket = null;
        }

    }

    private void sslInit() throws Exception {

        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs,
                                           String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs,
                                           String authType) {
            }
        }};
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new SecureRandom());
        SSLSocketFactory factory = sslContext.getSocketFactory();
        mBaseWebSocket.setSocketFactory(factory);
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
