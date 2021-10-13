package com.kikis.commnlibrary.dialog;

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
import com.kikis.commnlibrary.R;
import com.kikis.commnlibrary.R2;
import com.kikis.commnlibrary.adapter.BottomListDialogAdapter;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.lxj.xpopup.core.BottomPopupView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author: Kikis
 * @date: 2021/4/29
 * @page: 底部列表弹窗 BaseActionSheetPopupView
 */
public class BaseBottomListPopupView extends BottomPopupView {

    @BindView(R2.id.title_tv)
    public TextView title_tv;

    @BindView(R2.id.close_img)
    public ImageView close_img;

    private CustomResultListener listener;

    private BottomListDialogAdapter mAdapter;
    private List<String> itemsData;

    @BindView(R2.id.recyclerView)
    public RecyclerView recyclerView;

    public BaseBottomListPopupView(@NonNull Context context, CustomResultListener listener, List<String> itemsData) {
        super(context);
        this.listener = listener;
        this.itemsData = itemsData;
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_base_bottom_list_xpopup;
    }

    @Override
    protected int getMaxHeight() {
        return ScreenUtils.getAppScreenHeight() / 2;
    }


    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        ButterKnife.bind(this, this);

        mAdapter = new BottomListDialogAdapter(itemsData);

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

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
