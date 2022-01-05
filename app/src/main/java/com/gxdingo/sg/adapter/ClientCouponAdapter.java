package com.gxdingo.sg.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientCouponBean;
import com.gxdingo.sg.utils.DateUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @author: Weaving
 * @date: 2021/10/25
 * @page:
 */
public class ClientCouponAdapter extends BaseQuickAdapter<ClientCouponBean, BaseViewHolder> {
    public ClientCouponAdapter() {
        super(R.layout.module_recycle_item_coupon);
        addChildClickViewIds(R.id.btn_use);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ClientCouponBean clientCouponBean) {
        baseViewHolder.setText(R.id.coupon_name_tv, clientCouponBean.getCouponName());

        baseViewHolder.setText(R.id.coupon_amount_tv, String.valueOf(clientCouponBean.getCouponAmount()));
        baseViewHolder.setText(R.id.valid_date_tv, DateUtils.dealDateFormat(clientCouponBean.getExpireTime()));
//        if (clientCouponBean.getOrderAmount() == 0)
//            baseViewHolder.setText(R.id.order_coupon_tv,"无门槛");
    }
}
