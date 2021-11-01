package com.gxdingo.sg.biz;

import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

/**
 * 商家我的契约类
 * @author JM
 */
public class StoreMyContract {
    public interface StoreMyPresenter extends MvpPresenter<BasicsListener, StoreMyListener> {

    }

    public interface StoreMyListener {

    }
}
