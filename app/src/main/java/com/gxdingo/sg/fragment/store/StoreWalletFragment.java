package com.gxdingo.sg.fragment.store;

import android.view.View;

import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.StoreWalletContract;
import com.gxdingo.sg.presenter.StoreWalletPresenter;
import com.kikis.commnlibrary.fragment.BaseMvpFragment;
import com.kikis.commnlibrary.view.TemplateTitle;

import butterknife.BindView;

import static com.kikis.commnlibrary.utils.CommonUtils.getd;

/**
 * 商家端钱包
 *
 * @author JM
 */
public class StoreWalletFragment extends BaseMvpFragment<StoreWalletContract.StoreWalletPresenter> implements StoreWalletContract.StoreWalletListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @Override
    protected StoreWalletContract.StoreWalletPresenter createPresenter() {
        return new StoreWalletPresenter();
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
    protected int initContentView() {
        return R.layout.module_fragment_store_wallet;
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
    protected boolean refreshEnable() {
        return false;
    }

    @Override
    protected boolean loadmoreEnable() {
        return false;
    }

    @Override
    protected void init() {
        title_layout.setTitleText("钱包");
        title_layout.setMoreImg(R.drawable.module_svg_scan);
    }

    @Override
    protected void initData() {

    }
}
