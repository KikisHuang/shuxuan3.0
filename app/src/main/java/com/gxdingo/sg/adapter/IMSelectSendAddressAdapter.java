package com.gxdingo.sg.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.kikis.commnlibrary.bean.AddressBean;

import org.jetbrains.annotations.NotNull;

import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * IM选择发送地址适配器
 *
 * @author JM
 */
public class IMSelectSendAddressAdapter extends BaseQuickAdapter<AddressBean, BaseViewHolder> {

    public IMSelectSendAddressAdapter() {
        super(R.layout.module_item_im_select_send_address);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, AddressBean data) {


        if (!isEmpty(data.getName()))
            baseViewHolder.setText(R.id.tv_nick_name, data.getName());

        if (!isEmpty(data.getMobile()))
            baseViewHolder.setText(R.id.tv_phone, data.getMobile());
        if (!isEmpty(data.getStreet()))
            baseViewHolder.setText(R.id.tv_address, data.getStreet() +" "+ data.getDoorplate());

    }
}
