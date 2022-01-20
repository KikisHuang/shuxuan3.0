package com.gxdingo.sg.dialog;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gxdingo.sg.R;
import com.kikis.commnlibrary.biz.CustomArgsResultListener;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.core.PositionPopupView;

/**
 * @author: kikis
 * @date: 2021/12/25
 * @page:
 */
public class ChatFunctionDialog extends CenterPopupView {

    private CustomArgsResultListener customArgsResultListener;

    private LinearLayout copy_ll, report_ll, del_ll;

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
        copy_ll = findViewById(R.id.copy_ll);
        report_ll = findViewById(R.id.report_ll);
        del_ll = findViewById(R.id.del_ll);

        report_ll.setVisibility(isSelf ? VISIBLE : GONE);

        copy_ll.setOnClickListener(view -> {
            customArgsResultListener.onResult(0);
            dismiss();
        });

        report_ll.setOnClickListener(view -> {
            customArgsResultListener.onResult(1);
            dismiss();
        });
    }

}
