package com.gxdingo.sg.activity;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.donkingliang.labels.LabelsView;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.ClientTransactionRecordAdapter;
import com.gxdingo.sg.bean.ClientAccountTransactionBean;
import com.gxdingo.sg.bean.ClientCashInfoBean;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.kikis.commnlibrary.utils.CommonUtils.gets;

/**
 * @author: Weaving
 * @date: 2021/10/14
 * @page:
 */
public class ClientAccountRecordActivity extends BaseMvpActivity<ClientAccountSecurityContract.ClientAccountSecurityPresenter> implements ClientAccountSecurityContract.ClientAccountSecurityListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.date_tv)
    public TextView date_tv;

    @BindView(R.id.account_record_lv)
    public LabelsView account_record_lv;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

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
        return R.layout.module_activity_client_account_record;
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
        title_layout.setTitleText(gets(R.string.account_record));
        mAdapter = new ClientTransactionRecordAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(reference.get()));
    }

    @Override
    protected void initData() {
        labels = new ArrayList<>();
        labels.add("支出");
        labels.add("收入");
        labels.add("其他");
        account_record_lv.setLabels(labels);

        date_tv.setText(Calendar.getInstance().get(Calendar.YEAR)+gets(R.string.common_year)+Calendar.getInstance().get(Calendar.MONTH)+gets(R.string.common_month));
        date = Calendar.getInstance().get(Calendar.YEAR)+"-"+Calendar.getInstance().get(Calendar.MONTH);
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
    public void onTransactionResult(boolean refresh,List<ClientAccountTransactionBean.ListBean> transactions) {
        if (refresh)
            mAdapter.setList(transactions);
        else
            mAdapter.addData(transactions);
    }

    @Override
    public void onCashInfoResult(ClientCashInfoBean cashInfoBean) {

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
