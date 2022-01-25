package com.gxdingo.sg.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allen.library.SuperTextView;
import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.BankcardListActivity;
import com.gxdingo.sg.adapter.DialogBankcardAdapter;
import com.gxdingo.sg.bean.BankcardBean;
import com.gxdingo.sg.bean.ClientCashInfoBean;
import com.gxdingo.sg.bean.ThirdPartyBean;
import com.gxdingo.sg.biz.OnAccountSelectListener;
import com.gxdingo.sg.utils.LocalConstant;
import com.lxj.xpopup.core.BottomPopupView;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.CommonUtils.getd;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;
import static com.kikis.commnlibrary.utils.ScreenUtils.dp2px;

/**
 * @author: Weaving
 * @date: 2021/10/19
 * @page:
 */
public class ClientCashSelectDialog extends BottomPopupView {

    @BindView(R.id.alipay_stv)
    public SuperTextView alipay_stv;

    @BindView(R.id.wechaty_stv)
    public SuperTextView wechaty_stv;

    @BindView(R.id.add_bakcard_stv)
    public SuperTextView add_bakcard_stv;

    private OnAccountSelectListener listener;

    private ClientCashInfoBean cashInfoBean;

    //回调选择的银行卡id
    private int bankCardId;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.smartrefreshlayout)
    public SmartRefreshLayout smartrefreshlayout;

    private DialogBankcardAdapter mAdapter;


    public ClientCashSelectDialog(@NonNull Context context, ClientCashInfoBean cashInfoBean, OnAccountSelectListener listener) {
        super(context);
        this.cashInfoBean = cashInfoBean;
        this.listener = listener;
    }

    public void setData(List<BankcardBean> data, boolean refresh) {
        if (mAdapter != null) {

            if (refresh) {
                mAdapter.setList(data);
                if (smartrefreshlayout != null)
                    smartrefreshlayout.finishRefresh();
            } else {
                mAdapter.addData(data);
                if (smartrefreshlayout != null)
                    smartrefreshlayout.finishLoadMore();
            }

        }

    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_cash_selecte;
    }

    @Override
    public void setMinimumHeight(int minHeight) {
        super.setMinimumHeight(dp2px(350));
    }

    @Override
    protected int getMaxHeight() {
        return dp2px(350);
    }

    @Override
    protected void onShow() {
        super.onShow();
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        EventBus.getDefault().register(this);
        ButterKnife.bind(this, bottomPopupContainer);

        if (cashInfoBean.getIsShowAlipay() == 0)
            alipay_stv.setVisibility(GONE);
        else if (cashInfoBean.getIsShowWechat() == 0)
            wechaty_stv.setVisibility(GONE);

        bankCardId = SPUtils.getInstance().getInt(LocalConstant.CASH_SELECTED_ID_KEY, 0);
        //支付宝id 998 、微信 999
        if (bankCardId == 998 && cashInfoBean.getIsShowAlipay() == 1)
            alipay_stv.setRightIcon(R.drawable.module_svg_cash_selecte);
        else if (bankCardId == 999 && cashInfoBean.getIsShowWechat() == 1)
            wechaty_stv.setRightIcon(R.drawable.module_svg_cash_selecte);


        mAdapter = new DialogBankcardAdapter(bankCardId);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (listener != null) {
                bankCardId = cashInfoBean.getBankList().get(position).getId();
                mAdapter.setBankCardId(bankCardId);
                mAdapter.notifyDataSetChanged();
                alipay_stv.setRightIcon(0);
                wechaty_stv.setRightIcon(0);
                SPUtils.getInstance().put(LocalConstant.CASH_SELECTED_ID_KEY, bankCardId);

                if (!isEmpty(cashInfoBean.getBankList().get(position).getNumber())) {
                    String num = cashInfoBean.getBankList().get(position).getNumber().substring(cashInfoBean.getBankList().get(position).getNumber().length() - 4, cashInfoBean.getBankList().get(position).getNumber().length());

                    String name = cashInfoBean.getBankList().get(position).getCardName() + "(" + num + ")";
                    listener.onSelected("", 2, bankCardId, name, mAdapter.getData().get(position).getIcon());
                }
            }
        });
        recyclerView.setAdapter(mAdapter);
        //type 3刷新
        if (listener != null)
            listener.onSelected("", 3, 0, null, null);

        refreshInit();

        recyclerView.setVisibility(cashInfoBean.getIsShowBank() == 0 ? GONE : VISIBLE);

    }

    private void refreshInit() {

        //是否在加载的时候禁止列表的操作
        smartrefreshlayout.setDisableContentWhenLoading(false);

        smartrefreshlayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (listener != null)
                    listener.onSelected("", 4, 0, null, null);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

            }
        });

        //是否在列表不满一页时候开启上拉加载功能
