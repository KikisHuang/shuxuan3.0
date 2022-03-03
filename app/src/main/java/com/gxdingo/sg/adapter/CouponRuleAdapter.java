package com.gxdingo.sg.adapter;

import android.graphics.Color;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientCouponBean;

import org.jetbrains.annotations.NotNull;

import static com.kikis.commnlibrary.utils.CommonUtils.getc;

/**
 * @author: Kikis
 * @date: 2022/3/3
 * @page:优惠券规则适配器
 */
public class CouponRuleAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public CouponRuleAdapter() {
        super(R.layout.module_recycle_item_coupon_rule);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String string) {

        TextView rule_tv = baseViewHolder.findView(R.id.rule_tv);
    }
}
