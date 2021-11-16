package com.gxdingo.sg.biz;

import com.kikis.commnlibrary.bean.SubscribesListBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * 商家首页搜索契约类
 *
 * @author JM
 */
public class StoreHomeSearchContract {
    public interface StoreHomeSearchPresenter extends MvpPresenter<BasicsListener, StoreHomeSearchListener> {

        void getSearchHistory();

        void search(boolean b, String toString);
    }

    public interface StoreHomeSearchListener {

        void onHistoryResult(List<String> gsonToList);

        void onSearchResult(boolean refresh, ArrayList<SubscribesListBean.SubscribesMessage> list);
    }
}
