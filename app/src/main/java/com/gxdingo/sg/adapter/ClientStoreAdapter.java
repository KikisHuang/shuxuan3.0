package com.gxdingo.sg.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.donkingliang.labels.LabelsView;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.StoreListBean;
import com.kikis.commnlibrary.view.GlideRoundTransform;

import org.jetbrains.annotations.NotNull;

import static android.text.TextUtils.isEmpty;
import static com.kikis.commnlibrary.utils.BigDecimalUtils.div;
import static com.kikis.commnlibrary.utils.FormatUtils.double2Str;

/**
 * @author: Weaving
 * @date: 2021/10/18
 * @page:
 */
public class ClientStoreAdapter extends BaseQuickAdapter<StoreListBean.StoreBean, BaseViewHolder> {

    //0首页店铺item 1搜索店铺item
    private int mType;

    public ClientStoreAdapter() {
//        super(type==0?R.layout.module_recycle_item_store:R.layout.module_recycle_item_search_store);
        super(R.layout.module_recycle_item_store);
//        mType = type;
//        if (type == 0)
        addChildClickViewIds(R.id.store_avatar_iv, R.id.call_phone_iv);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, StoreListBean.StoreBean storeBean) {
        Glide.with(getContext())
                .load(isEmpty(storeBean.getAvatar()) ? R.drawable.module_svg_client_default_avatar : storeBean.getAvatar())
                .apply(RequestOptions.bitmapTransform(new GlideRoundTransform(6)))
                .into((ImageView) baseViewHolder.getView(R.id.store_avatar_iv));
        baseViewHolder.setText(R.id.store_name_tv, storeBean.getName());

        baseViewHolder.setText(R.id.distance_tv, "距离" + (div(double2Str(storeBean.getDistance()), String.valueOf(1000), 2) + "km"));

        LabelsView labelsView = baseViewHolder.getView(R.id.store_label_lv);

        labelsView.setLabels(storeBean.getClassNameList());

        if (storeBean.isShowTop()) {
            baseViewHolder.getView(R.id.ll_nearby_store).setVisibility(View.VISIBLE);
        } else {
            baseViewHolder.getView(R.id.ll_nearby_store).setVisibility(View.GONE);
        }

//        if (mType == 0)
        baseViewHolder.setText(R.id.phone_number_tv, storeBean.getContactNumber());

    }
}
