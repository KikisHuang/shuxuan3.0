package com.gxdingo.sg.fragment.store;

import android.annotation.SuppressLint;
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
import com.gxdingo.sg.activity.StoreActivity;
import com.gxdingo.sg.activity.StoreHomeSearchActivity;
import com.gxdingo.sg.adapter.IMMessageAdapter;
import com.gxdingo.sg.bean.ExitChatEvent;
import com.gxdingo.sg.bean.UserBean;
import com.gxdingo.sg.biz.StoreHomeContract;
import com.gxdingo.sg.dialog.StoreSelectBusinessStatusPopupView;
import com.gxdingo.sg.presenter.StoreHomePresenter;
import com.kikis.commnlibrary.utils.MessageCountManager;
import com.gxdingo.sg.utils.StoreLocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.activitiy.BaseActivity;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.kikis.commnlibrary.bean.SubscribesListBean;
import com.kikis.commnlibrary.fragment.BaseMvpFragment;
import com.kikis.commnlibrary.utils.RxUtil;
import com.kikis.commnlibrary.view.RoundImageView;
import com.lxj.xpopup.XPopup;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static com.gxdingo.sg.utils.ImServiceUtils.resetImService;
import static com.gxdingo.sg.utils.ImServiceUtils.startImService;
import static com.gxdingo.sg.utils.LocalConstant.NOTIFY_MSG_LIST_ADAPTER;
import static com.kikis.commnlibrary.utils.Constant.WEB_SOCKET_URL;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * ???????????????
 *
 * @author: Kikis
 */
public class StoreMessageFragment extends BaseMvpFragment<StoreHomeContract.StoreMessagePresenter> implements StoreHomeContract.StoreMessageListener {

    public IMMessageAdapter mStoreHomeIMMessageAdapter;

    @BindView(R.id.ll_search_layout)
    public LinearLayout llSearchLayout;

    @BindView(R.id.iv_more)
    public ImageView ivMore;

    @BindView(R.id.iv_more2)
    public ImageView ivMore2;

    @BindView(R.id.top_layout)
    public ConstraintLayout topLayout;

    @BindView(R.id.collapsing_toolbar)
    public CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.app_bar)
    public AppBarLayout appBar;

    @BindView(R.id.niv_store_avatar)
    public RoundImageView nivStoreAvatar;

    @BindView(R.id.tv_store_name)
    public TextView tvStoreName;

    @BindView(R.id.tv_business_status)
    public TextView tvBusinessStatus;

    @BindView(R.id.tv_business_time)
    public TextView tvBusinessTime;

    @BindView(R.id.ll_business_info_layout)
    public LinearLayout llBusinessInfoLayout;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.hint_img)
    public ImageView hintImg;

    @BindView(R.id.hint_tv)
    public TextView hintTv;

    @BindView(R.id.function_bt)
    public TextView functionBt;

    @BindView(R.id.classics_footer)
    public ClassicsFooter classicsFooter;

    @BindView(R.id.smartrefreshlayout)
    public SmartRefreshLayout smartrefreshlayout;

    @BindView(R.id.coordinator_layout)
    public CoordinatorLayout coordinatorLayout;

    @BindView(R.id.cl_store_name_layout)
    public ConstraintLayout clStoreNameLayout;

    @BindView(R.id.tv_search)
    public TextView tvSearch;

    @BindView(R.id.iv_search2)
    public ImageView ivSearch2;

    @BindView(R.id.ll_search_layout2)
    public LinearLayout llSearchLayout2;

    @BindView(R.id.nodata_layout)
    public View nodataLayout;


    @Override
    protected StoreHomeContract.StoreMessagePresenter createPresenter() {
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
        return R.layout.module_fragment_store_message;
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
     * ????????????
     */
    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        if (UserInfoUtils.getInstance().isLogin())
            //??????IM????????????
            getP().getIMSubscribesList(true);
    }


    /**
     * ????????????????????????
     */
    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        if (UserInfoUtils.getInstance().isLogin())
            //??????IM????????????
            getP().getIMSubscribesList(false);
    }

    @Override
    protected void init() {
        setAppBarLayoutListener();

        mStoreHomeIMMessageAdapter = new IMMessageAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(reference.get()));
        mStoreHomeIMMessageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                SubscribesListBean.SubscribesMessage subscribesMessage = mStoreHomeIMMessageAdapter.getItem(position);
                //?????????IM????????????
