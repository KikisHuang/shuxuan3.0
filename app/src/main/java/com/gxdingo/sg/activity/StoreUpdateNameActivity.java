package com.gxdingo.sg.activity;

import android.view.View;
import android.widget.TextView;

import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.StoreSettingsContract;
import com.gxdingo.sg.presenter.StoreSettingsPresenter;
import com.gxdingo.sg.view.RegexEditText;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;

import butterknife.BindView;
import butterknife.OnClick;

import static com.gxdingo.sg.http.ClientApi.CLIENT_SERVICE_AGREEMENT_KEY;
import static com.gxdingo.sg.http.ClientApi.STORE_NAMING_RULE;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * @author: Weaving
 * @date: 2021/11/11
 * @page:
 */
public class StoreUpdateNameActivity extends BaseMvpActivity<StoreSettingsContract.StoreSettingsPresenter>   {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.store_name_et)
    public RegexEditText store_name_et;

    private String storeName;

    @Override
    protected StoreSettingsContract.StoreSettingsPresenter createPresenter() {
        return new StoreSettingsPresenter();
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
        return R.layout.module_activity_store_update_name;
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
        title_layout.setTitleText("修改店铺名称");
        title_layout.setBackgroundColor(getc(R.color.divide_color));
        storeName  = getIntent().getStringExtra(Constant.PARAMAS+0);
        if (!isEmpty(storeName))
            store_name_et.setText(storeName);
    }

    @OnClick({R.id.btn_submit,R.id.store_name_rule_tv})
    public void onClickViews(View v){
        switch (v.getId()){
            case R.id.store_name_rule_tv:

                goToPagePutSerializable(reference.get(), WebActivity.class, getIntentEntityMap(new Object[]{true, 0, STORE_NAMING_RULE}));

//                goToPagePutSerializable(reference.get(), ArticleListActivity.class, getIntentEntityMap(new Object[]{0, STORE_NAMING_RULE}));
                break;
            case R.id.btn_submit:
                String s = store_name_et.getText().toString();
                if (isEmpty(s)){
                    onMessage("店铺名不能为空！");
                    return;
                }
                getP().updateStoreName(s);
                break;
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSucceed(int type) {
        super.onSucceed(type);
        finish();
    }
}
