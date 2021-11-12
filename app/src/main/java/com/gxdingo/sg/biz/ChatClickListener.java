package com.gxdingo.sg.biz;

import android.graphics.drawable.AnimationDrawable;

public interface ChatClickListener {
    void onImageClick(String string);

    void onAudioClick(String content,  boolean isPlay,int pos);

    void clearUnread(long id);

}
