package com.gxdingo.sg.model;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.reflect.TypeToken;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.NormalBean;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.http.HttpClient;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.StoreLocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.view.MyBaseSubscriber;
import com.kikis.commnlibrary.utils.Constant;
import com.zhouyou.http.callback.CallClazzProxy;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.ApiResult;

import java.util.Map;

import io.reactivex.Observable;

import static android.text.TextUtils.isEmpty;
import static com.gxdingo.sg.http.ClientApi.USER_MOBILE_CHANGE;
import static com.gxdingo.sg.utils.LocalConstant.LOGIN_WAY;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.GsonUtil.getJsonMap;

/**
 * @author: Weaving
 * @date: 2021/10/15
 * @page:
 */
public class ClientNetworkModel {

    protected int mPage = 1;

    protected int mPageSize = 15;

    public int getPage() {
        return mPage;
    }

    private NetWorkListener netWorkListener;

    /**
     * 翻页
     */
    public void nextPage() {
        mPage++;
    }


    public ClientNetworkModel(NetWorkListener netWorkListener) {
        this.netWorkListener = netWorkListener;
    }

    /**
     * 重置
     */
    public void resetPage() {
        if (mPage > 1)
            setPage(1);
    }

    protected void setPage(int mPage) {
        this.mPage = mPage;
    }

    protected int getSize() {
        return mPageSize;
    }

    protected void setSize(int mSize) {
        this.mPageSize = mSize;
    }

    /**
     * 页数自动计算
     *
     * @param refresh
     * @param size
     */
    public void pageNext(boolean refresh, int size) {
        if (netWorkListener == null)
            return;

        if (refresh) {

            if (size < mPageSize)
                netWorkListener.finishRefreshWithNoMoreData();
            else {
                netWorkListener.resetNoMoreData();
                resetPage();
            }

            if (size <= 0)
                netWorkListener.noData();
            else {
                nextPage();
                netWorkListener.haveData();
            }

            netWorkListener.finishRefresh(true);
        } else {
            //请求的长度小于0，显示没有更多数据布局
            if (size < mPageSize)
                netWorkListener.finishLoadmoreWithNoMoreData();
            else {
                nextPage();
                netWorkListener.haveData();
            }


            netWorkListener.finishLoadmore(true);
        }
        netWorkListener.onRequestComplete();
    }

    /**
     * 页面重置
     *
     * @param refresh
     * @param msg
     */
    public void pageReset(boolean refresh, String msg) {

        if (netWorkListener == null)
            return;

        if (refresh) {
            resetPage();
            netWorkListener.resetNoMoreData();
            netWorkListener.finishRefresh(false);
        } else
            netWorkListener.finishLoadmore(false);

        if (!isEmpty(msg))
            netWorkListener.onMessage(msg);

        netWorkListener.onRequestComplete();
    }


    /**
     * 绑定新手机
     *
     * @param context
     * @param mobile
     * @param code
     */
    public void bindNewPhone(Context context, String mobile, String code) {

        if (isEmpty(mobile)) {
            netWorkListener.onMessage(gets(R.string.phone_number_can_not_null));
            return;
        } else if (isEmpty(code)) {
            netWorkListener.onMessage(gets(R.string.verification_code_can_not_null));
            return;
        }

        if (netWorkListener != null)
            netWorkListener.onStarts();

        boolean isUser = SPUtils.getInstance().getBoolean(LOGIN_WAY);

        Map<String, String> map = getJsonMap();

        if (isUser) {
            map.put(Constant.MOBILE, mobile);
        } else {
//            map.put(StoreLocalConstant.OLD_MOBILE, UserInfoUtils.getInstance().getUserPhone());
//            map.put(StoreLocalConstant.NEW_MOBILE, mobile);
        }
        map.put(Constant.CODE, code);


        Observable<NormalBean> observable = HttpClient.post(isUser ? USER_MOBILE_CHANGE : "USER_UPDATE_MOBILE", map)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onMessage(e.getMessage());
                }
            }

            @Override
            public void onNext(NormalBean normalBean) {

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onMessage(gets(R.string.bind_succeed));
                    netWorkListener.onSucceed(LocalConstant.BIND_NEW_PHONE);
                }

            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);

    }
}
