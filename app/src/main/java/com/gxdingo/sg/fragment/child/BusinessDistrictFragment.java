package com.gxdingo.sg.fragment.child;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.BusinessDistrictMessageActivity;
import com.gxdingo.sg.activity.ChatActivity;
import com.gxdingo.sg.activity.ClientActivity;
import com.gxdingo.sg.activity.StoreDetailsActivity;
import com.gxdingo.sg.activity.NoticeMessageActivity;
import com.gxdingo.sg.activity.RankingActivity;
import com.gxdingo.sg.activity.StoreActivity;
import com.gxdingo.sg.activity.StoreBusinessDistrictReleaseActivity;
import com.gxdingo.sg.activity.WebActivity;
import com.gxdingo.sg.adapter.BusinessDistrictLabelAdapter;
import com.gxdingo.sg.adapter.BusinessDistrictListAdapter;
import com.gxdingo.sg.adapter.HomePageBannerAdapter;
import com.gxdingo.sg.bean.ActivityEvent;
import com.gxdingo.sg.bean.BannerBean;
import com.gxdingo.sg.bean.BusinessDistrictListBean;
import com.gxdingo.sg.bean.BusinessDistrictUnfoldCommentListBean;
import com.gxdingo.sg.bean.HomeBannerBean;
import com.gxdingo.sg.bean.NumberUnreadCommentsBean;
import com.gxdingo.sg.bean.ShareEvent;
import com.gxdingo.sg.biz.StoreBusinessDistrictContract;
import com.gxdingo.sg.dialog.BusinessDistrictCommentInputBoxDialogFragment;
import com.gxdingo.sg.dialog.SgConfirm2ButtonPopupView;
import com.gxdingo.sg.presenter.BusinessDistrictPresenter;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.StoreLocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.activitiy.BaseActivity;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.kikis.commnlibrary.fragment.BaseMvpFragment;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.utils.RecycleViewUtils;
import com.kikis.commnlibrary.utils.StringUtils;
import com.lxj.xpopup.XPopup;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.sunfusheng.marqueeview.MarqueeView;
import com.youth.banner.Banner;
import com.youth.banner.indicator.RectangleIndicator;

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
import static com.blankj.utilcode.util.TimeUtils.getNowMills;
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
 * ???????????????
 *
 * @author JM
 */
public class BusinessDistrictFragment extends BaseMvpFragment<StoreBusinessDistrictContract.StoreBusinessDistrictPresenter> implements StoreBusinessDistrictContract.StoreBusinessDistrictListener {


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
    //    TextView tvCommentUnfoldText;//?????????item??????????????????????????????
    int mDelPosition = -1;//??????????????????????????????

    //?????????????????? 0????????????????????? 1??????????????????????????? 2?????????????????????????????? 3?????????????????????????????????
    private int mType = 0;

    //???????????????????????????????????????id
    private String mcircleUserIdentifier = "";

    private Disposable mDisposable;
    //???????????????
    private int countDown = 15;

    private BusinessDistrictLabelAdapter mLabelAdapter;

    private RecyclerView label_recyclerView;

    private MarqueeView marqueeView;
    private LinearLayout notice_ll;

    private Banner mBanner;

    private List<BannerBean.NoticeListDTO> noticeList;

    /**
     * ?????????????????????????????????
     */
    public interface OnChildViewClickListener {
        /**
         * ??????item
         *
         * @param view           ????????????View
         * @param parentPosition ?????????
         * @param position       ?????????
         * @param object         ????????????
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
        return new BusinessDistrictPresenter();
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
     * ???????????????????????????
     *
     * @return
     */

    @Override
    protected View noDataLayout() {
        return nodataLayout;
    }

