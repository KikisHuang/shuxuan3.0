package com.gxdingo.sg.dialog;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.gxdingo.sg.R;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.kikis.commnlibrary.bean.SubscribesListBean;
import com.kikis.commnlibrary.biz.CustomArgsResultListener;
import com.kikis.commnlibrary.utils.GlideUtils;
import com.luck.picture.lib.listener.OnItemClickListener;
import com.lxj.xpopup.core.CenterPopupView;

import static com.blankj.utilcode.util.TimeUtils.getNowMills;
import static com.blankj.utilcode.util.TimeUtils.string2Millis;
import static com.gxdingo.sg.utils.DateUtils.dealDateFormat;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * @author: kikis
 * @date: 2022/1/21
 * @page:聊天转发确认弹窗
 */
public class ChatForwardDialog extends CenterPopupView {

    private OnClickListener listener;

    private ReceiveIMMessageBean receiveIMMessageBean;

    private SubscribesListBean.SubscribesMessage subscribesMessage;

    private ImageView recipient_avatar_img, map_img;

    private TextView recipient_name_tv, address_street_tv, address_tv, name_tv, phone_tv, cancel_tv, confirm_tv;

    public ChatForwardDialog(@NonNull Context context, SubscribesListBean.SubscribesMessage subscribesMessage, ReceiveIMMessageBean data, OnClickListener listener) {
        super(context);
        this.listener = listener;
        this.receiveIMMessageBean = data;
        this.subscribesMessage = subscribesMessage;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_chat_transpond;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();

        recipient_avatar_img = findViewById(R.id.recipient_avatar_img);
        map_img = findViewById(R.id.map_img);
        recipient_name_tv = findViewById(R.id.recipient_name_tv);
        address_street_tv = findViewById(R.id.address_street_tv);
        address_tv = findViewById(R.id.address_tv);
        name_tv = findViewById(R.id.name_tv);
        phone_tv = findViewById(R.id.phone_tv);
        cancel_tv = findViewById(R.id.cancel_tv);
        confirm_tv = findViewById(R.id.confirm_tv);

        if (subscribesMessage != null) {
            if (!isEmpty(subscribesMessage.getSendAvatar()))
                Glide.with(getContext()).load(subscribesMessage.getSendAvatar()).apply(GlideUtils.getInstance().getGlideRoundOptions(6)).into(recipient_avatar_img);

            if (!isEmpty(subscribesMessage.getSendNickname()))
                recipient_name_tv.setText(subscribesMessage.getSendNickname());
        }

        if (receiveIMMessageBean != null) {

            address_street_tv.setText(receiveIMMessageBean.getDataByType().getDoorplate());

            address_tv.setText(receiveIMMessageBean.getDataByType().getStreet());

            Glide.with(getContext()).load(!isEmpty(receiveIMMessageBean.getDataByType().locationImage)?receiveIMMessageBean.getDataByType().locationImage:R.drawable.bg_location_default).apply(GlideUtils.getInstance().getDefaultOptions()).into(map_img);

            if (!isEmpty(receiveIMMessageBean.getDataByType().getName()))
                name_tv.setText(receiveIMMessageBean.getDataByType().getName());

            if (!isEmpty(receiveIMMessageBean.getDataByType().getMobile()))
                phone_tv.setText(receiveIMMessageBean.getDataByType().getMobile());
        }

        cancel_tv.setOnClickListener(v -> dismiss());

        confirm_tv.setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(v);
            dismiss();
        });

    }

}
