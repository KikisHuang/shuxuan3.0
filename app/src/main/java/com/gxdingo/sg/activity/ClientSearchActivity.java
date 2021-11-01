package com.gxdingo.sg.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.donkingliang.labels.LabelsView;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.ClientStoreAdapter;
import com.gxdingo.sg.bean.CategoriesBean;
import com.gxdingo.sg.bean.StoreListBean;
import com.gxdingo.sg.biz.ClientHomeContract;
import com.gxdingo.sg.presenter.ClientHomePresenter;
import com.gxdingo.sg.view.ClearEditText;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: Weaving
 * @date: 2021/10/17
 * @page:
 */
public class ClientSearchActivity extends BaseMvpActivity<ClientHomeContract.ClientHomePresenter> implements ClientHomeContract.ClientHomeListener, TextView.OnEditorActionListener, LabelsView.OnLabelClickListener {

    @BindView(R.id.keyword_et)
    public ClearEditText keyword_et;

    @BindView(R.id.history_lv)
    public LabelsView history_lv;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    private ClientStoreAdapter mStoreAdapter;

    @Override
    protected ClientHomeContract.ClientHomePresenter createPresenter() {
        return new ClientHomePresenter();
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
        return R.layout.module_activity_client_search;
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
        mStoreAdapter = new ClientStoreAdapter(1);
        recyclerView.setAdapter(mStoreAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(reference.get()));
        keyword_et.setOnEditorActionListener(this);
        keyword_et.addTextChangedListener(textWatcher);
        history_lv.setOnLabelClickListener(this);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() == 0 && history_lv.getVisibility() == View.GONE){
                history_lv.setVisibility(View.VISIBLE);
                mStoreAdapter.setList(null);
            }
        }
    };

    @Override
    protected void initData() {
        getP().getSearchHistory();
    }

    @OnClick(R.id.btn_cancel)
    public void onClickViews(View v){
        switch (v.getId()){
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    @Override
    public void setDistrict(String district) {

    }

    @Override
    public void onCategoryResult(List<CategoriesBean> categories) {

    }

    @Override
    public void onStoresResult(boolean refresh, List<StoreListBean.StoreBean> storeBeans) {
        if (history_lv.getVisibility() == View.VISIBLE)
            history_lv.setVisibility(View.GONE);
        if (refresh)
            mStoreAdapter.setList(storeBeans);
        else
            mStoreAdapter.addData(storeBeans);
    }

    @Override
    public void onHistoryResult(List<String> searchHistories) {
        history_lv.setLabels(searchHistories);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH){
            getP().search(true,keyword_et.getText().toString());
        }
        return true;
    }

    @Override
    public void onLabelClick(TextView label, Object data, int position) {
        LogUtils.d("search =============" + data.toString());
        keyword_et.setText(data.toString());
        getP().search(true,data.toString());
    }
}
