package com.gxdingo.sg.fragment.store;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.BusinessDistrictMessageActivity;
import com.gxdingo.sg.activity.ChatActivity;
import com.gxdingo.sg.activity.ClientActivity;
import com.gxdingo.sg.activity.ClientStoreDetailsActivity;
import com.gxdingo.sg.activity.NoticeMessageActivity;
import com.gxdingo.sg.activity.StoreActivity;
import com.gxdingo.sg.activity.StoreBusinessDistrictReleaseActivity;
import com.gxdingo.sg.activity.WebActivity;
import com.gxdingo.sg.adapter.BusinessDistrictLabelAdapter;
import com.gxdingo.sg.adapter.BusinessDistrictListAdapter;
import com.gxdingo.sg.adapter.HomePageBannerAdapter;
import com.gxdingo.sg.bean.ActivityEvent;
import com.gxdingo.sg.bean.BusinessDistrictListBean;
import com.gxdingo.sg.bean.BusinessDistrictUnfoldCommentListBean;
import com.gxdingo.sg.bean.HomeBannerBean;
import com.gxdingo.sg.bean.NumberUnreadCommentsBean;
import com.gxdingo.sg.biz.StoreBusinessDistrictContract;
import com.gxdingo.sg.dialog.BusinessDistrictCommentInputBoxDialogFragment;
import com.gxdingo.sg.dialog.PostionFunctionDialog;
import com.gxdingo.sg.dialog.SgConfirm2ButtonPopupView;
import com.gxdingo.sg.presenter.StoreBusinessDistrictPresenter;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.StoreLocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.activitiy.BaseActivity;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.kikis.commnlibrary.fragment.BaseMvpFragment;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.utils.GsonUtil;
import com.kikis.commnlibrary.utils.RecycleViewUtils;
import com.kikis.commnlibrary.utils.StringUtils;
import com.lxj.xpopup.XPopup;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.sunfusheng.marqueeview.MarqueeView;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.ClipboardUtils.copyText;
import static com.gxdingo.sg.utils.LocalConstant.BACK_TOP_BUSINESS_DISTRICT;
import static com.gxdingo.sg.utils.LocalConstant.SHOW_BUSINESS_DISTRICT_UN_READ_DOT;
import static com.gxdingo.sg.utils.StoreLocalConstant.SOTRE_REVIEW_SUCCEED;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;
import static com.kikis.commnlibrary.utils.RecycleViewUtils.forceStopRecyclerViewScroll;

/**
 * 商家端商圈
 *
 * @author JM
 */
public class StoreBusinessDistrictFragment extends BaseMvpFragment<StoreBusinessDistrictContract.StoreBusinessDistrictPresenter> implements StoreBusinessDistrictContract.StoreBusinessDistrictListener {


    @BindView(R.id.title_tv)
    public TextView title_tv;
    @BindView(R.id.tv_unread_msg_count)
    TextView tvUnreadMsgCount;

