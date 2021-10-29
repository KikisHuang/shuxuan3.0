package com.gxdingo.sg.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientAccountTransactionBean;
import com.gxdingo.sg.bean.ClientCashInfoBean;
import com.gxdingo.sg.biz.ClientAccountSecurityContract;
import com.gxdingo.sg.biz.PayPwdContract;
import com.gxdingo.sg.presenter.ClientAccountSecurityPresenter;
import com.gxdingo.sg.presenter.PayPwdPresenter;
import com.gxdingo.sg.utils.ClientLocalConstant;
import com.gxdingo.sg.view.CountdownView;
import com.gxdingo.sg.view.PasswordLayout;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.blankj.utilcode.util.TimeUtils.getNowMills;
import static com.gxdingo.sg.utils.LocalConstant.CODE_SEND;
import static com.kikis.commnlibrary.utils.CommonUtils.getUserPhone;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;

/**
 * @author: Weaving
 * @date: 2021/10/15
 * @page:
 */
public class ClientSettingPayPwd2Activity extends BaseMvpActivity<PayPwdContract.PayPwdPresenter> implements PayPwdContract.PayPwdListener {

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

    @OnClick(R.id.btn_next_or_confirm)
    public void nextStep(){
        goToPage(this,ClientSettingPayPwd3Activity.class,getIntentMap(new String[]{password_layout.getPassString()}));
        password_layout.removeAllPwd();
    }

    @Override
    protected PayPwdContract.PayPwdPresenter createPresenter() {
        return new PayPwdPresenter();
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

        pay_pwd_hint.setVisibility(View.VISIBLE);
        pay_psw_cdv.setVisibility(View.GONE);
        btn_next_or_confirm.setText(gets(R.string.next));

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
        getP().getUserPhone();
    }


    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
        if (object.equals(ClientLocalConstant.UPDATE_SUCCESS))
            finish();
    }

    @Override
    public void setUserPhone(String phone) {
        hint_tv.setText("请为账号 "+getUserPhone(phone)+"设置6位数支付密码");
    }

    @Override
    public String getFirstPwd() {
        return null;
    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public void next() {

    }
}
