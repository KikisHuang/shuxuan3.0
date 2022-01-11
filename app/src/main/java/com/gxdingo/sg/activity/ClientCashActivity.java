package com.gxdingo.sg.activity;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.blankj.utilcode.util.LogUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientAccountTransactionBean;
import com.gxdingo.sg.bean.ClientCashInfoBean;
import com.gxdingo.sg.bean.TransactionBean;
import com.gxdingo.sg.bean.WeChatLoginEvent;
import com.gxdingo.sg.biz.ClientAccountSecurityContract;
import com.gxdingo.sg.biz.OnAccountSelectListener;
import com.gxdingo.sg.biz.PayPasswordListener;
import com.gxdingo.sg.dialog.AuthenticationStatusPopupView;
import com.gxdingo.sg.dialog.ClientCashSelectDialog;
import com.gxdingo.sg.dialog.PayPasswordPopupView;
import com.gxdingo.sg.presenter.ClientAccountSecurityPresenter;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.view.PasswordLayout;
import com.gxdingo.sg.view.RegexEditText;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.BigDecimalUtils;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.core.CenterPopupView;
import com.tencent.smtt.sdk.WebView;
import com.umeng.commonsdk.debug.E;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;
import static com.gxdingo.sg.utils.LocalConstant.AUTHENTICATION_SUCCEEDS;
import static com.kikis.commnlibrary.utils.CommonUtils.getd;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;

/**
 * @author: Weaving
 * @date: 2021/10/15
 * @page:
 */
