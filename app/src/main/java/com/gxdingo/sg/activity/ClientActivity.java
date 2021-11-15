package com.gxdingo.sg.activity;

import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.OneKeyLoginEvent;
import com.gxdingo.sg.bean.WeChatLoginEvent;
import com.gxdingo.sg.biz.ClientMainContract;
import com.gxdingo.sg.fragment.client.ClientBusinessDistrictFragment;
import com.gxdingo.sg.fragment.client.ClientHomeFragment;
import com.gxdingo.sg.fragment.client.ClientMessageFragment;
import com.gxdingo.sg.fragment.client.ClientMineFragment;
import com.gxdingo.sg.fragment.store.StoreBusinessDistrictFragment;
import com.gxdingo.sg.presenter.ClientMainPresenter;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.view.CircularRevealButton;
import com.gyf.immersionbar.ImmersionBar;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.bean.GoNoticePageEvent;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;
import static com.kikis.commnlibrary.utils.Constant.LOGOUT;
import static com.gxdingo.sg.utils.LocalConstant.LOGIN_WAY;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentMap;
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

    private long timeDValue = 0; // 计算时间差值，判断是否需要退出

    private boolean showLogin = true;

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
        fragmentInit();
        getP().persenterInit();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (!UserInfoUtils.getInstance().isLogin()&&showLogin){
//            goToPage(this, LoginActivity.class,null);
//            showLogin = !showLogin;
//        }

        //商家已登录则跳转到商家主界面
        if (UserInfoUtils.getInstance().isLogin()) {
            boolean isUse = SPUtils.getInstance().getBoolean(LOGIN_WAY);
            if (!isUse) {
                goToPage(this, StoreActivity.class, null);
                finish();
            }
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
        if (!isAcBackground && object instanceof ReceiveIMMessageBean)
            showNewMessageDialog((ReceiveIMMessageBean) object);

        if (object instanceof OneKeyLoginEvent) {
            getP().oneKeyLogin(((OneKeyLoginEvent) object).code);
        } else if (object instanceof WeChatLoginEvent) {
            WeChatLoginEvent event = (WeChatLoginEvent) object;
            if (!isEmpty(event.code) && event.login)
                getP().wechatLogin(event.code);
        } else if (object instanceof GoNoticePageEvent) {
            GoNoticePageEvent event = (GoNoticePageEvent) object;
            goToPagePutSerializable(reference.get(), ChatActivity.class, getIntentEntityMap(new Object[]{event.id}));
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
        }
    }

    @Override
    protected void initData() {

    }

    private void fragmentInit() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new ClientHomeFragment());
        mFragmentList.add(new ClientMessageFragment());
        mFragmentList.add(new StoreBusinessDistrictFragment());
        mFragmentList.add(new ClientMineFragment());
    }


    @OnClick({R.id.home_page_layout, R.id.message_layout, R.id.settle_in, R.id.business_layout, R.id.mine_layout})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;
        switch (v.getId()) {
            case R.id.home_page_layout:
                ImmersionBar.with(this).statusBarDarkFont(false).statusBarColor(R.color.main_tone).init();
                getP().checkTab(0);
                break;
            case R.id.message_layout:
                if (!UserInfoUtils.getInstance().isLogin()) {
                    getP().goLogin();
                    return;
                }
                ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).statusBarColor(R.color.white).init();
                getP().checkTab(1);
                break;
            case R.id.settle_in:
//                getP().checkTab(2);
//                ToastUtils.showLong("hi!索嗨，来入驻");
                goToPage(this,ClientSettleActivity.class,null);
                break;
            case R.id.business_layout:
                ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).statusBarColor(R.color.white).init();
                getP().checkTab(2);
                break;
            case R.id.mine_layout:
                if (!UserInfoUtils.getInstance().isLogin()) {
                    getP().goLogin();
                    return;
                }
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
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void hideFragment(int index) {
        FragmentTransaction fragmentTransaction = getFragmentTransaction();

        fragmentTransaction.hide(mFragmentList.get(index));
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onSeleted(int checkTab, int oldTab) {

        mMenuLayout.get(checkTab).setonSelected(true);

        mMenuLayout.get(oldTab).setonSelected(false);
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

}
