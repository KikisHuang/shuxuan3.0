package com.gxdingo.sg.fragment.store;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.SPUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.BusinessDistrictMessageActivity;
import com.gxdingo.sg.activity.ClientActivity;
import com.gxdingo.sg.activity.ClientStoreDetailsActivity;
import com.gxdingo.sg.activity.StoreActivity;
import com.gxdingo.sg.activity.StoreBusinessDistrictReleaseActivity;
import com.gxdingo.sg.adapter.BusinessDistrictListAdapter;
import com.gxdingo.sg.adapter.TabPageAdapter;
import com.gxdingo.sg.bean.BusinessDistrictListBean;
import com.gxdingo.sg.bean.BusinessDistrictUnfoldCommentListBean;
import com.gxdingo.sg.bean.NumberUnreadCommentsBean;
import com.gxdingo.sg.biz.StoreBusinessDistrictContract;
import com.gxdingo.sg.dialog.SgConfirm2ButtonPopupView;
import com.gxdingo.sg.presenter.StoreBusinessDistrictPresenter;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.StoreLocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.view.ScaleTransitionPagerTitleView;
import com.kikis.commnlibrary.fragment.BaseMvpFragment;
import com.kikis.commnlibrary.utils.Constant;
import com.lxj.xpopup.XPopup;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.uuzuche.lib_zxing.view.ViewfinderView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.gxdingo.sg.adapter.TabPageAdapter.STORE_BUSINESS_DISTRICT_TAB;
import static com.gxdingo.sg.utils.LocalConstant.LOGIN_WAY;
import static com.gxdingo.sg.utils.StoreLocalConstant.SOTRE_REVIEW_SUCCEED;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;
import static com.kikis.commnlibrary.utils.ScreenUtils.dp2px;

/**
 * 商家端商圈父类
 *
 * @author Kikis
 */
public class StoreBusinessDistrictParentFragment extends BaseMvpFragment<StoreBusinessDistrictContract.StoreBusinessDistrictPresenter> implements StoreBusinessDistrictContract.StoreBusinessDistrictListener {


    @BindView(R.id.tv_unread_msg_count)
    TextView tvUnreadMsgCount;


    @BindView(R.id.view_pager)
    public ViewPager view_pager;

    @BindView(R.id.magic_indicator)
    public MagicIndicator magic_indicator;

    private TabPageAdapter tabAdapter;


    private List<String> mTitles;

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

    @OnClick({R.id.unread_iv,R.id.iv_send_business_district})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.unread_iv:
                startActivity(new Intent(reference.get(), BusinessDistrictMessageActivity.class));
                break;
            case R.id.iv_send_business_district:
                startActivity(new Intent(reference.get(), StoreBusinessDistrictReleaseActivity.class));
                break;
        }
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
                indicator.setLineHeight(ConvertUtils.dp2px(2));
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

}
