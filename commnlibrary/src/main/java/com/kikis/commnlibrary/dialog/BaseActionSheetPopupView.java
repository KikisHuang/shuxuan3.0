package com.kikis.commnlibrary.dialog;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kikis.commnlibrary.R;
import com.kikis.commnlibrary.adapter.ActionSheetAdapter;
import com.kikis.commnlibrary.adapter.BaseRecyclerAdapter;
import com.lxj.xpopup.core.BottomPopupView;

import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.isEmpty;

/**
 * @author: Kikis
 * @date: 2021/3/12
 * @page: 按需求自定义的底部弹窗 BaseActionSheetPopupView
 */
public class BaseActionSheetPopupView extends BottomPopupView implements View.OnClickListener {

    private List<String> mItems;

    private RecyclerView mRecyclerView;

    private BaseRecyclerAdapter.OnItemClickListener listener;

    public BaseActionSheetPopupView(@NonNull Context context) {
        super(context);
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_base_action_sheet_xpopup;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        findViewById(R.id.txt_cancel).setOnClickListener(this);

        ActionSheetAdapter mAdapter = new ActionSheetAdapter(mItems);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                if (listener != null)
                    listener.onItemClick(itemView, pos);

                //点击item自动隐藏方法
                dismiss();
            }
        });
    }


    /**
     * 添加条目
     *
     * @param itemsName) 条目名称
     * @return
     */
    public BaseActionSheetPopupView addSheetItem(String... itemsName) {
        if (mItems == null)
            mItems = new ArrayList<>();

        for (String str : itemsName) {
            if (!isEmpty(str))
                mItems.add(str);
        }
        return this;
    }

    /**
     * 设置item点击事件监听
     *
     * @param listener
     */
    public BaseActionSheetPopupView setItemClickListener(BaseRecyclerAdapter.OnItemClickListener listener) {
        this.listener = listener;
        return this;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txt_cancel)
            dismiss();

    }

}
