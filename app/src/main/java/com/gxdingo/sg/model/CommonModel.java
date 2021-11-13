package com.gxdingo.sg.model;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
//import com.kikis.commnlibrary.bean.AddressBean;
import com.kikis.commnlibrary.bean.AddressBean;
import com.gxdingo.sg.bean.UpLoadBean;
import com.gxdingo.sg.biz.GridPhotoListener;
import com.gxdingo.sg.biz.PermissionsListener;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.utils.GsonUtil;
import com.kikis.commnlibrary.utils.RxUtil;
import com.luck.picture.lib.entity.LocalMedia;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle3.LifecycleProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static com.blankj.utilcode.util.StringUtils.isEmpty;
import static com.blankj.utilcode.util.TimeUtils.getNowString;
import static com.blankj.utilcode.util.TimeUtils.isToday;
import static com.gxdingo.sg.utils.LocalConstant.ADD;
import static com.gxdingo.sg.utils.LocalConstant.ADDRESS_CACHE;
import static com.gxdingo.sg.utils.PhotoUtils.getPhotoUrl;
import static com.kikis.commnlibrary.utils.Constant.isDebug;

/**
 * @author: Kikis
 * @date: 2021/4/20
 * @page:常用方法的model类
 */
public class CommonModel {


    public void editTextFilters(EditText editText, int limit) {
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(limit)});
    }

    public void editTextLengthListener(EditText editText, TextWatcher textWatcher) {

        editText.addTextChangedListener(textWatcher);
    }

    /**
     * 检测权限
     *
     * @param rxPermissions
     */
    public void checkPermission(RxPermissions rxPermissions, String[] permissions, PermissionsListener listener) {
        rxPermissions.request(permissions)
                .subscribe(aBoolean -> {
                    if (listener != null)
                        listener.onNext(aBoolean);
                });

    }


    /**
     * 添加数据
     *
     * @param lifecycleProvider
     * @param oldData
     * @param upLoadBean
     * @param limit
     * @param listener
     */
    public void addPhotoData(LifecycleProvider lifecycleProvider, List<LocalMedia> oldData, UpLoadBean upLoadBean, int limit, GridPhotoListener listener) {
        RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {
            for (String str : upLoadBean.urls) {
                LocalMedia localMedia = new LocalMedia();
                localMedia.setPath(str);
                oldData.add(localMedia);
            }
            e.onNext(0);
            e.onComplete();
        }), lifecycleProvider).subscribe(o -> gridPhotodataManager(oldData, lifecycleProvider, limit, listener));
    }

    /**
     * 数据处理
     */
    private void gridPhotodataManager(List<LocalMedia> data, LifecycleProvider lifecycleProvider, int limit, GridPhotoListener listener) {

        RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getId() == ADD && i != data.size() - 1)
                    data.remove(i);
            }
            e.onNext(0);
            e.onComplete();
        }), lifecycleProvider).subscribe(o -> {
            if (data.size() < limit && data.get(data.size() - 1).getId() != ADD) {
                //添加图片
                LocalMedia localMedia = new LocalMedia();
                localMedia.setId(ADD);
                data.add(localMedia);
            }
            listener.onSucceed();
        });

    }


    /**
     * LocalMedia 转list<String> 集合
     *
     * @param lifecycleProvider
     * @param data
     * @param listener
     */
    public void localDataToStr(LifecycleProvider lifecycleProvider, List<LocalMedia> data, CustomResultListener listener) {

        RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {
            List<String> photos = new ArrayList<>();
            for (LocalMedia localMedia : data) {
                if (localMedia.getId() != ADD)
                    photos.add(getPhotoUrl(localMedia));
            }
            e.onNext(photos);
            e.onComplete();
        }), lifecycleProvider).subscribe(o -> listener.onResult(o));
    }

    /**
     * list<String>转 LocalMedia 集合
     *
     * @param lifecycleProvider
     * @param data
     * @param listener
     */
    public void strToLocalData(LifecycleProvider lifecycleProvider, List<LocalMedia> oldData, List<String> data, int limit, GridPhotoListener listener) {
        RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {
            for (String str : data) {
                LocalMedia localMedia = new LocalMedia();
                localMedia.setPath(str);
                oldData.add(localMedia);
            }
            e.onNext(0);
            e.onComplete();
        }), lifecycleProvider).subscribe(o -> gridPhotodataManager(oldData, lifecycleProvider, limit, listener));
    }

    /**
     * 跳转拨号页面
     *
     * @param phonenum
     */
    public void goCallPage(Context context, String phonenum) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phonenum));
        context.startActivity(intent);
    }

    /**
     * 获取缓存默认地址
     */
    public AddressBean getCacheDefaultAddress() {

        String json = SPUtils.getInstance().getString(ADDRESS_CACHE);
        if (isDebug)
            LogUtils.i("CommonModel DefaultAddress Cache === " + json);
        if (isEmpty(json))
            return null;

        AddressBean addressBean = GsonUtil.GsonToBean(json, AddressBean.class);
        //不是同一天
        if (!isToday(addressBean.defaultCacheTime))
            return null;

        return addressBean;
    }


    /**
     * 缓存默认地址
     */
    public void cacheDefaultAddress(AddressBean addressBean) {

        if (addressBean.getLatitude() == 0 || addressBean.getLongitude() == 0) {
            LogUtils.i("缓存地址信息的经纬度有误，取消缓存");
            return;
        }

        if (isDebug)
            LogUtils.i("CommonModel Cache DefaultAddress" + GsonUtil.gsonToStr(addressBean));
        addressBean.defaultCacheTime = getNowString();

        SPUtils.getInstance().put(ADDRESS_CACHE, GsonUtil.gsonToStr(addressBean));
    }

    /**
     * 清除缓存默认地址
     */
    public void clearCacheDefaultAddress() {

        SPUtils.getInstance().put(ADDRESS_CACHE, "");
    }

    /**
     * 手机是否开启位置服务，如果没有开启那么所有app将不能使用定位功能
     */
    public static boolean isLocServiceEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }
}
