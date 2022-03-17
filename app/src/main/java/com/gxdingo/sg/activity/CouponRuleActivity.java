package com.gxdingo.sg.activity;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.CouponRuleAdapter;
import com.gxdingo.sg.bean.ClientCouponBean;
import com.gxdingo.sg.biz.ClientCouponContract;
import com.gxdingo.sg.presenter.ClientCouponPresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * @author: Kikis
 * @date: 2022/3/3
 * @page:优惠卷规则页面
 */
public class CouponRuleActivity extends BaseMvpActivity<ClientCouponContract.ClientCouponPresenter> implements ClientCouponContract.ClientCouponListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.notice_for_use_ryc)
    public RecyclerView notice_for_use_ryc;

    @BindView(R.id.matters_need_attention_ryc)
    public RecyclerView matters_need_attention_ryc;

    private List<String> instructions;

    private List<String> precautions;

    @BindView(R.id.condition_tv)
    public TextView condition_tv;

    @BindView(R.id.coupon_name_tv)
    public TextView coupon_name_tv;

    @BindView(R.id.date_tv)
    public TextView date_tv;

    private ClientCouponBean mClientCouponBean;


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
        return R.layout.module_include_custom_title;
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
        return R.layout.module_activity_coupon_rule;
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
        getP().getCoupons(true, "");
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        super.onLoadMore(refreshLayout);
        getP().getCoupons(false, "");
    }

    @Override
    protected void init() {
        title_layout.setBackgroundColor(getc(R.color.divide_color));
        title_layout.setTitleText(gets(R.string.rules_of_use));


        mClientCouponBean = (ClientCouponBean) getIntent().getSerializableExtra(Constant.SERIALIZABLE + 0);

        if (mClientCouponBean == null) {
            onMessage("没有获取到优惠券数据");
            finish();
        }

        instructions = mClientCouponBean.getInstructions();
        precautions = mClientCouponBean.getPrecautions();


        CouponRuleAdapter mAdapter = new CouponRuleAdapter();

        CouponRuleAdapter nAdapter = new CouponRuleAdapter();

        nAdapter.setList(instructions);
        mAdapter.setList(precautions);

        matters_need_attention_ryc.setAdapter(mAdapter);
        matters_need_attention_ryc.setLayoutManager(new LinearLayoutManager(reference.get()));

        notice_for_use_ryc.setAdapter(nAdapter);
        notice_for_use_ryc.setLayoutManager(new LinearLayoutManager(reference.get()));

//        condition_tv.setText(mClientCouponBean.get);

        if (!isEmpty(mClientCouponBean.getExpireTime()))
            date_tv.setText(mClientCouponBean.getExpireTime());

        if (!isEmpty(mClientCouponBean.getCouponName()))
            coupon_name_tv.setText(mClientCouponBean.getCouponName());

            condition_tv.setText(Float.valueOf(mClientCouponBean.getUseAmount()) <= 0 ? "无门槛" : "满" + mClientCouponBean.getUseAmount() + "减" + mClientCouponBean.getCouponAmount());
    }

    @Override
    protected void initData() {
    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public void onCouponsResult(boolean refresh, List<ClientCouponBean> couponBeans) {

    }
}
