package com.gxdingo.sg.fragment.store;

import android.view.View;

import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.StoreWalletContract;
import com.gxdingo.sg.presenter.StoreWalletPresenter;
import com.kikis.commnlibrary.fragment.BaseMvpFragment;

/**
 * 商家端钱包
 *
 * @author JM
 */
public class StoreWalletFragment extends BaseMvpFragment<StoreWalletContract.StoreWalletPresenter> implements StoreWalletContract.StoreWalletListener {

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
        return 0;
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

    }

    @Override
    protected void initData() {

    }
}
