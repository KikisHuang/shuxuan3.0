package com.gxdingo.sg.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.BusinessDistrictMessageAdapter;
import com.gxdingo.sg.bean.BankcardBean;
import com.gxdingo.sg.bean.BusinessDistrictListBean;
import com.gxdingo.sg.bean.BusinessDistrictMessageCommentListBean;
import com.gxdingo.sg.biz.BusinessDistrictMessageContract;
import com.gxdingo.sg.dialog.BusinessDistrictCommentInputBoxDialogFragment;
import com.gxdingo.sg.dialog.SgConfirm2ButtonPopupView;
import com.gxdingo.sg.presenter.BusinessDistrictMessagePresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商圈消息
 *
 * @author JM
 */
public class BusinessDistrictMessageActivity extends BaseMvpActivity<BusinessDistrictMessageContract.BusinessDistrictMessagePresenter>
        implements BusinessDistrictMessageContract.BusinessDistrictMessageListener {


    BusinessDistrictMessageAdapter mMessageAdapter;
    @BindView(R.id.title_layout)
    TemplateTitle titleLayout;
    @BindView(R.id.tv_right_button)
    TextView tvRightButton;
    @BindView(R.id.tv_right_image_button)
    ImageView tvRightImageButton;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.hint_img)
    ImageView hintImg;
    @BindView(R.id.hint_tv)
    TextView hintTv;
    @BindView(R.id.function_bt)
    TextView functionBt;
    //    @BindView(R.id.classics_footer)
//    ClassicsFooter classicsFooter;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    @BindView(R.id.nodata_layout)
    View nodataLayout;

    @Override
    protected BusinessDistrictMessageContract.BusinessDistrictMessagePresenter createPresenter() {
        return new BusinessDistrictMessagePresenter();
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
        return R.layout.module_activity_business_district_message;
    }

    @Override
    protected boolean refreshEnable() {
        return true;
    }

    @Override
    protected boolean loadmoreEnable() {
        return true;
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        //重置适配器相关参数
        if (mMessageAdapter != null)
            mMessageAdapter.reset();

        //获取消息评论列表
        getP().getMessageCommentList(true);
    }

    /**
     * 上拉加载更多数据
     */
    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        //获取消息评论列表
        getP().getMessageCommentList(false);
    }

    @Override
    protected void init() {
        titleLayout.setTitleTextSize(16);
        titleLayout.setTitleText("我的消息");
        tvRightButton.setText("清空");
        tvRightButton.setVisibility(View.VISIBLE);
        tvRightImageButton.setVisibility(View.GONE);

        mMessageAdapter = new BusinessDistrictMessageAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(reference.get()));
        recyclerView.setAdapter(mMessageAdapter);
        mMessageAdapter.setOnItemClickListener((adapter, view, position) -> {
            BusinessDistrictMessageCommentListBean.Comment comment = (BusinessDistrictMessageCommentListBean.Comment) adapter.getItem(position);
            showCommentInputBoxDialog(comment.getReplyNickname(), comment.getId());
        });
        mMessageAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            //点击评论列表下面的回复列表中的某一条
            if (view.getId() == R.id.ll_message_reply_content_layout) {
                new XPopup.Builder(reference.get())
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .isDarkTheme(false)
                        .dismissOnTouchOutside(false)
                        .asCustom(new SgConfirm2ButtonPopupView(reference.get(), "确定要删除该条评论回复？", "", () -> {
                            BusinessDistrictMessageCommentListBean.Reply reply = (BusinessDistrictMessageCommentListBean.Reply) view.getTag();
                            getP().deleteMyOwnComment(reply.getId());
                            view.setTag(null);
                        }))
                        .show();
            }


        });

        //获取消息评论列表
        getP().getMessageCommentList(true);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSucceed(int type) {
        //删除我自己的评论
        if (type == 300) {
            //获取消息评论列表
            getP().getMessageCommentList(true);
        }
    }

    @OnClick(R.id.tv_right_button)
    public void onViewClicked() {
        //获取消息评论列表
        getP().getMessageCommentList(true);
    }


    /**
     * 显示商圈评论弹窗
     *
     * @param hint     提示语，比如回复谁
     * @param parentId 回复谁的消息id
     */
    private void showCommentInputBoxDialog(String hint, long parentId) {


        Bundle bundle = new Bundle();
        bundle.putString(Constant.PARAMAS + 0, hint);

        BusinessDistrictCommentInputBoxDialogFragment fragment = BusinessDistrictCommentInputBoxDialogFragment.newInstance(BusinessDistrictCommentInputBoxDialogFragment.class, bundle);
        fragment.setOnCommentContentListener(object -> {
            String content = (String) object;
            if (TextUtils.isEmpty(content)) {
                onMessage("请输入回复内容！");
                return;
            }
            getP().submitCommentOrReply(parentId, content);
            fragment.dismiss();
        });
        fragment.show(getSupportFragmentManager(), BusinessDistrictMessageActivity.class.toString());

 /*
        mCommentInputBoxPopupView = new BusinessDistrictCommentInputBoxPopupView(this, hint, getSupportFragmentManager()
                , new BusinessDistrictCommentInputBoxPopupView.OnCommentContentListener() {
            @Override
            public void commentContent(Object object) {
                String content = (String) object;
                if (TextUtils.isEmpty(content)) {
                    onMessage("请输入回复内容！");
                    return;
                }
                getP().submitCommentOrReply(parentId, content);
                mCommentInputBoxPopupView.directlyDismiss();
            }
        });

        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .isDarkTheme(false)
                .asCustom(mCommentInputBoxPopupView).show();*/
    }

    /**
     * 商圈消息评论数据
     *
     * @param refresh
     */
    @Override
    public void onMessageCommentData(boolean refresh, BusinessDistrictMessageCommentListBean commentListBean) {
        if (commentListBean != null && commentListBean.getList() != null) {
            if (refresh) {
                mMessageAdapter.setList(commentListBean.getList());
            } else {
                mMessageAdapter.addData(commentListBean.getList());
            }
        }
    }

    /**
     * 提交评论/回复的结果
     */
    @Override
    public void onSubmitCommentOrReplyResult() {
        //重置适配器相关参数
        if (mMessageAdapter != null)
            mMessageAdapter.reset();

        //获取消息评论列表
        getP().getMessageCommentList(true);
    }

}
