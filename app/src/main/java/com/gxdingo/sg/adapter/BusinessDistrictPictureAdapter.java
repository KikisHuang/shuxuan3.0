package com.gxdingo.sg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gxdingo.sg.R;
import com.kikis.commnlibrary.view.RoundAngleImageView;
import com.kikis.commnlibrary.view.recycler_view.PullRecyclerView;

import java.util.ArrayList;

/**
 * 商圈图片适配器
 */
public class BusinessDistrictPictureAdapter extends PullRecyclerView.PullAdapter<BusinessDistrictPictureAdapter.PictureViewHolder> {

    Context mContext;
    ArrayList<String> mImages;

    public BusinessDistrictPictureAdapter(Context context, ArrayList<String> images) {
        mContext = context;
        mImages = images;
    }

    @NonNull
    @Override
    public PictureViewHolder onPullCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.module_item_business_district_picture, parent, false);
        return new PictureViewHolder(view);
    }

    @Override
    public void onPullBindViewHolder(@NonNull PictureViewHolder holder, int position) {
        Glide.with(mContext).load(mImages.get(position)).apply(getRequestOptions()).into(holder.imageView);
    }

    @Override
    public int getPullItemCount() {
        if (mImages == null) {
            return 0;
        }
        return mImages.size();
    }

    public class PictureViewHolder extends RecyclerView.ViewHolder {
        RoundAngleImageView imageView;

        public PictureViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }

    private RequestOptions getRequestOptions() {
        RequestOptions options = new RequestOptions();
        options.placeholder(com.kikis.commnlibrary.R.mipmap.news_default_icon);    //加载成功之前占位图
        options.error(com.kikis.commnlibrary.R.mipmap.news_default_icon);    //加载错误之后的错误图
        return options;
    }
}