    /**
     * ?????????????????????SmartRefreshLayout
     *
     * @return
     */
    @Override
    protected View refreshLayout() {
        return smartrefreshlayout;
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @Override
    protected boolean refreshEnable() {
        return true;
    }

    /**
     * ????????????????????????
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
            //???????????????????????????????????????id, mType???3??????????????????
            mcircleUserIdentifier = args.getString(Constant.SERIALIZABLE + 0);
        }

        //?????????????????? 0????????????????????? 1??????????????????????????? 2?????????????????????????????? 3?????????????????????????????????

        unread_iv.setVisibility(mType == 0 ? View.VISIBLE : View.GONE);

        img_back.setVisibility(mType == 3 ? View.VISIBLE : View.GONE);
//        more_img.setVisibility(mType == 3 ? View.VISIBLE : View.GONE);
        more_img.setVisibility(View.GONE);

        title_cl.setVisibility(mType == 1 || mType == 2 ? View.GONE : View.VISIBLE);

        if (mType != 3) {
            title_tv.setText("??????");
            ivSendBusinessDistrict.setVisibility(View.GONE);
        } else {
            String title_name = args.getString(Constant.SERIALIZABLE + 1);
            title_tv.setText(title_name);
            ivSendBusinessDistrict.setVisibility(View.GONE);
        }

        mAdapter = new BusinessDistrictListAdapter(mContext, mOnChildViewClickListener, mType);
        recyclerView.setLayoutManager(new LinearLayoutManager(reference.get()));


        //recyclerView.addItemDecoration(new SpaceItemDecoration(dp2px(10)));
        recyclerView.setAdapter(mAdapter);
        recyclerView.getItemAnimator().setChangeDuration(0);
        headViewInit();
    }

    private void headViewInit() {

        if (mType == 1) {

            //??????????????????
            LinearLayout mHeadLayout = (LinearLayout) LayoutInflater.from(reference.get()).inflate(R.layout.module_include_business_district_head, new LinearLayout(reference.get()));
            mBanner = mHeadLayout.findViewById(R.id.banner);

            label_recyclerView = mHeadLayout.findViewById(R.id.label_recyclerView);

            label_recyclerView.setLayoutManager(new LinearLayoutManager(reference.get(), RecyclerView.HORIZONTAL, false));

            mLabelAdapter = new BusinessDistrictLabelAdapter();

            marqueeView = mHeadLayout.findViewById(R.id.marqueeView);
            notice_ll = mHeadLayout.findViewById(R.id.notice_ll);

            marqueeView.setOnItemClickListener((position, textView) -> {
                goToPage(reference.get(), NoticeMessageActivity.class, getIntentMap(new String[]{noticeList.get(position).getContent()}));
            });

            label_recyclerView.setAdapter(mLabelAdapter);
            mAdapter.addHeaderView(mHeadLayout);
        }
    }

    @Override
    protected void initData() {

        if (mType == 1) {
            getP().getBannerDataInfo();
        }
    }

    @Override
    protected void lazyInit() {
        super.lazyInit();

        if (isFirstLoad) {
            isFirstLoad = !isFirstLoad;
            getP().checkLocationPermission(getRxPermissions(), mcircleUserIdentifier);
        }else {
            long lastViewTime = SPUtils.getInstance().getLong(LocalConstant.LAST_VIEW_TIME,0);

            if (getNowMills() - lastViewTime>LocalConstant.REFRESH_BUSINESS_DISTRICT_TIME){
                if (refreshLayout!=null){
                    refreshLayout.autoRefresh();
                    RecycleViewUtils.MoveToPositionTop(recyclerView, 0);
                }
            }else
                new Thread(() -> SPUtils.getInstance().put(LocalConstant.LAST_VIEW_TIME,getNowMills())).start();
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
        //??????????????????????????????
        if (object instanceof ReceiveIMMessageBean.DataByType) {
            if (mType != 3)
                getP().getNumberUnreadComments();
        } else if (object instanceof ActivityEvent) {
            if (mType == 1) {
                //????????????????????????????????????????????????
                cl_visit_countdown.setVisibility(View.VISIBLE);
                startCountDown((ActivityEvent) object);
            }

        } else if (object instanceof ShareEvent) {
            ShareEvent shareEvent = (ShareEvent) object;

            //??????????????????
            getP().shareGetBusinessDistrictList(mcircleUserIdentifier, shareEvent.code);

        }
    }

    /**
     * event??????
     *
     * @param type
     */
    protected void onTypeEvent(Integer type) {
        //??????????????????
        if (type == StoreLocalConstant.SOTRE_REFRESH_BUSINESS_DISTRICT_LIST
                || type == LocalConstant.LOGIN_SUCCEED || type == SOTRE_REVIEW_SUCCEED) {
            if (mType != 3)
                //??????????????????????????????
                getP().getNumberUnreadComments();

            if (mType == 2)
                mcircleUserIdentifier = UserInfoUtils.getInstance().getUserInfo().getIdentifier();
            isFirstLoad = false;
            RecycleViewUtils.MoveToPositionTop(recyclerView, 0);
            onRefresh(refreshLayout);

        } else if (type == BACK_TOP_BUSINESS_DISTRICT) {
            forceStopRecyclerViewScroll(recyclerView);
            //????????????
            RecycleViewUtils.MoveToPosition((LinearLayoutManager) recyclerView.getLayoutManager(), recyclerView, 0);
        } else if (type == SHOW_BUSINESS_DISTRICT_UN_READ_DOT) {
            if (mType != 3)
                //??????????????????????????????
                getP().getNumberUnreadComments();
        }
    }

    /**
     * ????????????
     */
    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        if (mType == 1)
            getP().getBannerDataInfo();

        //??????????????????
        getP().getBusinessDistrictList(true, mcircleUserIdentifier);
    }

