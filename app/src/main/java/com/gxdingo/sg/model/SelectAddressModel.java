package com.gxdingo.sg.model;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.poisearch.PoiSearch;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.gxdingo.sg.R;

import static com.amap.api.maps.model.MyLocationStyle.LOCATION_TYPE_SHOW;
import static com.gxdingo.sg.utils.ClientLocalConstant.LOCATION_LATITUDE_KEY;
import static com.gxdingo.sg.utils.ClientLocalConstant.LOCATION_LONGITUDE_KEY;

/**
 * @author: Weaving
 * @date: 2021/10/17
 * @page:
 */
public class SelectAddressModel implements AMap.OnMyLocationChangeListener, AMap.OnMapClickListener{

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private PoiSearch.Query query;

    private PoiSearch poiSearch;

    private DistrictSearch search;

    private GeocodeSearch geocoderSearch;

    private DistrictSearchQuery districtQuery;

    //本地我的定位
    private Location mMyLocation;

    private AMap mAmap;

    private Context mContext;

    private String cityCode;

    public void location(Context context, AMapLocationListener mLocationListener) {
        this.mContext = context;

        //初始化定位
        mLocationClient = new AMapLocationClient(context);

        mLocationClient.setLocationListener(mLocationListener);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setOnceLocation(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    public void setCityCode(String code) {
        cityCode = code;
    }

    public void stopLocation() {
        mLocationClient.stopLocation();
    }

    /**
     * 关键字检索POI
     *
     * @param keyWord
     * @param cityCode
     * @param listener
     */
    public void retrievalPOI(String keyWord, String cityCode, PoiSearch.OnPoiSearchListener listener) {
        query = new PoiSearch.Query(keyWord, "", cityCode);
        query.setExtensions(PoiSearch.EXTENSIONS_ALL);
        poiSearch = new PoiSearch(mContext, query);
        poiSearch.setOnPoiSearchListener(listener);
        poiSearch.searchPOIAsyn();
    }

    /**
     * 周边检索POI
     *
     * @param keyWord
     * @param cityCode
     * @param latitude
     * @param longitude
     * @param page
     */
    public void retrievalBoundPOI(String keyWord, String cityCode,
                                  double latitude, double longitude, int page, PoiSearch.OnPoiSearchListener listener) {
        query = new PoiSearch.Query(keyWord, "120000|170000|190107", cityCode);
        query.setExtensions(PoiSearch.EXTENSIONS_ALL);
        poiSearch = new PoiSearch(mContext, query);
        query.setPageNum(page);
        poiSearch.setOnPoiSearchListener(listener);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latitude, longitude), 1000));

        poiSearch.searchPOIAsyn();
    }

    /**
     * 查询行政区的异步接口
     *
     * @param keyword
     * @param page
     * @param listener
     */
    public void onDistrictQuery(String keyword, int page, DistrictSearch.OnDistrictSearchListener listener) {
        search = new DistrictSearch(mContext);
        districtQuery = new DistrictSearchQuery();
        districtQuery.setKeywords(keyword);
        districtQuery.setPageNum(page);
        districtQuery.setShowBoundary(true);
        search.setQuery(districtQuery);
        search.setOnDistrictSearchListener(listener);
        search.searchDistrictAsyn();
    }


    public void mapInit(AMap aMap, AMap.OnCameraChangeListener changeListener) {
        if (mAmap == null)
            this.mAmap = aMap;


        UiSettings mUiSettings = aMap.getUiSettings();

        mUiSettings.setZoomControlsEnabled(false);

        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。

        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_gps_location_marker));

        myLocationStyle.strokeWidth(0);

        myLocationStyle.strokeColor(Color.TRANSPARENT);//设置定位蓝点精度圆圈的边框颜色的方法。

        myLocationStyle.radiusFillColor(Color.TRANSPARENT);//设置定位蓝点精度圆圈的填充颜色的方法。

        myLocationStyle.myLocationType(LOCATION_TYPE_SHOW);//只定位一次

        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style

        aMap.setOnMapClickListener(this);

        aMap.setOnCameraChangeListener(changeListener);
/*
        int y = (int) (ScreenUtils.getScreenHeight() / 6.5);
        int x = ScreenUtils.getAppScreenWidth() / 2;
        //默认中心点
        aMap.setPointToCenter(x, y);*/


        double lat = Double.parseDouble(SPUtils.getInstance().getString(LOCATION_LATITUDE_KEY, "22.812972"));
        double lon = Double.parseDouble(SPUtils.getInstance().getString(LOCATION_LONGITUDE_KEY, "108.372339"));
        //默认位置
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 15));

        //设置希望展示的地图缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。

        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setOnMyLocationChangeListener(this);
        // myLocationStyle.myLocationIcon();//设置定位蓝点的icon图标方法，需要用到BitmapDescriptor类对象作为参数。
    }


    public Location getMyLocation() {
        return mMyLocation;
    }

    /**
     * 移动到坐标定位
     */
    public void moveCamera(LatLng latLng) {
        if (mAmap == null)
            return;

        CameraUpdate camera =
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        new LatLng(latLng.latitude, latLng.longitude),
                        15,
                        0f,
                        0f));
        mAmap.moveCamera(camera);
    }


    @Override
    public void onMyLocationChange(Location location) {

        if (location != null) {
            LogUtils.i("onMyLocationChange  Latitude === " + location.getLatitude() + " Longitude === " + location.getLongitude());

            SPUtils.getInstance().put(LOCATION_LATITUDE_KEY, String.valueOf(location.getLatitude()));
            SPUtils.getInstance().put(LOCATION_LONGITUDE_KEY, String.valueOf(location.getLongitude()));

            mMyLocation = location;
        }
    }


    @Override
    public void onMapClick(LatLng latLng) {
        moveCamera(latLng);
    }
}
