package com.gxdingo.sg.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientAccountTransactionBean;

import org.jetbrains.annotations.NotNull;

/**
 * @author: Weaving
 * @date: 2021/10/21
 * @page:
 */
public class ClientTransactionRecordAdapter extends BaseQuickAdapter<ClientAccountTransactionBean.ListBean, BaseViewHolder> {


    public ClientTransactionRecordAdapter() {
        super(R.layout.module_recycle_item_transaction_record);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ClientAccountTransactionBean.ListBean listBean) {
        baseViewHolder.setText(R.id.description_tv,listBean.getDescription());
        baseViewHolder.setText(R.id.time_tv,listBean.getCreateTime());
        baseViewHolder.setText(R.id.amount_tv,String.valueOf(listBean.getAmount()));
    }
}
