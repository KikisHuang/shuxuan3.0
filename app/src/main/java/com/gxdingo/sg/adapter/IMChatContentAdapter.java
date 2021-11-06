package com.gxdingo.sg.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.IMChatActivity;
import com.gxdingo.sg.bean.IMChatHistoryListBean;
import com.gxdingo.sg.bean.ReceiveIMMessageBean;
import com.gxdingo.sg.utils.EmotionsUtils;
import com.gxdingo.sg.utils.TextViewUtils;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.utils.BitmapUtils;
import com.kikis.commnlibrary.view.RoundAngleImageView;
import com.kikis.commnlibrary.view.recycler_view.PullRecyclerView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * IM聊天内容适配器
 *
 * @author JM
 */
public class IMChatContentAdapter extends PullRecyclerView.PullAdapter<IMChatContentAdapter.IMChatContentViewHolder> {
    private Context mContext;
    private ArrayList<ReceiveIMMessageBean> mMessageDatas;
    private IMChatActivity.OnIMChatUICallbackListener mOnIMChatUICallbackListener;
    private int mCurrPlayVoiceIndex = -1;//当前播放语音的索引
    private IMChatHistoryListBean.MyAvatarInfo mMyAvatarInfo;//自己头像信息
    private IMChatHistoryListBean.OtherAvatarInfo mOtherAvatarInfo;//对方头像信息

    public IMChatContentAdapter(Context context, ArrayList<ReceiveIMMessageBean> messageDatas
            , IMChatActivity.OnIMChatUICallbackListener onIMChatUICallbackListener) {
        mContext = context;
        mMessageDatas = messageDatas;
        mOnIMChatUICallbackListener = onIMChatUICallbackListener;
    }

    /**
     * 设置头像信息
     *
     * @param myAvatarInfo    自己
     * @param otherAvatarInfo 对方
     */
    public void setAvatar(IMChatHistoryListBean.MyAvatarInfo myAvatarInfo, IMChatHistoryListBean.OtherAvatarInfo otherAvatarInfo) {
        mMyAvatarInfo = myAvatarInfo;
        mOtherAvatarInfo = otherAvatarInfo;
    }

    @Override
    public int getPullItemCount() {
        if (mMessageDatas == null) {
            return 0;
        }
        return mMessageDatas.size();
    }

    @Override
    public int getPullItemViewType(int position) {
        int viewType = mMessageDatas.get(position).getType();
        return viewType;
    }

    @Override
    public IMChatContentViewHolder onPullCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = null;
        IMChatContentViewHolder viewHolder = null;

