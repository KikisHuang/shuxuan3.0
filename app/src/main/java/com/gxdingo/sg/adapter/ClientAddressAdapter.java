package com.gxdingo.sg.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.AddressBean;

import org.jetbrains.annotations.NotNull;

import static android.text.TextUtils.isEmpty;
import static com.kikis.commnlibrary.utils.CommonUtils.getd;

/**
 * @author: Weaving
 * @date: 2021/10/17
 * @page:
 */
public class ClientAddressAdapter extends BaseQuickAdapter<AddressBean, BaseViewHolder> {


    public ClientAddressAdapter() {
        super(R.layout.module_item_client_address);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, AddressBean addressBean) {
        TextView tv_receiver = baseViewHolder.getView(R.id.tv_receiver);
        TextView tv_phone_number = baseViewHolder.getView(R.id.tv_phone_number);
        TextView tv_address = baseViewHolder.getView(R.id.tv_address);
        TextView tv_label = baseViewHolder.getView(R.id.tv_label);

        tv_receiver.setText(!isEmpty(addressBean.getName()) ? addressBean.getName() : "");

        tv_phone_number.setText(!isEmpty(addressBean.getMobile()) ? addressBean.getMobile() : "");

        tv_address.setText(addressBean.getStreet() +" "+ addressBean.getDoorplate());

        tv_label.setText(!isEmpty(addressBean.getTag()) ? addressBean.getTag() : "");

        switch (addressBean.getTag()) {
            case "公司":
                tv_label.setBackground(getd(R.drawable.module_shape_address_label_black));
                break;
            case "家":
                tv_label.setBackground(getd(R.drawable.module_shape_address_label_green));
                break;
            case "学校":
                tv_label.setBackground(getd(R.drawable.module_shape_address_label_blue));
                break;
        }
    }
}
