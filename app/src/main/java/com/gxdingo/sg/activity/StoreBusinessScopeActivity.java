package com.gxdingo.sg.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.StoreBusinessScopeAdapter;
import com.gxdingo.sg.bean.BusinessScopeEvent;
import com.gxdingo.sg.bean.StoreBusinessScopeBean;
import com.gxdingo.sg.biz.StoreCertificationContract;
import com.gxdingo.sg.presenter.StoreCertificationPresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.view.TemplateTitle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 商家经营范围
 *
 * @author: Weaving
 * @date: 2021/6/2
 * @page:
 */
public class StoreBusinessScopeActivity extends BaseMvpActivity<StoreCertificationContract.StoreCertificationPresenter>
        implements StoreCertificationContract.StoreCertificationListener, OnItemClickListener {


    @BindView(R.id.rv_business_scope)
    public RecyclerView rv_business_scope;
    @BindView(R.id.title_layout)
    TemplateTitle titleLayout;
    @BindView(R.id.tv_right_button)
    TextView tvRightButton;
    @BindView(R.id.tv_right_image_button)
    ImageView tvRightImageButton;

    private StoreBusinessScopeAdapter mAdapter;

    private List<StoreBusinessScopeBean.ListBean> selectedCategory = new ArrayList<>();

    @Override
    protected StoreCertificationContract.StoreCertificationPresenter createPresenter() {
        return new StoreCertificationPresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return true;
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
        return null;
    }

    @Override
    protected boolean closeDispatchTouchEvent() {
        return false;
    }

    @Override
    protected int initContentView() {
        return R.layout.module_activity_store_business_scope;
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
        titleLayout.setTitleTextSize(16);
        titleLayout.setTitleText("经营范围");

        tvRightButton.setVisibility(View.VISIBLE);
        tvRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getP().confirmBusinessScope(mAdapter.getData());
                List<StoreBusinessScopeBean.ListBean> data = mAdapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).isSelect())
                        selectedCategory.add(data.get(i));
                }

                if (selectedCategory.size() < 1) {
                    onMessage("请至少选择一个分类");
                    return;
                }
                sendEvent(selectedCategory);
                finish();
            }
        });

        mAdapter = new StoreBusinessScopeAdapter();
        rv_business_scope.setLayoutManager(new LinearLayoutManager(reference.get()));
        rv_business_scope.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        getP().getCategory();
    }

    @Override
    public void uploadImage(String url) {

    }

    @Override
    public void onBusinessScopeResult(List<StoreBusinessScopeBean.ListBean> businessScopes) {
        mAdapter.setList(businessScopes);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void closeBusinessScope(BusinessScopeEvent businessScopeEvent) {
        sendEvent(businessScopeEvent);
        finish();
    }

    @Override
    public void certificationPassed() {

    }

    @Override
    public void onReview() {

    }

    @Override
    public void rejected() {

    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        List<StoreBusinessScopeBean.ListBean> data = mAdapter.getData();
        List<StoreBusinessScopeBean.ListBean> tempData = new ArrayList<>();

        boolean isSelect = data.get(position).isSelect();
        if (!isSelect) {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).isSelect())
                    tempData.add(data.get(i));
            }
            if (tempData.size() >= 2) {
                onMessage("最多选2个品类");
                return;
            }
        }
        data.get(position).setSelect(!isSelect);

        mAdapter.notifyDataSetChanged();
    }

}
