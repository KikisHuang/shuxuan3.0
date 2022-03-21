package com.gxdingo.sg.activity;

import android.content.Intent;
import android.view.View;
import android.view.WindowManager;

import com.amap.api.location.AMapLocationClient;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.gxdingo.sg.MyApplication;
import com.gxdingo.sg.biz.LoginContract;
import com.gxdingo.sg.dialog.ProtocolPopupView;
import com.gxdingo.sg.presenter.LoginPresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.utils.BaseLogUtils;
import com.kikis.commnlibrary.utils.GsonUtil;
import com.kikis.commnlibrary.utils.ScreenUtils;
import com.lxj.xpopup.XPopup;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.model.HttpHeaders;

import butterknife.OnClick;
import cn.net.shoot.sharetracesdk.AppData;
import cn.net.shoot.sharetracesdk.ShareTrace;
import cn.net.shoot.sharetracesdk.ShareTraceWakeUpListener;

import static com.gxdingo.sg.utils.LocalConstant.FIRST_LOGIN_KEY;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * @author: Weaving
 * @date: 2021/10/12
 * @page:
 */
public class SplashActivity extends BaseMvpActivity<LoginContract.LoginPresenter> implements ShareTraceWakeUpListener {

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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 应用在后台被调起来时获取一键调起参数
        ShareTrace.getWakeUpTrace(intent, this::onWakeUp);
    }


    @Override
    protected void init() {
        ShareTrace.getWakeUpTrace(getIntent(), this::onWakeUp);

        if (SPUtils.getInstance().getBoolean(FIRST_LOGIN_KEY, true)) {
            new XPopup.Builder(reference.get())
                    .isDestroyOnDismiss(true)
                    .isDarkTheme(false)
                    .autoDismiss(false)
                    .dismissOnBackPressed(false)
                    .dismissOnTouchOutside(false)
                    .asCustom(new ProtocolPopupView(reference.get(), o -> {
                        int type = (int) o;
                        if (type != 0) {
                            AMapLocationClient.updatePrivacyShow(this, true, true);
                            AMapLocationClient.updatePrivacyAgree(this, true);
                            MyApplication.getInstance().init();

                            goToPage(reference.get(), WelcomeActivity.class, null);
                        }
                        finish();
                    })).show();
        } else {
            AMapLocationClient.updatePrivacyShow(this, true, true);
            AMapLocationClient.updatePrivacyAgree(this, true);
            goToPage(reference.get(), ClientActivity.class, null);

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

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
        if (object instanceof MyApplication) {
            LogUtils.w(" ====== MyApplication ====== ");
        }
    }

    @Override
    public void onWakeUp(AppData data) {
        BaseLogUtils.i("onWakeUp sharetrace appData=" + (data == null ? null :GsonUtil.gsonToStr(data)));

        if (data != null ) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.put("AppData", GsonUtil.gsonToStr(data));
            EasyHttp.getInstance().getCommonHeaders().put(httpHeaders);
        }

    }
}
