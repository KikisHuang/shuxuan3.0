package com.gxdingo.sg.biz;

import com.gxdingo.sg.bean.StoreWalletBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

/**
 * 商家钱包契约类
 *
 * @author JM
 */
public class StoreWalletContract {

    public interface StoreWalletPresenter extends MvpPresenter<BasicsListener, StoreWalletListener> {
        void getWalletHome(boolean refresh);

        void cash(String password);

        void bind(String code,int type);

        void bindAli();

        void bindWechat();

        void goCashPage(int type);
    }

    public interface StoreWalletListener {

        int getBackCardId();

        void onWalletHomeResult(boolean refresh,StoreWalletBean walletBean);
    }
}
