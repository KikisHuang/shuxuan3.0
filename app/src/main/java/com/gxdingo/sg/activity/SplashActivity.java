package com.gxdingo.sg.activity;

import android.view.View;
import android.view.WindowManager;

import com.amap.api.location.AMapLocationClient;
import com.blankj.utilcode.util.SPUtils;
import com.gxdingo.sg.biz.LoginContract;
import com.gxdingo.sg.dialog.ProtocolPopupView;
import com.gxdingo.sg.presenter.LoginPresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.lxj.xpopup.XPopup;

import butterknife.OnClick;

import static com.gxdingo.sg.utils.LocalConstant.FIRST_LOGIN_KEY;
import static com.gxdingo.sg.utils.LocalConstant.LOGIN_WAY;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;

/**
 * @author: Weaving
 * @date: 2021/10/12
 * @page:
 */
public class SplashActivity extends BaseMvpActivity<LoginContract.LoginPresenter> {
    @Override
    protected LoginContract.LoginPresenter createPresenter() {
        return new LoginPresenter();
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
        if (SPUtils.getInstance().getBoolean(FIRST_LOGIN_KEY, true)) {
            new XPopup.Builder(reference.get())
                    .isDestroyOnDismiss(true)
                    .isDarkTheme(false)
                    .autoDismiss(false)
                    .dismissOnBackPressed(false)
                    .dismissOnTouchOutside(false)
                    .asCustom(new ProtocolPopupView(reference.get(), o -> {
                        int type = (int) o;
                        if (type == 0)
                            finish();
                        else {
                            AMapLocationClient.updatePrivacyShow(this, true, true);
                            AMapLocationClient.updatePrivacyAgree(this, true);
                            goToPage(reference.get(), WelcomeActivity.class, null);
                            finish();
                        }
                    })).show();
        } else {
            AMapLocationClient.updatePrivacyShow(this, true, true);
            AMapLocationClient.updatePrivacyAgree(this, true);

            boolean isUser = SPUtils.getInstance().getBoolean(LOGIN_WAY, true);
            Class clas = isUser ? ClientActivity.class : StoreActivity.class;
            goToPage(reference.get(), clas, null);
            finish();
        }
    }

    @Override
    protected void initData() {

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
}
