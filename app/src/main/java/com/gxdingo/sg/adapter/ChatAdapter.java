package com.gxdingo.sg.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
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

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cn.bingoogolapple.progressbar.BGAProgressBar;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.ClipboardUtils.copyText;
import static com.blankj.utilcode.util.ConvertUtils.dp2px;
import static com.blankj.utilcode.util.TimeUtils.getNowMills;
import static com.blankj.utilcode.util.TimeUtils.getNowString;
import static com.blankj.utilcode.util.TimeUtils.isToday;
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
import static com.gxdingo.sg.utils.LocalConstant.UNKNOWN;
import static com.kikis.commnlibrary.utils.MyToastUtils.customToast;

/**
 * @author: Kikis
 * @date: 2021/11/10
 * @page:
 */
public class ChatAdapter extends BaseRecyclerAdapter {

    //显示日期时间间隔（单位毫秒） 5分钟
    private int showDateInterval = 300000;

    private ChatClickListener chatClickListener;

    private IMChatHistoryListBean.MyAvatarInfo mMyAvatarInfo;

    private IMChatHistoryListBean.OtherAvatarInfo mOtherAvatarInfo;

    private String mTagContent = "";

    private long mTime = 0;

    private Disposable mDisposable;

    private ImageView iv_voice_scrolling;

    private int oldSendType = -1;

    public ChatAdapter(Context context, List list, IMChatHistoryListBean data, ChatClickListener chatClickListener) {
        super(list, context);

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
        boolean self = isEmpty(data.getSendIdentifier()) || myIdentifier.equals(data.getSendIdentifier());

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
        } else
            return UNKNOWN;

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
        else if (viewType == UNKNOWN)
            return R.layout.module_include_empty;

