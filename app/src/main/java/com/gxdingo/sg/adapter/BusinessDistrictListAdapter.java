package com.gxdingo.sg.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.BusinessDistrictListBean;
import com.gxdingo.sg.biz.NineClickListener;
import com.gxdingo.sg.fragment.store.StoreBusinessDistrictFragment;
import com.kikis.commnlibrary.utils.BaseLogUtils;
import com.kikis.commnlibrary.utils.GlideUtils;
import com.kikis.commnlibrary.view.RoundImageView;
import com.kikis.commnlibrary.view.recycler_view.PullDividerItemDecoration;
import com.kikis.commnlibrary.view.recycler_view.PullLinearLayoutManager;
import com.kikis.commnlibrary.view.recycler_view.PullRecyclerView;
import com.lzy.ninegrid.NineGridView;

import org.jetbrains.annotations.NotNull;

import static com.blankj.utilcode.util.ScreenUtils.getScreenWidth;
import static com.blankj.utilcode.util.TimeUtils.getNowMills;
import static com.blankj.utilcode.util.TimeUtils.getNowString;
import static com.blankj.utilcode.util.TimeUtils.string2Millis;
import static com.gxdingo.sg.utils.DateUtils.dealDateFormat;
import static com.gxdingo.sg.utils.LocalConstant.LOGIN_WAY;
import static com.kikis.commnlibrary.utils.BigDecimalUtils.div;
import static com.kikis.commnlibrary.utils.DateUtils.getCustomDate;

/**
 * 用户端和商家端商圈列表适配器
 */
public class BusinessDistrictListAdapter extends BaseQuickAdapter<BusinessDistrictListBean.BusinessDistrict, BaseViewHolder> {
    Context mContext;
    PullDividerItemDecoration mSpaceItemDecoration;
    StoreBusinessDistrictFragment.OnChildViewClickListener mOnChildViewClickListener;
    //消息评论总数
    private int mTotal = 0;

