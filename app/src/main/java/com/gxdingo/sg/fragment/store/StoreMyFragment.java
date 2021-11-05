package com.gxdingo.sg.fragment.store;

import android.view.View;

import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.StoreMyContract;
import com.gxdingo.sg.biz.StoreWalletContract;
import com.gxdingo.sg.presenter.StoreMyPresenter;
import com.kikis.commnlibrary.fragment.BaseMvpFragment;

/**
 * 商家端我的
 *
 * @author JM
 */
public class StoreMyFragment extends BaseMvpFragment<StoreMyContract.StoreMyPresenter> implements StoreMyContract.StoreMyListener {

    @Override
    protected StoreMyContract.StoreMyPresenter createPresenter() {
        return new StoreMyPresenter();
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
        return R.layout.module_fragment_store_my;
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
