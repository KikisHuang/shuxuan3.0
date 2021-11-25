package com.gxdingo.sg.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.StoreCertificationResultContract;
import com.gxdingo.sg.presenter.StoreCertificationResultPresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.view.TemplateTitle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商家认证结果
 *
 * @author JM
 */
public class StoreCertificationResultActivity extends BaseMvpActivity<StoreCertificationResultContract.StoreCertificationResultPresenter>
        implements StoreCertificationResultContract.StoreCertificationResultListener {

    @BindView(R.id.title_layout)
    TemplateTitle titleLayout;
    @BindView(R.id.iv_result_status)
    ImageView ivResultStatus;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    @Override
    protected StoreCertificationResultContract.StoreCertificationResultPresenter createPresenter() {
        return new StoreCertificationResultPresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return false;
    }

    @Override
    protected int activityTitleLayout() {
        return R.layout.module_include_new_custom_title;
    }

    @Override
    protected boolean ImmersionBar() {
        return false;
    }

    @Override
    protected int StatusBarColors() {
        return 0;
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
        return R.layout.module_activity_store_certification_result;
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
        titleLayout.setTitleText("");

        String status = getIntent().getStringExtra("static");
        //状态 0=待审核 10=已认证 20=已驳回 40=已禁用 90=永久关闭
        if (status.equals("0")) {

        } else if (status.equals("10")) {

        }
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
    }
}
