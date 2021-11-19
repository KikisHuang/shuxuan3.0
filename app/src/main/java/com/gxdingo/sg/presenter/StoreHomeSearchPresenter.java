package com.gxdingo.sg.presenter;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.StoreHomeSearchContract;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.model.StoreNetworkModel;
import com.gxdingo.sg.model.WebSocketModel;
import com.kikis.commnlibrary.bean.SubscribesListBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.kikis.commnlibrary.utils.GsonUtil;
import com.zhouyou.http.subsciber.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

import static com.blankj.utilcode.util.StringUtils.isEmpty;

public class StoreHomeSearchPresenter extends BaseMvpPresenter<BasicsListener, StoreHomeSearchContract.StoreHomeSearchListener>
        implements StoreHomeSearchContract.StoreHomeSearchPresenter, NetWorkListener {

    private WebSocketModel mWebSocketModel;

    private List<String> searchHistory;

    public StoreHomeSearchPresenter() {
        mWebSocketModel = new WebSocketModel(this);
    }

    @Override
    public void onSucceed(int type) {

    }

    @Override
    public void onMessage(String msg) {
        if (isBViewAttached())
            getBV().onMessage(msg);
    }

    @Override
    public void noData() {
        if (isBViewAttached())
            getBV().noData();
    }

    @Override
    public void onData(boolean refresh, Object o) {
        if (o instanceof SubscribesListBean) {
            //消息订阅回调
            SubscribesListBean subscribesListBean = (SubscribesListBean) o;
            getV().onSearchResult(refresh, subscribesListBean.getList());
        }
    }

    @Override
    public void haveData() {
        if (isBViewAttached())
            getBV().haveData();
    }

    @Override
    public void finishLoadmoreWithNoMoreData() {
        if (isBViewAttached())
            getBV().finishLoadmoreWithNoMoreData();
    }

    @Override
    public void finishRefreshWithNoMoreData() {
        if (isBViewAttached())
            getBV().finishRefreshWithNoMoreData();
    }

    @Override
    public void onRequestComplete() {
        if (isBViewAttached())
            getBV().onRequestComplete();
    }

    @Override
    public void resetNoMoreData() {
        if (isBViewAttached())
            getBV().resetNoMoreData();
    }

    @Override
    public void finishRefresh(boolean success) {
        if (isBViewAttached())
            getBV().finishRefresh(success);
    }

    @Override
    public void finishLoadmore(boolean success) {
        if (isBViewAttached())
            getBV().finishLoadmore(success);
    }

    @Override
    public void onAfters() {
        if (isBViewAttached())
            getBV().onAfters();
    }

    @Override
    public void onStarts() {
        if (isBViewAttached())
            getBV().onStarts();
    }

    @Override
    public void onDisposable(BaseSubscriber subscriber) {
        addDisposable(subscriber);
    }

    /**
     * 获取搜索历史
     */
    @Override
    public void getSearchHistory() {
        if (isViewAttached())
            try {
                String details = SPUtils.getInstance().getString("STORE_SEARCH_HISTORY", "");
                getV().onHistoryResult(GsonUtil.jsonToList(details, String.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    /**
     * 搜索
     *
     * @param b
     * @param content
     */
    @Override
    public void search(boolean b, String content) {
        saveSearch(content);
        if (mWebSocketModel != null)
            mWebSocketModel.getMessageSubscribesList(getContext(), b, content);

    }

    /**
     * 保存成历史搜索
     */
    public void saveSearch(String keyword) {
        if (!TextUtils.isEmpty(keyword)) {
            if (searchHistory == null) {
                searchHistory = getSearch();
            }
            if (searchHistory != null) {
                if (searchHistory.size() > 0) {
                    searchHistory.remove(keyword);
                }
                searchHistory.add(0, keyword);
                if (searchHistory.size() > 10) {
                    searchHistory.remove(searchHistory.size() - 1);
                }
            }
            SPUtils.getInstance().put("STORE_SEARCH_HISTORY", GsonUtil.gsonToStr(searchHistory));
        }
    }


    public List<String> getSearch() {
        try {
            String details = SPUtils.getInstance().getString("STORE_SEARCH_HISTORY", "");
            if (!isEmpty(details)) {
                return GsonUtil.jsonToList(details, String.class);
            } else {
                return new ArrayList<String>();
            }
        } catch (Exception e) {
            return new ArrayList<String>();
        }
    }
}
