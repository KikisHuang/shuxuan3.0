package com.gxdingo.sg.fragment.client;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.SubscribesBean;
import com.gxdingo.sg.biz.ClientMessageContract;
import com.gxdingo.sg.biz.ClientMineContract;
import com.gxdingo.sg.presenter.ClientMessagePresenter;
import com.gxdingo.sg.presenter.ClientMinePresenter;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.fragment.BaseMvpFragment;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.kikis.commnlibrary.utils.CommonUtils.getd;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientMessageFragment extends BaseMvpFragment<ClientMessageContract.ClientMessagePresenter> implements ClientMessageContract.ClientMessageListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.smartrefreshlayout)
    public SmartRefreshLayout smartrefreshlayout;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.nodata_layout)
    public View nodata_layout;

    @Override
    protected ClientMessageContract.ClientMessagePresenter createPresenter() {
        return new ClientMessagePresenter();
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
        return R.layout.module_include_refresh;
    }

    @Override
    protected View noDataLayout() {
        return nodata_layout;
    }

    @Override
    protected View refreshLayout() {
        return smartrefreshlayout;
    }

    @Override
    protected boolean refreshEnable() {
        return true;
    }

    @Override
    protected boolean loadmoreEnable() {
        return true;
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        super.onRefresh(refreshLayout);
        if (UserInfoUtils.getInstance().isLogin())
        getP().getSubscribesMessage(true);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        super.onLoadMore(refreshLayout);
        if (UserInfoUtils.getInstance().isLogin())
        getP().getSubscribesMessage(false);
    }

    @Override
    protected void init() {
        title_layout.setTitleText(gets(R.string.message));
        title_layout.setBackVisible(false);
    }

    @Override
    protected void initData() {
        if (UserInfoUtils.getInstance().isLogin())
            getP().getSubscribesMessage(true);
    }

    @Override
    public void onSubscribes(List<SubscribesBean> subscribesBeans) {

    }
}
