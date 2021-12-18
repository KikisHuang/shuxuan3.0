package com.gxdingo.sg.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.BusinessDistrictListAdapter;
import com.gxdingo.sg.adapter.StoreBusinessScopeAdapter;
import com.gxdingo.sg.bean.BusinessDistrictListBean;
import com.gxdingo.sg.bean.BusinessDistrictUnfoldCommentListBean;
import com.gxdingo.sg.bean.BusinessScopeEvent;
import com.gxdingo.sg.bean.NumberUnreadCommentsBean;
import com.gxdingo.sg.bean.StoreBusinessScopeBean;
import com.gxdingo.sg.biz.StoreBusinessDistrictContract;
import com.gxdingo.sg.biz.StoreCertificationContract;
import com.gxdingo.sg.fragment.store.StoreBusinessDistrictFragment;
import com.gxdingo.sg.presenter.StoreBusinessDistrictPresenter;
import com.gxdingo.sg.presenter.StoreCertificationPresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.gxdingo.sg.utils.emotion.EmotionMainFragment.CHAT_ID;
import static com.gxdingo.sg.utils.emotion.EmotionMainFragment.ROLE;
import static com.kikis.commnlibrary.fragment.BaseFragment.newInstance;
import static com.kikis.commnlibrary.utils.Constant.KEY;

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
        return new StoreBusinessDistrictPresenter();
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
        int storeId = getIntent().getIntExtra(Constant.SERIALIZABLE + 0, 0);

        Bundle bundle = new Bundle();
        bundle.putInt(Constant.SERIALIZABLE + 0, storeId);
        bundle.putInt(Constant.PARAMAS + 0, 3);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_ll, StoreBusinessDistrictFragment.newInstance(StoreBusinessDistrictFragment.class, bundle))   // 此处的R.id.fragment_container是要盛放fragment的父容器
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
}
