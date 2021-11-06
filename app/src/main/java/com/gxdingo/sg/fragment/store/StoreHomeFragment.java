package com.gxdingo.sg.fragment.store;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.ClientActivity;
import com.gxdingo.sg.activity.IMChatActivity;
import com.gxdingo.sg.activity.StoreHomeSearchActivity;
import com.gxdingo.sg.adapter.StoreHomeIMMessageAdapter;
import com.gxdingo.sg.bean.CategoriesBean;
import com.gxdingo.sg.bean.StoreListBean;
import com.gxdingo.sg.bean.SubscribesBean;
import com.gxdingo.sg.bean.UserBean;
import com.gxdingo.sg.biz.ClientHomeContract;
import com.gxdingo.sg.biz.StoreHomeContract;
import com.gxdingo.sg.dialog.IMSelectSendAddressPopupView;
import com.gxdingo.sg.dialog.StoreSelectBusinessStatusPopupView;
import com.gxdingo.sg.presenter.StoreHomePresenter;
import com.gxdingo.sg.service.IMMessageReceivingService;
import com.gxdingo.sg.utils.StoreLocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.fragment.BaseMvpFragment;
import com.kikis.commnlibrary.utils.ScreenUtils;
import com.kikis.commnlibrary.view.RoundAngleImageView;
import com.kikis.commnlibrary.view.RoundImageView;
import com.lxj.xpopup.XPopup;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.umeng.commonsdk.debug.I;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.gxdingo.sg.utils.LocalConstant.STORE_LOGIN_SUCCEED;
import static com.kikis.commnlibrary.utils.Constant.LOGOUT;
import static com.kikis.commnlibrary.utils.Constant.WEB_SOCKET_URL;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;

/**
 * 商家端主页
 *
 * @author: JM
 */
