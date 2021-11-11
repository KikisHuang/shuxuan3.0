package com.gxdingo.sg.adapter;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.IMChatHistoryListBean;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.gxdingo.sg.biz.ChatClickListener;
import com.gxdingo.sg.utils.SpanStringUtils;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.utils.emotion.EmotionUtils;
import com.kikis.commnlibrary.adapter.BaseRecyclerAdapter;
import com.kikis.commnlibrary.adapter.RecyclerViewHolder;
import com.kikis.commnlibrary.utils.GlideUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cn.bingoogolapple.progressbar.BGAProgressBar;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.ClipboardUtils.copyText;
import static com.blankj.utilcode.util.TimeUtils.getNowString;
import static com.blankj.utilcode.util.TimeUtils.string2Millis;
import static com.gxdingo.sg.utils.DateUtils.dealDateFormat;
import static com.gxdingo.sg.utils.LocalConstant.OtherAudio;
import static com.gxdingo.sg.utils.LocalConstant.OtherImage;
import static com.gxdingo.sg.utils.LocalConstant.OtherText;
import static com.gxdingo.sg.utils.LocalConstant.OtherTransfer;
import static com.gxdingo.sg.utils.LocalConstant.SelfAudio;
import static com.gxdingo.sg.utils.LocalConstant.SelfImage;
import static com.gxdingo.sg.utils.LocalConstant.SelfText;
import static com.gxdingo.sg.utils.LocalConstant.SelfTransfer;
import static com.kikis.commnlibrary.utils.MyToastUtils.customToast;

/**
 * @author: Kikis
 * @date: 2021/11/10
 * @page:
 */
public class ChatAdapter extends BaseRecyclerAdapter {

    //显示日期时间间隔（单位毫秒）
    private int showDateInterval = 900000;

    private ChatClickListener chatClickListener;

    private IMChatHistoryListBean.MyAvatarInfo mMyAvatarInfo;

    private IMChatHistoryListBean.OtherAvatarInfo mOtherAvatarInfo;

    public ChatAdapter(List list) {
        super(list);
    }

