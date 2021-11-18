package com.gxdingo.sg.biz;

import com.kikis.commnlibrary.bean.AddressBean;
import com.gxdingo.sg.bean.IMChatHistoryListBean;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;
import java.util.Map;

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
        void getChatHistoryList(String shareUuid, int otherId, int otherRole);


        /**
         * 发送消息
         */
        void sendMessage(String shareUuid, int type, String content, int voiceDuration, Map<String, Object> params);

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
         * @param pos
         */
        void sendPictureMessage(String shareUuid, String url, int pos);

        /**
         * 发送语音消息
         *
         * @param url 语音URL
         */
        void sendVoiceMessage(String shareUuid, String url, int voiceDuration);

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

        /**
         * 跳转第三方地图
         *
         * @param pos
         * @param mAddress
         */
        void goOutSideNavigation(int pos, AddressBean mAddress);

        /**
         * 开始录制语音
         */
        void startRecorder();

        /**
         * 停止录制语音
         */
        void stopRecorder();

        /**
         * 取消录制语音
         */
        void cancelRecorder();

        /**
         * 获取录音权限
         *
         * @param rxPermissions
         */
        void checkRecordPermissions(RxPermissions rxPermissions);

        /**
         * 语音播放
         *
         * @param content
         */
        void playVoice(String content);

        /**
         * 停止语音播放
         */
        void stopVoice();

        /**
         * 清除语音未读
         *
         * @param id
         */
        void clearMessageUnread(long id);

        /**
         * 领取转账
         *
         * @param position
         * @param id
         */
        void getTransfer(int position, long id);

        /**
         * 获取地址列表
         */
        void getAddressList();

        /**
         * 获取默认地址
         */
        void getCacheAddress();
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
         * 发送(文字、图片、语音、转账)消息成功带下标
         *
         * @param receiveIMMessageBean 接收的IM消息（刚才发送成功的消息）
         */
        void onSendMessageSuccessResultPos(ReceiveIMMessageBean receiveIMMessageBean, int pos);

        /**
         * 回调上传图片URL
         *
         * @param url 服务器返回上传后的URL
         */
        void onUploadImageUrl(String url);

        /**
         * 获取uuid
         *
         * @return
         */
        String getShareUUID();

        /**
         * 接收转账成功
         *
         * @param position
         */
        void getTransFerSucceed(int position);

        /**
         * 显示选择地址弹窗
         *
         * @param list
         */
        void showSelectAddressDialog(List<AddressBean> list);

        /**
         * 默认地址回调
         *
         * @param cacheDefaultAddress
         */
        void onAddressResult(AddressBean cacheDefaultAddress);
    }
}
