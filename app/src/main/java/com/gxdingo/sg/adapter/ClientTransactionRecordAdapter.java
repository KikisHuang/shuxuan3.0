package com.gxdingo.sg.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientAccountTransactionBean;
import com.gxdingo.sg.bean.TransactionBean;

import org.jetbrains.annotations.NotNull;

import static com.gxdingo.sg.utils.DateUtils.dealDateFormat;

/**
 * @author: Weaving
 * @date: 2021/10/21
 * @page:
 */
public class ClientTransactionRecordAdapter extends BaseQuickAdapter<TransactionBean, BaseViewHolder> {


    public ClientTransactionRecordAdapter() {
        super(R.layout.module_recycle_item_transaction_record);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, TransactionBean listBean) {
        baseViewHolder.setText(R.id.description_tv,listBean.getDescription());
        baseViewHolder.setText(R.id.time_tv,dealDateFormat(listBean.getCreateTime(),"MM-dd HH:mm"));
        baseViewHolder.setText(R.id.amount_tv,String.valueOf(listBean.getAmount()));
    }
}
