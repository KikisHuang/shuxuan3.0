package com.gxdingo.sg.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
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
import com.gxdingo.sg.model.OneKeyModel;
import com.gxdingo.sg.presenter.ClientHomePresenter;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.view.ClearEditText;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.bean.AddressBean;
import com.kikis.commnlibrary.utils.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * @author: Weaving
 * @date: 2021/10/17
 * @page:
 */
public class ClientSearchActivity extends BaseMvpActivity<ClientHomeContract.ClientHomePresenter> implements ClientHomeContract.ClientHomeListener, TextView.OnEditorActionListener, LabelsView.OnLabelClickListener {

    @BindView(R.id.location_tv)
    public TextView location_tv;

    @BindView(R.id.keyword_et)
    public ClearEditText keyword_et;

    @BindView(R.id.history_lv)
    public LabelsView history_lv;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

//    @BindView(R.id.ll_nearby_store)
//    public LinearLayout ll_nearby_store;

//    @BindView(R.id.recommend_store_rv)
//    public RecyclerView recommend_store_rv;

    private ClientStoreAdapter mStoreAdapter;

    private String location;

    private boolean searchModel;

    @Override
    protected ClientHomeContract.ClientHomePresenter createPresenter() {
        return new ClientHomePresenter();
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
        location = getIntent().getStringExtra(Constant.PARAMAS+0);
        if (!isEmpty(location))
            location_tv.setText(location);
        mStoreAdapter = new ClientStoreAdapter();
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
                getP().getNearbyStore(true,false,0);
            }
        }
    };

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
        if (object instanceof AddressBean){
            AddressBean addressBean = (AddressBean) object;
            if (addressBean.selectType==1){
                getP().search((AddressBean) object,keyword_et.getText().toString());
            }

        }
    }

    @Override
    protected void initData() {
        getP().getSearchHistory();
        getP().checkPermissions(getRxPermissions(),false);
    }

    @OnClick({R.id.btn_cancel,R.id.location_tv})
    public void onClickViews(View v){
        switch (v.getId()){
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.location_tv:
                if (UserInfoUtils.getInstance().isLogin())
                    goToPagePutSerializable(reference.get(), ClientAddressListActivity.class,getIntentEntityMap(new Object[]{1}));
                else
                   getP().oauth(this);
                break;
        }
    }

    @Override
    public void setDistrict(String district) {
        location_tv.setText(district);
    }

    @Override
    public void onCategoryResult(List<CategoriesBean> categories) {

    }

    @Override
    public void onStoresResult(boolean refresh, boolean searchModel,List<StoreListBean.StoreBean> storeBeans) {
        if (searchModel){
            if (history_lv.getVisibility() == View.VISIBLE)
                history_lv.setVisibility(View.GONE);
            if (refresh)
                mStoreAdapter.setList(storeBeans);
            else
                mStoreAdapter.addData(storeBeans);
        }else {
            mStoreAdapter.addData(storeBeans);
        }
//        if (history_lv.getVisibility() == View.VISIBLE)
//            history_lv.setVisibility(View.GONE);
//        if (refresh)
//            mStoreAdapter.setList(storeBeans);
//        else
//            mStoreAdapter.addData(storeBeans);
    }

    @Override
    public void onHistoryResult(List<String> searchHistories) {
        history_lv.setLabels(searchHistories);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH){
            searchModel = true;
            getP().search(true,true,keyword_et.getText().toString());
        }
        return true;
    }

    @Override
    public void onLabelClick(TextView label, Object data, int position) {
        LogUtils.d("search =============" + data.toString());
        keyword_et.setText(data.toString());
        getP().search(true,true,data.toString());
    }
}
