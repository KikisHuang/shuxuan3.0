package com.gxdingo.sg.biz;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.gxdingo.sg.bean.AddressBean;
import com.gxdingo.sg.bean.CategoriesBean;
import com.gxdingo.sg.bean.StoreDetail;
import com.gxdingo.sg.bean.StoreListBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientHomeContract {

    public interface ClientHomePresenter extends MvpPresenter<BasicsListener,ClientHomeListener>{

        void checkPermissions(RxPermissions rxPermissions);

        void getCategory();

        void getNearbyStore(boolean refresh,int categoryId);

        void getNearbyStore(AddressBean addressBean,int categoryId);

        void callStore(String s);

        void getSearchHistory();

        void search(boolean refresh,String content);
    }

    public interface ClientHomeListener{

        void setDistrict(String district);

        void onCategoryResult(List<CategoriesBean> categories);

        void onStoresResult(boolean refresh, List<StoreListBean.StoreBean> storeBeans);

        void onHistoryResult(List<String> searchHistories);
    }
}
