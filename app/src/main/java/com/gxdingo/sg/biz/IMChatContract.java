package com.gxdingo.sg.biz;

import android.content.Context;

import com.gxdingo.sg.bean.IMChatHistoryListBean;
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
         * 上传图片后回调
         *
         * @param url 服务器返回上传后的URL
         */
        void uploadImage(String url);
    }
}
