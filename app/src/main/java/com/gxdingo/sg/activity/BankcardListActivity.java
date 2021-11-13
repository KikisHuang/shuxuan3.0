package com.gxdingo.sg.activity;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.BankcardAdapter;
import com.gxdingo.sg.bean.BankcardBean;
import com.gxdingo.sg.biz.BankcardContract;
import com.gxdingo.sg.presenter.BankcardPresenter;
import com.gxdingo.sg.utils.LocalConstant;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * @author: Weaving
 * @date: 2021/10/25
 * @page:
 */
public class BankcardListActivity extends BaseMvpActivity<BankcardContract.BankcardPresenter> implements BankcardContract.BankcardListener, OnItemChildClickListener, OnItemClickListener {

    @BindView(R.id.title_layout)
    public TemplateTitle templateTitle;

    @BindView(R.id.smartrefreshlayout)
    public SmartRefreshLayout smartrefreshlayout;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.nodata_layout)
    public View nodata_layout;

    private BankcardAdapter mAdapter;

    //选择银行卡
    private boolean isSelect;

    //跳转商家提现页面
    private boolean isCash;

    @OnClick(R.id.txt_more)
    public void OnClickViews(View v){
        switch (v.getId()){
            case R.id.txt_more:
                goToPage(this,AddBankcardActivity.class,null);
                break;
        }
    }


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
        return R.layout.module_include_refresh;
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
    public void onLoadMore(RefreshLayout refreshLayout) {
        super.onLoadMore(refreshLayout);
        getP().getCardList(false);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        super.onRefresh(refreshLayout);
        getP().getCardList(true);
    }

    @Override
    protected void init() {
        templateTitle.setTitleText("我的银行卡");
        templateTitle.setMoreText("添加");
        templateTitle.setMoreTextColor(getc(R.color.blue_text));
        isSelect = getIntent().getBooleanExtra(Constant.SERIALIZABLE+0,false);
        isCash= getIntent().getBooleanExtra(Constant.SERIALIZABLE+1,false);
        mAdapter = new BankcardAdapter(1);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(reference.get()));
    }

    @Override
    protected void initData() {
        getP().getCardList(true);
    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type == LocalConstant.CLIENT_REFRESH_BANKCARD_LIST)
            getP().getCardList(true);
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
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        BankcardBean item = (BankcardBean) adapter.getItem(position);
        goToPagePutSerializable(this,UnbindBankcardActivity.class,getIntentEntityMap(new Object[]{item}));
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        if (!isSelect) return;
        BankcardBean item = (BankcardBean) adapter.getItem(position);
        sendEvent(item);
        if (!isCash)finish();
    }
}
