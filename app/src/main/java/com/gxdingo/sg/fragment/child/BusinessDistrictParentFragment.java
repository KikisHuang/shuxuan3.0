package com.gxdingo.sg.fragment.child;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.BusinessDistrictMessageActivity;
import com.gxdingo.sg.activity.ClientActivity;
import com.gxdingo.sg.activity.ClientSearchActivity;
import com.gxdingo.sg.activity.RealNameAuthenticationActivity;
import com.gxdingo.sg.activity.StoreBusinessDistrictReleaseActivity;
import com.gxdingo.sg.adapter.TabPageAdapter;
import com.gxdingo.sg.bean.BannerBean;
import com.gxdingo.sg.bean.BusinessDistrictListBean;
import com.gxdingo.sg.bean.BusinessDistrictUnfoldCommentListBean;
import com.gxdingo.sg.bean.NumberUnreadCommentsBean;
import com.gxdingo.sg.bean.UserBean;
import com.gxdingo.sg.biz.StoreBusinessDistrictContract;
import com.gxdingo.sg.biz.onSwipeGestureListener;
import com.gxdingo.sg.dialog.AuthenticationStatusPopupView;
import com.gxdingo.sg.dialog.SgConfirm2ButtonPopupView;
import com.gxdingo.sg.presenter.BusinessDistrictPresenter;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.view.NoScrollViewPager;
import com.gxdingo.sg.view.ScaleTransitionPagerTitleView;
import com.kikis.commnlibrary.bean.ReLoginBean;
import com.kikis.commnlibrary.fragment.BaseMvpFragment;
import com.lxj.xpopup.XPopup;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.gxdingo.sg.adapter.TabPageAdapter.STORE_BUSINESS_DISTRICT_TAB;
import static com.gxdingo.sg.utils.LocalConstant.LOGIN_SUCCEED;
import static com.gxdingo.sg.utils.LocalConstant.LOGOUT_SUCCEED;
import static com.gxdingo.sg.utils.LocalConstant.SHOW_BUSINESS_DISTRICT_UN_READ_DOT;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;

/**
 * 商家端商圈父类
 *
 * @author Kikis
 */
public class BusinessDistrictParentFragment extends BaseMvpFragment<StoreBusinessDistrictContract.StoreBusinessDistrictPresenter> implements StoreBusinessDistrictContract.StoreBusinessDistrictListener {


    @BindView(R.id.tv_unread_msg_count)
    TextView tvUnreadMsgCount;

    @BindView(R.id.view_pager)
    public NoScrollViewPager view_pager;

    @BindView(R.id.magic_indicator)
    public MagicIndicator magic_indicator;

    private TabPageAdapter tabAdapter;

    private List<String> mTitles;

