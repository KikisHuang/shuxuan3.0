package com.gxdingo.sg.biz;

import com.lxj.xpopup.core.BasePopupView;

/**
 * @author: Weaving
 * @date: 2021/6/7
 * @page:
 */
public interface OnContentListener {

    void onConfirm(BasePopupView popupView,String content);

    default void cancel(BasePopupView popupView){};
}
