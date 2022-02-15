package com.gxdingo.sg.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.ConvertUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.TabPageAdapter;
import com.gxdingo.sg.bean.RankListBean;
import com.gxdingo.sg.biz.AppBarStateChangeListener;
import com.gxdingo.sg.biz.RankingContract;
import com.gxdingo.sg.http.Api;
import com.gxdingo.sg.presenter.RankingPresenter;
import com.gxdingo.sg.view.ScaleTransitionPagerTitleView;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.AnimationUtil;
import com.tencent.bugly.proguard.B;

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

import static com.gxdingo.sg.adapter.TabPageAdapter.RANKING_TAB;
import static com.gxdingo.sg.http.Api.CLIENT_PRIVACY_AGREEMENT_KEY;
import static com.gxdingo.sg.http.Api.HTML;
import static com.gxdingo.sg.http.Api.RANKING_RULE;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.CommonUtils.getd;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;
import static com.kikis.commnlibrary.utils.ScreenUtils.dp2px;

/**
 * @author: Kikis
 * @date: 2022/2/9
 * @page: 排行榜页面
 */
public class RankingActivity extends BaseMvpActivity<RankingContract.RankingPresenter> implements RankingContract.RankingListener {


    @BindView(R.id.top_tv)
    public TextView top_tv;

    @BindView(R.id.img_back)
    public ImageView img_back;

    @BindView(R.id.view_pager)
    public ViewPager view_pager;

    @BindView(R.id.magic_indicator)
    public MagicIndicator magic_indicator;

    @BindView(R.id.app_bar)
    public AppBarLayout appBar;

    private TabPageAdapter tabAdapter;

    private List<String> mTitles;


    @Override
    protected RankingContract.RankingPresenter createPresenter() {
        return new RankingPresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return false;
    }

    @Override
    protected int activityTitleLayout() {
        return 0;
    }

    @Override
    protected boolean ImmersionBar() {
        return false;
    }

    @Override
    protected int StatusBarColors() {
        return R.color.white;
    }

    @Override
    protected int NavigationBarColor() {
        return 0;
    }

    @Override
    protected int activityBottomLayout() {
        return 0;
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
    protected boolean closeDispatchTouchEvent() {
        return false;
    }

    @Override
    protected int initContentView() {
        return R.layout.module_activity_ranking2;
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

        mTitles.add("周榜");
        mTitles.add("月榜");


        tabAdapter = new TabPageAdapter(getSupportFragmentManager(), mTitles, RANKING_TAB);
        view_pager.setAdapter(tabAdapter);
        view_pager.setOffscreenPageLimit(2);

        initMagicIndicator();
        setAppBarLayoutListener();
    }

    @Override
    protected void initData() {

    }

    private void setAppBarLayoutListener() {

        appBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {

            @Override
            public void onStateChanged(AppBarLayout appBarLayout, int state, int verticalOffset) {

                //折叠状态
                top_tv.setVisibility(state == COLLAPSED ? View.VISIBLE : View.GONE);

                img_back.setImageDrawable(state != COLLAPSED ? getd(R.drawable.module_svg_back_white_icon) : getd(R.drawable.module_svg_back_black_icon));
            }

            @Override
            public void onStateOffset(AppBarLayout appBarLayout, int verticalOffset) {

            }
        });
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
//                simplePagerTitleView.setDeselectedStyle(Typeface.BOLD);
                simplePagerTitleView.setText(mTitles.get(index));
                simplePagerTitleView.setTextSize(18);
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
                indicator.setLineHeight(ConvertUtils.dp2px(3));
                indicator.setLineWidth(ConvertUtils.dp2px(20));
                indicator.setRoundRadius(ConvertUtils.dp2px(2));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(getc(R.color.green16));

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

    @OnClick({R.id.img_back,R.id.rule_tv})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.rule_tv:
                goToPagePutSerializable(reference.get(), WebActivity.class, getIntentEntityMap(new Object[]{false, Api.URL + HTML + "identifier=" + RANKING_RULE}));
                break;

        }
    }

    @Override
    public void onRankingListResult(RankListBean data) {

    }
}
