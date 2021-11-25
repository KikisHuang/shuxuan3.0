package com.gxdingo.sg.activity;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.BankcardAdapter;
import com.gxdingo.sg.bean.BankcardBean;
import com.gxdingo.sg.biz.BankcardContract;
import com.gxdingo.sg.presenter.BankcardPresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.view.TemplateTitle;

import java.util.List;

import butterknife.BindView;

/**
 * @author: Weaving
 * @date: 2021/10/22
 * @page:
 */
public class SupportBankActivity extends BaseMvpActivity<BankcardContract.BankcardPresenter> implements BankcardContract.BankcardListener, OnItemClickListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    private BankcardAdapter mAdapter;

    @Override
    protected BankcardContract.BankcardPresenter createPresenter() {
        return new BankcardPresenter();
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
        return R.layout.module_include_refresh;
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
        title_layout.setTitleText("当前支持以下银行");
        mAdapter = new BankcardAdapter(0);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(reference.get()));
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        getP().getSupportCardList();
    }
    @Override
    public void onDataResult(List<BankcardBean> bankcardBeans) {
        mAdapter.setList(bankcardBeans);
    }

    @Override
    public String getBankType() {
        return null;
    }

    @Override
    public String getPersonOfCard() {
        return null;
    }

    @Override
    public String getIdCard() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getNumber() {
        return null;
    }

    @Override
    public String getMobile() {
        return null;
    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        BankcardBean item = (BankcardBean) adapter.getItem(position);
        sendEvent(item);
        finish();
    }
}
