package com.gxdingo.sg.adapter;

import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientCouponBean;
import com.kikis.commnlibrary.utils.GlideUtils;

import org.jetbrains.annotations.NotNull;

import static com.kikis.commnlibrary.utils.CommonUtils.getc;

/**
 * @author: Kikis
 * @date: 2022/3/10
 * @page:
 */
public class QualificationAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int height;

    public QualificationAdapter(int height) {
        super(R.layout.module_recycle_item_qualification);
        this.height = height;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String url) {

        ImageView img = baseViewHolder.findView(R.id.img);

        img.getLayoutParams().height = height;

        Glide.with(getContext()).load(url).apply(GlideUtils.getInstance().getDefaultOptions()).into(img);
    }
}
