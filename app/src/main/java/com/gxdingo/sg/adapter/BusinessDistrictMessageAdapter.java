package com.gxdingo.sg.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.BusinessDistrictMessageCommentListBean;
import com.gxdingo.sg.utils.EmotionsUtils;
import com.gxdingo.sg.utils.TextViewUtils;
import com.kikis.commnlibrary.utils.BitmapUtils;
import com.kikis.commnlibrary.utils.DateUtils;
import com.kikis.commnlibrary.view.RoundAngleImageView;
import com.kikis.commnlibrary.view.RoundImageView;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.blankj.utilcode.util.TimeUtils.getNowMills;
import static com.blankj.utilcode.util.TimeUtils.string2Millis;
import static com.gxdingo.sg.utils.DateUtils.dealDateFormat;
import static com.kikis.commnlibrary.utils.DateUtils.getCustomDate;

/**
 * 商圈消息适配器
 *
 * @author JM
 */
public class BusinessDistrictMessageAdapter extends BaseQuickAdapter<BusinessDistrictMessageCommentListBean.Comment, BaseViewHolder> {

    int mFirstReadPosition = -1;//第一条已读的评论位置

    public BusinessDistrictMessageAdapter() {
        super(R.layout.module_item_business_district_message);
    }

    /**
     * 重置
     */
    public void reset() {
        mFirstReadPosition = -1;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, BusinessDistrictMessageCommentListBean.Comment comment) {

        LinearLayout llHaveReadLayout = baseViewHolder.findView(R.id.ll_have_read_layout);
        RoundImageView ivAvatar = baseViewHolder.findView(R.id.iv_avatar);
        TextView tvStoreName = baseViewHolder.findView(R.id.tv_store_name);
        TextView tvContent = baseViewHolder.findView(R.id.tv_content);
        RoundAngleImageView ivPicture = baseViewHolder.findView(R.id.iv_picture);

        if (comment.getParentRead() == 1 && mFirstReadPosition == -1) {
            mFirstReadPosition = getItemPosition(comment);
        }
        llHaveReadLayout.setVisibility(mFirstReadPosition == getItemPosition(comment) ? View.VISIBLE : View.GONE);

        Glide.with(getContext()).load(comment.getUserAvatar()).apply(getRequestOptions()).into(ivAvatar);
        tvStoreName.setText(comment.getReplyNickname());
        tvContent.setText(TextViewUtils.contentConversion(getContext(), comment.getContent()));

        Glide.with(getContext()).load(comment.getCircleImage()).into(ivPicture);

        if (!TextUtils.isEmpty(comment.getCreateTime()))
            ((TextView) baseViewHolder.findView(R.id.tv_time)).setText(getCustomDate(string2Millis(dealDateFormat(comment.getCreateTime())), getNowMills()));

        //回复评论的内容
        RecyclerView recyclerView = baseViewHolder.findView(R.id.rv_reply_content);
        BusinessDistrictMessageReplyContentAdapter mMyReplyaListAdapter = new BusinessDistrictMessageReplyContentAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mMyReplyaListAdapter);
        mMyReplyaListAdapter.setList(comment.getMyReplyList());
        mMyReplyaListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            BusinessDistrictMessageCommentListBean.Reply reply = (BusinessDistrictMessageCommentListBean.Reply) adapter.getItem(position);
            view.setTag(reply);//通过tag传递回复对象
            setOnItemChildClick(view, position);//将回复评论内容列表的点击事件传递给父适配器
        });

    }

    private RequestOptions getRequestOptions() {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.ic_user_default_avatar);    //加载成功之前占位图
        options.error(R.mipmap.ic_user_default_avatar);    //加载错误之后的错误图
        return options;
    }

}
