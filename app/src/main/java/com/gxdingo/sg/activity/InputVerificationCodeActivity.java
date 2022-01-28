package com.gxdingo.sg.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.LoginContract;
import com.gxdingo.sg.presenter.LoginPresenter;
import com.gxdingo.sg.view.CountdownView;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;

import butterknife.BindView;
import butterknife.OnClick;

import static com.blankj.utilcode.util.StringUtils.isEmpty;
import static com.blankj.utilcode.util.TimeUtils.getNowMills;
import static com.gxdingo.sg.utils.LocalConstant.LOGIN_SUCCEED;
import static com.gxdingo.sg.utils.LocalConstant.CODE_SEND;
import static com.kikis.commnlibrary.utils.CommonUtils.getUserPhone;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;

/**
 * @author: Weaving
 * @date: 2021/10/14
 * @page:
 */
public class InputVerificationCodeActivity extends BaseMvpActivity<LoginContract.LoginPresenter> implements LoginContract.LoginListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.send_code)
    public CountdownView send_code;

    @BindView(R.id.verification_code_send_hint_tv)
    public TextView verification_code_send_hint_tv;

    @BindView(R.id.et_verification_code)
    public EditText et_verification_code;

    public String mPhoneNumber = "";

    public String mOpenId = "";

    public String mAppName = "";


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
        return R.layout.module_activity_input_verification_code;
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
        mPhoneNumber = getIntent().getStringExtra(Constant.SERIALIZABLE + 0);
        mOpenId = getIntent().getStringExtra(Constant.SERIALIZABLE + 1);
        mAppName = getIntent().getStringExtra(Constant.SERIALIZABLE + 2);

        if (isEmpty(mPhoneNumber)) {
            onMessage(getString(R.string.dont_get_phone_number));
            finish();
            return;
        }
        title_layout.setTitleText(gets(R.string.please_input_verification_code));
//        getP().getPhoneHint(mPhoneNumber);
        verification_code_send_hint_tv.setText("验证码已发送至+86"+getUserPhone(mPhoneNumber));
        getP().getVerificationCodeTime();
    }


    @OnClick({R.id.send_code, R.id.verificatione_bt})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;
        switch (v.getId()) {
            case R.id.send_code:
                getP().sendVerificationCode();
                break;
            case R.id.verificatione_bt:
                getP().bindPhone(mOpenId, mAppName);
                break;
        }

    }

    @Override
    public void onSucceed(int type) {
        super.onSucceed(type);
        if (type == CODE_SEND) {
            SPUtils.getInstance().put(Constant.SMS_CODE_KEY, getNowMills());
            onMessage(gets(R.string.captcha_code_sent));
            send_code.setTotalTime(60);
            send_code.start();
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public String getCode() {
        return et_verification_code.getText().toString();
    }

    @Override
    public String getMobile() {
        return mPhoneNumber;
    }

    @Override
    public boolean getCheckState() {
        return false;
    }

    @Override
    public void setVerificationCodeTime(int time) {
        send_code.setTotalTime(time);

        send_code.start();
    }


    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type == LOGIN_SUCCEED)
            finish();
    }
}
