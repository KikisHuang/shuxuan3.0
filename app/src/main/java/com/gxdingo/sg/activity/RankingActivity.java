package com.gxdingo.sg.activity;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientMineBean;
import com.gxdingo.sg.biz.ClientHomeContract;
import com.gxdingo.sg.biz.ClientMineContract;
import com.gxdingo.sg.biz.RankingContract;
import com.gxdingo.sg.presenter.ClientMinePresenter;
import com.gxdingo.sg.presenter.RankinigPresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: Kikis
 * @date: 2022/2/9
 * @page: 排行榜页面
 */
public class RankingActivity extends BaseMvpActivity<RankingContract.RankingPresenter> implements RankingContract.RankingListener {



    @Override
    protected RankingContract.RankingPresenter createPresenter() {
        return new RankinigPresenter();
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
        return R.layout.module_activity_unsubscribe;
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
    }

    @Override
    protected void initData() {

    }

    @OnClick({})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;
        switch (v.getId()) {


        }
    }
}
