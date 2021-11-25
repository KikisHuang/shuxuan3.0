package com.gxdingo.sg.activity;

import android.view.View;

import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientCouponBean;
import com.gxdingo.sg.biz.ClientCouponContract;
import com.gxdingo.sg.presenter.ClientCouponPresenter;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.view.RegexEditText;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.view.TemplateTitle;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;

/**
 * @author: Weaving
 * @date: 2021/10/29
 * @page:
 */
public class ClientFillInvitationCodeActivity extends BaseMvpActivity<ClientCouponContract.ClientCouponPresenter> implements ClientCouponContract.ClientCouponListener{

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.invitation_code_et)
    public RegexEditText invitation_code_et;

    @OnClick(R.id.btn_confirm)
    public void onClickViews(View v){
        switch (v.getId()){
            case R.id.btn_confirm:
                getP().receive();
                break;
        }
    }

    @Override
    protected ClientCouponContract.ClientCouponPresenter createPresenter() {
        return new ClientCouponPresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return false;
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
        return R.layout.module_activity_client_fill_invitation_code;
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
        title_layout.setTitleText(gets(R.string.fill_invitation_code));
        title_layout.setBackgroundColor(getc(R.color.divide_color));
    }

    @Override
    protected void initData() {

    }

    @Override
    public String getCode() {
        return invitation_code_et.getText().toString();
    }

    @Override
    public void onCouponsResult(boolean refresh, List<ClientCouponBean> couponBeans) {

    }

    @Override
    public void onSucceed(int type) {
        super.onSucceed(type);
        sendEvent(LocalConstant.CLIENT_REFRESH_USER_HOME);
        finish();
    }
}
