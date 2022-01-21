package com.gxdingo.sg.dialog;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gxdingo.sg.R;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.kikis.commnlibrary.biz.CustomArgsResultListener;
import com.lxj.xpopup.core.CenterPopupView;

import static com.blankj.utilcode.util.TimeUtils.getNowMills;
import static com.blankj.utilcode.util.TimeUtils.string2Millis;
import static com.gxdingo.sg.utils.DateUtils.dealDateFormat;

/**
 * @author: kikis
 * @date: 2022/1/21
 * @page:聊天转发确认弹窗
 */
public class ChatTranspondDialog extends CenterPopupView {

    private CustomArgsResultListener customArgsResultListener;

    private ReceiveIMMessageBean receiveIMMessageBean;

    private ImageView recipient_avatar_img;

    private TextView recipient_name_tv, address_street_tv, address_tv, name_tv, phone_tv, cancel_tv, confirm_tv;

    public ChatTranspondDialog(@NonNull Context context, ReceiveIMMessageBean data, CustomArgsResultListener listener) {
        super(context);
        customArgsResultListener = listener;
        this.receiveIMMessageBean = data;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_chat_transpond;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();

        recipient_avatar_img = findViewById(R.id.recipient_avatar_img);
        recipient_name_tv = findViewById(R.id.recipient_name_tv);
        address_street_tv = findViewById(R.id.address_street_tv);
        address_tv = findViewById(R.id.address_tv);
        name_tv = findViewById(R.id.name_tv);
        phone_tv = findViewById(R.id.phone_tv);
        cancel_tv = findViewById(R.id.cancel_tv);
        confirm_tv = findViewById(R.id.confirm_tv);
    }

}
