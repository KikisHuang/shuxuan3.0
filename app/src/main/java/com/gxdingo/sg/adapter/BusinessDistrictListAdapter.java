package com.gxdingo.sg.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.BusinessDistrictListBean;
import com.gxdingo.sg.biz.NineClickListener;
import com.gxdingo.sg.fragment.child.BusinessDistrictFragment;
import com.kikis.commnlibrary.utils.BaseLogUtils;
import com.kikis.commnlibrary.utils.GlideUtils;
import com.kikis.commnlibrary.view.RoundImageView;
import com.kikis.commnlibrary.view.recycler_view.PullDividerItemDecoration;
import com.kikis.commnlibrary.view.recycler_view.PullLinearLayoutManager;
import com.kikis.commnlibrary.view.recycler_view.PullRecyclerView;
import com.lzy.ninegrid.NineGridView;

import org.jetbrains.annotations.NotNull;

import static com.blankj.utilcode.util.ConvertUtils.dp2px;
import static com.blankj.utilcode.util.ScreenUtils.getScreenWidth;
import static com.blankj.utilcode.util.TimeUtils.getNowString;
import static com.blankj.utilcode.util.TimeUtils.string2Date;
import static com.blankj.utilcode.util.TimeUtils.string2Millis;
import static com.gxdingo.sg.utils.DateUtils.dealDateFormat;
import static com.kikis.commnlibrary.utils.BigDecimalUtils.div;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.CommonUtils.getd;
import static com.kikis.commnlibrary.utils.DateUtils.showTimeText;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * 用户端和商家端商圈列表适配器
 */
public class BusinessDistrictListAdapter extends BaseQuickAdapter<BusinessDistrictListBean.BusinessDistrict, BaseViewHolder> {
    Context mContext;
    PullDividerItemDecoration mSpaceItemDecoration;
    BusinessDistrictFragment.OnChildViewClickListener mOnChildViewClickListener;
    //单张图片宽度
    private int singleWidth = 0;

    private int mType;

