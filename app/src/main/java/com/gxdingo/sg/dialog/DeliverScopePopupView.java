package com.gxdingo.sg.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gxdingo.sg.bean.DistanceBean;
import com.kikis.commnlibrary.R2;
import com.kikis.commnlibrary.adapter.BottomListDialogAdapter;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.lxj.xpopup.core.BottomPopupView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author: Weaving
 * @date: 2021/6/24
 * @page:
 */
public class DeliverScopePopupView extends BottomPopupView {

    @BindView(R2.id.title_tv)
    public TextView title_tv;

    @BindView(R2.id.close_img)
    public ImageView close_img;

    private CustomResultListener listener;

    private BottomListDialogAdapter mAdapter;

    private List<DistanceBean> itemsData;

    @BindView(R2.id.recyclerView)
    public RecyclerView recyclerView;

    public DeliverScopePopupView(@NonNull Context context, CustomResultListener listener, List<DistanceBean> distanceBeans) {
        super(context);
        this.listener = listener;
        this.itemsData = distanceBeans;
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return com.kikis.commnlibrary.R.layout.module_dialog_base_bottom_list_xpopup;
    }

    @Override
    protected int getMaxHeight() {
        return ScreenUtils.getAppScreenHeight() / 2;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        ButterKnife.bind(this, this);
        title_tv.setText("配送范围");
        List<String> data = new ArrayList<>();
        for (DistanceBean d : itemsData )
            data.add(d.getName());

        mAdapter = new BottomListDialogAdapter(data);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (listener != null) {
                    listener.onResult(position);
                }
                dismiss();
            }
        });
        close_img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
