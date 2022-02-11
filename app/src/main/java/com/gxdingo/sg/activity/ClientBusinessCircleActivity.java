package com.gxdingo.sg.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.BannerBean;
import com.gxdingo.sg.bean.BusinessDistrictListBean;
import com.gxdingo.sg.bean.BusinessDistrictUnfoldCommentListBean;
import com.gxdingo.sg.bean.NumberUnreadCommentsBean;
import com.gxdingo.sg.biz.StoreBusinessDistrictContract;
import com.gxdingo.sg.fragment.child.BusinessDistrictFragment;
import com.gxdingo.sg.presenter.BusinessDistrictPresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.Constant;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 商圈
 *
 * @author: Kikis
 * @date: 2021/11/15
 * @page:
 */
public class ClientBusinessCircleActivity extends BaseMvpActivity<StoreBusinessDistrictContract.StoreBusinessDistrictPresenter>
        implements StoreBusinessDistrictContract.StoreBusinessDistrictListener {

    @BindView(R.id.main_ll)
    public FrameLayout main_ll;


    @Override
    protected StoreBusinessDistrictContract.StoreBusinessDistrictPresenter createPresenter() {
        return new BusinessDistrictPresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return true;
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
        return R.layout.module_activity_client_business_circle;
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
    protected void onBaseCreate() {
        super.onBaseCreate();
    }

    @Override
    protected void init() {
        String storeId = getIntent().getStringExtra(Constant.SERIALIZABLE + 0);
        String title_name = getIntent().getStringExtra(Constant.SERIALIZABLE + 1);

        Bundle bundle = new Bundle();
        bundle.putString(Constant.SERIALIZABLE + 0, storeId);
        bundle.putString(Constant.SERIALIZABLE + 1, title_name);

        bundle.putInt(Constant.PARAMAS + 0, 3);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_ll, BusinessDistrictFragment.newInstance(BusinessDistrictFragment.class, bundle))   // 此处的R.id.fragment_container是要盛放fragment的父容器
                .commit();
    }

    @Override
    protected void initData() {

    }



    @Override
    public void onBusinessDistrictData(boolean refresh, BusinessDistrictListBean bean) {

    }

    @Override
    public void onSubmitCommentOrReplyResult() {

    }

    @Override
    public void onReturnCommentListResult(BusinessDistrictListBean.BusinessDistrict businessDistrict, BusinessDistrictUnfoldCommentListBean commentListBean) {

    }

    @Override
    public void onNumberUnreadComments(NumberUnreadCommentsBean unreadCommentsBean) {

    }

    @Override
    public void onCommentListRefresh(ArrayList<BusinessDistrictListBean.Comment> commentList, ArrayList<BusinessDistrictUnfoldCommentListBean.UnfoldComment> unfoldCommentList, BusinessDistrictListBean.BusinessDistrict businessDistrict, int total) {

    }

    @Override
    public void refreshLikeNum(String o, int position) {

    }

    @Override
    public void onBannerResult(BannerBean data) {

    }
}
