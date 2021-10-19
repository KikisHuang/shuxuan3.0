package com.gxdingo.sg.activity;

import android.view.View;

import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.ClientHomeContract;
import com.gxdingo.sg.presenter.ClientHomePresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;

/**
 * @author: Weaving
 * @date: 2021/10/17
 * @page:
 */
public class ClientSearchActivity extends BaseMvpActivity<ClientHomeContract.ClientHomePresenter> {


    @Override
    protected ClientHomeContract.ClientHomePresenter createPresenter() {
        return new ClientHomePresenter();
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
        return R.color.white;
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
        return R.layout.module_activity_client_search;
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

    }

    @Override
    protected void initData() {

    }
}
