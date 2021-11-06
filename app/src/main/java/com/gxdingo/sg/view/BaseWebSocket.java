//package com.gxdingo.sg.view;
//
//import com.blankj.utilcode.util.LogUtils;
//import com.gxdingo.sg.bean.NewMessageList;
//import com.gxdingo.sg.bean.NoticesBean;
//import com.gxdingo.sg.model.WebSocketModel;
//import com.kikis.commnlibrary.bean.NewMessage;
//import com.kikis.commnlibrary.utils.GsonUtil;
//
//import org.greenrobot.eventbus.EventBus;
//import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.drafts.Draft_6455;
//import org.java_websocket.enums.ReadyState;
//import org.java_websocket.handshake.ServerHandshake;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.net.URI;
//import java.util.List;
//
//import static com.gxdingo.sg.utils.LocalConstant.NEWS;
//import static com.gxdingo.sg.utils.LocalConstant.REPLY;
//import static com.kikis.commnlibrary.utils.Constant.isDebug;
//import static com.kikis.commnlibrary.utils.MyToastUtils.customToast;
//
//public class BaseWebSocket extends WebSocketClient {
//
//    public static BaseWebSocket instance;
//
//    private int mChatTingId = 0;
//
//    public static BaseWebSocket getInstance(URI uri) {
//        if (instance != null) {
//            instance.close();
//            instance = null;
//        }
//        instance = new BaseWebSocket(uri);
//        return instance;
//    }
//
//    public static BaseWebSocket getInstance() {
//        return instance;
//    }
//
//    /**
//     * 设置当前聊天id
//     *
//     * @param id
//     */
//    public void setChatTingId(int id) {
//        mChatTingId = id;
//    }
//
//    /**
//     * 获取当前聊天id
//     */
//    public int getChatTingId() {
//        return mChatTingId;
//    }
//
//    /**
//     * 清除当前聊天id
//     */
//    public void clearChatTingId() {
//        if (mChatTingId > 0)
//            mChatTingId = 0;
//    }
//
//    public BaseWebSocket(URI serverUri) {
//        super(serverUri, new Draft_6455());
//    }
//
//
//    @Override
//    public void onOpen(ServerHandshake handshakedata) {
//        if (isDebug)
//            LogUtils.w("BaseWebSocket", "onOpen()");
//    }
//
//    @Override
//    public void onMessage(String message) {
//
//        if (isDebug)
//            LogUtils.i("BaseWebSocket", "onMessage() == " + message);
//
//        NoticesBean chatBean = GsonUtil.GsonToBean(message, NoticesBean.class);
//
//        //请求成功
//        if (chatBean.getCode() == 0) {
//            switch (chatBean.getType()) {
//                case REPLY:
//                    try {
//
//                        JSONObject object = new JSONObject(message);
//
//                        JSONObject data = new JSONObject(object.optString("data"));
//
//                        List<NewMessage> newlist = GsonUtil.jsonToList(data.optString("newsList"), NewMessage.class);
//
//                        //返回自上次下线之后的未读消息
//                        if (newlist != null && newlist.size() > 0)
//                            EventBus.getDefault().post(new NewMessageList(newlist));
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    break;
//
//                case NEWS:
//                    try {
//                        if (WebSocketModel.getInstance(null) != null)
//                            WebSocketModel.getInstance(null).addUnReadMessageNum();
//
//                        JSONObject jsonObject = new JSONObject(message);
//
//                        EventBus.getDefault().post(GsonUtil.GsonToBean(jsonObject.optString("data"), NewMessage.class));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    break;
//            }
//        } else {
//            customToast(chatBean.getMsg());
//            LogUtils.e(chatBean.getMsg() + "code  === " + chatBean.getCode());
//        }
//
//    }
//
//
//    @Override
//    public void send(String text) {
//        super.send(text);
//
//        if (!getReadyState().equals(ReadyState.OPEN)){
//            try {
//                connectBlocking();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return;
//        }
//
//        if (isDebug)
//            LogUtils.i("BaseWebSocket", "send msg === " + text);
//    }
//
//    @Override
//    public void onClose(int code, String reason, boolean remote) {
//
//        if (instance != null)
//            instance = null;
//
//        if (isDebug)
//            LogUtils.i("BaseWebSocket", "关闭聊天socket 正在尝试重连....");
//        try {
//            if (!isOpen()) {
//                if (getReadyState().equals(ReadyState.NOT_YET_CONNECTED)) {
//                    try {
//                        connectBlocking();
//                    } catch (IllegalStateException e) {
//                    }
//                } else if (getReadyState().equals(ReadyState.CLOSING) || getReadyState().equals(ReadyState.CLOSED)) {
//                    reconnect();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtils.i("BaseWebSocket", " error === " + e);
//        }
//    }
//
//    @Override
//    public void onError(Exception ex) {
//
//        if (isDebug)
//            LogUtils.e("BaseWebSocket", "onError() === " + ex);
////        customToast("连接聊天服务器异常");
//    }
//}
