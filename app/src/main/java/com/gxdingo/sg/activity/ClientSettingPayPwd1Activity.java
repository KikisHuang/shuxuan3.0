package com.gxdingo.sg.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.ClientAccountSecurityContract;
import com.gxdingo.sg.presenter.ClientAccountSecurityPresenter;
import com.gxdingo.sg.view.CountdownView;
import com.gxdingo.sg.view.PasswordLayout;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;

import butterknife.BindView;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.TimeUtils.getNowMills;
import static com.gxdingo.sg.utils.LocalConstant.CODE_SEND;
import static com.kikis.commnlibrary.utils.CommonUtils.getUserPhone;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;

/**
 * @author: Weaving
 * @date: 2021/10/15
 * @page:
 */
public class ClientSettingPayPwd1Activity extends BaseMvpActivity<ClientAccountSecurityContract.ClientAccountSecurityPresenter> implements ClientAccountSecurityContract.ClientAccountSecurityListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.hint_tv)
    public TextView hint_tv;

    @BindView(R.id.password_layout)
    public PasswordLayout password_layout;

    @BindView(R.id.pay_pwd_hint)
    public TextView pay_pwd_hint;

    @BindView(R.id.pay_psw_cdv)
    public CountdownView pay_psw_cdv;

    @BindView(R.id.btn_next_or_confirm)
    public Button btn_next_or_confirm;

    private boolean isUpdate;

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
        return R.layout.module_activity_client_setting_pay_pwd;
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
        if (isUpdate)
            title_layout.setTitleText(gets(R.string.update_pay_pwd));
        else
            title_layout.setTitleText(gets(R.string.setting_pay_pwd));

        pay_pwd_hint.setVisibility(View.GONE);
        btn_next_or_confirm.setVisibility(View.GONE);

        password_layout.setPwdChangeListener(new PasswordLayout.pwdChangeListener() {
            @Override
            public void onChange(String pwd) {

            }

            @Override
            public void onNull() {

            }

            @Override
            public void onFinished(String pwd) {
                getP().certify();
            }
        });
    }

    @Override
    protected void initData() {
        getP().sendVerificationCode();
    }

    @Override
    public void onSucceed(int type) {
        super.onSucceed(type);
        if (type == CODE_SEND) {
            getP().getUserPhone();
            SPUtils.getInstance().put(Constant.SMS_CODE_KEY, getNowMills());
            onMessage(gets(R.string.captcha_code_sent));
            pay_psw_cdv.setText(gets(R.string.resend));
            pay_psw_cdv.setTotalTime(60);
            pay_psw_cdv.start();
        }
    }

    @Override
    public void setUserPhone(String phone) {
        hint_tv.setText("为确认身份，我们已发送验证码到手机号： "+getUserPhone(phone)+"进行验证。");
    }

    @Override
    public String getCode() {
        return password_layout.getPassString();
    }

    @Override
    public void next() {
        goToPage(this,ClientSettingPayPwd2Activity.class,null);
    }

    @Override
    public void oldPhoneNumberCountDown() {

    }

    @Override
    public void newPhoneNumberCountDown() {

    }

    @Override
    public void changeTitle(String title) {

    }

    @Override
    public void changeHint(String hint) {

    }

    @Override
    public void changeNextBtnText(String text) {

    }

    @Override
    public void bottomHintVisibility(int visib) {

    }

    @Override
    public void oldPhoneCodeCountdownVisibility(int visib) {

    }

    @Override
    public void newPhoneCodeCountdownVisibility(int visib) {

    }

    @Override
    public void countryCodeShow(boolean show) {

    }

    @Override
    public void setEdittextInputType(int type) {

    }

    @Override
    public void setEdittextContent(String content) {

    }

    @Override
    public void setEdittextHint(String hint) {

    }

    @Override
    public int getNumberCountDown() {
        return 0;
    }

    @Override
    public void onMessage(String msg) {
        super.onMessage(msg);
        if (!isEmpty(password_layout.getPassString()))
            password_layout.removeAllPwd();
    }
}
