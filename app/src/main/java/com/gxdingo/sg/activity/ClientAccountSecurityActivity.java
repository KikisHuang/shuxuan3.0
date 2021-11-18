package com.gxdingo.sg.activity;

import android.view.View;

import com.allen.library.SuperTextView;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientAccountTransactionBean;
import com.gxdingo.sg.bean.ClientCashInfoBean;
import com.gxdingo.sg.bean.ThirdPartyBean;
import com.gxdingo.sg.bean.TransactionBean;
import com.gxdingo.sg.bean.WeChatLoginEvent;
import com.gxdingo.sg.biz.ClientAccountSecurityContract;
import com.gxdingo.sg.biz.MyConfirmListener;
import com.gxdingo.sg.dialog.SgConfirm2ButtonPopupView;
import com.gxdingo.sg.presenter.ClientAccountSecurityPresenter;
import com.gxdingo.sg.utils.pay.WechatUtils;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;
import com.tencent.bugly.beta.Beta;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.AppUtils.getAppVersionName;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.Constant.LOGOUT;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * @author: Weaving
 * @date: 2021/10/15
 * @page:
 */
public class ClientAccountSecurityActivity extends BaseMvpActivity<ClientAccountSecurityContract.ClientAccountSecurityPresenter> implements ClientAccountSecurityContract.ClientAccountSecurityListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.pay_psw_stv)
    public SuperTextView pay_psw_stv;

    @BindView(R.id.binding_phone_stv)
    public SuperTextView binding_phone_stv;

    @BindView(R.id.binding_ali_stv)
    public SuperTextView binding_ali_stv;

    @BindView(R.id.binding_wechat_stv)
    public SuperTextView binding_wechat_stv;

    @BindView(R.id.binding_bankcard_stv)
    public SuperTextView binding_bankcard_stv;

    @BindView(R.id.version_stv)
    public SuperTextView version_stv;

    @BindView(R.id.cancel_account_stv)
    public SuperTextView cancel_account_stv;

    private ClientCashInfoBean cashInfoBean;


    @OnClick({R.id.pay_psw_stv,R.id.binding_phone_stv,R.id.binding_ali_stv,R.id.binding_wechat_stv,R.id.binding_bankcard_stv,R.id.version_stv,R.id.cancel_account_stv,})
    public void onClickViews(View v){
        switch (v.getId()){
            case R.id.pay_psw_stv:
                if (cashInfoBean==null)
                    return;
                if (cashInfoBean.getWithdrawalPassword()==0)
                    goToPage(this,ClientSettingPayPwd1Activity.class,null);
                else
                    goToPage(this,ClientUpdatePayPwdActivity.class,null);
                break;
            case R.id.binding_phone_stv:
                goToPage(this,ChangeBindingPhoneActivity.class,null);
                break;
            case R.id.binding_ali_stv:
                if (isEmpty(cashInfoBean.getAlipay()))
                    getP().bindAli();
                else {
                    new XPopup.Builder(reference.get())
                            .isDarkTheme(false)
                            .asCustom(new SgConfirm2ButtonPopupView(reference.get(), "确定解绑支付宝？", new MyConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    getP().unbind(0);
                                }
                            })).show();
                }
                break;
            case R.id.binding_wechat_stv:
                if (isEmpty(cashInfoBean.getWechat()))
                    getP().bindWechat();
                else {
                    new XPopup.Builder(reference.get())
                            .isDarkTheme(false)
                            .asCustom(new SgConfirm2ButtonPopupView(reference.get(), "确定解绑微信？", new MyConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    getP().unbind(1);
                                }
                            })).show();
                }
                break;
            case R.id.binding_bankcard_stv:
                goToPage(this, BankcardListActivity.class,null);
                break;
            case R.id.version_stv:
                Beta.checkUpgrade(true, false);
                break;
            case R.id.cancel_account_stv:
                new XPopup.Builder(reference.get())
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .isDarkTheme(false)
                        .dismissOnTouchOutside(false)
                        .asCustom(new SgConfirm2ButtonPopupView(reference.get(), gets(R.string.confirm_logoff), "账号注销后无法恢复，请谨慎操作", () -> getP().loginOff()))
                        .show();
                break;
        }
    }

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
        if (object instanceof WeChatLoginEvent)
            getP().bind(((WeChatLoginEvent) object).code, 1);
        else if (object instanceof ThirdPartyBean){
            ThirdPartyBean thirdPartyBean = (ThirdPartyBean)object;
            if (thirdPartyBean.type==0){
                binding_ali_stv.setRightString("解绑");
                cashInfoBean.setAlipay(thirdPartyBean.getNickname());
            }else if (thirdPartyBean.type==1){
                binding_wechat_stv.setRightString("解绑");
                cashInfoBean.setWechat(thirdPartyBean.getNickname());
            }
            binding_ali_stv.setRightTextColor(getc(R.color.gray_a9));
        }
    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type == LOGOUT)
            finish();
    }

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
        return R.layout.module_activity_client_account_security;
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
        title_layout.setTitleText(gets(R.string.account_security));
        version_stv.setRightString("当前版本 v"+getAppVersionName());
    }

    @Override
    public void onSucceed(int type) {
        super.onSucceed(type);
        getP().getCashInfo();
    }

    @Override
    protected void initData() {
        getP().getCashInfo();
    }

    @Override
    public void onTransactionResult(boolean refresh, List<TransactionBean> transactions) {

    }

    @Override
    public void onCashInfoResult(ClientCashInfoBean cashInfoBean) {
        this.cashInfoBean = cashInfoBean;
        if (cashInfoBean.getWithdrawalPassword()==0)
            pay_psw_stv.setRightString("去设置");
        else
            pay_psw_stv.setRightString("修改");
        if (!isEmpty(cashInfoBean.getMobile()))
            binding_phone_stv.setRightString(cashInfoBean.getMobile());

        if (isEmpty(cashInfoBean.getAlipay())){
            binding_ali_stv.setRightString("去绑定");
            binding_ali_stv.setRightTextColor(getc(R.color.blue_text));
        }else {
            binding_ali_stv.setRightString("解绑");
            binding_ali_stv.setRightTextColor(getc(R.color.gray_a9));
        }

        if (isEmpty(cashInfoBean.getWechat())){
            binding_wechat_stv.setRightString("去绑定");
            binding_wechat_stv.setRightTextColor(getc(R.color.blue_text));
        }else {
            binding_wechat_stv.setRightString("解绑");
            binding_wechat_stv.setRightTextColor(getc(R.color.gray_a9));
        }
    }

    @Override
    public String getCashAmount() {
        return null;
    }

    @Override
    public long getBackCardId() {
        return 0;
    }

    @Override
    public int getType() {
        return 0;
    }

}
