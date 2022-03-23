package com.gxdingo.sg.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.LogUtils;
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
import static com.blankj.utilcode.util.TimeUtils.getNowString;
import static com.blankj.utilcode.util.TimeUtils.isToday;
import static com.blankj.utilcode.util.TimeUtils.string2Millis;
import static com.gxdingo.sg.utils.DateUtils.dealDateFormat;
import static com.gxdingo.sg.utils.LocalConstant.AddressInfoDel;
import static com.gxdingo.sg.utils.LocalConstant.OtherAudio;
import static com.gxdingo.sg.utils.LocalConstant.OtherImage;
import static com.gxdingo.sg.utils.LocalConstant.OtherLocationMapInfo;
import static com.gxdingo.sg.utils.LocalConstant.OtherLogistics;
import static com.gxdingo.sg.utils.LocalConstant.OtherRevocation;
import static com.gxdingo.sg.utils.LocalConstant.OtherText;
import static com.gxdingo.sg.utils.LocalConstant.OtherTransfer;
import static com.gxdingo.sg.utils.LocalConstant.SelfAudio;
import static com.gxdingo.sg.utils.LocalConstant.SelfImage;
import static com.gxdingo.sg.utils.LocalConstant.SelfLocationMapInfo;
import static com.gxdingo.sg.utils.LocalConstant.SelfLogistics;
import static com.gxdingo.sg.utils.LocalConstant.SelfRevocation;
import static com.gxdingo.sg.utils.LocalConstant.SelfText;
import static com.gxdingo.sg.utils.LocalConstant.SelfTransfer;
import static com.gxdingo.sg.utils.LocalConstant.UNKNOWN;

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


        //撤回的消息状态 /状态。0=正常；1=撤回
        if (data.getStatus() == 1)
            return self ? SelfRevocation : OtherRevocation;

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
        } else if (data.getType() == 30) {
            //删除掉的地址显示一条文字提示
            if (data.getDataByType() == null)
                return AddressInfoDel;

            if (self)
                return SelfLocationMapInfo;
            else
                return OtherLocationMapInfo;
        } else if (data.getType() == 999) {
            //todo 物流的类型还没确定
            if (self)
                return SelfLogistics;
            else
                return OtherLogistics;
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
        else if (viewType == SelfLogistics)
            return R.layout.module_item_chat_self_logistics;
        else if (viewType == OtherLogistics)
            return R.layout.module_item_chat_other_logistics;
        else if (viewType == SelfRevocation || viewType == OtherRevocation)
            return R.layout.module_item_chat_revocation;
        else if (viewType == SelfLocationMapInfo)
            return R.layout.module_item_chat_self_map_location_info;
        else if (viewType == OtherLocationMapInfo)
            return R.layout.module_item_chat_other_map_location_info;
        else if (viewType == AddressInfoDel)
            return R.layout.module_item_chat_revocation;
        else if (viewType == UNKNOWN)
            return R.layout.module_include_empty;


        return -1;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, Object item) {

        int itemType = getItemViewType(position);

        if (itemType == -1 || itemType == UNKNOWN) return;

        if (itemType == OtherRevocation || itemType == SelfRevocation) {
            //消息撤回类型
            TextView revocation_tv = holder.getTextView(R.id.revocation_tv);
            revocation_tv.setText(itemType == OtherRevocation ? "对方撤回了一条消息" : "你撤回了一条消息");
            return;
        } else if (itemType == AddressInfoDel) {
            //地址信息删除已类型
            holder.setText(R.id.revocation_tv, "该条地址信息已被删除");
            return;
        }

        ReceiveIMMessageBean data = (ReceiveIMMessageBean) item;

        //通用数据设置
        genericViewDataInit(position, data, holder);

        //定位地图信息类型
        if (itemType == SelfLocationMapInfo || itemType == OtherLocationMapInfo) {

            TextView address_tv = holder.getTextView(R.id.address_tv);
            TextView addressaddress_street_tv_tv = holder.getTextView(R.id.address_street_tv);
            TextView name_tv = holder.getTextView(R.id.name_tv);
            TextView phone_tv = holder.getTextView(R.id.phone_tv);
            ImageView map_img = holder.getImageView(R.id.map_img);
            View map_ll = holder.getView(R.id.map_ll);


            map_ll.setOnClickListener(v -> {
                if (chatClickListener != null)
                    chatClickListener.onLocationMapClick(position);
            });

            map_ll.setOnLongClickListener(v -> {
                if (chatClickListener != null)
                    chatClickListener.onLongClickChatItem(position, itemType == SelfLocationMapInfo ? true : false);
                return false;
            });

            if (data.getDataByType() != null) {

                if (!isEmpty(data.getDataByType().getDoorplate()))
                    addressaddress_street_tv_tv.setText(data.getDataByType().getDoorplate());

                if (!isEmpty(data.getDataByType().getStreet()))
                    address_tv.setText(data.getDataByType().getStreet());

                if (!isEmpty(data.getDataByType().getName()))
                    name_tv.setText(data.getDataByType().getName());

                if (!isEmpty(data.getDataByType().getMobile()))
                    phone_tv.setText(data.getDataByType().getMobile());

                Glide.with(mContext).load(!isEmpty(data.getDataByType().locationImage) ? data.getDataByType().locationImage : R.drawable.bg_location_default).into(map_img);

            }

        }

        //文字类型
        if (itemType == SelfText || itemType == OtherText) {

            TextView content = holder.getTextView(R.id.content_tv);
            if (!StringUtils.isEmpty(data.getContent())) {
                content.setText(SpanStringUtils.getEmotionContent(EmotionUtils.EMOTION_CLASSIC_TYPE,
                        mContext, data.getContent()));
            }
            content.setOnLongClickListener(v -> {
                if (chatClickListener != null) {
                    chatClickListener.onLongClickChatItem(position, itemType == SelfText ? true : false);
                }
                return false;
            });
        }

        //图片内容加载
        if (itemType == OtherImage || itemType == SelfImage) {
            //上传进度
            if (itemType == SelfImage)
                ImgProgressBarInit(data, holder);

            ImageView content_img = holder.getImageView(R.id.content_img);

            Glide.with(mContext)
                    .load(!isEmpty(data.getContent()) ? data.getContent() : R.drawable.load_faile_icon)
                    .apply(GlideUtils.getInstance().getGlideRoundOptions(3))
                    .into(content_img);

            content_img.setOnClickListener(v -> {

                if (chatClickListener != null)
                    chatClickListener.onImageClick(data.getContent());
            });

            content_img.setOnLongClickListener(v -> {
                if (chatClickListener != null) {
                    chatClickListener.onLongClickChatItem(position, itemType == SelfImage);
                }
                return false;
            });
        }

        //语音类型
        if (itemType == OtherAudio || itemType == SelfAudio) {

            LinearLayout voice_ll = holder.getLinearLayout(R.id.voice_ll);
            TextView tv_voice_second = holder.getTextView(R.id.tv_voice_second);
            TextView voice_text_tv = holder.getTextView(R.id.voice_text_tv);
            ImageView iv_voice_scrolling = holder.getImageView(R.id.iv_voice_scrolling);

            voice_text_tv.setVisibility(!isEmpty(data.voiceText) ? View.VISIBLE : View.GONE);

            if (!isEmpty(data.voiceText))
                voice_text_tv.setText(data.voiceText);

            if (itemType == OtherAudio) {
                TextView unread_tv = holder.getTextView(R.id.unread_tv);
                unread_tv.setVisibility(data.recipientRead == 0 ? View.VISIBLE : View.GONE);
            }

            voice_ll.setOnLongClickListener(v -> {
                if (chatClickListener != null) {
                    chatClickListener.onLongClickChatItem(position, itemType == SelfAudio);
                }
                return false;
            });

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
                if (itemType == SelfAudio)
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
                voice_ll.post(() -> {
                            try {

                                AnimationDrawable anim = (AnimationDrawable) iv_voice_scrolling.getBackground();

                                if (mTagContent == "") {
                                    //未有语音在播放，播放语音
                                    mTagContent = data.getContent();
                                    mTime = System.currentTimeMillis();
                                    startTimer(data.getVoiceDuration(), iv_voice_scrolling, itemType);
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
                                    startTimer(data.getVoiceDuration(), iv_voice_scrolling, itemType);
                                    anim.start();

                                    if (chatClickListener != null) {
                                        chatClickListener.onAudioClick(data.getContent(), true, position);

                                        if (data.recipientRead == 0)
                                            chatClickListener.clearUnread(position, data.getId());
                                    }

                                }
                            } catch (Exception e) {
                                LogUtils.e(" audio play error === " + e);
                            }
                        }
                );

            });

        }

        //转账类型
        if (itemType == SelfTransfer || itemType == OtherTransfer) {

            ReceiveIMMessageBean.DataByType dataByType = data.getDataByType();

            ConstraintLayout cl_transfer_accounts_bg = (ConstraintLayout) holder.getView(R.id.cl_transfer_accounts_bg);
            TextView amount_tv = holder.getTextView(R.id.amount_tv);
            TextView status_tv = holder.getTextView(R.id.status_tv);
            TextView transfer_type_name_tv = holder.getTextView(R.id.transfer_type_name_tv);
            ImageView iv_transfer_accounts_type_icon = holder.getImageView(R.id.iv_transfer_accounts_type_icon);


            cl_transfer_accounts_bg.setOnClickListener(v -> {
                //只有stats ==1 待领取状态的他人转账消息才可点击
                if (chatClickListener != null && itemType == OtherTransfer && data.getDataByType().getStatus() == 1)
                    chatClickListener.onTransferClick(position, data.getId());
            });
            cl_transfer_accounts_bg.setOnLongClickListener(v -> {
                if (chatClickListener != null) {
                    chatClickListener.onLongClickChatItem(position, itemType == SelfTransfer);
                }
                return false;
            });


            if (dataByType != null) {
                cl_transfer_accounts_bg.setVisibility(View.VISIBLE);

                NumberFormat nf = NumberFormat.getInstance();
                String account = nf.format(dataByType.getAmount());

                amount_tv.setText(account);

                //0=未付款；1=待领取；2=已收款；3=拒绝收款；4=过期退回
                if (dataByType.getStatus() == 1) {
                    status_tv.setText("待领取");
                    cl_transfer_accounts_bg.getBackground().setAlpha(255);
                } else if (dataByType.getStatus() == 2) {

                    boolean isSelf = data.getSendIdentifier().equals(UserInfoUtils.getInstance().getIdentifier());

                    if (isSelf) {
                        //转账
                        status_tv.setText(data.getType() == 20 ? "已被接收" : "已收款");
                    } else {
                        status_tv.setText(data.getType() == 21 ? "已收款" : "已被接收");
                    }


                    cl_transfer_accounts_bg.getBackground().setAlpha(100);
                } else if (dataByType.getStatus() == 3) {
                    status_tv.setText("拒绝收款");
                    cl_transfer_accounts_bg.getBackground().setAlpha(100);
                } else if (dataByType.getStatus() == 4) {
                    status_tv.setText("过期退回");
                    cl_transfer_accounts_bg.getBackground().setAlpha(100);
                }
                //转账方支付类型。10=微信,20=支付宝
                if (dataByType.getPayType() == 10) {
                    transfer_type_name_tv.setText("微信转账");
                    iv_transfer_accounts_type_icon.setImageResource(R.drawable.module_im_chat_transfer_accounts_type_wechat_icon);
                } else if (dataByType.getPayType() == 20) {
                    transfer_type_name_tv.setText("支付宝转账");
                    iv_transfer_accounts_type_icon.setImageResource(R.drawable.module_im_chat_transfer_accounts_type_alipay_icon);
                } else if (dataByType.getPayType() == 30) {
                    transfer_type_name_tv.setText("钱包转账");
                    iv_transfer_accounts_type_icon.setImageResource(R.drawable.module_im_chat_transfer_accounts_type_blance_icon);
                }
            } else {
                cl_transfer_accounts_bg.setVisibility(View.INVISIBLE);
            }

        }
    }


    /**
     * 通用view数据初始化 (头像、聊天时间)
     *
     * @param position
     * @param data
     * @param holder
     */
    private void genericViewDataInit(int position, ReceiveIMMessageBean data, RecyclerViewHolder holder) {
        //头像
        ImageView avatar_img = holder.getImageView(R.id.avatar_img);

        avatar_img.setOnClickListener(v -> chatClickListener.onAvatarClickListener(position, mOtherAvatarInfo.getSendIdentifier()));

        String avatarUrl = "";
        if (getItemViewType(position) == SelfText || getItemViewType(position) == SelfImage || getItemViewType(position) == SelfAudio || getItemViewType(position) == SelfTransfer || getItemViewType(position) == SelfLocationMapInfo || getItemViewType(position) == SelfLogistics) {
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

            String t = temp != null ? dealDateFormat(temp.getCreateTime()) : "2020-04-09 23:00:00";

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
