package com.gxdingo.sg.activity;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ActivityEvent;
import com.gxdingo.sg.bean.HelpBean;
import com.gxdingo.sg.bean.NumberUnreadCommentsBean;
import com.gxdingo.sg.bean.OneKeyLoginEvent;
import com.gxdingo.sg.bean.ShareEvent;
import com.gxdingo.sg.bean.UserReward;
import com.gxdingo.sg.biz.ClientMainContract;
import com.gxdingo.sg.biz.MyConfirmListener;
import com.gxdingo.sg.dialog.HelpPopupView;
import com.gxdingo.sg.dialog.SgConfirm2ButtonPopupView;
import com.gxdingo.sg.dialog.SgConfirmHintPopupView;
import com.gxdingo.sg.fragment.client.ClientHomeFragment;
import com.gxdingo.sg.fragment.client.ClientMessageFragment;
import com.gxdingo.sg.fragment.client.ClientMineFragment;
import com.gxdingo.sg.fragment.client.SettledFragment;
import com.gxdingo.sg.fragment.child.BusinessDistrictParentFragment;
import com.gxdingo.sg.presenter.ClientMainPresenter;
import com.gxdingo.sg.utils.ClientLocalConstant;
import com.gxdingo.sg.utils.ImMessageUtils;
import com.gxdingo.sg.utils.ImServiceUtils;
import com.gxdingo.sg.utils.LocalConstant;
import com.kikis.commnlibrary.utils.MessageCountManager;
import com.gxdingo.sg.utils.ScreenListener;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.view.CircularRevealButton;
import com.gyf.immersionbar.ImmersionBar;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.bean.GoNoticePageEvent;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.kikis.commnlibrary.utils.ScreenUtils;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

