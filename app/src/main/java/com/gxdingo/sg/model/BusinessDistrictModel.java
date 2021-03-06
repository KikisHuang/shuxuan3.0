package com.gxdingo.sg.model;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.reflect.TypeToken;
import com.gxdingo.sg.bean.BannerBean;
import com.gxdingo.sg.bean.BusinessDistrictCommentOrReplyBean;
import com.gxdingo.sg.bean.BusinessDistrictListBean;
import com.gxdingo.sg.bean.BusinessDistrictMessageCommentListBean;
import com.gxdingo.sg.bean.BusinessDistrictUnfoldCommentListBean;
import com.gxdingo.sg.bean.NormalBean;
import com.gxdingo.sg.bean.NumberUnreadCommentsBean;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.http.Api;
import com.gxdingo.sg.http.HttpClient;
import com.gxdingo.sg.utils.ClientLocalConstant;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.StoreLocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.view.MyBaseSubscriber;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.biz.MultiParameterCallbackListener;
import com.kikis.commnlibrary.utils.GsonUtil;
import com.zhouyou.http.callback.CallClazzProxy;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.ApiResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

import static com.gxdingo.sg.http.Api.BUSINESS_DISTRICT_COMMENT_OR_ADD;
import static com.gxdingo.sg.http.Api.BUSINESS_DISTRICT_COMMENT_OR_REPLY;
import static com.gxdingo.sg.http.Api.BUSINESS_DISTRICT_GET_COMMENT;
import static com.gxdingo.sg.http.Api.BUSINESS_DISTRICT_LIST;
import static com.gxdingo.sg.http.Api.BUSINESS_DISTRICT_MESSAGE_COMMENT_LIST;
import static com.gxdingo.sg.http.Api.CIRCLE_HEADER_ADS;
import static com.gxdingo.sg.http.Api.CIRCLE_LIKEDORUNLIKED;
import static com.gxdingo.sg.http.Api.DELETE_MY_OWN_COMMENT;
import static com.gxdingo.sg.http.Api.GET_NUMBER_UNREAD_COMMENTS;
import static com.gxdingo.sg.http.Api.RELEASE_BUSINESS_DISTRICT_INFO;
import static com.gxdingo.sg.http.Api.STORE_DELETE_BUSINESS_DISTRICT_DYNAMICS;
import static com.gxdingo.sg.utils.LocalConstant.lat;
import static com.gxdingo.sg.utils.LocalConstant.lon;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

public class BusinessDistrictModel {

    protected int mPage = 1;
    protected int mPageSize = 10;
    private NetWorkListener netWorkListener;

