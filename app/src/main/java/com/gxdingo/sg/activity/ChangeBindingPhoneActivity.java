package com.gxdingo.sg.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientAccountTransactionBean;
import com.gxdingo.sg.bean.ClientCashInfoBean;
import com.gxdingo.sg.biz.ChangeBindPhoneContract;
import com.gxdingo.sg.biz.ClientAccountSecurityContract;
import com.gxdingo.sg.presenter.ChangeBindPhonePresenter;
import com.gxdingo.sg.presenter.ClientAccountSecurityPresenter;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.view.CountdownView;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.bean.ReLoginBean;
import com.kikis.commnlibrary.view.TemplateTitle;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.kikis.commnlibrary.utils.CommonUtils.getUserPhone;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;

/**
 * @author: Weaving
 * @date: 2021/10/15
 * @page:
 */
public class ChangeBindingPhoneActivity extends BaseMvpActivity<ChangeBindPhoneContract.ChangeBindPhonePresenter> implements ChangeBindPhoneContract.ChangeBindPhoneListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.text_change_phone_hint)
    public TextView text_change_phone_hint;

    @BindView(R.id.country_code_tv)
    public TextView country_code_tv;

    @BindView(R.id.et_certify_code)
    public EditText et_certify_code;

    @BindView(R.id.text_change_phone)
    public TextView text_change_phone;

    @BindView(R.id.btn_send_code)
    public CountdownView btn_send_code;

    @BindView(R.id.new_phone_btn_send_code)
    public CountdownView new_phone_btn_send_code;

    @BindView(R.id.bottom_hint_tv)
    public TextView bottom_hint_tv;

    @BindView(R.id.btn_next)
    public Button btn_next;
    @Override
    protected ChangeBindPhoneContract.ChangeBindPhonePresenter createPresenter() {
        return new ChangeBindPhonePresenter();
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
        return R.layout.module_activity_change_binding_phone;
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
        title_layout.setTitleText(gets(R.string.update_phone));

        btn_send_code.setTotalTime(60);
        new_phone_btn_send_code.setTotalTime(60);

        getP().getUserPhone();
//        getP().checkCode();
//        getP().sendVerificationCode();

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.title_back, R.id.new_phone_btn_send_code, R.id.btn_send_code, R.id.btn_next})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;
        switch (v.getId()) {
            case R.id.title_back:
                getP().lastStep();
                break;
            case R.id.btn_send_code:
                getP().sendOldPhoneVerificationCode();

                break;
            case R.id.new_phone_btn_send_code:
                getP().sendNewPhoneVerificationCode(UserInfoUtils.getInstance().getUserPhone());

                break;
            case R.id.btn_next:
                getP().nextStep(et_certify_code.getText().toString());

                break;

        }
    }

    @Override
    public void onFailed() {
        super.onFailed();
//        sendEvent(new ReLoginBean());
        finish();
    }

    @Override
    public void setUserPhone(String phone) {
        changeHint("为确认身份，请输入" + getUserPhone(phone) + "收到的短信验证码");
    }

//    @Override
//    public String getOldPhoneNum() {
//        return null;
//    }

    @Override
    public void oldPhoneNumberCountDown() {
        if (btn_send_code.getCurrentSecond() <= 0)
            btn_send_code.start();
    }

    @Override
    public void newPhoneNumberCountDown() {
        if (new_phone_btn_send_code.getCurrentSecond() <= 0)
            new_phone_btn_send_code.start();
    }

    @Override
    public void changeTitle(String title) {
        text_change_phone.setText(title);
    }

    @Override
    public void changeHint(String hint) {
        text_change_phone_hint.setText(hint);
    }

    @Override
    public void changeNextBtnText(String text) {
        btn_next.setText(text);
    }

    @Override
    public void bottomHintVisibility(int visib) {
        bottom_hint_tv.setVisibility(visib);
    }

    @Override
    public void oldPhoneCodeCountdownVisibility(int visib) {
        btn_send_code.setVisibility(visib);
    }

    @Override
    public void newPhoneCodeCountdownVisibility(int visib) {
        new_phone_btn_send_code.setVisibility(visib);
    }

    @Override
    public void countryCodeShow(boolean show) {
        country_code_tv.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setEdittextInputType(int type) {
        et_certify_code.setInputType(type);
    }

    @Override
    public void setEdittextContent(String content) {
        et_certify_code.setText(content);
        et_certify_code.setSelection(et_certify_code.length());
    }

    @Override
    public void setEdittextHint(String hint) {
        et_certify_code.setHint(hint);
    }

    @Override
    public int getNumberCountDown() {
        return new_phone_btn_send_code.getCurrentSecond();
    }
}
