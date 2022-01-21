package com.gxdingo.sg.dialog;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gxdingo.sg.R;
import com.kikis.commnlibrary.adapter.BaseRecyclerAdapter;
import com.lxj.xpopup.core.PositionPopupView;

import java.util.List;

/**
 * @author: kikis
 * @date: 2021/12/25
 * @page:
 */
public class PostionFunctionDialog extends PositionPopupView {

    private OnClickListener listener;

    //0 分享 1 分享、投诉 2 资质、分享、投诉
    private int type = 0;


    public PostionFunctionDialog(@NonNull Context context, OnClickListener listener) {
        super(context);
        this.listener = listener;
    }

    public PostionFunctionDialog(@NonNull Context context, OnClickListener listener, int type) {
        super(context);
        this.listener = listener;
        this.type = type;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_function;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        LinearLayout share_ll = findViewById(R.id.share_ll);
        LinearLayout report_ll = findViewById(R.id.report_ll);
        LinearLayout certification_ll = findViewById(R.id.certification_ll);

        certification_ll.setVisibility(type == 2 ? VISIBLE : GONE);

        if (type == 0)
            report_ll.setVisibility(GONE);

        if (listener != null) {
            share_ll.setOnClickListener(v -> {
                listener.onClick(v);
                dismiss();
            });
            certification_ll.setOnClickListener(v -> {
                listener.onClick(v);
                dismiss();
            });
            report_ll.setOnClickListener(v -> {
                listener.onClick(v);
                dismiss();
            });
        }
    }

}
