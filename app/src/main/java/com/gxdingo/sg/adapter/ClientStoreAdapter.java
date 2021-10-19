package com.gxdingo.sg.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;

import org.jetbrains.annotations.NotNull;

/**
 * @author: Weaving
 * @date: 2021/10/18
 * @page:
 */
public class ClientStoreAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ClientStoreAdapter() {
        super(R.layout.module_recycle_item_store);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String s) {

    }
}
