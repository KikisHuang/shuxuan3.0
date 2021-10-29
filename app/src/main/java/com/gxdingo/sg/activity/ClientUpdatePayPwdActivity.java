package com.gxdingo.sg.activity;

import android.view.View;
import android.widget.TextView;

import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientAccountTransactionBean;
import com.gxdingo.sg.bean.ClientCashInfoBean;
import com.gxdingo.sg.biz.ClientAccountSecurityContract;
import com.gxdingo.sg.biz.PayPwdContract;
import com.gxdingo.sg.presenter.ClientAccountSecurityPresenter;
import com.gxdingo.sg.presenter.PayPwdPresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
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
public class ClientUpdatePayPwdActivity extends BaseMvpActivity<PayPwdContract.PayPwdPresenter> implements PayPwdContract.PayPwdListener{

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.hint_tv)
    public TextView hint_tv;

    @OnClick({R.id.btn_no,R.id.btn_yes})
    public void OnClickViews(View v){
        switch (v.getId()){
            case R.id.btn_no:
                goToPage(this,ClientSettingPayPwd1Activity.class,getIntentMap(new String[]{String.valueOf(1)}));
                finish();
                break;
            case R.id.btn_yes:
                goToPage(this,ClientCertifyPayPswActivity.class,getIntentMap(new String[]{String.valueOf(1)}));
                finish();
                break;
        }
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
        return R.layout.module_activity_client_update_pay_pwd;
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
        title_layout.setTitleText(gets(R.string.update_pay_pwd));
    }

    @Override
    protected void initData() {
        getP().getUserPhone();
    }


    @Override
    public void setUserPhone(String phone) {
        hint_tv.setText("您是否还记得账号"+getUserPhone(phone)+"当前所使用的支付 密码");
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
