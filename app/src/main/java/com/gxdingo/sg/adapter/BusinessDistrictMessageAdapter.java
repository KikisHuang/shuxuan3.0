package com.gxdingo.sg.adapter;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * 商圈消息适配器
 *
 * @author JM
 */
public class BusinessDistrictMessageAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {

    public BusinessDistrictMessageAdapter() {
        super(R.layout.module_item_business_district_message);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Object o) {
        RecyclerView recyclerView = baseViewHolder.findView(R.id.rv_reply_content);
        BusinessDistrictMessageReplyContentAdapter adapter = new BusinessDistrictMessageReplyContentAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        ArrayList<Object> datas = new ArrayList<>();
        datas.add(new Object());
        datas.add(new Object());
        datas.add(new Object());
        adapter.setList(datas);
    }
}