    /**
     * ????????????????????????
     */
    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        //??????????????????
        getP().getBusinessDistrictList(false, mcircleUserIdentifier);
    }

    @Override
    public void onSucceed(int type) {
        //????????????????????????
        if (type == 200) {
            ArrayList<BusinessDistrictListBean.BusinessDistrict> businessDistrict = (ArrayList<BusinessDistrictListBean.BusinessDistrict>) mAdapter.getData();
            if (mDelPosition >= 0) {
                businessDistrict.remove(mDelPosition);
            }
            mDelPosition = -1;
            mAdapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.img_back, R.id.unread_iv, R.id.iv_send_business_district})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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

    /**
     * ???????????????????????????
     */
    OnChildViewClickListener mOnChildViewClickListener = new OnChildViewClickListener() {
        @Override
        public void item(View view, int parentPosition, int position, Object object) {

            //??????????????????
            if (view.getId() == R.id.tv_open_comment) {
                BusinessDistrictListBean.BusinessDistrict businessDistrict = mAdapter.getItem(parentPosition);
                showCommentInputBoxDialog(businessDistrict, "????????????", businessDistrict.getId(), 0);
            }
            //???????????????????????????????????????????????????
            else if (view.getId() == R.id.rv_comment_list) {
                if (checkLogin()) {
                    BusinessDistrictListBean.BusinessDistrict businessDistrict = mAdapter.getItem(parentPosition);
                    if (businessDistrict != null) {
                        ArrayList<BusinessDistrictListBean.Comment> commentList = businessDistrict.getCommentList();
                        if (commentList != null) {
                            BusinessDistrictListBean.Comment comment = commentList.get(position);
                            if (UserInfoUtils.getInstance().getIdentifier().equals(comment.getIdentifier())) {
                                onMessage("??????????????????????????????");
                                return;
                            }
                            showCommentInputBoxDialog(businessDistrict, "??????" + comment.getReplyNickname(), businessDistrict.getId(), comment.getId());
                        }
                    }
                }
            }
            //?????????????????????????????????????????????10?????????????????????/??????????????????????????????????????????
            else if (view.getId() == R.id.tv_comment_count || view.getId() == R.id.ll_comment_unfold_put_away_layout) {

                //tvCommentUnfoldText??????R.id.tv_comment_unfold_put_away_text???????????????????????????????????????????????????????????????????????????????????????
                BusinessDistrictListBean.BusinessDistrict businessDistrict = mAdapter.getItem(parentPosition);
                getP().getUnfoldCommentList(businessDistrict, businessDistrict.getId(), businessDistrict.getCurrentPage(), businessDistrict.getPageSize());
            }
            //??????????????????
            else if (view.getId() == R.id.iv_delete) {
                new XPopup.Builder(reference.get())
                        .isDestroyOnDismiss(true) //???????????????????????????????????????????????????
                        .isDarkTheme(false)
                        .dismissOnTouchOutside(false)
                        .asCustom(new SgConfirm2ButtonPopupView(reference.get(), "??????????????????????????????", "", () -> {
                            BusinessDistrictListBean.BusinessDistrict businessDistrict = mAdapter.getItem(parentPosition);
                            mDelPosition = parentPosition;
                            getP().deleteBusinessDistrictDynamics(businessDistrict.getId());
                        }))
                        .show();
            } else if (view.getId() == R.id.picture_gridview) {
                getP().PhotoViewer(mAdapter.getData().get(parentPosition).getImages(), position);
            } else if (view.getId() == R.id.iv_avatar || view.getId() == R.id.tv_store_name) {
                if (mType == 3)
                    goToPagePutSerializable(reference.get(), ChatActivity.class, getIntentEntityMap(new Object[]{null, 11, mAdapter.getData().get(parentPosition).circleUserIdentifier}));
                else {
                    String id = String.valueOf(object);
                    goToPagePutSerializable(getContext(), StoreDetailsActivity.class, getIntentEntityMap(new Object[]{id}));
                }

            } else if (view.getId() == R.id.like_tv) {
                if (checkLogin()) {
                    if (checkClickInterval(R.id.like_tv)) {
                        int status = (int) object;
                        getP().likedOrUnliked(status, mAdapter.getData().get(parentPosition).getId(), parentPosition);
                    } else
                        onMessage(getString(R.string.take_a_break));

                }

            } else if (view.getId() == R.id.share_tv) {

                String imgUrl = mAdapter.getData().get(parentPosition).getImages() != null && mAdapter.getData().get(parentPosition).getImages().size() > 0 ? mAdapter.getData().get(parentPosition).getImages().get(0) : LocalConstant.SHARE_BUSINESS_DISTRICT_URL;

                getP().shareLink(mAdapter.getData().get(parentPosition).getContent(), imgUrl, (String) object);

            }
        }
    };

