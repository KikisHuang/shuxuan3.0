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
import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.material.appbar.AppBarLayout;
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
import com.gxdingo.sg.biz.AppBarStateChangeListener;
import com.gxdingo.sg.biz.MyConfirmListener;
import com.gxdingo.sg.biz.StoreWalletContract;
import com.gxdingo.sg.dialog.AuthenticationStatusPopupView;
import com.gxdingo.sg.dialog.ScanningHintPopupView;
import com.gxdingo.sg.dialog.SgConfirm2ButtonPopupView;
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
import static com.gxdingo.sg.utils.SignatureUtils.getAcType;
import static com.gxdingo.sg.utils.SignatureUtils.numberDecode;
import static com.gxdingo.sg.utils.StoreLocalConstant.REQUEST_CODE_SCAN;
import static com.kikis.commnlibrary.utils.ColorUtils.changeAlpha;
import static com.kikis.commnlibrary.utils.CommonUtils.goNotifySetting;
import static com.kikis.commnlibrary.utils.FormatUtils.double2Str;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * ???????????????
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

    @BindView(R.id.appbar_layout)
    public AppBarLayout appbar_layout;

    @BindView(R.id.record_stv)
    public SuperTextView record_stv;

    @BindView(R.id.divide)
    public View divide;

    @BindView(R.id.transaction_rv)
    public RecyclerView transaction_rv;

    private ClientTransactionRecordAdapter mAdapter;

    private StoreWalletBean mWalletBean;

    private String scanContent  = "";

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
        return R.layout.module_fragment_new_store_wallet;
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
        title_layout.setTitleText("??????");
        title.setTextSize(16);
        title.getPaint().setFakeBoldText(true);

        title_layout.setMoreImg(R.drawable.module_svg_scan);
        title_layout.setBackVisible(false);
        mAdapter = new ClientTransactionRecordAdapter();
        transaction_rv.setAdapter(mAdapter);
        transaction_rv.setLayoutManager(new LinearLayoutManager(reference.get()));
        mAdapter.setOnItemClickListener(this);
        AppbarInit();
    }

    private void AppbarInit() {

        appbar_layout.addOnOffsetChangedListener(new AppBarStateChangeListener() {

            @Override
            public void onStateChanged(AppBarLayout appBarLayout, int state, int verticalOffset) {

            }

            @Override
            public void onStateOffset(AppBarLayout appBarLayout, int verticalOffset) {

            }
        });
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void lazyInit() {
        super.lazyInit();
        if (UserInfoUtils.getInstance().isLogin()) {
            getP().getWalletHome(true);
        }
    }

    @OnClick({R.id.img_more, R.id.alipay_stv, R.id.wechat_stv, R.id.bankcard_stv, R.id.record_stv})
    public void OnClickViews(View v) {


        switch (v.getId()) {
            case R.id.img_more:
//                Intent intent = new Intent(getContext(), CustomCaptureActivity.class);
//                startActivityForResult(intent, REQUEST_CODE_SCAN);

//                getRxPermissions();

                getP().checkPermissions(getRxPermissions());

                break;
            case R.id.alipay_stv:
                if (mWalletBean != null && mWalletBean.getIsShowAlipay() == 1)
                    goToCash(ClientLocalConstant.ALIPAY);
                else
                    onMessage("?????????????????????????????????");

                break;
            case R.id.wechat_stv:
                if (mWalletBean != null && mWalletBean.getIsShowWechat() == 1)
                    goToCash(ClientLocalConstant.WECHAT);
                else
                    onMessage("?????????????????????????????????");

                break;
            case R.id.bankcard_stv:
                if (mWalletBean != null && mWalletBean.getIsShowBank() == 1)
                    goToPagePutSerializable(getContext(), BankcardListActivity.class, getIntentEntityMap(new Object[]{true, true}));
                else
                    onMessage("?????????????????????????????????");
                break;
            case R.id.record_stv:
                goToPage(getContext(), ClientAccountRecordActivity.class, null);
                break;
        }
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

            banAppBarScroll(walletBean != null && walletBean.getTransactionList() != null && walletBean.getTransactionList().size() > 3);

            mAdapter.setList(walletBean.getTransactionList());
            balance_tv.setText(walletBean.getBalance());
         /*   if (walletBean.getIsShowAlipay() == 0 && walletBean.getIsShowWechat() == 0 && walletBean.getIsShowBank() == 0) {
                ll_account.setVisibility(View.GONE);
            } else {
                alipay_stv.setVisibility(walletBean.getIsShowAlipay() == 1 ? View.VISIBLE : View.GONE);
                wechat_stv.setVisibility(walletBean.getIsShowWechat() == 1 ? View.VISIBLE : View.GONE);
                bankcard_stv.setVisibility(walletBean.getIsShowBank() == 1 ? View.VISIBLE : View.GONE);
            }*/
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
    public void onRemindResult(String data) {
        ScanningHintPopupView dialog =  new ScanningHintPopupView(reference.get(), data, flag -> {
                SPUtils.getInstance().put(LocalConstant.SCANNING_NO_REMIND, (Boolean) flag);
                getP().scanCode(scanContent);
        });
        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true)
                .isDarkTheme(false)
                .asCustom(dialog).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // ???????????????/????????????
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {

            if (data != null) {
                //?????????????????????
                 scanContent = data.getStringExtra("success_result");
                int mType = getAcType(numberDecode(scanContent));
                if (mType == 11) {
                    boolean showDialog = SPUtils.getInstance().getBoolean(LocalConstant.SCANNING_NO_REMIND, false);

                    if (!showDialog)
                        getP().getNoRemindContent();
                     else
                        getP().scanCode(scanContent);

                } else
                    onMessage("??????????????????????????????");
                //?????????BitMap??????
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


    /**
     * ??????appbar?????????
     *
     * @param isScroll true ???????????? false ????????????
     */
    private void banAppBarScroll(boolean isScroll) {
        View mAppBarChildAt = appbar_layout.getChildAt(0);
        AppBarLayout.LayoutParams mAppBarParams = (AppBarLayout.LayoutParams) mAppBarChildAt.getLayoutParams();
        if (isScroll) {
            mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
            mAppBarChildAt.setLayoutParams(mAppBarParams);
        } else {
            mAppBarParams.setScrollFlags(0);
        }

    }
}
