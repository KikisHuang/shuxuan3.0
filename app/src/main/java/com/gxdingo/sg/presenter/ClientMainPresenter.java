package com.gxdingo.sg.presenter;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import com.alipay.sdk.app.OpenAuthTask;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.ClientStoreDetailsActivity;
import com.gxdingo.sg.bean.HelpBean;
import com.gxdingo.sg.bean.NumberUnreadCommentsBean;
import com.gxdingo.sg.biz.ClientMainContract;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.model.BusinessDistrictModel;
import com.gxdingo.sg.model.ClientMainModel;
import com.gxdingo.sg.model.ClientNetworkModel;
import com.gxdingo.sg.model.LoginModel;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.model.OneKeyModel;
import com.gxdingo.sg.model.ShibbolethModel;
import com.gxdingo.sg.model.WebSocketModel;
import com.gxdingo.sg.utils.ClientLocalConstant;
import com.gxdingo.sg.utils.LocalConstant;
import com.kikis.commnlibrary.utils.MessageCountManager;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.zhouyou.http.subsciber.BaseSubscriber;

import org.greenrobot.eventbus.EventBus;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.StringUtils.getString;
import static com.gxdingo.sg.utils.LocalConstant.BACK_TOP_BUSINESS_DISTRICT;
import static com.gxdingo.sg.utils.LocalConstant.BACK_TOP_MESSAGE_LIST;
import static com.gxdingo.sg.utils.LocalConstant.BACK_TOP_SHOP;
import static com.gxdingo.sg.utils.pay.AlipayTool.simpleAuth;
import static com.kikis.commnlibrary.utils.BadgerManger.resetBadger;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.CommonUtils.isNotificationEnabled;
import static com.kikis.commnlibrary.utils.CommonUtils.isWeixinAvilible;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientMainPresenter extends BaseMvpPresenter<BasicsListener, ClientMainContract.ClientMainListener> implements ClientMainContract.ClientMainPresenter, NetWorkListener {

    private ClientMainModel model;

    private NetworkModel networkModel;

    private OneKeyModel oneKeyModel;

    private WebSocketModel mWebSocketModel;

    private ClientNetworkModel clientNetworkModel;

    private LoginModel mModdel;

    private BusinessDistrictModel businessDistrictModel;

    private String helpCode;

    public ClientMainPresenter() {
        clientNetworkModel = new ClientNetworkModel(this);

        model = new ClientMainModel();
        networkModel = new NetworkModel(this);
        oneKeyModel = new OneKeyModel(this);
        mWebSocketModel = new WebSocketModel(this);
        mModdel = new LoginModel();
        businessDistrictModel = new BusinessDistrictModel(this);
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

        if (o instanceof HelpBean) {
            getV().onHelpDataResult((HelpBean) o);
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

    @Override
    public void onMvpDestroy() {
        super.onMvpDestroy();
        if (model != null)
            model = null;
    }

    @Override
    public void persenterInit() {
        if (model != null && isViewAttached()) {

            FragmentTransaction fragmentTransaction = getV().getFragmentTransaction();

            model.fragmentInit(fragmentTransaction, getV().getFragmentList());
            if (isViewAttached())
                getV().showFragment(fragmentTransaction, 0);
        }
    }

    private void showFm(int tab) {

        model.hideAllFragment(getV().getFragmentList().size(), getContext(), index -> {
            if (isViewAttached()) {
                getV().hideFragment(index);
            }

        });
        getV().showFragment(getV().getFragmentTransaction(), tab);

    }

    @Override
    public void checkTab(int tab) {
        if (model != null) {

            if (tab == 0 && model.getOldTab() == 0)
                EventBus.getDefault().post(BACK_TOP_BUSINESS_DISTRICT);
            if (tab == 1 && model.getOldTab() == 1)
                EventBus.getDefault().post(BACK_TOP_SHOP);
            if (tab == 3 && model.getOldTab() == 3)
                EventBus.getDefault().post(BACK_TOP_MESSAGE_LIST);

            if (isViewAttached() && tab != model.getOldTab()){
                getV().onSeleted(tab, model.getOldTab());
                showFm(tab);
                model.recordTab(tab);
            }

        }
    }

    @Override
    public void oneKeyLogin(String code) {
        if (networkModel != null)
            networkModel.oneClickLogin(getContext(), code);
    }

    @Override
    public void aliLogin() {
        if (networkModel != null)
            networkModel.getAliyPayAuthinfo(getContext(), str -> simpleAuth((Activity) getContext(), (String) str, callback));
    }

    @Override
    public void getWechatAuth() {
        if (!isViewAttached() || mModdel == null)
            return;
        if (isWeixinAvilible(getContext()))
            mModdel.wxLogin();
        else {
            if (isBViewAttached())
                getBV().onMessage(String.format(getString(R.string.uninstall_app), gets(R.string.wechat)));
        }
    }

    @Override
    public void wechatLogin(String code) {
        if (networkModel != null && isViewAttached()) {
            networkModel.thirdPartyLogin(getContext(), code, ClientLocalConstant.WECHAT);
        }
    }

    @Override
    public void goLogin() {
     /*   if (oneKeyModel != null)
            oneKeyModel.getKey(getContext(), this, (CustomResultListener<OneKeyLoginEvent>) event -> {
                if (networkModel != null)
                    networkModel.oneClickLogin(getContext(), event.code, event.isUser);
            });*/
        UserInfoUtils.getInstance().goToOauthPage(getContext());
    }


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
                if (networkModel != null) {
                    if (!isEmpty(authCode)) {
                        networkModel.thirdPartyLogin(getContext(), authCode, ClientLocalConstant.ALIPAY);
                    } else
                        onMessage("没有获取到authCode");
                }

                // 执行原有处理逻辑
            }
        }
    };

    @Override
    public void getSocketUrl() {

    }

    @Override
    public void destroySocket() {

    }

    @Override
    public void getAliKey() {
        if (oneKeyModel != null && UserInfoUtils.getInstance().isLogin())
            oneKeyModel.getKey(getContext());
    }

    /**
     * 获取未读消息数
     */
    @Override
    public void getUnreadMessageNum() {

        if (mWebSocketModel != null) {
            mWebSocketModel.getUnreadMessageNumber(getContext(), data -> {
                MessageCountManager.getInstance().setUnreadMessageNum((Integer) data);
                resetBadger(getContext());

                if (isViewAttached())
                    getV().setUnreadMsgNum((Integer) data);
            });
        }
        if (businessDistrictModel != null) {
            businessDistrictModel.getNumberUnreadComments(getContext(), objects -> {
                if (objects[0] instanceof NumberUnreadCommentsBean) {
                    /**
                     * 返回商圈评论未读数
                     */
                    NumberUnreadCommentsBean unreadCommentsBean = (NumberUnreadCommentsBean) objects[0];
                    if (isViewAttached())
                        getV().setBusinessUnreadMsgNum(unreadCommentsBean);
                }
            });
        }


    }

    /**
     * 监测通知栏是否开启
     */
    @Override
    public void checkNotifications() {

        if (SPUtils.getInstance().getBoolean(LocalConstant.NOTIFICATION_MANAGER_KEY, true) && !isNotificationEnabled(getContext()))
            getV().showNotifyDialog();
    }

    @Override
    public void checkHelpCode() {
        ShibbolethModel.checkShibboleth((type, code) -> {

            //30口令类型为邀请商家活动 40为分享跳转商圈
            if (type == 40) {
                //分享跳转商圈
                getV().goToBusinessDistrict(code);
            } else if (type == 50) {
                goToPagePutSerializable(getContext(), ClientStoreDetailsActivity.class, getIntentEntityMap(new Object[]{code}));
            } else if (type != 30) {
                if (UserInfoUtils.getInstance().isLogin()) {
                    helpCode = code;
                    if (clientNetworkModel != null)
                        clientNetworkModel.inviteHelp(getContext(), code);
                }
            }
        }, 200, false);

    }

    @Override
    public void help() {
        if (clientNetworkModel != null)
            clientNetworkModel.helpAfter(getContext(), helpCode);
    }

    @Override
    public void fllInvitationCode(String code) {
        if (StringUtils.isEmpty(code)) {
            onMessage("请填写商家邀请码");
            return;
        }
        if (clientNetworkModel != null)
            clientNetworkModel.receiveCoupon(getContext(), code);
    }
}
