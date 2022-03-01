package com.gxdingo.sg.adapter;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.BannerBean;
import com.gxdingo.sg.bean.CategoriesBean;
import com.kikis.commnlibrary.adapter.BaseRecyclerAdapter;
import com.kikis.commnlibrary.adapter.RecyclerViewHolder;
import com.kikis.commnlibrary.bean.AddressBean;
import com.kikis.commnlibrary.utils.GlideUtils;

import org.jetbrains.annotations.NotNull;

import static com.blankj.utilcode.util.StringUtils.isEmpty;

/**
 * @author: kikis
 * @date: 2022/1/19
 * @page:
 */
public class BusinessDistrictLabelAdapter extends BaseQuickAdapter<BannerBean.IconListDTO, BaseViewHolder> {


    public BusinessDistrictLabelAdapter() {
        super(R.layout.module_item_business_district_label);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, BannerBean.IconListDTO data) {

        ImageView icon = baseViewHolder.findView(R.id.icon_img);

        if (!isEmpty(data.getName()))
            baseViewHolder.setText(R.id.title_tv, data.getName());

        Glide.with(getContext()).load(data.getIconUrl()).apply(GlideUtils.getInstance().getDefaultOptions()).into(icon);

    }
}
