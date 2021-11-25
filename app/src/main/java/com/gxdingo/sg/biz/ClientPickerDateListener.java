package com.gxdingo.sg.biz;

import com.lxj.xpopup.core.BottomPopupView;

/**
 * @author: Weaving
 * @date: 2021/10/21
 * @page:
 */
public interface ClientPickerDateListener {
    /**
     * 选择完日期后回调
     *
     * @param year              年
     * @param month             月
     */
    void onSelected(BottomPopupView dialog, int year, int month);

    /**
     * 点击取消时回调
     */
    default void onCancel(BottomPopupView dialog) {}
}
