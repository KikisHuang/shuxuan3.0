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
import com.gxdingo.sg.bean.SendIMMessageBean;
import com.gxdingo.sg.websocket.BaseWebSocket;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URI;

import static com.kikis.commnlibrary.utils.Constant.WEB_SOCKET_URL;

/**
 * WebSocket消息接收服务
 *
 * @author JM
 */
public class IMMessageReceivingService extends Service {
    private final String TAG = "IMMessageReceivingService";
    public static final String EXTRA_WEB_SOCKET_URL = "ExtraWebSocketUrl";
    BaseWebSocket mBaseWebSocket;
    String mUrl = "";//web socket接入url

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
                mUrl = SPUtils.getInstance().getString(WEB_SOCKET_URL);
                if (!TextUtils.isEmpty(mUrl)) {
                    URI uri = URI.create(mUrl);
                    mBaseWebSocket = new BaseWebSocket(uri) {
                        @Override
                        public void onMessage(String message) {
                            //接收到服务器传来的消息
                            Log.e(TAG, message);
                            onMessageConversion(message);
                        }
                    };
                    try {
                        mBaseWebSocket.connectBlocking();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    mBaseWebSocket = null;
                }
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
        //接收到聊天界面发送的消息
        if (object instanceof SendIMMessageBean) {
            SendIMMessageBean sendIMMessage = (SendIMMessageBean) object;
            //sendMessage(sendIMMessage);//发送消息
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        Log.e(TAG, "WebSocketService服务被销毁");

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
