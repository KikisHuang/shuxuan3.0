package com.gxdingo.sg.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;

import org.jetbrains.annotations.NotNull;

/**
 * 商家首页搜索结果适配器
 *
 * @author JM
 */
public class StoreHomeSearchResultAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {

    public StoreHomeSearchResultAdapter() {
        super(R.layout.module_item_store_home_search_result);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Object o) {

    }
}