    private boolean checkLogin() {
        if (!UserInfoUtils.getInstance().isLogin()) {
            new XPopup.Builder(reference.get())
                    .isDarkTheme(false)
                    .isDestroyOnDismiss(true) //???????????????????????????????????????????????????
                    .asCustom(new SgConfirm2ButtonPopupView(reference.get(), gets(R.string.please_login), () -> UserInfoUtils.getInstance().goToOauthPage(reference.get()))).show();
            return false;
        }
        return true;
    }


    /**
     * ????????????????????????
     *
     * @param businessDistrict ??????/???????????????????????????
     * @param hint             ???????????????????????????
     * @param circleId         ??????ID
     * @param parentId         ??????????????????id
     */
    private void showCommentInputBoxDialog(BusinessDistrictListBean.BusinessDistrict businessDistrict, String hint, long circleId, long parentId) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.PARAMAS + 0, hint);

        BusinessDistrictCommentInputBoxDialogFragment fragment = BusinessDistrictCommentInputBoxDialogFragment.newInstance(BusinessDistrictCommentInputBoxDialogFragment.class, bundle);
        fragment.setOnCommentContentListener(object -> {
            String content = (String) object;
            if (isEmpty(content)) {
                onMessage("????????????????????????");
                return;
            }
            getP().submitCommentOrReply(businessDistrict, circleId, parentId, content);
            fragment.dismiss();
        });
        fragment.show(getActivity().getSupportFragmentManager(), BusinessDistrictCommentInputBoxDialogFragment.class.toString());
    }

    /**
     * ??????????????????
     *
     * @param bean
     */
    @Override
    public void onBusinessDistrictData(boolean refresh, BusinessDistrictListBean bean) {

        if (bean != null && bean.getList() != null) {
            if (refresh) {

                boolean login = UserInfoUtils.getInstance().isLogin();
                if (login) {
                    //??????????????????????????????????????????????????????
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
     * ????????????/???????????????
     */
    @Override
    public void onSubmitCommentOrReplyResult() {
        mAdapter.notifyDataSetChanged();
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param businessDistrict ????????????????????????
     * @param commentListBean  ????????????????????????
     */
    @Override
    public void onReturnCommentListResult(BusinessDistrictListBean.BusinessDistrict businessDistrict, BusinessDistrictUnfoldCommentListBean commentListBean) {
        //???????????????????????????
        ArrayList<BusinessDistrictListBean.Comment> commentList = businessDistrict.getCommentList();
        if (commentList != null) {
            //?????????????????????
            ArrayList<BusinessDistrictUnfoldCommentListBean.UnfoldComment> unfoldCommentList = commentListBean.getList();
            if (unfoldCommentList != null) {
                //????????????????????????
                getP().onDuplicateRemovalMerge(commentList, unfoldCommentList, businessDistrict, commentListBean.getTotal());
            }
        }
    }

    /**
     * ??????????????????????????????
     */
    @Override
    public void onNumberUnreadComments(NumberUnreadCommentsBean unreadCommentsBean) {

        //????????????????????????????????????
        if (mType != 3) {
            tvUnreadMsgCount.setVisibility(unreadCommentsBean.getUnread() > 0 ? View.VISIBLE : View.INVISIBLE);
            tvUnreadMsgCount.setText(String.valueOf(unreadCommentsBean.getUnread()));
        }


        if (UserInfoUtils.getInstance().isLogin() && UserInfoUtils.getInstance().getUserInfo() != null && UserInfoUtils.getInstance().getUserInfo().getRole() != null) {
            //?????????????????????????????????
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
        //????????????????????????????????????  if (commentList.size() >= commentListBean.getTotal()

        if (commentList.size() >= total) {
      /*      //???????????????
            if (tvCommentUnfoldText != null) {
                tvCommentUnfoldText.setText("??????");
                //??????????????????
                changeDrawable(tvCommentUnfoldText, R.drawable.module_svg_business_district_comment_put_away);
                tvCommentUnfoldText = null;
            }*/
        } else {
            //????????????????????????1
            businessDistrict.setCurrentPage(businessDistrict.getCurrentPage() + 1);
       /*     if (tvCommentUnfoldText != null) {
                tvCommentUnfoldText.setText("????????????");
                //??????????????????
                changeDrawable(tvCommentUnfoldText, R.drawable.module_svg_business_district_comment_unfold);
                tvCommentUnfoldText = null;
            }*/
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshLikeNum(String o, int pos, int status) {

        mAdapter.getData().get(pos).likedStatus = status;

        mAdapter.getData().get(pos).liked = o;

        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onBannerResult(BannerBean data) {

        if (mBanner != null && data.getAppHomeHeader() != null) {

            mBanner.setAdapter(new HomePageBannerAdapter(reference.get(), data.getAppHomeHeader()));

            mBanner.setIndicator(new RectangleIndicator(reference.get()));

            mBanner.setOnBannerListener((d, position) -> {
                HomeBannerBean bannerBean = (HomeBannerBean) d;

                if (checkLogin()) {
                    if ((bannerBean.getType() == 2 || bannerBean.getType() == 3) && !StringUtils.isEmpty(bannerBean.getPage()))
                        //?????? 0=????????? 1=APP?????? 2=H5??????  3=H5???????????????????????????????????????
                        goToPagePutSerializable(reference.get(), WebActivity.class, getIntentEntityMap(new Object[]{false, bannerBean.getPage(), "", bannerBean.getType() == 3 ? true : false}));
                    else if (bannerBean.getType() == 1)
                        //???????????????
                        goToPage(reference.get(), RankingActivity.class, null);
                }
            });
        }

        if (marqueeView != null && data.noticeStringList != null && data.noticeStringList.size() > 0) {
            noticeList = data.getNoticeList();
            marqueeView.startWithList(data.noticeStringList);
            notice_ll.setVisibility(View.VISIBLE);
        } else
            notice_ll.setVisibility(View.GONE);

        if (mLabelAdapter != null && data.getIconList() != null) {
            mLabelAdapter.setList(data.getIconList());
        }
    }

    @Override
    public void showAuthenticationDialog() {

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
