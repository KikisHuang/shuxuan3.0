package com.gxdingo.sg.activity;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.Utils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.UserBean;
import com.gxdingo.sg.biz.StoreMainContract;
import com.gxdingo.sg.fragment.store.StoreBusinessDistrictFragment;
import com.gxdingo.sg.fragment.store.StoreMyFragment;
import com.gxdingo.sg.fragment.client.ClientBusinessDistrictFragment;
import com.gxdingo.sg.fragment.store.StoreHomeFragment;
import com.gxdingo.sg.fragment.store.StoreWalletFragment;
import com.gxdingo.sg.presenter.StoreMainPresenter;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.view.CircularRevealButton;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.OnClick;

import static com.blankj.utilcode.util.AppUtils.registerAppStatusChangedListener;
import static com.gxdingo.sg.utils.LocalConstant.STORE_LOGIN_SUCCEED;
import static com.kikis.commnlibrary.utils.Constant.LOGOUT;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;

/**
 * Created by Kikis on 2021/4/6
 * 商家端主页面
 */
public class StoreActivity extends BaseMvpActivity<StoreMainContract.StoreMainPresenter> implements Utils.OnAppStatusChangedListener, StoreMainContract.StoreMainListener {


    private List<Fragment> mFragmentList;


    @BindViews({R.id.crb_store_home_page_layout, R.id.crb_store_wallet, R.id.crb_store_business_district, R.id.crb_store_my})
    public List<CircularRevealButton> mMenuLayout;

    private long timeDValue = 0; // 计算时间差值，判断是否需要退出


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
        return false;
    }

    @Override
    protected int StatusBarColors() {
        return 0;
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
    protected void init() {
        checkUserStatus();
        fragmentInit();
        getP().persenterInit();

        registerAppStatusChangedListener(this);
    }

    StoreBusinessDistrictFragment mStoreBusinessDistrictFragment;

    private void fragmentInit() {
        mStoreBusinessDistrictFragment = new StoreBusinessDistrictFragment();
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new StoreHomeFragment());
        mFragmentList.add(new StoreWalletFragment());
        mFragmentList.add(mStoreBusinessDistrictFragment);
        mFragmentList.add(new StoreMyFragment());
    }

    @Override
    protected void onBaseCreate() {
        super.onBaseCreate();
        // 显示状态栏
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void initData() {
        if (UserInfoUtils.getInstance().isLogin())
            getP().getSocketUrl();

    }

    @Override
    protected void statusBarInit() {
        super.statusBarInit();
        mImmersionBar.statusBarDarkFont(true).init();
    }

    @OnClick({R.id.crb_store_home_page_layout, R.id.crb_store_wallet, R.id.crb_store_business_district, R.id.crb_store_my})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;
        switch (v.getId()) {
            case R.id.crb_store_home_page_layout:
                getP().checkTab(0);
                break;
            case R.id.crb_store_wallet:
                getP().checkTab(1);
                break;
            case R.id.crb_store_business_district:
                getP().checkTab(2);
                break;
            case R.id.crb_store_my:
                getP().checkTab(3);
                break;
        }
    }

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
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
    }

    /**
     * 获取fragment集合
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
     * 显示fragment
     *
     * @param index
     */
    @Override
    public void showFragment(FragmentTransaction fragmentTransaction, int index) {

        fragmentTransaction.show(mFragmentList.get(index));
        fragmentTransaction.commitAllowingStateLoss();

    }

    /**
     * 隐藏fragment
     *
     * @param index
     */
    @Override
    public void hideFragment(int index) {
        FragmentTransaction fragmentTransaction = getFragmentTransaction();

        fragmentTransaction.hide(mFragmentList.get(index));
        fragmentTransaction.commitAllowingStateLoss();
    }


    /**
     * 选中
     *
     * @param checkTab
     * @param oldTab
     */
    @Override
    public void onSeleted(int checkTab, int oldTab) {

        mMenuLayout.get(checkTab).setonSelected(true);

        mMenuLayout.get(oldTab).setonSelected(false);

    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type == STORE_LOGIN_SUCCEED) {
            checkUserStatus();
            getP().getSocketUrl();
        } else if (type == LOGOUT) {
            getP().destroySocket();
        }
    }

    /**
     * 检查用户状态
     */
    private void checkUserStatus() {
        if (!UserInfoUtils.getInstance().isLogin()) {
            UserInfoUtils.getInstance().goToLoginPage(reference.get(), "");
            finish();
            return;
        }

        UserBean userBean = UserInfoUtils.getInstance().getUserInfo();
        //当store.id == 0（表示未填写过入驻信息）或store.status == 0（表示正在审核）或store.status == 20 （表示被驳回）的时候需要跳转到商家认证界面获取显示状态或者填写入驻信息
        if (userBean.getStore().getId() == 0 || userBean.getStore().getStatus() == 0 || userBean.getStore().getStatus() == 20) {
            //跳转到商家认证界面
            goToPage(reference.get(), StoreCertificationActivity.class, null);
        }
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
    public void onForeground(Activity activity) {
        LocalConstant.isBackground = false;
    }

    @Override
    public void onBackground(Activity activity) {
        LocalConstant.isBackground = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getP().release();
    }
}