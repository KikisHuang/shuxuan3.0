package com.gxdingo.sg.biz;

import com.gxdingo.sg.bean.StoreWalletBean;
import com.gxdingo.sg.bean.TransactionDetails;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;
import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * 商家钱包契约类
 *
 * @author JM
 */
public class StoreWalletContract {

    public interface StoreWalletPresenter extends MvpPresenter<BasicsListener, StoreWalletListener> {

        void getWalletHome(boolean refresh);

        void checkPermissions(RxPermissions rxPermissions);

        void scanCode(String couponIdentifier);

        void cash(String balance,String password);

        void bind(String code,int type);

        void bindAli();

        void bindWechat();

        void getTransactionDetails();
    }

    public interface StoreWalletListener {

        int getBackCardId();

        void onWalletHomeResult(boolean refresh,StoreWalletBean walletBean);

        String getCashType();

        void onTransactionDetail(TransactionDetails transactionDetails);
    }
}
