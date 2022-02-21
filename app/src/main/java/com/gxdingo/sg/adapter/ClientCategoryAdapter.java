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
import com.kikis.commnlibrary.utils.GlideUtils;

import java.util.List;

import static com.blankj.utilcode.util.StringUtils.isEmpty;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;

/**
 * @author: kikis
 * @date: 2022/1/19
 * @page:
 */
public class ClientCategoryAdapter extends BaseRecyclerAdapter {

    private int categoryId = 0;

    public ClientCategoryAdapter() {
        super(null);
    }


    public void setCategoryId(int id) {
        categoryId = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.module_recycle_item_category;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, Object item) {
        CategoriesBean categoriesBean = (CategoriesBean) item;


        ImageView class_img = holder.getImageView(R.id.class_img);
        TextView class_name_tv = holder.getTextView(R.id.class_name_tv);

        Glide.with(mContext).load(isEmpty(categoriesBean.getImage()) ? R.mipmap.ic_default_avatar : categoriesBean.getImage()).apply(GlideUtils.getInstance().getCircleCrop()).into(class_img);

        if (categoriesBean.getName() != null)
            class_name_tv.setText(categoriesBean.getName());

        if (categoryId == categoriesBean.getId()) {
            class_name_tv.setBackgroundResource(R.drawable.module_shape_bg_light_green_11r);
            class_name_tv.setTextColor(getc(R.color.white));
        } else {
            class_name_tv.setBackgroundResource(0);
            class_name_tv.setTextColor(getc(R.color.graya9a9a9));
        }

    }


}
