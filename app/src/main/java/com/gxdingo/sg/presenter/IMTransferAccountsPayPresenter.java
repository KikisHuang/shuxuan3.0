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

                //???????????????
                AlipayTool alipayTool = new AlipayTool();

                alipayTool.pay((Activity) getContext(), payBean.getAlipayInfo(), mHandler);


            } else if (payBean.getWxpayInfo() != null) {

                if (isViewAttached())
                    getV().onSetTransferAccounts(payBean.getTransferAccounts());

                //????????????
                WeChatTool wtool = new WeChatTool();
                wtool.sendPayReq(payBean.getWxpayInfo());


            } else {
                //????????????
                if (isViewAttached())
                    getV().onSetTransferAccounts(payBean.getTransferAccounts());
                if (isBViewAttached())
                    getBV().onSucceed(0);
            }

        } else if (o instanceof ClientCouponsBean) {
            ClientCouponsBean ccb = (ClientCouponsBean) o;
            getV().setCouponsHint(ccb.getList() != null && ccb.getList().size() > 0?"???????????????":"?????????????????????");

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
     * ????????????
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
     * ?????????????????????
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = (PayResult) msg.obj;
                    /**
                     * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                     */
                    String resultInfo = payResult.getResult();// ?????????????????????????????????
                    String resultStatus = payResult.getResultStatus();

                    // ??????resultStatus ???9000?????????????????????
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // ??????????????????????????????????????????????????????????????????????????????
                        if (isBViewAttached())
                            getBV().onSucceed(0);
                    } else if (TextUtils.equals(resultStatus, "8000")) {
                        //todo??? 8000???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                        EventBus.getDefault().post(Constant.PAYMENT_SUCCESS);
                    } else if (!TextUtils.equals(resultStatus, "6001"))
                        onMessage("????????????,????????? " + resultStatus);
                    break;
                }

                default:
                    break;
            }
        }
    };

}
