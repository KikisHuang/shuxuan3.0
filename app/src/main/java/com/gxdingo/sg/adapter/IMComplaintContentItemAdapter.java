package com.gxdingo.sg.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;

import org.jetbrains.annotations.NotNull;

/**
 * IM投诉内容项适配器
 * @author JM
 */
public class IMComplaintContentItemAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {

    public IMComplaintContentItemAdapter() {
        super(R.layout.module_item_im_complaint_content_item);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Object o) {

    }
}
