package com.gxdingo.sg.biz;

import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

/**
 * IM聊天契约类
 *
 * @author JM
 */
public class IMChatContract {
    public interface IMChatPresenter extends MvpPresenter<BasicsListener, IMChatListener> {
        /**
         * 照片来源点击
         *
         * @param pos 0 相册，1 相机
         */
        void photoSourceClick(int pos);
    }

    public interface IMChatListener {
        /**
         * 上传图片后回调
         * @param url 服务器返回上传后的URL
         */
        void uploadImage(String url);
    }
}
