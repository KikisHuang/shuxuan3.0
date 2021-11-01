package com.gxdingo.sg.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.kikis.commnlibrary.utils.GlideUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @author: Weaving
 * @date: 2021/10/20
 * @page:
 */
public class ClientStorePhotoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public ClientStorePhotoAdapter() {
        super(R.layout.module_recycle_item_client_store_photo);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String s) {
        ImageView photo_img = baseViewHolder.getView(R.id.photo_iv);

        Glide.with(getContext()).load(s).apply(GlideUtils.getInstance().getDefaultOptions()).into(photo_img);
    }
}
