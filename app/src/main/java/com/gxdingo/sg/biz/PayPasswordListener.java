package com.gxdingo.sg.biz;

import com.gxdingo.sg.view.PasswordLayout;
import com.lxj.xpopup.core.CenterPopupView;

/**
 * @author: Weaving
 * @date: 2021/5/11
 * @page:
 */
public interface PayPasswordListener {
    void finished(CenterPopupView popupView, PasswordLayout passwordLayout, String password);
}
