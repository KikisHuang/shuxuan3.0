package com.gxdingo.sg.adapter;

import android.view.View;
import android.widget.ImageView;
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
import com.kikis.commnlibrary.utils.GlideUtils;
import com.kikis.commnlibrary.view.RoundAngleImageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.blankj.utilcode.util.TimeUtils.getNowMills;
import static com.blankj.utilcode.util.TimeUtils.string2Date;
import static com.gxdingo.sg.utils.DateUtils.dealDateFormat;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.DateUtils.showTimeText;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * 商家主页IM消息适配器
 *
 * @author JM
 */
public class IMMessageAdapter extends BaseQuickAdapter<SubscribesListBean.SubscribesMessage, BaseViewHolder> {

    private CommonDaoUtils<DraftBean> mDraftUtils;

    private boolean mIsForward;

    public IMMessageAdapter() {
        super(R.layout.module_item_store_home_im_message);

        DaoUtilsStore mStore = DaoUtilsStore.getInstance();

        mDraftUtils = mStore.getDratfUtils();

    }

    public IMMessageAdapter(boolean isforward) {
        super(R.layout.module_item_store_home_im_message);

        DaoUtilsStore mStore = DaoUtilsStore.getInstance();

        mDraftUtils = mStore.getDratfUtils();
        mIsForward = isforward;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, SubscribesListBean.SubscribesMessage subscribesMessage) {
        ImageView nivAvatar = baseViewHolder.findView(R.id.niv_avatar);
        TextView tvUnreadMsgCount = baseViewHolder.findView(R.id.tv_unread_msg_count);
        TextView tvNickname = baseViewHolder.findView(R.id.tv_nickname);
        TextView tvContent = baseViewHolder.findView(R.id.tv_content);
        TextView tvTime = baseViewHolder.findView(R.id.tv_time);
        TextView draft_tag_tv = baseViewHolder.findView(R.id.draft_tag_tv);
        ImageView settop_img = baseViewHolder.findView(R.id.settop_img);
        ImageView v_img = baseViewHolder.findView(R.id.v_img);

        //转发页面
        if (mIsForward) {
            tvUnreadMsgCount.setVisibility(View.GONE);
            draft_tag_tv.setVisibility(View.GONE);
        } else {
            tvUnreadMsgCount.setVisibility(View.VISIBLE);
            draft_tag_tv.setVisibility(View.VISIBLE);
        }

        v_img.setVisibility(subscribesMessage.avatarIcon == 1 ? View.VISIBLE : View.GONE);

        if (!mIsForward)
            settop_img.setVisibility(subscribesMessage.sort > 0 ? View.VISIBLE : View.GONE);
        else
            settop_img.setVisibility(View.GONE);

        Glide.with(getContext()).load(!isEmpty(subscribesMessage.getSendAvatar())?subscribesMessage.getSendAvatar(): R.drawable.module_svg_client_default_avatar).apply(GlideUtils.getInstance().getGlideRoundOptions(6)).into(nivAvatar);

        DraftBean draftBean = mDraftUtils.queryByQueryBuilderUnique(DraftBeanDao.Properties.Uuid.eq(subscribesMessage.getShareUuid()));
        //转发列表不显示
        if (!mIsForward) {
            //草稿
            if (draftBean != null && !isEmpty(draftBean.draft)) {

                draft_tag_tv.setVisibility(View.VISIBLE);

                tvContent.setText(draftBean.draft);

            } else
                draft_tag_tv.setVisibility(View.GONE);
        }

        tvTime.setText(showTimeText(string2Date(dealDateFormat(subscribesMessage.getUpdateTime()))));

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
