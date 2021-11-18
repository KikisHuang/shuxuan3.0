package com.gxdingo.sg.biz;

import android.content.Context;

import com.kikis.commnlibrary.bean.AddressBean;
import com.gxdingo.sg.bean.CategoriesBean;
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

        void checkPermissions(RxPermissions rxPermissions,boolean search);

        void getCategory();

        void getNearbyStore(boolean refresh,boolean search,int categoryId);

        void getNearbyStore(AddressBean addressBean,int categoryId);

        void callStore(String s);

        void convertStore();

        void getSearchHistory();

        void search(boolean refresh,boolean search,String content);

        void search(AddressBean addressBean,String content);

        void getSettleImage();

        void oauth(Context context);
    }

    public interface ClientHomeListener{

        void setDistrict(String district);

        void onCategoryResult(List<CategoriesBean> categories);

        void onStoresResult(boolean refresh,boolean search, List<StoreListBean.StoreBean> storeBeans);

        void onHistoryResult(List<String> searchHistories);
    }
}
