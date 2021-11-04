package com.gxdingo.sg.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.LoginContract;
import com.gxdingo.sg.presenter.LoginPresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.GlideUtils;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.indicator.RectangleIndicator;
import com.youth.banner.indicator.RoundLinesIndicator;
import com.youth.banner.listener.OnPageChangeListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.gxdingo.sg.utils.LocalConstant.LOGIN_WAY;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;

/**
 * @author: Weaving
 * @date: 2021/11/2
 * @page:
 */
public class WelcomeActivity extends BaseMvpActivity<LoginContract.LoginPresenter> {

    @BindView(R.id.banner)
    public Banner banner;

    @BindView(R.id.go_bt)
    public Button go_bt;

    @BindView(R.id.skip_tv)
    public TextView skip_tv;

    private List<Integer> bannerData;

    @Override
    protected LoginContract.LoginPresenter createPresenter() {
        return new LoginPresenter();
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
    protected boolean ImmersionBar() {
        return false;
    }

    @Override
    protected int StatusBarColors() {
        return R.color.divide_color;
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
        return R.layout.module_activity_welcome;
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
        bannerData = new ArrayList<>();
        bannerData.add(R.drawable.welcome_page1);
        bannerData.add(R.drawable.welcome_page2);

        banner.setAdapter(new BannerImageAdapter<Integer>(bannerData) {
            @Override
            public void onBindView(BannerImageHolder holder, Integer data, int position, int size) {

                Glide.with(reference.get()).load(data).apply(GlideUtils.getInstance().getDefaultOptions()).into(holder.imageView);

            }
        })
                .addBannerLifecycleObserver(this)//添加生命周期观察者
                .setIndicator(new RectangleIndicator(reference.get()));

        banner.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                go_bt.setVisibility(position == 1 ? View.VISIBLE : View.GONE);
                skip_tv.setVisibility(position == 1 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData() {

    }
    @OnClick({R.id.skip_tv, R.id.go_bt})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;
        switch (v.getId()) {
            case R.id.skip_tv:
            case R.id.go_bt:
                boolean isUser = SPUtils.getInstance().getBoolean(LOGIN_WAY, true);
//                Class clas = isUser ? ClientActivity.class : StoreActivity.class;
//                goToPage(reference.get(), clas, null);
                finish();
                break;

        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        banner.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //停止轮播
        banner.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁
        banner.destroy();
    }
}
