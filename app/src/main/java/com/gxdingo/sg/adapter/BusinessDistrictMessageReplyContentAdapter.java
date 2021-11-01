package com.gxdingo.sg.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;

import org.jetbrains.annotations.NotNull;

/**
 * 商圈消息-回复内容适配器
 *
 * @author JM
 */
public class BusinessDistrictMessageReplyContentAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {

    public BusinessDistrictMessageReplyContentAdapter() {
        super(R.layout.module_item_business_district_message_reply_content);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Object o) {
        
    }
}
