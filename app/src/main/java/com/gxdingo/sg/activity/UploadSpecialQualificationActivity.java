package com.gxdingo.sg.activity;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMap;
import com.blankj.utilcode.util.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.QualificationAdapter;
import com.gxdingo.sg.adapter.UploadSpecialQualificationAdapter;
import com.gxdingo.sg.bean.StoreAuthInfoBean;
import com.gxdingo.sg.bean.StoreDetail;
import com.gxdingo.sg.biz.ClientStoreContract;
import com.gxdingo.sg.presenter.ClientStorePresenter;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.utils.RxUtil;
import com.kikis.commnlibrary.view.TemplateTitle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * @author: Kikis
 * @date: 2022/3/10
 * @page:
 */
public class UploadSpecialQualificationActivity extends BaseMvpActivity<ClientStoreContract.ClientStorePresenter> implements OnItemChildClickListener, ClientStoreContract.ClientStoreListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    private UploadSpecialQualificationAdapter mAdapter;

    @Override
    protected ClientStoreContract.ClientStorePresenter createPresenter() {
        return new ClientStorePresenter();
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
        return R.layout.module_activity_upload_special_qualification;
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
        title_layout.setTitleText("上传证件");

        recyclerView.setLayoutManager(new LinearLayoutManager(reference.get()));

        mAdapter = new UploadSpecialQualificationAdapter();
        mAdapter.setOnItemChildClickListener(this);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void initData() {
        getP().getStoreQualifications(UserInfoUtils.getInstance().getIdentifier(),true);
    }

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);

    }

    @OnClick({R.id.submit_tv})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;
        switch (v.getId()) {
            case R.id.submit_tv:
                getP().submit(mAdapter.getData());
                break;
        }
    }

    @Override
    public void onSucceed(int type) {
        super.onSucceed(type);
        if (type==100)
            finish();
    }

    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {

        getP().chooseAnAlbum(data -> {
            mAdapter.getData().get(position).setProve(String.valueOf(data));
            mAdapter.getData().get(position).unUpload = true;
            mAdapter.notifyItemChanged(position);
        });

    }

    @Override
    public void onStoreDetailResult(StoreDetail storeDetail) {

    }

    @Override
    public AMap getMap() {
        return null;
    }

    @Override
    public void onQualificationsDataResult(List<StoreAuthInfoBean.CategoryListBean> newData) {
        if (mAdapter != null)
            mAdapter.setList(newData);
    }
}
