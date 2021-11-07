package com.gxdingo.sg.presenter;

import com.gxdingo.sg.bean.SubscribesListBean;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.StoreHomeContract;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.model.WebSocketModel;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.zhouyou.http.subsciber.BaseSubscriber;

/**
 * 商家主页控制器
 *
 * @author JM
 */
public class StoreHomePresenter extends BaseMvpPresenter<BasicsListener, StoreHomeContract.StoreHomeListener>
        implements StoreHomeContract.StoreHomePresenter, NetWorkListener {

    private NetworkModel networkModel;
    private WebSocketModel mWebSocketModel;

    public StoreHomePresenter() {
        networkModel = new NetworkModel(this);
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
            getV().onIMSubscribesInfo(refresh, subscribesListBean);
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

    }

    /**
     * 获取IM订阅信息列表
     */
    @Override
    public void getIMSubscribesList(boolean refresh) {
        if (networkModel != null) {
            mWebSocketModel.getMessageSubscribesList(getContext(), refresh);
        }
    }

    /**
     * 截取营业时间
     *
     * @param time 时间  2021-05-08T06:25:47.117+00:00
     * @return
     */
    @Override
    public String onInterceptionBusinessHours(String time) {
        String hm = "";//时分
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SS");
//        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        try {
//            Date date = sdf.parse(time);
//            String t = sdf.format(date);
//            t = t.replace("T", " ");
//            t = sdf2.format(sdf2.parse(t));
//            String[] tArray = t.split(" ");
//            hm = tArray[1];
//        } catch (ParseException e) {
//        }
        return hm;
    }
}
