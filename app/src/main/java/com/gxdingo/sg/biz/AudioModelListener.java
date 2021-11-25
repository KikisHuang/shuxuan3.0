package com.gxdingo.sg.biz;

/**
 * @author: Kikis
 * @date: 2021/5/7
 * @page:
 */
public interface AudioModelListener {

    void onAudioMessage(String msg);

    void onAudioError(String ermsg);

    void onRecorderComplete();

}
