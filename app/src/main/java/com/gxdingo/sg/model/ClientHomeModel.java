package com.gxdingo.sg.model;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.gxdingo.sg.R;

import static com.amap.api.maps.model.MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER;
import static com.gxdingo.sg.utils.ClientLocalConstant.LOCATION_LATITUDE_KEY;
import static com.gxdingo.sg.utils.ClientLocalConstant.LOCATION_LONGITUDE_KEY;

/**
 * @author: Weaving
 * @date: 2021/10/20
 * @page:
 */
public class ClientHomeModel implements AMap.OnMyLocationChangeListener{
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;


    private Context mContext;

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

    /**
     * 初始化地图控件
     *
     * @param aMap
     * @param latitude
     * @param longitude
     */
    public void mapInit(AMap aMap, double latitude, double longitude) {

        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。

        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_gps_location_marker));

        myLocationStyle.strokeWidth(0);

        myLocationStyle.strokeColor(Color.TRANSPARENT);//设置定位蓝点精度圆圈的边框颜色的方法。

        myLocationStyle.radiusFillColor(Color.TRANSPARENT);//设置定位蓝点精度圆圈的填充颜色的方法。

        myLocationStyle.myLocationType(LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);

        myLocationStyle.interval(15000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。

        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style

        int y = (int) (ScreenUtils.getScreenHeight() / 6.5);

        int x = ScreenUtils.getAppScreenWidth() / 2;

        //默认中心点
        aMap.setPointToCenter(x, y);


        double lat = Double.parseDouble(SPUtils.getInstance().getString(LOCATION_LATITUDE_KEY, "22.812972"));

        double lon = Double.parseDouble(SPUtils.getInstance().getString(LOCATION_LONGITUDE_KEY, "108.372339"));

        //没有商家定位，以个人定位为中心点
        if (latitude == 0 && longitude == 0)
            //默认位置
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 15));
        else
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));

        //设置希望展示的地图缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。

        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setOnMyLocationChangeListener(this);
        // myLocationStyle.myLocationIcon();//设置定位蓝点的icon图标方法，需要用到BitmapDescriptor类对象作为参数。
    }

    @Override
    public void onMyLocationChange(Location location) {
        if (location != null && location.getLongitude() > 0 && location.getLatitude() > 0) {
            SPUtils.getInstance().put(LOCATION_LATITUDE_KEY, String.valueOf(location.getLatitude()));
            SPUtils.getInstance().put(LOCATION_LONGITUDE_KEY, String.valueOf(location.getLongitude()));
        }

    }
}