public class StoreHomeFragment extends BaseMvpFragment<StoreHomeContract.StoreHomePresenter> implements StoreHomeContract.StoreHomeListener {
    Context mContext;
    StoreHomeIMMessageAdapter mStoreHomeIMMessageAdapter;
    @BindView(R.id.tv_status_bar)
    LinearLayout tvStatusBar;
    @BindView(R.id.ll_search_layout)
    LinearLayout llSearchLayout;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.iv_more2)
    ImageView ivMore2;
    @BindView(R.id.top_layout)
    ConstraintLayout topLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.niv_store_avatar)
    RoundImageView nivStoreAvatar;
    @BindView(R.id.tv_store_name)
    TextView tvStoreName;
    @BindView(R.id.tv_business_status)
    TextView tvBusinessStatus;
    @BindView(R.id.tv_business_time)
    TextView tvBusinessTime;
    @BindView(R.id.ll_business_info_layout)
    LinearLayout llBusinessInfoLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.hint_img)
    ImageView hintImg;
    @BindView(R.id.hint_tv)
    TextView hintTv;
    @BindView(R.id.function_bt)
    TextView functionBt;
    @BindView(R.id.classics_footer)
    ClassicsFooter classicsFooter;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.cl_store_name_layout)
    ConstraintLayout clStoreNameLayout;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.iv_search2)
    ImageView ivSearch2;
    @BindView(R.id.ll_search_layout2)
    LinearLayout llSearchLayout2;
    @BindView(R.id.nodata_layout)
    View nodataLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Override
    protected StoreHomeContract.StoreHomePresenter createPresenter() {
        return new StoreHomePresenter();
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
    protected int initContentView() {
        return R.layout.module_fragment_store_home;
    }

    @Override
    protected View noDataLayout() {
        return nodataLayout;
    }

    @Override
    protected View refreshLayout() {
        return smartrefreshlayout;
    }

    @Override
    protected boolean refreshEnable() {
        return true;
    }

    @Override
    protected boolean loadmoreEnable() {
        return true;
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        //获取IM订阅信息
        getP().getIMSubscribesList(true);
    }

    /**
     * 上拉加载更多数据
     */
    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        //获取IM订阅信息
        getP().getIMSubscribesList(false);
    }

    @Override
    protected void init() {
        int statusBarHeight = ScreenUtils.getStatusHeight(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
        tvStatusBar.setLayoutParams(params);
        setAppBarLayoutListener();

        mStoreHomeIMMessageAdapter = new StoreHomeIMMessageAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(reference.get()));
        mStoreHomeIMMessageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                SubscribesBean.MessageBean messageBean = mStoreHomeIMMessageAdapter.getItem(position);
                //跳转到IM聊天界面
                Map<String, String> intentMap = new HashMap<>();
                intentMap.put(IMChatActivity.EXTRA_SHARE_UUID, messageBean.getShareUuid());
                intentMap.put(IMChatActivity.EXTRA_OTHER_NAME, messageBean.getSendNickname());
                goToPage(reference.get(), IMChatActivity.class, intentMap);
            }
        });
        recyclerView.setAdapter(mStoreHomeIMMessageAdapter);
    }

    private void setAppBarLayoutListener() {
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @SuppressLint("ResourceType")
            @Override
            public void onOffsetChanged(AppBarLayout arg0, int arg1) {
                if (Math.abs(arg1) >= arg0.getTotalScrollRange()) {
                    llBusinessInfoLayout.setVisibility(View.INVISIBLE);
                    llSearchLayout2.setVisibility(View.VISIBLE);
                } else {
                    llBusinessInfoLayout.setVisibility(View.VISIBLE);
                    llSearchLayout2.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onBaseEvent(Object object) {
        super.onBaseEvent(object);

    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type == StoreLocalConstant.SOTRE_REVIEW_SUCCEED) {//店铺审核成功重新初始化数据
            initData();
        }
    }

    @Override
    protected void initData() {
        UserBean userBean = UserInfoUtils.getInstance().getUserInfo();
        if (userBean != null) {
            UserBean.StoreBean storeBean = userBean.getStore();
            Glide.with(mContext).load(storeBean.getAvatar()).apply(getRequestOptions()).into(nivStoreAvatar);
            tvStoreName.setText(storeBean.getName());
            setBusinessStatus(storeBean);

            getP().getIMSubscribesList(true);//获取IM订阅信息
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private RequestOptions getRequestOptions() {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.ic_user_default_avatar);    //加载成功之前占位图
        options.error(R.mipmap.ic_user_default_avatar);    //加载错误之后的错误图
        return options;
    }

    /**
     * 设置营业状态
     */
    private void setBusinessStatus(UserBean.StoreBean storeBean) {
        tvBusinessStatus.setText(storeBean.getBusinessStatus() == 0 ? "暂停营业" : "正常营业");
        if (!TextUtils.isEmpty(storeBean.getOpenTime()) && !TextUtils.isEmpty(storeBean.getCloseTime())) {
            String openTime = getP().onInterceptionBusinessHours(storeBean.getOpenTime());
            String closeTime = getP().onInterceptionBusinessHours(storeBean.getCloseTime());
            tvBusinessTime.setText(openTime + "-" + closeTime);
        }
    }

    @OnClick({R.id.tv_search, R.id.iv_search2, R.id.iv_more, R.id.iv_more2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                startActivity(new Intent(mContext, StoreHomeSearchActivity.class));
                break;
            case R.id.iv_search2:
                startActivity(new Intent(mContext, StoreHomeSearchActivity.class));
                break;
            case R.id.iv_more:
                showSelectBusinessStatusDialog();
                break;
            case R.id.iv_more2:
                showSelectBusinessStatusDialog();
                break;
        }
    }

    /**
     * 显示选择营业状态弹窗
     */
    private void showSelectBusinessStatusDialog() {
        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .isDarkTheme(false)
                .asCustom(new StoreSelectBusinessStatusPopupView(mContext, new StoreSelectBusinessStatusPopupView.OnBusinessStatusListener() {

                    @Override
                    public void onStatus(int code, String name) {
                        tvBusinessStatus.setText(name);
                    }
                }).show());
    }


    /**
     * 返回IM订阅信息列表(包含有请求web socket接入url)
     */
    @Override
    public void onIMSubscribesInfo(boolean refresh, SubscribesBean subscribesBean) {
        if (subscribesBean != null && subscribesBean.getList() != null) {
            //保存web socket接入url
            SPUtils.getInstance().put(WEB_SOCKET_URL, subscribesBean.getWebsocketUrl());
            //启动IM消息接收服务
            mContext.startService(new Intent(mContext, IMMessageReceivingService.class));

            if (refresh) {
                mStoreHomeIMMessageAdapter.setList(subscribesBean.getList());
            } else {
                mStoreHomeIMMessageAdapter.addData(subscribesBean.getList());
            }
        }
    }
}