//        refreshLayout.setEnableLoadMoreWhenContentNotFull(false);

        //是否启用加载更多
        smartrefreshlayout.setEnableLoadMore(true);
        //是否启用刷新
        smartrefreshlayout.setEnableRefresh(false);

        //关闭越界拖动
        smartrefreshlayout.setEnableOverScrollDrag(false);

        MaterialHeader materialHeader = new MaterialHeader(getContext());
        materialHeader.setColorSchemeResources(com.kikis.commnlibrary.R.color.green_dominant_tone);
        smartrefreshlayout.setRefreshHeader(materialHeader);
        smartrefreshlayout.setRefreshFooter(new ClassicsFooter(getContext()));
    }


    @OnClick({R.id.alipay_stv, R.id.wechaty_stv, R.id.add_bakcard_stv})
    public void OnClickViews(View v) {
        switch (v.getId()) {
            case R.id.add_bakcard_stv:
                goToPagePutSerializable(getContext(), BankcardListActivity.class, getIntentEntityMap(new Object[]{true}));
                break;
            case R.id.alipay_stv:
                if (listener != null) {
                    if (mAdapter != null && !isEmpty(cashInfoBean.getAlipay())) {
                        alipay_stv.setRightIcon(R.drawable.module_svg_cash_selecte);
                        mAdapter.setBankCardId(998);
                        mAdapter.notifyDataSetChanged();
                        wechaty_stv.setRightIcon(0);
                        //支付宝id 998
                        SPUtils.getInstance().put(LocalConstant.CASH_SELECTED_ID_KEY, 998);
                    }

                    listener.onSelected(cashInfoBean.getAlipay(), 998, 998, null, null);
                }
                break;
            case R.id.wechaty_stv:
                if (listener != null) {
                    if (mAdapter != null && !isEmpty(cashInfoBean.getWechat())) {
                        mAdapter.setBankCardId(999);
                        mAdapter.notifyDataSetChanged();
                        wechaty_stv.setRightIcon(R.drawable.module_svg_cash_selecte);
                        alipay_stv.setRightIcon(0);

                        //微信id 999
                        SPUtils.getInstance().put(LocalConstant.CASH_SELECTED_ID_KEY, 999);
                    }

                    listener.onSelected(cashInfoBean.getWechat(), 999, 999, null, null);
                }
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBIndEvent(Object object) {

        if (object instanceof ThirdPartyBean) {
            ThirdPartyBean thirdPartyBean = (ThirdPartyBean) object;
            if (thirdPartyBean.type == 0 && !isEmpty(thirdPartyBean.getNickname())) {
                bankCardId = 998;
                SPUtils.getInstance().put(LocalConstant.CASH_SELECTED_ID_KEY, bankCardId);
                cashInfoBean.setAlipay(thirdPartyBean.getNickname());
                alipay_stv.setRightIcon(R.drawable.module_svg_cash_selecte);
                mAdapter.setBankCardId(998);
                mAdapter.notifyDataSetChanged();
                wechaty_stv.setRightIcon(0);
            } else if (thirdPartyBean.type == 1 && !isEmpty(thirdPartyBean.getNickname())) {
                bankCardId = 999;
                SPUtils.getInstance().put(LocalConstant.CASH_SELECTED_ID_KEY, bankCardId);
                cashInfoBean.setWechat(thirdPartyBean.getNickname());
                wechaty_stv.setRightIcon(R.drawable.module_svg_cash_selecte);
                alipay_stv.setRightIcon(0);
                mAdapter.setBankCardId(999);
                mAdapter.notifyDataSetChanged();
            }
        } else if (object instanceof BankcardBean) {
            BankcardBean bankcardBean = (BankcardBean) object;

            if (!bankcardBean.getType().equals("10")) {
                bankCardId = bankcardBean.getId();
                SPUtils.getInstance().put(LocalConstant.CASH_SELECTED_ID_KEY, bankCardId);
                mAdapter.setBankCardId(bankCardId);
                if (listener != null) {
                    //刷新
                    listener.onSelected("", 3, 0, null, null);
                    //选中该账户
                    listener.onSelected("", 2, bankCardId, bankcardBean.getName(), bankcardBean.getIcon());
                }

            }

        }

    }
}
