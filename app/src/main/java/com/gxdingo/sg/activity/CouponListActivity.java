package com.gxdingo.sg.activity;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.CouponAdapter;
import com.gxdingo.sg.bean.ClientCouponBean;
import com.gxdingo.sg.biz.ClientCouponContract;
import com.gxdingo.sg.presenter.ClientCouponPresenter;
import com.gxdingo.sg.utils.LocalConstant;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.List;

import butterknife.BindView;

import static com.blankj.utilcode.util.TimeUtils.string2Millis;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * @author: Weaving
 * @date: 2021/10/31
 * @page:
 */
public class CouponListActivity extends BaseMvpActivity<ClientCouponContract.ClientCouponPresenter> implements ClientCouponContract.ClientCouponListener, OnItemChildClickListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.smartrefreshlayout)
    public SmartRefreshLayout smartRefreshLayout;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.nodata_layout)
    public View nodata_layout;

    private CouponAdapter mCouponAdapter;

    //0 我的页面进入 1 转账页面进入
    private int mType = 0;

    //商家 mIdentifie
    private String mIdentifie;

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
        getP().getCoupons(true, mIdentifie);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        super.onLoadMore(refreshLayout);
        getP().getCoupons(false, mIdentifie);
    }

    @Override
    protected void init() {
        title_layout.setBackgroundColor(getc(R.color.divide_color));
        title_layout.setTitleText("选择优惠券");
        mCouponAdapter = new CouponAdapter();
        recyclerView.setAdapter(mCouponAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(reference.get()));
        recyclerView.setPadding(20, 0, 20, 0);
        recyclerView.setBackgroundColor(getc(R.color.divide_color));
        mCouponAdapter.setOnItemChildClickListener(this);

        mType = getIntent().getIntExtra(Constant.SERIALIZABLE + 0, 0);

        mIdentifie = getIntent().getStringExtra(Constant.SERIALIZABLE + 1);
    }

    @Override
    protected void initData() {
        getP().getCoupons(true, mIdentifie);
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
        boolean isPastDue = item.getStatus() == 1 || item.getStatus() == 2;
        if (!isPastDue) {
            //规则
            if (view.getId() == R.id.rule_tv) {

                goToPagePutSerializable(reference.get(), CouponRuleActivity.class, getIntentEntityMap(new Object[]{item.getInstructions(), item.getPrecautions()}));

            } else {
                if (mType == 0) {

                    if (item.getIsNeedWriteOff() == 1 && item.getWriteOff() == 1) {
                        // 状态。0=待使用；1=已使用；2=已过期
                        if (item.getStatus() == 0){
                            goToPagePutSerializable(reference.get(), ClientStoreDetailsActivity.class, getIntentEntityMap(new Object[]{item.userIdentifier}));
                            return;
                        }
                    }
                    //我的页面进入使用优惠券跳转店铺列表
                    sendEvent(LocalConstant.GO_STORE_LIST_PAGE);
                    finish();
                    return;
                } else if (mType == 1) {
                    //转账页面进入

                    // 是否需要核销0 = 否 1 = 是
                    // 核销状态 0 = 未核销 1 = 已核销
                    if (item.getIsNeedWriteOff() == 1 && item.getWriteOff() == 0) {
                        // 状态。0=待使用；1=已使用；2=已过期
                        if (item.getStatus() == 0)
                            goToPagePutSerializable(reference.get(), CouponQrCodeScanActivity.class, getIntentEntityMap(new Object[]{item, mType}));
                    } else {
                        sendEvent(item);
                        finish();
                    }
                }
            }
        } else
            onMessage("优惠卷已过期");

    }
}
