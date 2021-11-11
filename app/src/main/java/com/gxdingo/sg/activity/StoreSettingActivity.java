package com.gxdingo.sg.activity;

import android.view.View;

import com.allen.library.SuperTextView;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.StoreQRCodeBean;
import com.gxdingo.sg.biz.StoreSettingsContract;
import com.gxdingo.sg.presenter.StoreSettingsPresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.view.TemplateTitle;

import butterknife.BindView;

/**
 * @author: Weaving
 * @date: 2021/11/10
 * @page:
 */
public class StoreSettingActivity extends BaseMvpActivity<StoreSettingsContract.StoreSettingsPresenter> implements StoreSettingsContract.StoreSettingsListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.change_avatar_stv)
    public SuperTextView change_avatar_stv;

    @BindView(R.id.change_nickname_stv)
    public SuperTextView change_nickname_stv;

    @BindView(R.id.change_address_stv)
    public SuperTextView change_address_stv;

    @BindView(R.id.change_mobile_stv)
    public SuperTextView change_mobile_stv;

    @BindView(R.id.business_time_stv)
    public SuperTextView business_time_stv;

    @BindView(R.id.business_scope_stv)
    public SuperTextView business_scope_stv;

    @Override
    protected StoreSettingsContract.StoreSettingsPresenter createPresenter() {
        return new StoreSettingsPresenter();
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
        return R.layout.module_activity_store_setting;
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
        title_layout.setTitleText("店铺设置");
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onQRResult(StoreQRCodeBean qrCodeBean) {

    }
}