    public BusinessDistrictListAdapter(Context context
            , StoreBusinessDistrictFragment.OnChildViewClickListener onChildViewClickListener) {
        super(R.layout.module_item_business_district_list);
        this.mContext = context;
        mSpaceItemDecoration = new PullDividerItemDecoration(mContext, (int) mContext.getResources().getDimension(R.dimen.dp6), (int) mContext.getResources().getDimension(R.dimen.dp6));
        mOnChildViewClickListener = onChildViewClickListener;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, BusinessDistrictListBean.BusinessDistrict data) {

        TextView tvTime = baseViewHolder.findView(R.id.tv_time);
        ImageView ivDelete = baseViewHolder.findView(R.id.iv_delete);
        RoundImageView ivAvatar = baseViewHolder.findView(R.id.iv_avatar);
        TextView tvStoreName = baseViewHolder.findView(R.id.tv_store_name);
        TextView tvContent = baseViewHolder.findView(R.id.tv_content);
        TextView tvCommentCount = baseViewHolder.findView(R.id.tv_comment_count);
        LinearLayout llCommentLayout = baseViewHolder.findView(R.id.ll_comment_layout);
        TextView tvOpenComment = baseViewHolder.findView(R.id.tv_open_comment);
        NineGridView picture_gridview = baseViewHolder.findView(R.id.picture_gridview);
        ImageView single_img = baseViewHolder.findView(R.id.single_img);

        PullRecyclerView rvCommentList = baseViewHolder.findView(R.id.rv_comment_list);
        LinearLayout llCommentUnfoldPutAwayLayout = baseViewHolder.findView(R.id.ll_comment_unfold_put_away_layout);
        TextView tvCommentUnfoldPutAwayText = baseViewHolder.findView(R.id.tv_comment_unfold_put_away_text);
        TextView client_date_tv = baseViewHolder.findView(R.id.client_date_tv);

        //登录方式，true 用户，false 商家
        boolean isUse = SPUtils.getInstance().getBoolean(LOGIN_WAY);
//        if (isUse) {
//            /**
//             * 如果是用户端则将时间控件移动到删除按钮的位置，并将删除按钮隐藏
//             */
//            ConstraintLayout.LayoutParams params = new ConstraintLayout
//                    .LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
//            params.bottomToBottom = 0;
//            params.topToTop = 0;
//            params.setMargins(0, (int) mContext.getResources().getDimension(R.dimen.dp9), (int) mContext.getResources().getDimension(R.dimen.dp20), 0);
//            tvTime.setLayoutParams(params);
//            ivDelete.setVisibility(View.GONE);
//            client_date_tv.setText();
//        } else {
//            /**
//             * 商家端则给删除按钮点击事件
//             */
//            tvTime.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mOnChildViewClickListener != null)
//                        mOnChildViewClickListener.item(v, getItemPosition(data), -1, null);
//                }
//            });
//        }


        Glide.with(mContext).load(data.getStareAvatar()).apply(getRequestOptions()).into(ivAvatar);

        ivAvatar.setOnClickListener(v -> mOnChildViewClickListener.item(v, getItemPosition(data), getItemPosition(data), data.getStoreId()));

        tvStoreName.setOnClickListener(v -> mOnChildViewClickListener.item(v, getItemPosition(data), getItemPosition(data), data.getStoreId()));

        tvStoreName.setText(data.getStoreName());
        tvContent.setText(data.getContent());
        String createTime = dealDateFormat(data.getCreateTime());
        if (isUse) {
            client_date_tv.setText(getCustomDate(string2Millis(createTime), getNowMills()));
            client_date_tv.setVisibility(View.VISIBLE);
            tvTime.setVisibility(View.GONE);
            ivDelete.setVisibility(View.GONE);
        } else {
            tvTime.setText(getCustomDate(string2Millis(createTime), getNowMills()));
            tvTime.setVisibility(View.VISIBLE);
            ivDelete.setVisibility(View.VISIBLE);
            client_date_tv.setVisibility(View.GONE);
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

        if (data.getImages() != null && data.getImages().size() == 1) {
            single_img.setVisibility(View.VISIBLE);
            picture_gridview.setVisibility(View.GONE);
//            Glide.with(mContext).load(data.getImages().get(0)).apply(GlideUtils.getInstance().getGlideRoundOptions(6).centerCrop()).into(single_img);


            single_img.setOnClickListener(v -> {
                if (mOnChildViewClickListener != null && data.getImages() != null)
                    mOnChildViewClickListener.item(single_img, baseViewHolder.getAdapterPosition(), 0, data.getImages());
            });

            Glide.with(mContext).load(data.getImages().get(0)).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @androidx.annotation.Nullable Transition<? super Drawable> transition) {
                    try {
                        String width = String.valueOf(resource.getIntrinsicWidth());

                        String height = String.valueOf(resource.getIntrinsicHeight());

//                    int newheight = getScreenWidth() * height / width;

                        float ratio = Float.parseFloat(div(width, height, 5));

                        int h = (int) (getScreenWidth() * 1 / 2);

                        single_img.getLayoutParams().width = (int) (h * ratio);

                        single_img.getLayoutParams().height = h;

//                        single_img.setImageDrawable(resource);

                        Glide.with(mContext).load(data.getImages().get(0)).apply(GlideUtils.getInstance().getGlideRoundOptions(6)).into(single_img);

                    } catch (Exception e) {
                        BaseLogUtils.e("图片宽高计算异常 === " + e);
                    }
                }
            });

        } else {


            picture_gridview.setVisibility(View.VISIBLE);
            single_img.setVisibility(View.GONE);

            if (getItemPosition(data) == 0)
                picture_gridview.setVisibility(View.GONE);

            //单张图片宽度
            //picture_gridview.setSingleImageSize((int) (getScreenWidth() * 1.5 / 2));
            //picture_gridview.setSingleImageRatio(0.5625f);

     /*       ArrayList<ImageInfo> imageInfo = new ArrayList<>();

            for (int i = 0; i < data.getImages().size(); i++) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(data.getImages().get(i));
                info.setBigImageUrl(data.getImages().get(i));
                imageInfo.add(info);
            }*/
            if (data.imageInfos != null && data.imageInfos.size() > 0) {
                picture_gridview.setAdapter(new MyNineGridViewClickAdapter(mContext, data.imageInfos, baseViewHolder.getAdapterPosition(), new NineClickListener() {
                    @Override
                    public void onNineGridViewClick(int position, int index) {

                        if (mOnChildViewClickListener != null && data.getImages() != null)
                            mOnChildViewClickListener.item(picture_gridview, position, index, data.getImages());
                    }
                }));
            }
        }


        if (data.getCommentList().size() < 10) {
            //小余10条不显示 展开、收起布局
            llCommentUnfoldPutAwayLayout.setVisibility(View.GONE);
        } else if (data.getCommentList().size() == 10 && data.getCommentList().size() != data.getComments()) {

            llCommentUnfoldPutAwayLayout.setVisibility(View.VISIBLE);
            tvCommentUnfoldPutAwayText.setText("展开更多");
            changeDrawable(tvCommentUnfoldPutAwayText, R.drawable.module_svg_business_district_comment_unfold);

        } else if (data.getCommentList().size() == data.getComments()) {
            //已经显示完所有评论并且超过10条显示收起布局
            llCommentUnfoldPutAwayLayout.setVisibility(View.VISIBLE);
            tvCommentUnfoldPutAwayText.setText("收起");
            changeDrawable(tvCommentUnfoldPutAwayText, R.drawable.module_svg_business_district_comment_put_away);
        }


        tvCommentCount.setText(data.getComments() + "评论");
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

}
