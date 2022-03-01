package com.gxdingo.sg.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.RankBean;
import com.kikis.commnlibrary.bean.AddressBean;
import com.kikis.commnlibrary.utils.GlideUtils;

import org.jetbrains.annotations.NotNull;

import static android.text.TextUtils.isEmpty;

/**
 * @author: Kikis
 * @date: 2022/2/10
 * @page:
 */
public class RankingAdapter extends BaseQuickAdapter<RankBean, BaseViewHolder> {


    public RankingAdapter() {
        super(R.layout.module_item_ranking);
        addChildClickViewIds(R.id.viwe_tv);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, RankBean data) {
        TextView level_tv = baseViewHolder.getView(R.id.level_tv);
        TextView store_name_tv = baseViewHolder.getView(R.id.store_name_tv);
        TextView classify_tv = baseViewHolder.getView(R.id.classify_tv);
        TextView issue_num_tv = baseViewHolder.getView(R.id.issue_num_tv);
        ImageView store_avatar_img = baseViewHolder.getView(R.id.store_avatar_img);


        if (!isEmpty(data.getAvatar()))
            Glide.with(getContext()).load(data.getAvatar()).apply(GlideUtils.getInstance().getGlideRoundOptions(6)).into(store_avatar_img);

        level_tv.setText(getItemPosition(data) + 1 + "");

        if (!isEmpty(data.getNickname()))
            store_name_tv.setText(data.getNickname());


        StringBuffer sb = new StringBuffer();

        if (data.getCategoryList() != null && data.getCategoryList().size() > 0) {
            for (int i = 0; i < data.getCategoryList().size(); i++) {
                if (i == 0)
                    sb.append(data.getCategoryList().get(i));
                else
                    sb.append(" | " + data.getCategoryList().get(i));
            }
            classify_tv.setText(sb);
        } else
            classify_tv.setText("");


        issue_num_tv.setText(data.getCount() + "");

    }
}
