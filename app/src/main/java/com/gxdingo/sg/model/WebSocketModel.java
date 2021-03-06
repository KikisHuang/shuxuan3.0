package com.gxdingo.sg.model;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.reflect.TypeToken;
import com.gxdingo.sg.bean.BaseTransferResult;
import com.gxdingo.sg.bean.ClientCouponBean;
import com.gxdingo.sg.bean.IMChatHistoryListBean;
import com.gxdingo.sg.bean.NormalBean;
import com.gxdingo.sg.bean.PayBean;
import com.gxdingo.sg.bean.TransferBean;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.gxdingo.sg.bean.SendIMMessageBean;
import com.kikis.commnlibrary.bean.SubscribesListBean;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.http.HttpClient;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.view.MyBaseSubscriber;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.utils.BaseLogUtils;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.utils.GsonUtil;
import com.zhouyou.http.callback.CallClazzProxy;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.ApiResult;
import com.zhouyou.http.request.PostRequest;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

import static com.blankj.utilcode.util.StringUtils.isEmpty;
import static com.gxdingo.sg.http.Api.CHAT_SETTOP;
import static com.gxdingo.sg.http.Api.GET_CHAT_HISTORY_LIST;
import static com.gxdingo.sg.http.Api.GET_TRANSFER;
import static com.gxdingo.sg.http.Api.IM_URL;
import static com.gxdingo.sg.http.Api.MESSAGE_CLEAR_ALL;
import static com.gxdingo.sg.http.Api.MESSAGE_DELETE;
import static com.gxdingo.sg.http.Api.MESSAGE_READ;
import static com.gxdingo.sg.http.Api.MESSAGE_SEND;
import static com.gxdingo.sg.http.Api.MESSAGE_SUBSCRIBES;
import static com.gxdingo.sg.http.Api.MESSAGE_WITHDRAW;
import static com.gxdingo.sg.http.Api.SUBSCRIBE_DELETE;
import static com.gxdingo.sg.http.Api.SUM_UNREAD;
import static com.gxdingo.sg.http.Api.TRANSFER;
import static com.gxdingo.sg.utils.RSAEncrypt.decryptByPrivateKey;
import static com.gxdingo.sg.utils.RSAUtils.getPrivateKeyPath;
import static com.kikis.commnlibrary.utils.BadgerManger.resetBadger;
import static com.kikis.commnlibrary.utils.GsonUtil.getJsonMap;

/**
 * WebSocketModel
 *
 * @author: JM
 */
public class WebSocketModel {

    private NetWorkListener netWorkListener;

    private int mPage = 1;

    private int mPageSize = 10;

    public WebSocketModel(NetWorkListener netWorkListener) {
        this.netWorkListener = netWorkListener;
    }

    /**
     * ??????
     */
    public void nextPage() {
        mPage++;
    }

    /**
     * ??????
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
     * ??????????????????
     *
     * @param refresh
     * @param size
     */
    public void pageNext(boolean refresh, int size) {
        if (netWorkListener == null)
            return;

        //??????
        if (refresh) {
            /**
             * ???????????????????????????????????????????????????
             */
            if (size < mPageSize)
                netWorkListener.finishRefreshWithNoMoreData();//???????????????????????????????????????
            else {
                netWorkListener.resetNoMoreData();//????????????????????????
                resetPage();//????????????
            }

            /**
             * ????????????????????????????????????
             */
            if (size <= 0)
                netWorkListener.noData();//???????????????????????????????????????????????????
            else {
                nextPage();//????????????????????????1
                netWorkListener.haveData();//???????????????????????????????????????
            }

            netWorkListener.finishRefresh(true);//????????????
        }
        //????????????
        else {
            //?????????????????????0?????????????????????????????????
            if (size < mPageSize)
                netWorkListener.finishLoadmoreWithNoMoreData();//???????????????????????????????????????
            else {
                nextPage();//????????????????????????1
                netWorkListener.haveData();//?????????
            }

            netWorkListener.finishLoadmore(true);//??????????????????
        }
        netWorkListener.onRequestComplete();//????????????
    }