        if (viewType == 0) {
            //文字
            view = LayoutInflater.from(mContext).inflate(R.layout.module_item_im_chat_text, viewGroup, false);
            viewHolder = new TextViewHolder(view);
        } else if (viewType == 10) {
            //图片
            view = LayoutInflater.from(mContext).inflate(R.layout.module_item_im_chat_picture, viewGroup, false);
            viewHolder = new PictureViewHolder(view);
        } else if (viewType == 20 || viewType == 21) {
            //转账
            view = LayoutInflater.from(mContext).inflate(R.layout.module_item_im_chat_transfer_accounts, viewGroup, false);
            viewHolder = new TransferAccountsViewHolder(view);
        } else if (viewType == 11) {
            //语音
            view = LayoutInflater.from(mContext).inflate(R.layout.module_item_im_chat_voice, viewGroup, false);
            viewHolder = new VoiceViewHolder(view);
        } else {
            //未知，一般用来处理低版本APP收到高版本APP的新视图类型时显示（即旧版本没有该视图时）
            view = LayoutInflater.from(mContext).inflate(R.layout.module_item_im_chat_unknown_content, viewGroup, false);
            viewHolder = new UnknownContentViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onPullBindViewHolder(IMChatContentViewHolder contentViewHolder, int position) {
        ReceiveIMMessageBean messageBean = mMessageDatas.get(position);

        String myIdentifier = UserInfoUtils.getInstance().getIdentifier();//自己的Identifier
        String sendIdentifier = messageBean.getSendIdentifier();//消息发送方的Identifier
        boolean isOneself = false;

        if (!TextUtils.isEmpty(myIdentifier) && !TextUtils.isEmpty(sendIdentifier))
            isOneself = sendIdentifier.equals(myIdentifier);//判断是自己还是对方

        contentViewHolder.setOneselfOrOtherView(isOneself);//设置是自己还是对方视图
        contentViewHolder.setTime(messageBean.getCreateTime());//设置时间
        if (isOneself) {
            if (mMyAvatarInfo != null)
                contentViewHolder.setAvatar(mMyAvatarInfo.getSendAvatar());//设置头像
        } else {
            if (mOtherAvatarInfo != null)
                contentViewHolder.setAvatar(mOtherAvatarInfo.getSendAvatar());//设置头像
        }

        //文字视图
        if (contentViewHolder instanceof TextViewHolder) {
            TextViewHolder textViewHolder = (TextViewHolder) contentViewHolder;
            textViewHolder.tvSendText.setText(TextViewUtils.contentConversion(mContext, messageBean.getContent()));
        }
        //图片视图
        else if (contentViewHolder instanceof PictureViewHolder) {
            PictureViewHolder pictureHolder = (PictureViewHolder) contentViewHolder;
            Glide.with(mContext).load(messageBean.getContent()).apply(getRequestOptions()).into(pictureHolder.ivPicture);
        }
        //转账视图
        else if (contentViewHolder instanceof TransferAccountsViewHolder) {
            ReceiveIMMessageBean.MsgAccounts msgAccounts = messageBean.getMsgAccounts();
            TransferAccountsViewHolder transferAccountsHolder = (TransferAccountsViewHolder) contentViewHolder;
            if (msgAccounts != null) {
                transferAccountsHolder.clTransferAccountsBg.setVisibility(View.VISIBLE);
                transferAccountsHolder.tvAmount.setText(msgAccounts.getAmount().toString());
                //0=未付款；1=待领取；2=已收款；3=拒绝收款；4=过期退回
                if (msgAccounts.getPayType() == 1) {
                    transferAccountsHolder.tvReceivePaymentStatus.setText("待领取");
                    transferAccountsHolder.clTransferAccountsBg.getBackground().setAlpha(255);
                } else if (msgAccounts.getPayType() == 2) {
                    transferAccountsHolder.tvReceivePaymentStatus.setText("已收款");
                    transferAccountsHolder.clTransferAccountsBg.getBackground().setAlpha(100);
                } else if (msgAccounts.getPayType() == 3) {
                    transferAccountsHolder.tvReceivePaymentStatus.setText("拒绝收款");
                    transferAccountsHolder.clTransferAccountsBg.getBackground().setAlpha(100);
                } else if (msgAccounts.getPayType() == 4) {
                    transferAccountsHolder.tvReceivePaymentStatus.setText("过期退回");
                    transferAccountsHolder.clTransferAccountsBg.getBackground().setAlpha(100);
                }
                //转账方支付类型。10=微信,20=支付宝
                if (msgAccounts.getPayType() == 10) {
                    transferAccountsHolder.tvTransferAccountsTypeName.setText("微信转账");
                    transferAccountsHolder.ivTransferAccountsTypeIcon.setImageResource(R.drawable.module_im_chat_transfer_accounts_type_wechat_icon);
                } else if (msgAccounts.getPayType() == 20) {
                    transferAccountsHolder.tvTransferAccountsTypeName.setText("支付宝转账");
                    transferAccountsHolder.ivTransferAccountsTypeIcon.setImageResource(R.drawable.module_im_chat_transfer_accounts_type_alipay_icon);
                }
            } else {
                transferAccountsHolder.clTransferAccountsBg.setVisibility(View.INVISIBLE);
            }
        }
        //语音视图
        else if (contentViewHolder instanceof VoiceViewHolder) {
            VoiceViewHolder voiceViewHolder = (VoiceViewHolder) contentViewHolder;
            voiceViewHolder.tvVoiceSecond.setText(String.valueOf(messageBean.getVoiceDuration()));
            voiceViewHolder.llVoiceBg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (voiceViewHolder.mVoiceScrollingAnimation.isRunning()) {
                        mCurrPlayVoiceIndex = -1;
                        voiceViewHolder.mVoiceScrollingAnimation.stop();//停止播放语音动画
                    } else {
                        mCurrPlayVoiceIndex = position;
                        voiceViewHolder.mVoiceScrollingAnimation.start();//启动播放语音动画
                    }
                    notifyDataSetChanged();//刷新界面
                    if (mOnIMChatUICallbackListener != null) {
                        mOnIMChatUICallbackListener.onCallback(((VoiceViewHolder) contentViewHolder).itemView, position, messageBean.getType(), messageBean.getContent());
                    }
                }
            });

