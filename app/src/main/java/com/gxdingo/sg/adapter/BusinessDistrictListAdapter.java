package com.gxdingo.sg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gxdingo.sg.R;
import com.gxdingo.sg.fragment.store.StoreBusinessDistrictFragment;
import com.kikis.commnlibrary.view.RoundImageView;
import com.kikis.commnlibrary.view.recycler_view.PullDividerItemDecoration;
import com.kikis.commnlibrary.view.recycler_view.PullGridLayoutManager;
import com.kikis.commnlibrary.view.recycler_view.PullLinearLayoutManager;
import com.kikis.commnlibrary.view.recycler_view.PullRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 用户端和商家端商圈列表适配器
 */
public class BusinessDistrictListAdapter extends PullRecyclerView.PullAdapter<BusinessDistrictListAdapter.ViewHolder> {
    Context mContext;
    PullDividerItemDecoration mSpaceItemDecoration;
    ArrayList<StoreBusinessDistrictFragment.TestValue> mDatas;
    StoreBusinessDistrictFragment.OnPictureClickListener mOnPictureClickListener;
    StoreBusinessDistrictFragment.OnCommentClickListener mOnCommentClickListener;

    public BusinessDistrictListAdapter(Context context, ArrayList<StoreBusinessDistrictFragment.TestValue> datas
            , StoreBusinessDistrictFragment.OnPictureClickListener onPictureClickListener
            , StoreBusinessDistrictFragment.OnCommentClickListener onCommentClickListener) {
        this.mContext = context;
        this.mDatas = datas;
        mSpaceItemDecoration = new PullDividerItemDecoration(mContext, (int) mContext.getResources().getDimension(R.dimen.dp6), (int) mContext.getResources().getDimension(R.dimen.dp6));
        mOnPictureClickListener = onPictureClickListener;
        mOnCommentClickListener = onCommentClickListener;
    }

    @Override
    public int getPullItemCount() {
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size();
    }

    @Override
    public ViewHolder onPullCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.module_item_business_district_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onPullBindViewHolder(ViewHolder viewHolder, int position) {
        StoreBusinessDistrictFragment.TestValue testValue = mDatas.get(position);
        viewHolder.tvStoreName.setText("金源-" + position);

        viewHolder.tvOpenComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnCommentClickListener != null)
                    mOnCommentClickListener.item(v, position, -1, null);
            }
        });

        BusinessDistrictPictureAdapter mBusinessDistrictPictureAdapter = new BusinessDistrictPictureAdapter(mContext);
        viewHolder.rvPicture.removeItemDecoration(mSpaceItemDecoration);
        viewHolder.rvPicture.setStartPulldownAnimation(false);
        viewHolder.rvPicture.setRecyclerViewScrollEnabled(false);
        viewHolder.rvPicture.addItemDecoration(mSpaceItemDecoration);
        viewHolder.rvPicture.setNestedScrollingEnabled(false);
        viewHolder.rvPicture.setLayoutManager(new PullGridLayoutManager(mContext, 3));
        viewHolder.rvPicture.post(new Runnable() {
            @Override
            public void run() {
                viewHolder.rvPicture.setPullAdapter(mBusinessDistrictPictureAdapter);
            }
        });

        BusinessDistrictCommentAdapter mBusinessDistrictCommentAdapter = new BusinessDistrictCommentAdapter(mContext, testValue);
        viewHolder.rvCommentList.setStartPulldownAnimation(false);
        viewHolder.rvCommentList.setRecyclerViewScrollEnabled(false);
        viewHolder.rvCommentList.setNestedScrollingEnabled(false);
        viewHolder.rvCommentList.setLayoutManager(new PullLinearLayoutManager(mContext));

        viewHolder.rvCommentList.setOnItemClickListener(new PullRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int p) {
                if (mOnCommentClickListener != null)
                    mOnCommentClickListener.item(view, position, p, null);
            }
        });
        viewHolder.llCommentUnfoldPutAway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testValue.c = 5;
                mBusinessDistrictCommentAdapter.notifyDataSetChanged();
            }
        });
        viewHolder.rvCommentList.post(new Runnable() {
            @Override
            public void run() {

                viewHolder.rvCommentList.setPullAdapter(mBusinessDistrictCommentAdapter);

            }
        });
    }

    ;

//    public BusinessDistrictListAdapter(Context context) {
//        super(R.layout.module_item_business_district_list);
//        mContext = context;
//        mSpaceItemDecoration = new PullDividerItemDecoration(context, dp2px(4), dp2px(4));
//
//    }
//
//    @Override
//    protected void convert(@NotNull BaseViewHolder baseViewHolder, StoreBusinessDistrictFragment.TestValue o) {
//
//        TextView tvStoreName = baseViewHolder.findView(R.id.tv_store_name);
//        tvStoreName.setText("金源-" + getItemPosition(o));
//        PullRecyclerView rvPicture = baseViewHolder.findView(R.id.rv_picture);
//        PullRecyclerView rvCommentList = baseViewHolder.findView(R.id.rv_comment_list);
//        LinearLayout llCommentUnfoldPutAway = baseViewHolder.findView(R.id.ll_comment_unfold_put_away);
//
//        rvPicture.post(new Runnable() {
//            @Override
//            public void run() {
//                BusinessDistrictPictureAdapter mBusinessDistrictPictureAdapter = new BusinessDistrictPictureAdapter(mContext);
//                rvPicture.removeItemDecoration(mSpaceItemDecoration);
//                rvPicture.setStartPulldownAnimation(false);
//                rvPicture.setRecyclerViewScrollEnabled(false);
//                rvPicture.addItemDecoration(mSpaceItemDecoration);
//                rvPicture.setNestedScrollingEnabled(false);
//                rvPicture.setLayoutManager(new PullGridLayoutManager(getContext(), 3));
//                rvPicture.setPullAdapter(mBusinessDistrictPictureAdapter);
//            }
//        });
//
//        rvCommentList.post(new Runnable() {
//            @Override
//            public void run() {
//                BusinessDistrictCommentAdapter mBusinessDistrictCommentAdapter = new BusinessDistrictCommentAdapter(getContext(), o);
//                rvCommentList.setStartPulldownAnimation(false);
//                rvCommentList.setRecyclerViewScrollEnabled(false);
//                rvCommentList.setNestedScrollingEnabled(false);
//                rvCommentList.setLayoutManager(new PullLinearLayoutManager(getContext()));
//                rvCommentList.setPullAdapter(mBusinessDistrictCommentAdapter);
//                llCommentUnfoldPutAway.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        o.c = 5;
//                        mBusinessDistrictCommentAdapter.notifyDataSetChanged();
//                    }
//                });
//            }
//        });
//
//    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_avatar)
        RoundImageView ivAvatar;
        @BindView(R.id.tv_store_name)
        TextView tvStoreName;
        @BindView(R.id.tv_today_new_products_label)
        TextView tvTodayNewProductsLabel;
        @BindView(R.id.iv_delete)
        ImageView ivDelete;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.rv_picture)
        PullRecyclerView rvPicture;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_comment)
        TextView tvComment;
        @BindView(R.id.tv_open_comment)
        TextView tvOpenComment;
        @BindView(R.id.rv_comment_list)
        PullRecyclerView rvCommentList;
        @BindView(R.id.tv_comment_unfold_put_away)
        TextView tvCommentUnfoldPutAway;
        @BindView(R.id.ll_comment_unfold_put_away)
        LinearLayout llCommentUnfoldPutAway;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
