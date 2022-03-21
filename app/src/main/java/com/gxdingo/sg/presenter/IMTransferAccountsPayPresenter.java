package com.gxdingo.sg.presenter;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.gxdingo.sg.activity.ClientSettingPayPwd1Activity;
import com.gxdingo.sg.bean.ClientCouponBean;
import com.gxdingo.sg.bean.ClientCouponsBean;
import com.gxdingo.sg.bean.PayBean;
import com.gxdingo.sg.biz.IMTransferAccountsPayContract;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.model.ClientNetworkModel;
import com.gxdingo.sg.model.WebSocketModel;
import com.gxdingo.sg.utils.pay.AlipayTool;
import com.gxdingo.sg.utils.pay.PayResult;
import com.gxdingo.sg.utils.pay.WeChatTool;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.kikis.commnlibrary.utils.Constant;
import com.zhouyou.http.subsciber.BaseSubscriber;

import org.greenrobot.eventbus.EventBus;

import static com.gxdingo.sg.utils.LocalConstant.SDK_PAY_FLAG;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;

public class IMTransferAccountsPayPresenter extends BaseMvpPresenter<BasicsListener, IMTransferAccountsPayContract.IMTransferAccountsPayListener> implements IMTransferAccountsPayContract.IMTransferAccountsPayPresenter, NetWorkListener {

    private WebSocketModel mWebSocketModel;

    private ClientNetworkModel clientNetworkModel;

    public IMTransferAccountsPayPresenter() {

        mWebSocketModel = new WebSocketModel(this);

        clientNetworkModel = new ClientNetworkModel(this);
    }

    @Override
    public void onSucceed(int type) {
        if (type == 601)
            goToPage(getContext(), ClientSettingPayPwd1Activity.class, null);

    }

    @Override
    public void onMessage(String msg) {
        if (isBViewAttached())
            getBV().onMessage(msg);
    }

    @Override
    public void noData() {
        if (isBViewAttached())
            getBV().noData();
    }

    @Override
    public void onData(boolean refresh, Object o) {

        if (o instanceof PayBean) {
            PayBean payBean = (PayBean) o;
            if (payBean.getAlipayInfo() != null) {

                if (isViewAttached())
                    getV().onSetTransferAccounts(payBean.getTransferAccounts());

                //支付宝支付
                AlipayTool alipayTool = new AlipayTool();

                alipayTool.pay((Activity) getContext(), payBean.getAlipayInfo(), mHandler);


            } else if (payBean.getWxpayInfo() != null) {

                if (isViewAttached())
                    getV().onSetTransferAccounts(payBean.getTransferAccounts());

                //微信支付
                WeChatTool wtool = new WeChatTool();
                wtool.sendPayReq(payBean.getWxpayInfo());


            } else {
                //余额支付
                if (isViewAttached())
                    getV().onSetTransferAccounts(payBean.getTransferAccounts());
                if (isBViewAttached())
                    getBV().onSucceed(0);
            }

        } else if (o instanceof ClientCouponsBean) {
            ClientCouponsBean ccb = (ClientCouponsBean) o;
            getV().setCouponsHint(ccb.getList() != null && ccb.getList().size() > 0?"选择优惠券":"暂无可用优惠券");

        }
    }

    @Override
    public void haveData() {
        if (isBViewAttached())
            getBV().haveData();
    }

    @Override
    public void finishLoadmoreWithNoMoreData() {
        if (isBViewAttached())
            getBV().finishLoadmoreWithNoMoreData();
    }

    @Override
    public void finishRefreshWithNoMoreData() {
        if (isBViewAttached())
            getBV().finishRefreshWithNoMoreData();
    }

    @Override
    public void onRequestComplete() {
        if (isBViewAttached())
            getBV().onRequestComplete();
    }

    @Override
    public void resetNoMoreData() {
        if (isBViewAttached())
            getBV().resetNoMoreData();
    }

    @Override
    public void finishRefresh(boolean success) {
        if (isBViewAttached())
            getBV().finishRefresh(success);
    }

    @Override
    public void finishLoadmore(boolean success) {
        if (isBViewAttached())
            getBV().finishLoadmore(success);
    }

    @Override
    public void onAfters() {
        if (isBViewAttached())
            getBV().onAfters();
    }

    @Override
    public void onStarts() {
        if (isBViewAttached())
            getBV().onStarts();
    }

    @Override
    public void onDisposable(BaseSubscriber subscriber) {
        addDisposable(subscriber);
    }

    /**
     * 发起转账
     * @param mShareUuid
     * @param type
     * @param passwd
     * @param amount
     * @param couponBean
     * @param mTotalAmount
     */
    @Override
    public void transfer(String mShareUuid, int type, String passwd, String amount, ClientCouponBean couponBean, String mTotalAmount) {

        if (mWebSocketModel != null)
            mWebSocketModel.transfer(getContext(), mShareUuid, type, passwd, amount,couponBean,mTotalAmount);

    }

    @Override
    public void getCoupons(String sendIdentifier) {
        if (clientNetworkModel != null)
            clientNetworkModel.getCoupons(getContext(), true,sendIdentifier);
    }

    /**
     * 支付宝支付回调
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = (PayResult) msg.obj;
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        if (isBViewAttached())
                            getBV().onSucceed(0);
                    } else if (TextUtils.equals(resultStatus, "8000")) {
                        //todo“ 8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        EventBus.getDefault().post(Constant.PAYMENT_SUCCESS);
                    } else if (!TextUtils.equals(resultStatus, "6001"))
                        onMessage("支付失败,状态码 " + resultStatus);
                    break;
                }

                default:
                    break;
            }
        }
    };

}
