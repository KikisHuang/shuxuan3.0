package com.gxdingo.sg.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.donkingliang.labels.LabelsView;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.StoreListBean;
import com.kikis.commnlibrary.utils.GlideUtils;
import com.kikis.commnlibrary.view.GlideRoundTransform;

import org.jetbrains.annotations.NotNull;

import static android.text.TextUtils.isEmpty;
import static com.kikis.commnlibrary.utils.BigDecimalUtils.div;
import static com.kikis.commnlibrary.utils.CommonUtils.HideMobile;
import static com.kikis.commnlibrary.utils.FormatUtils.double2Str;

/**
 * @author: Weaving
 * @date: 2021/10/18
 * @page:
 */
public class ClientStoreAdapter extends BaseQuickAdapter<StoreListBean.StoreBean, BaseViewHolder> {


    public ClientStoreAdapter() {
//        super(type==0?R.layout.module_recycle_item_store:R.layout.module_recycle_item_search_store);
        super(R.layout.module_recycle_item_store);
        addChildClickViewIds(R.id.store_avatar_iv, R.id.call_phone_iv);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, StoreListBean.StoreBean storeBean) {
        Glide.with(getContext())
                .load(isEmpty(storeBean.getAvatar()) ? R.drawable.module_svg_client_default_avatar : storeBean.getAvatar())
                .apply(GlideUtils.getInstance().getCircleCrop())
                .into((ImageView) baseViewHolder.getView(R.id.store_avatar_iv));

        baseViewHolder.setText(R.id.store_name_tv, storeBean.getName());

        baseViewHolder.setText(R.id.distance_tv, "距离" + (div(double2Str(storeBean.getDistance()), String.valueOf(1000), 2) + "km"));

        TextView label_tv = baseViewHolder.findView(R.id.label_tv);
        LinearLayout label_ll = baseViewHolder.findView(R.id.label_ll);
        LinearLayout state_ll = baseViewHolder.findView(R.id.state_ll);
        TextView state_tv = baseViewHolder.findView(R.id.state_tv);


        state_ll.setVisibility(storeBean.businessStatus == 1 ? View.GONE : View.VISIBLE);

        label_ll.setVisibility(storeBean.businessStatus == 1 ? View.VISIBLE : View.GONE);


        if (storeBean.businessStatus == 0 || storeBean.businessStatus == 2)
            state_tv.setText(storeBean.businessStatus == 0 ? "暂停营业" : "休息中");

        if (storeBean.getClassNameList() != null && storeBean.getClassNameList().size() > 0) {

            String label = "";

            for (int i = 0; i < storeBean.getClassNameList().size(); i++) {
                if (i == 0)
                    label = storeBean.getClassNameList().get(i);
                else
                    label += " | " + storeBean.getClassNameList().get(i);
            }
            label_tv.setText(label);
        }


        if (storeBean.isShowTop()) {
            baseViewHolder.getView(R.id.ll_nearby_store).setVisibility(View.VISIBLE);
        } else {
            baseViewHolder.getView(R.id.ll_nearby_store).setVisibility(View.GONE);
        }

        if (!isEmpty(storeBean.getContactNumber()))
            baseViewHolder.setText(R.id.phone_number_tv, HideMobile(storeBean.getContactNumber()));

    }
}
