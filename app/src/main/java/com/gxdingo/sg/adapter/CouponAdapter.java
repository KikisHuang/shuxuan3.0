package com.gxdingo.sg.adapter;

import android.graphics.Color;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientCouponBean;
import com.gxdingo.sg.utils.DateUtils;

import org.jetbrains.annotations.NotNull;

import static com.blankj.utilcode.util.TimeUtils.getNowMills;
import static com.blankj.utilcode.util.TimeUtils.string2Millis;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;

/**
 * @author: Weaving
 * @date: 2021/10/25
 * @page:
 */
public class CouponAdapter extends BaseQuickAdapter<ClientCouponBean, BaseViewHolder> {

    public CouponAdapter() {
        super(R.layout.module_recycle_item_coupon);
        addChildClickViewIds(R.id.btn_use, R.id.rule_tv);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ClientCouponBean clientCouponBean) {

        TextView btn_use = baseViewHolder.findView(R.id.btn_use);
        TextView coupon_name_tv = baseViewHolder.findView(R.id.coupon_name_tv);
        TextView valid_date_tv = baseViewHolder.findView(R.id.valid_date_tv);
        TextView rule_tv = baseViewHolder.findView(R.id.rule_tv);
        TextView order_coupon_tv = baseViewHolder.findView(R.id.order_coupon_tv);
        TextView coupon_amount_tv = baseViewHolder.findView(R.id.coupon_amount_tv);
        TextView rmb_symbol = baseViewHolder.findView(R.id.rmb_symbol);

        coupon_amount_tv.setText(clientCouponBean.getCouponAmount());

        coupon_name_tv.setText(clientCouponBean.getCouponName());

        valid_date_tv.setText(clientCouponBean.getExpireTime());
        // 状态。0=待使用；1=已使用；2=已过期
        boolean isPastDue = clientCouponBean.getStatus() == 1 || clientCouponBean.getStatus() == 2;

        coupon_name_tv.setTextColor(isPastDue ? getc(R.color.graya9a9a9) : getc(R.color.graye2e2e2));
//        valid_date_tv.setTextColor(isPastDue ? getc(R.color.graya9a9a9) : getc(R.color.graya9a9a9));
        order_coupon_tv.setTextColor(isPastDue ? getc(R.color.graya9a9a9) : Color.parseColor("#A3BC69"));
        coupon_amount_tv.setTextColor(isPastDue ? getc(R.color.graya9a9a9) : Color.parseColor("#C30404"));
        rmb_symbol.setTextColor(isPastDue ? getc(R.color.graya9a9a9) : Color.parseColor("#C30404"));

        if (clientCouponBean.getStatus() == 1)
            btn_use.setText("已使用");
        else if (clientCouponBean.getStatus() == 2)
            btn_use.setText("已过期");
        else
            btn_use.setText("立即使用");

        btn_use.setBackgroundResource(isPastDue ? R.drawable.module_shape_bg_grey_15r : R.drawable.module_shape_bg_green_15r);

        baseViewHolder.setText(R.id.order_coupon_tv, Float.valueOf(clientCouponBean.getUseAmount()) <= 0 ? "无门槛" : "满" + clientCouponBean.getUseAmount() + "减" + clientCouponBean.getCouponAmount());


    }
}
