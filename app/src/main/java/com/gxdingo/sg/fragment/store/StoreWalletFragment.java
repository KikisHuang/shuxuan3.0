package com.gxdingo.sg.fragment.store;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allen.library.SuperTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.BankcardListActivity;
import com.gxdingo.sg.activity.ClientAccountRecordActivity;
import com.gxdingo.sg.activity.CustomCaptureActivity;
import com.gxdingo.sg.activity.RealNameAuthenticationActivity;
import com.gxdingo.sg.activity.StoreBillDetailActivity;
import com.gxdingo.sg.activity.StoreCashActivity;
import com.gxdingo.sg.adapter.ClientTransactionRecordAdapter;
import com.gxdingo.sg.bean.BankcardBean;
import com.gxdingo.sg.bean.StoreWalletBean;
import com.gxdingo.sg.bean.ThirdPartyBean;
import com.gxdingo.sg.bean.TransactionBean;
import com.gxdingo.sg.bean.TransactionDetails;
import com.gxdingo.sg.bean.WeChatLoginEvent;
import com.gxdingo.sg.biz.StoreWalletContract;
import com.gxdingo.sg.dialog.AuthenticationStatusPopupView;
import com.gxdingo.sg.presenter.StoreWalletPresenter;
import com.gxdingo.sg.utils.ClientLocalConstant;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.StoreLocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.fragment.BaseMvpFragment;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static android.text.TextUtils.isEmpty;
import static com.gxdingo.sg.utils.LocalConstant.AUTHENTICATION_SUCCEEDS;
import static com.gxdingo.sg.utils.StoreLocalConstant.REQUEST_CODE_SCAN;
import static com.kikis.commnlibrary.utils.FormatUtils.double2Str;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * 商家端钱包
 *
 * @author Kikis
 */
