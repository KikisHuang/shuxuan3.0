package com.gxdingo.sg.activity;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.NumberUnreadCommentsBean;
import com.gxdingo.sg.bean.UserBean;
import com.gxdingo.sg.biz.MyConfirmListener;
import com.gxdingo.sg.biz.StoreMainContract;
import com.gxdingo.sg.dialog.SgConfirm2ButtonPopupView;
import com.gxdingo.sg.fragment.child.BusinessDistrictParentFragment;
import com.gxdingo.sg.fragment.store.StoreHomeFragment;
import com.gxdingo.sg.fragment.store.StoreMessageFragment;
import com.gxdingo.sg.fragment.store.StoreMyFragment;
import com.gxdingo.sg.fragment.store.StoreWalletFragment;
import com.gxdingo.sg.presenter.StoreMainPresenter;
import com.gxdingo.sg.utils.ImMessageUtils;
import com.gxdingo.sg.utils.ImServiceUtils;
import com.gxdingo.sg.utils.LocalConstant;
import com.gyf.immersionbar.ImmersionBar;
import com.kikis.commnlibrary.utils.MessageCountManager;
import com.gxdingo.sg.utils.ScreenListener;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.view.CircularRevealButton;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.bean.GoNoticePageEvent;
import com.kikis.commnlibrary.bean.ReLoginBean;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.kikis.commnlibrary.utils.BaseLogUtils;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

import static com.blankj.utilcode.util.AppUtils.registerAppStatusChangedListener;
import static com.gxdingo.sg.utils.ImServiceUtils.startImService;
import static com.gxdingo.sg.utils.LocalConstant.SHOW_BUSINESS_DISTRICT_UN_READ_DOT;
import static com.kikis.commnlibrary.utils.BadgerManger.resetBadger;
import static com.kikis.commnlibrary.utils.CommonUtils.goNotifySetting;
import static com.kikis.commnlibrary.utils.Constant.LOGOUT;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;
import static com.kikis.commnlibrary.utils.ScreenUtils.dp2px;

/**
 * Created by Kikis on 2021/4/6
 * ??????????????????
 */
public class StoreActivity extends BaseMvpActivity<StoreMainContract.StoreMainPresenter> implements ScreenListener.ScreenStateListener, Utils.OnAppStatusChangedListener, StoreMainContract.StoreMainListener {


    private List<Fragment> mFragmentList;

    @BindViews({R.id.crb_store_home_page_layout, R.id.crb_store_message_layout, R.id.crb_store_wallet, R.id.crb_store_business_district, R.id.crb_store_my})
    public List<CircularRevealButton> mMenuLayout;

    private long timeDValue = 0; // ?????????????????????????????????????????????

    private static StoreActivity instance;

    @BindView(R.id.tv_unread_msg_count)
    public TextView tv_unread_msg_count;

    @BindView(R.id.tv_business_unread_msg_count)
    public TextView tv_business_unread_msg_count;

    //????????????
    private ScreenListener screenListener;

    private Disposable mDisposable;

    public static StoreActivity getInstance() {
        return instance;
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
        return R.color.main_tone;
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
        return R.layout.module_activity_store;
    }

    @Override
    protected StoreMainContract.StoreMainPresenter createPresenter() {
        return new StoreMainPresenter();
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
        checkUserStatus();
        fragmentInit();
        getP().persenterInit();

        registerAppStatusChangedListener(this);

        registerAppStatusChangedListener(this);
        screenListener = new ScreenListener(reference.get());
        screenListener.begin(this);

        getP().checkNotifications();

    }

    BusinessDistrictParentFragment mStoreBusinessDistrictFragment;

