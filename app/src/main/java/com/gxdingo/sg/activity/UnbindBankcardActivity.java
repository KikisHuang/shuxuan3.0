package com.gxdingo.sg.activity;

import android.view.View;
import android.widget.TextView;

import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.BankcardBean;
import com.gxdingo.sg.biz.BankcardContract;
import com.gxdingo.sg.biz.MyConfirmListener;
import com.gxdingo.sg.dialog.SgConfirm2ButtonPopupView;
import com.gxdingo.sg.presenter.BankcardPresenter;
import com.gxdingo.sg.utils.LocalConstant;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: Weaving
 * @date: 2021/10/26
 * @page:
 */
public class UnbindBankcardActivity extends BaseMvpActivity<BankcardContract.BankcardPresenter>  {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.tv_bank_card_name)
    public TextView tv_bank_card_name;

    @BindView(R.id.tv_bank_card_type)
    public TextView tv_bank_card_type;

    @BindView(R.id.tv_bank_card_number)
    public TextView tv_bank_card_number;

    private BankcardBean mBankcardBean;

    @OnClick(R.id.btn_unbind)
    public void onClickViews(View v){
        switch (v.getId()){
            case R.id.btn_unbind:
                new XPopup.Builder(reference.get())
                        .isDarkTheme(false)
                        .asCustom(new SgConfirm2ButtonPopupView(reference.get(), "提现银行卡解绑后一个月之内不能再次绑定该卡，确认解绑吗？", new MyConfirmListener() {
                            @Override
                            public void onConfirm() {
                                if (mBankcardBean!=null)
                                    getP().delete(mBankcardBean.getId());
                            }
                        })).show();

                break;
        }
    }

    @Override
    protected BankcardContract.BankcardPresenter createPresenter() {
        return new BankcardPresenter();
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
        return R.layout.module_activity_unbind_backcard;
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
        mBankcardBean = (BankcardBean) getIntent().getSerializableExtra(Constant.SERIALIZABLE+0);
    }

    @Override
    protected void initData() {
        if (mBankcardBean!=null){
            tv_bank_card_name.setText(mBankcardBean.getName());
            tv_bank_card_type.setText(mBankcardBean.getCardName());
            tv_bank_card_number.setText(mBankcardBean.getNumber());
        }
    }

    @Override
    public void onSucceed(int type) {
        super.onSucceed(type);
        sendEvent(LocalConstant.CLIENT_REFRESH_BANKCARD_LIST);
        finish();
    }
}
