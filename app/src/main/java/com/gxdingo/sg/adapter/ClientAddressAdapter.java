package com.gxdingo.sg.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.kikis.commnlibrary.bean.AddressBean;

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
        addChildClickViewIds(R.id.btn_edit);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, AddressBean addressBean) {
        TextView tv_receiver = baseViewHolder.getView(R.id.tv_receiver);
        TextView tv_phone_number = baseViewHolder.getView(R.id.tv_phone_number);
        TextView tv_address = baseViewHolder.getView(R.id.tv_address);

        tv_receiver.setText(!isEmpty(addressBean.getName()) ? addressBean.getName() : "");

        tv_phone_number.setText(!isEmpty(addressBean.getMobile()) ? addressBean.getMobile() : "");

        tv_address.setText(addressBean.getStreet() +" "+ addressBean.getDoorplate());

    }
}
