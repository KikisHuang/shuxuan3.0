package com.gxdingo.sg.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.district.DistrictItem;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.SelectAddressAdapter;
import com.gxdingo.sg.bean.ItemDistanceBean;
import com.gxdingo.sg.bean.SelectAddressEvent;
import com.gxdingo.sg.biz.AddressContract;
import com.gxdingo.sg.dialog.PostionFunctionDialog;
import com.gxdingo.sg.presenter.AddressPresenter;
import com.gxdingo.sg.view.RegexEditText;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.bean.AddressBean;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.kikis.commnlibrary.dialog.BaseActionSheetPopupView;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.utils.RecycleViewUtils;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.ConvertUtils.dp2px;
import static com.blankj.utilcode.util.PermissionUtils.isGranted;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * @author: Kikis
 * @date: 2022/1/21
 * @page:
 */
public class AddressMapInfoActivity extends BaseMvpActivity<AddressContract.AddressPresenter> implements AddressContract.AddressListener, OnItemClickListener {


    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.address_info_cl)
    public ConstraintLayout address_info_cl;

    @BindView(R.id.contract_info_cl)
    public ConstraintLayout contract_info_cl;

    @BindView(R.id.address_street_tv)
    public TextView address_street_tv;

    @BindView(R.id.address_tv)
    public TextView address_tv;

    @BindView(R.id.distance_tv)
    public TextView distance_tv;

    @BindView(R.id.name_tv)
    public TextView name_tv;

    @BindView(R.id.phone_tv)
    public TextView phone_tv;

    @BindView(R.id.navigation_img)
    public ImageView navigation_img;

    @BindView(R.id.call_phone_img)
    public ImageView call_phone_img;

    private BaseActionSheetPopupView mNavigationPopupView;

    @BindView(R.id.mapView)
    public MapView mapView;

    private ReceiveIMMessageBean.DataByType mDataByType;
    private ReceiveIMMessageBean receiveIMMessageBean;

    @Override
    protected AddressContract.AddressPresenter createPresenter() {
        return new AddressPresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return true;
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
        return R.layout.module_activity_address_map_info;
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
        title_layout.setTitleText(getString(R.string.address_info));

        title_layout.setMoreImg(R.drawable.module_svg_more_8935);

        if (mapView != null)
            mapView.onCreate(savedInstanceState);

        getAMap().getUiSettings().setLogoBottomMargin(-100);//下移高德LOG到屏幕外
        // 去掉高德地图右下角隐藏的缩放按钮
        getAMap().getUiSettings().setZoomControlsEnabled(false);

        receiveIMMessageBean = (ReceiveIMMessageBean) getIntent().getSerializableExtra(Constant.SERIALIZABLE + 0);

        if (receiveIMMessageBean == null || receiveIMMessageBean.getDataByType() == null) {
            onMessage("没有获取到地址信息");
            finish();
        }
        mDataByType = receiveIMMessageBean.getDataByType();
        if (mDataByType != null) {
            //初始默认地址
            LatLng latLng = new LatLng(mDataByType.getLatitude(), mDataByType.getLongitude());//构造一个位置
            getAMap().moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

            addMarkers(mDataByType.getLatitude(), mDataByType.getLongitude());
        }

    }

    @Override
    protected void initData() {

        if (!isEmpty(mDataByType.getDoorplate()))
            address_street_tv.setText(mDataByType.getDoorplate());

        if (!isEmpty(mDataByType.getStreet()))
            address_tv.setText(mDataByType.getStreet());

        if (!isEmpty(mDataByType.getName()))
            name_tv.setText(mDataByType.getName());

        if (!isEmpty(mDataByType.getMobile()))
            phone_tv.setText(mDataByType.getMobile());

        if (isGranted(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION))
            getP().getDistance(mDataByType.getLatitude(), mDataByType.getLongitude());

    }


    @OnClick({R.id.btn_more, R.id.navigation_img, R.id.call_phone_img})
    public void OnClickViews(View view) {

        if (!checkClickInterval(view.getId()))
            return;

        switch (view.getId()) {
            case R.id.btn_more:
                showDialog(title_layout.findViewById(R.id.title));
                break;
            case R.id.navigation_img:
                showNaviDIalog();
                break;
            case R.id.call_phone_img:
                if (mDataByType != null)
                    getP().callPhone(mDataByType);
                break;
        }

    }

    private void showDialog(View view) {
        int pos[] = {-1, -1}; //保存当前坐标的数组

        view.getLocationOnScreen(pos); //获取选中的 Item 在屏幕中的位置，以左上角为原点 (0, 0)

        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .offsetY(pos[1])
                .offsetX(pos[0])
                .autoDismiss(true)
                .hasShadowBg(false)
                .asCustom(new PostionFunctionDialog(reference.get(), v -> {
                    goToPagePutSerializable(reference.get(), ForwardListActivity.class, getIntentEntityMap(new Object[]{receiveIMMessageBean}));

                }).show());
    }

    private void showNaviDIalog() {
        if (mNavigationPopupView == null) {
            mNavigationPopupView = (BaseActionSheetPopupView) new XPopup.Builder(reference.get())
                    .isDarkTheme(false)
                    .asCustom(new BaseActionSheetPopupView(reference.get()).addSheetItem(gets(R.string.gaode_map), gets(R.string.baidu_map), gets(R.string.tencent_map)).setItemClickListener((itemv, pos) -> {
                        getP().goOutSideNavigation(pos, mDataByType);
                    })).show();
        } else
            mNavigationPopupView.show();
    }


    @Override
    public void onDataResult(boolean refresh, List<AddressBean> addressBeans) {

    }

    @Override
    public void setAddressData(AddressBean bean) {

    }

    @Override
    public void saveBtnEnable(boolean en) {

    }

    @Override
    public int getAddressId() {
        return 0;
    }

    @Override
    public String getAddress() {
        return null;
    }

    @Override
    public String getAddressDetail() {
        return null;
    }

    @Override
    public String getContact() {
        return null;
    }

    @Override
    public String getMobile() {
        return null;
    }

    @Override
    public String getRegionPath() {
        return null;
    }


    @Override
    public LatLonPoint getPoint() {
        return null;
    }

    @Override
    public void showDelDialog() {

    }

    @Override
    public String getDoorplate() {
        return null;
    }

    @Override
    public void setCityName(String cityName) {
    }

    @Override
    public void searchResult(boolean refresh, List<PoiItem> poiItems, boolean isSearch) {

    }


    @Override
    public AMap getAMap() {
        return mapView.getMap();
    }

    @Override
    public void onDistanceResult(ItemDistanceBean bean) {

        if (!isEmpty(bean.distance))
            distance_tv.setText(bean.distance);

    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {

    }


    /**
     * 添加标注
     */
    private void addMarkers(double la, double lon) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.anchor(1.3f, 1.5f);//点标记的锚点
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_map_location_red);
        markerOptions.icon(BitmapDescriptorFactory
                .fromBitmap(bitmap));
        markerOptions.position(new LatLng(la, lon));
        Marker growMarker = mapView.getMap().addMarker(markerOptions);
        growMarker.setClickable(true); //marker 设置是否可点击
        growMarker.showInfoWindow();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null)
            mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        if (mapView != null) {
            mapView.onDestroy();
            mapView = null;
        }

        if (mNavigationPopupView != null) {
            mNavigationPopupView.setItemClickListener(null);
            mNavigationPopupView.destroy();
            mNavigationPopupView = null;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        if (mapView != null)
            mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        if (mapView != null)
            mapView.onPause();
    }
}
