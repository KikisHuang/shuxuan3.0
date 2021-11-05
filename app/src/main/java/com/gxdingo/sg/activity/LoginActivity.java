package com.gxdingo.sg.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.WeChatLoginEvent;
import com.gxdingo.sg.biz.LoginContract;
import com.gxdingo.sg.presenter.LoginPresenter;
import com.gxdingo.sg.view.CountdownView;
import com.gxdingo.sg.view.RegexEditText;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.bean.ReLoginBean;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;

import butterknife.BindView;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.TimeUtils.getNowMills;
import static com.gxdingo.sg.utils.LocalConstant.CLIENT_LOGIN_SUCCEED;
import static com.gxdingo.sg.utils.LocalConstant.CODE_SEND;
import static com.gxdingo.sg.utils.LocalConstant.LOGIN_WAY;
import static com.gxdingo.sg.utils.LocalConstant.STORE_LOGIN_SUCCEED;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;

/**
 * @author: Weaving
 * @date: 2021/10/12
 * @page:
 */
public class LoginActivity extends BaseMvpActivity<LoginContract.LoginPresenter> implements LoginContract.LoginListener {

    //用户身份登录
    private boolean isUserId = true;

    //登录页登录成功是否返回首页
    private boolean backHome = true;

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.img_back)
    public ImageView img_back;

    @BindView(R.id.role_tv)
    public TextView role_tv;

    @BindView(R.id.switch_login_bt)
    public TextView switch_login_bt;

    @BindView(R.id.alipay_login)
    public ImageView alipay_login;

    @BindView(R.id.wechat_login)
    public ImageView wechat_login;

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
        backHome = getIntent().getBooleanExtra(Constant.SERIALIZABLE + 0, true);
//        getP().switchPanel(false,true);
        isUserId = SPUtils.getInstance().getBoolean(LOGIN_WAY, true);
        role_tv.setText(isUserId ? gets(R.string.client_shuxuan) : gets(R.string.store_shuxuan));
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.switch_login_bt, R.id.alipay_login
            , R.id.wechat_login, R.id.send_verification_code_bt, R.id.login_bt})
    public void onClickViews(View v) {
        switch (v.getId()) {
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
    public void onSucceed(int type) {
        super.onSucceed(type);
        if (type == CODE_SEND) {
            SPUtils.getInstance().put(Constant.SMS_CODE_KEY, getNowMills());
            onMessage(gets(R.string.captcha_code_sent));
            send_verification_code_bt.setText(gets(R.string.resend));
            send_verification_code_bt.setTotalTime(60);
            send_verification_code_bt.start();
        }
    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type == CLIENT_LOGIN_SUCCEED || type == STORE_LOGIN_SUCCEED) {
            if (type == STORE_LOGIN_SUCCEED) {
                sendEvent(new ReLoginBean());
                SPUtils.getInstance().put(LOGIN_WAY, false);//保存商家登录

                goToPage(reference.get(), StoreActivity.class, null);

            } else {


                SPUtils.getInstance().put(LOGIN_WAY, true);
                if (backHome) {
//                    sendEvent(new ReLoginBean());

                    SPUtils.getInstance().put(LOGIN_WAY, true);//保存用户登录
                    if (backHome) {
                        sendEvent(new ReLoginBean());
                        goToPage(reference.get(), ClientActivity.class, null);
                    }
                }
                finish();
            }
        }
    }

        @Override
        protected void onBaseEvent (Object object){
            //微信登录事件
            if (object instanceof WeChatLoginEvent) {
                WeChatLoginEvent event = (WeChatLoginEvent) object;
                if (!isEmpty(event.code))
                    getP().weChatLogin(event.code);
            }
        }


        @Override
        public String getCode () {
            return verification_code_ed.getText().toString();
        }

        @Override
        public String getMobile () {
            return et_phone_number.getText().toString();
        }

        @Override
        public boolean isClient () {
            return isUserId;
        }

        @Override
        public void setVerificationCodeTime ( int time){

        }

        //身份切换
        @Override
        public void showIdButton () {
            role_tv.setText(isUserId ? "树享客户端" : "树享商家端");
            switch_login_bt.setText(isUserId ? gets(R.string.store_id_login) : gets(R.string.user_id_login));
        }

}
