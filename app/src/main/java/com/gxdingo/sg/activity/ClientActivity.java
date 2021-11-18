package com.gxdingo.sg.activity;

import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import com.blankj.utilcode.util.SPUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.OneKeyLoginEvent;
import com.gxdingo.sg.bean.WeChatLoginEvent;
import com.gxdingo.sg.biz.ClientMainContract;
import com.gxdingo.sg.fragment.client.ClientHomeFragment;
import com.gxdingo.sg.fragment.client.ClientMessageFragment;
import com.gxdingo.sg.fragment.client.ClientMineFragment;
import com.gxdingo.sg.fragment.store.StoreBusinessDistrictFragment;
import com.gxdingo.sg.presenter.ClientMainPresenter;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.MessageCountUtils;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.view.CircularRevealButton;
import com.gyf.immersionbar.ImmersionBar;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.bean.GoNoticePageEvent;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import q.rorbin.badgeview.QBadgeView;

import static android.text.TextUtils.isEmpty;
import static com.gxdingo.sg.utils.LocalConstant.CLIENT_LOGIN_SUCCEED;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.Constant.LOGOUT;
import static com.gxdingo.sg.utils.LocalConstant.LOGIN_WAY;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientActivity extends BaseMvpActivity<ClientMainContract.ClientMainPresenter> implements ClientMainContract.ClientMainListener {

    private List<Fragment> mFragmentList;

    @BindViews({R.id.home_page_layout, R.id.message_layout, R.id.business_layout, R.id.mine_layout})
    public List<CircularRevealButton> mMenuLayout;

    @BindView(R.id.msg_fl)
    public FrameLayout msg_fl;

    @BindView(R.id.business_fl)
    public FrameLayout business_fl;

    private long timeDValue = 0; // 计算时间差值，判断是否需要退出

    private boolean showLogin = true;

    private static ClientActivity instance;
    StoreBusinessDistrictFragment mStoreBusinessDistrictFragment;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (!UserInfoUtils.getInstance().isLogin()&&showLogin){
//            goToPage(this, LoginActivity.class,null);
//            showLogin = !showLogin;
//        }

        if (UserInfoUtils.getInstance().isLogin())
            getP().getUnreadMessageNum();

        //商家已登录则跳转到商家主界面
        if (UserInfoUtils.getInstance().isLogin()) {
            boolean isUse = SPUtils.getInstance().getBoolean(LOGIN_WAY);
            if (!isUse) {
                goToPage(this, StoreActivity.class, null);
                finish();
            }
        }
    }

    /**
     * 重写findViewById(int)，让依附的Fragment中使用PopupView弹出窗口能添加使用Fragment
     *
     * @param id
     * @return
     */
    @Override
    public View findViewById(int id) {
        if (id == R.id.rl_child_function_menu_layout && mStoreBusinessDistrictFragment != null) {
            return mStoreBusinessDistrictFragment.getCommentInputBoxPopupView().findViewById(R.id.rl_child_function_menu_layout);
        }
        return super.findViewById(id);
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

            setUnreadMsgNum(MessageCountUtils.getInstance().getUnreadMessageNum());
        }


        if (object instanceof OneKeyLoginEvent) {
            getP().oneKeyLogin(((OneKeyLoginEvent) object).code, ((OneKeyLoginEvent) object).isUser);
        } else if (object instanceof WeChatLoginEvent) {
            WeChatLoginEvent event = (WeChatLoginEvent) object;
            if (!isEmpty(event.code) && event.login)
                getP().wechatLogin(event.code);
        } else if (object instanceof GoNoticePageEvent) {
            GoNoticePageEvent event = (GoNoticePageEvent) object;
            goToPagePutSerializable(reference.get(), ChatActivity.class, getIntentEntityMap(new Object[]{event.id, event.type}));
        }
    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type == LocalConstant.ALIPAY_LOGIN_EVENT) {
            getP().aliLogin();
        } else if (type == LocalConstant.WECHAT_LOGIN_EVENT) {
            LocalConstant.isLogin = true;
            getP().getWechatAuth();
        } else if (type == LOGOUT) {
            ImmersionBar.with(this).statusBarDarkFont(false).statusBarColor(R.color.main_tone).init();
            getP().checkTab(0);
        } else if (type == LocalConstant.STORE_LOGIN_SUCCEED) {
            finish();
        } else if (type == CLIENT_LOGIN_SUCCEED) {
            getP().getUnreadMessageNum();
        }
    }

    @Override
    protected void initData() {


    }

    private void fragmentInit() {
        mStoreBusinessDistrictFragment = new StoreBusinessDistrictFragment();
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new ClientHomeFragment());
        mFragmentList.add(new ClientMessageFragment());
        mFragmentList.add(mStoreBusinessDistrictFragment);
        mFragmentList.add(new ClientMineFragment());
    }


    @OnClick({R.id.home_page_layout, R.id.message_layout, R.id.settle_in, R.id.business_layout, R.id.mine_layout})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;

        if (v.getId() != R.id.home_page_layout && !UserInfoUtils.getInstance().isLogin()) {
            getP().goLogin();
            return;
        }

        switch (v.getId()) {
            case R.id.home_page_layout:
                ImmersionBar.with(this).statusBarDarkFont(false).statusBarColor(R.color.main_tone).init();
                getP().checkTab(0);
                break;
            case R.id.message_layout:
                ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).statusBarColor(R.color.white).init();
                getP().checkTab(1);
                break;
            case R.id.settle_in:
//                getP().checkTab(2);
//                ToastUtils.showLong("hi!索嗨，来入驻");
                goToPage(this, ClientSettleActivity.class, null);
                break;
            case R.id.business_layout:
                ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).statusBarColor(R.color.white).init();
                getP().checkTab(2);
                break;
            case R.id.mine_layout:
                ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).statusBarColor(R.color.white).init();
                getP().checkTab(3);
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
        new QBadgeView(reference.get()).setShowShadow(false).bindTarget(msg_fl).setBadgeBackgroundColor(getc(R.color.msg_dot_red)).setGravityOffset(18, -2, true).setBadgeNumber(data);
    }

    @Override
    public void setBusinessUnreadMsgNum(int num) {
        new QBadgeView(reference.get()).setShowShadow(false).bindTarget(business_fl).setBadgeBackgroundColor(getc(R.color.msg_dot_red)).setGravityOffset(18, -2, true).setBadgeNumber(num);
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
    protected void onDestroy() {
        super.onDestroy();
        if (instance != null)
            instance = null;
    }
}
