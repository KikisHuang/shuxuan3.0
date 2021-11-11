package com.gxdingo.sg.biz;

import com.lxj.xpopup.core.BasePopupView;

/**
 * @author: Weaving
 * @date: 2021/6/7
 * @page:
 */
public interface OnBusinessTimeListener {
    /**
     * 选择完时间后回调
     *
     * @param startHour              开始 时
     * @param startMinute            开始 分钟
     * @param endHour                结束 时
     * @param endMinute              结束 分钟
     */
    void onSelected(BasePopupView popupView, int startHour, int startMinute, int endHour, int endMinute);

    /**
     * 点击取消时回调
     */
    default void onCancel(BasePopupView popupView) {}
}
