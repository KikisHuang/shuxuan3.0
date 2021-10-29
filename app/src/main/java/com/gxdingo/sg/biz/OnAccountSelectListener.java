package com.gxdingo.sg.biz;

import com.lxj.xpopup.core.BottomPopupView;

/**
 * @author: Weaving
 * @date: 2021/10/29
 * @page:
 */
public interface OnAccountSelectListener {

    void onSelected(BottomPopupView dialog,String account,int type,int bankCardId);
}
