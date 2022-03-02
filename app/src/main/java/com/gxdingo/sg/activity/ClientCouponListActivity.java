package com.gxdingo.sg.activity;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allen.library.SuperTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.ClientCouponAdapter;
import com.gxdingo.sg.bean.ClientCouponBean;
import com.gxdingo.sg.biz.ClientCouponContract;
import com.gxdingo.sg.presenter.ClientCouponPresenter;
import com.gxdingo.sg.utils.DateUtils;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.List;

import butterknife.BindView;

import static com.blankj.utilcode.util.TimeUtils.getNowMills;
import static com.blankj.utilcode.util.TimeUtils.string2Millis;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * @author: Weaving
 * @date: 2021/10/31
 * @page:
 */
public class ClientCouponListActivity extends BaseMvpActivity<ClientCouponContract.ClientCouponPresenter> implements ClientCouponContract.ClientCouponListener, OnItemChildClickListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.smartrefreshlayout)
    public SmartRefreshLayout smartRefreshLayout;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.nodata_layout)
    public View nodata_layout;


    private ClientCouponAdapter mCouponAdapter;

    @Override
    protected ClientCouponContract.ClientCouponPresenter createPresenter() {
        return new ClientCouponPresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return false;
    }

    @Override
    protected int activityTitleLayout() {
        return 0;
    }

    @Override
    protected boolean ImmersionBar() {
        return true;
    }

    @Override
    protected int StatusBarColors() {
        return R.color.divide_color;
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
        return smartRefreshLayout;
    }

    @Override
    protected boolean closeDispatchTouchEvent() {
        return false;
    }

    @Override
    protected int initContentView() {
        return R.layout.module_activity_coupon_list;
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
        getP().getCoupons(true);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        super.onLoadMore(refreshLayout);
        getP().getCoupons(false);
    }

    @Override
    protected void init() {
        title_layout.setBackgroundColor(getc(R.color.divide_color));
        title_layout.setTitleText("选择优惠券");
        mCouponAdapter = new ClientCouponAdapter();
        recyclerView.setAdapter(mCouponAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(reference.get()));
        recyclerView.setPadding(20, 0, 20, 0);
        recyclerView.setBackgroundColor(getc(R.color.divide_color));
        mCouponAdapter.setOnItemChildClickListener(this);
    }

    @Override
    protected void initData() {
        getP().getCoupons(true);
    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public void onCouponsResult(boolean refresh, List<ClientCouponBean> couponBeans) {
        if (refresh)
            mCouponAdapter.setList(couponBeans);
        else
            mCouponAdapter.addData(couponBeans);

    }

    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {

        ClientCouponBean item = (ClientCouponBean) adapter.getItem(position);

        // 状态。0=待使用；1=已使用；2=已过期
        boolean isPastDue = item.status == 1 || item.status == 2;
        if (!isPastDue) {

            if (view.getId() == R.id.rule_tv) {
                //todo 使用规则页面

            } else {
                sendEvent(item);
                finish();
                //todo 后期增加是否核销的参数
//                goToPagePutSerializable(reference.get(), ClientCouponDetailsActivity.class, getIntentEntityMap(new Object[]{item}));

            }
        } else
            onMessage("优惠卷已过期");

    }
}
