package com.gxdingo.sg.biz;

import com.gxdingo.sg.bean.StoreListBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

import java.util.List;

/**
 * @author: Weaving
 * @date: 2021/10/26
 * @page:
 */
public class ClientSearchContract {

    public interface ClientSearchPresenter extends MvpPresenter<BasicsListener,ClientSearchListener>{
        void search();
    }

    public interface ClientSearchListener {

        String getSearchContent();

        void onStoresResult(List<StoreListBean.StoreBean> storeBeans);
    }
}
