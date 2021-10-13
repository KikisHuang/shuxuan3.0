package com.gxdingo.sg.fragment.client;

import android.view.View;

import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.ClientBusinessDistrictContract;
import com.gxdingo.sg.presenter.ClientBusinessDistrictPresenter;
import com.kikis.commnlibrary.fragment.BaseMvpFragment;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientBusinessDistrictFragment extends BaseMvpFragment<ClientBusinessDistrictContract.ClientBusinessPresenter> {
    @Override
    protected ClientBusinessDistrictContract.ClientBusinessPresenter createPresenter() {
        return new ClientBusinessDistrictPresenter();
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
        return R.layout.module_fragment_client_business_district;
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
