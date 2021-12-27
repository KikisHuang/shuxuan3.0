package com.gxdingo.sg.model;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.reflect.TypeToken;
import com.gxdingo.sg.bean.BusinessDistrictCommentOrReplyBean;
import com.gxdingo.sg.bean.BusinessDistrictListBean;
import com.gxdingo.sg.bean.BusinessDistrictMessageCommentListBean;
import com.gxdingo.sg.bean.BusinessDistrictUnfoldCommentListBean;
import com.gxdingo.sg.bean.NormalBean;
import com.gxdingo.sg.bean.NumberUnreadCommentsBean;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.http.Api;
import com.gxdingo.sg.http.HttpClient;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.StoreLocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.view.MyBaseSubscriber;
import com.kikis.commnlibrary.biz.MultiParameterCallbackListener;
import com.kikis.commnlibrary.utils.GsonUtil;
import com.zhouyou.http.callback.CallClazzProxy;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.ApiResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

import static com.gxdingo.sg.http.ClientApi.BUSINESS_DISTRICT_COMMENT_OR_ADD;
import static com.gxdingo.sg.http.StoreApi.BUSINESS_DISTRICT_COMMENT_OR_REPLY;
import static com.gxdingo.sg.http.StoreApi.BUSINESS_DISTRICT_GET_COMMENT;
import static com.gxdingo.sg.http.StoreApi.BUSINESS_DISTRICT_LIST;
import static com.gxdingo.sg.http.StoreApi.BUSINESS_DISTRICT_MESSAGE_COMMENT_LIST;
import static com.gxdingo.sg.http.StoreApi.DELETE_MY_OWN_COMMENT;
import static com.gxdingo.sg.http.StoreApi.GET_NUMBER_UNREAD_COMMENTS;
import static com.gxdingo.sg.http.StoreApi.RELEASE_BUSINESS_DISTRICT_INFO;
import static com.gxdingo.sg.http.StoreApi.STORE_DELETE_BUSINESS_DISTRICT_DYNAMICS;
import static com.gxdingo.sg.utils.LocalConstant.LOGIN_WAY;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

public class BusinessDistrictModel {

    protected int mPage = 1;
    protected int mPageSize = 10;
    private NetWorkListener netWorkListener;

    public BusinessDistrictModel(NetWorkListener netWorkListener) {
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
     * 获取商圈列表
     */
    public void getBusinessDistrict(Context context, boolean refresh, int storeId) {
        if (refresh)
            resetPage();//重置页码

        Map<String, String> map = new HashMap<>();
        map.put(StoreLocalConstant.CURRENT, String.valueOf(mPage));
        map.put(StoreLocalConstant.SIZE, String.valueOf(mPageSize));

        if (UserInfoUtils.getInstance().isLogin() && !isEmpty(LocalConstant.AdCode))
            map.put("area", LocalConstant.AdCode);

        if (storeId > 0)
            map.put("storeId", String.valueOf(storeId));

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
                }
                netWorkListener.onData(refresh, storeDataBean);

                if (storeDataBean != null && storeDataBean.getList() != null) {
                    //下一页
                    pageNext(refresh, storeDataBean.getList().size());
                }
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);

    }

    /**
     * 商家发布商圈信息
     */
    public void storeReleaseBusinessDistrict(Context context, String content, List<String> images) {
        Map<String, String> map = new HashMap<>();
        map.put(StoreLocalConstant.CONTENT, content);
        map.put(StoreLocalConstant.IMAGES, GsonUtil.gsonToStr(images));

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
     * 获取商圈消息评论列表
     */
    public void getMessageCommentList(Context context, boolean refresh) {
        if (refresh)
            resetPage();//重置页码

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
                    //下一页
                    pageNext(refresh, commentListBean.getList().size());
                }
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);

    }

    /**
     * 商圈评论/回复
     */
    public void commentOrReply(Context context, BusinessDistrictListBean.BusinessDistrict businessDistrict, long circleId, long parentId, String content, MultiParameterCallbackListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put(StoreLocalConstant.CIRCLE_ID, String.valueOf(circleId));
        map.put(StoreLocalConstant.PARENT_ID, String.valueOf(parentId));
        map.put(StoreLocalConstant.CONTENT, content);

        if (netWorkListener != null)
            netWorkListener.onStarts();

        Observable<BusinessDistrictCommentOrReplyBean> observable = HttpClient.post(UserInfoUtils.getInstance().getUserInfo().getRole()==10?BUSINESS_DISTRICT_COMMENT_OR_ADD:BUSINESS_DISTRICT_COMMENT_OR_REPLY, map)
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

                if (listener!=null)
                    listener.multipleDataResult(commentOrReplyBean, businessDistrict);
            }
        };

        observable.subscribe(subscriber);
        netWorkListener.onDisposable(subscriber);

    }

    /**
     * 获取评论（展开评论）
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
     * 获取商圈评论未读数量
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
     * 商家删除商圈动态
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
     * 删除我自己的评论
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
}