    private SgConfirm2ButtonPopupView mLoginDialog;

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
        return R.layout.module_fragment_store_business_district_parent;
    }

    @Override
    protected View noDataLayout() {
        return null;
    }

    @Override
    protected View refreshLayout() {
        return null;
    }

    @Override
    protected boolean refreshEnable() {
        return false;
    }

    @Override
    protected boolean loadmoreEnable() {
        return false;
    }

    @Override
    protected void init() {

        mTitles = new ArrayList<>();

        mTitles.add(gets(R.string.store_business_district));
        mTitles.add(gets(R.string.my));

        initMagicIndicator();
        tabAdapter = new TabPageAdapter(getChildFragmentManager(), mTitles, STORE_BUSINESS_DISTRICT_TAB);
        view_pager.setAdapter(tabAdapter);
        view_pager.setScroll(UserInfoUtils.getInstance().isLogin());
        view_pager.setOnSwipeGestureListener(new onSwipeGestureListener() {
            @Override
            public void onRightSwipe() {

            }

            @Override
            public void onLeftSwipe() {
                if (!UserInfoUtils.getInstance().isLogin())
                    showLoginDialog(getString(R.string.please_login_after_browse_business_district));
            }
        });
        view_pager.setOffscreenPageLimit(2);
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void lazyInit() {
        super.lazyInit();
        boolean login = UserInfoUtils.getInstance().isLogin();
        if (login) {
            getP().getNumberUnreadComments();
        }
    }

    @OnClick({R.id.search_iv, R.id.unread_iv, R.id.iv_send_business_district})
    public void onViewClicked(View view) {

        if (view.getId() != R.id.search_iv && !UserInfoUtils.getInstance().isLogin()) {
            showLoginDialog(getString(R.string.please_login_before_operation));
            return;
        }

        switch (view.getId()) {
            case R.id.search_iv:
                goToPage(getContext(), ClientSearchActivity.class, getIntentMap(new String[]{LocalConstant.AoiName}));
                break;
            case R.id.unread_iv:
                startActivity(new Intent(reference.get(), BusinessDistrictMessageActivity.class));
                break;
            case R.id.iv_send_business_district:
                UserBean userBean = UserInfoUtils.getInstance().getUserInfo();
                //登录角色 Role 10=客户 11=商家
                if (userBean.getRole() == 10 && userBean.getAuthenticationStatus() == 0)
                    getP().refreshUserStatus();
                 else
                    startActivity(new Intent(reference.get(), StoreBusinessDistrictReleaseActivity.class));
                break;
        }
    }

    /**
     * 显示去实名认证弹窗
     */
    private void showDialog() {

        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .autoDismiss(true)
                .hasShadowBg(true)
                .asCustom(new AuthenticationStatusPopupView(reference.get(), null, status -> {
                    if (status == -1)
                        goToPage(reference.get(), RealNameAuthenticationActivity.class, null);
                }).show());

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Override
    public void onBusinessDistrictData(boolean refresh, BusinessDistrictListBean bean) {

    }

    @Override
    public void onSubmitCommentOrReplyResult() {

    }

    @Override
    public void onReturnCommentListResult(BusinessDistrictListBean.BusinessDistrict businessDistrict, BusinessDistrictUnfoldCommentListBean commentListBean) {

    }

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
        if (object instanceof ReLoginBean) {
            //重登状态重置滑动
            view_pager.setScroll(UserInfoUtils.getInstance().isLogin());
        }
    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);

        if (type == SHOW_BUSINESS_DISTRICT_UN_READ_DOT) {
            //获取商圈评论未读数量
            getP().getNumberUnreadComments();
        } else if (type == LOGIN_SUCCEED || type == LOGOUT_SUCCEED) {

            if (type == LOGOUT_SUCCEED && view_pager.getCurrentItem() != 0)
                view_pager.setCurrentItem(0);

            view_pager.setScroll(type == LOGIN_SUCCEED ? true : false);
        }
    }

    /**
     * 返回商圈评论未读数量
     */
    @Override
    public void onNumberUnreadComments(NumberUnreadCommentsBean unreadCommentsBean) {
        tvUnreadMsgCount.setVisibility(unreadCommentsBean.getUnread() > 0 ? View.VISIBLE : View.INVISIBLE);
        tvUnreadMsgCount.setText(String.valueOf(unreadCommentsBean.getUnread()));

        if (ClientActivity.getInstance() != null)
            ClientActivity.getInstance().setBusinessUnreadMsgNum(unreadCommentsBean);

 /*       if (UserInfoUtils.getInstance().getUserInfo().getRole() == 10) {
            if (ClientActivity.getInstance() != null)
                ClientActivity.getInstance().setBusinessUnreadMsgNum(unreadCommentsBean.getUnread());
        } else {
            if (StoreActivity.getInstance() != null)
                StoreActivity.getInstance().setBusinessUnreadMsgNum(unreadCommentsBean.getUnread());
        }*/
    }

    @Override
    public void onCommentListRefresh(ArrayList<BusinessDistrictListBean.Comment> commentList, ArrayList<BusinessDistrictUnfoldCommentListBean.UnfoldComment> unfoldCommentList, BusinessDistrictListBean.BusinessDistrict businessDistrict, int total) {

    }

    @Override
    public void refreshLikeNum(String o, int postition, int status) {

    }

    @Override
    public void onBannerResult(BannerBean data) {

    }

    @Override
    public void showAuthenticationDialog() {
        showDialog();
    }


    /**
     * MagicIndicator 通用标题初始化方法
     */
    private void initMagicIndicator() {

        CommonNavigator commonNavigator = new CommonNavigator(reference.get());
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitles == null ? 0 : mTitles.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ScaleTransitionPagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
//                simplePagerTitleView.setMinScale(0.91f);
                simplePagerTitleView.setMinScale(0.96f);
                simplePagerTitleView.setDeselectedStyle(Typeface.BOLD);
                simplePagerTitleView.setText(mTitles.get(index));
                simplePagerTitleView.setTextSize(16);
                simplePagerTitleView.setMaxEms(10);
                simplePagerTitleView.setSingleLine(true);
                simplePagerTitleView.setEllipsize(TextUtils.TruncateAt.END);
                simplePagerTitleView.setNormalColor(getc(R.color.gray_a9));
                simplePagerTitleView.setSelectedColor(getc(R.color.graye2e2e2));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (index == 1 && !UserInfoUtils.getInstance().isLogin()) {
                            showLoginDialog(getString(R.string.please_login_after_browse_business_district));
                        } else
                            view_pager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {

             /*   LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineWidth(dp2px(20));
                indicator.setLineHeight(dp2px(3));
                indicator.setRoundRadius(dip2px(context, 2));
                indicator.setColors(getc(R.color.green_dominant_tone));*/


                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(ConvertUtils.dp2px(2.5f));
                indicator.setLineWidth(ConvertUtils.dp2px(20));
                indicator.setRoundRadius(ConvertUtils.dp2px(1));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(getc(R.color.graye2e2e2));

                return indicator;
            }
        });
        magic_indicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerDrawable(new ColorDrawable() {
            @Override
            public int getIntrinsicWidth() {
                return ConvertUtils.dp2px(10);
            }
        });
        ViewPagerHelper.bind(magic_indicator, view_pager);
    }

    private void showLoginDialog(String msg) {
        if (mLoginDialog == null) {
            mLoginDialog = new SgConfirm2ButtonPopupView(reference.get(), msg, () -> UserInfoUtils.getInstance().goToOauthPage(reference.get()));

            new XPopup.Builder(reference.get())
                    .isDarkTheme(false)
                    .asCustom(mLoginDialog).show();
        } else if (!mLoginDialog.isShow())
            mLoginDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mLoginDialog != null) {
            mLoginDialog.destroy();
            mLoginDialog = null;
        }
    }
}
