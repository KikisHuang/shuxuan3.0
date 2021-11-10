package com.gxdingo.sg.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.gxdingo.sg.bean.ReceiveIMMessageBean;
import com.gxdingo.sg.bean.SocketLoginEvent;
import com.gxdingo.sg.http.HttpClient;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.SignatureUtils;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.websocket.BaseWebSocket;
import com.kikis.commnlibrary.utils.GsonUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.gxdingo.sg.http.Api.isUat;
import static com.gxdingo.sg.utils.LocalConstant.IM_OFFICIAL_HTTP_KEY;
import static com.gxdingo.sg.utils.LocalConstant.IM_UAT_HTTP_KEY;
import static com.gxdingo.sg.utils.LocalConstant.WEB_SOCKET_KEY;
import static com.kikis.commnlibrary.utils.Constant.WEB_SOCKET_URL;
import static com.kikis.commnlibrary.utils.Constant.isDebug;

/**
 * WebSocket消息接收服务
 * @author JM
 */
public class IMMessageReceivingService extends Service {
    private final String TAG = "IMMessageReceivingService";
    private BaseWebSocket mBaseWebSocket;
    private String mUrl = "";//web socket接入url
    private Timer mWebsocketStatusTimer;//websocket连接状态定时器
    private Timer mHeartbeatTimer;//心跳检测定时器

    @Override
    public void onCreate() {
        //向EventBus注册监听
        EventBus.getDefault().register(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        synchronized (this) {
            //避免多次实例化BaseWebSocket
            if (mBaseWebSocket == null) {
                connectWs();//连接websocket
            }
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(Object object) {

        if (object instanceof Integer) {
            int code = (int) object;
            //重置BaseWebSocket
            if (code == 100999) {
                mBaseWebSocket = null;//设置null，重新启动IMMessageReceivingService服务即可在onStartCommand方法重新实例化BaseWebSocket
            }
        }
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        Log.e(TAG, "WebSocketService服务被销毁");

        if (mWebsocketStatusTimer != null) {
            mWebsocketStatusTimer.cancel();
            mWebsocketStatusTimer = null;
        }

        if (mHeartbeatTimer != null) {
            mHeartbeatTimer.cancel();
            mHeartbeatTimer = null;
        }

        try {
            if (mBaseWebSocket != null) {
                mBaseWebSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mBaseWebSocket = null;
        }
        super.onDestroy();
        ToastUtils.showLong("测试：IM接收服务已停止");
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
                if (mBaseWebSocket != null) {
                    if (mBaseWebSocket.getReadyState() == ReadyState.OPEN) {
                        mBaseWebSocket.send(getWebSocketPassParameters(LocalConstant.PING));
                    }
                }
            }
        };
        mHeartbeatTimer = new Timer();
        mHeartbeatTimer.schedule(task2, 45 * 1000, 45 * 1000);//45秒进行一次心跳检测
    }

    /**
     * 连接
     */
    private void connectWs() {
        mUrl = SPUtils.getInstance().getString(WEB_SOCKET_URL);
        if (!TextUtils.isEmpty(mUrl)) {
            URI uri = URI.create(mUrl);
            mBaseWebSocket = new BaseWebSocket(uri) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.e(TAG, "onOpen：连接成功");
                    if (mBaseWebSocket.isOpen()) {
                        //连接成功后，发送登录信息
                        mBaseWebSocket.send(getWebSocketPassParameters(LocalConstant.LOGIN));
                    }
                }

                @Override
                public void onMessage(String message) {
                    //接收到服务器传来的消息
                    Log.e(TAG, "onMessage：" + message);
                    onMessageConversion(message);
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
        if (exec == LocalConstant.LOGIN) {
            signMap.put(LocalConstant.CROSSTOKEN, UserInfoUtils.getInstance().getUserInfo().getCrossToken());
        }
        String sign = SignatureUtils.generate(signMap, isUat ? IM_UAT_HTTP_KEY : isDebug ? IM_OFFICIAL_HTTP_KEY : WEB_SOCKET_KEY, SignatureUtils.SignType.MD5);

        String loginParameter = "";
        if (exec == LocalConstant.LOGIN) {
            SocketLoginEvent event = new SocketLoginEvent(exec, timesTemp, sign, crossToken);
            loginParameter = GsonUtil.gsonToStr(event);
        } else if (exec == LocalConstant.PING) {
            SocketLoginEvent event = new SocketLoginEvent(exec, timesTemp, sign, "");
            loginParameter = GsonUtil.gsonToStr(event);
        }
        return loginParameter;
    }

    /**
     * 接收的JSON消息转bean
     *
     * @param jsonMessage
     */
    private void onMessageConversion(String jsonMessage) {
        Gson gson = new Gson();
        ReceiveIMMessageBean chatMessageBean = gson.fromJson(jsonMessage, ReceiveIMMessageBean.class);
        passMessage(chatMessageBean);//传递消息
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
}