    //    @BindView(R.id.rl_message_list)
//    RelativeLayout rlMessageList;
    @BindView(R.id.iv_send_business_district)
    ImageView ivSendBusinessDistrict;
    @BindView(R.id.unread_iv)
    ImageView unread_iv;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.hint_img)
    ImageView hintImg;
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.more_img)
    ImageView more_img;
    @BindView(R.id.hint_tv)
    TextView hintTv;
    @BindView(R.id.function_bt)
    TextView functionBt;
    @BindView(R.id.classics_footer)
    ClassicsFooter classicsFooter;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;

    @BindView(R.id.title_cl)
    ConstraintLayout title_cl;

    @BindView(R.id.nodata_layout)
    View nodataLayout;

    @BindView(R.id.cl_visit_countdown)
    public ConstraintLayout cl_visit_countdown;

    @BindView(R.id.count_down_tv)
    public TextView count_down_tv;

    Context mContext;
    BusinessDistrictListAdapter mAdapter;
    //    TextView tvCommentUnfoldText;//适配器item中的展开更多控件引用
    int mDelPosition = -1;//要删除商圈的索引位置

    //页面进入类型 0客户端浏览商圈 1商家端浏览全部商圈 2商家端浏览自己的商圈 3单独浏览一个商家的商圈
    private int mType = 0;

    //客户端查询单独商家商圈所需id
    private String mcircleUserIdentifier = "";

    private Disposable mDisposable;
    //活动倒计时
    private int countDown = 15;


    private BusinessDistrictLabelAdapter mLabelAdapter;

    private RecyclerView label_recyclerView;

    private MarqueeView marqueeView;

    private Banner mBanner;

    /**
     * 商圈子视图点击监听接口
     */
    public interface OnChildViewClickListener {
        /**
         * 图片item
         *
         * @param view           被点击的View
         * @param parentPosition 父位置
         * @param position       子位置
         * @param object         内容实体
         */
        void item(View view, int parentPosition, int position, Object object);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    protected StoreBusinessDistrictContract.StoreBusinessDistrictPresenter createPresenter() {
        return new StoreBusinessDistrictPresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return true;
    }

    @Override
    protected int activityTitleLayout() {
        return 0;
    }

    @Override
    protected int initContentView() {
        return R.layout.module_fragment_store_business_district;
    }

    /**
     * 没有数据的时候布局
     *
     * @return
     */

    @Override
    protected View noDataLayout() {
        return nodataLayout;
    }

    /**
     * 下拉布局，一般SmartRefreshLayout
     *
     * @return
     */
    @Override
    protected View refreshLayout() {
        return smartrefreshlayout;
    }

    /**
     * 开启下拉刷新
     *
     * @return
     */
    @Override
    protected boolean refreshEnable() {
        return true;
    }

    /**
     * 开启加载更多数据
     *
     * @return
     */
    @Override
    protected boolean loadmoreEnable() {
        return true;
    }

    @Override
    protected void init() {
        if (args != null) {
            mType = args.getInt(Constant.PARAMAS + 0, 0);
            //客户端查询单独商家商圈所需id, mType为3时才会有该值
            mcircleUserIdentifier = args.getString(Constant.SERIALIZABLE + 0);
        }

        //页面进入类型 0客户端浏览商圈 1商家端浏览全部商圈 2商家端浏览自己的商圈 3单独浏览一个商家的商圈

        unread_iv.setVisibility(mType == 0 ? View.VISIBLE : View.GONE);

        img_back.setVisibility(mType == 3 ? View.VISIBLE : View.GONE);
        more_img.setVisibility(mType == 3 ? View.VISIBLE : View.GONE);

        title_cl.setVisibility(mType == 1 || mType == 2 ? View.GONE : View.VISIBLE);

        if (mType != 2) {
            title_tv.setText("商圈");
            ivSendBusinessDistrict.setVisibility(View.GONE);
        }/* else {
            title_tv.setText("我的商圈");
            ConstraintLayout.LayoutParams clp = (ConstraintLayout.LayoutParams) title_cl.getLayoutParams();
            clp.topMargin = dp2px(40);
        }*/

        mAdapter = new BusinessDistrictListAdapter(mContext, mOnChildViewClickListener, mType);
        recyclerView.setLayoutManager(new LinearLayoutManager(reference.get()));


        //recyclerView.addItemDecoration(new SpaceItemDecoration(dp2px(10)));
        recyclerView.setAdapter(mAdapter);
        recyclerView.getItemAnimator().setChangeDuration(0);
        headViewInit();
    }

    private void headViewInit() {

        if (mType == 1) {

            //增加头部广告
            LinearLayout mHeadLayout = (LinearLayout) LayoutInflater.from(reference.get()).inflate(R.layout.module_include_business_district_head, new LinearLayout(reference.get()));
            mBanner = mHeadLayout.findViewById(R.id.banner);
            String bannerTestJson = "[{\"id\":12,\"position\":\"app-home-middle\",\"identifier\":\"\",\"title\":\"大转盘活动\",\"type\":2,\"image\":\"https://dgkjuat.oss-cn-guangzhou.aliyuncs.com/upload/20211210/f2f9555bf330499ba59d481951b66f70.jpg\",\"page\":\"http://uat.gxdingo.com/html/#/pages/activity/turntable/lottery\",\"status\":1,\"sort\":0,\"remark\":\"大转盘活动\",\"createTime\":\"2021-12-06T09:38:01.000+00:00\",\"positionName\":null},{\"id\":13,\"position\":\"app-home-middle\",\"identifier\":\"\",\"title\":\"0元拿活动\",\"type\":2,\"image\":\"https://dgkjuat.oss-cn-guangzhou.aliyuncs.com/upload/20211210/b1cca225ca734b5e9bad0c3130d2b0ca.jpg\",\"page\":\"http://uat.gxdingo.com/html/#/pages/activity/integral/index\",\"status\":1,\"sort\":0,\"remark\":\"0元拿活动\",\"createTime\":\"2021-12-10T06:16:35.000+00:00\",\"positionName\":null}]";

            List<HomeBannerBean> bannerBeans = GsonUtil.jsonToList(bannerTestJson, HomeBannerBean.class);

            mBanner.setAdapter(new HomePageBannerAdapter(reference.get(), bannerBeans));
            mBanner.setOnBannerListener((data, position) -> {
                HomeBannerBean bannerBean = (HomeBannerBean) data;
                if (bannerBean.getType() == 2 && !StringUtils.isEmpty(bannerBean.getPage())) {
                    if (!UserInfoUtils.getInstance().isLogin()) {
                        UserInfoUtils.getInstance().goToOauthPage(reference.get());
                        onMessage(gets(R.string.please_login));
                        return;
                    }
                    goToPagePutSerializable(reference.get(), WebActivity.class, getIntentEntityMap(new Object[]{false, bannerBean.getPage()}));
                }
            });

            label_recyclerView = mHeadLayout.findViewById(R.id.label_recyclerView);

            label_recyclerView.setLayoutManager(new LinearLayoutManager(reference.get(), RecyclerView.HORIZONTAL, false));

            mLabelAdapter = new BusinessDistrictLabelAdapter();

            marqueeView = mHeadLayout.findViewById(R.id.marqueeView);

            List<String> messages = new ArrayList<>();
            messages.add("1.:因疫情原因，部分地区无法配送，奖品可以直接折算现金");
            messages.add("2.:因疫情原因，部分地区无法配送，奖品可以直接折算现金");

            marqueeView.startWithList(messages);
            marqueeView.setOnItemClickListener((position, textView) -> {

                goToPage(reference.get(), NoticeMessageActivity.class, getIntentMap(new String[]{textView.getText().toString()}));
            });
            List<String> datas = new ArrayList<>();

            datas.add("");
            datas.add("");
            datas.add("");

            mLabelAdapter.setList(datas);
            label_recyclerView.setAdapter(mLabelAdapter);
            mAdapter.addHeaderView(mHeadLayout);
        }
    }

    @Override
    protected void initData() {


    }

    @Override
    protected void lazyInit() {
        super.lazyInit();

        if (isFirstLoad) {
            isFirstLoad = !isFirstLoad;
            getP().checkLocationPermission(getRxPermissions(), mcircleUserIdentifier);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (cl_visit_countdown != null && cl_visit_countdown.getVisibility() == View.VISIBLE) {
                cl_visit_countdown.setVisibility(View.GONE);
            }
        }
        if (hidden && mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }

    }

    private void startCountDown(ActivityEvent event) {
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
        countDown = 15;
        count_down_tv.setText(countDown + "");
        Observable observable = Observable.interval(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread());

        observable.compose(((BaseActivity) getActivity()).bindToLifecycle());

        observable.subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {
                mDisposable = disposable;
            }

            @Override
            public void onNext(@NonNull Long number) {

                String value = String.valueOf(--countDown);

                if (countDown <= 0)
                    onComplete();

                if (count_down_tv != null && !isEmpty(value))
                    count_down_tv.setText(value);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

                cl_visit_countdown.setVisibility(View.GONE);
                getP().complete(event.identifier);
                if (mDisposable != null) {
                    mDisposable.dispose();
                    mDisposable = null;
                }
            }
        });
    }

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
        //商圈未读评论类型事件
        if (object instanceof ReceiveIMMessageBean.DataByType) {
            if (mType != 3)
                getP().getNumberUnreadComments();
        } else if (object instanceof ActivityEvent) {
            //活动事件
            cl_visit_countdown.setVisibility(View.VISIBLE);
            startCountDown((ActivityEvent) object);

        }
    }

    /**
     * event事件
     *
     * @param type
     */
    protected void onTypeEvent(Integer type) {
        //刷新商圈列表
        if (type == StoreLocalConstant.SOTRE_REFRESH_BUSINESS_DISTRICT_LIST
                || type == LocalConstant.LOGIN_SUCCEED || type == SOTRE_REVIEW_SUCCEED) {
            if (mType != 3)
                //获取商圈评论未读数量
                getP().getNumberUnreadComments();
            //获取商圈列表
            getP().getBusinessDistrictList(true, mcircleUserIdentifier);
            isFirstLoad = false;
        } else if (type == BACK_TOP_BUSINESS_DISTRICT) {
            forceStopRecyclerViewScroll(recyclerView);
            //返回顶部
            RecycleViewUtils.MoveToPosition((LinearLayoutManager) recyclerView.getLayoutManager(), recyclerView, 0);
        } else if (type == SHOW_BUSINESS_DISTRICT_UN_READ_DOT) {
            if (mType != 3)
                //获取商圈评论未读数量
                getP().getNumberUnreadComments();
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        //获取商圈列表
        getP().getBusinessDistrictList(true, mcircleUserIdentifier);
    }

    /**
     * 上拉加载更多数据
     */
    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        //获取商圈列表
        getP().getBusinessDistrictList(false, mcircleUserIdentifier);
    }

    @Override
    public void onSucceed(int type) {
        //商家删除商圈动态
        if (type == 200) {
            ArrayList<BusinessDistrictListBean.BusinessDistrict> businessDistrict = (ArrayList<BusinessDistrictListBean.BusinessDistrict>) mAdapter.getData();
            if (mDelPosition >= 0) {
                businessDistrict.remove(mDelPosition);
            }
            mDelPosition = -1;
            mAdapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.more_img, R.id.img_back, R.id.unread_iv, R.id.iv_send_business_district})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.more_img:
                showDialog(title_tv);
                break;
            case R.id.img_back:
                getActivity().finish();
                break;
            case R.id.unread_iv:
                startActivity(new Intent(mContext, BusinessDistrictMessageActivity.class));
                break;
            case R.id.iv_send_business_district:
                startActivity(new Intent(mContext, StoreBusinessDistrictReleaseActivity.class));
                break;
        }
    }

    private void showDialog(View view) {
        int pos[] = {-1, -1}; //保存当前坐标的数组

        view.getLocationOnScreen(pos); //获取选中的 Item 在屏幕中的位置，以左上角为原点 (0, 0)

        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .offsetY(pos[1])
                .offsetX(pos[0])
                .autoDismiss(true)
                .hasShadowBg(false)
                .asCustom(new PostionFunctionDialog(reference.get(), v -> {
                    //todo 分享链接没有，投诉功能待实现

                }).show());
    }

    /**
     * 商圈子视图点击回调
     */
    OnChildViewClickListener mOnChildViewClickListener = new OnChildViewClickListener() {
        @Override
        public void item(View view, int parentPosition, int position, Object object) {

            //点击评论一下
            if (view.getId() == R.id.tv_open_comment) {
                BusinessDistrictListBean.BusinessDistrict businessDistrict = mAdapter.getItem(parentPosition);
                showCommentInputBoxDialog(businessDistrict, "评论一下", businessDistrict.getId(), 0);
            }
            //点击评论列表中的某一条数据，回复谁
            else if (view.getId() == R.id.rv_comment_list) {
                BusinessDistrictListBean.BusinessDistrict businessDistrict = mAdapter.getItem(parentPosition);
                if (businessDistrict != null) {
                    ArrayList<BusinessDistrictListBean.Comment> commentList = businessDistrict.getCommentList();
                    if (commentList != null) {
                        BusinessDistrictListBean.Comment comment = commentList.get(parentPosition);
                        if (UserInfoUtils.getInstance().getIdentifier().equals(comment.getIdentifier())) {
                            onMessage("您不能回复自己的评论");
                            return;
                        }
                        /*  if (businessDistrict.getIdentifier().equals(comment.getIdentifier())) {
                            onMessage("您不能回复自己的评论");
                            return;
                        }*/
                        showCommentInputBoxDialog(businessDistrict, "回复" + comment.getReplyNickname(), businessDistrict.getId(), comment.getId());
                    }
                }
            }
            //点击评论数量（评论数量大于等于10）或者展开更多/收起布局（标题显示展开更多）
            else if (view.getId() == R.id.tv_comment_count || view.getId() == R.id.ll_comment_unfold_put_away_layout) {
                //tvCommentUnfoldText引用R.id.tv_comment_unfold_put_away_text，方便根据请求展开评论接口的时候根据是否还有数据做响应操作
                BusinessDistrictListBean.BusinessDistrict businessDistrict = mAdapter.getItem(parentPosition);
                getP().getUnfoldCommentList(businessDistrict, businessDistrict.getId(), businessDistrict.getCurrentPage(), businessDistrict.getPageSize());
            }
            //点击删除商圈
            else if (view.getId() == R.id.iv_delete) {
                new XPopup.Builder(reference.get())
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .isDarkTheme(false)
                        .dismissOnTouchOutside(false)
                        .asCustom(new SgConfirm2ButtonPopupView(reference.get(), "确定要删除该条商圈？", "", () -> {
                            BusinessDistrictListBean.BusinessDistrict businessDistrict = mAdapter.getItem(parentPosition);
                            mDelPosition = parentPosition;
                            getP().deleteBusinessDistrictDynamics(businessDistrict.getId());
                        }))
                        .show();
            } else if (view.getId() == R.id.picture_gridview) {
                getP().PhotoViewer(mAdapter.getData().get(parentPosition).getImages(), position);
            } else if (view.getId() == R.id.iv_avatar || view.getId() == R.id.tv_store_name) {
                if (mType == 3)
                    goToPagePutSerializable(reference.get(), ChatActivity.class, getIntentEntityMap(new Object[]{null, 11, (int) mAdapter.getData().get(parentPosition).getStoreId()}));
                else {
                    int storeId = Integer.valueOf(String.valueOf(object));
                    goToPagePutSerializable(getContext(), ClientStoreDetailsActivity.class, getIntentEntityMap(new Object[]{storeId}));
                }

            } else if (view.getId() == R.id.like_tv) {
                int status = (int) object;

                getP().likedOrUnliked(status, mAdapter.getData().get(parentPosition).getId(), parentPosition);

                mAdapter.getData().get(parentPosition).likedStatus = status;

                if (mType == 1)
                    mAdapter.notifyItemChanged(parentPosition + 1);
                else
                    mAdapter.notifyItemChanged(parentPosition);

            } else if (view.getId() == R.id.share_tv) {

                String imgUrl = mAdapter.getData().get(parentPosition).getImages() != null && mAdapter.getData().get(parentPosition).getImages().size() > 0 ? mAdapter.getData().get(parentPosition).getImages().get(0) : LocalConstant.SHARE_BUSINESS_DISTRICT_URL;

                getP().shareLink(mAdapter.getData().get(parentPosition).getContent(), imgUrl, (String) object);
            }
        }
    };


    /**
     * 显示商圈评论弹窗
     *
     * @param businessDistrict 评论/回复的那条商圈信息
     * @param hint             提示语，比如回复谁
     * @param circleId         商圈ID
     * @param parentId         回复谁的消息id
     */
    private void showCommentInputBoxDialog(BusinessDistrictListBean.BusinessDistrict businessDistrict, String hint, long circleId, long parentId) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.PARAMAS + 0, hint);

        BusinessDistrictCommentInputBoxDialogFragment fragment = BusinessDistrictCommentInputBoxDialogFragment.newInstance(BusinessDistrictCommentInputBoxDialogFragment.class, bundle);
        fragment.setOnCommentContentListener(object -> {
            String content = (String) object;
            if (isEmpty(content)) {
                onMessage("请输入评论内容！");
                return;
            }
            getP().submitCommentOrReply(businessDistrict, circleId, parentId, content);
            fragment.dismiss();
        });
        fragment.show(getActivity().getSupportFragmentManager(), BusinessDistrictCommentInputBoxDialogFragment.class.toString());
    }

    /**
     * 返回商圈数据
     *
     * @param bean
     */
    @Override
    public void onBusinessDistrictData(boolean refresh, BusinessDistrictListBean bean) {
        Log.d("TAG", "onBusinessDistrictData: ");

        if (bean != null && bean.getList() != null) {
            if (refresh) {

                boolean login = UserInfoUtils.getInstance().isLogin();
                if (login) {
                    //不是浏览别人的商圈类型，就获取消息数
                    if (mType != 3)
                        getP().getNumberUnreadComments();
                }
                mAdapter.setList(bean.getList());
            } else {
                mAdapter.addData(bean.getList());
            }
        }
    }

    /**
     * 提交评论/回复的结果
     */
    @Override
    public void onSubmitCommentOrReplyResult() {
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 返回评论列表结果（展开评论）
     *
     * @param businessDistrict 展开评论所属商圈
     * @param commentListBean  新获取展开的评论
     */
    @Override
    public void onReturnCommentListResult(BusinessDistrictListBean.BusinessDistrict businessDistrict, BusinessDistrictUnfoldCommentListBean commentListBean) {
        //得到该商圈评论集合
        ArrayList<BusinessDistrictListBean.Comment> commentList = businessDistrict.getCommentList();
        if (commentList != null) {
            //得到展开的评论
            ArrayList<BusinessDistrictUnfoldCommentListBean.UnfoldComment> unfoldCommentList = commentListBean.getList();
            if (unfoldCommentList != null) {
                //去重合并评论数据
                getP().onDuplicateRemovalMerge(commentList, unfoldCommentList, businessDistrict, commentListBean.getTotal());
            }
        }
    }

    /**
     * 返回商圈评论未读数量
     */
    @Override
    public void onNumberUnreadComments(NumberUnreadCommentsBean unreadCommentsBean) {

        //单独商圈页面不显示未读数
        if (mType != 3) {
            tvUnreadMsgCount.setVisibility(unreadCommentsBean.getUnread() > 0 ? View.VISIBLE : View.INVISIBLE);
            tvUnreadMsgCount.setText(String.valueOf(unreadCommentsBean.getUnread()));
        }


        if (UserInfoUtils.getInstance().isLogin() && UserInfoUtils.getInstance().getUserInfo() != null && UserInfoUtils.getInstance().getUserInfo().getRole() != null) {
            //刷新主菜单未读消息提示
            if (UserInfoUtils.getInstance().getUserInfo().getRole() == 10) {
                if (ClientActivity.getInstance() != null)
                    ClientActivity.getInstance().setBusinessUnreadMsgNum(unreadCommentsBean);
            } else {
                if (StoreActivity.getInstance() != null)
                    StoreActivity.getInstance().setBusinessUnreadMsgNum(unreadCommentsBean);
            }
        }
    }

    @Override
    public void onCommentListRefresh(ArrayList<BusinessDistrictListBean.Comment> commentList, ArrayList<BusinessDistrictUnfoldCommentListBean.UnfoldComment> unfoldCommentList, BusinessDistrictListBean.BusinessDistrict businessDistrict, int total) {
        //根据总数判断是否还有数据  if (commentList.size() >= commentListBean.getTotal()

        if (commentList.size() >= total) {
      /*      //没有下一页
            if (tvCommentUnfoldText != null) {
                tvCommentUnfoldText.setText("收起");
                //替换向上图标
                changeDrawable(tvCommentUnfoldText, R.drawable.module_svg_business_district_comment_put_away);
                tvCommentUnfoldText = null;
            }*/
        } else {
            //有下一页页面累加1
            businessDistrict.setCurrentPage(businessDistrict.getCurrentPage() + 1);
       /*     if (tvCommentUnfoldText != null) {
                tvCommentUnfoldText.setText("展开更多");
                //替换向下图标
                changeDrawable(tvCommentUnfoldText, R.drawable.module_svg_business_district_comment_unfold);
                tvCommentUnfoldText = null;
            }*/
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshLikeNum(String o, int position) {
        mAdapter.getData().get(position).liked = o;

        if (mType == 1)
            mAdapter.notifyItemChanged(position + 1);
        else
            mAdapter.notifyItemChanged(position);
    }

    /**
     * 更换TextView图标
     *
     * @param textView
     * @param resId
     */
    private void changeDrawable(TextView textView, int resId) {
        Drawable drawable = mContext.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, null, drawable, null);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mType == 1 && marqueeView != null)
            marqueeView.startFlipping();

        if (mType == 1 && mBanner != null)
            mBanner.start();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mType == 1 && marqueeView != null)
            marqueeView.stopFlipping();

        if (mType == 1 && mBanner != null)
            mBanner.stop();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mType == 1 && mBanner != null)
            mBanner.destroy();
    }


}
