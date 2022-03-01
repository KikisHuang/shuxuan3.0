package com.gxdingo.sg.fragment.client;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.ChatActivity;
import com.gxdingo.sg.activity.ClientAddressListActivity;
import com.gxdingo.sg.activity.ClientSearchActivity;
import com.gxdingo.sg.activity.ClientStoreDetailsActivity;
import com.gxdingo.sg.adapter.ClientCategoryAdapter;
import com.gxdingo.sg.adapter.ClientStoreAdapter;
import com.gxdingo.sg.bean.HelpBean;
import com.gxdingo.sg.bean.ShareBean;
import com.gxdingo.sg.bean.changeLocationEvent;
import com.gxdingo.sg.biz.HelpListener;
import com.gxdingo.sg.biz.OnContentListener;
import com.gxdingo.sg.dialog.FillInvitationCodePopupView;
import com.gxdingo.sg.dialog.HelpPopupView;
import com.gxdingo.sg.utils.ClientLocalConstant;
import com.gxdingo.sg.utils.LocalConstant;
import com.kikis.commnlibrary.bean.AddressBean;
import com.gxdingo.sg.bean.CategoriesBean;
import com.gxdingo.sg.bean.StoreListBean;
import com.gxdingo.sg.biz.ClientHomeContract;
import com.gxdingo.sg.dialog.ClientCallPhoneDialog;
import com.gxdingo.sg.presenter.ClientHomePresenter;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.adapter.BaseRecyclerAdapter;
import com.kikis.commnlibrary.fragment.BaseMvpFragment;
import com.kikis.commnlibrary.utils.RecycleViewUtils;
import com.kikis.commnlibrary.utils.ScreenUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.blankj.utilcode.util.PermissionUtils.isGranted;
import static com.gxdingo.sg.utils.LocalConstant.BACK_TOP_SHOP;
import static com.gxdingo.sg.utils.LocalConstant.LOGIN_SUCCEED;
import static com.gxdingo.sg.utils.LocalConstant.FIRST_INTER_KEY;
import static com.gxdingo.sg.utils.LocalConstant.GO_SETTLED;
import static com.gxdingo.sg.utils.LocalConstant.REFRESH_LOCATION;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;
import static com.kikis.commnlibrary.utils.PermissionUtils.gotoPermission;
import static com.kikis.commnlibrary.utils.RecycleViewUtils.forceStopRecyclerViewScroll;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;
import static com.scwang.smart.refresh.layout.util.SmartUtil.dp2px;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientHomeFragment extends BaseMvpFragment<ClientHomeContract.ClientHomePresenter> implements ClientHomeContract.ClientHomeListener, OnItemChildClickListener, OnItemClickListener {

    @BindView(R.id.scrollView)
    public NestedScrollView scrollView;

    @BindView(R.id.title_layout)
    public RelativeLayout title_layout;

    @BindView(R.id.smartrefreshlayout)
    public SmartRefreshLayout smartrefreshlayout;

    @BindView(R.id.location_tv)
    public TextView location_tv;

    @BindView(R.id.location_tt_tv)
    public TextView location_tt_tv;

    @BindView(R.id.category_rv)
    public RecyclerView category_rv;


    @BindView(R.id.store_rv)
    public RecyclerView store_rv;

    @BindView(R.id.noStores_layout)
    public LinearLayout noStores_layout;

    @BindView(R.id.noLocation_layout)
    public LinearLayout noLocation_layout;

    private ClientStoreAdapter mStoreAdapter;

    private ClientCategoryAdapter mCategoryAdapter;

    //是否获取定位
    private boolean location = true;

    private int categoryId = 0;

    private int mTitleHeight = dp2px(70);


    @Override
    protected ClientHomeContract.ClientHomePresenter createPresenter() {
        return new ClientHomePresenter(true);
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
        return R.layout.module_fragment_client_home;
    }

    @Override
    protected View noDataLayout() {
        return noStores_layout;
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


    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        super.onRefresh(refreshLayout);
        if (location)
            getContentView().post(() -> getP().getNearbyStore(true, true, categoryId));
        else {
            getP().checkPermissions(getRxPermissions(), true);
            smartrefreshlayout.finishRefresh();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
//        getP().checkHelpCode();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void lazyInit() {
        super.lazyInit();
        categoryId = 0;
        mCategoryAdapter.setCategoryId(categoryId);
        mCategoryAdapter.notifyDataSetChanged();
        getContentView().post(() -> getP().getNearbyStore(true, true, categoryId));
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        super.onLoadMore(refreshLayout);
        if (location)
            getContentView().post(() -> getP().getNearbyStore(false, false, categoryId));
        else {
            getP().checkPermissions(getRxPermissions(), true);
            smartrefreshlayout.finishLoadMore();
        }

    }


    @Override
    protected void init() {

        scrollViewInit();
        mStoreAdapter = new ClientStoreAdapter();
        store_rv.setAdapter(mStoreAdapter);
        store_rv.setLayoutManager(new LinearLayoutManager(reference.get()));
        mStoreAdapter.setOnItemChildClickListener(this);
        mStoreAdapter.setOnItemClickListener(this);
        mCategoryAdapter = new ClientCategoryAdapter();
        category_rv.setAdapter(mCategoryAdapter);

        category_rv.setLayoutManager(new LinearLayoutManager(reference.get(), RecyclerView.HORIZONTAL, false));

        mCategoryAdapter.setOnItemClickListener(this);
    }

    @OnClick({R.id.location_tv, R.id.location_tt_tv, R.id.ll_search, R.id.btn_search, R.id.btn_empower, R.id.btn_become_store, R.id.btn_invitation})
    public void OnClickViews(View v) {
        switch (v.getId()) {

            case R.id.location_tt_tv:
            case R.id.location_tv:
                if (UserInfoUtils.getInstance().isLogin())
                    goToPagePutSerializable(reference.get(), ClientAddressListActivity.class, getIntentEntityMap(new Object[]{1}));
                else
                    getP().oauth(getContext());

                break;
            case R.id.btn_search:
            case R.id.ll_search:
                goToPage(getContext(), ClientSearchActivity.class, getIntentMap(new String[]{location_tv.getText().toString()}));
                break;
            case R.id.btn_empower:
//                getP().checkPermissions(getRxPermissions(), true);
                gotoPermission(reference.get());
                break;
            case R.id.btn_become_store:
                if (UserInfoUtils.getInstance().isLogin())
                    getP().convertStore();
                else
                    getP().oauth(getContext());
                break;
            case R.id.btn_invitation:
                sendEvent(GO_SETTLED);
//                IntentUtils.goToPage(reference.get(), ClientSettleActivity.class, null);
                break;
        }
    }

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
        if (object instanceof AddressBean) {
            AddressBean addressBean = (AddressBean) object;
            if (addressBean.selectType == 1) {
                this.location = true;
                getP().getNearbyStore(object, categoryId);
            }
        } else if (object instanceof changeLocationEvent) {
            this.location = true;
            changeLocationEvent event = (changeLocationEvent) object;
            getP().getNearbyStore(event, categoryId);
        }
    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type == REFRESH_LOCATION) {
            //判断如果有权限，进行重新定位，刷新操作
            if (isGranted(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION))
                getP().checkPermissions(getRxPermissions(), true);
        } else if (type == BACK_TOP_SHOP || type == LOGIN_SUCCEED) {
            if (scrollView != null) {
                scrollView.post(() -> {
                    scrollView.fling(0);
                    scrollView.smoothScrollTo(0, 0);
                });
            }
            forceStopRecyclerViewScroll(store_rv);
            //返回顶部
            RecycleViewUtils.MoveToPosition((LinearLayoutManager) store_rv.getLayoutManager(), store_rv, 0);
        }
    }

    @Override
    protected void initData() {

        getP().checkPermissions(getRxPermissions(), true);

        getP().getCategory();

    }


    private void scrollViewInit() {

        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            if (mTitleHeight > scrollY)
                setTitleViewAlpha(0f);
            else {
                setTitleViewAlpha(1);
            }

        });
    }


    @Override
    public void onFailed() {
        super.onFailed();
        location = false;
        if (noLocation_layout.getVisibility() == View.INVISIBLE || noLocation_layout.getVisibility() == View.GONE) {
            noLocation_layout.setVisibility(View.VISIBLE);
            NoDataBgSwitch(false);
        }

    }

    private void setTitleViewAlpha(float alpha) {
        title_layout.setAlpha(alpha);
        location_tt_tv.setAlpha(alpha);
    }

    @Override
    public void setDistrict(String district) {
        if (noLocation_layout.getVisibility() == View.VISIBLE)
            noLocation_layout.setVisibility(View.GONE);
        location_tv.setText(district);
        location_tt_tv.setText(district);
    }

    @Override
    public void onCategoryResult(List<CategoriesBean> categories) {
        if (mCategoryAdapter != null) {
            mCategoryAdapter.setList(categories);
        }
    }


    @Override
    public void onStoresResult(boolean refresh, boolean search, List<StoreListBean.StoreBean> storeBeans) {

        if (noLocation_layout.getVisibility() == View.VISIBLE)
            NoDataBgSwitch(false);

        if (refresh)
            mStoreAdapter.setList(storeBeans);
        else
            mStoreAdapter.addData(storeBeans);
    }


    @Override
    public void onHistoryResult(List<String> searchHistories) {

    }


    @Override
    public void onShareUrlResult(ShareBean shareBean) {

    }

    private void categorPitch(int id) {
        categoryId = id;
        mCategoryAdapter.setCategoryId(categoryId);
        mCategoryAdapter.notifyDataSetChanged();
        if (!location)
            getP().checkPermissions(getRxPermissions(), true);
        else
            getP().getNearbyStore(true, true, categoryId);
    }

    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        StoreListBean.StoreBean item = (StoreListBean.StoreBean) adapter.getItem(position);
        switch (view.getId()) {
            case R.id.store_avatar_iv:

                goToPagePutSerializable(getContext(), ClientStoreDetailsActivity.class, getIntentEntityMap(new Object[]{item.storeUserIdentifier}));
                break;
            case R.id.call_phone_iv:
                new XPopup.Builder(reference.get())
                        .isDarkTheme(false)
                        .asCustom(new ClientCallPhoneDialog(reference.get(), item.getContactNumber(), (popupView, content) -> getP().callStore(content)))
                        .show();
                break;
        }
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {

        if (adapter instanceof ClientCategoryAdapter) {
            //分类点击事件
            CategoriesBean categoriesBean = ((CategoriesBean) mCategoryAdapter.getData().get(position));

            if (mCategoryAdapter.getCategoryId() != categoriesBean.getId()) {
                categorPitch(categoriesBean.getId());
            } else {
                //反选(如果是0为全部，全部无需反选)
                if (categoriesBean.getId() != 0) {
                    categorPitch(0);
                }
            }

        } else {
            //店铺列表点击事件
            if (UserInfoUtils.getInstance().isLogin()) {
                StoreListBean.StoreBean item = (StoreListBean.StoreBean) adapter.getItem(position);
//        goToPagePutSerializable(getContext(), ClientStoreDetailsActivity.class,getIntentEntityMap(new Object[]{item.getId()}));
                goToPagePutSerializable(reference.get(), ChatActivity.class, getIntentEntityMap(new Object[]{null, 11, item.storeUserIdentifier}));
            } else {
                UserInfoUtils.getInstance().goToOauthPage(getContext());
            }
        }

    }

    @Override
    public void onSucceed(int type) {
        super.onSucceed(type);

    }
}
