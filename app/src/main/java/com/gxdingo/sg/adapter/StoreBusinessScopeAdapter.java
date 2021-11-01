package com.gxdingo.sg.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.StoreBusinessScopeBean;

import org.jetbrains.annotations.NotNull;

/**
 * @author: Weaving
 * @date: 2021/6/2
 * @page:
 */
public class StoreBusinessScopeAdapter extends BaseQuickAdapter<StoreBusinessScopeBean.ListBean, BaseViewHolder> {

    public StoreBusinessScopeAdapter() {
        super(R.layout.module_recycle_item_store_business_scope);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, StoreBusinessScopeBean.ListBean listBean) {
        baseViewHolder.setText(R.id.tv_category,listBean.getName());
        ImageView ivSelect = baseViewHolder.getView(R.id.iv_selected_category);
        if (listBean.isSelect())
            ivSelect.setImageResource(R.drawable.module_svg_check);
        else
            ivSelect.setImageResource(R.drawable.module_svg_uncheck);

    }
}
