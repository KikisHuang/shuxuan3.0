package com.gxdingo.sg.adapter;

import com.allen.library.SuperTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.WebBean;

import org.jetbrains.annotations.NotNull;

/**
 * @author: Weaving
 * @date: 2021/6/21
 * @page:
 */
public class ArticleListAdapter extends BaseQuickAdapter<WebBean, BaseViewHolder> {
    public ArticleListAdapter() {
        super(R.layout.module_recycle_item_article_list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, WebBean webBean) {
        SuperTextView superTextView = baseViewHolder.getView(R.id.stv_item_about_us);
        superTextView.setLeftString(webBean.getTitle());
    }
}
