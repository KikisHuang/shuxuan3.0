package com.gxdingo.sg.activity;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.donkingliang.labels.LabelsView;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.ClientTransactionRecordAdapter;
import com.gxdingo.sg.bean.ClientAccountTransactionBean;
import com.gxdingo.sg.bean.ClientCashInfoBean;
import com.gxdingo.sg.bean.TransactionBean;
import com.gxdingo.sg.biz.ClientAccountSecurityContract;
import com.gxdingo.sg.biz.ClientMineContract;
import com.gxdingo.sg.biz.ClientPickerDateListener;
import com.gxdingo.sg.dialog.SelectDateDialog;
import com.gxdingo.sg.presenter.ClientAccountSecurityPresenter;
import com.gxdingo.sg.presenter.ClientMinePresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.gxdingo.sg.utils.LocalConstant.LOGIN_WAY;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * @author: Weaving
 * @date: 2021/10/14
 * @page:
 */
public class ClientAccountRecordActivity extends BaseMvpActivity<ClientAccountSecurityContract.ClientAccountSecurityPresenter> implements ClientAccountSecurityContract.ClientAccountSecurityListener, OnItemClickListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.date_tv)
    public TextView date_tv;

    @BindView(R.id.account_record_lv)
    public LabelsView account_record_lv;

    @BindView(R.id.smartrefreshlayout)
    public SmartRefreshLayout smartrefreshlayout;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.nodata_layout)
    public View nodata_layout;

    private ClientTransactionRecordAdapter mAdapter;

    private List<String> labels ;

    //0=支出；1=收入 -1全部
    private int status = -1;

    private String date ;

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
        return nodata_layout;
    }

    @Override
    protected View refreshLayout() {
        return smartrefreshlayout;
    }

    @Override
    protected boolean closeDispatchTouchEvent() {
        return false;
    }

    @Override
    protected int initContentView() {
        return R.layout.module_activity_client_account_record;
    }

    @Override
    protected boolean refreshEnable() {
        return true;
    }

    @Override
    protected boolean loadmoreEnable() {
        return true;
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        super.onRefresh(refreshLayout);
        getP().getAccountRecord(true,status,date);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        super.onLoadMore(refreshLayout);
        getP().getAccountRecord(false,status,date);
    }

    @Override
    protected void init() {
        title_layout.setTitleText(gets(R.string.account_record));
        mAdapter = new ClientTransactionRecordAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(reference.get()));
        mAdapter.setOnItemClickListener(this);
        account_record_lv.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
            @Override
            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                switch (position){
                    case 0:
                        status = 0;
                        break;
                    case 1:
                        status = 1;
                        break;
                    case 2:
                        status = -1;
                        break;
                }
                getP().getAccountRecord(true,status,date);
            }
        });
    }

    @Override
    protected void initData() {
        labels = new ArrayList<>();
        labels.add("支出");
        labels.add("收入");
        labels.add("其他");
        account_record_lv.setLabels(labels);
        account_record_lv.setSelects(2);
        date_tv.setText(Calendar.getInstance().get(Calendar.YEAR)+gets(R.string.common_year)+(Calendar.getInstance().get(Calendar.MONTH)+1)+gets(R.string.common_month));
        date = Calendar.getInstance().get(Calendar.YEAR)+"-"+(Calendar.getInstance().get(Calendar.MONTH)+1);
        getP().getAccountRecord(true,status,date);
    }

    @OnClick(R.id.ll_selected_date)
    public void OnClickViews(View v){
        switch (v.getId()){
            case R.id.ll_selected_date:
                new XPopup.Builder(reference.get())
                        .isDarkTheme(false)
                        .asCustom(new SelectDateDialog(reference.get(), new ClientPickerDateListener() {
                            @Override
                            public void onSelected(BottomPopupView dialog, int year, int month) {
                                date_tv.setText(year+gets(R.string.common_year)+month+gets(R.string.common_month));
//                                Calendar calendar = Calendar.getInstance();
//                                calendar.set(Calendar.YEAR, year);
//                                // 月份从零开始，所以需要减 1
//                                calendar.set(Calendar.MONTH, month - 1);
////                                ToastUtils.showLong("时间戳：" + calendar.getTimeInMillis());
                                date = year+"-"+month;
                                getP().getAccountRecord(true,status,date);
                                dialog.dismiss();
                            }
                        }))
                        .show();
                break;
        }
    }

    @Override
    public void onTransactionResult(boolean refresh,List<TransactionBean> transactions) {
        if (refresh)
            mAdapter.setList(transactions);
        else
            mAdapter.addData(transactions);
    }

    @Override
    public void onCashInfoResult(ClientCashInfoBean cashInfoBean) {

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

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        boolean isUser = SPUtils.getInstance().getBoolean(LOGIN_WAY);
        if (!isUser){
            TransactionBean item = (TransactionBean) adapter.getItem(position);
            goToPagePutSerializable(reference.get(), StoreBillDetailActivity.class,getIntentEntityMap(new Object[]{item.getId()}));
        }
    }
}
