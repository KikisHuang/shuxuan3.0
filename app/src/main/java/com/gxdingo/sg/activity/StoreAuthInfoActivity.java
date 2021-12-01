package com.gxdingo.sg.activity;

import android.view.View;

import com.allen.library.SuperTextView;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.StoreAuthInfoBean;
import com.gxdingo.sg.biz.StoreSettingsContract;
import com.gxdingo.sg.presenter.StoreSettingsPresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.view.TemplateTitle;

import butterknife.BindView;
import butterknife.OnClick;

import static com.kikis.commnlibrary.utils.IntentUtils.getIntentMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;

/**
 * @author: Weaving
 * @date: 2021/11/11
 * @page:
 */
public class StoreAuthInfoActivity extends BaseMvpActivity<StoreSettingsContract.StoreSettingsPresenter> {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.business_scope_stv)
    public SuperTextView business_scope_stv;

    @BindView(R.id.details_address_stv)
    public SuperTextView details_address_stv;


    private StoreAuthInfoBean authInfoBean;

    @Override
    protected StoreSettingsContract.StoreSettingsPresenter createPresenter() {
        return new StoreSettingsPresenter();
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
        return R.layout.module_activity_store_ahth_info;
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
        title_layout.setTitleText("认证信息");
    }

    @Override
    protected void initData() {
        getP().getAuthInfo();
    }

    @OnClick(R.id.business_license_stv)
    public void onClickViews(View v){
        if (authInfoBean!=null)
            goToPage(this,StoreQualificationActivity.class,getIntentMap(new String[]{authInfoBean.getBusinessLicence()}));
    }

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
        if (object instanceof StoreAuthInfoBean){
            authInfoBean = (StoreAuthInfoBean) object;
            StringBuffer stringBuffer = new StringBuffer();
            for (int i=0;i<authInfoBean.getCategoryList().size();i++){
                stringBuffer.append(authInfoBean.getCategoryList().get(i).getName());
                if (i!=authInfoBean.getCategoryList().size()-1)
                    stringBuffer.append(",");
            }
            business_scope_stv.setRightString(stringBuffer.toString());
            details_address_stv.setRightString(authInfoBean.getAddress());
        }

    }
}