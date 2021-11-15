package com.gxdingo.sg.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.kikis.commnlibrary.bean.SubscribesListBean;
import com.kikis.commnlibrary.utils.GlideUtils;

import org.jetbrains.annotations.NotNull;

import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * 商家首页搜索结果适配器
 *
 * @author JM
 */
public class StoreHomeSearchResultAdapter extends BaseQuickAdapter<SubscribesListBean.SubscribesMessage, BaseViewHolder> {

    public StoreHomeSearchResultAdapter() {
        super(R.layout.module_item_store_home_search_result);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, SubscribesListBean.SubscribesMessage data) {

        ImageView iv_avatar = baseViewHolder.getView(R.id.iv_avatar);

        Glide.with(getContext()).load(data.getSendAvatar()).apply(GlideUtils.getInstance().getGlideRoundOptions(6)).into(iv_avatar);


        if (!isEmpty(data.getSendNickname()))
            baseViewHolder.setText(R.id.tv_name, data.getSendNickname());


    }
}
