package com.gxdingo.sg.activity;

import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMap;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.ClientAddressAdapter;
import com.gxdingo.sg.bean.AddressBean;
import com.gxdingo.sg.biz.AddressContract;
import com.gxdingo.sg.presenter.AddressPresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.gxdingo.sg.utils.ClientLocalConstant.REFRESH_ADDRESS_LIST;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * @author: Weaving
 * @date: 2021/10/15
 * @page:
 */
public class ClientAddressListActivity extends BaseMvpActivity<AddressContract.AddressPresenter> implements AddressContract.AddressListener, OnItemChildClickListener, OnItemClickListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.nodata_layout)
    public LinearLayout nodata_layout;

    @BindView(R.id.recyclerView)
    public RecyclerView mRecyclerView;

    private ClientAddressAdapter mAdapter;

    //0不返回 1 首页 2聊天
    private int selectType;

    @Override
    protected AddressContract.AddressPresenter createPresenter() {
        return new AddressPresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return true;
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
        return nodata_layout;
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
        return R.layout.module_include_refresh;
    }

    @OnClick(R.id.txt_more)
    public void onClickViews(View v) {
        switch (v.getId()) {
            case R.id.txt_more:
                goToPage(this, ClientNewAddressActivity.class, null);
                break;
        }
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
        selectType = getIntent().getIntExtra(Constant.SERIALIZABLE + 0, 0);
        title_layout.setTitleText(gets(R.string.receiving_address));
        title_layout.setMoreText(gets(R.string.add_address));
        mAdapter = new ClientAddressAdapter();
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(reference.get()));

    }

    @Override
    protected void initData() {
        getP().getAddressList(true);
    }

    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        goToPagePutSerializable(this, ClientNewAddressActivity.class, getIntentEntityMap(new Object[]{false, mAdapter.getData().get(position)}));
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        if (selectType != 0) {
            AddressBean item = (AddressBean) adapter.getItem(position);
            item.selectType = selectType;
            sendEvent(item);
            finish();
        }
    }


    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type == REFRESH_ADDRESS_LIST)
            getP().getAddressList(true);
    }

    @Override
    public void onDataResult(boolean refresh, List<AddressBean> addressBeans) {
        if (refresh)
            mAdapter.setList(addressBeans);
        else
            mAdapter.addData(addressBeans);
    }

    @Override
    public void setAddressData(AddressBean bean) {

    }

    @Override
    public void saveBtnEnable(boolean en) {

    }

    @Override
    public int getAddressId() {
        return 0;
    }

    @Override
    public String getAddress() {
        return null;
    }

    @Override
    public String getAddressDetail() {
        return null;
    }

    @Override
    public String getContact() {
        return null;
    }

    @Override
    public String getMobile() {
        return null;
    }

    @Override
    public String getRegionPath() {
        return null;
    }

    @Override
    public String getLabelString() {
        return null;
    }

    @Override
    public int getGender() {
        return 0;
    }

    @Override
    public LatLonPoint getPoint() {
        return null;
    }

    @Override
    public void showDelDialog() {

    }

    @Override
    public String getDoorplate() {
        return null;
    }

    @Override
    public void setCityName(String cityName) {

    }

    @Override
    public void searchResult(boolean refresh, List<PoiItem> poiItems) {

    }

    @Override
    public AMap getAMap() {
        return null;
    }
}
