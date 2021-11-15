package com.gxdingo.sg.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.donkingliang.labels.LabelsView;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.StoreHomeIMMessageAdapter;
import com.gxdingo.sg.adapter.StoreHomeSearchResultAdapter;
import com.gxdingo.sg.biz.StoreHomeSearchContract;
import com.gxdingo.sg.presenter.StoreHomeSearchPresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.bean.SubscribesListBean;
import com.kikis.commnlibrary.biz.KeyboardHeightObserver;
import com.kikis.commnlibrary.utils.SystemUtils;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.VISIBLE;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * 商家主页搜索
 *
 * @author JM
 */
public class StoreHomeSearchActivity extends BaseMvpActivity<StoreHomeSearchContract.StoreHomeSearchPresenter>
        implements StoreHomeSearchContract.StoreHomeSearchListener, LabelsView.OnLabelClickListener, KeyboardHeightObserver {

    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.et_search_box)
    EditText etSearchBox;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.hint_img)
    ImageView hintImg;
    @BindView(R.id.hint_tv)
    TextView hintTv;
    @BindView(R.id.function_bt)
    TextView functionBt;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;

    @BindView(R.id.nodata_layout)
    View nodataLayout;

    StoreHomeSearchResultAdapter mAdapter;
    @BindView(R.id.search_labels)
    public LabelsView search_labels;

    @Override
    protected StoreHomeSearchContract.StoreHomeSearchPresenter createPresenter() {
        return new StoreHomeSearchPresenter();
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
        return nodataLayout;
    }

    @Override
    protected View refreshLayout() {
        return smartrefreshlayout;
    }

    @Override
    protected boolean closeDispatchTouchEvent() {
        return false;
    }

    @Override
    protected int initContentView() {
        return R.layout.module_activity_store_home_search;
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
    protected void init() {

        etSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    ivEmpty.setVisibility(View.INVISIBLE);
                    search_labels.setVisibility(VISIBLE);
                    getP().getSearchHistory();

                    if (mAdapter.getData().size() > 0)
                        mAdapter.getData().clear();

                } else {
                    ivEmpty.setVisibility(VISIBLE);
                    search_labels.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etSearchBox.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //完成自己的事件
                SystemUtils.hideKeyboard(v);
                if (!isEmpty(etSearchBox.getText().toString()))
                    getP().search(true, etSearchBox.getText().toString());
                return true;
            }
            return false;
        });

        mAdapter = new StoreHomeSearchResultAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> goToPagePutSerializable(reference.get(), ChatActivity.class, getIntentEntityMap(new Object[]{((SubscribesListBean.SubscribesMessage) mAdapter.getData()).getShareUuid(), ((SubscribesListBean.SubscribesMessage) mAdapter.getData()).getSendUserRole()})));
    }

    @Override
    protected void initData() {
        getP().getSearchHistory();
        search_labels.setOnLabelClickListener(this);

    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        super.onRefresh(refreshLayout);
        getP().search(true, "");
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        super.onLoadMore(refreshLayout);
        getP().search(false, etSearchBox.getText().toString());
    }

    @Override
    public void onKeyboardHeightChanged(int height, int orientation) {

    }

    @OnClick({R.id.tv_cancel, R.id.iv_empty})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.iv_empty:
                etSearchBox.setText("");
                break;
        }
    }

    /**
     * 搜索历史回调
     *
     * @param gsonToList
     */
    @Override
    public void onHistoryResult(List<String> gsonToList) {

        search_labels.setVisibility(gsonToList != null && gsonToList.size() > 0 ? VISIBLE : View.GONE);

        if (gsonToList != null && gsonToList.size() > 0)
            search_labels.setLabels(gsonToList);

    }

    /**
     * 搜索回调
     *
     * @param refresh
     * @param list
     */
    @Override
    public void onSearchResult(boolean refresh, ArrayList<SubscribesListBean.SubscribesMessage> list) {

        if (list != null)
            mAdapter.setList(list);
    }

    /**
     * 标签点击回调
     *
     * @param label
     * @param data
     * @param position
     */
    @Override
    public void onLabelClick(TextView label, Object data, int position) {

        if (!etSearchBox.getText().toString().equals(search_labels.getLabels().get(position).toString())) {
            etSearchBox.setText(search_labels.getLabels().get(position).toString());
            etSearchBox.setSelection(etSearchBox.getText().length());
            getP().search(true, search_labels.getLabels().get(position).toString());
        }


    }
}
