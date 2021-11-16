package com.gxdingo.sg.fragment.client;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.kikis.commnlibrary.bean.AddressBean;
import com.gxdingo.sg.bean.CategoriesBean;
import com.gxdingo.sg.bean.StoreListBean;
import com.gxdingo.sg.biz.ClientHomeContract;
import com.gxdingo.sg.dialog.ClientCallPhoneDialog;
import com.gxdingo.sg.model.OneKeyModel;
import com.gxdingo.sg.presenter.ClientHomePresenter;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.activitiy.BaseActivity;
import com.kikis.commnlibrary.adapter.BaseRecyclerAdapter;
import com.kikis.commnlibrary.fragment.BaseMvpFragment;
import com.kikis.commnlibrary.utils.RxUtil;
import com.lxj.xpopup.XPopup;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;
import static com.scwang.smart.refresh.layout.util.SmartUtil.dp2px;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientHomeFragment extends BaseMvpFragment<ClientHomeContract.ClientHomePresenter> implements ClientHomeContract.ClientHomeListener, BaseRecyclerAdapter.OnItemClickListener, OnItemChildClickListener, OnItemClickListener {

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

    private List<CategoriesBean> mDefaultTypeData;

    private List<CategoriesBean> mAllTypeData;

    //是否获取定位
    private boolean location = true ;

    private int categoryId = 0;

    private int mTitleHeight = dp2px(80);


    @Override
    protected ClientHomeContract.ClientHomePresenter createPresenter() {
        return new ClientHomePresenter();
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
            getP().getNearbyStore(true,categoryId);
        else{
            getP().checkPermissions(getRxPermissions());
            smartrefreshlayout.finishRefresh();
        }

    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        categoryId = 0;
//        getP().getNearbyStore(true,categoryId);
//    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            categoryId = 0;
            getP().getNearbyStore(true,categoryId);
        }
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        super.onLoadMore(refreshLayout);
        if (location)
            getP().getNearbyStore(false,categoryId);
        else{
            getP().checkPermissions(getRxPermissions());
            smartrefreshlayout.finishLoadMore();
        }

    }

    @Override
    protected void init() {
        scrollViewInit();
        mStoreAdapter = new ClientStoreAdapter(0);
        store_rv.setAdapter(mStoreAdapter);
        store_rv.setLayoutManager(new LinearLayoutManager(reference.get()));
        mStoreAdapter.setOnItemChildClickListener(this);
        mStoreAdapter.setOnItemClickListener(this);
        mCategoryAdapter = new ClientCategoryAdapter();
        category_rv.setAdapter(mCategoryAdapter);
        category_rv.setLayoutManager(new GridLayoutManager(reference.get(),5){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mCategoryAdapter.setOnItemClickListener(this);
    }

    @OnClick({R.id.location_tv,R.id.location_tt_tv,R.id.ll_search,R.id.btn_search,R.id.btn_empower,R.id.btn_become_store,R.id.btn_invitation})
    public void OnClickViews(View v){
        switch (v.getId()){
            case R.id.location_tt_tv:
            case R.id.location_tv:
                if (UserInfoUtils.getInstance().isLogin())
                    goToPagePutSerializable(reference.get(), ClientAddressListActivity.class,getIntentEntityMap(new Object[]{1}));
                else
                    new OneKeyModel().sdkInit(getContext());
                break;
            case R.id.btn_search:
            case R.id.ll_search:
                goToPage(getContext(), ClientSearchActivity.class,getIntentMap(new String[]{location_tv.getText().toString()}));
                break;
            case R.id.btn_empower:
                getP().checkPermissions(getRxPermissions());
                break;
            case R.id.btn_become_store:
                getP().convertStore();
                break;
            case R.id.btn_invitation:
                Intent textIntent = new Intent(Intent.ACTION_SEND);
                textIntent.setType("text/plain");
                textIntent.putExtra(Intent.EXTRA_TEXT, "http://gxdingo.com/getapp-shuxuan");
                startActivity(Intent.createChooser(textIntent, "分享"));
                break;
        }
    }

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
        if (object instanceof AddressBean){
            AddressBean addressBean = (AddressBean) object;
            if (addressBean.selectType==1){
                this.location = true;
                getP().getNearbyStore((AddressBean) object,categoryId);
            }

        }
    }

    @Override
    protected void initData() {

        mAllTypeData = new ArrayList<>();
        mDefaultTypeData = new ArrayList<>();
        getP().checkPermissions(getRxPermissions());
        getP().getCategory();
    }

    private void addData(List<CategoriesBean> categories) {

        mCategoryAdapter.clear();
        mCategoryAdapter.notifyDataSetChanged();

        RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {

            mAllTypeData.addAll(categories);

            for (int i = 0; i < categories.size(); i++) {
                if (mDefaultTypeData.size() < 4)
                    mDefaultTypeData.add(categories.get(i));

            }

            e.onNext(mDefaultTypeData);
            e.onComplete();
        }), (BaseActivity) reference.get()).subscribe(o -> switchData(mDefaultTypeData));

    }


    private void switchData(List<CategoriesBean> data) {
        mCategoryAdapter.clear();

        mCategoryAdapter.addDataAll(data);

        if (mAllTypeData.size() > 4) {
            CategoriesBean categoriesBean = new CategoriesBean();
            mCategoryAdapter.addData(categoriesBean, mCategoryAdapter.getData().size());
        }

        mCategoryAdapter.notifyDataSetChanged();
    }

    private void scrollViewInit() {

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (mTitleHeight > scrollY)
                    setTitleViewAlpha(0f);
                else {
                    setTitleViewAlpha(1);
                }

            }
        });
    }

    @Override
    public void onFailed() {
        super.onFailed();
        location = false;
        if (noLocation_layout.getVisibility() == View.INVISIBLE ||noLocation_layout.getVisibility() == View.GONE )
            noLocation_layout.setVisibility(View.VISIBLE);
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
        addData(categories);
    }


    @Override
    public void onStoresResult(boolean refresh, List<StoreListBean.StoreBean> storeBeans) {
        if (refresh)
            mStoreAdapter.setList(storeBeans);
        else
            mStoreAdapter.addData(storeBeans);
    }

    @Override
    public void onHistoryResult(List<String> searchHistories) {

    }

    @Override
    public void onItemClick(View itemView, int pos) {
        if (mCategoryAdapter.getData().size() > 4 && pos == mCategoryAdapter.getData().size() - 1) {

            boolean expan = ((CategoriesBean) mCategoryAdapter.getData().get(pos)).isSelected;

            switchData(expan ? mDefaultTypeData : mAllTypeData);

            ((CategoriesBean) mCategoryAdapter.getData().get(mCategoryAdapter.getData().size() - 1)).isSelected = !expan;

            mCategoryAdapter.notifyDataSetChanged();
        }else {
            CategoriesBean categoriesBean = ((CategoriesBean) mCategoryAdapter.getData().get(pos));
            categoryId = categoriesBean.getId();
            if (!location)
                getP().checkPermissions(getRxPermissions());
            else
                getP().getNearbyStore(true,categoryId);
        }
    }

    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        switch (view.getId()){
            case R.id.store_avatar_iv:
                StoreListBean.StoreBean item = (StoreListBean.StoreBean) adapter.getItem(position);
                goToPagePutSerializable(getContext(), ClientStoreDetailsActivity.class,getIntentEntityMap(new Object[]{item.getId()}));
                break;
            case R.id.call_phone_iv:
                new XPopup.Builder(reference.get())
                        .isDarkTheme(false)
                        .asCustom(new ClientCallPhoneDialog(reference.get(),""))
                        .show();
                break;
        }

    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {

        if (UserInfoUtils.getInstance().isLogin()){
            StoreListBean.StoreBean item = (StoreListBean.StoreBean) adapter.getItem(position);
//        goToPagePutSerializable(getContext(), ClientStoreDetailsActivity.class,getIntentEntityMap(new Object[]{item.getId()}));
            goToPagePutSerializable(reference.get(), ChatActivity.class, getIntentEntityMap(new Object[]{null,11,item.getId()}));
        }else {
            UserInfoUtils.getInstance().goToLoginPage(getContext(),"");
        }
    }
}