    /**
     * ????????????????????????
     *
     * @param context
     * @param content
     */
    public void getMessageSubscribesList(Context context, boolean refresh, String content) {
        if (!UserInfoUtils.getInstance().isLogin())
            return;

        if (refresh)
            resetPage();//????????????

        Map<String, String> map = getJsonMap();

        //map.put(Constant.SEARCH_CONTENT, "");
        map.put(Constant.CURRENT, String.valueOf(mPage));
        map.put(Constant.SIZE, String.valueOf(mPageSize));

        if (!isEmpty(content))
            map.put("searchContent", content);


//        if (netWorkListener != null)
//            netWorkListener.onStarts();

        PostRequest request = HttpClient.imPost(IM_URL + MESSAGE_SUBSCRIBES, map);

        if (UserInfoUtils.getInstance().isLogin() && !isEmpty(UserInfoUtils.getInstance().getUserInfo().getCrossToken()))
            request.headers(LocalConstant.CROSSTOKEN, UserInfoUtils.getInstance().getUserInfo().getCrossToken());

        Observable<SubscribesListBean> observable = request
                .execute(new CallClazzProxy<ApiResult<SubscribesListBean>, SubscribesListBean>(new TypeToken<SubscribesListBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<SubscribesListBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);

                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
                    //netWorkListener.onAfters();
                }
            }

            @Override
            public void onNext(SubscribesListBean subscribesBean) {
                if (netWorkListener != null) {
                    //netWorkListener.onAfters();
                    netWorkListener.onData(refresh, subscribesBean);
                }
                if (subscribesBean != null && subscribesBean.getList() != null) {
                    //?????????
                    pageNext(refresh, subscribesBean.getList().size());
                }
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }


    /**
     * ?????????????????????????????????
     *
     * @param context
     */
    public void refreshMessageList(Context context) {

        Map<String, String> map = getJsonMap();

        //map.put(Constant.SEARCH_CONTENT, "");
        map.put(Constant.CURRENT, String.valueOf(1));
        map.put(Constant.SIZE, String.valueOf(mPage * mPageSize));

//        if (netWorkListener != null)
//            netWorkListener.onStarts();

        PostRequest request = HttpClient.imPost(IM_URL + MESSAGE_SUBSCRIBES, map);
        if (UserInfoUtils.getInstance().isLogin() && !isEmpty(UserInfoUtils.getInstance().getUserInfo().getCrossToken()))
            request.headers(LocalConstant.CROSSTOKEN, UserInfoUtils.getInstance().getUserInfo().getCrossToken());
        Observable<SubscribesListBean> observable = request
                .execute(new CallClazzProxy<ApiResult<SubscribesListBean>, SubscribesListBean>(new TypeToken<SubscribesListBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<SubscribesListBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);

                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
                    //netWorkListener.onAfters();
                }
            }

            @Override
            public void onNext(SubscribesListBean subscribesBean) {
                if (netWorkListener != null) {
                    //netWorkListener.onAfters();
                    netWorkListener.onData(true, subscribesBean);

                    if (subscribesBean != null && subscribesBean.getList() != null && subscribesBean.getList().size() > 0)
                        netWorkListener.haveData();
                }
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }


    /**
     * ?????????????????????
     *
     * @param context
     */
    public void getUnreadMessageNumber(Context context, CustomResultListener customResultListener) {

        Map<String, String> map = getJsonMap();

        PostRequest request = HttpClient.imPost(IM_URL + SUM_UNREAD, map);

        if (UserInfoUtils.getInstance().isLogin() && !isEmpty(UserInfoUtils.getInstance().getUserInfo().getCrossToken()))
            request.headers(LocalConstant.CROSSTOKEN, UserInfoUtils.getInstance().getUserInfo().getCrossToken());

        Observable<NormalBean> observable = request
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);
            }

            @Override
            public void onNext(NormalBean normalBean) {
                if (customResultListener != null)
                    customResultListener.onResult(normalBean.unread);

            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }

    /**
     * ??????????????????????????????
     */
    public void sendMessage(Context context, SendIMMessageBean sendIMMessageBean, CustomResultListener<ReceiveIMMessageBean> listener) {
        Map<String, String> map = new HashMap<>();
        map.put("shareUuid", sendIMMessageBean.getShareUuid());
        map.put("type", String.valueOf(sendIMMessageBean.getType()));
        if (!isEmpty(sendIMMessageBean.getContent()))
            map.put("content", sendIMMessageBean.getContent());
        if (sendIMMessageBean.getVoiceDuration() > 0)
            map.put("voiceDuration", String.valueOf(sendIMMessageBean.getVoiceDuration()));

        if (sendIMMessageBean.getParams() != null)
            map.put("params", GsonUtil.gsonToStr(sendIMMessageBean.getParams()));
/*
        if (netWorkListener != null) {
            netWorkListener.onStarts();
        }*/

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
                BaseLogUtils.e(e);
                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
//                    netWorkListener.onAfters();
                }

            }

            @Override
            public void onNext(ReceiveIMMessageBean receiveIMMessageBean) {
//                if (netWorkListener != null) {
//                    netWorkListener.onAfters();
//                }
                if (listener != null) {
                    listener.onResult(receiveIMMessageBean);
                }
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ????????????????????????
     */
    public void getChatHistoryList(Context context, String shareUuid, String otherId, int otherRole, CustomResultListener customResultListener) {
        Map<String, String> map = new HashMap<>();

        if (!isEmpty(shareUuid))
            map.put("shareUuid", shareUuid);
        else {
            map.put("otherId", String.valueOf(otherId));
//            map.put("otherRole", String.valueOf(otherRole));
        }

        map.put("current", String.valueOf(mPage));
        map.put("size", String.valueOf(mPageSize));

        PostRequest request = HttpClient.imPost(IM_URL + GET_CHAT_HISTORY_LIST, map);
        if (UserInfoUtils.getInstance().isLogin())
            request.headers(LocalConstant.CROSSTOKEN, UserInfoUtils.getInstance().getUserInfo().getCrossToken());

        Observable<IMChatHistoryListBean> observable = request
                .execute(new CallClazzProxy<ApiResult<IMChatHistoryListBean>, IMChatHistoryListBean>(new TypeToken<IMChatHistoryListBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<IMChatHistoryListBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);
                if (netWorkListener != null) {
                    netWorkListener.onMessage(e.getMessage());
                    netWorkListener.onAfters();
                }
                if (customResultListener != null) {
                    customResultListener.onResult(null);
                }
                netWorkListener.finishRefresh(true);//????????????
            }

            @Override
            public void onNext(IMChatHistoryListBean imChatHistoryListBean) {
                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                }
                if (customResultListener != null) {
                    customResultListener.onResult(imChatHistoryListBean);
                }
                if (imChatHistoryListBean != null && imChatHistoryListBean.getList() != null) {
                    //?????????
                    pageNext(false, imChatHistoryListBean.getList().size());
                }
                netWorkListener.finishRefresh(true);//????????????
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }


    /**
     * ????????????????????????
     */
    public void refreshChatHistoryList(Context context, String shareUuid, String otherId, int otherRole, CustomResultListener customResultListener) {

        if (!UserInfoUtils.getInstance().isLogin() || UserInfoUtils.getInstance().getUserInfo() == null || isEmpty(UserInfoUtils.getInstance().getUserInfo().getCrossToken()))
            return;

        Map<String, String> map = new HashMap<>();

        if (!isEmpty(shareUuid))
            map.put("shareUuid", shareUuid);
        else {
            map.put("otherId", String.valueOf(otherId));
//            map.put("otherRole", String.valueOf(otherRole));
        }

        map.put("current", String.valueOf(1));
        map.put("size", String.valueOf(mPageSize));
/*
        if (netWorkListener != null) {
            netWorkListener.onStarts();
        }*/

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
                BaseLogUtils.e(e);
            }

            @Override
            public void onNext(IMChatHistoryListBean imChatHistoryListBean) {
                if (customResultListener != null) {
                    customResultListener.onResult(imChatHistoryListBean);
                }
                netWorkListener.finishRefresh(true);//????????????
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ??????????????????
     */
    public void clearUnreadMessage(Context context, String shareUuid, CustomResultListener customResultListener) {
        Map<String, String> map = new HashMap<>();

        map.put("shareUuid", shareUuid);

        PostRequest request = HttpClient.imPost(IM_URL + MESSAGE_CLEAR_ALL, map);
        request.headers(LocalConstant.CROSSTOKEN, UserInfoUtils.getInstance().getUserInfo().getCrossToken());
        Observable<NormalBean> observable = request
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);
            }

            @Override
            public void onNext(NormalBean normalBean) {
                if (customResultListener != null) {
                    customResultListener.onResult(shareUuid);
                }
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ????????????
     */
    public void messageRead(Context context, long id, CustomResultListener customResultListener) {
        Map<String, String> map = new HashMap<>();

        map.put("contentId", String.valueOf(id));

        PostRequest request = HttpClient.imPost(IM_URL + MESSAGE_READ, map);
        request.headers(LocalConstant.CROSSTOKEN, UserInfoUtils.getInstance().getUserInfo().getCrossToken());
        Observable<NormalBean> observable = request
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);
            }

            @Override
            public void onNext(NormalBean normalBean) {
                if (customResultListener != null) {
                    customResultListener.onResult(0);
                }
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ????????????
     */
    public void transfer(Context context, String shareuuid, int type, String pass, String amount, ClientCouponBean couponBean, String mTotalAmount) {

        if (netWorkListener != null)
            netWorkListener.onStarts();

        Map<String, String> map = new HashMap<>();

        if (type == 30 && !isEmpty(pass))
            map.put("withdrawalPassword", pass);

        map.put("shareUuid", String.valueOf(shareuuid));

        map.put("payType", String.valueOf(type));

        map.put("amount", String.valueOf(amount));

        map.put("totalAmount", String.valueOf(mTotalAmount));

        if (couponBean != null && !isEmpty(couponBean.getCouponIdentifier()))
            map.put("couponIdentifier", String.valueOf(couponBean.getCouponIdentifier()));

        PostRequest request = HttpClient.imPost(IM_URL + TRANSFER, map);
        request.headers(LocalConstant.CROSSTOKEN, UserInfoUtils.getInstance().getUserInfo().getCrossToken());
        Observable<String> observable = request
                .execute(new CallClazzProxy<BaseTransferResult<String>, String>(new TypeToken<String>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<String>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);
                if (netWorkListener != null) {
                    //?????????????????????
                    if (e.getCode() == 601)
                        netWorkListener.onSucceed(601);

                    netWorkListener.onMessage(e.getMessage());
                    netWorkListener.onAfters();
                }
            }

            @Override
            public void onNext(String data) {
                //??????
                TransferBean transferBean = GsonUtil.GsonToBean(data, TransferBean.class);
                if (transferBean.getCode()==0){
                    String docodeJson = "";
                    try {
                        docodeJson = decryptByPrivateKey(transferBean.getInfo(), getPrivateKeyPath());
                    } catch (Exception e) {
                        LogUtils.e(e);
                        if (netWorkListener != null)
                            netWorkListener.onMessage("????????????????????????");
                        return;
                    }

                    if (!isEmpty(docodeJson)) {
                        PayBean payBean = GsonUtil.GsonToBean(docodeJson, PayBean.class);
                        if (netWorkListener != null) {
                            netWorkListener.onAfters();
                            netWorkListener.onData(true, payBean);
                        }
                    } else {
                        if (netWorkListener != null)
                            netWorkListener.onMessage("??????????????????");
                    }
                }else {
                    if (netWorkListener != null) {
                        //?????????????????????
                        if (transferBean.getCode() == 601)
                            netWorkListener.onSucceed(601);

                        netWorkListener.onMessage(transferBean.getMsg());
                        netWorkListener.onAfters();
                    }
                }

            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);
    }

    /**
     * ????????????????????????
     *
     * @param context
     * @param id
     */
    public void getTransfer(Context context, long id, CustomResultListener customResultListener) {
        Map<String, String> map = new HashMap<>();

        map.put("msgId", String.valueOf(id));

        PostRequest request = HttpClient.imPost(IM_URL + GET_TRANSFER, map);
        request.headers(LocalConstant.CROSSTOKEN, UserInfoUtils.getInstance().getUserInfo().getCrossToken());
        Observable<NormalBean> observable = request
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);
                if (netWorkListener != null)
                    netWorkListener.onMessage(e.getMessage());
            }

            @Override
            public void onNext(NormalBean payBean) {
                if (netWorkListener != null)
                    netWorkListener.onMessage("????????????");
                if (customResultListener != null)
                    customResultListener.onResult(payBean.id);
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);

    }


    /**
     * ????????????
     *
     * @param context
     * @param id
     */
    public void revocationMessage(Context context, long id, CustomResultListener customResultListener) {

        Map<String, String> map = new HashMap<>();

        map.put("contentId", String.valueOf(id));

        PostRequest request = HttpClient.imPost(IM_URL + MESSAGE_WITHDRAW, map);
        request.headers(LocalConstant.CROSSTOKEN, UserInfoUtils.getInstance().getUserInfo().getCrossToken());
        Observable<NormalBean> observable = request
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);
                if (netWorkListener != null)
                    netWorkListener.onMessage(e.getMessage());
            }

            @Override
            public void onNext(NormalBean normalBean) {
                if (customResultListener != null)
                    customResultListener.onResult(normalBean);
            }
        };
        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }


    /**
     * ????????????
     */
    public void chatSetTop(Context context, String id, int sort, CustomResultListener customResultListener) {

        Map<String, String> map = getJsonMap();

        map.put("id", id);

        map.put("sort", String.valueOf(sort));

        PostRequest request = HttpClient.imPost(IM_URL + CHAT_SETTOP, map);
        request.headers(LocalConstant.CROSSTOKEN, UserInfoUtils.getInstance().getUserInfo().getCrossToken());

        Observable<NormalBean> observable = request
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);
                if (netWorkListener != null)
                    netWorkListener.onMessage(e.getMessage());
            }

            @Override
            public void onNext(NormalBean normalBean) {

                if (customResultListener != null) {
                    customResultListener.onResult("ok");
                }
                if (netWorkListener != null)
                    netWorkListener.onMessage(normalBean.msg);
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }

    /**
     * ????????????????????????
     */
    public void chatSubDel(Context context, String sid, CustomResultListener customResultListener) {

        Map<String, String> map = getJsonMap();

        map.put(LocalConstant.SUBSCRIBEID, sid);


        PostRequest request = HttpClient.imPost(IM_URL + SUBSCRIBE_DELETE, map);
        request.headers(LocalConstant.CROSSTOKEN, UserInfoUtils.getInstance().getUserInfo().getCrossToken());

        Observable<NormalBean> observable = request
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);
                if (netWorkListener != null)
                    netWorkListener.onMessage(e.getMessage());
            }

            @Override
            public void onNext(NormalBean normalBean) {

                if (customResultListener != null) {
                    customResultListener.onResult("ok");
                }
                if (netWorkListener != null)
                    netWorkListener.onMessage(normalBean.msg);
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }

    /**
     * ??????????????????
     *
     * @param sid ??????id
     */
    public void messageDel(Context context, String sid, CustomResultListener customResultListener) {

        Map<String, String> map = getJsonMap();

        map.put(LocalConstant.CONTENTID, sid);

        PostRequest request = HttpClient.imPost(IM_URL + MESSAGE_DELETE, map);

        if (UserInfoUtils.getInstance().getUserInfo() != null && !isEmpty(UserInfoUtils.getInstance().getUserInfo().getCrossToken()))
            request.headers(LocalConstant.CROSSTOKEN, UserInfoUtils.getInstance().getUserInfo().getCrossToken());

        Observable<NormalBean> observable = request
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });

        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                BaseLogUtils.e(e);
                if (netWorkListener != null)
                    netWorkListener.onMessage(e.getMessage());
            }

            @Override
            public void onNext(NormalBean normalBean) {

                if (customResultListener != null) {
                    customResultListener.onResult("ok");
                }
                if (netWorkListener != null)
                    netWorkListener.onMessage(normalBean.msg);
            }
        };

        observable.subscribe(subscriber);
        if (netWorkListener != null)
            netWorkListener.onDisposable(subscriber);
    }
}
