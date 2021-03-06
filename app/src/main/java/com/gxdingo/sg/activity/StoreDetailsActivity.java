package com.gxdingo.sg.activity;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.bumptech.glide.Glide;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.ClientStorePhotoAdapter;
import com.gxdingo.sg.adapter.ShopsIconAdapter;
import com.gxdingo.sg.bean.StoreAuthInfoBean;
import com.gxdingo.sg.bean.StoreDetail;
import com.gxdingo.sg.biz.ClientStoreContract;
import com.gxdingo.sg.dialog.PostionFunctionDialog;
import com.gxdingo.sg.dialog.SgConfirm2ButtonPopupView;
import com.gxdingo.sg.presenter.ClientStorePresenter;
import com.gxdingo.sg.utils.ShareUtils;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.dialog.BaseActionSheetPopupView;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.utils.GlideUtils;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;
import static android.widget.LinearLayout.HORIZONTAL;
import static cc.shinichi.library.ImagePreview.LoadStrategy.NetworkAuto;
import static com.gxdingo.sg.utils.DateUtils.dealDateFormat;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.IntentUtils.getImagePreviewInstance;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * @author: Weaving
 * @date: 2021/10/19
 * @page:
 */
public class StoreDetailsActivity extends BaseMvpActivity<ClientStoreContract.ClientStorePresenter> implements ClientStoreContract.ClientStoreListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.mapView)
    public MapView mapView;

    @BindView(R.id.ll_navigation)
    public LinearLayout ll_navigation;

    @BindView(R.id.address_tv)
    public TextView address_tv;

    @BindView(R.id.distance_tv)
    public TextView distance_tv;

    @BindView(R.id.business_district_cl)
    public ConstraintLayout business_district_cl;

    @BindView(R.id.address_cl)
    public ConstraintLayout address_cl;

    @BindView(R.id.top_line)
    public TextView top_line;

    @BindView(R.id.store_photo_rv)
    public RecyclerView store_photo_rv;

    @BindView(R.id.avatar_img)
    public ImageView avatar_img;

    @BindView(R.id.icon_rcy)
    public RecyclerView icon_rcy;

    @BindView(R.id.name_tv)
    public TextView name_tv;

    @BindView(R.id.secondary_tv)
    public TextView secondary_tv;

    private BasePopupView mNavigationPopupView;

    private String storeId;

    private StoreDetail mStoreDetail;

    private ClientStorePhotoAdapter mPhotoAdapter;

    private GestureDetector gestureDetector;
    private ShopsIconAdapter mIconAdapter;

    private SgConfirm2ButtonPopupView mLoginDialog;

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
        return R.layout.module_activity_store_details;
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

        storeId = getIntent().getStringExtra(Constant.SERIALIZABLE + 0);
        if (isEmpty(storeId)) {
            onMessage("???????????????????????????");
            finish();
        }
        title_layout.setTitleText("???????????????");
