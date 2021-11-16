package com.gxdingo.sg.fragment.store;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.ChatActivity;
import com.gxdingo.sg.activity.IMChatActivity;
import com.gxdingo.sg.activity.StoreHomeSearchActivity;
import com.gxdingo.sg.adapter.StoreHomeIMMessageAdapter;
import com.gxdingo.sg.bean.ExitChatEvent;
import com.kikis.commnlibrary.activitiy.BaseActivity;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.kikis.commnlibrary.bean.SubscribesListBean;
import com.gxdingo.sg.bean.UserBean;
import com.gxdingo.sg.biz.StoreHomeContract;
import com.gxdingo.sg.dialog.StoreSelectBusinessStatusPopupView;
import com.gxdingo.sg.presenter.StoreHomePresenter;
import com.gxdingo.sg.service.IMMessageReceivingService;
import com.gxdingo.sg.utils.StoreLocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.fragment.BaseMvpFragment;
import com.kikis.commnlibrary.utils.RxUtil;
import com.kikis.commnlibrary.utils.ScreenUtils;
import com.kikis.commnlibrary.view.RoundImageView;
import com.luck.picture.lib.entity.LocalMedia;
import com.lxj.xpopup.XPopup;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.trello.rxlifecycle3.LifecycleProvider;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static com.gxdingo.sg.utils.LocalConstant.ADD;
import static com.kikis.commnlibrary.utils.Constant.WEB_SOCKET_URL;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

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
        if (UserInfoUtils.getInstance().isLogin())
        //获取IM订阅信息
        getP().getIMSubscribesList(true);
    }


    /**
     * 上拉加载更多数据
     */
    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        if (UserInfoUtils.getInstance().isLogin())
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
                SubscribesListBean.SubscribesMessage subscribesMessage = mStoreHomeIMMessageAdapter.getItem(position);
                //跳转到IM聊天界面
//                Map<String, String> intentMap = new HashMap<>();
//                intentMap.put(IMChatActivity.EXTRA_SHARE_UUID, subscribesMessage.getShareUuid());
//                goToPage(reference.get(), IMChatActivity.class, intentMap);

                goToPagePutSerializable(reference.get(), ChatActivity.class, getIntentEntityMap(new Object[]{subscribesMessage.getShareUuid(), subscribesMessage.getSendUserRole()}));
                getP().clearUnreadMsg(subscribesMessage.getShareUuid());
            }
        });
        recyclerView.setAdapter(mStoreHomeIMMessageAdapter);
    }

    @Override
    protected void initData() {
        UserInfoUtils userInfo = UserInfoUtils.getInstance();
        if (userInfo != null && userInfo.isLogin()) {
            UserBean userBean = userInfo.getUserInfo();
            if (userBean != null) {
                //显示头像、名称和营业状态信息
                UserBean.StoreBean storeBean = userBean.getStore();
                Glide.with(mContext).load(storeBean.getAvatar()).apply(getRequestOptions()).into(nivStoreAvatar);
                tvStoreName.setText(storeBean.getName());
                setBusinessStatus(storeBean);

                //获取IM订阅信息
                getP().getIMSubscribesList(true);
            }
        }
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
        //从IMMessageReceivingService接收到的IM消息
        if (object instanceof ReceiveIMMessageBean) {
            ReceiveIMMessageBean receiveIMMessageBean = (ReceiveIMMessageBean) object;
            if (receiveIMMessageBean != null && !TextUtils.isEmpty(receiveIMMessageBean.getSendIdentifier())) {
                //不用做处理，直接刷新IM订阅列表即可（消息发送者的那条订阅消息会显示在第一条）
                getP().refreshList();

            }
        }
        //聊天页面退出事件，因为如果在当前聊天页面，未读消息数需要客户端手动清除？
        if (object instanceof ExitChatEvent) {
            ExitChatEvent exitChatEvent = (ExitChatEvent) object;
            getP().clearUnreadMsg(exitChatEvent.id);

        }
    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        //店铺审核成功重新初始化数据
        if (type == StoreLocalConstant.SOTRE_REVIEW_SUCCEED) {
            initData();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getP().refreshList();
    }

    @Override
    public void onResume() {
        super.onResume();
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

                        getP().updateBusinessStatus(code);
                    }
                }).show());
    }

    /**
     * 返回IM订阅信息列表(包含有请求web socket接入url)
     */
    @Override
    public void onIMSubscribesInfo(boolean refresh, SubscribesListBean subscribesListBean) {
        if (subscribesListBean != null && subscribesListBean.getList() != null) {
            //取出本地存储的websocket url
            String webSocketUrl = SPUtils.getInstance().getString(WEB_SOCKET_URL);
            /*
             * 本地保存的webSocketUrl是否服务器的一样，不一样可能url发生改变，
             * 需要将BaseWebSocket设置null,重新连接（前提是IMMessageReceivingService在运行）
             */
            if (!webSocketUrl.equals(subscribesListBean.getWebsocketUrl())) {
                EventBus.getDefault().post(100999);//发送重置码
            }

            //保存web socket接入url
            SPUtils.getInstance().put(WEB_SOCKET_URL, subscribesListBean.getWebsocketUrl());

            //启动IM消息接收服务
            mContext.startService(new Intent(mContext, IMMessageReceivingService.class));

            if (refresh) {
                mStoreHomeIMMessageAdapter.setList(subscribesListBean.getList());
            } else {
                mStoreHomeIMMessageAdapter.addData(subscribesListBean.getList());
            }
        }
    }

    @Override
    public void changeBusinessStatus(int status) {
        tvBusinessStatus.setText(status == 1 ? "营业中" : "未营业");
    }

    @Override
    public void clearMessageUnreadItem(String id) {

        //清除item未读消息
        RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {
            for (int i = 0; i < mStoreHomeIMMessageAdapter.getData().size(); i++) {
                SubscribesListBean.SubscribesMessage data = mStoreHomeIMMessageAdapter.getData().get(i);

                if (data.getShareUuid().equals(id)) {
                    data.setUnreadNum(0);
                    e.onNext(i);
                }
            }
            e.onComplete();
        }), (BaseActivity) reference.get()).subscribe(o -> {
            int pos = (int) o;
            mStoreHomeIMMessageAdapter.notifyItemChanged(pos);
        });

    }


}
