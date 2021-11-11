package com.gxdingo.sg.websocket;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BaseWebSocket extends WebSocketClient {

    private static final String TAG = "BaseWebSocket";

    public BaseWebSocket(URI serverUri) {
        super(serverUri, new Draft_6455());//使用的协议版本
    }

    /**
     * websocket连接开启时调用
     *
     * @param handshakedata
     */
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.e(TAG, "onOpen：已经成功连接！");
    }

    @Override
    public void send(byte[] data) {
        super.send(data);

    }

    /**
     * 接收到消息时调用
     *
     * @param message
     */
    @Override
    public void onMessage(String message) {
        Log.e(TAG, "onMessage()");
    }

    /**
     * 连接断开时调用
     *
     * @param code
     * @param reason
     * @param remote
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.e(TAG, "onClose()");
    }

    /**
     * 连接出错时调用
     *
     * @param ex
     */
    @Override
    public void onError(Exception ex) {
        Log.e(TAG, "onError()");
    }

}