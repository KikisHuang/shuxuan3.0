package com.gxdingo.sg.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SPUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.LoginContract;
import com.gxdingo.sg.presenter.LoginPresenter;
import com.gxdingo.sg.view.CountdownView;
import com.gxdingo.sg.view.RegexEditText;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.view.TemplateTitle;

import butterknife.BindView;
import butterknife.OnClick;

import static com.gxdingo.sg.utils.LocalConstant.LOGIN_WAY;

/**
 * @author: Weaving
 * @date: 2021/10/12
 * @page:
 */
public class LoginActivity extends BaseMvpActivity<LoginContract.LoginPresenter> implements LoginContract.LoginListener {

    //用户身份登录
    private boolean isUserId = true;


    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.img_back)
    public ImageView img_back;


    //------- 一键登陆面板 ------

    @BindView(R.id.one_click_login_panel)
    public LinearLayout one_click_login_panel;

    @BindView(R.id.one_click_login_bt)
    public Button one_click_login_bt;

    @BindView(R.id.certify_login_bt)
    public Button certify_login_bt;

    @BindView(R.id.switch_login_bt)
    public Button switch_login_bt;

    @BindView(R.id.alipay_login)
    public ImageView alipay_login;

    @BindView(R.id.wechat_login)
    public ImageView wechat_login;

    //-------验证码登陆面板 ------

    @BindView(R.id.certify_panel)
    public LinearLayout certify_panel;

    @BindView(R.id.et_phone_number)
    public RegexEditText et_phone_number;

    @BindView(R.id.verification_code_ed)
    public RegexEditText verification_code_ed;

    @BindView(R.id.send_verification_code_bt)
    public CountdownView send_verification_code_bt;

    @BindView(R.id.login_bt)
    public Button login_bt;


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
        return R.layout.module_activity_login;
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
        getP().switchPanel(false,true);
        isUserId = SPUtils.getInstance().getBoolean(LOGIN_WAY, true);
        switch_login_bt.setText(isUserId?getString(R.string.store_id_login):getString(R.string.user_id_login));
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.img_back,R.id.one_click_login_bt,R.id.certify_login_bt,R.id.switch_login_bt,R.id.alipay_login
            ,R.id.wechat_login,R.id.send_verification_code_bt,R.id.login_bt})
    public void onClickViews(View v){
        switch (v.getId()){
            case R.id.img_back:
                getP().switchPanel(false,true);
                break;
            case R.id.one_click_login_bt:
                break;
            case R.id.certify_login_bt:
                getP().switchPanel(true,false);
                break;
            case R.id.switch_login_bt:
                isUserId = !isUserId;
                getP().switchUrl(isUserId);
                break;
            case R.id.alipay_login:
                getP().alipayAuth();
                break;
            case R.id.wechat_login:
                getP().getWechatAuth();
                break;
            case R.id.send_verification_code_bt:
                getP().sendVerificationCode();
                break;
            case R.id.login_bt:
                getP().login();
                break;
        }
    }

    @Override
    public String getCode() {
        return verification_code_ed.getText().toString();
    }

    @Override
    public String getMobile() {
        return et_phone_number.getText().toString();
    }

    @Override
    public boolean isClient() {
        return isUserId;
    }

    @Override
    public void setVerificationCodeTime(int time) {

    }

    @Override
    public void setPanel(int showBack, int oneClick,int certify) {
        img_back.setVisibility(showBack);
        one_click_login_panel.setVisibility(oneClick);
        certify_panel.setVisibility(certify);
    }

    //身份切换
    @Override
    public void showIdButton() {
        switch_login_bt.setText(isUserId?getString(R.string.store_id_login):getString(R.string.user_id_login));
    }
}
