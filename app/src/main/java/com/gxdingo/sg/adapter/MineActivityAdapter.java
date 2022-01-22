package com.gxdingo.sg.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientCouponBean;
import com.gxdingo.sg.utils.DateUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @author: Kikis
 * @date: 2022/kikis/22
 * @page:
 */
public class MineActivityAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public MineActivityAdapter() {
        super(R.layout.module_recycle_item_mine_activity);
        addChildClickViewIds(R.id.btn_use);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String string) {
//        baseViewHolder.setText(R.id.ac_name_tv, clientCouponBean.getCouponName());

//        baseViewHolder.setText(R.id.welfare_tv, String.valueOf(clientCouponBean.getCouponAmount()));
        ImageView ac_img = baseViewHolder.itemView.findViewById(R.id.ac_img);

    }
}