//        title_layout.setMoreText("??????");
        title_layout.setMoreImg(R.drawable.module_svg_more_8935);
        if (mapView != null)
            mapView.onCreate(savedInstanceState);

        //???????????????RecyclerView
        mPhotoAdapter = new ClientStorePhotoAdapter();
        store_photo_rv.setAdapter(mPhotoAdapter);
        store_photo_rv.setLayoutManager(new LinearLayoutManager(reference.get(),LinearLayoutManager.HORIZONTAL,false));

        //????????????RecyclerView
        icon_rcy.setLayoutManager(new LinearLayoutManager(reference.get(),LinearLayoutManager.HORIZONTAL,false));
        mIconAdapter = new ShopsIconAdapter();
        icon_rcy.setAdapter(mIconAdapter);

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
                    goToPagePutSerializable(reference.get(), ClientBusinessCircleActivity.class, getIntentEntityMap(new Object[]{storeId, mStoreDetail.getName()}));
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

    @Override
    protected void initData() {
        getP().getStoreDetail(getRxPermissions(), storeId);
    }

    @OnClick({R.id.avatar_img, R.id.btn_more, R.id.ll_navigation, R.id.business_district_cl, R.id.ll_send_message, R.id.ll_phone_contract})
    public void onClickViews(View v) {

        if (!UserInfoUtils.getInstance().isLogin()) {
            showLoginDialog(getString(R.string.please_login_before_operation));
            return;
        }

        switch (v.getId()) {
            case R.id.btn_more:

                showBaseDialog();
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
                if (!isEmpty(storeId) && mStoreDetail != null)
                    goToPagePutSerializable(reference.get(), ClientBusinessCircleActivity.class, getIntentEntityMap(new Object[]{storeId, mStoreDetail.getName()}));
                break;
            case R.id.ll_send_message:
                if (UserInfoUtils.getInstance().isLogin() && mStoreDetail != null) {
                    goToPagePutSerializable(reference.get(), ChatActivity.class, getIntentEntityMap(new Object[]{null, 11, storeId}));
                } else {
                    UserInfoUtils.getInstance().goToOauthPage(this);
                }
                break;
            case R.id.ll_phone_contract:
                if (mStoreDetail != null)
                    getP().callStore(mStoreDetail.getContactNumber());
                break;
            case R.id.avatar_img:
                if (mStoreDetail != null)
                    getImagePreviewInstance(reference.get(), NetworkAuto, 0, true).setImage(mStoreDetail.getAvatar()).start();

                break;
        }
    }

    private void showBaseDialog() {

        int pos[] = {-1, -1}; //???????????????????????????

        title_layout.findViewById(com.kikis.commnlibrary.R.id.title).getLocationOnScreen(pos);//??????????????? Item ????????????????????????????????????????????? (0, 0)

        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true) //???????????????????????????????????????????????????
                .offsetY(pos[1])
                .offsetX(pos[0])
                .autoDismiss(true)
                .hasShadowBg(false)
                .asCustom(new PostionFunctionDialog(reference.get(), v -> {
                    switch (v.getId()) {
                        case R.id.certification_ll:
                            if (mStoreDetail != null && mStoreDetail.getLicence() != null)
                                goToPage(this, StoreQualificationActivity.class, getIntentMap(new String[]{mStoreDetail.getLicence().getBusinessLicence(),storeId}));
                            break;
                        case R.id.share_ll:
                            if (mStoreDetail != null && !isEmpty(mStoreDetail.forwardingUrl))
                                ShareUtils.UmShare(reference.get(), null, mStoreDetail.forwardingUrl, "????????????", mStoreDetail.getIntroduction(), R.mipmap.ic_app_logo, SHARE_MEDIA.WEIXIN);

                            break;
                        case R.id.report_ll:
                            goToPagePutSerializable(reference.get(), IMComplaintActivity.class, getIntentEntityMap(new Object[]{storeId}));
                            break;

                    }

                }, mStoreDetail.getReleaseUserType() == 0 ? 2 : 1).show());
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
        if (mapView != null) {
            mapView.getMap().clear();
            mapView.onDestroy();
            mapView = null;
        }
        if (mNavigationPopupView != null) {
            mNavigationPopupView.destroy();
            mNavigationPopupView = null;
        }
        if (mLoginDialog != null) {
            mLoginDialog.destroy();
            mLoginDialog = null;
        }
    }


    @Override
    public void onStoreDetailResult(StoreDetail storeDetail) {

        mStoreDetail = storeDetail;

        Glide.with(reference.get()).load(!isEmpty(storeDetail.getAvatar()) ? storeDetail.getAvatar() : R.drawable.module_svg_client_default_avatar).apply(GlideUtils.getInstance().getDefaultOptions()).into(avatar_img);


        if (!isEmpty(storeDetail.getName()))
            name_tv.setText(storeDetail.getName());

        if (!isEmpty(storeDetail.getName()))
            secondary_tv.setText(storeDetail.getName());

        StringBuffer label = new StringBuffer();

        if (storeDetail.iconList != null && storeDetail.iconList.size() > 0)
            mIconAdapter.setList(storeDetail.iconList);

        // ????????????0=?????? 1=??????
        if (storeDetail.getReleaseUserType() == 0) {
            mapView.setVisibility(View.VISIBLE);
            address_cl.setVisibility(View.VISIBLE);
            ll_navigation.setVisibility(View.VISIBLE);

            for (int i = 0; i < storeDetail.getClassNameList().size(); i++) {
                if (i == 0)
                    label.append(storeDetail.getClassNameList().get(i));
                else
                    label.append(" | " + storeDetail.getClassNameList().get(i));
            }

            if (!isEmpty(storeDetail.getOpenTime()) && !isEmpty(storeDetail.getCloseTime()))
                label.append(" " + storeDetail.getOpenTime() + " - " + storeDetail.getCloseTime());

        } else {
            top_line.setVisibility(storeDetail.getImages() != null && storeDetail.getImages().size() > 0 ? View.VISIBLE : View.GONE);

            mapView.setVisibility(View.GONE);
            ll_navigation.setVisibility(View.GONE);
            address_cl.setVisibility(View.GONE);

            if (storeDetail.getAuthStatus() == 0) {
                label.append("?????????");
            } else if (storeDetail.getAuthStatus() == 1) {
                label.append("?????????");
            } else if (storeDetail.getAuthStatus() == 2) {
                label.append("?????????");
            } else if (storeDetail.getAuthStatus() == 3) {
                label.append("????????????");
            }
        }

        secondary_tv.setText(label);

        if (storeDetail == null) {
            onMessage("???????????????????????????");
            finish();
        }

        title_layout.setTitleText(storeDetail.getName());
        address_tv.setText(storeDetail.getAddress());

        if (!isEmpty(storeDetail.getDistance()))
            distance_tv.setText(storeDetail.getDistance());
        else
            distance_tv.setVisibility(View.GONE);

        mPhotoAdapter.setList(storeDetail.getImages());

        business_district_cl.setVisibility(storeDetail == null || storeDetail.getImages() == null || storeDetail.getImages().size() <= 0 ? View.GONE : View.VISIBLE);

    }

    @Override
    public AMap getMap() {
        return mapView.getMap();
    }

    @Override
    public void onQualificationsDataResult(List<StoreAuthInfoBean.CategoryListBean> newData) {

    }


    private void showLoginDialog(String msg) {
        if (mLoginDialog == null) {
            mLoginDialog = new SgConfirm2ButtonPopupView(reference.get(), msg, () -> UserInfoUtils.getInstance().goToOauthPage(reference.get()));

            new XPopup.Builder(reference.get())
                    .isDarkTheme(false)
                    .asCustom(mLoginDialog).show();
        } else if (!mLoginDialog.isShow())
            mLoginDialog.show();
    }

}
