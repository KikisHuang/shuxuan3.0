package com.gxdingo.sg.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.solver.state.State;

import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.ClientAccountSecurityContract;
import com.gxdingo.sg.presenter.ClientAccountSecurityPresenter;
import com.gxdingo.sg.view.CountdownView;
import com.gxdingo.sg.view.PasswordLayout;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;

import butterknife.BindView;

import static com.kikis.commnlibrary.utils.CommonUtils.getUserPhone;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;

/**
 * @author: Weaving
 * @date: 2021/10/15
 * @page:
 */
public class ClientSettingPayPwd3Activity extends BaseMvpActivity<ClientAccountSecurityContract.ClientAccountSecurityPresenter> implements ClientAccountSecurityContract.ClientAccountSecurityListener {

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

    private String password;

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
        password = getIntent().getStringExtra(Constant.PARAMAS+0);
        hint_tv.setText("再次输入");
        pay_pwd_hint.setVisibility(View.GONE);
        pay_psw_cdv.setVisibility(View.GONE);
        btn_next_or_confirm.setText(gets(R.string.confirm));

        password_layout.setPwdChangeListener(new PasswordLayout.pwdChangeListener() {
            @Override
            public void onChange(String pwd) {

            }

            @Override
            public void onNull() {

            }

            @Override
            public void onFinished(String pwd) {
                btn_next_or_confirm.setEnabled(true);
            }
        });
    }

    @Override
    protected void initData() {
        getP().sendVerificationCode();
    }


    @Override
    public void setUserPhone(String phone) {

    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public void next() {

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
}
