package com.gxdingo.sg.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientAccountTransactionBean;
import com.gxdingo.sg.bean.StoreWalletBean;
import com.gxdingo.sg.bean.TransactionBean;
import com.gxdingo.sg.bean.TransactionDetails;
import com.gxdingo.sg.biz.StoreHomeContract;
import com.gxdingo.sg.biz.StoreWalletContract;
import com.gxdingo.sg.presenter.StoreHomePresenter;
import com.gxdingo.sg.presenter.StoreWalletPresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.utils.GlideUtils;
import com.kikis.commnlibrary.view.TemplateTitle;

import butterknife.BindView;

import static com.gxdingo.sg.utils.DateUtils.dealDateFormat;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * @author: Weaving
 * @date: 2021/11/10
 * @page:
 */
public class StoreBillDetailActivity extends BaseMvpActivity<StoreWalletContract.StoreWalletPresenter> implements StoreWalletContract.StoreWalletListener {


    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.avatar_img)
    public ImageView avatar_img;

    @BindView(R.id.transfer_object_tv)
    public TextView transfer_object_tv;

    @BindView(R.id.amount_tv)
    public TextView amount_tv;

    @BindView(R.id.pay_method_stv)
    public SuperTextView pay_method_stv;

    @BindView(R.id.current_status_stv)
    public SuperTextView current_status_stv;

    @BindView(R.id.create_time_stv)
    public SuperTextView create_time_stv;

    @BindView(R.id.receive_time_stv)
    public SuperTextView receive_time_stv;

    @BindView(R.id.pay_time_stv)
    public SuperTextView pay_time_stv;

    @BindView(R.id.payment_time_stv)
    public SuperTextView payment_time_stv;

    private int moneyLogId;

    @Override
    protected StoreWalletContract.StoreWalletPresenter createPresenter() {
        return new StoreWalletPresenter();
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
        return R.layout.module_activity_store_bill_details;
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
        title_layout.setTitleText("账单详情");
        moneyLogId = getIntent().getIntExtra(Constant.SERIALIZABLE + 0, -1);
    }

    @Override
    protected void initData() {
        getP().getTransactionDetails();
    }

    @Override
    public int getBackCardId() {
        return moneyLogId;
    }

    @Override
    public void onWalletHomeResult(boolean refresh, StoreWalletBean walletBean) {

    }

    @Override
    public String getCashType() {
        return null;
    }

    @Override
    public void onTransactionDetail(TransactionDetails transactionDetails) {
        Glide.with(this).load(isEmpty(transactionDetails.getAvatar()) ? R.drawable.module_svg_client_default_avatar : transactionDetails.getAvatar()).apply(GlideUtils.getInstance().getGlideRoundOptions(6))
                .into(avatar_img);
        transfer_object_tv.setText(transactionDetails.getTitle());
        amount_tv.setText(String.valueOf(transactionDetails.getAmount()));
        if (isEmpty(transactionDetails.getPayType()))
            pay_method_stv.setVisibility(View.GONE);
        else
            pay_method_stv.setRightString(transactionDetails.getPayType());
        current_status_stv.setRightString(transactionDetails.getStatusText());
        create_time_stv.setRightString(dealDateFormat(transactionDetails.getCreateTime()));

        if (isEmpty(transactionDetails.getReceiveTime()))
            receive_time_stv.setVisibility(View.GONE);
        else
            receive_time_stv.setRightString(dealDateFormat(transactionDetails.getReceiveTime()));

        if (isEmpty(transactionDetails.getPayTime()))
            pay_time_stv.setVisibility(View.GONE);
        else
            pay_time_stv.setRightString(dealDateFormat(transactionDetails.getPayTime()));

        if (isEmpty(transactionDetails.getPaymentTime()))
            payment_time_stv.setVisibility(View.GONE);
        else
            payment_time_stv.setRightString(dealDateFormat(transactionDetails.getPaymentTime()));
    }

    @Override
    public void onRemindResult(String data) {

    }
}
