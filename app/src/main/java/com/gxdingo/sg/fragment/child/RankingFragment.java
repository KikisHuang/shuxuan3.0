package com.gxdingo.sg.fragment.child;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.ArticleListActivity;
import com.gxdingo.sg.activity.ClientAccountSecurityActivity;
import com.gxdingo.sg.activity.ClientAddressListActivity;
import com.gxdingo.sg.activity.StoreAuthInfoActivity;
import com.gxdingo.sg.activity.StoreQRCodeActivity;
import com.gxdingo.sg.activity.StoreSettingActivity;
import com.gxdingo.sg.activity.WebActivity;
import com.gxdingo.sg.adapter.RankingAdapter;
import com.gxdingo.sg.biz.MyConfirmListener;
import com.gxdingo.sg.biz.RankingContract;
import com.gxdingo.sg.dialog.SgConfirm2ButtonPopupView;
import com.gxdingo.sg.presenter.RankingPresenter;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.fragment.BaseMvpFragment;
import com.lxj.xpopup.XPopup;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.gxdingo.sg.adapter.TabPageAdapter.TAB;
import static com.gxdingo.sg.http.Api.SERVER_URL;
import static com.gxdingo.sg.http.Api.WEB_URL;
import static com.gxdingo.sg.utils.DateUtils.dealDateFormat;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * @author: Kikis
 * @date: 2022/2/10
 * @page: 排行榜子页面
 */
public class RankingFragment extends BaseMvpFragment<RankingContract.RankingPresenter> implements RankingContract.RankingListener {


    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.nodata_layout)
    public View nodata_layout;

    private int mTab = 0;

    private RankingAdapter mAdapter;


    @Override
    protected RankingContract.RankingPresenter createPresenter() {
        return new RankingPresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return true;
    }

    @Override
    protected int activityTitleLayout() {
        return 0;
    }

    @Override
    protected int initContentView() {
        return R.layout.module_include_nodata_recycle;
    }

    @Override
    protected View noDataLayout() {
        return nodata_layout;
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
        if (args != null)
            mTab = args.getInt(TAB);

        mAdapter = new RankingAdapter();

        List<String> testData = new ArrayList<>();

        testData.add("");
        testData.add("");
        testData.add("");
        testData.add("");
        testData.add("");
        testData.add("");
        testData.add("");
        testData.add("");
        testData.add("");

        mAdapter.setList(testData);

        recyclerView.setLayoutManager(new LinearLayoutManager(reference.get()));

        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {


    }

    @Override
    protected void lazyInit() {
        super.lazyInit();

    }

    @OnClick({})
    public void OnClickViews(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);

    }
}
