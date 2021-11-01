package com.gxdingo.sg.activity;

import android.view.View;
import android.widget.Button;

import com.allen.library.SuperTextView;
import com.blankj.utilcode.util.SPUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.BankcardBean;
import com.gxdingo.sg.biz.BankcardContract;
import com.gxdingo.sg.presenter.BankcardPresenter;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.view.CountdownView;
import com.gxdingo.sg.view.RegexEditText;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.RegexUtils.isIDCard18;
import static com.blankj.utilcode.util.TimeUtils.getNowMills;
import static com.gxdingo.sg.utils.LocalConstant.CODE_SEND;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;

/**
 * @author: Weaving
 * @date: 2021/10/22
 * @page:
 */
public class AddBankcardActivity extends BaseMvpActivity<BankcardContract.BankcardPresenter> implements BankcardContract.BankcardListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;


    @BindView(R.id.et_name)
    public RegexEditText et_name;

    @BindView(R.id.et_card_number)
    public RegexEditText et_card_number;

    @BindView(R.id.stv_bank_name)
    public SuperTextView stv_bank_name;

    @BindView(R.id.et_bank_card_number)
    public RegexEditText et_bank_card_number;

    @BindView(R.id.et_mobile_number)
    public RegexEditText et_mobile_number;

    @BindView(R.id.et_certify_code)
    public RegexEditText et_certify_code;

    @BindView(R.id.btn_send_code)
    public CountdownView btn_send_code;

    @BindView(R.id.btn_submit)
    public Button btn_submit;

    private BankcardBean mBankcardBean;

    @OnClick({R.id.stv_bank_name,R.id.btn_submit,R.id.btn_send_code})
    public void OnClickViews(View v){
        switch (v.getId()){
            case R.id.stv_bank_name:
                goToPage(this, SupportBankActivity.class, null);
                break;
            case R.id.btn_send_code:
                getP().sendVerificationCode();
                break;
            case R.id.btn_submit:
                getP().bindCard();
                break;
        }
    }

    @Override
    protected BankcardContract.BankcardPresenter createPresenter() {
        return new BankcardPresenter();
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
        return R.layout.module_activity_add_bankcard;
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
        title_layout.setTitleText("添加银行卡");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
        if (object instanceof BankcardBean){
            mBankcardBean = (BankcardBean) object;
            stv_bank_name.setCenterString(mBankcardBean.getName());
        }

    }

    @Override
    public void onSucceed(int type) {
        super.onSucceed(type);
        if (type == CODE_SEND) {
            SPUtils.getInstance().put(Constant.SMS_CODE_KEY, getNowMills());
            onMessage(gets(R.string.captcha_code_sent));
            btn_send_code.setText(gets(R.string.resend));
            btn_send_code.setTotalTime(60);
            btn_send_code.start();
        }else if (type == 1){
            onMessage(gets(R.string.add_success));
            sendEvent(LocalConstant.CLIENT_REFRESH_BANKCARD_LIST);
            finish();
        }
    }

    @Override
    public void onDataResult(List<BankcardBean> bankcardBeans) {

    }

    @Override
    public String getBankType() {
        if (mBankcardBean!=null)
            return mBankcardBean.getBankType();
        return "";
    }

    @Override
    public String getPersonOfCard() {
        return et_name.getText().toString();
    }

    @Override
    public String getIdCard() {
        return et_card_number.getText().toString();
    }

    @Override
    public String getName() {
        if (mBankcardBean!=null)
            return mBankcardBean.getName();
        return "";
    }

    @Override
    public String getNumber() {
        return et_bank_card_number.getText().toString();
    }

    @Override
    public String getMobile() {
        return et_mobile_number.getText().toString();
    }

    @Override
    public String getCode() {
        return et_certify_code.getText().toString();
    }
}
