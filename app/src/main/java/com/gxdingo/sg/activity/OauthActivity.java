package com.gxdingo.sg.activity;

import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.gxdingo.sg.bean.WeChatLoginEvent;
import com.gxdingo.sg.biz.LoginContract;
import com.gxdingo.sg.presenter.LoginPresenter;
import com.gxdingo.sg.utils.LocalConstant;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;

import butterknife.OnClick;

import static com.gxdingo.sg.utils.LocalConstant.QUITLOGINPAGE;
import static com.gxdingo.sg.utils.WechatUtils.weChatLoginType;

/**
 * @author: Kikis
 * @date: 2021/11/19
 * @page:一键登录唤起页
 */
public class OauthActivity extends BaseMvpActivity<LoginContract.LoginPresenter> {


    private static OauthActivity instance;

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
        return 0;
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
        return 0;
    }

    @Override
    protected boolean refreshEnable() {
        return false;
    }

    @Override
    protected boolean loadmoreEnable() {
        return false;
    }

    public static OauthActivity getInstance() {
        return instance;
    }

    @Override
    protected void init() {
        instance = this;
    }

    @Override
    protected void initData() {
        getP().oauth();
    }

    @Override
    protected void onBaseCreate() {
        super.onBaseCreate();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @OnClick({})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;
        switch (v.getId()) {

        }
    }

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
        //微信登录事件
        if (object instanceof WeChatLoginEvent) {
            if (weChatLoginType == 0) {
                WeChatLoginEvent event = (WeChatLoginEvent) object;
                if (!TextUtils.isEmpty(event.code))
                    getP().oauthWeChatLogin(event.code);
            }

        }
    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type == LocalConstant.LOGIN_SUCCEED || type == QUITLOGINPAGE) {
            if (type != QUITLOGINPAGE)
                getP().quitlogin();

            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (instance != null)
            instance = null;
    }
}
