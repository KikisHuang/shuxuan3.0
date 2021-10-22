package com.gxdingo.sg.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.StoreListBean;
import com.kikis.commnlibrary.view.GlideRoundTransform;

import org.jetbrains.annotations.NotNull;

import static android.text.TextUtils.isEmpty;

/**
 * @author: Weaving
 * @date: 2021/10/18
 * @page:
 */
public class ClientStoreAdapter extends BaseQuickAdapter<StoreListBean.StoreBean, BaseViewHolder> {

    public ClientStoreAdapter() {
        super(R.layout.module_recycle_item_store);
        addChildClickViewIds(R.id.call_phone_iv);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, StoreListBean.StoreBean storeBean) {
        Glide.with(getContext())
                .load(isEmpty(storeBean.getAvatar())?R.drawable.module_svg_client_default_avatar:storeBean.getAvatar())
                .apply(RequestOptions.bitmapTransform(new GlideRoundTransform(6)))
                .into((ImageView) baseViewHolder.getView(R.id.store_avatar_iv));
        baseViewHolder.setText(R.id.store_name_tv,storeBean.getName());
        baseViewHolder.setText(R.id.distance_tv,"距离"+storeBean.getDistance());
        baseViewHolder.setText(R.id.phone_number_tv,storeBean.getContactNumber());
    }
}
