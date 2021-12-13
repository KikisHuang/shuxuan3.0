package com.gxdingo.sg.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.WeChatLoginEvent;
import com.gxdingo.sg.biz.LoginContract;
import com.gxdingo.sg.presenter.LoginPresenter;
import com.gxdingo.sg.view.CountdownView;
import com.gxdingo.sg.view.PartTextClickSpan;
import com.gxdingo.sg.view.RegexEditText;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.bean.ReLoginBean;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.TimeUtils.getNowMills;
import static com.gxdingo.sg.http.ClientApi.CLIENT_PRIVACY_AGREEMENT_KEY;
import static com.gxdingo.sg.http.ClientApi.CLIENT_SERVICE_AGREEMENT_KEY;
import static com.gxdingo.sg.http.ClientApi.STORE_PRIVACY_AGREEMENT_KEY;
import static com.gxdingo.sg.http.ClientApi.STORE_SERVICE_AGREEMENT_KEY;
import static com.gxdingo.sg.utils.LocalConstant.CLIENT_LOGIN_SUCCEED;
import static com.gxdingo.sg.utils.LocalConstant.CODE_SEND;
import static com.gxdingo.sg.utils.LocalConstant.LOGIN_WAY;
import static com.gxdingo.sg.utils.LocalConstant.STORE_LOGIN_SUCCEED;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

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


    @BindView(R.id.user_login_tv)
    public TextView user_login_tv;

    @BindView(R.id.store_login_tv)
    public TextView store_login_tv;

    @BindView(R.id.agreement_tv)
    public TextView agreement_tv;

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

    @BindView(R.id.login_check_bt)
    public CheckBox login_check_bt;


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
        setLoginWayState();
        setTextHighLightWithClick(agreement_tv.getText().toString(), new String[]{"《服务协议》", "《隐私政策》"});
    }

    @Override
    protected void initData() {


    }

    @OnClick({R.id.user_login_tv, R.id.store_login_tv, R.id.alipay_login
            , R.id.wechat_login, R.id.send_verification_code_bt, R.id.login_bt})
    public void onClickViews(View v) {
        switch (v.getId()) {
            case R.id.user_login_tv:
                isUserId = true;
                getP().switchUrl(isUserId);
                break;
            case R.id.store_login_tv:
                isUserId = false;
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
//                    sendEvent(new ReLoginBean());
                if (backHome) {
                    sendEvent(new ReLoginBean());
                    goToPage(reference.get(), ClientActivity.class, null);
                }
            }
            finish();
        }
    }

    @Override
    protected void onBaseEvent(Object object) {
        //微信登录事件
        if (object instanceof WeChatLoginEvent) {
            WeChatLoginEvent event = (WeChatLoginEvent) object;
            if (!isEmpty(event.code))
                getP().weChatLogin(event.code);
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
    public boolean getCheckState() {
        return login_check_bt.isChecked();
    }

    @Override
    public boolean isClient() {
        return isUserId;
    }

    @Override
    public void setVerificationCodeTime(int time) {

    }

    //身份切换
    @Override
    public void showIdButton() {
        role_tv.setText(isUserId ? "树选客户端" : "树选商家端");
        setLoginWayState();
    }

    /**
     * 设置文字高亮及点击事件
     *
     * @param text
     * @param keyWord
     */
    private void setTextHighLightWithClick(String text, String[] keyWord) {

        agreement_tv.setClickable(true);

        agreement_tv.setHighlightColor(Color.TRANSPARENT);

        agreement_tv.setMovementMethod(LinkMovementMethod.getInstance());


        SpannableString s = new SpannableString(text);

        for (int i = 0; i < keyWord.length; i++) {
            Pattern p = Pattern.compile(keyWord[i]);
            Matcher m = p.matcher(s);

            while (m.find()) {

                int start = m.start();

                int end = m.end();

                int finalI = i;

                s.setSpan(new PartTextClickSpan(getc(R.color.deepskyblue), false, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (finalI == 0)
                            goToPagePutSerializable(reference.get(), WebActivity.class, getIntentEntityMap(new Object[]{true, 0, isUserId ? CLIENT_SERVICE_AGREEMENT_KEY : STORE_SERVICE_AGREEMENT_KEY}));
                        else
                            goToPagePutSerializable(reference.get(), WebActivity.class, getIntentEntityMap(new Object[]{true, 0, isUserId ? CLIENT_PRIVACY_AGREEMENT_KEY : STORE_PRIVACY_AGREEMENT_KEY}));
                    }
                }), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        agreement_tv.setText(s);

    }

    private void setLoginWayState() {
        if (isUserId) {
            store_login_tv.setBackgroundResource(R.drawable.module_bg_main_color_round6);
            store_login_tv.setTextColor(getc(R.color.white));

            user_login_tv.setBackgroundResource(R.drawable.module_bg_enter_payment_password);
            user_login_tv.setTextColor(getc(R.color.green_dominant_tone));
        } else {
            user_login_tv.setBackgroundResource(R.drawable.module_bg_main_color_round6);
            user_login_tv.setTextColor(getc(R.color.white));

            store_login_tv.setBackgroundResource(R.drawable.module_bg_enter_payment_password);
            store_login_tv.setTextColor(getc(R.color.green_dominant_tone));
        }

    }
}
