package com.gxdingo.sg.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.alipay.sdk.app.OpenAuthTask;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.StoreCashActivity;
import com.gxdingo.sg.bean.AuthResult;
import com.gxdingo.sg.bean.NormalBean;
import com.gxdingo.sg.bean.StoreWalletBean;
import com.gxdingo.sg.bean.ThirdPartyBean;
import com.gxdingo.sg.bean.TransactionDetails;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.PermissionsListener;
import com.gxdingo.sg.biz.StoreWalletContract;
import com.gxdingo.sg.model.CommonModel;
import com.gxdingo.sg.model.LoginModel;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.model.StoreNetworkModel;
import com.gxdingo.sg.utils.ClientLocalConstant;
import com.gxdingo.sg.utils.LocalConstant;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.kikis.commnlibrary.utils.Constant;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.commonsdk.debug.E;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.zhouyou.http.subsciber.BaseSubscriber;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission_group.STORAGE;
import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.StringUtils.getString;
import static com.gxdingo.sg.utils.LocalConstant.SDK_AUTH_FLAG;
import static com.gxdingo.sg.utils.pay.AlipayTool.auth;
import static com.gxdingo.sg.utils.pay.AlipayTool.simpleAuth;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.CommonUtils.isWeixinAvilible;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * 商家钱包控制器
 *
 * @author JM
 */
public class StoreWalletPresenter extends BaseMvpPresenter<BasicsListener, StoreWalletContract.StoreWalletListener> implements StoreWalletContract
        .StoreWalletPresenter, NetWorkListener {

    private NetworkModel mNetworkModel;

    private StoreNetworkModel storeNetworkModel;

    private LoginModel mModdel;

    private CommonModel commonModel;
//    private StoreWalletBean walletBean;


    public StoreWalletPresenter() {
        mNetworkModel = new NetworkModel(this);
        storeNetworkModel = new StoreNetworkModel(this);
        mModdel = new LoginModel();
        commonModel = new CommonModel();
    }

    @Override
    public void getWalletHome(boolean refresh) {
        if (storeNetworkModel != null)
            storeNetworkModel.getWalletHome(getContext(), refresh);
    }

    @Override
    public void checkPermissions(RxPermissions rxPermissions) {
        if (commonModel != null)
            commonModel.checkPermission(rxPermissions, new String[]{CAMERA, VIBRATE}, new PermissionsListener() {
                @Override
                public void onNext(boolean value) {
                    if (!value) {
                        getBV().onFailed();
                    } else {
                        getBV().onSucceed(1);
                    }
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
    }

    @Override
    public void scanCode(String couponIdentifier) {
        if (storeNetworkModel != null)
            storeNetworkModel.scanCode(getContext(), couponIdentifier);
    }

//    @Override
//    public void checkStoragePermissions(RxPermissions rxPermissions) {
//        if (commonModel!=null)
//            commonModel.checkPermission(rxPermissions, new String[]{STORAGE}, new PermissionsListener() {
//                @Override
//                public void onNext(boolean value) {
//                    if (!value){
//                        getBV().onFailed();
//                    }else {
//                        getBV().onSucceed(2);
//                    }
//                }
//
//                @Override
//                public void onError(Throwable e) {
//
//                }
//
//                @Override
//                public void onComplete() {
//
//                }
//            });
//
//    }

    @Override
    public void cash(String balance, String password) {
        if (storeNetworkModel != null)
            storeNetworkModel.balanceCash(getContext(), getV().getCashType(), balance, password, getV().getBackCardId());
    }

    @Override
    public void bind(String code, int type) {
        if (storeNetworkModel != null)
            storeNetworkModel.bindThirdParty(getContext(), code, type);
    }

    @Override
    public void bindAli() {
        mNetworkModel.getAliyPayAuthinfo(getContext(), str -> {
            auth((Activity) getContext(), (String) str, handler);
//            simpleAuth((Activity) getContext(), (String) str, callback);
        });
    }

    @Override
    public void bindWechat() {
        if (!isViewAttached() || mModdel == null)
            return;
        if (isWeixinAvilible(getContext())) {
            LocalConstant.isLogin = false;
            mModdel.wxLogin();
        } else {
            if (isBViewAttached())
                getBV().onMessage(String.format(getString(R.string.uninstall_app), gets(R.string.wechat)));
        }
    }

    @Override
    public void getTransactionDetails() {
        if (storeNetworkModel != null)
            storeNetworkModel.getTransactionDetail(getContext(), getV().getBackCardId());
    }

    /**
     * 获取扫码核销优惠券弹窗内容
     */
    @Override
    public void getNoRemindContent() {

        if (storeNetworkModel != null)
            storeNetworkModel.getScanningInfo(getContext());

    }


    @Override
    public void onSucceed(int type) {
        if (isBViewAttached())
            getBV().onSucceed(type);
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
        if (!isViewAttached())
            return;

        if (o instanceof StoreWalletBean)
            getV().onWalletHomeResult(refresh, (StoreWalletBean) o);
         else if (o instanceof TransactionDetails)
                getV().onTransactionDetail((TransactionDetails) o);
         else if (o instanceof NormalBean)
            //获取扫码核销优惠券弹窗内容回调
            getV().onRemindResult( ((NormalBean) o).remindValue);

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
            getBV().onAfters();
    }

    @Override
    public void onDisposable(BaseSubscriber subscriber) {
        addDisposable(subscriber);
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
            switch (msg.what) {
                case SDK_AUTH_FLAG:
                    AuthResult authResult = (AuthResult) msg.obj;
                    if (storeNetworkModel != null) {
                        if (!isEmpty(authResult.getAuthCode())) {
                            storeNetworkModel.bindThirdParty(getContext(), authResult.getAuthCode(), 0);
                        } else
                            onMessage("没有获取到authCode");
                    }
                    break;
            }
        }
    };
    /**
     * 支付宝sdk结果回调，主线程中执行
     * 为了避免内存泄漏，支付宝sdk内不强引用该回调
     * 请持有该callback引用，以免callback被回收导致无法获取业务结果
     */
    private OpenAuthTask.Callback callback = new OpenAuthTask.Callback() {
        @Override
        public void onResult(int i, String s, Bundle bundle) {
            if (i == OpenAuthTask.OK) {

                String authCode = bundle.getString("auth_code");
                if (storeNetworkModel != null) {
                    if (!isEmpty(authCode)) {
                        storeNetworkModel.bindThirdParty(getContext(), authCode, 0);
                    } else
                        onMessage("没有获取到authCode");
                }

                // 执行原有处理逻辑
            }
        }
    };
}
