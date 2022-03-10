package com.gxdingo.sg.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.StoreAuthInfoBean;
import com.kikis.commnlibrary.utils.GlideUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * @author: Kikis
 * @date: 2022/3/10
 * @page:
 */
public class UploadSpecialQualificationAdapter extends BaseQuickAdapter<StoreAuthInfoBean.CategoryListBean, BaseViewHolder> {


    public UploadSpecialQualificationAdapter() {
        super(R.layout.module_recycle_item_upload_special_qualification);
        addChildClickViewIds(R.id.upload_tv, R.id.qualification_img);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, StoreAuthInfoBean.CategoryListBean data) {

        if (data.getType() == 0) {
            //普通品类隐藏布局
            baseViewHolder.itemView.setVisibility(View.GONE);
            // 需要将GroupView中的所有子View宽高都设置0
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(0, 0);//根据自己的布局，来选择相应的LayoutParams
            baseViewHolder.itemView.setLayoutParams(layoutParams);
            return;
        } else {
            //特殊品类
            baseViewHolder.itemView.setVisibility(View.VISIBLE);

            TextView upload_tv = baseViewHolder.findView(R.id.upload_tv);

            TextView name_tv = baseViewHolder.findView(R.id.name_tv);

            ImageView qualification_img = baseViewHolder.findView(R.id.qualification_img);


            upload_tv.setText(data.unUpload ? "重新上传" : "点击上传");

            if (!isEmpty(data.licenceName))
                name_tv.setText(data.licenceName);

            if (data.unUpload) {
                Glide.with(getContext()).load(data.getProve()).apply(new RequestOptions()
                        .dontAnimate()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)).into(qualification_img);

                qualification_img.setVisibility(View.VISIBLE);
            } else
                qualification_img.setVisibility(View.GONE);

        }


    }
}
