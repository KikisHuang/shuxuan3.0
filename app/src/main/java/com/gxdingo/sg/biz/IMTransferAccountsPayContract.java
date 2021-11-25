package com.gxdingo.sg.biz;

import com.gxdingo.sg.bean.PayBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

/**
 * IM转账支付契约类
 *
 * @author JM
 */
public class IMTransferAccountsPayContract {


    public interface IMTransferAccountsPayPresenter extends MvpPresenter<BasicsListener, IMTransferAccountsPayListener> {
        /**
         * 发起转账
         *  @param mShareUuid
         * @param type
         * @param passwd
         * @param amount
         */
        void transfer(String mShareUuid, int type, String passwd, String amount);
    }

    public interface IMTransferAccountsPayListener {
        //转账详情
        void onSetTransferAccounts(PayBean.TransferAccountsDTO transferAccounts);
    }
}
