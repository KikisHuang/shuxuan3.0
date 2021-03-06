package com.gxdingo.sg.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.BankcardBean;
import com.gxdingo.sg.bean.StoreWalletBean;
import com.gxdingo.sg.bean.TransactionDetails;
import com.gxdingo.sg.biz.PayPasswordListener;
import com.gxdingo.sg.biz.StoreWalletContract;
import com.gxdingo.sg.dialog.AuthenticationStatusPopupView;
import com.gxdingo.sg.dialog.PayPasswordPopupView;
import com.gxdingo.sg.presenter.StoreWalletPresenter;
import com.gxdingo.sg.utils.ClientLocalConstant;
import com.gxdingo.sg.view.PasswordLayout;
import com.gxdingo.sg.view.RegexEditText;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.BigDecimalUtils;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.CenterPopupView;
import com.tencent.smtt.sdk.WebView;

import butterknife.BindView;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;
import static com.gxdingo.sg.utils.LocalConstant.AUTHENTICATION_SUCCEEDS;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.CommonUtils.isQQClientAvailable;
import static com.kikis.commnlibrary.utils.FormatUtils.double2Str;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * @author: Weaving
 * @date: 2021/11/11
 * @page:
 */
public class StoreCashActivity extends BaseMvpActivity<StoreWalletContract.StoreWalletPresenter> implements StoreWalletContract.StoreWalletListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.cash_account_stv)
    public SuperTextView cash_account_stv;

    @BindView(R.id.et_cash_amount)
    public RegexEditText et_cash_amount;

    @BindView(R.id.content_tv)
    public TextView content_tv;

    //	???????????????bank=????????????alipay=????????????wechat=??????
    private String mType;

    private StoreWalletBean mWalletBean;

    private PasswordLayout mPasswordLayout;

    private int mBankCardId = -1;

    //????????????????????????
    private boolean showAuthenticationStatusDialog = false;

    @Override
    protected StoreWalletContract.StoreWalletPresenter createPresenter() {
        return new StoreWalletPresenter();
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
        return R.layout.module_activity_store_cash;
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
        mType = getIntent().getStringExtra(Constant.SERIALIZABLE + 0);
        mWalletBean = (StoreWalletBean) getIntent().getSerializableExtra(Constant.SERIALIZABLE + 1);
        mBankCardId = getIntent().getIntExtra(Constant.SERIALIZABLE + 2, -1);
        if (mWalletBean == null || isEmpty(mType)) {
            onMessage("???????????????????????????");
            finish();

        }
        if (mWalletBean != null)
            et_cash_amount.setHint("???????????????" + mWalletBean.getBalance() + "???");

        et_cash_amount.addTextChangedListener(textWatcher);
        if (mType.equals(ClientLocalConstant.ALIPAY))
            cash_account_stv.setRightString(gets(R.string.alipay));
        else if (mType.equals(ClientLocalConstant.WECHAT))
            cash_account_stv.setRightString(gets(R.string.wechat));
        else
            cash_account_stv.setRightString(gets(R.string.back_card));

        if (mWalletBean != null && !isEmpty(mWalletBean.getExplain()))
            content_tv.setText(mWalletBean.getExplain());

        clickInterval  = 500;
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.cash_account_stv, R.id.btn_all, R.id.btn_confirm})
    public void OnClickViews(View v) {
        switch (v.getId()) {
//            case R.id.cash_account_stv:
//                if (mType == 2)
//                    goToPagePutSerializable(this,BankcardListActivity.class,getIntentEntityMap(new Object[]{true}));
//                break;
            case R.id.btn_all:
                et_cash_amount.setText(mWalletBean.getBalance());
                break;
            case R.id.btn_confirm:

                if (mWalletBean != null) {
                    //?????????
                    if (mWalletBean.authStatus == 1) {
                        String balance = et_cash_amount.getText().toString();
                        if (isEmpty(balance)) {
                            onMessage("?????????????????????");
                            return;
                        }
                        if (!BigDecimalUtils.compare(balance, "0")) {
                            onMessage("???????????????????????????");
                            return;
                        }
                        showPayPswDialog(balance);
                    }else
                        showAuthenticationStatusDialog();
                }

                break;
        }

    }

    private void showPayPswDialog(String balance) {
        new XPopup.Builder(reference.get())
                .isDarkTheme(false)
                .dismissOnTouchOutside(false)
                .asCustom(new PayPasswordPopupView(reference.get(), new PayPasswordListener() {
                    @Override
                    public void finished(CenterPopupView popupView, PasswordLayout passwordLayout, String password) {
                        mPasswordLayout = passwordLayout;
                        getP().cash(balance, password);
                    }
                }))
                .show();
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
            if (!isEmpty(s.toString()))
                if (BigDecimalUtils.compare(s.toString(), mWalletBean.getBalance()))
                    et_cash_amount.setText(mWalletBean.getBalance());
        }
    };

    @Override
    public int getBackCardId() {
        return mBankCardId;
    }

    @Override
    public void onWalletHomeResult(boolean refresh, StoreWalletBean walletBean) {

        mWalletBean = walletBean;
        if (showAuthenticationStatusDialog)
            showAuthenticationStatusDialog();
    }

    @Override
    public String getCashType() {
        return mType;
    }

    @Override
    public void onTransactionDetail(TransactionDetails transactionDetails) {

    }

    @Override
    public void onRemindResult(String data) {

    }

    @Override
    public void onSucceed(int type) {
//        sendEvent(StoreLocalConstant.CASH_SUCCESS);
        finish();
    }

    @Override
    public void onMessage(String msg) {
        super.onMessage(msg);
        if (mPasswordLayout != null)
            mPasswordLayout.removeAllPwd();
    }


    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type == AUTHENTICATION_SUCCEEDS) {
            showAuthenticationStatusDialog = false;
            getP().getWalletHome(true);
        }
    }

    /**
     * ????????????????????????
     */
    private void showAuthenticationStatusDialog() {

        //???????????????????????????????????????
        if (mWalletBean.authStatus == 0) {
            goToPage(reference.get(), RealNameAuthenticationActivity.class, null);
            onMessage("??????????????????????????????");
            return;
        }

        if (!showAuthenticationStatusDialog)
            showAuthenticationStatusDialog = true;

    }
}
