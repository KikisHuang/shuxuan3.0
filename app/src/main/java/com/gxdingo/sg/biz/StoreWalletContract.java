package com.gxdingo.sg.biz;

import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

/**
 * 商家钱包契约类
 *
 * @author JM
 */
public class StoreWalletContract {

    public interface StoreWalletPresenter extends MvpPresenter<BasicsListener, StoreWalletListener> {

    }

    public interface StoreWalletListener {

    }
}
