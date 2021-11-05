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
import com.gxdingo.sg.biz.IMComplaintContract;
import com.gxdingo.sg.presenter.IMComplaintPresenter;
import com.gxdingo.sg.view.SpaceItemDecoration;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.biz.KeyboardHeightObserver;
import com.kikis.commnlibrary.view.TemplateTitle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.blankj.utilcode.util.SizeUtils.dp2px;

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

    @Override
    protected IMComplaintContract.IMComplaintPresenter createPresenter() {
        return new IMComplaintPresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return false;
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
        titleLayout.setTitleText("投诉举报");
        mAdapter = new IMComplaintContentItemAdapter();
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(IMComplaintActivity.this,IMComplaintContentActivity.class);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(reference.get()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(dp2px(10),1));
        recyclerView.setAdapter(mAdapter);
        List<Object> tempData = new ArrayList<>();
        tempData.add(new Object());
        tempData.add(new Object());
        tempData.add(new Object());
        tempData.add(new Object());
        tempData.add(new Object());
        mAdapter.setList(tempData);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onKeyboardHeightChanged(int height, int orientation) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    /**
     * 该方法这里用不到
     */
    @Override
    public void getPhotoDataList(UpLoadBean upLoadBean) {

    }
}
