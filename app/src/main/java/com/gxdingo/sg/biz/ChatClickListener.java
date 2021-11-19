package com.gxdingo.sg.biz;

public interface ChatClickListener {
    void onImageClick(String string);

    void onAudioClick(String content,  boolean isPlay,int pos);

    void clearUnread(long id);

    void onTransferClick(int position, long id);

    void onAvatarClickListener(int position, long id);
}
