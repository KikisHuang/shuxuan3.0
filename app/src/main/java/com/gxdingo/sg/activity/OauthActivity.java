package com.gxdingo.sg.activity;

import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.amap.api.location.AMapLocationClient;
import com.blankj.utilcode.util.SPUtils;
import com.gxdingo.sg.bean.IdSwitchEvent;
import com.gxdingo.sg.bean.WeChatLoginEvent;
import com.gxdingo.sg.biz.LoginContract;
import com.gxdingo.sg.dialog.ProtocolPopupView;
import com.gxdingo.sg.presenter.LoginPresenter;
import com.gxdingo.sg.utils.ClientLocalConstant;
import com.gxdingo.sg.utils.LocalConstant;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.bean.ReLoginBean;
import com.lxj.xpopup.XPopup;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.OnClick;

import static com.gxdingo.sg.utils.LocalConstant.FIRST_LOGIN_KEY;
import static com.gxdingo.sg.utils.LocalConstant.LOGIN_WAY;
import static com.gxdingo.sg.utils.LocalConstant.QUITLOGINPAGE;
import static com.gxdingo.sg.utils.LocalConstant.STORE_LOGIN_SUCCEED;
import static com.kikis.commnlibrary.utils.Constant.LOGOUT;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.KikisUitls.getContext;

/**
 * @author: Kikis
 * @date: 2021/11/19
 * @page:一键登录唤起页
 */
public class OauthActivity extends BaseMvpActivity<LoginContract.LoginPresenter> {
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

    @Override
    protected void init() {

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
            WeChatLoginEvent event = (WeChatLoginEvent) object;
            if (!TextUtils.isEmpty(event.code))
                getP().oauthWeChatLogin(event.code);
        }else if (object instanceof IdSwitchEvent){
            IdSwitchEvent event = (IdSwitchEvent) object;
            getP().switchId(event.isUser);
        }
    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type == LocalConstant.CLIENT_LOGIN_SUCCEED || type == LocalConstant.STORE_LOGIN_SUCCEED || type == QUITLOGINPAGE){
            if (type == STORE_LOGIN_SUCCEED) {
                sendEvent(new ReLoginBean());
                SPUtils.getInstance().put(LOGIN_WAY, false);//保存商家登录

                goToPage(reference.get(), StoreActivity.class, null);

            }
            finish();
        }


    }
}
