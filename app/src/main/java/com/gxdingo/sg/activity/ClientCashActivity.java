package com.gxdingo.sg.activity;

import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.BankcardBean;
import com.gxdingo.sg.bean.ClientCashInfoBean;
import com.gxdingo.sg.bean.TransactionBean;
import com.gxdingo.sg.bean.WeChatLoginEvent;
import com.gxdingo.sg.biz.ClientAccountSecurityContract;
import com.gxdingo.sg.biz.OnAccountSelectListener;
import com.gxdingo.sg.biz.PayPasswordListener;
import com.gxdingo.sg.dialog.AuthenticationStatusPopupView;
import com.gxdingo.sg.dialog.ClientCashSelectDialog;
import com.gxdingo.sg.dialog.OneSentenceHintPopupView;
import com.gxdingo.sg.dialog.PayPasswordPopupView;
import com.gxdingo.sg.presenter.ClientAccountSecurityPresenter;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.view.PasswordLayout;
import com.gxdingo.sg.view.RegexEditText;
import com.kikis.commnlibrary.activitiy.BaseActivity;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.bean.SubscribesListBean;
import com.kikis.commnlibrary.utils.BigDecimalUtils;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.utils.GlideUtils;
import com.kikis.commnlibrary.utils.MessageCountManager;
import com.kikis.commnlibrary.utils.RxUtil;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.core.CenterPopupView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

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

    @BindView(R.id.methods_name_tv)
    public TextView methods_name_tv;

    @BindView(R.id.methods_icon_img)
    public ImageView methods_icon_img;

    @BindView(R.id.et_cash_amount)
    public RegexEditText et_cash_amount;

    private String mBottomExplain = "";
    private String amount = "0.0";

    //0銀行卡 10微信 20支付寶
    private int mtype = -1;

    private int mBankCardId = -1;

    private ClientCashInfoBean cashInfoBean;

    private PasswordLayout mPasswordLayout;

    private ClientCashSelectDialog clientCashSelectDialog;


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
        title_layout.setMoreText("说明");
        amount = getIntent().getStringExtra(Constant.PARAMAS + 0);
        et_cash_amount.setHint("可转出到卡" + amount + "元");
        et_cash_amount.setText(amount + "");
        et_cash_amount.setSelection(et_cash_amount.getText().toString().length());
        et_cash_amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        clickInterval = 500;
    }

    @Override
    protected void initData() {


        getP().getCashInfo();
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @OnClick({R.id.btn_more, R.id.cash_account_cl, R.id.btn_confirm})
    public void onClickViews(View v) {
        checkClickInterval(v.getId());
        switch (v.getId()) {
            case R.id.btn_more:
                if (!isEmpty(mBottomExplain)) {
                    new XPopup.Builder(reference.get())
                            .isDarkTheme(false)
                            .hasShadowBg(true)
                            .asCustom(new OneSentenceHintPopupView(reference.get(), mBottomExplain))
                            .show();
                }
                break;
            case R.id.cash_account_cl:
                if (cashInfoBean != null) {
                    showCashSelectDialog();
                }
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
                    } else {
                        //未认证状态直接跳转认证页面
                        if (cashInfoBean.authStatus == 0) {
                            goToPage(reference.get(), RealNameAuthenticationActivity.class, null);
                            onMessage("请先填写实名认证信息");
                            return;
                        }
                    }
                }


                break;
        }
    }

    private void showCashSelectDialog() {
        if (clientCashSelectDialog == null) {
            cashInfoBean.setIsShowWechat(1);
            clientCashSelectDialog = new ClientCashSelectDialog(reference.get(), cashInfoBean, new OnAccountSelectListener() {
                @Override
                public void onSelected(String account, int type, int bankCardId, String cardName, String icon) {


                    if (type == 998) {
                        if (isEmpty(account))
                            getP().bindAli();
                        else {
                            mBankCardId = bankCardId;
                            //支付宝
                            methods_name_tv.setText("支付宝");
                            Glide.with(reference.get()).load(R.drawable.module_svg_alipay_icon).into(methods_icon_img);
                            mtype = 20;
                            if (clientCashSelectDialog != null)
                                clientCashSelectDialog.dismiss();
                        }

                    } else if (type == 999) {
                        if (isEmpty(account))
                            getP().bindWechat();
                        else {

                            mBankCardId = bankCardId;
                            methods_name_tv.setText("微信");
                            Glide.with(reference.get()).load(R.drawable.module_svg_wechat_pay_icon).into(methods_icon_img);
                            mtype = 10;
                            if (clientCashSelectDialog != null)
                                clientCashSelectDialog.dismiss();
                        }

                    } else if (type == 2) {
                        mBankCardId = bankCardId;
                        //银行卡
                        methods_name_tv.setText(cardName);
                        Glide.with(reference.get()).load(icon).into(methods_icon_img);
                        mtype = 0;

                        if (clientCashSelectDialog != null)
                            clientCashSelectDialog.dismiss();
                    } else if (type == 3) {
                        //刷新
                        getP().getCardList(true);
                    } else if (type == 4) {
                        //加载更多
                        getP().getCardList(false);
                    }
                }
            });

            new XPopup.Builder(reference.get())
                    .isDarkTheme(false)
                    .asCustom(clientCashSelectDialog)
                    .show();
        } else
            clientCashSelectDialog.show();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (clientCashSelectDialog != null) {
            clientCashSelectDialog.destroy();
            clientCashSelectDialog = null;
        }
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
        if (type == AUTHENTICATION_SUCCEEDS)
            getP().getCashInfo();

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

        int bankCardId = SPUtils.getInstance().getInt(LocalConstant.CASH_SELECTED_ID_KEY, 0);

        if (bankCardId == 998 && !isEmpty(cashInfoBean.getAlipay()) && cashInfoBean.getIsShowAlipay() == 1) {
            methods_name_tv.setText("支付宝");
            Glide.with(reference.get()).load(R.drawable.module_svg_alipay_icon).into(methods_icon_img);
            mtype = 20;
        } else if (bankCardId == 999 && !isEmpty(cashInfoBean.getWechat()) && cashInfoBean.getIsShowWechat() == 1) {
            methods_name_tv.setText("微信");
            Glide.with(reference.get()).load(R.drawable.module_svg_wechat_pay_icon).into(methods_icon_img);
            mtype = 10;
        } else if (cashInfoBean.getBankList() != null && cashInfoBean.getBankList().size() > 0 && cashInfoBean.getIsShowBank() == 1) {
            RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {
                for (int i = 0; i < cashInfoBean.getBankList().size(); i++) {
                    BankcardBean data = cashInfoBean.getBankList().get(i);
                    if (data.getId() == bankCardId) {
                        e.onNext(i);
                        break;
                    }
                }
                e.onComplete();
            }), (BaseActivity) reference.get()).subscribe(o -> {
                int pos = (int) o;

                if (!isEmpty(cashInfoBean.getBankList().get(pos).getNumber())) {
                    String num = cashInfoBean.getBankList().get(pos).getNumber().substring(cashInfoBean.getBankList().get(pos).getNumber().length() - 4, cashInfoBean.getBankList().get(pos).getNumber().length());
                    methods_name_tv.setText(cashInfoBean.getBankList().get(pos).getCardName() + "(" + num + ")");
                }
                mtype = 0;
                Glide.with(reference.get()).load(cashInfoBean.getBankList().get(pos).getIcon()).into(methods_icon_img);
            });

        }

        if (!isEmpty(cashInfoBean.getExplain()))
            mBottomExplain = cashInfoBean.getExplain();
    }


    /**
     * @param @param drw
     * @return void
     * @desc 设置左边图标
     */
    public void setAlertLeftIcon(TextView tv, Drawable drw) {
        drw.setBounds(0, 0, drw.getMinimumWidth(), drw.getMinimumHeight());
        tv.setCompoundDrawables(drw, null, null, null);
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

    @Override
    public void onDataResult(ArrayList<BankcardBean> list, boolean b) {
        if (clientCashSelectDialog != null) {
            clientCashSelectDialog.setData(list, b);
        }

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
