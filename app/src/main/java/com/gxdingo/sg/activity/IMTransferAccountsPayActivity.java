package com.gxdingo.sg.activity;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientCouponBean;
import com.gxdingo.sg.bean.IMChatHistoryListBean;
import com.gxdingo.sg.bean.PayBean;
import com.gxdingo.sg.biz.IMTransferAccountsPayContract;
import com.gxdingo.sg.dialog.EnterPaymentPasswordPopupView;
import com.gxdingo.sg.presenter.IMTransferAccountsPayPresenter;
import com.gxdingo.sg.utils.CashierInputFilter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.biz.KeyboardHeightObserver;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.utils.GlideUtils;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;

import butterknife.BindView;
import butterknife.OnClick;

import static com.kikis.commnlibrary.utils.BigDecimalUtils.compare;
import static com.kikis.commnlibrary.utils.BigDecimalUtils.sub;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.Constant.PAYMENT_SUCCESS;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * IM-转账支付
 *
 * @author JM
 */
public class IMTransferAccountsPayActivity extends BaseMvpActivity<IMTransferAccountsPayContract.IMTransferAccountsPayPresenter> implements IMTransferAccountsPayContract.IMTransferAccountsPayListener, KeyboardHeightObserver {
    @BindView(R.id.title_layout)
    TemplateTitle titleLayout;
    @BindView(R.id.tv_go)
    TextView tvGo;
    @BindView(R.id.niv_avatar)
    ImageView nivAvatar;
    @BindView(R.id.tv_payee_name)
    TextView tvPayeeName;
    @BindView(R.id.tv_transfer_amount_title)
    TextView tvTransferAmountTitle;
    @BindView(R.id.tv_transfer_amount)
    LinearLayout tvTransferAmount;

    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    @BindView(R.id.sum_tv)
    TextView sum_tv;

    @BindView(R.id.et_amount)
    EditText etAmount;

    @BindView(R.id.coupon_stv)
    public SuperTextView coupon_stv;

    private String mShareUuid = "";

    private int mPayType = 30;

    private IMChatHistoryListBean.OtherAvatarInfo otherInfo;

    private PayBean.TransferAccountsDTO transferAccounts;

    //优惠券实体类
    private ClientCouponBean couponBean;


    @Override
    protected IMTransferAccountsPayContract.IMTransferAccountsPayPresenter createPresenter() {
        return new IMTransferAccountsPayPresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return true;
    }

    @Override
    protected int activityTitleLayout() {
        return R.layout.module_include_new_custom_title;
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
        return R.layout.module_activity_im_transfer_accounts_pay;
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
        titleLayout.setTitleText("转账支付");
//        etAmount.setInputType(InputType.TYPE_NULL);
        mShareUuid = getIntent().getStringExtra(Constant.SERIALIZABLE + 0);
        mPayType = getIntent().getIntExtra(Constant.SERIALIZABLE + 1, 30);
        otherInfo = (IMChatHistoryListBean.OtherAvatarInfo) getIntent().getSerializableExtra(Constant.SERIALIZABLE + 2);


        if (otherInfo != null) {
            if (!isEmpty(otherInfo.getSendNickname()))
                tvPayeeName.setText(otherInfo.getSendNickname());

            if (!isEmpty(otherInfo.getSendAvatar()))
                Glide.with(reference.get()).load(otherInfo.getSendAvatar()).apply(GlideUtils.getInstance().getGlideRoundOptions(6)).into(nivAvatar);
        }

        InputFilter[] filters = {new CashierInputFilter()};
        etAmount.setFilters(filters); //设置金额输入的过滤器，保证只能输入金额类型
        etAmount.addTextChangedListener(textWatcher);

        if (isEmpty(mShareUuid)) {
            onMessage("没有获取到 shareUuid ");
            finish();
        }
    }

    @Override
    protected void initData() {
        getP().getCoupons(otherInfo.getSendIdentifier());

    }

    @Override
    public void onKeyboardHeightChanged(int height, int orientation) {

    }

    private TextWatcher textWatcher = new TextWatcher() {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            if (!isEmpty(s.toString())) {
                //记录一下总金额
                if (couponBean != null) {
                    if (compare(etAmount.getText().toString(), couponBean.getUseAmount())) {
                        //如果消费超过优惠券的使用门槛，则使用优惠券抵扣金额
                        sum_tv.setText(sub(etAmount.getText().toString(), couponBean.getCouponAmount(), 2));
                        coupon_stv.setRightTextColor(getc(R.color.red));
                    } else {
                        sum_tv.setText(s.toString());
                    }
                } else {
                    sum_tv.setText(s.toString());
                }

            } else {

                sum_tv.setText("0");
            }


        }
    };


    @OnClick({R.id.tv_submit, R.id.coupon_stv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                //设置1秒点击间隔
                if (clickInterval != 1000)
                    clickInterval = 1000;
                if (checkClickInterval(view.getId())) {
                    //余额支付密码弹窗
                    if (mPayType == 30) {
                        new XPopup.Builder(reference.get())
                                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                                .isDarkTheme(false)
                                .dismissOnTouchOutside(false)
                                .asCustom(new EnterPaymentPasswordPopupView(reference.get(), pass -> getP().transfer(mShareUuid, mPayType, pass, sum_tv.getText().toString(), couponBean, etAmount.getText().toString()))).show();
                    } else
                        getP().transfer(mShareUuid, mPayType, "", sum_tv.getText().toString(), couponBean, etAmount.getText().toString());
                }
                break;
            case R.id.coupon_stv:
                goToPagePutSerializable(this, CouponListActivity.class, getIntentEntityMap(new Object[]{1, otherInfo.getSendIdentifier()}));
                break;
        }
    }

    @Override
    public void onSetTransferAccounts(PayBean.TransferAccountsDTO transferAccounts) {
        this.transferAccounts = transferAccounts;
    }

    @Override
    public void setCouponsHint(String s) {
        coupon_stv.setRightString(s);
    }

    @Override
    public void onSucceed(int type) {
        super.onSucceed(type);
        onMessage(gets(R.string.payment_succeed));
        sendEvent(transferAccounts);
        finish();
    }

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
        if (object instanceof ClientCouponBean) {

            couponBean = (ClientCouponBean) object;

            coupon_stv.setRightString(Float.valueOf(couponBean.getUseAmount()) <= 0 ? "无门槛" : "满" + couponBean.getUseAmount() + "减" + couponBean.getCouponAmount());

            if (couponBean != null && couponBean.getStatus() == 0)
                coupon_stv.setRightTextColor(getc(R.color.red));

            if (!isEmpty(etAmount.getText().toString())) {
                if (compare(etAmount.getText().toString(), couponBean.getUseAmount())) {
                    //如果消费超过优惠券的使用门槛，则使用优惠券抵扣金额
                    sum_tv.setText(sub(etAmount.getText().toString(), couponBean.getCouponAmount(), 2));

                }
            } else
                sum_tv.setText("0");

        }
    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type == PAYMENT_SUCCESS && transferAccounts != null) {
            onSucceed(0);
        }

    }
}