public class StoreWalletFragment extends BaseMvpFragment<StoreWalletContract.StoreWalletPresenter> implements StoreWalletContract.StoreWalletListener, OnItemClickListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.title)
    public TextView title;

    @BindView(R.id.balance_tv)
    public TextView balance_tv;

    @BindView(R.id.ll_account)
    public LinearLayout ll_account;

    @BindView(R.id.alipay_stv)
    public SuperTextView alipay_stv;

    @BindView(R.id.wechat_stv)
    public SuperTextView wechat_stv;

    @BindView(R.id.bankcard_stv)
    public SuperTextView bankcard_stv;

    @BindView(R.id.transaction_rv)
    public RecyclerView transaction_rv;

    private ClientTransactionRecordAdapter mAdapter;

    private StoreWalletBean mWalletBean;

    //显示认证状态弹窗
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
        return 0;
    }

    @Override
    protected int initContentView() {
        return R.layout.module_fragment_store_wallet;
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
    protected boolean refreshEnable() {
        return false;
    }

    @Override
    protected boolean loadmoreEnable() {
        return false;
    }

    @Override
    protected void init() {
        title_layout.setTitleText("钱包");
        title.setTextSize(16);
        title.getPaint().setFakeBoldText(true);

        title_layout.setMoreImg(R.drawable.module_svg_scan);
        title_layout.setBackVisible(false);
        mAdapter = new ClientTransactionRecordAdapter();
        transaction_rv.setAdapter(mAdapter);
        transaction_rv.setLayoutManager(new LinearLayoutManager(reference.get()));
        mAdapter.setOnItemClickListener(this);

    }


    @Override
    protected void initData() {

    }

    @Override
    protected void lazyInit() {
        super.lazyInit();
        if (UserInfoUtils.getInstance().isLogin()) {
            showAuthenticationStatusDialog = false;
            getP().getWalletHome(true);
        }
    }

    @OnClick({R.id.img_more, R.id.alipay_stv, R.id.wechat_stv, R.id.bankcard_stv, R.id.record_stv})
    public void OnClickViews(View v) {

        if ((v.getId() == R.id.alipay_stv || v.getId() == R.id.wechat_stv || v.getId() == R.id.bankcard_stv) && mWalletBean != null && mWalletBean.authStatus != 1) {
            showAuthenticationStatusDialog();
            return;
        }

        switch (v.getId()) {
            case R.id.img_more:
//                Intent intent = new Intent(getContext(), CustomCaptureActivity.class);
//                startActivityForResult(intent, REQUEST_CODE_SCAN);

//                getRxPermissions();

                getP().checkPermissions(getRxPermissions());

                break;
            case R.id.alipay_stv:
                if (mWalletBean != null) {
                    goToCash(ClientLocalConstant.ALIPAY);

                }

                break;
            case R.id.wechat_stv:
                if (mWalletBean != null) {
                    goToCash(ClientLocalConstant.WECHAT);
                }

                break;
            case R.id.bankcard_stv:
                if (mWalletBean != null)
                    goToPagePutSerializable(getContext(), BankcardListActivity.class, getIntentEntityMap(new Object[]{true, true}));

                break;
            case R.id.record_stv:
                goToPage(getContext(), ClientAccountRecordActivity.class, null);
                break;
        }
    }


    /**
     * 显示认证状态弹窗
     */
    private void showAuthenticationStatusDialog() {

        //未认证状态直接跳转认证页面
        if (mWalletBean.authStatus == 0) {
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
                .asCustom(new AuthenticationStatusPopupView(reference.get(), mWalletBean.authStatus, mWalletBean.authImage, mWalletBean.rejectReason,status -> {

                    if (mWalletBean.authStatus == 2)
                        getP().getWalletHome(true);
                    else if (mWalletBean.authStatus == 3)
                        goToPage(reference.get(), RealNameAuthenticationActivity.class, null);
                    else if (mWalletBean.authStatus == 1) {
                        //认证成功0无需操作
                    }

                }).show());
    }


    @Override
    public void onSucceed(int type) {
        super.onSucceed(type);
        Intent intent = new Intent(getContext(), CustomCaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    private void goToCash(String type) {

        if (mWalletBean == null) return;

        if (type.equals(ClientLocalConstant.ALIPAY) && isEmpty(mWalletBean.getAlipay())) {
            getP().bindAli();
            return;
        }

        if (type.equals(ClientLocalConstant.WECHAT) && isEmpty(mWalletBean.getWechat())) {
            getP().bindWechat();
            return;
        }


        goToPagePutSerializable(getContext(), StoreCashActivity.class, getIntentEntityMap(new Object[]{type, mWalletBean}));
    }

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
        if (object instanceof WeChatLoginEvent) {
            WeChatLoginEvent event = (WeChatLoginEvent) object;
            if (!isEmpty(event.code))
                getP().bind(event.code, 1);
        } else if (object instanceof ThirdPartyBean) {
            ThirdPartyBean thirdPartyBean = (ThirdPartyBean) object;
            if (thirdPartyBean.type == 0)
                mWalletBean.setAlipay(thirdPartyBean.getNickname());
            else if (thirdPartyBean.type == 1)
                mWalletBean.setWechat(thirdPartyBean.getNickname());
            goToPagePutSerializable(getContext(), StoreCashActivity.class, getIntentEntityMap(new Object[]{thirdPartyBean.type == 1 ? ClientLocalConstant.WECHAT : ClientLocalConstant.ALIPAY, mWalletBean}));
        } else if (object instanceof BankcardBean) {
            BankcardBean bankcardBean = (BankcardBean) object;
            if (bankcardBean.isCash())
                goToPagePutSerializable(getContext(), StoreCashActivity.class, getIntentEntityMap(new Object[]{ClientLocalConstant.BANK, mWalletBean, bankcardBean.getId()}));
        }

    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);

        if (type == LocalConstant.CASH_SUCCESSS)
            getP().getWalletHome(true);

        else if (type == StoreLocalConstant.SOTRE_REVIEW_SUCCEED) {
            getP().getWalletHome(true);
        } else if (type == AUTHENTICATION_SUCCEEDS) {
            showAuthenticationStatusDialog = false;
            getP().getWalletHome(true);
        }
    }

    @Override
    public int getBackCardId() {
        return 0;
    }

    @Override
    public void onWalletHomeResult(boolean refresh, StoreWalletBean walletBean) {
        mWalletBean = walletBean;
        if (refresh) {

            if (showAuthenticationStatusDialog) {
                showAuthenticationStatusDialog();
                if (mWalletBean.authStatus == 1)
                    showAuthenticationStatusDialog = false;
            }

            mAdapter.setList(walletBean.getTransactionList());
            balance_tv.setText(double2Str(walletBean.getBalance()));
            if (walletBean.getIsShowAlipay() == 0 && walletBean.getIsShowWechat() == 0 && walletBean.getIsShowBank() == 0) {
                ll_account.setVisibility(View.GONE);
            } else {
                alipay_stv.setVisibility(walletBean.getIsShowAlipay() == 1 ? View.VISIBLE : View.GONE);
                wechat_stv.setVisibility(walletBean.getIsShowWechat() == 1 ? View.VISIBLE : View.GONE);
                bankcard_stv.setVisibility(walletBean.getIsShowBank() == 1 ? View.VISIBLE : View.GONE);
            }
        } else
            mAdapter.addData(walletBean.getTransactionList());
    }

    @Override
    public String getCashType() {
        return null;
    }

    @Override
    public void onTransactionDetail(TransactionDetails transactionDetails) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {

            if (data != null) {
                //返回的文本内容
                String content = data.getStringExtra("success_result");
//                ToastUtils.showLong(content);
                getP().scanCode(content);
                //返回的BitMap图像
//                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
            }
        }
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        TransactionBean item = (TransactionBean) adapter.getItem(position);
//        goToPagePutSerializable(getContext(), StoreBillDetailActivity.class,getIntentEntityMap(new Object[]{item}));
        goToPagePutSerializable(getContext(), StoreBillDetailActivity.class, getIntentEntityMap(new Object[]{item.getId()}));
    }
}
