package com.gxdingo.sg.model;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.reflect.TypeToken;
import com.gxdingo.sg.bean.IMChatHistoryListBean;
import com.gxdingo.sg.bean.ReceiveIMMessageBean;
import com.gxdingo.sg.bean.SendIMMessageBean;
import com.gxdingo.sg.bean.SubscribesBean;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.http.HttpClient;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.view.MyBaseSubscriber;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.utils.GsonUtil;
import com.zhouyou.http.callback.CallClazzProxy;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.ApiResult;
import com.zhouyou.http.request.PostRequest;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

import static com.gxdingo.sg.http.Api.GET_CHAT_HISTORY_LIST;
import static com.gxdingo.sg.http.Api.IM_URL;
import static com.gxdingo.sg.http.Api.MESSAGE_SEND;
import static com.gxdingo.sg.http.Api.MESSAGE_SUBSCRIBES;
import static com.kikis.commnlibrary.utils.GsonUtil.getJsonMap;

/**
 * WebSocketModel
 *
 * @author: JM
 */
public class WebSocketModel {

    private NetWorkListener netWorkListener;
    private int mPage = 1;
    private int mPageSize = 15;

    public WebSocketModel(NetWorkListener netWorkListener) {
        this.netWorkListener = netWorkListener;
    }

    /**
     * 翻页
     */
    public void nextPage() {
        mPage++;
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

        //刷新
        if (refresh) {
            /**
             * 控制列表下拉刷新和上拉加载更多视图
             */
            if (size < mPageSize)
                netWorkListener.finishRefreshWithNoMoreData();//完成刷新并标记没有更多数据
            else {
                netWorkListener.resetNoMoreData();//重置没有更多数据
                resetPage();//重置页码
            }

            /**
             * 控制是否有数据显示的视图
             */
            if (size <= 0)
                netWorkListener.noData();//没有数据（显示传入的没有数据布局）
            else {
                nextPage();//有数据，页码累加1
                netWorkListener.haveData();//有数据（隐藏没有数据布局）
            }

            netWorkListener.finishRefresh(true);//完成刷新
        }
        //加载更多
        else {
            //请求的长度小于0，显示没有更多数据布局
            if (size < mPageSize)
                netWorkListener.finishLoadmoreWithNoMoreData();//完成加载并标记没有更多数据
            else {
                nextPage();//有数据，页码累加1
                netWorkListener.haveData();//有数据
            }


            netWorkListener.finishLoadmore(true);//完成加载更多
        }
        netWorkListener.onRequestComplete();//完成请求
    }

    /**
     * 获取消息订阅列表
     *
     * @param context
     */
    public void getMessageSubscribesList(Context context, boolean refresh) {
        if (refresh)
            resetPage();//重置页码

        Map<String, String> map = getJsonMap();

        //map.put(Constant.SEARCH_CONTENT, "");
        map.put(Constant.CURRENT, String.valueOf(mPage));
        map.put(Constant.SIZE, String.valueOf(mPageSize));

        if (netWorkListener != null)
            netWorkListener.onStarts();

        PostRequest request = HttpClient.imPost(IM_URL + MESSAGE_SUBSCRIBES, map);
        request.headers(LocalConstant.CROSSTOKEN, UserInfoUtils.getInstance().getUserInfo().getCrossToken());
        Observable<SubscribesBean> observable = request
                .execute(new CallClazzProxy<ApiResult<SubscribesBean>, SubscribesBean>(new TypeToken<SubscribesBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<SubscribesBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);

                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
                    netWorkListener.onAfters();
                }
            }

            @Override
            public void onNext(SubscribesBean subscribesBean) {

                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onData(refresh, subscribesBean);
                }
                if (subscribesBean != null && subscribesBean.getList() != null) {
                    //下一页
                    pageNext(refresh, subscribesBean.getList().size());
                }
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }

    /**
     * 向服务器发送聊天消息
     */
    public void sendMessage(Context context, SendIMMessageBean sendIMMessageBean) {
        if (netWorkListener != null) {
            netWorkListener.onStarts();
        }
        Map<String, String> map = new HashMap<>();
        map.put("shareUuid", sendIMMessageBean.getShareUuid());
        map.put("type", String.valueOf(sendIMMessageBean.getType()));
        map.put("content", sendIMMessageBean.getContent());
        map.put("voiceDuration", String.valueOf(sendIMMessageBean.getVoiceDuration()));
        map.put("params", GsonUtil.gsonToStr(sendIMMessageBean.getParams()));

        PostRequest request = HttpClient.imPost(IM_URL + MESSAGE_SEND, map);
        request.headers(LocalConstant.CROSSTOKEN, UserInfoUtils.getInstance().getUserInfo().getCrossToken());
        Observable<ReceiveIMMessageBean> observable = request
                .execute(new CallClazzProxy<ApiResult<ReceiveIMMessageBean>, ReceiveIMMessageBean>(new TypeToken<ReceiveIMMessageBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<ReceiveIMMessageBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
                    netWorkListener.onAfters();
                }

            }

            @Override
            public void onNext(ReceiveIMMessageBean receiveIMMessageBean) {
                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                }
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * 获取聊天记录列表
     */
    public void getChatHistoryList(Context context, String shareUuid, CustomResultListener customResultListener) {
        if (netWorkListener != null) {
            netWorkListener.onStarts();
        }

        Map<String, String> map = new HashMap<>();
        map.put("shareUuid", shareUuid);
        map.put("current", String.valueOf(mPage));
        map.put("size", String.valueOf(mPageSize));

        PostRequest request = HttpClient.imPost(IM_URL + GET_CHAT_HISTORY_LIST, map);
        request.headers(LocalConstant.CROSSTOKEN, UserInfoUtils.getInstance().getUserInfo().getCrossToken());
        Observable<IMChatHistoryListBean> observable = request
                .execute(new CallClazzProxy<ApiResult<IMChatHistoryListBean>, IMChatHistoryListBean>(new TypeToken<IMChatHistoryListBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<IMChatHistoryListBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
                    netWorkListener.onAfters();
                }
            }

            @Override
            public void onNext(IMChatHistoryListBean imChatHistoryListBean) {
                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                }
                if (customResultListener != null) {
                    customResultListener.onResult(imChatHistoryListBean);
                }
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }
}
