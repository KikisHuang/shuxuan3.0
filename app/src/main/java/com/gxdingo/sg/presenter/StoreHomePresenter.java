package com.gxdingo.sg.presenter;

import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.biz.StoreHomeContract;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.zhouyou.http.subsciber.BaseSubscriber;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 商家主页控制器
 *
 * @author JM
 */
public class StoreHomePresenter extends BaseMvpPresenter<BasicsListener, StoreHomeContract.StoreHomeListener>
        implements StoreHomeContract.StoreHomePresenter, NetWorkListener {


    @Override
    public void onSucceed(int type) {

    }

    @Override
    public void onMessage(String msg) {

    }

    @Override
    public void noData() {

    }

    @Override
    public void onData(boolean refresh, Object o) {

    }

    @Override
    public void haveData() {

    }

    @Override
    public void finishLoadmoreWithNoMoreData() {

    }

    @Override
    public void finishRefreshWithNoMoreData() {

    }

    @Override
    public void onRequestComplete() {

    }

    @Override
    public void resetNoMoreData() {

    }

    @Override
    public void finishRefresh(boolean success) {

    }

    @Override
    public void finishLoadmore(boolean success) {

    }

    @Override
    public void onAfters() {

    }

    @Override
    public void onStarts() {

    }

    @Override
    public void onDisposable(BaseSubscriber subscriber) {

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SS");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = sdf.parse(time);
            String t = sdf.format(date);
            t = t.replace("T", " ");
            t = sdf2.format(sdf2.parse(t));
            String[] tArray = t.split(" ");
            hm = tArray[1];
        } catch (ParseException e) {
        }
        return hm;
    }
}