//                Map<String, String> intentMap = new HashMap<>();
//                intentMap.put(IMChatActivity.EXTRA_SHARE_UUID, subscribesMessage.getShareUuid());
//                goToPage(reference.get(), IMChatActivity.class, intentMap);

                goToPagePutSerializable(reference.get(), ChatActivity.class, getIntentEntityMap(new Object[]{subscribesMessage.getShareUuid(), subscribesMessage.getSendUserRole()}));
                getP().clearUnreadMsg(subscribesMessage.getShareUuid());

                if (StoreActivity.getInstance() != null)
                    StoreActivity.getInstance().setUnreadMsgNum(MessageCountManager.getInstance().reduceUnreadMessageNum(subscribesMessage.getUnreadNum()));

            }
        });
        recyclerView.setAdapter(mStoreHomeIMMessageAdapter);
    }

    @Override
    protected void initData() {

    }


    @Override
    protected void lazyInit() {
        super.lazyInit();
        UserInfoUtils userInfo = UserInfoUtils.getInstance();
        if (userInfo != null && userInfo.isLogin()) {
            if (isFirstLoad) {
                isFirstLoad = !isFirstLoad;
                setStoreInfo(userInfo);
                //??????IM????????????
                getP().getIMSubscribesList(true);
            } else {
                getP().refreshList();
                setStoreInfo(userInfo);
            }
        }
    }

    private void setStoreInfo(UserInfoUtils userInfo) {
        UserBean userBean = userInfo.getUserInfo();
        if (userBean != null) {
            //??????????????????????????????????????????
            UserBean.StoreBean storeBean = userBean.getStore();
            Glide.with(reference.get()).load(storeBean.getAvatar()).apply(getRequestOptions()).into(nivStoreAvatar);
            tvStoreName.setText(storeBean.getName());
            setBusinessStatus(storeBean);
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
        //???IMMessageReceivingService????????????IM??????
        if (object instanceof ReceiveIMMessageBean) {
            ReceiveIMMessageBean receiveIMMessageBean = (ReceiveIMMessageBean) object;
            if (receiveIMMessageBean != null && !TextUtils.isEmpty(receiveIMMessageBean.getSendIdentifier())) {
                //??????????????????????????????IM?????????????????????????????????????????????????????????????????????????????????
                getP().refreshList();

            }
        }
        //????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (object instanceof ExitChatEvent) {
            ExitChatEvent exitChatEvent = (ExitChatEvent) object;
            getP().clearUnreadMsg(exitChatEvent.id);

        }
    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        //???????????????????????????????????????
        if (type == StoreLocalConstant.SOTRE_REVIEW_SUCCEED) {

            UserInfoUtils userInfo = UserInfoUtils.getInstance();
            if (userInfo != null && userInfo.isLogin())
                setStoreInfo(userInfo);
            //??????IM????????????
            getP().getIMSubscribesList(true);
        } else if (type == NOTIFY_MSG_LIST_ADAPTER)
            mStoreHomeIMMessageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick({R.id.tv_search, R.id.iv_search2, R.id.iv_more, R.id.ll_search_layout2, R.id.iv_more2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                startActivity(new Intent(reference.get(), StoreHomeSearchActivity.class));
                break;
            case R.id.iv_search2:
                startActivity(new Intent(reference.get(), StoreHomeSearchActivity.class));
                break;
            case R.id.iv_more:
            case R.id.ll_search_layout2:
                showSelectBusinessStatusDialog();
                break;
            case R.id.iv_more2:
                showSelectBusinessStatusDialog();
                break;
        }
    }

    private RequestOptions getRequestOptions() {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.ic_user_default_avatar);    //???????????????????????????
        options.error(R.mipmap.ic_user_default_avatar);    //??????????????????????????????
        return options;
    }

    /**
     * ??????????????????
     */
    private void setBusinessStatus(UserBean.StoreBean storeBean) {
        tvBusinessStatus.setText(storeBean.getBusinessStatus() == 0 ? "????????????" : "????????????");
        if (!TextUtils.isEmpty(storeBean.getOpenTime()) && !TextUtils.isEmpty(storeBean.getCloseTime())) {
            String openTime = getP().onInterceptionBusinessHours(storeBean.getOpenTime());
            String closeTime = getP().onInterceptionBusinessHours(storeBean.getCloseTime());
            tvBusinessTime.setText(openTime + "-" + closeTime);
        }
    }

    /**
     * ??????????????????????????????
     */
    private void showSelectBusinessStatusDialog() {
        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true) //???????????????????????????????????????????????????
                .isDarkTheme(false)
                .asCustom(new StoreSelectBusinessStatusPopupView(reference.get(), new StoreSelectBusinessStatusPopupView.OnBusinessStatusListener() {
                    @Override
                    public void onStatus(int code, String name) {

                        getP().updateBusinessStatus(code);
                    }
                }).show());
    }

    /**
     * ??????IM??????????????????(???????????????web socket??????url)
     */
    @Override
    public void onIMSubscribesInfo(boolean refresh, SubscribesListBean subscribesListBean) {
        if (subscribesListBean != null && subscribesListBean.getList() != null) {
            //?????????????????????websocket url
            String webSocketUrl = SPUtils.getInstance().getString(WEB_SOCKET_URL);
            /*
             * ???????????????webSocketUrl??????????????????????????????????????????url???????????????
             * ?????????BaseWebSocket??????null,????????????????????????IMMessageReceivingService????????????
             */
            if (!webSocketUrl.equals(subscribesListBean.getWebsocketUrl())) {
//                EventBus.getDefault().post(100999);//???????????????
                resetImService();
            }

            //??????web socket??????url
            SPUtils.getInstance().put(WEB_SOCKET_URL, subscribesListBean.getWebsocketUrl());

            //??????IM??????????????????
            startImService();
            //mContext.startService(new Intent(mContext, IMMessageReceivingService.class));

            if (refresh)
                mStoreHomeIMMessageAdapter.setList(subscribesListBean.getList());
            else
                mStoreHomeIMMessageAdapter.addData(subscribesListBean.getList());

        }
    }

    @Override
    public void changeBusinessStatus(int status) {
        tvBusinessStatus.setText(status == 1 ? "????????????" : "????????????");
    }

    @Override
    public void clearMessageUnreadItem(String id) {

        //??????item????????????
        RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {
            for (int i = 0; i < mStoreHomeIMMessageAdapter.getData().size(); i++) {
                SubscribesListBean.SubscribesMessage data = mStoreHomeIMMessageAdapter.getData().get(i);

                if (data.getShareUuid().equals(id)) {
                    MessageCountManager.getInstance().reduceUnreadMessageNum(data.getUnreadNum());
                    data.setUnreadNum(0);
                    e.onNext(i);
                }
            }
            e.onComplete();
        }), (BaseActivity) reference.get()).subscribe(o -> {
            int pos = (int) o;

            if (StoreActivity.getInstance() != null)
                StoreActivity.getInstance().setUnreadMsgNum(MessageCountManager.getInstance().getUnreadMessageNum());

            mStoreHomeIMMessageAdapter.notifyItemChanged(pos);
        });

    }


}
