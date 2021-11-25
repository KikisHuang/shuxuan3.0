package com.gxdingo.sg.activity;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.ClientStorePhotoAdapter;
import com.gxdingo.sg.bean.CategoriesBean;
import com.gxdingo.sg.bean.StoreDetail;
import com.gxdingo.sg.bean.StoreListBean;
import com.gxdingo.sg.biz.ClientHomeContract;
import com.gxdingo.sg.biz.ClientStoreContract;
import com.gxdingo.sg.presenter.ClientHomePresenter;
import com.gxdingo.sg.presenter.ClientStorePresenter;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.dialog.BaseActionSheetPopupView;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * @author: Weaving
 * @date: 2021/10/19
 * @page:
 */
public class ClientStoreDetailsActivity extends BaseMvpActivity<ClientStoreContract.ClientStorePresenter> implements ClientStoreContract.ClientStoreListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.mapView)
    public MapView mapView;

    @BindView(R.id.address_tv)
    public TextView address_tv;

    @BindView(R.id.distance_tv)
    public TextView distance_tv;

    @BindView(R.id.store_photo_rv)
    public RecyclerView store_photo_rv;

    private BasePopupView mNavigationPopupView;

    private int storeId;

    private StoreDetail mStoreDetail;

    private ClientStorePhotoAdapter mPhotoAdapter;

    private GestureDetector  gestureDetector;


    @Override
    protected ClientStoreContract.ClientStorePresenter createPresenter() {
        return new ClientStorePresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return false;
    }

    @Override
    protected int activityTitleLayout() {
        return R.layout.module_include_custom_title;
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
        return R.layout.module_activity_client_store_details;
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
        storeId = getIntent().getIntExtra(Constant.SERIALIZABLE + 0, 0);
        title_layout.setTitleText("金源便利店");
        title_layout.setMoreText("资质");
        if (mapView != null)
            mapView.onCreate(savedInstanceState);

        mPhotoAdapter = new ClientStorePhotoAdapter();
        store_photo_rv.setAdapter(mPhotoAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(reference.get());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        store_photo_rv.setLayoutManager(linearLayoutManager);

        gestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (mStoreDetail != null)
                    goToPagePutSerializable(reference.get(), ClientBusinessCircleActivity.class, getIntentEntityMap(new Object[]{mStoreDetail.getId()}));
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });

        store_photo_rv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

    }

    @OnClick({R.id.txt_more, R.id.ll_navigation, R.id.business_district_cl, R.id.ll_send_message, R.id.ll_phone_contract})
    public void onClickViews(View v) {
        switch (v.getId()) {
            case R.id.txt_more:
                if (mStoreDetail!=null && mStoreDetail.getLicence()!=null)
                    goToPage(this,StoreQualificationActivity.class,getIntentMap(new String[]{mStoreDetail.getLicence().getBusinessLicence()}));
                break;
            case R.id.ll_navigation:
                if (mNavigationPopupView == null) {
                    mNavigationPopupView = new XPopup.Builder(reference.get())
                            .isDarkTheme(false)
                            .asCustom(new BaseActionSheetPopupView(reference.get()).addSheetItem(gets(R.string.gaode_map), gets(R.string.baidu_map), gets(R.string.tencent_map)).setItemClickListener((itemv, pos) -> {
                                getP().goOutSideNavigation(pos);
                            })).show();
                } else
                    mNavigationPopupView.show();
                break;
            case R.id.business_district_cl:
                if (mStoreDetail != null)
                    goToPagePutSerializable(reference.get(), ClientBusinessCircleActivity.class, getIntentEntityMap(new Object[]{mStoreDetail.getId()}));
                break;
            case R.id.ll_send_message:
                if (UserInfoUtils.getInstance().isLogin()&&  mStoreDetail != null){
                    goToPagePutSerializable(reference.get(), ChatActivity.class, getIntentEntityMap(new Object[]{null,11,mStoreDetail.getId()}));
                }else {
                    UserInfoUtils.getInstance().goToOauthPage(this);
                }
                break;
            case R.id.ll_phone_contract:
                if (mStoreDetail != null)
                    getP().callStore(mStoreDetail.getContactNumber());
                break;
        }
    }


    @Override
    public void onStarts() {
        super.onStarts();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null)
            mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null)
            mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null)
            mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null)
            mapView.onDestroy();
    }

    @Override
    protected void initData() {
        getP().getStoreDetail(storeId);
    }


    @Override
    public void onStoreDetailResult(StoreDetail storeDetail) {
        if (storeDetail == null) {
            onMessage("未获取到商家信息！");
            finish();
        }
        mStoreDetail = storeDetail;
        title_layout.setTitleText(storeDetail.getName());
        address_tv.setText(storeDetail.getAddress());
        if (!isEmpty(storeDetail.getDistance()))
            distance_tv.setText(storeDetail.getDistance());
        else
            distance_tv.setVisibility(View.GONE);
        mPhotoAdapter.setList(storeDetail.getImages());
    }

    @Override
    public AMap getMap() {
        return mapView.getMap();
    }
}
