package com.gxdingo.sg.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.BusinessDistrictMessageAdapter;
import com.gxdingo.sg.biz.BusinessDistrictMessageContract;
import com.gxdingo.sg.presenter.BusinessDistrictMessagePresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商圈消息
 *
 * @author JM
 */
public class BusinessDistrictMessageActivity extends BaseMvpActivity<BusinessDistrictMessageContract.BusinessDistrictMessagePresenter> implements BusinessDistrictMessageContract.BusinessDistrictMessageListener {


    BusinessDistrictMessageAdapter mMessageAdapter;
    @BindView(R.id.title_layout)
    TemplateTitle titleLayout;
    @BindView(R.id.tv_right_button)
    TextView tvRightButton;
    @BindView(R.id.tv_right_image_button)
    ImageView tvRightImageButton;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.hint_img)
    ImageView hintImg;
    @BindView(R.id.hint_tv)
    TextView hintTv;
    @BindView(R.id.function_bt)
    TextView functionBt;
    //    @BindView(R.id.classics_footer)
//    ClassicsFooter classicsFooter;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;

    @Override
    protected BusinessDistrictMessageContract.BusinessDistrictMessagePresenter createPresenter() {
        return new BusinessDistrictMessagePresenter();
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
        return smartrefreshlayout;
    }

    @Override
    protected boolean closeDispatchTouchEvent() {
        return false;
    }

    @Override
    protected int initContentView() {
        return R.layout.module_activity_business_district_message;
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
    protected void init() {
        titleLayout.setTitleTextSize(16);
        titleLayout.setTitleText("我的消息");
        tvRightButton.setText("清空");
        tvRightButton.setVisibility(View.VISIBLE);
        tvRightImageButton.setVisibility(View.GONE);

        mMessageAdapter = new BusinessDistrictMessageAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(reference.get()));
        recyclerView.setAdapter(mMessageAdapter);

        ArrayList<Object> datas = new ArrayList<>();
        datas.add(new Object());
        datas.add(new Object());
        datas.add(new Object());
        datas.add(new Object());
        datas.add(new Object());
        datas.add(new Object());
        datas.add(new Object());
        datas.add(new Object());
        datas.add(new Object());
        datas.add(new Object());
        datas.add(new Object());
        datas.add(new Object());
        datas.add(new Object());
        mMessageAdapter.setList(datas);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_right_button)
    public void onViewClicked() {
    }
}
