package com.gxdingo.sg.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.dialog.IMSelectTransferAccountsWayPopupView;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * IM选择转账方式适配器
 *
 * @author JM
 */
public class IMSelectTransferAccountsWayAdapter extends BaseQuickAdapter<IMSelectTransferAccountsWayPopupView.TransferAccountsWay, BaseViewHolder> {

    public IMSelectTransferAccountsWayAdapter() {
        super(R.layout.module_item_im_select_transfer_accounts_way);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, IMSelectTransferAccountsWayPopupView.TransferAccountsWay data) {
        baseViewHolder.setBackgroundResource(R.id.iv_icon, data.icon);
        baseViewHolder.setText(R.id.tv_name, data.name);
    }
}