            AnimationDrawable mVoiceScrollingAnimation = voiceViewHolder.mVoiceScrollingAnimation;
            if (mVoiceScrollingAnimation != null) {
                //是否是当前播放的语音，是就继续动画不是就停止
                if (mCurrPlayVoiceIndex != position) {
                    mVoiceScrollingAnimation.stop();//停止播放语音动画
                } else {
                    mVoiceScrollingAnimation.start();//启动播放语音动画
                }
            }
        }
        //未知内容视图
        else if (contentViewHolder instanceof UnknownContentViewHolder) {
            UnknownContentViewHolder unknownContentViewHolder = (UnknownContentViewHolder) contentViewHolder;
        }

    }


    private RequestOptions getRequestOptions() {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.ic_user_default_avatar);    //加载成功之前占位图
        options.error(R.mipmap.ic_user_default_avatar);    //加载错误之后的错误图
        return options;
    }

    /**
     * 父ViewHolder
     */
    public abstract class IMChatContentViewHolder extends RecyclerView.ViewHolder {
        RoundAngleImageView ivAvatar;
        TextView tvMessageTime;

        public IMChatContentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessageTime = itemView.findViewById(R.id.tv_message_time);
        }

        /**
         * 设置是自己还是对方视图
         */
        public abstract void setOneselfOrOtherView(boolean isOneself);

        /**
         * 设置时间，空则占位隐藏（必须在抽象方法setOneselfOrOtherView后调用）
         */
        public void setTime(String time) {
            if (tvMessageTime != null) {
                tvMessageTime.setVisibility(TextUtils.isEmpty(time) ? View.INVISIBLE : View.VISIBLE);
                tvMessageTime.setText(time);
            }
        }

        /**
         * 设置头像（必须在抽象方法setOneselfOrOtherView后调用）
         */
        public void setAvatar(String avatar) {
            if (ivAvatar != null) {
                Glide.with(mContext).load(avatar).apply(getRequestOptions()).into(ivAvatar);
            }
        }
    }

    /**
     * 文字视图
     */
    public class TextViewHolder extends IMChatContentViewHolder {
        View itemView;
        TextView tvSendText;

        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        @Override
        public void setOneselfOrOtherView(boolean isOneself) {
            ConstraintLayout clOneselfSendTextLayout = itemView.findViewById(R.id.cl_oneself_send_text_layout);
            RoundAngleImageView nivOneselfAvatar = itemView.findViewById(R.id.niv_oneself_avatar);
            TextView tvOneselfSendText = itemView.findViewById(R.id.tv_oneself_send_text);

            ConstraintLayout clOthersSendTextLayout = itemView.findViewById(R.id.cl_others_send_text_layout);
            RoundAngleImageView nivOthersAvatar = itemView.findViewById(R.id.niv_others_avatar);
            TextView tvOthersSendText = itemView.findViewById(R.id.tv_others_send_text);

            //内容提供者是自己就显示自己发送文字布局反之显示他人发送文字布局
            if (isOneself) {
                clOneselfSendTextLayout.setVisibility(View.VISIBLE);
                clOthersSendTextLayout.setVisibility(View.GONE);

                ivAvatar = nivOneselfAvatar;
                tvSendText = tvOneselfSendText;
            } else {
                clOneselfSendTextLayout.setVisibility(View.GONE);
                clOthersSendTextLayout.setVisibility(View.VISIBLE);

                ivAvatar = nivOthersAvatar;
                tvSendText = tvOthersSendText;
            }
        }
    }

    /**
     * 图片视图
     */
    public class PictureViewHolder extends IMChatContentViewHolder {
        View itemView;
        RoundAngleImageView ivPicture;

        public PictureViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        @Override
        public void setOneselfOrOtherView(boolean isOneself) {
            ConstraintLayout clOneselfSendPictureLayout = itemView.findViewById(R.id.cl_oneself_send_picture_layout);
            RoundAngleImageView ivOneselfAvatar = itemView.findViewById(R.id.iv_oneself_avatar);
            RoundAngleImageView ivOneselfPicture = itemView.findViewById(R.id.iv_oneself_picture);

            ConstraintLayout clOthersSendPictureLayout = itemView.findViewById(R.id.cl_others_send_picture_layout);
            RoundAngleImageView ivOthersAvatar = itemView.findViewById(R.id.iv_others_avatar);
            RoundAngleImageView ivOthersPicture = itemView.findViewById(R.id.iv_others_picture);

            //内容提供者是自己就显示自己发送文字布局反之显示他人发送文字布局
            if (isOneself) {
                clOneselfSendPictureLayout.setVisibility(View.VISIBLE);
                clOthersSendPictureLayout.setVisibility(View.GONE);

                ivAvatar = ivOneselfAvatar;
                ivPicture = ivOneselfPicture;
            } else {
                clOneselfSendPictureLayout.setVisibility(View.GONE);
                clOthersSendPictureLayout.setVisibility(View.VISIBLE);

                ivAvatar = ivOthersAvatar;
                ivPicture = ivOthersPicture;
            }
        }
    }

    /**
     * 转账视图
     */
    public class TransferAccountsViewHolder extends IMChatContentViewHolder {
        View itemView;
        ConstraintLayout clTransferAccountsBg;
        TextView tvAmount;
        TextView tvReceivePaymentStatus;
        ImageView ivTransferAccountsTypeIcon;
        TextView tvTransferAccountsTypeName;

        public TransferAccountsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        @Override
        public void setOneselfOrOtherView(boolean isOneself) {
            ConstraintLayout clOneselfSendTransferAccountsLayout = itemView.findViewById(R.id.cl_oneself_send_transfer_accounts_layout);
            ConstraintLayout clOneselfTransferAccountsBg = itemView.findViewById(R.id.cl_oneself_transfer_accounts_bg);
            RoundAngleImageView ivOneselfAvatar = itemView.findViewById(R.id.niv_oneself_avatar);
            TextView tvOneselfAmount = itemView.findViewById(R.id.tv_oneself_amount);
            TextView tvOneselfReceivePaymentStatus = itemView.findViewById(R.id.tv_oneself_receive_payment_status);
            ImageView ivOneselfTransferAccountsTypeIcon = itemView.findViewById(R.id.iv_oneself_transfer_accounts_type_icon);
            TextView tvOneselfTransferAccountsTypeName = itemView.findViewById(R.id.tv_oneself_transfer_accounts_type_name);

            ConstraintLayout clOthersSendTransferAccountsLayout = itemView.findViewById(R.id.cl_others_send_transfer_accounts_layout);
            ConstraintLayout clOthersTransferAccountsBg = itemView.findViewById(R.id.cl_others_transfer_accounts_bg);
            RoundAngleImageView ivOthersAvatar = itemView.findViewById(R.id.niv_others_avatar);
            TextView tvOthersAmount = itemView.findViewById(R.id.tv_others_amount);
            TextView tvOthersReceivePaymentStatus = itemView.findViewById(R.id.tv_others_receive_payment_status);
            ImageView ivOthersTransferAccountsTypeIcon = itemView.findViewById(R.id.iv_others_transfer_accounts_type_icon);
            TextView tvOthersTransferAccountsTypeName = itemView.findViewById(R.id.tv_others_transfer_accounts_type_name);

            //内容提供者是自己就显示自己发送文字布局反之显示他人发送文字布局
            if (isOneself) {
                clOneselfSendTransferAccountsLayout.setVisibility(View.VISIBLE);
                clOthersSendTransferAccountsLayout.setVisibility(View.GONE);

                clTransferAccountsBg = clOneselfTransferAccountsBg;
                ivAvatar = ivOneselfAvatar;
                tvAmount = tvOneselfAmount;
                tvReceivePaymentStatus = tvOneselfReceivePaymentStatus;
                ivTransferAccountsTypeIcon = ivOneselfTransferAccountsTypeIcon;
                tvTransferAccountsTypeName = tvOneselfTransferAccountsTypeName;
            } else {
                clOneselfSendTransferAccountsLayout.setVisibility(View.GONE);
                clOthersSendTransferAccountsLayout.setVisibility(View.VISIBLE);

                clTransferAccountsBg = clOthersTransferAccountsBg;
                ivAvatar = ivOthersAvatar;
                tvAmount = tvOthersAmount;
                tvReceivePaymentStatus = tvOthersReceivePaymentStatus;
                ivTransferAccountsTypeIcon = ivOthersTransferAccountsTypeIcon;
                tvTransferAccountsTypeName = tvOthersTransferAccountsTypeName;
            }
        }
    }

    /**
     * 语音视图
     */
    public class VoiceViewHolder extends IMChatContentViewHolder {
        View itemView;
        LinearLayout llVoiceBg;
        TextView tvVoiceSecond;
        ImageView ivVoiceScrolling;
        AnimationDrawable mVoiceScrollingAnimation;

        public VoiceViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        @Override
        public void setOneselfOrOtherView(boolean isOneself) {
            ConstraintLayout clOneselfSendVoiceLayout = itemView.findViewById(R.id.cl_oneself_send_voice_layout);
            RoundAngleImageView ivOneselfAvatar = itemView.findViewById(R.id.niv_oneself_avatar);
            LinearLayout llOneselfVoiceBg = itemView.findViewById(R.id.ll_oneself_voice_bg);
            TextView tvOneselfVoiceSecond = itemView.findViewById(R.id.tv_oneself_voice_second);
            ImageView ivOneselfVoiceScrolling = itemView.findViewById(R.id.iv_oneself_voice_scrolling);

            ConstraintLayout clOthersSendVoiceLayout = itemView.findViewById(R.id.cl_others_send_voice_layout);
            RoundAngleImageView ivOthersAvatar = itemView.findViewById(R.id.niv_others_avatar);
            LinearLayout llOthersVoiceBg = itemView.findViewById(R.id.ll_others_voice_bg);
            TextView tvOthersVoiceSecond = itemView.findViewById(R.id.tv_others_voice_second);
            ImageView ivOthersVoiceScrolling = itemView.findViewById(R.id.iv_others_voice_scrolling);

            //内容提供者是自己就显示自己发送文字布局反之显示他人发送文字布局
            if (isOneself) {
                clOneselfSendVoiceLayout.setVisibility(View.VISIBLE);
                clOthersSendVoiceLayout.setVisibility(View.GONE);

                ivAvatar = ivOneselfAvatar;
                llVoiceBg = llOneselfVoiceBg;
                tvVoiceSecond = tvOneselfVoiceSecond;
                ivVoiceScrolling = ivOneselfVoiceScrolling;

                ivVoiceScrolling.setBackgroundResource(R.drawable.module_im_chat_oneself_voice_play_scrolling);
            } else {
                clOneselfSendVoiceLayout.setVisibility(View.GONE);
                clOthersSendVoiceLayout.setVisibility(View.VISIBLE);

                ivAvatar = ivOthersAvatar;
                llVoiceBg = llOthersVoiceBg;
                tvVoiceSecond = tvOthersVoiceSecond;
                ivVoiceScrolling = ivOthersVoiceScrolling;

                ivVoiceScrolling.setBackgroundResource(R.drawable.module_im_chat_others_voice_play_scrolling);
            }
            mVoiceScrollingAnimation = (AnimationDrawable) ivVoiceScrolling.getBackground();
        }
    }

    /**
     * 未知内容视图
     */
    public class UnknownContentViewHolder extends IMChatContentViewHolder {

        public UnknownContentViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_others_avatar);
        }

        @Override
        public void setOneselfOrOtherView(boolean isOneself) {

        }
    }
}
