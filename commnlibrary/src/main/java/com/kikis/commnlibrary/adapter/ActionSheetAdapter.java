package com.kikis.commnlibrary.adapter;


import android.view.View;
import android.widget.TextView;

import com.kikis.commnlibrary.R;

import java.util.List;

/**
 * @author: Kikis
 * @date: 2021/3/12
 * @page:
 */
public class ActionSheetAdapter extends BaseRecyclerAdapter {

    public ActionSheetAdapter(List list) {
        super(list);
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.module_recycle_item_action_sheet;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, Object item) {
        String name = (String) item;

        TextView top_line_tv = holder.getTextView(R.id.top_line_tv);

        top_line_tv.setVisibility(position == 0 ? View.GONE : View.VISIBLE);

        holder.setText(R.id.name_tv, name);

    }
}
