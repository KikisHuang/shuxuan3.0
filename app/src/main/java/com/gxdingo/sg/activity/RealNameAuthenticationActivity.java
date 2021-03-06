package com.gxdingo.sg.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.AuthenticationBean;
import com.gxdingo.sg.bean.UserBean;
import com.gxdingo.sg.biz.AuthenticationContract;
import com.gxdingo.sg.dialog.AuthenticationStatusPopupView;
import com.gxdingo.sg.presenter.AuthenticationPresenter;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;
import com.tencent.bugly.crashreport.biz.UserInfoBean;

import butterknife.BindView;
import butterknife.OnClick;

import static com.blankj.utilcode.util.ClipboardUtils.copyText;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;

/**
 * @author: Kikis
 * @date: 2021/12/30
 * @page:实名认证页
 */
public class RealNameAuthenticationActivity extends BaseMvpActivity<AuthenticationContract.AuthenticationPresenter> implements AuthenticationContract.AuthenticationListener {


    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;


    @BindView(R.id.bottom_cl)
    public ConstraintLayout bottom_cl;

    @BindView(R.id.name_edt)
    public EditText name_edt;

    @BindView(R.id.idcard_edt)
    public EditText idcard_edt;


    @BindView(R.id.submit_bt)
    public Button submit_bt;


    @Override
    protected AuthenticationContract.AuthenticationPresenter createPresenter() {
        return new AuthenticationPresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return true;
    }

    @Override
    protected int activityTitleLayout() {
        return R.layout.module_include_custom_title;
    }

    @Override
    protected boolean ImmersionBar() {
        return true;
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
        return R.layout.module_activity_real_name_authentication;
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
        title_layout.setTitleText(gets(R.string.real_name_authentication));


    }

    @Override
    protected void initData() {


    }


    @OnClick({R.id.submit_bt})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;
        switch (v.getId()) {
            case R.id.submit_bt:
                getP().verifyInit();
                break;

        }
    }

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);

    }

    @Override
    public String getIdCardName() {
        return name_edt.getText().toString();
    }

    @Override
    public String getIdCardNumber() {
        return idcard_edt.getText().toString();
    }

    /**
     * 显示认证状态弹窗
     *
     * @param data
     */
    @Override
    public void onShowAuthenticationStatusDialog(AuthenticationBean data) {

        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .autoDismiss(true)
                .hasShadowBg(true)
                .asCustom(new AuthenticationStatusPopupView(reference.get(), data, status -> {

                    if (status == 1) {
                        //认证成功保存认证状态
                        UserBean userInfo = UserInfoUtils.getInstance().getUserInfo();
                        userInfo.setAuthenticationStatus(1);
                        UserInfoUtils.getInstance().saveUserInfo(userInfo);

                        sendEvent(LocalConstant.AUTHENTICATION_SUCCEEDS);
                        finish();
                    }


                }).show());

    }


    @Override
    public void onSucceed(int type) {
        super.onSucceed(type);
        finish();
    }
}