    public BusinessDistrictListAdapter(Context context
            , BusinessDistrictFragment.OnChildViewClickListener onChildViewClickListener, int mType) {
        super(R.layout.module_item_business_district_list);
        this.mContext = context;
        singleWidth = (int) (getScreenWidth() * 1 / 3);
        mSpaceItemDecoration = new PullDividerItemDecoration(mContext, (int) mContext.getResources().getDimension(R.dimen.dp6), (int) mContext.getResources().getDimension(R.dimen.dp6));
        mOnChildViewClickListener = onChildViewClickListener;
        this.mType = mType;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, BusinessDistrictListBean.BusinessDistrict data) {

        TextView tvTime = baseViewHolder.findView(R.id.tv_time);
        ImageView ivDelete = baseViewHolder.findView(R.id.iv_delete);
        RecyclerView icon_rcy = baseViewHolder.findView(R.id.icon_rcy);
        RoundImageView ivAvatar = baseViewHolder.findView(R.id.iv_avatar);
        TextView tvStoreName = baseViewHolder.findView(R.id.tv_store_name);
        TextView tvContent = baseViewHolder.findView(R.id.tv_content);
        TextView tvCommentCount = baseViewHolder.findView(R.id.tv_comment_count);
        LinearLayout llCommentLayout = baseViewHolder.findView(R.id.ll_comment_layout);
        TextView tvOpenComment = baseViewHolder.findView(R.id.tv_open_comment);
        TextView like_tv = baseViewHolder.findView(R.id.like_tv);
        TextView share_tv = baseViewHolder.findView(R.id.share_tv);
        NineGridView picture_gridview = baseViewHolder.findView(R.id.picture_gridview);

        PullRecyclerView rvCommentList = baseViewHolder.findView(R.id.rv_comment_list);
        LinearLayout llCommentUnfoldPutAwayLayout = baseViewHolder.findView(R.id.ll_comment_unfold_put_away_layout);
        TextView tvCommentUnfoldPutAwayText = baseViewHolder.findView(R.id.tv_comment_unfold_put_away_text);
        TextView distance_tv = baseViewHolder.findView(R.id.distance_tv);

        TextView one_label = baseViewHolder.findView(R.id.one_label);
        TextView two_label = baseViewHolder.findView(R.id.two_label);


        if (data.labels != null && data.labels.size() > 0) {
            if (data.labels.size() > 1) {
                one_label.setVisibility(View.VISIBLE);
                two_label.setVisibility(View.VISIBLE);

                GradientDrawable mGrad = (GradientDrawable) one_label.getBackground();
                mGrad.setColor(getc(R.color.white));
                mGrad.setStroke(dp2px(1), Color.parseColor(data.labels.get(0).getColor()));


                GradientDrawable mGrad1 = (GradientDrawable) two_label.getBackground();
                mGrad1.setColor(getc(R.color.white));
                mGrad1.setStroke(dp2px(1), Color.parseColor(data.labels.get(1).getColor()));

                one_label.setTextColor(Color.parseColor(data.labels.get(0).getColor()));
                two_label.setTextColor(Color.parseColor(data.labels.get(1).getColor()));

                if (!isEmpty(data.labels.get(0).getName()))
                    one_label.setText(data.labels.get(0).getName());

                if (!isEmpty(data.labels.get(1).getName()))
                    two_label.setText(data.labels.get(1).getName());

            } else {
                one_label.setVisibility(View.VISIBLE);

                GradientDrawable mGrad = (GradientDrawable) one_label.getBackground();
                mGrad.setColor(getc(R.color.white));
                mGrad.setStroke(dp2px(1), Color.parseColor(data.labels.get(0).getColor()));

                one_label.setTextColor(Color.parseColor(data.labels.get(0).getColor()));

                if (!isEmpty(data.labels.get(0).getName()))
                    one_label.setText(data.labels.get(0).getName());

                two_label.setVisibility(View.GONE);
            }


        } else {
            one_label.setVisibility(View.GONE);
            two_label.setVisibility(View.GONE);
        }


        if (!isEmpty(data.liked) && !data.liked.equals("0"))
            like_tv.setText(data.liked);
        else
            like_tv.setText("");

        setAlertLeftIcon(like_tv, data.likedStatus == 0 ? getd(R.drawable.module_svg_unlike_heart) : getd(R.drawable.module_svg_like_heart));

        like_tv.setOnClickListener(v -> {
            // 0 未点赞/取消赞 1已点赞
            if (mOnChildViewClickListener != null)
                mOnChildViewClickListener.item(v, getItemPosition(data), getItemPosition(data), data.likedStatus == 0 ? 1 : 0);
        });
        share_tv.setOnClickListener(v -> {
            if (mOnChildViewClickListener != null)
                mOnChildViewClickListener.item(v, getItemPosition(data), getItemPosition(data), data.forwardingUrl);
        });

        Glide.with(mContext).load(data.getAvatar()).apply(getRequestOptions()).into(ivAvatar);

        if (data.iconList != null && data.iconList.size() > 0) {
            if (icon_rcy.getAdapter() != null && icon_rcy.getAdapter().getItemCount() > 0)
                icon_rcy.removeAllViews();

            icon_rcy.setVisibility(View.VISIBLE);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            icon_rcy.setLayoutManager(linearLayoutManager);

            ShopsIconAdapter adapter = new ShopsIconAdapter();
            adapter.setList(data.iconList);
            icon_rcy.setAdapter(adapter);
        }else
            icon_rcy.setVisibility(View.GONE);


        ivAvatar.setOnClickListener(v -> mOnChildViewClickListener.item(v, getItemPosition(data), getItemPosition(data), data.circleUserIdentifier));

        tvStoreName.setOnClickListener(v -> mOnChildViewClickListener.item(v, getItemPosition(data), getItemPosition(data), data.circleUserIdentifier));

        if (!isEmpty(data.getNickName()))
            tvStoreName.setText(data.getNickName());

        if (!isEmpty(data.getContent()))
            tvContent.setText(data.getContent());

        String createTime = dealDateFormat(data.getCreateTime());

        tvTime.setText(showTimeText(string2Date(createTime)));

        if (!isEmpty(data.distance))
            distance_tv.setText(data.distance);

        //用户端ui布局显示
        if (mType != 2) {
            distance_tv.setVisibility(View.VISIBLE);
            ivDelete.setVisibility(View.GONE);
        } else {
            //商家端ui布局显示
            ivDelete.setVisibility(View.VISIBLE);

            distance_tv.setVisibility(View.GONE);
        }

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnChildViewClickListener != null)
                    mOnChildViewClickListener.item(v, getItemPosition(data), -1, null);
            }
        });


        //删除商圈
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnChildViewClickListener != null)
                    mOnChildViewClickListener.item(v, getItemPosition(data), -1, null);
            }
        });


        /**
         * 图片列表
         */

        picture_gridview.setVisibility(data.getImages() != null && data.getImages().size() > 0 ? View.VISIBLE : View.GONE);

        if (data.getImages() != null && data.getImages().size() > 0) {

            if (data.imageInfos != null && data.imageInfos.size() > 0) {
                picture_gridview.setAdapter(new MyNineGridViewClickAdapter(mContext, data.imageInfos, getItemPosition(data), new NineClickListener() {
                    @Override
                    public void onNineGridViewClick(int position, int index) {

                        if (mOnChildViewClickListener != null && data.getImages() != null)
                            mOnChildViewClickListener.item(picture_gridview, position, index, data.getImages());
                    }
                }));
            }

            if (data.getImages().size() == 1) {

                Glide.with(mContext).load(data.getImages().get(0)).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @androidx.annotation.Nullable Transition<? super Drawable> transition) {
                        try {
                            String width = String.valueOf(resource.getIntrinsicWidth());

                            String height = String.valueOf(resource.getIntrinsicHeight());

                            float ratio = Float.parseFloat(div(width, height, 5));

                            if (resource.getIntrinsicWidth() > resource.getIntrinsicHeight())
                                //单张图片宽度
                                picture_gridview.setSingleImageSize((int) (getScreenWidth() * 1.2 / 2));
                            else
                                picture_gridview.setSingleImageSize(singleWidth);

                            //比例
                            picture_gridview.setSingleImageRatio(ratio);

                            if (picture_gridview.getImageViewsSize() > 0) {
                                Glide.with(mContext).load(resource)//
                                        .apply(GlideUtils.getInstance().getGlideRoundOptions(6).placeholder(R.drawable.load_faile_icon))
                                        .into(picture_gridview.getImageView(0));
                            }
//                            notifyItemChanged(getItemPosition(data));

                        } catch (Exception e) {
                            BaseLogUtils.e("图片宽高计算异常 === " + e);
                        }
                    }
                });
            }


        }


        if (data.getCommentList().size() < 10) {
            //小余10条不显示 展开、收起布局
            llCommentUnfoldPutAwayLayout.setVisibility(View.GONE);
        } else if (data.getCommentList().size() >= 10 && data.getCommentList().size() != data.getComments()) {

            llCommentUnfoldPutAwayLayout.setVisibility(View.VISIBLE);
            tvCommentUnfoldPutAwayText.setText("展开更多");
            changeDrawable(tvCommentUnfoldPutAwayText, R.drawable.module_svg_business_district_comment_unfold);

        } else if (data.getCommentList().size() == data.getComments()) {
            //已经显示完所有评论并且超过10条显示收起布局
            llCommentUnfoldPutAwayLayout.setVisibility(View.VISIBLE);
            tvCommentUnfoldPutAwayText.setText("收起");
            changeDrawable(tvCommentUnfoldPutAwayText, R.drawable.module_svg_business_district_comment_put_away);
        }

        if (data.getComments() > 0)
            tvCommentCount.setText(data.getComments() + "");
        else
            tvCommentCount.setText("");

        //点击评论数量展开评论列表
        tvCommentCount.setOnClickListener(v -> {

            //评论展开状态是初始状态并且布局是隐藏的并且评论数大于等于10的，回调给界面那边请求“展开评论”接口获取评论数据
            if (data.getCommentOpen() == 0 && llCommentLayout.getVisibility() == View.GONE && data.getComments() >= 10) {
                //只在初始化的时候执行一次，获取更多展开评论则通过点击展开评论文本控件
                if (mOnChildViewClickListener != null)
                    mOnChildViewClickListener.item(v, getItemPosition(data), -1, tvCommentUnfoldPutAwayText);//将tvCommentUnfoldPutAwayText传递到界面去
            }

            if (llCommentLayout.getVisibility() == View.VISIBLE) {
                llCommentLayout.setVisibility(View.GONE);
                data.setCommentOpen(2);//关闭状态
            } else {
                llCommentLayout.setVisibility(View.VISIBLE);
                data.setCommentOpen(1);//打开状态
            }
        });
        //点击展开更多/收起
        llCommentUnfoldPutAwayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果是“收起”标题则执行隐藏评论布局，反之就回调给界面请求展开评论接口
                if (tvCommentUnfoldPutAwayText.getText().toString().equals("收起")) {
                    data.setCommentOpen(2);//关闭状态
                    llCommentLayout.setVisibility(View.GONE);

                } else if (tvCommentUnfoldPutAwayText.getText().toString().equals("展开更多")) {
                    if (mOnChildViewClickListener != null)
                        mOnChildViewClickListener.item(v, getItemPosition(data), -1, tvCommentUnfoldPutAwayText);//将tvCommentUnfoldPutAwayText传递到界面去
                }
            }
        });

        /**
         * 评论列表展开状态
         */
        if (data.getCommentOpen() == 0) {//初始化状态
            //0条评论或者评论大于10条的时候不展开显示评论
            if (data.getComments() == 0 || data.getComments() >= 10) {
                llCommentLayout.setVisibility(View.GONE);
            } else {
                llCommentLayout.setVisibility(View.VISIBLE);
            }
        } else if (data.getCommentOpen() == 1) {//开状态
            llCommentLayout.setVisibility(View.VISIBLE);
        } else if (data.getCommentOpen() == 2) {//关状态
            llCommentLayout.setVisibility(View.GONE);
        }

        //评论一下
        tvOpenComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnChildViewClickListener != null)
                    mOnChildViewClickListener.item(v, getItemPosition(data), -1, null);
            }
        });

        /**
         * 评论列表
         */
        BusinessDistrictCommentAdapter mBusinessDistrictCommentAdapter = new BusinessDistrictCommentAdapter(mContext, data.getCommentList());
        rvCommentList.setStartPulldownAnimation(false);
        rvCommentList.setRecyclerViewScrollEnabled(false);
        rvCommentList.setNestedScrollingEnabled(false);
        rvCommentList.setLayoutManager(new PullLinearLayoutManager(mContext));

        rvCommentList.setOnItemClickListener((view, p) -> {
            if (mOnChildViewClickListener != null && data.getCommentList() != null)
                mOnChildViewClickListener.item(rvCommentList, getItemPosition(data), p, null);
        });
        rvCommentList.setPullAdapter(mBusinessDistrictCommentAdapter);

    }


    private RequestOptions getRequestOptions() {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.ic_user_default_avatar);    //加载成功之前占位图
        options.error(R.mipmap.ic_user_default_avatar);    //加载错误之后的错误图
        return options;
    }

    /**
     * 更换TextView图标
     *
     * @param textView
     * @param resId
     */
    private void changeDrawable(TextView textView, int resId) {
        Drawable drawable = mContext.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, null, drawable, null);
    }


    /**
     * @param @param drw
     * @return void
     * @desc 设置左边图标
     */
    public void setAlertLeftIcon(TextView tv, Drawable drw) {
        drw.setBounds(0, 0, drw.getMinimumWidth(), drw.getMinimumHeight());
        tv.setCompoundDrawables(null, null, drw, null);
    }

}