import static com.blankj.utilcode.util.AppUtils.registerAppStatusChangedListener;
import static com.gxdingo.sg.utils.ImServiceUtils.startImService;
import static com.gxdingo.sg.utils.LocalConstant.FIRST_INTER_KEY;
import static com.gxdingo.sg.utils.LocalConstant.GO_STORE_LIST_PAGE;
import static com.gxdingo.sg.utils.LocalConstant.LOGIN_SUCCEED;
import static com.gxdingo.sg.utils.LocalConstant.GO_SETTLED;
import static com.gxdingo.sg.utils.LocalConstant.SHOW_BUSINESS_DISTRICT_UN_READ_DOT;
import static com.kikis.commnlibrary.utils.BadgerManger.resetBadger;
import static com.kikis.commnlibrary.utils.CommonUtils.goNotifySetting;
import static com.kikis.commnlibrary.utils.Constant.LOGOUT;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;
import static com.kikis.commnlibrary.utils.ScreenUtils.dp2px;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientActivity extends BaseMvpActivity<ClientMainContract.ClientMainPresenter> implements ScreenListener.ScreenStateListener, Utils.OnAppStatusChangedListener, ClientMainContract.ClientMainListener {

    private List<Fragment> mFragmentList;

    @BindViews({R.id.business_layout, R.id.home_page_layout, R.id.settle_in, R.id.message_layout, R.id.mine_layout})
    public List<CircularRevealButton> mMenuLayout;

    @BindView(R.id.msg_fl)
    public FrameLayout msg_fl;

    @BindView(R.id.business_fl)
    public FrameLayout business_fl;

    @BindView(R.id.tv_unread_msg_count)
    public TextView tv_unread_msg_count;

    @BindView(R.id.tv_business_unread_msg_count)
    public TextView tv_business_unread_msg_count;

    private long timeDValue = 0; // 计算时间差值，判断是否需要退出

    private static ClientActivity instance;
    //屏幕监听
    private ScreenListener screenListener;

    //刷新商圈定时器disposable
    private Disposable mDisposable;

    public static ClientActivity getInstance() {
        return instance;
    }

    @Override
    protected ClientMainContract.ClientMainPresenter createPresenter() {
        return new ClientMainPresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return true;
    }

    @Override
    protected int activityTitleLayout() {
        return 0;
    }

    @Override
    protected boolean ImmersionBar() {
        return true;
    }

    @Override
    protected int StatusBarColors() {
        return R.color.grayf6;
    }

    @Override
    protected int NavigationBarColor() {
        return 0;
    }

    @Override
    protected int activityBottomLayout() {
        return 0;
    }

    @Override
    protected View noDataLayout() {
        return null;
    }

    @Override
    protected View refreshLayout() {
        return null;
    }

    @Override
    protected boolean closeDispatchTouchEvent() {
        return false;
    }

    @Override
    protected int initContentView() {
        return R.layout.module_activity_client_main;
    }

    @Override
    protected boolean refreshEnable() {
        return false;
    }

    @Override
    protected boolean loadmoreEnable() {
        return false;
    }

    @Override
    protected void init() {
        instance = this;
        fragmentInit();
        getP().persenterInit();
        getP().getAliKey();
        registerAppStatusChangedListener(this);
        screenListener = new ScreenListener(reference.get());
        screenListener.begin(this);

        getP().checkNotifications();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getP().checkHelpCode();
//        if (!UserInfoUtils.getInstance().isLogin()&&showLogin){
//            goToPage(this, LoginActivity.class,null);
//            showLogin = !showLogin;
//        }
    /*    if (AliPushMessageReceiver.count>0){
            AliPushMessageReceiver.count = 0;
            BadgeUtil.setBadge(0,this);
        }*/
        if (UserInfoUtils.getInstance().isLogin()) {
            getP().getUnreadMessageNum();
            startImService();
        }
    }

    @Override
    public void onSucceed(int type) {
        super.onSucceed(type);

        if (type == ClientLocalConstant.FILL_SUCCESS) {
            SPUtils.getInstance().put(FIRST_INTER_KEY, false);
        }
    }

    @Override
    protected void onBaseCreate() {
        super.onBaseCreate();
        // 显示状态栏
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onBaseEvent(Object object) {

        //全局新消息布局
        if (object instanceof ReceiveIMMessageBean) {
            if (!isAcBackground)
                showNewMessageDialog((ReceiveIMMessageBean) object);

            setUnreadMsgNum(MessageCountManager.getInstance().getUnreadMessageNum());
        }

        if (object instanceof OneKeyLoginEvent) {
            getP().oneKeyLogin(((OneKeyLoginEvent) object).code);
        }/* else if (object instanceof WeChatLoginEvent) {
            WeChatLoginEvent event = (WeChatLoginEvent) object;
            if (!isEmpty(event.code) && event.login)
                getP().wechatLogin(event.code);
        }*/ else if (object instanceof GoNoticePageEvent) {
            GoNoticePageEvent event = (GoNoticePageEvent) object;
            goToPagePutSerializable(reference.get(), ChatActivity.class, getIntentEntityMap(new Object[]{event.id, event.type}));
        } else if (object instanceof ReceiveIMMessageBean.DataByType) {
            //商圈未读评论类型事件
            getP().getUnreadMessageNum();
        } else if (object instanceof ActivityEvent) {
            //活动事件
            ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).statusBarColor(R.color.white).init();
            getP().checkTab(0);
        }
    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type == LocalConstant.ALIPAY_LOGIN_EVENT) {
            getP().aliLogin();
        }/* else if (type == LocalConstant.WECHAT_LOGIN_EVENT) {
            LocalConstant.isLogin = true;
            getP().getWechatAuth();
        }*/ else if (type == LOGOUT) {
            setUnreadMsgNum(0);
            setBusinessUnreadMsgNum(null);
            ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).statusBarColor(R.color.grayf6).init();
            getP().checkTab(0);
        } else if (type == LOGIN_SUCCEED) {
            if (UserInfoUtils.getInstance().getUserInfo().getIsFirstLogin() == 1 && UserInfoUtils.getInstance().getUserInfo().userReward != null && UserInfoUtils.getInstance().getUserInfo().userReward.getCouponList().size() > 0) {
                List<UserReward.CouponListDTO> couponList = UserInfoUtils.getInstance().getUserInfo().userReward.getCouponList();
                showAwardDialog(couponList);
            }
            getP().getUnreadMessageNum();
        } else if (type == SHOW_BUSINESS_DISTRICT_UN_READ_DOT) {
            //商圈有未读消息数
            getP().getUnreadMessageNum();
        } else if (type == GO_SETTLED) {
            getP().checkTab(2);
        } else if (type == GO_STORE_LIST_PAGE) {
            getP().checkTab(1);
        }
    }


    @Override
    protected void initData() {
    }

    private void fragmentInit() {

        mFragmentList = new ArrayList<>();
        mFragmentList.add(new BusinessDistrictParentFragment());
        mFragmentList.add(new ClientHomeFragment());
        mFragmentList.add(new SettledFragment());
        mFragmentList.add(new ClientMessageFragment());
        mFragmentList.add(new ClientMineFragment());
    }

    private void showAwardDialog(List<UserReward.CouponListDTO> userReward) {
        //todo 等待增加 list position 1的数据
        new XPopup.Builder(reference.get())
                .maxWidth((ScreenUtils.getScreenWidth(reference.get())))
                .isDarkTheme(false)
                .asCustom(new SgConfirmHintPopupView(reference.get(), userReward.get(0).getName(), userReward.get(0).getRemark(), "领取", 0)).show();

    }

    @OnClick({R.id.home_page_layout, R.id.message_layout, R.id.settle_in, R.id.business_layout, R.id.mine_layout})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;

        if ((v.getId() == R.id.message_layout || v.getId() == R.id.settle_in || v.getId() == R.id.mine_layout) && !UserInfoUtils.getInstance().isLogin()) {
            getP().goLogin();
            return;
        }

        if (v.getId() == R.id.mine_layout)
            ImmersionBar.with(reference.get()).init();
        else
            ImmersionBar.with(reference.get()).statusBarDarkFont(true, 0.2f).statusBarColor(v.getId()==R.id.settle_in ? R.color.white : R.color.grayf5).init();


        switch (v.getId()) {
            case R.id.home_page_layout:
                getP().checkTab(1);
                break;
            case R.id.message_layout:

                getP().checkTab(3);
                break;
            case R.id.settle_in:
                getP().checkTab(2);
                break;
            case R.id.business_layout:
                getP().checkTab(0);
                break;
            case R.id.mine_layout:
                getP().checkTab(4);
                break;
        }
    }

    @Override
    public List<Fragment> getFragmentList() {
        return mFragmentList;
    }

    @Override
    public FragmentTransaction getFragmentTransaction() {
        return getSupportFragmentManager()
                .beginTransaction();
    }

    @Override
    public void showFragment(FragmentTransaction fragmentTransaction, int index) {

        fragmentTransaction.show(mFragmentList.get(index));
        //android x懒加载
        fragmentTransaction.setMaxLifecycle(mFragmentList.get(index), Lifecycle.State.RESUMED);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void hideFragment(int index) {
        FragmentTransaction fragmentTransaction = getFragmentTransaction();

        fragmentTransaction.hide(mFragmentList.get(index));
        //android x懒加载
        fragmentTransaction.setMaxLifecycle(mFragmentList.get(index), Lifecycle.State.STARTED);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onSeleted(int checkTab, int oldTab) {

        mMenuLayout.get(checkTab).setonSelected(true);

        mMenuLayout.get(oldTab).setonSelected(false);

    }

    @Override
    public void setUnreadMsgNum(int data) {
        tv_unread_msg_count.setText(data > 99 ? "99" : "" + data);
        tv_unread_msg_count.setVisibility(data <= 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setBusinessUnreadMsgNum(NumberUnreadCommentsBean data) {
        if (data == null) {
            tv_business_unread_msg_count.setVisibility(View.GONE);
            return;
        }

        if (data.getUnread() > 0) {
            tv_business_unread_msg_count.getLayoutParams().width = dp2px(16);
            tv_business_unread_msg_count.getLayoutParams().height = dp2px(16);
            tv_business_unread_msg_count.setText(data.getUnread() > 99 ? "99" : "" + data.getUnread());
            tv_business_unread_msg_count.setVisibility(data.getUnread() <= 0 ? View.GONE : View.VISIBLE);
        } else {

            tv_business_unread_msg_count.getLayoutParams().width = dp2px(10);
            tv_business_unread_msg_count.getLayoutParams().height = dp2px(10);
            tv_business_unread_msg_count.setText("");
            tv_business_unread_msg_count.setVisibility(data.getCircleUnread() <= 0 ? View.GONE : View.VISIBLE);

        }
    }

    /**
     * 显示提示用户未开启通知栏弹窗
     */
    @Override
    public void showNotifyDialog() {
        SgConfirm2ButtonPopupView sgConfirm2ButtonPopupView = new SgConfirm2ButtonPopupView(reference.get(), "检测到您未开启通知栏权限，消息无法准确推送到，是否去开启？", new MyConfirmListener() {
            @Override
            public void onConfirm() {
                goNotifySetting(reference.get());
                SPUtils.getInstance().put(LocalConstant.NOTIFICATION_MANAGER_KEY, false);
            }
        });
        sgConfirm2ButtonPopupView.setCancelCilcikListener(v -> {
            SPUtils.getInstance().put(LocalConstant.NOTIFICATION_MANAGER_KEY, false);
        });
        new XPopup.Builder(reference.get())
                .isDarkTheme(false)
                .asCustom(sgConfirm2ButtonPopupView).show();
    }

    @Override
    public void onHelpDataResult(HelpBean helpBean) {
        new XPopup.Builder(reference.get())
                .maxWidth((int) (ScreenUtils.getScreenWidth(reference.get())))
                .isDarkTheme(false)
                .asCustom(new HelpPopupView(reference.get(), helpBean, () -> getP().help()))
                .show();
    }


    @Override
    public void goToBusinessDistrict(String code) {
        getP().checkTab(0);
        sendEvent(new ShareEvent(code));
    }

    @Override
    public void showContinuousLoginAwardDialog(List<UserReward.CouponListDTO> popupCouponList) {
        //todo 待测试连续登录弹窗
        showAwardDialog(popupCouponList);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 判断退出
            if (timeDValue == 0) {
                onMessage(getResources().getString(R.string.appfinish));
                timeDValue = System.currentTimeMillis();
                return true;
            } else {
                timeDValue = System.currentTimeMillis() - timeDValue;
                if (timeDValue >= 1500) { // 大于1.5秒不处理。
                    timeDValue = 0;
                    return true;
                } else {
                    //finish();
                    moveTaskToBack(false);
                }

            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        resetBadger(reference.get());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (instance != null)
            instance = null;

        if (screenListener != null)
            screenListener.unregisterListener();
    }

    @Override
    public void onForeground(Activity activity) {
        //前后台切换刷新定位、附近商家接口。
        sendEvent(LocalConstant.REFRESH_LOCATION);
        LocalConstant.isBackground = false;
    }

    @Override
    public void onBackground(Activity activity) {
        LocalConstant.isBackground = true;

        if (ImMessageUtils.getInstance().isRunning())
            ImServiceUtils.stopImService();
    }

    @Override
    public void onScreenOn() {

    }

    @Override
    public void onScreenOff() {
        if (ImMessageUtils.getInstance().isRunning())
            ImServiceUtils.stopImService();
    }

    @Override
    public void onUserPresent() {

    }
}