    public BusinessDistrictModel(NetWorkListener netWorkListener) {
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
     * ??????????????????
     */
    public void getBusinessDistrict(Context context, boolean refresh, String circleUserIdentifier, String circleCode) {
        if (refresh)
            resetPage();//????????????

        Map<String, String> map = new HashMap<>();
        map.put(StoreLocalConstant.CURRENT, String.valueOf(mPage));
        map.put(StoreLocalConstant.SIZE, String.valueOf(mPageSize));

        if (lat != 0 && lon != 0) {
            map.put(StoreLocalConstant.LONGITUDE, String.valueOf(lon));
            map.put(StoreLocalConstant.LATITUDE, String.valueOf(lat));
        }


        if (UserInfoUtils.getInstance().isLogin() && !isEmpty(LocalConstant.AdCode))
            map.put("area", LocalConstant.AdCode);

        if (!isEmpty(circleCode))
            map.put("circleCode", circleCode);

        if (UserInfoUtils.getInstance().isLogin() && !isEmpty(UserInfoUtils.getInstance().getIdentifier()))
            map.put("identifier", UserInfoUtils.getInstance().getIdentifier());


        if (!isEmpty(circleUserIdentifier))
            map.put("circleUserIdentifier", circleUserIdentifier);

        if (netWorkListener != null)
            netWorkListener.onStarts();

        Observable<BusinessDistrictListBean> observable = HttpClient.post(BUSINESS_DISTRICT_LIST, map)
                .execute(new CallClazzProxy<ApiResult<BusinessDistrictListBean>, BusinessDistrictListBean>(new TypeToken<BusinessDistrictListBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<BusinessDistrictListBean>(context) {
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
            public void onNext(BusinessDistrictListBean storeDataBean) {
                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    netWorkListener.onData(refresh, storeDataBean);
                }


                if (storeDataBean != null && storeDataBean.getList() != null) {
                    //?????????
                    pageNext(refresh, storeDataBean.getList().size());
                }
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);

    }

    /**
     * ????????????????????????
     */
    public void storeReleaseBusinessDistrict(Context context, String content, List<String> images, List<BusinessDistrictListBean.Labels> labels) {

        Map<String, String> map = new HashMap<>();
        map.put(StoreLocalConstant.CONTENT, content);

        map.put(StoreLocalConstant.IMAGES, GsonUtil.gsonToStr(images));

        if (labels != null && labels.size() > 0) {
            map.put(LocalConstant.LABELS, GsonUtil.gsonToStr(labels));
        }

        if (lon != 0)
            map.put(LocalConstant.LONGITUDE, GsonUtil.gsonToStr(lon));
        if (lat != 0)
            map.put(LocalConstant.LATITUDE, GsonUtil.gsonToStr(lat));

/*        if (!isEmpty(LocalConstant.AdCode))
            map.put(ClientLocalConstant.REGIONPATH, LocalConstant.AdCode);*/

        if (netWorkListener != null)
            netWorkListener.onStarts();

        Observable<NormalBean> observable = HttpClient.post(RELEASE_BUSINESS_DISTRICT_INFO, map)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
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
            public void onNext(NormalBean normalBean) {
                if (netWorkListener != null)
                    netWorkListener.onAfters();

                if (netWorkListener != null)
                    netWorkListener.onSucceed(100);

            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);

    }

    /**
     * ??????????????????????????????
     */
    public void getMessageCommentList(Context context, boolean refresh) {
        if (refresh)
            resetPage();//????????????

        if (netWorkListener != null)
            netWorkListener.onStarts();

        Observable<BusinessDistrictMessageCommentListBean> observable = HttpClient.post(BUSINESS_DISTRICT_MESSAGE_COMMENT_LIST)
                .execute(new CallClazzProxy<ApiResult<BusinessDistrictMessageCommentListBean>, BusinessDistrictMessageCommentListBean>(new TypeToken<BusinessDistrictMessageCommentListBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<BusinessDistrictMessageCommentListBean>(context) {
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
            public void onNext(BusinessDistrictMessageCommentListBean commentListBean) {
                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                }
                netWorkListener.onData(refresh, commentListBean);

                if (commentListBean != null && commentListBean.getList() != null) {
                    //?????????
                    pageNext(refresh, commentListBean.getList().size());
                }
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);

    }

    /**
     * ????????????/??????
     */
    public void commentOrReply(Context context, BusinessDistrictListBean.BusinessDistrict businessDistrict, long circleId, long parentId, String content, MultiParameterCallbackListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put(StoreLocalConstant.CIRCLE_ID, String.valueOf(circleId));
        map.put(StoreLocalConstant.PARENT_ID, String.valueOf(parentId));
        map.put(StoreLocalConstant.CONTENT, content);

        if (netWorkListener != null)
            netWorkListener.onStarts();

        //UserInfoUtils.getInstance().getUserInfo().getRole() == 10 ? BUSINESS_DISTRICT_COMMENT_OR_ADD : BUSINESS_DISTRICT_COMMENT_OR_REPLY
        Observable<BusinessDistrictCommentOrReplyBean> observable = HttpClient.post(BUSINESS_DISTRICT_COMMENT_OR_ADD, map)
                .execute(new CallClazzProxy<ApiResult<BusinessDistrictCommentOrReplyBean>, BusinessDistrictCommentOrReplyBean>(new TypeToken<BusinessDistrictCommentOrReplyBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<BusinessDistrictCommentOrReplyBean>(context) {
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
            public void onNext(BusinessDistrictCommentOrReplyBean commentOrReplyBean) {
                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                }

                if (listener != null)
                    listener.multipleDataResult(commentOrReplyBean, businessDistrict);
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);

    }

    /**
     * ??????????????????????????????
     */
    public void getCommentList(Context context, BusinessDistrictListBean.BusinessDistrict businessDistrict, long circleId, int current, int size, MultiParameterCallbackListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put(StoreLocalConstant.CIRCLE_ID, String.valueOf(circleId));
        map.put(StoreLocalConstant.CURRENT, String.valueOf(current));
        map.put(StoreLocalConstant.SIZE, String.valueOf(size));

        if (netWorkListener != null)
            netWorkListener.onStarts();

        Observable<BusinessDistrictUnfoldCommentListBean> observable = HttpClient.post(BUSINESS_DISTRICT_GET_COMMENT, map)
                .execute(new CallClazzProxy<ApiResult<BusinessDistrictUnfoldCommentListBean>, BusinessDistrictUnfoldCommentListBean>(new TypeToken<BusinessDistrictUnfoldCommentListBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<BusinessDistrictUnfoldCommentListBean>(context) {
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
            public void onNext(BusinessDistrictUnfoldCommentListBean commentListBean) {
                if (netWorkListener != null) {
                    netWorkListener.onAfters();
                    listener.multipleDataResult(commentListBean, businessDistrict);
                }
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);

    }

    /**
     * ??????????????????????????????
     */
    public void getNumberUnreadComments(Context context, MultiParameterCallbackListener listener) {
        if (netWorkListener != null)
            netWorkListener.onStarts();

        Observable<NumberUnreadCommentsBean> observable = HttpClient.post(GET_NUMBER_UNREAD_COMMENTS)
                .execute(new CallClazzProxy<ApiResult<NumberUnreadCommentsBean>, NumberUnreadCommentsBean>(new TypeToken<NumberUnreadCommentsBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<NumberUnreadCommentsBean>(context) {
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
            public void onNext(NumberUnreadCommentsBean unreadBean) {
                if (netWorkListener != null)
                    netWorkListener.onAfters();

                if (listener != null)
                    listener.multipleDataResult(unreadBean);

            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);

    }

    /**
     * ????????????????????????
     */
    public void storeDeleteBusinessDistrictDynamics(Context context, long id) {
        Map<String, String> map = new HashMap<>();
        map.put(StoreLocalConstant.ID, String.valueOf(id));

        if (netWorkListener != null)
            netWorkListener.onStarts();

        Observable<NormalBean> observable = HttpClient.post(STORE_DELETE_BUSINESS_DISTRICT_DYNAMICS, map)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
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
            public void onNext(NormalBean normalBean) {
                if (netWorkListener != null)
                    netWorkListener.onAfters();

                if (netWorkListener != null)
                    netWorkListener.onSucceed(200);

            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);

    }

    /**
     * ????????????????????????
     */
    public void deleteMyOwnComment(Context context, long id) {
        Map<String, String> map = new HashMap<>();
        map.put(StoreLocalConstant.ID, String.valueOf(id));

        if (netWorkListener != null)
            netWorkListener.onStarts();

        Observable<NormalBean> observable = HttpClient.post(DELETE_MY_OWN_COMMENT, map)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
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
            public void onNext(NormalBean normalBean) {
                if (netWorkListener != null)
                    netWorkListener.onAfters();

                if (netWorkListener != null)
                    netWorkListener.onSucceed(300);

            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);

    }

    /**
     * ?????? or ????????????
     */
    public void likedOrUnliked(Context context, int status, long id, CustomResultListener customResultListener) {

        Map<String, String> map = new HashMap<>();

        map.put(LocalConstant.STATUS, String.valueOf(status));
/*
        if (UserInfoUtils.getInstance().isLogin() && UserInfoUtils.getInstance().getUserInfo() != null)
            map.put("role", String.valueOf(UserInfoUtils.getInstance().getUserInfo().getRole()));*/

        map.put("circleId", String.valueOf(id));

        Observable<NormalBean> observable = HttpClient.post(CIRCLE_LIKEDORUNLIKED, map)
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);

            }

            @Override
            public void onNext(NormalBean normalBean) {

                if (customResultListener != null)
                    customResultListener.onResult(normalBean.liked);

            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);

    }


    /**
     * ????????????/??????/??????
     */
    public void getBannerDataInfo(Context context, CustomResultListener customResultListener) {

        Observable<BannerBean> observable = HttpClient.post(CIRCLE_HEADER_ADS)
                .execute(new CallClazzProxy<ApiResult<BannerBean>, BannerBean>(new TypeToken<BannerBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<BannerBean>(context) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);

            }

            @Override
            public void onNext(BannerBean bannerBean) {

                if (customResultListener != null)
                    customResultListener.onResult(bannerBean);

            }
        };
        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);

    }
}
