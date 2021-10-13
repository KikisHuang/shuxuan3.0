package com.kikis.commnlibrary.biz;

/**
 * Created by lian on 2017/11/27.
 */
public interface EdittextListener {
    void onContentChange(String s);

    void onSelectionChanged(int selStart, int selEnd);

    void onEnterClick();
}
