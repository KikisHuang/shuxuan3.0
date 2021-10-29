package com.gxdingo.sg.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.solver.state.State;

import com.blankj.utilcode.util.ToastUtils;
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

import static com.kikis.commnlibrary.utils.CommonUtils.getUserPhone;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;

/**
 * @author: Weaving
 * @date: 2021/10/15
 * @page:
 */
public class ClientSettingPayPwd3Activity extends BaseMvpActivity<PayPwdContract.PayPwdPresenter> implements PayPwdContract.PayPwdListener{

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

    private String firstPassword;

    private boolean isUpdate;

    @OnClick(R.id.btn_next_or_confirm)
    public void confirm(){
        getP().changePayPwd();
    }

    @Override
    protected PayPwdContract.PayPwdPresenter createPresenter() {
        return new PayPwdPresenter();
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
    public void onSucceed(int type) {
        super.onSucceed(type);
        if (type == 1){
            sendEvent(ClientLocalConstant.UPDATE_SUCCESS);
            ToastUtils.showLong("修改成功!");
            finish();
        }

    }

    @Override
    protected void init() {
        if (isUpdate)
            title_layout.setTitleText(gets(R.string.update_pay_pwd));
        else
            title_layout.setTitleText(gets(R.string.setting_pay_pwd));
        firstPassword = getIntent().getStringExtra(Constant.PARAMAS+0);
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

    }


    @Override
    public void setUserPhone(String phone) {

    }

    /*
     * 第一次输入的密码
     * */
    @Override
    public String getFirstPwd() {
        return firstPassword;
    }

    /*
    * 输入完成密码
    * */
    @Override
    public String getCode() {
        return password_layout.getPassString();
    }

    @Override
    public void next() {

    }

    @Override
    public void onMessage(String msg) {
        super.onMessage(msg);
        password_layout.removeAllPwd();
    }
}
