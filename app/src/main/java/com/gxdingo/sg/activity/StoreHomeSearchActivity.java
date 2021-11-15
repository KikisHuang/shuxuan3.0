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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.StoreHomeIMMessageAdapter;
import com.gxdingo.sg.adapter.StoreHomeSearchResultAdapter;
import com.gxdingo.sg.biz.StoreHomeSearchContract;
import com.gxdingo.sg.presenter.StoreHomeSearchPresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.biz.KeyboardHeightObserver;
import com.kikis.commnlibrary.utils.SystemUtils;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商家主页搜索
 *
 * @author JM
 */
public class StoreHomeSearchActivity extends BaseMvpActivity<StoreHomeSearchContract.StoreHomeSearchPresenter>
        implements StoreHomeSearchContract.StoreHomeSearchListener, KeyboardHeightObserver {

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
    @BindView(R.id.classics_footer)
    ClassicsFooter classicsFooter;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    @BindView(R.id.ll_search_result_layout)
    LinearLayout llSearchResultLayout;

    StoreHomeSearchResultAdapter mAdapter;

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
        return R.layout.module_activity_store_home_search;
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
        etSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    ivEmpty.setVisibility(View.INVISIBLE);
                } else {
                    ivEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etSearchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    //完成自己的事件
                    SystemUtils.hideKeyboard(v);
                    return true;
                }
                return false;
            }
        });

        mAdapter = new StoreHomeSearchResultAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        ArrayList<Object> datas = new ArrayList<>();
        datas.add(new Object());
        datas.add(new Object());
        mAdapter.setList(datas);
    }

    @Override
    protected void initData() {

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

}