public class ClientCashActivity extends BaseMvpActivity<ClientAccountSecurityContract.ClientAccountSecurityPresenter> implements ClientAccountSecurityContract.ClientAccountSecurityListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.cash_account_stv)
    public SuperTextView cash_account_stv;

    @BindView(R.id.et_cash_amount)
    public RegexEditText et_cash_amount;

    @BindView(R.id.content_tv)
    public TextView content_tv;

    private String amount = "0.0";

    //0銀行卡 10微信 20支付寶
    private int mtype = -1;

    private int mBankCardId = -1;

    private ClientCashInfoBean cashInfoBean;

    private PasswordLayout mPasswordLayout;

    //显示认证状态弹窗
    private boolean showAuthenticationStatusDialog = false;

    @Override
    protected ClientAccountSecurityContract.ClientAccountSecurityPresenter createPresenter() {
        return new ClientAccountSecurityPresenter();
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
        return R.layout.module_activity_client_cash;
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
        title_layout.setTitleText(gets(R.string.balance_cash));
        et_cash_amount.addTextChangedListener(textWatcher);
        amount = getIntent().getStringExtra(Constant.PARAMAS + 0);
        et_cash_amount.setHint("可转出到卡" + amount + "元");
        et_cash_amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        clickInterval  = 500;
    }

    @Override
    protected void initData() {
        getP().getCashInfo();
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @OnClick({R.id.cash_account_stv, R.id.btn_all, R.id.btn_confirm})
    public void onClickViews(View v) {
        switch (v.getId()) {
            case R.id.cash_account_stv:
                if (cashInfoBean != null) {
                    new XPopup.Builder(reference.get())
                            .isDarkTheme(false)
                            .asCustom(new ClientCashSelectDialog(reference.get(), cashInfoBean, new OnAccountSelectListener() {
                                @Override
                                public void onSelected(BottomPopupView dialog, String account, int type, int bankCardId) {
                                    if (bankCardId > 0) {
                                        mBankCardId = bankCardId;
                                        dialog.dismiss();
                                        cash_account_stv.setLeftIcon(getd(R.drawable.module_svg_bankcard_pay_icon));
                                        cash_account_stv.setLeftString(gets(R.string.back_card));
                                        mtype = 0;
                                        return;
                                    }
                                    if (isEmpty(account)) {
                                        if (type == 0)
                                            getP().bindAli();
                                        else if (type == 1)
                                            getP().bindWechat();
                                    } else {
                                        if (type == 0) {
                                            cash_account_stv.setLeftIcon(getd(R.drawable.module_svg_alipay_icon));
                                            cash_account_stv.setLeftString(gets(R.string.alipay));
                                            mtype = 20;
                                        } else if (type == 1) {
                                            cash_account_stv.setLeftIcon(getd(R.drawable.module_svg_wechat_pay_icon));
                                            cash_account_stv.setLeftString(gets(R.string.wechat));
                                            mtype = 10;
                                        }
                                        dialog.dismiss();
                                    }
                                }
                            }))
                            .show();
                }

                break;
            case R.id.btn_all:
                et_cash_amount.setText(amount);
                break;
            case R.id.btn_confirm:

                if (cashInfoBean != null) {
                    //已认证
                    if (cashInfoBean.authStatus == 1) {

                        String balance = et_cash_amount.getText().toString();
                        if (isEmpty(balance)) {
                            onMessage("请输入提现金额");
                            return;
                        }
                        if (!BigDecimalUtils.compare(balance, "0")) {
                            onMessage("请输入有效提现金额");
                            return;
                        }

                        if (mtype != -1)
                            showPayPswDialog();
                        else
                            onMessage("请选择提现方式");
                    } else
                        showAuthenticationStatusDialog();

                }


                break;
        }
    }


    /**
     * 显示认证状态弹窗
     */
    private void showAuthenticationStatusDialog() {

        //未认证状态直接跳转认证页面
        if (cashInfoBean.authStatus == 0) {
            goToPage(reference.get(), RealNameAuthenticationActivity.class, null);
            onMessage("请先填写实名认证信息");
            return;
        }

        if (!showAuthenticationStatusDialog)
            showAuthenticationStatusDialog = true;

        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .autoDismiss(true)
                .hasShadowBg(true)
                .asCustom(new AuthenticationStatusPopupView(reference.get(), cashInfoBean.authStatus, cashInfoBean.authImage, cashInfoBean.rejectReason, status -> {
                    if (!checkClickInterval(123))
                        return;
                        if (cashInfoBean.authStatus == 2)
                            getP().getCashInfo();
                        else if (cashInfoBean.authStatus == 3)
                            goToPage(reference.get(), RealNameAuthenticationActivity.class, null);
                        else if (cashInfoBean.authStatus == 1) {
                            //认证成功无需操作
                        }
                }).show());
    }


    private void showPayPswDialog() {
        new XPopup.Builder(reference.get())
                .isDarkTheme(false)
                .dismissOnTouchOutside(false)
                .asCustom(new PayPasswordPopupView(reference.get(), new PayPasswordListener() {
                    @Override
                    public void finished(CenterPopupView popupView, PasswordLayout passwordLayout, String password) {
                        mPasswordLayout = passwordLayout;
                        getP().cash(password);
                    }
                }))
                .show();
    }

    @Override
    public void onTransactionResult(boolean refresh, List<TransactionBean> transactions) {

    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type == AUTHENTICATION_SUCCEEDS) {
            showAuthenticationStatusDialog = false;
            getP().getCashInfo();
        }

    }

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
        if (object instanceof WeChatLoginEvent) {
            WeChatLoginEvent event = (WeChatLoginEvent) object;
            if (!isEmpty(event.code))
                getP().bind(event.code, 1);
        }

    }

    @Override
    public void onCashInfoResult(ClientCashInfoBean cashInfoBean) {
        this.cashInfoBean = cashInfoBean;

        if (showAuthenticationStatusDialog)
            showAuthenticationStatusDialog();

        if (!isEmpty(cashInfoBean.getAlipay()) && cashInfoBean.getIsShowAlipay() == 1) {
            cash_account_stv.setLeftIcon(getd(R.drawable.module_svg_alipay_icon));
            cash_account_stv.setLeftString(gets(R.string.alipay));
            mtype = 20;
        } else if (!isEmpty(cashInfoBean.getWechat()) && cashInfoBean.getIsShowWechat() == 1) {
            cash_account_stv.setLeftIcon(getd(R.drawable.module_svg_wechat_pay_icon));
            cash_account_stv.setLeftString(gets(R.string.wechat));
            mtype = 10;
        } else if (cashInfoBean.getBankList() != null && cashInfoBean.getBankList().size() > 0 && cashInfoBean.getIsShowBank() == 1) {
            cash_account_stv.setLeftIcon(getd(R.drawable.module_svg_bankcard_pay_icon));
            cash_account_stv.setLeftString(gets(R.string.back_card));
            mBankCardId = cashInfoBean.getBankList().get(0).getId();
            mtype = 0;
        } else {
            cash_account_stv.setLeftString("选择提现账户");
        }
        if (!isEmpty(cashInfoBean.getExplain()))
            content_tv.setText(cashInfoBean.getExplain());
    }

    @Override
    public String getCashAmount() {
        return et_cash_amount.getText().toString();
    }

    @Override
    public long getBackCardId() {
        return mBankCardId;
    }

    @Override
    public int getType() {
        return mtype;
    }

    private TextWatcher textWatcher = new TextWatcher() {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0,
                            s.toString().indexOf(".") + 3);
                    et_cash_amount.setText(s);
                    et_cash_amount.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                et_cash_amount.setText(s);
                et_cash_amount.setSelection(2);
            }

            if (s.toString().startsWith("0")
                    && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    et_cash_amount.setText(s.subSequence(0, 1));
                    et_cash_amount.setSelection(1);
                    return;
                }
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

            int posDot = et_cash_amount.getText().toString().indexOf(".");
            if (posDot < 0) {
                return;
            } else if (posDot == 0) {
                //首位是点，去掉点
                s.delete(posDot, posDot + 1);
                if (!isEmpty(s.toString())) {
                    if (BigDecimalUtils.compare(s.toString(), amount))
                        et_cash_amount.setText(amount);
                }
            }
        }
    };

    @Override
    public void onSucceed(int type) {
//        sendEvent(LocalConstant.CASH_SUCCESSS);
        finish();
    }

    @Override
    public void onMessage(String msg) {
        super.onMessage(msg);
        if (mPasswordLayout != null)
            mPasswordLayout.removeAllPwd();
    }


}
