package com.gxdingo.sg.adapter;

import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.gen.DraftBeanDao;
import com.gxdingo.sg.db.CommonDaoUtils;
import com.gxdingo.sg.db.DaoUtilsStore;
import com.gxdingo.sg.db.bean.DraftBean;
import com.gxdingo.sg.utils.DateUtils;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.bean.SubscribesListBean;
import com.gxdingo.sg.utils.TextViewUtils;
import com.kikis.commnlibrary.view.RoundAngleImageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.blankj.utilcode.util.TimeUtils.getNowMills;
import static com.blankj.utilcode.util.TimeUtils.string2Millis;
import static com.gxdingo.sg.db.SqlUtils.EQUAL;
import static com.gxdingo.sg.db.SqlUtils.WHERE;
import static com.gxdingo.sg.utils.DateUtils.IsToday;
import static com.gxdingo.sg.utils.DateUtils.IsYesterday;
import static com.gxdingo.sg.utils.DateUtils.dealDateFormat;
import static com.gxdingo.sg.utils.LocalConstant.LOGIN_WAY;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.Constant.error;
import static com.kikis.commnlibrary.utils.Constant.isDebug;
import static com.kikis.commnlibrary.utils.DateUtils.getCustomDate;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * 商家主页IM消息适配器
 *
 * @author JM
 */
public class StoreHomeIMMessageAdapter extends BaseQuickAdapter<SubscribesListBean.SubscribesMessage, BaseViewHolder> {

    private CommonDaoUtils<DraftBean> mDraftUtils;


    public StoreHomeIMMessageAdapter() {
        super(R.layout.module_item_store_home_im_message);

        DaoUtilsStore mStore = DaoUtilsStore.getInstance();

        mDraftUtils = mStore.getDratfUtils();

    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, SubscribesListBean.SubscribesMessage subscribesMessage) {
        RoundAngleImageView nivAvatar = baseViewHolder.findView(R.id.niv_avatar);
        TextView tvUnreadMsgCount = baseViewHolder.findView(R.id.tv_unread_msg_count);
        TextView tvNickname = baseViewHolder.findView(R.id.tv_nickname);
        TextView tvContent = baseViewHolder.findView(R.id.tv_content);
        TextView tvTime = baseViewHolder.findView(R.id.tv_time);
        TextView store_tab_tv = baseViewHolder.findView(R.id.store_tab_tv);
        TextView draft_tag_tv = baseViewHolder.findView(R.id.draft_tag_tv);


        store_tab_tv.setVisibility(UserInfoUtils.getInstance().getUserInfo().getRole() == 11 ? View.VISIBLE : View.GONE);

        if (UserInfoUtils.getInstance().getUserInfo().getRole() == 11) {
            if (subscribesMessage.getSendUserRole() == 10) {
                //用户
                store_tab_tv.setTextColor(getc(R.color.green_dominant_tone));
                store_tab_tv.setBackgroundResource(R.drawable.module_border_green_round8);
                store_tab_tv.setText("客户");
            } else if (subscribesMessage.getSendUserRole() == 11) {
                //商家
                store_tab_tv.setTextColor(getc(R.color.yellow_tag));
                store_tab_tv.setBackgroundResource(R.drawable.module_border_yellow_round8);
                store_tab_tv.setText("商家");
            } else if (subscribesMessage.getSendUserRole() == 12) {
                //商家
                store_tab_tv.setTextColor(getc(R.color.blue_text));
                store_tab_tv.setBackgroundResource(R.drawable.module_border_blue_round8);
                store_tab_tv.setText("客服");
            }
        }


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

        DraftBean draftBean = mDraftUtils.queryByQueryBuilderUnique(DraftBeanDao.Properties.Uuid.eq(subscribesMessage.getShareUuid()));

        //草稿
        if (draftBean != null && !isEmpty(draftBean.draft)) {

            draft_tag_tv.setVisibility(View.VISIBLE);

            tvContent.setText(draftBean.draft);

        } else
            draft_tag_tv.setVisibility(View.GONE);

        tvTime.setText(getCustomDate(string2Millis(dealDateFormat(subscribesMessage.getUpdateTime())), getNowMills()));

        tvUnreadMsgCount.setVisibility(subscribesMessage.getUnreadNum() > 0 ? View.VISIBLE : View.INVISIBLE);
        tvUnreadMsgCount.setText(String.valueOf(subscribesMessage.getUnreadNum()));
        tvNickname.setText(subscribesMessage.getSendNickname());

        if (draftBean == null || isEmpty(draftBean.draft)) {
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

    }

    private RequestOptions getRequestOptions() {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.ic_user_default_avatar);    //加载成功之前占位图
        options.error(R.mipmap.ic_user_default_avatar);    //加载错误之后的错误图
        return options;
    }
}