        return -1;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, Object item) {
        if (getItemViewType(position) == -1 || getItemViewType(position) == UNKNOWN) return;

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
            ImageView iv_voice_scrolling = holder.getImageView(R.id.iv_voice_scrolling);
            if (getItemViewType(position) == OtherAudio) {
                TextView unread_tv = holder.getTextView(R.id.unread_tv);
                unread_tv.setVisibility(data.recipientRead == 0 ? View.VISIBLE : View.GONE);
            }

            if (data.getVoiceDuration() > 0) {
                //动态设置语音宽度
                int value = (int) ((28 + data.getVoiceDuration()) * 2.5);

                voice_ll.getLayoutParams().width = value > 160 ? dp2px(160) : dp2px(value);

                tv_voice_second.setText(data.getVoiceDuration() + "\"");
            }


            //重置动画，语音动画显示错误
            if (iv_voice_scrolling.getBackground() != null) {
                iv_voice_scrolling.setBackgroundResource(0);
                iv_voice_scrolling.setBackground(null);
            }

            if (iv_voice_scrolling.getBackground() == null) {
                if (getItemViewType(position) == SelfAudio)
                    iv_voice_scrolling.setBackgroundResource(R.drawable.module_im_chat_oneself_voice_play_scrolling);
                else
                    iv_voice_scrolling.setBackgroundResource(R.drawable.module_im_chat_others_voice_play_scrolling);
            }

            //如果是正在播放的item
            if (data.getContent() == mTagContent) {
                //播放中,继续显示播放动画
                if (System.currentTimeMillis() - mTime <= (data.getVoiceDuration() * 1000)) {
                    ((AnimationDrawable) iv_voice_scrolling.getBackground()).start();
                } else {
                    //超时清除下标跟时间戳
                    mTagContent = "";
                    mTime = 0;
                    stopAnima((AnimationDrawable) iv_voice_scrolling.getBackground());
                }

            } else
                stopAnima((AnimationDrawable) iv_voice_scrolling.getBackground());


            voice_ll.setOnClickListener(v -> {
                AnimationDrawable anim = (AnimationDrawable) iv_voice_scrolling.getBackground();

                if (mTagContent == "") {
                    //未有语音在播放，播放语音
                    mTagContent = data.getContent();
                    mTime = System.currentTimeMillis();
                    startTimer(data.getVoiceDuration(), iv_voice_scrolling, getItemViewType(position));
                    anim.start();

                    if (chatClickListener != null) {
                        chatClickListener.onAudioClick(data.getContent(), true, position);
                        if (data.recipientRead == 0)
                            chatClickListener.clearUnread(position, data.getId());
                    }

                } else if (data.getContent() == mTagContent && System.currentTimeMillis() - mTime <= (data.getVoiceDuration() * 1000)) {
                    //正在播放中,取消语音播放
                    mTagContent = "";
                    mTime = 0;
                    stopAnima(anim);
                    cancel();

                    if (chatClickListener != null)
                        chatClickListener.onAudioClick(data.getContent(), false, position);

                } else {
                    //已经超时的播放，播放语音
                    mTagContent = data.getContent();
                    mTime = System.currentTimeMillis();
                    startTimer(data.getVoiceDuration(), iv_voice_scrolling, getItemViewType(position));
                    anim.start();

                    if (chatClickListener != null) {
                        chatClickListener.onAudioClick(data.getContent(), true, position);

                        if (data.recipientRead == 0)
                            chatClickListener.clearUnread(position, data.getId());
                    }

                }



                   /*     if (anim.isRunning()) {
//                            stopVoiceAnima(holder);
                            anim.selectDrawable(0);//选择当前动画的第一帧，然后停止
                            anim.stop();
                            cancel();
                            if (chatClickListener != null)
                                chatClickListener.onAudioClick(data.getContent(), anim,position);
                        } else {
                            stopVoiceAnima(holder);
                            if (getItemViewType(position) == OtherAudio && data.recipientRead == 0) {
                                data.recipientRead = 1;
                                notifyItemChanged(position);
                                chatClickListener.clearUnread(data.getId());
                            }
                            anim.start();//启动动画
                            startTimer(data.getVoiceDuration());
                            if (chatClickListener != null)
                                chatClickListener.onAudioClick(data.getContent(), anim,position);
                        }*/

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


            cl_transfer_accounts_bg.setOnClickListener(v -> {
                if (chatClickListener != null && getItemViewType(position) == OtherTransfer)
                    chatClickListener.onTransferClick(position, data.getId());
            });

            if (msgAccounts != null) {
                cl_transfer_accounts_bg.setVisibility(View.VISIBLE);

                NumberFormat nf = NumberFormat.getInstance();
                String account = nf.format(msgAccounts.getAmount());

                amount_tv.setText(account);

                //0=未付款；1=待领取；2=已收款；3=拒绝收款；4=过期退回
                if (msgAccounts.getStatus() == 1) {
                    status_tv.setText("待领取");
                    cl_transfer_accounts_bg.getBackground().setAlpha(255);
                } else if (msgAccounts.getStatus() == 2) {

                    boolean isSelf = data.getSendIdentifier().equals(UserInfoUtils.getInstance().getIdentifier());

                    if (isSelf) {
                        //转账
                        status_tv.setText(data.getType() == 20 ? "已被接收" : "已收款");
                    } else {
                        status_tv.setText(data.getType() == 21 ? "已收款" : "已被接收");
                    }


                    cl_transfer_accounts_bg.getBackground().setAlpha(100);
                } else if (msgAccounts.getStatus() == 3) {
                    status_tv.setText("拒绝收款");
                    cl_transfer_accounts_bg.getBackground().setAlpha(100);
                } else if (msgAccounts.getStatus() == 4) {
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
                } else if (msgAccounts.getPayType() == 30) {
                    transfer_type_name_tv.setText("钱包转账");
                    iv_transfer_accounts_type_icon.setImageResource(R.drawable.module_im_chat_transfer_accounts_type_blance_icon);
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

        avatar_img.setOnClickListener(v -> chatClickListener.onAvatarClickListener(position, data.getId()));

        String avatarUrl = "";
        if (getItemViewType(position) == SelfText || getItemViewType(position) == SelfImage || getItemViewType(position) == SelfAudio || getItemViewType(position) == SelfTransfer) {
            if (mMyAvatarInfo != null && !isEmpty(mMyAvatarInfo.getSendAvatar()))
                avatarUrl = mMyAvatarInfo.getSendAvatar();
        } else {
            if (mOtherAvatarInfo != null && !isEmpty(mOtherAvatarInfo.getSendAvatar()))
                avatarUrl = mOtherAvatarInfo.getSendAvatar();
        }

        Glide.with(mContext).load(!isEmpty(avatarUrl) ? avatarUrl : R.drawable.module_svg_client_default_avatar).apply(GlideUtils.getInstance().getGlideRoundOptions(6)).into(avatar_img);


        TextView time_tv = holder.getTextView(R.id.time_tv);

        if (!isEmpty(data.getCreateTime())) {

            String date = dealDateFormat(data.getCreateTime());

            time_tv.setText(isToday(date) ? dealDateFormat(data.getCreateTime(), "HH:mm") : date);
        }


        if (position == 0)
            time_tv.setVisibility(View.VISIBLE);
        else {

            ReceiveIMMessageBean temp = null;

            //此方法是为了排除地址类型
            for (int i = position - 1; i >= 0; i--) {
                //只取不是地址类型的消息时间对比
                if (((ReceiveIMMessageBean) getData().get(i)).getType() != 30) {
                    temp = (ReceiveIMMessageBean) getData().get(i);
                    break;
                }
            }

            String t = dealDateFormat(temp != null ? temp.getCreateTime() : "2020-04-09 23:00:00");

            String d = dealDateFormat(data.getCreateTime());

            if (!isEmpty(t) && !isEmpty(d)) {
                if (t.length() <= 8)
                    t = getNowString(new SimpleDateFormat("yyyy-MM-dd ", Locale.getDefault())) + t;
                if (d.length() <= 8)
                    d = getNowString(new SimpleDateFormat("yyyy-MM-dd ", Locale.getDefault())) + d;

                //上一次的发送时间
                long lasttime = string2Millis(t);
                //本次消息的发送时间
                long postime = string2Millis(d);

                //如果上次发送消息的时间超过当前消息900秒(15分钟)才重新显示，否则不显示时间。
                if (postime - lasttime >= showDateInterval) {
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


    private void startTimer(int duration, ImageView iv, int type) {

        //如果上一次点击的语音img是同一个，清除动画
        if (iv_voice_scrolling != null && iv_voice_scrolling != iv) {
            cancel();
            clearAnima();
        }

        Observable observable = Observable.timer(duration * 1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable disposable) {
                mDisposable = disposable;
                iv_voice_scrolling = iv;
                oldSendType = type;
            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull Long number) {

            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                cancel();
                clearAnima();
            }

            @Override
            public void onComplete() {
                //取消订阅
                cancel();
                clearAnima();
            }
        });

    }

    /**
     * 清除imgview动画
     */
    private void clearAnima() {
        if (iv_voice_scrolling != null) {
            iv_voice_scrolling.setBackgroundResource(0);
            iv_voice_scrolling.setBackground(null);
            if (oldSendType == SelfAudio)
                iv_voice_scrolling.setBackgroundResource(R.drawable.module_im_chat_oneself_voice_play_scrolling);
            else
                iv_voice_scrolling.setBackgroundResource(R.drawable.module_im_chat_others_voice_play_scrolling);
        }
    }

    /**
     * 停止当前语音动画
     */
    private void stopAnima(AnimationDrawable animationDrawable) {
        if (animationDrawable != null) {
            animationDrawable.selectDrawable(0);//选择当前动画的第一帧，然后停止
            animationDrawable.stop();
        }
    }


    /**
     * 取消订阅
     */
    public void cancel() {
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }
}
