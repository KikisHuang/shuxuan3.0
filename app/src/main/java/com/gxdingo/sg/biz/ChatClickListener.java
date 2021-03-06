package com.gxdingo.sg.biz;

import android.view.View;

public interface ChatClickListener {
    void onImageClick(String string);

    void onAudioClick(String content,  boolean isPlay,int pos);

    void onLocationMapClick(int pos);

    void clearUnread(int position, long id);

    void onTransferClick(int position, long id);

    void onAvatarClickListener(int position, String id);

    void onLongClickChatItem( int position, boolean isSelf);
}
