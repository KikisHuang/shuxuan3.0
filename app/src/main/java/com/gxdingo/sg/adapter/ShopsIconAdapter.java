package com.gxdingo.sg.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.CategoriesBean;
import com.kikis.commnlibrary.utils.GlideUtils;

import org.jetbrains.annotations.NotNull;

import static com.blankj.utilcode.util.StringUtils.isEmpty;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;

/**
 * @author: kikis
 * @date: 2022/3/8
 * @page:商家认证图标适配器
 */
public class ShopsIconAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public ShopsIconAdapter() {
        super(R.layout.module_recycle_item_shops_icon);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder holder, String iconUrl) {

        ImageView icon_img = holder.getView(R.id.icon_img);

        Glide.with(getContext()).load(iconUrl).apply(GlideUtils.getInstance().getDefaultOptions().fitCenter()).into(icon_img);

    }
}
