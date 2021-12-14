package com.gxdingo.sg.activity;

import android.view.View;

import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.ClientExchangeRecordContract;
import com.gxdingo.sg.presenter.ClientExchangeRecordPresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.view.TemplateTitle;

import butterknife.BindView;

/**
 * @author: Weaving
 * @date: 2021/12/9
 * @page:
 */
public class ClientExchangeRecordActivity extends BaseMvpActivity<ClientExchangeRecordContract.ClientExchangeRecordPresenter> {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @Override
    protected ClientExchangeRecordContract.ClientExchangeRecordPresenter createPresenter() {
        return new ClientExchangeRecordPresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return false;
    }

    @Override
    protected int activityTitleLayout() {
        return R.layout.module_include_custom_title;
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
        return R.layout.module_activity_exchange_record;
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
        title_layout.setTitleText("收支兑换记录");
    }

    @Override
    protected void initData() {

    }
}
