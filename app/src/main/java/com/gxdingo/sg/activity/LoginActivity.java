package com.gxdingo.sg.activity;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
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
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

import static com.blankj.utilcode.util.TimeUtils.getNowMills;
import static com.gxdingo.sg.http.Api.CLIENT_PRIVACY_AGREEMENT_KEY;
import static com.gxdingo.sg.http.Api.CLIENT_SERVICE_AGREEMENT_KEY;
import static com.gxdingo.sg.utils.LocalConstant.LOGIN_SUCCEED;
import static com.gxdingo.sg.utils.LocalConstant.CODE_SEND;
import static com.gxdingo.sg.utils.WechatUtils.weChatLoginType;
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


    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.img_back)
    public ImageView img_back;

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
//        getP().switchPanel(false,true);
        setTextHighLightWithClick(agreement_tv.getText().toString(), new String[]{"《服务协议》", "《隐私政策》"});

        getP().getVerificationCodeTime();
        if (OauthActivity.getInstance() != null)
            OauthActivity.getInstance().finish();
    }

    @Override
    protected void initData() {


    }

    @OnClick({R.id.alipay_login
            , R.id.wechat_login, R.id.send_verification_code_bt, R.id.login_bt})
    public void onClickViews(View v) {
        switch (v.getId()) {
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
            send_verification_code_bt.setText(gets(R.string.resend));
            send_verification_code_bt.setTotalTime(60);
            send_verification_code_bt.start();
        }
    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type == LOGIN_SUCCEED) {
//            sendEvent(new ReLoginBean());
            goToPage(reference.get(), ClientActivity.class, null);
            finish();
        }
    }

    @Override
    protected void onBaseEvent(Object object) {
        //微信登录事件
        if (object instanceof WeChatLoginEvent) {

            if (weChatLoginType == 1) {
                WeChatLoginEvent event = (WeChatLoginEvent) object;
                if (!TextUtils.isEmpty(event.code))
                    getP().oauthWeChatLogin(event.code);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (send_verification_code_bt != null)
            send_verification_code_bt.destroy();
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
    public void setVerificationCodeTime(int time) {
        /*send_verification_code_bt.setTotalTime(time);

        send_verification_code_bt.start();*/
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
                            goToPagePutSerializable(reference.get(), WebActivity.class, getIntentEntityMap(new Object[]{true, 0, CLIENT_SERVICE_AGREEMENT_KEY}));
                        else
                            goToPagePutSerializable(reference.get(), WebActivity.class, getIntentEntityMap(new Object[]{true, 0, CLIENT_PRIVACY_AGREEMENT_KEY}));
                    }
                }), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        agreement_tv.setText(s);

    }

}
