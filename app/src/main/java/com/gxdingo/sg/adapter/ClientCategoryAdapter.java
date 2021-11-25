package com.gxdingo.sg.adapter;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.CategoriesBean;
import com.kikis.commnlibrary.adapter.BaseRecyclerAdapter;
import com.kikis.commnlibrary.adapter.RecyclerViewHolder;

import java.util.List;

import static com.blankj.utilcode.util.StringUtils.isEmpty;

/**
 * @author: Weaving
 * @date: 2021/10/18
 * @page:
 */
public class ClientCategoryAdapter extends BaseRecyclerAdapter {

    private int width = 0;

    public ClientCategoryAdapter() {
        super(null);
        width = (int) (ScreenUtils.getScreenWidth() * 1 / 5.5);
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.module_recycle_item_category;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, Object item) {
        CategoriesBean categoriesBean = (CategoriesBean) item;

        LinearLayout class_item = holder.getLinearLayout(R.id.class_item);

        ImageView class_img = holder.getImageView(R.id.class_img);
        TextView class_name_tv = holder.getTextView(R.id.class_name_tv);


        class_item.getLayoutParams().height = width;

        if (getItemCount() > 4 && position == getItemCount() - 1) {

            if (!categoriesBean.isSelected) {
                Glide.with(mContext).load(R.drawable.module_svg_category_expand).into(class_img);
                class_name_tv.setText("更多");
            } else {
                Glide.with(mContext).load(R.drawable.module_svg_category_unexpand).into(class_img);
                class_name_tv.setText("收起");
            }

        } else
            Glide.with(mContext).load(isEmpty(categoriesBean.getImage()) ? R.mipmap.ic_default_avatar : categoriesBean.getImage()).into(class_img);


        if (categoriesBean.getName() != null)
            class_name_tv.setText(categoriesBean.getName());
    }


}
