package com.gxdingo.sg.activity;

import android.view.View;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.ToastUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.ClientMainContract;
import com.gxdingo.sg.fragment.client.ClientBusinessDistrictFragment;
import com.gxdingo.sg.fragment.client.ClientHomeFragment;
import com.gxdingo.sg.fragment.client.ClientMessageFragment;
import com.gxdingo.sg.fragment.client.ClientMineFragment;
import com.gxdingo.sg.presenter.ClientMainPresenter;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.view.CircularRevealButton;
import com.gyf.immersionbar.ImmersionBar;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.OnClick;

import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientActivity extends BaseMvpActivity<ClientMainContract.ClientMainPresenter> implements ClientMainContract.ClientMainListener{

    private List<Fragment> mFragmentList;

    @BindViews({R.id.home_page_layout, R.id.message_layout,R.id.business_layout, R.id.mine_layout})
    public List<CircularRevealButton> mMenuLayout;

    private long timeDValue = 0; // 计算时间差值，判断是否需要退出

    private boolean showLogin = true;

    @Override
    protected ClientMainContract.ClientMainPresenter createPresenter() {
        return new ClientMainPresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return false;
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
        if (!UserInfoUtils.getInstance().isLogin()&&showLogin){
            goToPage(this, LoginActivity.class,null);
            showLogin = !showLogin;
        }
    }

    @Override
    protected void onBaseCreate() {
        super.onBaseCreate();
        // 显示状态栏
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void initData() {

    }

    private void fragmentInit(){
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new ClientHomeFragment());
        mFragmentList.add(new ClientMessageFragment());
        mFragmentList.add(new ClientBusinessDistrictFragment());
        mFragmentList.add(new ClientMineFragment());
    }


    @OnClick({R.id.home_page_layout, R.id.message_layout, R.id.settle_in,R.id.business_layout, R.id.mine_layout})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;
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
                ToastUtils.showLong("hi!索嗨，来入驻");
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
}
