package com.kikis.commnlibrary.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kikis.commnlibrary.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.blankj.utilcode.util.ScreenUtils.getScreenWidth;

/**
 * @author: Kikis
 * @date: 2021/5/17
 * @page:
 */
public class BottomListDialogAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public BottomListDialogAdapter(@Nullable List<String> data) {
        super(R.layout.module_recycle_item_bottom_list, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String data) {

        TextView name_tv = baseViewHolder.getView(R.id.name_tv);
        name_tv.setText(data);
    }
}
