package com.gxdingo.sg.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;

import org.jetbrains.annotations.NotNull;

/**
 * @author: kikis
 * @date: 2022/1/19
 * @page:
 */
public class BusinessDistrictReleaseLabelAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public BusinessDistrictReleaseLabelAdapter() {
        super(R.layout.module_item_business_district_release_label);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String data) {

        ImageView icon = baseViewHolder.findView(R.id.icon_img);

        baseViewHolder.setText(R.id.tag_tv, "新品上架");

    }
}
