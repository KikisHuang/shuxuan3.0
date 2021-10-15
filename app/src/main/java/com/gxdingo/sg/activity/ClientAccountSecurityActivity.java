package com.gxdingo.sg.activity;

import android.view.View;

import com.allen.library.SuperTextView;
import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.ClientAccountSecurityContract;
import com.gxdingo.sg.presenter.ClientAccountSecurityPresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.view.TemplateTitle;

import butterknife.BindView;
import butterknife.OnClick;

import static com.kikis.commnlibrary.utils.CommonUtils.gets;

/**
 * @author: Weaving
 * @date: 2021/10/15
 * @page:
 */
public class ClientAccountSecurityActivity extends BaseMvpActivity<ClientAccountSecurityContract.ClientAccountSecurityPresenter> implements ClientAccountSecurityContract.ClientAccountSecurityListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.pay_psw_stv)
    public SuperTextView pay_psw_stv;

    @BindView(R.id.binding_phone_stv)
    public SuperTextView binding_phone_stv;

    @BindView(R.id.binding_ali_stv)
    public SuperTextView binding_ali_stv;

    @BindView(R.id.binding_wechat_stv)
    public SuperTextView binding_wechat_stv;

    @BindView(R.id.binding_bankcard_stv)
    public SuperTextView binding_bankcard_stv;

    @BindView(R.id.version_stv)
    public SuperTextView version_stv;

    @BindView(R.id.cancel_account_stv)
    public SuperTextView cancel_account_stv;


    @OnClick({R.id.pay_psw_stv,R.id.binding_phone_stv,R.id.binding_ali_stv,R.id.binding_wechat_stv,R.id.binding_bankcard_stv,R.id.version_stv,R.id.cancel_account_stv,})
    public void onClickViews(View v){
        switch (v.getId()){
            case R.id.pay_psw_stv:
                break;
            case R.id.binding_phone_stv:
                break;
            case R.id.binding_ali_stv:
                break;
            case R.id.binding_wechat_stv:
                break;
            case R.id.binding_bankcard_stv:
                break;
            case R.id.version_stv:
                break;
            case R.id.cancel_account_stv:
                break;
        }
    }


    @Override
    protected ClientAccountSecurityContract.ClientAccountSecurityPresenter createPresenter() {
        return new ClientAccountSecurityPresenter();
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
        return R.layout.module_activity_client_account_security;
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
        title_layout.setTitleText(gets(R.string.account_security));
    }

    @Override
    protected void initData() {

    }
}
