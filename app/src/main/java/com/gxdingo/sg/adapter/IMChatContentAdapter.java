package com.gxdingo.sg.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
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
import com.gxdingo.sg.utils.EmotionsUtils;
import com.gxdingo.sg.utils.TextViewUtils;
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
public class IMChatContentAdapter extends PullRecyclerView.PullAdapter<RecyclerView.ViewHolder> {

    public static final int IM_CHAT_CONTENT_PROVIDER_ONESELF = 1;//IM聊天内容提供者-自己
    public static final int IM_CHAT_CONTENT_PROVIDER_OTHERS = 2;//IM聊天内容提供者-他人

    private Context mContext;
    private ArrayList<ChatTest> mTempDatas;

    private IMChatActivity.OnIMChatUICallbackListener mOnIMChatUICallbackListener;

    public IMChatContentAdapter(Context context, ArrayList<ChatTest> tempDatas, IMChatActivity.OnIMChatUICallbackListener onIMChatUICallbackListener) {
        mContext = context;
        mTempDatas = tempDatas;
        mOnIMChatUICallbackListener = onIMChatUICallbackListener;
    }

    @Override
    public int getPullItemCount() {
        return mTempDatas.size();
    }

    @Override
    public int getPullItemViewType(int position) {
        int viewType = 0;
        int contentProvider = mTempDatas.get(position).fx;
        int contentType = mTempDatas.get(position).lx;
        /**
         * 判断内容提供者是自己还是他人
         */
        if (contentProvider == IM_CHAT_CONTENT_PROVIDER_ONESELF) {
            /**
             * 自己（数字1开头）
             */
            if (contentType == 1) {//文字
                viewType = 101;
            } else if (contentType == 2) {//图片
                viewType = 102;
            } else if (contentType == 3) {//转账
                viewType = 103;
            } else if (contentType == 4) {//语音
                viewType = 104;
            } else {//未知
                viewType = 100;
            }
        } else if (contentProvider == IM_CHAT_CONTENT_PROVIDER_OTHERS) {
            /**
             * 他人（数字2开头）
             */
            if (contentType == 1) {//文字
                viewType = 201;
            } else if (contentType == 2) {//图片
                viewType = 202;
            } else if (contentType == 3) {//转账
                viewType = 203;
            } else if (contentType == 4) {//语音
                viewType = 204;
            } else {//未知
                viewType = 200;
            }
        }
        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onPullCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == 101 || viewType == 201) {
            //文字
            view = LayoutInflater.from(mContext).inflate(R.layout.module_item_im_chat_text, viewGroup, false);
            viewHolder = new TextViewHolder(view, viewType == 101 ? IM_CHAT_CONTENT_PROVIDER_ONESELF : IM_CHAT_CONTENT_PROVIDER_OTHERS);
        } else if (viewType == 102 || viewType == 202) {
            //图片
            view = LayoutInflater.from(mContext).inflate(R.layout.module_item_im_chat_picture, viewGroup, false);
            viewHolder = new PictureViewHolder(view, viewType == 102 ? IM_CHAT_CONTENT_PROVIDER_ONESELF : IM_CHAT_CONTENT_PROVIDER_OTHERS);
        } else if (viewType == 103 || viewType == 203) {
            //转账
            view = LayoutInflater.from(mContext).inflate(R.layout.module_item_im_chat_transfer_accounts, viewGroup, false);
            viewHolder = new TransferAccountsViewHolder(view, viewType == 103 ? IM_CHAT_CONTENT_PROVIDER_ONESELF : IM_CHAT_CONTENT_PROVIDER_OTHERS);
        } else if (viewType == 104 || viewType == 204) {
            //语音
            view = LayoutInflater.from(mContext).inflate(R.layout.module_item_im_chat_voice, viewGroup, false);
            viewHolder = new VoiceViewHolder(view, viewType == 104 ? IM_CHAT_CONTENT_PROVIDER_ONESELF : IM_CHAT_CONTENT_PROVIDER_OTHERS);
        } else if (viewType == 100 || viewType == 200) {
            //未知，一般用来处理低版本APP收到高版本APP的新视图类型时显示（即旧版本没有该视图时）
            view = LayoutInflater.from(mContext).inflate(R.layout.module_item_im_chat_unknown_content, viewGroup, false);
            viewHolder = new UnknownContentViewHolder(view, viewType == 100 ? IM_CHAT_CONTENT_PROVIDER_ONESELF : IM_CHAT_CONTENT_PROVIDER_OTHERS);
        }
        return viewHolder;
    }

    @Override
    public void onPullBindViewHolder(RecyclerView.ViewHolder contentViewHolder, int position) {
        ChatTest chatTest = mTempDatas.get(position);
        //文字视图
        if (contentViewHolder instanceof TextViewHolder) {
            TextViewHolder textViewHolder = (TextViewHolder) contentViewHolder;
            Glide.with(mContext).load(chatTest.nr).apply(getRequestOptions()).into(textViewHolder.ivAvatar);
            textViewHolder.tvSendText.setText(TextViewUtils.contentConversion(mContext,chatTest.nr2));
        }
        //图片视图
        else if (contentViewHolder instanceof PictureViewHolder) {
            PictureViewHolder pictureHolder = (PictureViewHolder) contentViewHolder;
            Glide.with(mContext).load(chatTest.nr).apply(getRequestOptions()).into(pictureHolder.ivAvatar);
            Glide.with(mContext).load(chatTest.nr2).apply(getRequestOptions()).into(pictureHolder.ivPicture);

        }
        //转账视图
        else if (contentViewHolder instanceof TransferAccountsViewHolder) {
            TransferAccountsViewHolder transferAccountsHolder = (TransferAccountsViewHolder) contentViewHolder;
            Glide.with(mContext).load(chatTest.nr).apply(getRequestOptions()).into(transferAccountsHolder.ivAvatar);
            transferAccountsHolder.tvAmount.setText(chatTest.nr2);
            transferAccountsHolder.tvReceivePaymentStatus.setText("待接收");
            transferAccountsHolder.tvTransferAccountsTypeName.setText("微信转账");
            transferAccountsHolder.ivTransferAccountsTypeIcon.setImageResource(R.drawable.module_im_chat_transfer_accounts_type_alipay_icon);
        }
        //语音视图
        else if (contentViewHolder instanceof VoiceViewHolder) {
            VoiceViewHolder voiceViewHolder = (VoiceViewHolder) contentViewHolder;
            Glide.with(mContext).load(chatTest.nr).apply(getRequestOptions()).into(voiceViewHolder.ivAvatar);
            voiceViewHolder.tvVoiceSecond.setText(chatTest.nr2);
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
                        mOnIMChatUICallbackListener.onCallback(voiceViewHolder.mContentProvider, chatTest);
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
            Glide.with(mContext).load(chatTest.nr).apply(getRequestOptions()).into(unknownContentViewHolder.ivAvatar);
        }
    }

    private int mCurrPlayVoiceIndex = -1;//当前播放语音的索引

    private RequestOptions getRequestOptions() {
        RequestOptions options = new RequestOptions();
        options.placeholder(com.kikis.commnlibrary.R.mipmap.news_default_icon);    //加载成功之前占位图
        options.error(com.kikis.commnlibrary.R.mipmap.news_default_icon);    //加载错误之后的错误图
        return options;
    }

    /**
     * 父ViewHolder
     */
    private class IMChatContentViewHolder extends RecyclerView.ViewHolder {
        int mContentProvider;//内容提供者,自己或者他人
        RoundAngleImageView ivAvatar;
        TextView tvMessageTime;


        public IMChatContentViewHolder(@NonNull View itemView, int contentProvider) {
            super(itemView);
            mContentProvider = contentProvider;
            tvMessageTime = itemView.findViewById(R.id.tv_message_time);
        }
    }

    /**
     * 文字视图
     */
    public class TextViewHolder extends IMChatContentViewHolder {
        TextView tvSendText;

        public TextViewHolder(@NonNull View itemView, int contentProvider) {
            super(itemView, contentProvider);
            ConstraintLayout clOneselfSendTextLayout = itemView.findViewById(R.id.cl_oneself_send_text_layout);
            RoundAngleImageView nivOneselfAvatar = itemView.findViewById(R.id.niv_oneself_avatar);
            TextView tvOneselfSendText = itemView.findViewById(R.id.tv_oneself_send_text);

            ConstraintLayout clOthersSendTextLayout = itemView.findViewById(R.id.cl_others_send_text_layout);
            RoundAngleImageView nivOthersAvatar = itemView.findViewById(R.id.niv_others_avatar);
            TextView tvOthersSendText = itemView.findViewById(R.id.tv_others_send_text);

            //内容提供者是自己就显示自己发送文字布局反之显示他人发送文字布局
            if (mContentProvider == IM_CHAT_CONTENT_PROVIDER_ONESELF) {
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
        RoundAngleImageView ivPicture;

        public PictureViewHolder(@NonNull View itemView, int contentProvider) {
            super(itemView, contentProvider);
            ConstraintLayout clOneselfSendPictureLayout = itemView.findViewById(R.id.cl_oneself_send_picture_layout);
            RoundAngleImageView ivOneselfAvatar = itemView.findViewById(R.id.iv_oneself_avatar);
            RoundAngleImageView ivOneselfPicture = itemView.findViewById(R.id.iv_oneself_picture);

            ConstraintLayout clOthersSendPictureLayout = itemView.findViewById(R.id.cl_others_send_picture_layout);
            RoundAngleImageView ivOthersAvatar = itemView.findViewById(R.id.iv_others_avatar);
            RoundAngleImageView ivOthersPicture = itemView.findViewById(R.id.iv_others_picture);

            //内容提供者是自己就显示自己发送文字布局反之显示他人发送文字布局
            if (mContentProvider == IM_CHAT_CONTENT_PROVIDER_ONESELF) {
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
        ConstraintLayout clTransferAccountsBg;
        TextView tvAmount;
        TextView tvReceivePaymentStatus;
        ImageView ivTransferAccountsTypeIcon;
        TextView tvTransferAccountsTypeName;

        public TransferAccountsViewHolder(@NonNull View itemView, int contentProvider) {
            super(itemView, contentProvider);
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
            if (mContentProvider == IM_CHAT_CONTENT_PROVIDER_ONESELF) {
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
        LinearLayout llVoiceBg;
        TextView tvVoiceSecond;
        ImageView ivVoiceScrolling;
        AnimationDrawable mVoiceScrollingAnimation;

        public VoiceViewHolder(@NonNull View itemView, int contentProvider) {
            super(itemView, contentProvider);
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
            if (mContentProvider == IM_CHAT_CONTENT_PROVIDER_ONESELF) {
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

        public UnknownContentViewHolder(@NonNull View itemView, int contentProvider) {
            super(itemView, contentProvider);
            ivAvatar = itemView.findViewById(R.id.iv_others_avatar);
        }
    }

    public static class ChatTest {
        public int fx;
        public int lx;
        public String nr;
        public String nr2;

        public ChatTest(int fx, int lx, String nr, String nr2) {
            this.fx = fx;
            this.lx = lx;
            this.nr = nr;
            this.nr2 = nr2;
        }
    }
}
