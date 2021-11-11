package com.gxdingo.sg.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientAccountTransactionBean;
import com.gxdingo.sg.bean.TransactionBean;
import com.gxdingo.sg.biz.StoreHomeContract;
import com.gxdingo.sg.presenter.StoreHomePresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;

import butterknife.BindView;

/**
 * @author: Weaving
 * @date: 2021/11/10
 * @page:
 */
public class StoreBillDetailActivity extends BaseMvpActivity<StoreHomeContract.StoreHomePresenter> {


    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.avatar_img)
    public ImageView avatar_img;

    @BindView(R.id.transfer_object_tv)
    public TextView transfer_object_tv;

    @BindView(R.id.amount_tv)
    public TextView amount_tv;

    @BindView(R.id.pay_method_stv)
    public SuperTextView pay_method_stv;

    @BindView(R.id.current_status_stv)
    public SuperTextView current_status_stv;

    @BindView(R.id.create_time_stv)
    public SuperTextView create_time_stv;

    @BindView(R.id.receive_time_stv)
    public SuperTextView receive_time_stv;

    private TransactionBean billBean;

    @Override
    protected StoreHomeContract.StoreHomePresenter createPresenter() {
        return new StoreHomePresenter();
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
        return R.layout.module_activity_store_bill_details;
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
        title_layout.setTitleText("账单详情");
        billBean= (TransactionBean) getIntent().getSerializableExtra(Constant.SERIALIZABLE+0);
    }

    @Override
    protected void initData() {

    }
}
