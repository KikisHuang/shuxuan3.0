package com.gxdingo.sg.biz;

import com.gxdingo.sg.bean.ClientCouponBean;
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
         * @param mShareUuid
         * @param type
         * @param passwd
         * @param amount
         * @param couponBean
         * @param mTotalAmount
         */
        void transfer(String mShareUuid, int type, String passwd, String amount, ClientCouponBean couponBean, String mTotalAmount);

        /**
         * 获取优惠券列表
         * @param sendIdentifier
         */
        void getCoupons(String sendIdentifier);
    }

    public interface IMTransferAccountsPayListener {
        //转账详情
        void onSetTransferAccounts(PayBean.TransferAccountsDTO transferAccounts);

        //优惠券提示回调
        void setCouponsHint(String s);
    }
}
