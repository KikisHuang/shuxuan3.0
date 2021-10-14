package com.gxdingo.sg.activity;

import android.view.View;

import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.LoginContract;
import com.gxdingo.sg.presenter.LoginPresenter;
import com.gxdingo.sg.view.RegexEditText;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;

import butterknife.BindView;
import butterknife.OnClick;

import static com.gxdingo.sg.utils.LocalConstant.CLIENT_LOGIN_SUCCEED;
import static com.gxdingo.sg.utils.LocalConstant.STORE_LOGIN_SUCCEED;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;

/**
 * @author: Weaving
 * @date: 2021/10/14
 * @page:
 */
public class BindingPhoneActivity extends BaseMvpActivity<LoginContract.LoginPresenter> implements LoginContract.LoginListener {


    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.et_phone_number)
    public RegexEditText et_phone_number;

    private String openId, appName;

    private boolean mIsUse;
    @Override
    protected LoginContract.LoginPresenter createPresenter() {
        return new LoginPresenter();
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
        return R.layout.module_activity_binding_phone;
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
        title_layout.setTitleText("绑定手机号");
        title_layout.setBackgroundColor(getc(R.color.white));
        openId = getIntent().getStringExtra(Constant.SERIALIZABLE + 0);
        appName = getIntent().getStringExtra(Constant.SERIALIZABLE + 1);
        mIsUse = getIntent().getBooleanExtra(Constant.SERIALIZABLE + 2, false);
    }

    @OnClick({R.id.send_verification_code_bt})
    public void bindPhone(View v){
        if (!checkClickInterval(v.getId()))
            return;
        switch (v.getId()) {
            case R.id.send_verification_code_bt:
                getP().sendVerificationCode();
                break;
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public String getMobile() {
        return et_phone_number.getText().toString();
    }

    @Override
    public boolean isClient() {
        return false;
    }

    @Override
    public void setVerificationCodeTime(int time) {

    }

    @Override
    public void setPanel(int showBack, int oneClick, int certify) {

    }

    @Override
    public void showIdButton() {

    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type == CLIENT_LOGIN_SUCCEED || type == STORE_LOGIN_SUCCEED)
            finish();
    }
}
