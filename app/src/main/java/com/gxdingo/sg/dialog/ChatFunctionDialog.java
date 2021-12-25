package com.gxdingo.sg.dialog;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gxdingo.sg.R;
import com.kikis.commnlibrary.biz.CustomArgsResultListener;
import com.lxj.xpopup.core.PositionPopupView;

/**
 * @author: kikis
 * @date: 2021/12/25
 * @page:
 */
public class ChatFunctionDialog extends PositionPopupView {

    private CustomArgsResultListener customArgsResultListener;

    private TextView copy_tv;
    private TextView revocation_tv;
    private boolean isSelf;
    private int type = 0;

    public ChatFunctionDialog(@NonNull Context context, boolean self, int type, CustomArgsResultListener listener) {
        super(context);
        customArgsResultListener = listener;
        isSelf = self;
        this.type = type;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_chat_function;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        revocation_tv = findViewById(R.id.revocation_tv);
        copy_tv = findViewById(R.id.copy_tv);

        copy_tv.setVisibility(type == 0 ? VISIBLE : GONE);

        revocation_tv.setVisibility(isSelf ? VISIBLE : GONE);

        copy_tv.setOnClickListener(view -> {
            customArgsResultListener.onResult(0);
            dismiss();
        });

        revocation_tv.setOnClickListener(view -> {
            customArgsResultListener.onResult(1);
            dismiss();
        });
    }

}
