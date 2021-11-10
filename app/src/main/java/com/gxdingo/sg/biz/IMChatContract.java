package com.gxdingo.sg.biz;

import android.content.Context;

import com.gxdingo.sg.bean.IMChatHistoryListBean;
import com.gxdingo.sg.bean.ReceiveIMMessageBean;
import com.gxdingo.sg.bean.SendIMMessageBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

/**
 * IM聊天契约类
 *
 * @author JM
 */
public class IMChatContract {
    public interface IMChatPresenter extends MvpPresenter<BasicsListener, IMChatListener> {

        /**
         * 获取聊天记录列表
         */
        void getChatHistoryList(String shareUuid);

        /**
         * 发送文本消息
         *
         * @param text 文本消息
         */
        void sendTextMessage(String shareUuid, String text);

        /**
         * 发送图片消息
         *
         * @param url 图片URL
         */
        void sendPictureMessage(String shareUuid, String url);

        /**
         * 发送语音消息
         *
         * @param url 语音URL
         */
        void sendVoiceMessage(String shareUuid, String url,int voiceDuration);

        /**
         * 发送转账消息
         *
         * @param id
         */
        void sendTransferAccountsMessage(String shareUuid, String id);

        /**
         * 照片来源点击
         *
         * @param pos 0 相册，1 相机
         */
        void photoSourceClick(int pos);
    }

    public interface IMChatListener {

        /**
         * 返回聊天记录列表
         */
        void onChatHistoryList(IMChatHistoryListBean imChatHistoryListBean);

        /**
         * 发送(文字、图片、语音、转账)消息成功
         *
         * @param receiveIMMessageBean 接收的IM消息（刚才发送成功的消息）
         */
        void onSendMessageSuccess(ReceiveIMMessageBean receiveIMMessageBean);

        /**
         * 回调上传图片URL
         *
         * @param url 服务器返回上传后的URL
         */
        void onUploadImageUrl(String url);
    }
}
