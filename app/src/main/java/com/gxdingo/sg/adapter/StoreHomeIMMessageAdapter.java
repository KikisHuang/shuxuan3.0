package com.gxdingo.sg.adapter;

import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.utils.DateUtils;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.bean.SubscribesListBean;
import com.gxdingo.sg.utils.TextViewUtils;
import com.kikis.commnlibrary.view.RoundAngleImageView;

import org.jetbrains.annotations.NotNull;

import static com.blankj.utilcode.util.TimeUtils.getNowMills;
import static com.blankj.utilcode.util.TimeUtils.string2Millis;
import static com.gxdingo.sg.utils.DateUtils.IsToday;
import static com.gxdingo.sg.utils.DateUtils.IsYesterday;
import static com.gxdingo.sg.utils.DateUtils.dealDateFormat;
import static com.gxdingo.sg.utils.LocalConstant.LOGIN_WAY;
import static com.kikis.commnlibrary.utils.DateUtils.getCustomDate;

/**
 * 商家主页IM消息适配器
 *
 * @author JM
 */
public class StoreHomeIMMessageAdapter extends BaseQuickAdapter<SubscribesListBean.SubscribesMessage, BaseViewHolder> {

    private boolean isUser = false;

    public StoreHomeIMMessageAdapter() {
        super(R.layout.module_item_store_home_im_message);
        isUser = SPUtils.getInstance().getBoolean(LOGIN_WAY, true);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, SubscribesListBean.SubscribesMessage subscribesMessage) {
        RoundAngleImageView nivAvatar = baseViewHolder.findView(R.id.niv_avatar);
        TextView tvUnreadMsgCount = baseViewHolder.findView(R.id.tv_unread_msg_count);
        TextView tvNickname = baseViewHolder.findView(R.id.tv_nickname);
        TextView tvContent = baseViewHolder.findView(R.id.tv_content);
        TextView tvTime = baseViewHolder.findView(R.id.tv_time);
        TextView store_tab_tv = baseViewHolder.findView(R.id.store_tab_tv);
        //todo module_border_blue_round8 module_border_green_round8 module_border_yellow_round8 标签显示


        Glide.with(getContext()).load(subscribesMessage.getSendAvatar()).apply(getRequestOptions()).into(nivAvatar);

        String date = DateUtils.dealDateFormat(subscribesMessage.getUpdateTime(), "yyyy-MM-dd HH:mm");

/*
        if (IsToday(date))
            tvTime.setText(dealDateFormat(subscribesMessage.getUpdateTime(), "HH:mm"));
        else if (IsYesterday(date))
            tvTime.setText("昨天" + dealDateFormat(subscribesMessage.getUpdateTime(), "HH:mm"));
        else
            tvTime.setText(date);
*/

        tvTime.setText(getCustomDate(string2Millis(dealDateFormat(subscribesMessage.getUpdateTime())), getNowMills()));

        tvUnreadMsgCount.setVisibility(subscribesMessage.getUnreadNum() > 0 ? View.VISIBLE : View.INVISIBLE);
        tvUnreadMsgCount.setText(String.valueOf(subscribesMessage.getUnreadNum()));
        tvNickname.setText(subscribesMessage.getSendNickname());
        if (subscribesMessage.getLastMsgType() == 0) {
            tvContent.setText(TextViewUtils.contentConversion(getContext(), subscribesMessage.getLastMsg()));
        } else if (subscribesMessage.getLastMsgType() == 1) {
            tvContent.setText("[表情]");
        } else if (subscribesMessage.getLastMsgType() == 10) {
            tvContent.setText("[图片]");
        } else if (subscribesMessage.getLastMsgType() == 11) {
            tvContent.setText("[语音]");
        } else if (subscribesMessage.getLastMsgType() == 12) {
            tvContent.setText("[视频]");
        } else if (subscribesMessage.getLastMsgType() == 20) {
            tvContent.setText("[转账]");
        } else if (subscribesMessage.getLastMsgType() == 21) {
            tvContent.setText("[收款]");
        } else if (subscribesMessage.getLastMsgType() == 30) {
            tvContent.setText("[位置]");
        }
    }

    private RequestOptions getRequestOptions() {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.ic_user_default_avatar);    //加载成功之前占位图
        options.error(R.mipmap.ic_user_default_avatar);    //加载错误之后的错误图
        return options;
    }
}
