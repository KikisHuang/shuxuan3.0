package com.gxdingo.sg.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
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
import com.kikis.commnlibrary.bean.AddressBean;
import com.gxdingo.sg.biz.AddressContract;
import com.gxdingo.sg.presenter.AddressPresenter;
import com.gxdingo.sg.view.RegexEditText;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.utils.RecycleViewUtils;
import com.kikis.commnlibrary.view.TemplateTitle;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cc.shinichi.library.tool.ui.ToastUtil;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.ConvertUtils.dp2px;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;
import static com.kikis.commnlibrary.utils.CommonUtils.getPath;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;

/**
 * @author: Weaving
 * @date: 2021/10/17
 * @page:
 */
public class SelectAddressActivity extends BaseMvpActivity<AddressContract.AddressPresenter> implements AddressContract.AddressListener, OnItemClickListener {


    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.tv_location)
    public TextView tv_location;

    @BindView(R.id.return_to_location_bt)
    public FrameLayout return_to_location_bt;

    @BindView(R.id.et_keyword)
    public RegexEditText et_keyword;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.nodata_layout)
    public View nodata_layout;

    @BindView(R.id.card_view)
    public CardView card_view;

    @BindView(R.id.bottom_sheet_layout)
    public ConstraintLayout bottom_sheet_layout;

    private BottomSheetBehavior behavior;

    @BindView(R.id.map_fl)
    public FrameLayout map_fl;


    private SelectAddressAdapter mAdapter;

    private DistrictItem districtItem;

    @BindView(R.id.mapView)
    public MapView mapView;

    private SelectAddressEvent selectAddressEvent;

    private boolean mIsScreenshots = true;

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
        return nodata_layout;
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
        return R.layout.module_activity_select_address_new;
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
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
        //旧方法(废弃)，城市回调
        if (object instanceof DistrictItem) {
            districtItem = (DistrictItem) object;
            tv_location.setText(districtItem.getName());
            getP().searchPOIAsyn(true, et_keyword.getText().toString(), districtItem.getAdcode());
        }
    }

    @Override
    protected void init() {

        mIsScreenshots = getIntent().getBooleanExtra(Constant.SERIALIZABLE + 0, true);
        title_layout.setTitleText(getString(R.string.select_receiving_address));

        title_layout.setMoreText(gets(R.string.confirm));
        mAdapter = new SelectAddressAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(reference.get()));

        mAdapter.setOnItemClickListener(this);

        behavior = BottomSheetBehavior.from(bottom_sheet_layout);

        int peekHieght = (int) (ScreenUtils.getAppScreenHeight() / 2.2);

        behavior.setPeekHeight(peekHieght);

        behavior.setState(STATE_COLLAPSED);

        map_fl.setPadding(0, 0, 0, peekHieght - dp2px(75));

        behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                card_view.setRadius(newState == STATE_EXPANDED ? dp2px(0) : dp2px(20));

                if (newState == STATE_EXPANDED && return_to_location_bt.getVisibility() == View.VISIBLE)
                    return_to_location_bt.setVisibility(View.GONE);

                if (newState == STATE_COLLAPSED && return_to_location_bt.getVisibility() == View.GONE)
                    return_to_location_bt.setVisibility(View.VISIBLE);

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        if (mapView != null)
            mapView.onCreate(savedInstanceState);

        //初始默认地址
        LatLng latLng = new LatLng(22.817802, 108.365386);//构造一个位置
        getAMap().moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));

        //todo 不设置延时有时候会出现有数据返回但是列表不显示的问题，后期待排查
        getContentView().postDelayed(() -> getP().checkPermissions(getRxPermissions()), 100);
    }

    @Override
    protected void initData() {
        selectAddressEvent = new SelectAddressEvent();
    }


    @OnClick({R.id.btn_more, R.id.return_to_location_bt, R.id.ll_location, R.id.btn_search})
    public void OnClickViews(View view) {

        clickInterval = 500;

        if (!checkClickInterval(view.getId()))
            return;

        switch (view.getId()) {
            case R.id.btn_more:
                if (selectAddressEvent.poiItem != null) {
                    mapScreenShot();
                } else
                    onMessage("请选择一个地址");
                break;
            case R.id.ll_location:
//                goToPage(this, ClientLocationCityActivity.class, null);
                break;
            case R.id.btn_search:
                search();
                break;
            case R.id.return_to_location_bt:
                getP().moveCamera(null);
                break;
            case R.id.foot_layout:

                getP().loadmoreData(et_keyword.getText().toString());

                break;
        }

    }

    /**
     * 对地图进行截屏
     */
    private void mapScreenShot() {
        //不截屏直接退出
        if (!mIsScreenshots)
            finish();

        onStarts();
        getAMap().getMapScreenShot(new AMap.OnMapScreenShotListener() {
            @Override
            public void onMapScreenShot(Bitmap bitmap) {

            }

            @Override
            public void onMapScreenShot(Bitmap bitmap, int status) {
                onAfters();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                if (null == bitmap) {
                    return;
                }
                try {
                    String path = getPath() + "/shuxuan_mapScreenShop_"
                            + sdf.format(new Date()) + ".png";

                    FileOutputStream fos = new FileOutputStream(path);

                    boolean b = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    try {
                        fos.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //status!=0 地图渲染完成，截屏无网格 else 地图未渲染完成，截屏有网格
                    if (b) {
                        //截屏成功
                        selectAddressEvent.fliepath = path;
                        sendEvent(selectAddressEvent);
                        finish();
                    } else
                        onMessage("获取截图信息失败,请重新操作");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void search() {
        String keyword = et_keyword.getText().toString();
        if (isEmpty(keyword)) {
            onMessage("搜索内容不能为空");
        } else {
            mAdapter.setKeyWord(keyword);
            if (districtItem != null)
                getP().searchPOIAsyn(true, keyword, districtItem.getAdcode());
            else
                getP().searchPOIAsyn(true, keyword, "");
        }
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
        if (!isEmpty(cityName))
            tv_location.setText(cityName);
    }

    @Override
    public void searchResult(boolean refresh, List<PoiItem> poiItems, boolean isSearch) {

        if ((poiItems.size() <= 0 && mAdapter.getFooterLayoutCount() > 0))
            mAdapter.removeFooterView(LayoutInflater.from(reference.get()).inflate(R.layout.module_include_loadmore_foot, new LinearLayout(reference.get()), false));

        if (refresh) {
            if (poiItems.size() > 0) {
                if (mAdapter.getFooterLayoutCount() <= 0) {
                    LinearLayout layout = (LinearLayout) LayoutInflater.from(reference.get()).inflate(R.layout.module_include_loadmore_foot, new LinearLayout(reference.get()), false);

                    mAdapter.addFooterView(layout);
                    layout.setOnClickListener(v -> {
                        OnClickViews(v);
                    });
                }
            }

            if (isSearch)
                mAdapter.checkPost(-1);

            mAdapter.setList(poiItems);
//            mAdapter.notifyDataSetChanged();
        } else {

            if (poiItems.size() < 15) {
                if (mAdapter.getFooterLayout() != null)
                    mAdapter.getFooterLayout().setVisibility(View.GONE);

            } else {
                if (mAdapter.getFooterLayout() != null)
                    mAdapter.getFooterLayout().setVisibility(View.VISIBLE);
            }

            mAdapter.addData(poiItems);
        }

    }

    @Override
    public AMap getAMap() {
        return mapView.getMap();
    }

    @Override
    public void onDistanceResult(ItemDistanceBean bean) {

    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        if (mapView != null && mapView.getMap() != null)
            mapView.getMap().clear(true);

        PoiItem poiItem = (PoiItem) adapter.getItem(position);
//        sendEvent(poiItem);
        selectAddressEvent.poiItem = poiItem;
        mAdapter.checkPost(0);

        getP().moveCamera(new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude()));

        addMarkers(poiItem);

        RecycleViewUtils.MoveToPositionTop(recyclerView, 0);

        behavior.setState(STATE_COLLAPSED);
    }


    /**
     * 添加标注
     *
     * @param poiItem
     */
    private void addMarkers(PoiItem poiItem) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.anchor(1.3f, 1.5f);//点标记的锚点
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_map_location_red);
        markerOptions.icon(BitmapDescriptorFactory
                .fromBitmap(bitmap));
        markerOptions.position(new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude()));
        Marker growMarker = mapView.getMap().addMarker(markerOptions);
        growMarker.setClickable(true); //marker 设置是否可点击
        startGrowAnimation(growMarker);
        growMarker.showInfoWindow();
    }


    private void startGrowAnimation(Marker marker) {

        if (marker != null) {
            Animation animation = new ScaleAnimation(0, 1, 0, 1);
            animation.setInterpolator(new LinearInterpolator());

            //整个移动所需要的时间
            animation.setDuration(200);
            animation.setFillMode(1);//动画保存之前的状态为1 之后为0
            //设置动画
            marker.setAnimation(animation);
            //开始动画
            marker.startAnimation();
            marker.showInfoWindow();
        }
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
        if (mapView != null)
            mapView.onDestroy();
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
