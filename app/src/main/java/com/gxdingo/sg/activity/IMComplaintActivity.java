package com.gxdingo.sg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.IMComplaintContentItemAdapter;
import com.gxdingo.sg.bean.UpLoadBean;
import com.gxdingo.sg.bean.WebBean;
import com.gxdingo.sg.biz.IMComplaintContract;
import com.gxdingo.sg.presenter.IMComplaintPresenter;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.view.SpaceItemDecoration;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.biz.KeyboardHeightObserver;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.blankj.utilcode.util.SizeUtils.dp2px;
import static com.gxdingo.sg.utils.LocalConstant.COMPLAINT_SUCCEED;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * IM-投诉举报
 *
 * @author JM
 */
public class IMComplaintActivity extends BaseMvpActivity<IMComplaintContract.IMComplaintPresenter> implements IMComplaintContract.IMComplaintListener, KeyboardHeightObserver {
    @BindView(R.id.title_layout)
    TemplateTitle titleLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.hint_img)
    ImageView hintImg;
    @BindView(R.id.hint_tv)
    TextView hintTv;
    @BindView(R.id.function_bt)
    TextView functionBt;

    IMComplaintContentItemAdapter mAdapter;
    private String identifier;


    @Override
    protected IMComplaintContract.IMComplaintPresenter createPresenter() {
        return new IMComplaintPresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return true;
    }

    @Override
    protected int activityTitleLayout() {
        return R.layout.module_include_new_custom_title;
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
        return R.layout.module_activity_im_complaint;
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

        String sendIdentifier = getIntent().getStringExtra(Constant.SERIALIZABLE + 0);
        int roleId = getIntent().getIntExtra(Constant.SERIALIZABLE + 1, 0);
        String UUID = getIntent().getStringExtra(Constant.SERIALIZABLE + 2);

        identifier = UserInfoUtils.getInstance().getUserInfo().getRole() == 10 ? "shuxuanyonghutousu" : "shuxuanshangjiatousu";

        titleLayout.setTitleText("投诉举报");
        mAdapter = new IMComplaintContentItemAdapter();
        mAdapter.setOnItemClickListener((adapter, view, position) -> goToPagePutSerializable(reference.get(), IMComplaintContentActivity.class, getIntentEntityMap(new Object[]{mAdapter.getData().get(position).getTitle(), sendIdentifier, roleId, UUID})));
        recyclerView.setLayoutManager(new LinearLayoutManager(reference.get()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(dp2px(10), 1));
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void initData() {
        getP().getDataList(identifier);
    }

    @Override
    public void onKeyboardHeightChanged(int height, int orientation) {

    }

    /**
     * 该方法这里用不到
     */
    @Override
    public void getPhotoDataList(UpLoadBean upLoadBean) {

    }

    /**
     * 投诉列表
     *
     * @param list
     */
    @Override
    public void onArticleListResult(List<WebBean> list) {
        mAdapter.setList(list);
    }
    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type==COMPLAINT_SUCCEED)
            finish();
    }
}
