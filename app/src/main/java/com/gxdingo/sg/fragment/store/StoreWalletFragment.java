package com.gxdingo.sg.fragment.store;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allen.library.SuperTextView;
import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.ClientAccountRecordActivity;
import com.gxdingo.sg.activity.StoreBillDetailActivity;
import com.gxdingo.sg.activity.StoreCashActivity;
import com.gxdingo.sg.adapter.ClientTransactionRecordAdapter;
import com.gxdingo.sg.bean.StoreWalletBean;
import com.gxdingo.sg.bean.ThirdPartyBean;
import com.gxdingo.sg.bean.TransactionBean;
import com.gxdingo.sg.bean.WeChatLoginEvent;
import com.gxdingo.sg.biz.StoreWalletContract;
import com.gxdingo.sg.presenter.StoreWalletPresenter;
import com.kikis.commnlibrary.fragment.BaseMvpFragment;
import com.kikis.commnlibrary.view.TemplateTitle;

import butterknife.BindView;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;
import static com.kikis.commnlibrary.utils.CommonUtils.getd;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * 商家端钱包
 *
 * @author JM
 */
public class StoreWalletFragment extends BaseMvpFragment<StoreWalletContract.StoreWalletPresenter> implements StoreWalletContract.StoreWalletListener, OnItemClickListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

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
        title_layout.setMoreImg(R.drawable.module_svg_scan);
        title_layout.setBackVisible(false);
        mAdapter = new ClientTransactionRecordAdapter();
        transaction_rv.setAdapter(mAdapter);
        transaction_rv.setLayoutManager(new LinearLayoutManager(reference.get()));
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
//        getP().getWalletHome(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        getP().getWalletHome(true);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden)
            getP().getWalletHome(true);
    }

    @OnClick({R.id.img_more,R.id.alipay_stv,R.id.wechat_stv,R.id.bankcard_stv,R.id.record_stv})
    public void OnClickViews(View v){
        switch (v.getId()){
            case R.id.img_more:
                break;
            case R.id.alipay_stv:
                getP().goCashPage(0);
                break;
            case R.id.wechat_stv:
                getP().goCashPage(1);
                break;
            case R.id.bankcard_stv:
                getP().goCashPage(2);
                break;
            case R.id.record_stv:
                goToPage(getContext(), ClientAccountRecordActivity.class,null);
                break;
        }
    }

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
        if (object instanceof WeChatLoginEvent) {
            WeChatLoginEvent event = (WeChatLoginEvent) object;
            if (!isEmpty(event.code))
                getP().bind(event.code,1);
        }
//        else if (object instanceof ThirdPartyBean){
//            ThirdPartyBean thirdPartyBean = (ThirdPartyBean) object;
//            if (thirdPartyBean.type == 0)
//                walletBean.setAlipay(thirdPartyBean.getNickname());
//            else if (thirdPartyBean.type == 1)
//                walletBean.setWechat(thirdPartyBean.getNickname());
//            goToPagePutSerializable(getContext(), StoreCashActivity.class,getIntentEntityMap(new Object[]{thirdPartyBean.type,walletBean}));
//        }

    }

    @Override
    public int getBackCardId() {
        return 0;
    }

    @Override
    public void onWalletHomeResult(boolean refresh,StoreWalletBean walletBean) {

        if (refresh){
            mAdapter.setList(walletBean.getTransactionList());
            balance_tv.setText(String.valueOf(walletBean.getBalance()));
            if (walletBean.getIsShowAlipay()==0 && walletBean.getIsShowWechat() == 0 && walletBean.getIsShowBank() ==0){
                ll_account.setVisibility(View.GONE);
            }else {
                alipay_stv.setVisibility(walletBean.getIsShowAlipay()==1?View.VISIBLE:View.GONE);
                wechat_stv.setVisibility(walletBean.getIsShowWechat()==1?View.VISIBLE:View.GONE);
                bankcard_stv.setVisibility(walletBean.getIsShowBank()==1?View.VISIBLE:View.GONE);
            }
        }

        else
            mAdapter.addData(walletBean.getTransactionList());
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        TransactionBean item = (TransactionBean) adapter.getItem(position);
        goToPagePutSerializable(getContext(), StoreBillDetailActivity.class,getIntentEntityMap(new Object[]{item}));
    }
}
