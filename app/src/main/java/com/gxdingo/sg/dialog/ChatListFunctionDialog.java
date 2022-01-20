package com.gxdingo.sg.dialog;

import android.content.Context;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.gxdingo.sg.R;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.core.PositionPopupView;

/**
 * @author: kikis
 * @date: 2022/1/20
 * @page:
 */
public class ChatListFunctionDialog extends CenterPopupView {

    private OnClickListener listener;


    public ChatListFunctionDialog(@NonNull Context context, OnClickListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_chat_list_function;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        LinearLayout settop_ll = findViewById(R.id.settop_ll);
        LinearLayout del_ll = findViewById(R.id.del_ll);


        if (listener != null) {
            settop_ll.setOnClickListener(v -> {listener.onClick(v);dismiss();});
            del_ll.setOnClickListener(v -> {listener.onClick(v);dismiss();});
        }
    }

}
