package com.gxdingo.sg.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.BusinessDistrictListBean;

import org.jetbrains.annotations.NotNull;

import static com.blankj.utilcode.util.ConvertUtils.dp2px;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * @author: kikis
 * @date: 2022/1/19
 * @page:
 */
public class BusinessDistrictReleaseLabelAdapter extends BaseQuickAdapter<BusinessDistrictListBean.Labels, BaseViewHolder> {


    public BusinessDistrictReleaseLabelAdapter() {
        super(R.layout.module_item_business_district_release_label);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, BusinessDistrictListBean.Labels data) {

        TextView tag_tv = baseViewHolder.findView(R.id.tag_tv);

        if (data.isCheck) {
            GradientDrawable mGrad = (GradientDrawable) tag_tv.getBackground();
            mGrad.setStroke(dp2px(1), Color.parseColor(data.getColor()));
            mGrad.setColor(getc(R.color.white));
            tag_tv.setTextColor(Color.parseColor(data.getColor()));
        } else {
            tag_tv.setBackgroundResource(R.drawable.module_shape_border_f6_30r);
            GradientDrawable mGrad = (GradientDrawable) tag_tv.getBackground();
            mGrad.setStroke(dp2px(1), Color.parseColor("#f1efef"));
            mGrad.setColor(getc(R.color.grayf6));
            tag_tv.setTextColor(getc(R.color.graya9a9a9));
        }

        if (!isEmpty(data.getName()))
            tag_tv.setText(data.getName());

    }
}
