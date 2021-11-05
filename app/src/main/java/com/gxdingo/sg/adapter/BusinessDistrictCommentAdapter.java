package com.gxdingo.sg.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.BusinessDistrictListBean;
import com.gxdingo.sg.utils.EmotionsUtils;
import com.gxdingo.sg.utils.TextViewUtils;
import com.kikis.commnlibrary.utils.BitmapUtils;
import com.kikis.commnlibrary.view.RoundAngleImageView;
import com.kikis.commnlibrary.view.recycler_view.PullRecyclerView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 商圈评论适配器
 *
 * @author JM
 */
public class BusinessDistrictCommentAdapter extends PullRecyclerView.PullAdapter<BusinessDistrictCommentAdapter.CommentViewHolder> {

    Context mContext;
    ArrayList<BusinessDistrictListBean.Comment> mCommentDatas;

    public BusinessDistrictCommentAdapter(Context context, ArrayList<BusinessDistrictListBean.Comment> commentDatas) {
        mContext = context;
        this.mCommentDatas = commentDatas;
    }

    @NonNull
    @Override
    public CommentViewHolder onPullCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.module_item_business_district_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onPullBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        BusinessDistrictListBean.Comment comment = mCommentDatas.get(position);
        //判断该条评论是否某某回复某某
        if (TextUtils.isEmpty(comment.getParentNickname())) {
            holder.tvReplyNickname.setText(comment.getReplyNickname());
            holder.tvReplyFont.setVisibility(View.GONE);
            holder.tvParentNickname.setVisibility(View.GONE);
        } else {
            holder.tvReplyNickname.setText(comment.getReplyNickname());
            holder.tvReplyFont.setVisibility(View.VISIBLE);
            holder.tvParentNickname.setVisibility(View.VISIBLE);
            holder.tvParentNickname.setText(comment.getParentNickname());
        }

        holder.tvContent.setText(TextViewUtils.contentConversion(mContext,comment.getContent()));
    }

    @Override
    public int getPullItemCount() {
        if (mCommentDatas == null) {
            return 0;
        }
        return mCommentDatas.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_reply_nickname)
        TextView tvReplyNickname;
        @BindView(R.id.tv_reply_font)
        TextView tvReplyFont;
        @BindView(R.id.tv_parent_nickname)
        TextView tvParentNickname;
        @BindView(R.id.tv_content)
        TextView tvContent;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
