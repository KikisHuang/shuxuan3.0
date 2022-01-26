package com.gxdingo.sg.dialog;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gxdingo.sg.R;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.kikis.commnlibrary.biz.CustomArgsResultListener;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.core.PositionPopupView;

import static com.blankj.utilcode.util.TimeUtils.getNowMills;
import static com.blankj.utilcode.util.TimeUtils.string2Millis;
import static com.gxdingo.sg.utils.DateUtils.dealDateFormat;

/**
 * @author: kikis
 * @date: 2021/12/25
 * @page:
 */
public class ChatFunctionDialog extends CenterPopupView {

    private CustomArgsResultListener customArgsResultListener;

    private LinearLayout copy_ll, report_ll, del_ll, turn_the_text_ll;

    private boolean isSelf;
    private ReceiveIMMessageBean receiveIMMessageBean;

    private int type = 0;

    public ChatFunctionDialog(@NonNull Context context, boolean self, ReceiveIMMessageBean data, CustomArgsResultListener listener) {
        super(context);
        customArgsResultListener = listener;
        isSelf = self;
        this.receiveIMMessageBean = data;
        this.type = receiveIMMessageBean.getType();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_chat_function;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        copy_ll = findViewById(R.id.copy_ll);
        report_ll = findViewById(R.id.report_ll);
        del_ll = findViewById(R.id.del_ll);
        turn_the_text_ll = findViewById(R.id.turn_the_text_ll);

        ////消息类型 0=文本 1=表情 10=图片 11=语音 12=视频 20=转账 21=收款 30=定位位置信息
        copy_ll.setVisibility(type == 0 ? VISIBLE : GONE);

        turn_the_text_ll.setVisibility(type == 11 ? VISIBLE : GONE);


        //自己发送的
        if (isSelf) {
            long nowTime = getNowMills();

            long sendTime = string2Millis(dealDateFormat(receiveIMMessageBean.getCreateTime()));

            //小于60s显示撤回
            if (nowTime - sendTime <= 60000)
                report_ll.setVisibility(VISIBLE);
            else
                report_ll.setVisibility(GONE);

        } else
            report_ll.setVisibility(GONE);



        del_ll.setOnClickListener(v -> {
            if (customArgsResultListener != null)
                customArgsResultListener.onResult(3);
            dismiss();
        });

        copy_ll.setOnClickListener(view -> {
            if (customArgsResultListener != null)
                customArgsResultListener.onResult(0);
            dismiss();
        });

        report_ll.setOnClickListener(view -> {
            if (customArgsResultListener != null)
                customArgsResultListener.onResult(1);
            dismiss();
        });
        turn_the_text_ll.setOnClickListener(view -> {
            if (customArgsResultListener != null)
                customArgsResultListener.onResult(2);
            dismiss();
        });
    }

}