    private void fragmentInit() {
        mStoreBusinessDistrictFragment = new BusinessDistrictParentFragment();
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new StoreHomeFragment());
        mFragmentList.add(new StoreMessageFragment());
        mFragmentList.add(new StoreWalletFragment());
        mFragmentList.add(mStoreBusinessDistrictFragment);
        mFragmentList.add(new StoreMyFragment());
    }

    @Override
    protected void onBaseCreate() {
        super.onBaseCreate();
        // ???????????????
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    @Override
    protected void initData() {
    }


    @Override
    protected void statusBarInit() {
        super.statusBarInit();
        mImmersionBar.statusBarDarkFont(true).init();
    }

    @OnClick({R.id.crb_store_home_page_layout, R.id.crb_store_message_layout, R.id.crb_store_wallet, R.id.crb_store_business_district, R.id.crb_store_my})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;
        switch (v.getId()) {
            case R.id.crb_store_home_page_layout:
                getP().checkTab(0);
                break;
            case R.id.crb_store_message_layout:
                getP().checkTab(1);
                break;
            case R.id.crb_store_wallet:
                getP().checkTab(2);
                break;
            case R.id.crb_store_business_district:
                getP().checkTab(3);
                break;
            case R.id.crb_store_my:
                getP().checkTab(4);
                break;
        }
    }

    @Override
    protected void onBaseEvent(Object object) {

        //?????????????????????
        if (object instanceof ReceiveIMMessageBean) {
            if (!isAcBackground)
                showNewMessageDialog((ReceiveIMMessageBean) object);

            setUnreadMsgNum(MessageCountManager.getInstance().getUnreadMessageNum());
        }

        if (object instanceof ReLoginBean)
            getP().logout();

        if (object instanceof GoNoticePageEvent) {
            GoNoticePageEvent event = (GoNoticePageEvent) object;
            goToPagePutSerializable(reference.get(), ChatActivity.class, getIntentEntityMap(new Object[]{event.id, event.type}));

        } else if (object instanceof ReceiveIMMessageBean.DataByType) {
            getP().getUnreadMessageNum();
        }


//        if (object.equals(StoreLocalConstant.NAVIGATION_ORDER))
//            getP().checkTab(1);
//        else if (object instanceof NewMessage){
//            NewMessage newMessage= (NewMessage) object;
//            if (newMessage.getOrder()!=null&&newMessage.getOrder().getStatus()==0)
//                getP().play();
//        }
    }


    @Override
    public void onStarts() {
        super.onStarts();
        /*if (AliPushMessageReceiver.count>0){
            AliPushMessageReceiver.count = 0;
            BadgeUtil.setBadge(0,this);
        }*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (UserInfoUtils.getInstance().isLogin() && UserInfoUtils.getInstance().getUserInfo().getStore().getStatus() == 10) {
            getP().getUnreadMessageNum();
            startImService();
        }
    }


    /**
     * ??????fragment??????
     */
    public List<Fragment> getFragmentList() {
        return mFragmentList;
    }

    @Override
    public FragmentTransaction getFragmentTransaction() {
        return getSupportFragmentManager()
                .beginTransaction();
    }

    /**
     * ??????fragment
     *
     * @param index
     */
    @Override
    public void showFragment(FragmentTransaction fragmentTransaction, int index) {

        fragmentTransaction.show(mFragmentList.get(index));
        //android x?????????
        fragmentTransaction.setMaxLifecycle(mFragmentList.get(index), Lifecycle.State.RESUMED);
        fragmentTransaction.commitAllowingStateLoss();

    }

    /**
     * ??????fragment
     *
     * @param index
     */
    @Override
    public void hideFragment(int index) {
        FragmentTransaction fragmentTransaction = getFragmentTransaction();

        fragmentTransaction.hide(mFragmentList.get(index));
        fragmentTransaction.setMaxLifecycle(mFragmentList.get(index), Lifecycle.State.STARTED);
        fragmentTransaction.commitAllowingStateLoss();
    }


    /**
     * ??????
     *
     * @param checkTab
     * @param oldTab
     */
    @Override
    public void onSeleted(int checkTab, int oldTab) {
        int color = 0;
        boolean statusBarDarkFont = false;
        switch (checkTab) {
            case 0:
                statusBarDarkFont = false;
                color = R.color.main_tone;
                break;
            case 1:
                statusBarDarkFont = false;
                color = R.color.green68aa50;
                break;
            case 2:
            case 3:
                statusBarDarkFont = true;
                color = R.color.white;
                break;
            case 4:
                statusBarDarkFont = true;
                color = R.color.grayf6;
                break;
        }
        ImmersionBar.with(this).statusBarDarkFont(statusBarDarkFont).statusBarColor(color).init();
        mMenuLayout.get(checkTab).setonSelected(true);
        mMenuLayout.get(oldTab).setonSelected(false);
    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type == LOGOUT) {
            setUnreadMsgNum(0);
            setBusinessUnreadMsgNum(null);
            finish();
        } else if (type == LocalConstant.LOGIN_SUCCEED) {
            finish();
        } else if (type == SHOW_BUSINESS_DISTRICT_UN_READ_DOT) {
            //????????????????????????
            getP().getUnreadMessageNum();
        }

//        if (type == STORE_LOGIN_SUCCEED) {//????????????
//            checkUserStatus();//??????????????????
//        } else if (type == LOGOUT) {//????????????
//            getP().destroySocket();
//        }
    }

    /**
     * ??????????????????
     */
    private void checkUserStatus() {
        if (!UserInfoUtils.getInstance().isLogin()) {
            getP().login();
//            UserInfoUtils.getInstance().goToLoginPage(reference.get(), "");
            finish();
            return;
        }

        getP().getAliKey();

        UserBean userBean = UserInfoUtils.getInstance().getUserInfo();
        //???store.id == 0???????????????????????????????????????store.status == 0???????????????????????????store.status == 20 ?????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (userBean.getStore().getId() == 0 || userBean.getStore().getStatus() == 0 || userBean.getStore().getStatus() == 20) {

            BaseLogUtils.e("Store status === " + userBean.getStore().getStatus());
            //???????????????????????????
            goToPage(reference.get(), StoreCertificationActivity.class, null);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - timeDValue) > 1500) {
                onMessage(getResources().getString(R.string.appfinish));
                timeDValue = System.currentTimeMillis();
                return true;
            } else {
                moveTaskToBack(true);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
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


    @Override
    public void onForeground(Activity activity) {
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
        getP().release();
        if (instance != null)
            instance = null;
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


    /**
     * ??????????????????????????????????????????
     */
    @Override
    public void showNotifyDialog() {
        SgConfirm2ButtonPopupView sgConfirm2ButtonPopupView = new SgConfirm2ButtonPopupView(reference.get(), "???????????????????????????????????????????????????????????????????????????????????????", new MyConfirmListener() {
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
}