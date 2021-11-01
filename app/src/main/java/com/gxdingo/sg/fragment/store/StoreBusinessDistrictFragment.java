package com.gxdingo.sg.fragment.store;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.BusinessDistrictMessageActivity;
import com.gxdingo.sg.activity.StoreBusinessDistrictReleaseActivity;
import com.gxdingo.sg.adapter.BusinessDistrictListAdapter;
import com.gxdingo.sg.biz.StoreBusinessDistrictContract;
import com.gxdingo.sg.dialog.BusinessDistrictCommentInputBoxPopupView;
import com.gxdingo.sg.presenter.StoreBusinessDistrictPresenter;
import com.kikis.commnlibrary.fragment.BaseMvpFragment;
import com.kikis.commnlibrary.utils.ScreenUtils;
import com.kikis.commnlibrary.view.recycler_view.PullDividerItemDecoration;
import com.kikis.commnlibrary.view.recycler_view.PullLinearLayoutManager;
import com.kikis.commnlibrary.view.recycler_view.PullRecyclerView;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.blankj.utilcode.util.SizeUtils.dp2px;

/**
 * 商家端商圈
 *
 * @author JM
 */
public class StoreBusinessDistrictFragment extends BaseMvpFragment<StoreBusinessDistrictContract.StoreBusinessDistrictPresenter> implements StoreBusinessDistrictContract.StoreBusinessDistrictListener {
    Context mContext;

    BusinessDistrictListAdapter mAdapter;

    ArrayList<TestValue> mDatas = new ArrayList<>();
    @BindView(R.id.tv_status_bar)
    LinearLayout tvStatusBar;
    @BindView(R.id.tv_unread_msg_count)
    TextView tvUnreadMsgCount;
    @BindView(R.id.rl_message_list)
    RelativeLayout rlMessageList;
    @BindView(R.id.iv_send_business_district)
    ImageView ivSendBusinessDistrict;
    @BindView(R.id.recyclerView)
    PullRecyclerView recyclerView;

    BusinessDistrictCommentInputBoxPopupView mCommentInputBoxPopupView;


    /**
     * 商圈图片监听接口
     */
    public interface OnPictureClickListener {
        /**
         * 图片item
         *
         * @param view           图片View
         * @param parentPosition 父位置
         * @param position       评论位置
         * @param object         内容实体
         */
        void item(View view, int parentPosition, int position, Object object);
    }

    /**
     * 商圈评论监听接口
     */
    public interface OnCommentClickListener {
        /**
         * 图片item
         *
         * @param view           评论View
         * @param parentPosition 父位置
         * @param position       评论位置
         * @param object         内容实体
         */
        void item(View view, int parentPosition, int position, Object object);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    protected StoreBusinessDistrictContract.StoreBusinessDistrictPresenter createPresenter() {
        return new StoreBusinessDistrictPresenter();
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
    protected int initContentView() {
        return R.layout.module_fragment_store_business_district;
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
    protected boolean refreshEnable() {
        return false;
    }

    @Override
    protected boolean loadmoreEnable() {
        return false;
    }

    @Override
    protected void init() {
        int statusBarHeight = ScreenUtils.getStatusHeight(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
        tvStatusBar.setLayoutParams(params);

        mDatas.add(new TestValue());
        mDatas.add(new TestValue());
        mDatas.add(new TestValue());
        mDatas.add(new TestValue());
        mDatas.add(new TestValue());
        mDatas.add(new TestValue());
        mDatas.add(new TestValue());
        mDatas.add(new TestValue());
        mDatas.add(new TestValue());
        mDatas.add(new TestValue());
        mDatas.add(new TestValue());
        mDatas.add(new TestValue());
        mDatas.add(new TestValue());
        mDatas.add(new TestValue());
        mDatas.add(new TestValue());
        mDatas.add(new TestValue());
        mDatas.add(new TestValue());
        mDatas.add(new TestValue());
        mDatas.add(new TestValue());
        mDatas.add(new TestValue());
        mDatas.add(new TestValue());
        mDatas.add(new TestValue());
        mDatas.add(new TestValue());
        mDatas.add(new TestValue());

        mAdapter = new BusinessDistrictListAdapter(mContext, mDatas, mOnPictureClickListener, mOnCommentClickListener);
        recyclerView.setLayoutManager(new PullLinearLayoutManager(reference.get()));
        recyclerView.addItemDecoration(new PullDividerItemDecoration(mContext, dp2px(10)));
        recyclerView.setPullAdapter(mAdapter);
        recyclerView.setOnItemClickListener(new PullRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });

    }

    @OnClick({R.id.rl_message_list, R.id.iv_send_business_district})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_message_list:
                startActivity(new Intent(mContext, BusinessDistrictMessageActivity.class));
                break;
            case R.id.iv_send_business_district:
                startActivity(new Intent(mContext, StoreBusinessDistrictReleaseActivity.class));
                break;
        }
    }

    /**
     * 商圈图片点击回调
     */
    OnPictureClickListener mOnPictureClickListener = new OnPictureClickListener() {
        @Override
        public void item(View view, int parentPosition, int position, Object object) {

        }
    };

    /**
     * 商圈评论点击回调
     */
    OnCommentClickListener mOnCommentClickListener = new OnCommentClickListener() {

        @Override
        public void item(View view, int parentPosition, int position, Object object) {
            showCommentInputBoxDialog("评论一下");
        }
    };

    @Override
    protected void initData() {

    }

    /**
     * 获取商圈评论输入框弹出窗口View
     *
     * @return
     */
    public View getCommentInputBoxPopupView() {
        return mCommentInputBoxPopupView.getView();
    }

    /**
     * 显示商圈评论弹窗
     */
    private void showCommentInputBoxDialog(String hint) {
        mCommentInputBoxPopupView = new BusinessDistrictCommentInputBoxPopupView(mContext, hint, getFragmentManager()
                , new BusinessDistrictCommentInputBoxPopupView.OnCommentContentListener() {
            @Override
            public void commentContent(Object object) {

            }
        });

        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .isDarkTheme(false)
                .asCustom(mCommentInputBoxPopupView).show();
    }

    public class TestValue {
        public int c = 0;
    }
}
