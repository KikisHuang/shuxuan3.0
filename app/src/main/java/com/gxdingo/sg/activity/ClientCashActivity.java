package com.gxdingo.sg.activity;

import android.view.View;

import com.allen.library.SuperTextView;
import com.blankj.utilcode.util.LogUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientAccountTransactionBean;
import com.gxdingo.sg.bean.ClientCashInfoBean;
import com.gxdingo.sg.biz.ClientAccountSecurityContract;
import com.gxdingo.sg.dialog.ClientCashSelectDialog;
import com.gxdingo.sg.presenter.ClientAccountSecurityPresenter;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;
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

    private int type = -1;

    private ClientCashInfoBean cashInfoBean;

    @Override
    protected ClientAccountSecurityContract.ClientAccountSecurityPresenter createPresenter() {
        return new ClientAccountSecurityPresenter();
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
    }

    @Override
    protected void initData() {
        getP().getCashInfo();
    }


    @OnClick({R.id.cash_account_stv,R.id.btn_confirm})
    public void onClickViews(View v){
        switch (v.getId()){
            case R.id.cash_account_stv:
                if (cashInfoBean!=null)
                    new XPopup.Builder(reference.get())
                            .isDarkTheme(false)
                            .asCustom(new ClientCashSelectDialog(reference.get(),cashInfoBean))
                            .show();
                break;
            case R.id.btn_confirm:
                break;
        }
    }

    @Override
    public void onTransactionResult(boolean refresh, List<ClientAccountTransactionBean.ListBean> transactions) {

    }

    @Override
    public void onCashInfoResult(ClientCashInfoBean cashInfoBean) {
        this.cashInfoBean = cashInfoBean;
        if (!isEmpty(cashInfoBean.getAlipay())&&cashInfoBean.getIsShowAlipay()==1){
            cash_account_stv.setLeftIcon(getd(R.drawable.module_svg_alipay_icon));
            cash_account_stv.setLeftString(gets(R.string.alipay));
            type = 20;
        }else if (!isEmpty(cashInfoBean.getWechat())&&cashInfoBean.getIsShowWechat()==1){
            cash_account_stv.setLeftIcon(getd(R.drawable.module_svg_wechat_pay_icon));
            cash_account_stv.setLeftString(gets(R.string.wechat));
            type = 10;
        }else if (cashInfoBean.getBankList()!=null&&cashInfoBean.getBankList().size()>0&&cashInfoBean.getIsShowBank()==1){
            cash_account_stv.setLeftIcon(getd(R.drawable.module_svg_bankcard_pay_icon));
            cash_account_stv.setLeftString(gets(R.string.back_card));
            type = 0;
        }else {
            cash_account_stv.setLeftString("选择提现账户");
        }
    }

    @Override
    public void setUserPhone(String phone) {

    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public void next() {

    }

    @Override
    public void oldPhoneNumberCountDown() {

    }

    @Override
    public void newPhoneNumberCountDown() {

    }

    @Override
    public void changeTitle(String title) {

    }

    @Override
    public void changeHint(String hint) {

    }

    @Override
    public void changeNextBtnText(String text) {

    }

    @Override
    public void bottomHintVisibility(int visib) {

    }

    @Override
    public void oldPhoneCodeCountdownVisibility(int visib) {

    }

    @Override
    public void newPhoneCodeCountdownVisibility(int visib) {

    }

    @Override
    public void countryCodeShow(boolean show) {

    }

    @Override
    public void setEdittextInputType(int type) {

    }

    @Override
    public void setEdittextContent(String content) {

    }

    @Override
    public void setEdittextHint(String hint) {

    }

    @Override
    public int getNumberCountDown() {
        return 0;
    }
}
