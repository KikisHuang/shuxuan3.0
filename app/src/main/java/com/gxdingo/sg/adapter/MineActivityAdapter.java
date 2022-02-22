package com.gxdingo.sg.adapter;

import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientCouponBean;
import com.gxdingo.sg.bean.ClientMineBean;
import com.gxdingo.sg.utils.DateUtils;

import org.jetbrains.annotations.NotNull;

import static com.kikis.commnlibrary.utils.ScreenUtils.dp2px;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * @author: Kikis
 * @date: 2022/kikis/22
 * @page:
 */
public class MineActivityAdapter extends BaseQuickAdapter<ClientMineBean.AdsListBean, BaseViewHolder> {

    public MineActivityAdapter() {
        super(R.layout.module_recycle_item_mine_activity);
        addChildClickViewIds(R.id.btn_use);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ClientMineBean.AdsListBean data) {

        ImageView ac_img = baseViewHolder.itemView.findViewById(R.id.ac_img);
        ConstraintLayout item_cl = baseViewHolder.itemView.findViewById(R.id.item_cl);

        item_cl.setPadding(dp2px(15), 0, getItemPosition(data) % 2 != 0 ? dp2px(15) : 0, 0);

        if (!isEmpty(data.getTitle()))
            baseViewHolder.setText(R.id.ac_name_tv, data.getTitle());


        if (!isEmpty(data.getRemark()))
            baseViewHolder.setText(R.id.welfare_tv, data.getRemark());


        if (!isEmpty(data.getImage()))
            Glide.with(getContext()).load(data.getImage()).into(ac_img);
    }
}
