package com.gxdingo.sg.activity;

import android.view.View;

import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.LoginContract;
import com.gxdingo.sg.presenter.LoginPresenter;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.view.RegexEditText;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;

import butterknife.BindView;
import butterknife.OnClick;

import static com.gxdingo.sg.utils.LocalConstant.LOGIN_SUCCEED;
import static com.gxdingo.sg.utils.LocalConstant.CODE_SEND;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

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

    @Override
    protected LoginContract.LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return true;
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

    }

    @OnClick({R.id.send_verification_code_bt,R.id.title_back})
    public void bindPhone(View v){
        if (!checkClickInterval(v.getId()))
            return;
        switch (v.getId()) {
            case R.id.send_verification_code_bt:
                getP().sendVerificationCode();
                break;
            case R.id.title_back:
                sendEvent(LocalConstant.QUITLOGINPAGE);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendEvent(LocalConstant.QUITLOGINPAGE);
        finish();
    }

    @Override
    public void onSucceed(int type) {
        super.onSucceed(type);
        if (type == CODE_SEND)
            goToPagePutSerializable(this, InputVerificationCodeActivity.class, getIntentEntityMap(new Object[]{et_phone_number.getText().toString(), openId, appName}));
        else
            finish();
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
    public boolean getCheckState() {
        return false;
    }


    @Override
    public void setVerificationCodeTime(int time) {

    }


    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type == LOGIN_SUCCEED )
            finish();
    }
}