    public ChatAdapter(List list, IMChatHistoryListBean data, ChatClickListener chatClickListener) {
        super(list);

        if (data.getMyAvatarInfo() != null)
            this.mMyAvatarInfo = data.getMyAvatarInfo();
        if (data.getOtherAvatarInfo() != null)
            this.mOtherAvatarInfo = data.getOtherAvatarInfo();

        this.chatClickListener = chatClickListener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemViewType(int position) {
        ReceiveIMMessageBean data = (ReceiveIMMessageBean) getData().get(position);

        String myIdentifier = UserInfoUtils.getInstance().getIdentifier();//自己的Identifier

        //是否自己
        boolean self = isEmpty(data.getSendIdentifier())||myIdentifier.equals(data.getSendIdentifier());

        // genre 消息类型 0=文本 1=图片 2=语音 3=视频 4=商品 5=订单
        //消息类型 0=文本 1=表情 10=图片 11=语音 12=视频 20=转账 21=收款 30=定位位置信息
        if (data.getType() == 0 || data.getType() == 1) {
            if (self)
                return SelfText;
            else
                return OtherText;
        } else if (data.getType() == 10) {
            if (self)
                return SelfImage;
            else
                return OtherImage;
        } else if (data.getType() == 11) {
            if (self)
                return SelfAudio;
            else
                return OtherAudio;
        } else if (data.getType() == 20 || data.getType() == 21) {
            if (self)
                return SelfTransfer;
            else
                return OtherTransfer;
        }

        return SelfText;
    }

    @Override
    public int getItemLayoutId(int viewType) {

        if (viewType == SelfText)
            return R.layout.module_item_chat_self_text;
        else if (viewType == OtherText)
            return R.layout.module_item_chat_other_text;
        else if (viewType == SelfImage)
            return R.layout.module_item_chat_self_img;
        else if (viewType == OtherImage)
            return R.layout.module_item_chat_other_img;
        else if (viewType == OtherAudio)
            return R.layout.module_item_chat_other_audio;
        else if (viewType == SelfAudio)
            return R.layout.module_item_chat_self_audio;
        else if (viewType == SelfTransfer)
            return R.layout.module_item_chat_self_transfer;
        else if (viewType == OtherTransfer)
            return R.layout.module_item_chat_other_transfer;

        return -1;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, Object item) {
        if (getItemViewType(position) == -1) return;

        ReceiveIMMessageBean data = (ReceiveIMMessageBean) item;

        genericViewDataInit(position, data, holder);

        if (getItemViewType(position) == SelfText || getItemViewType(position) == OtherText) {

                TextView content = holder.getTextView(R.id.content_tv);
                if (!StringUtils.isEmpty(data.getContent())) {
                    content.setText(SpanStringUtils.getEmotionContent(EmotionUtils.EMOTION_CLASSIC_TYPE,
                            mContext, data.getContent()));
                }
                content.setOnLongClickListener(v -> {
                    copyText(content.getText().toString());
                    customToast("已复制到剪贴板");
                    return false;
                });
        }

        //图片内容加载
        if (getItemViewType(position) == OtherImage || getItemViewType(position) == SelfImage) {
            //上传进度
            if (getItemViewType(position) == SelfImage)
                ImgProgressBarInit(data, holder);

            ImageView content_img = holder.getImageView(R.id.content_img);

            Glide.with(mContext)
                    .load(!isEmpty(data.getContent()) ? data.getContent() : R.drawable.ic_default_image)
                    .apply(GlideUtils.getInstance().getGlideRoundOptions(3))
                    .placeholder(R.drawable.module_shape_bg_graydd)
                    .into(content_img);

            content_img.setOnClickListener(v -> {

                if (chatClickListener != null)
                    chatClickListener.onImageClick(data.getContent());
            });
        }

        //语音类型
        if (getItemViewType(position) == OtherAudio || getItemViewType(position) == SelfAudio) {

            LinearLayout voice_ll = holder.getLinearLayout(R.id.voice_ll);
            TextView tv_voice_second = holder.getTextView(R.id.tv_voice_second);

            tv_voice_second.setText(String.valueOf(data.getVoiceDuration()));

            voice_ll.setOnClickListener(v -> {
                //todo 语音播放动画联动
                if (chatClickListener != null)
                    //语音播放
                    chatClickListener.onAudioClick(data.getContent());
            });

        }

        //转账类型
        if (getItemViewType(position) == SelfTransfer || getItemViewType(position) == OtherTransfer) {

            ReceiveIMMessageBean.MsgAccounts msgAccounts = data.getMsgAccounts();

            ConstraintLayout cl_transfer_accounts_bg = (ConstraintLayout) holder.getView(R.id.cl_transfer_accounts_bg);
            TextView amount_tv = holder.getTextView(R.id.amount_tv);
            TextView status_tv = holder.getTextView(R.id.status_tv);
            TextView transfer_type_name_tv = holder.getTextView(R.id.transfer_type_name_tv);
            ImageView iv_transfer_accounts_type_icon = holder.getImageView(R.id.iv_transfer_accounts_type_icon);

            if (msgAccounts != null) {
                cl_transfer_accounts_bg.setVisibility(View.VISIBLE);
                amount_tv.setText(msgAccounts.getAmount().toString());
                //0=未付款；1=待领取；2=已收款；3=拒绝收款；4=过期退回
                if (msgAccounts.getPayType() == 1) {
                    status_tv.setText("待领取");
                    cl_transfer_accounts_bg.getBackground().setAlpha(255);
                } else if (msgAccounts.getPayType() == 2) {
                    status_tv.setText("已收款");
                    cl_transfer_accounts_bg.getBackground().setAlpha(100);
                } else if (msgAccounts.getPayType() == 3) {
                    status_tv.setText("拒绝收款");
                    cl_transfer_accounts_bg.getBackground().setAlpha(100);
                } else if (msgAccounts.getPayType() == 4) {
                    status_tv.setText("过期退回");
                    cl_transfer_accounts_bg.getBackground().setAlpha(100);
                }
                //转账方支付类型。10=微信,20=支付宝
                if (msgAccounts.getPayType() == 10) {
                    transfer_type_name_tv.setText("微信转账");
                    iv_transfer_accounts_type_icon.setImageResource(R.drawable.module_im_chat_transfer_accounts_type_wechat_icon);
                } else if (msgAccounts.getPayType() == 20) {
                    transfer_type_name_tv.setText("支付宝转账");
                    iv_transfer_accounts_type_icon.setImageResource(R.drawable.module_im_chat_transfer_accounts_type_alipay_icon);
                }
            } else {
                cl_transfer_accounts_bg.setVisibility(View.INVISIBLE);
            }

        }
    }

    /**
     * 通用view数据初始化
     *
     * @param position
     * @param data
     * @param holder
     */
    private void genericViewDataInit(int position, ReceiveIMMessageBean data, RecyclerViewHolder holder) {
        //头像
        ImageView avatar_img = holder.getImageView(R.id.avatar_img);

        String avatarUrl = "";
        if (getItemViewType(position) == SelfText || getItemViewType(position) == SelfImage || getItemViewType(position) == SelfAudio || getItemViewType(position) == SelfTransfer) {
            if (mMyAvatarInfo != null && !isEmpty(mMyAvatarInfo.getSendAvatar()))
                avatarUrl = mMyAvatarInfo.getSendAvatar();
        } else {
            if (mOtherAvatarInfo != null && !isEmpty(mOtherAvatarInfo.getSendAvatar()))
                avatarUrl = mOtherAvatarInfo.getSendAvatar();
        }
        Glide.with(mContext).load(avatarUrl).apply(GlideUtils.getInstance().getGlideRoundOptions(6)).into(avatar_img);


        TextView time_tv = holder.getTextView(R.id.time_tv);

        if (!isEmpty(data.getCreateTime()))
        time_tv.setText(dealDateFormat(data.getCreateTime()));

        if (position == 0)
            time_tv.setVisibility(View.VISIBLE);
        else {
            ReceiveIMMessageBean temp = (ReceiveIMMessageBean) mData.get(position - 1);

            String t = dealDateFormat(temp.getCreateTime());

            String d = dealDateFormat(data.getCreateTime());

            if (!isEmpty(t) && !isEmpty(d)) {
                if (t.length() <= 8)
                    t = getNowString(new SimpleDateFormat("yyyy-MM-dd ", Locale.getDefault())) + t;
                if (d.length() <= 8)
                    d = getNowString(new SimpleDateFormat("yyyy-MM-dd ", Locale.getDefault())) + d;

                //上一次的发送时间
                long lasttime = string2Millis(t);
                //本次消息的发送时间
                long nowtime = string2Millis(d);

                //如果上次发送消息的时间超过当前消息900秒(15分钟)才重新显示，否则不显示时间。
                if ((nowtime - lasttime) > showDateInterval) {
                    time_tv.setVisibility(View.VISIBLE);
                } else
                    time_tv.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 图片上传进度通用方法
     *
     * @param data
     * @param holder
     */
    private void ImgProgressBarInit(ReceiveIMMessageBean data, RecyclerViewHolder holder) {
        FrameLayout shadow_bg_layout = (FrameLayout) holder.getView(R.id.shadow_bg_layout);

        //只有content为空时才是本地上传图片的操作
        shadow_bg_layout.setVisibility(data.upload_progress > 0 && data.upload_progress < 100 ? View.VISIBLE : View.GONE);

        BGAProgressBar circle_progress_bar = (BGAProgressBar) holder.getView(R.id.circle_progress_bar);
        circle_progress_bar.setProgress(data.upload_progress);
    }

}
