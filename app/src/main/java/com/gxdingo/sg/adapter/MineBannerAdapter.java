package com.gxdingo.sg.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientMineBean;
import com.gxdingo.sg.bean.HomeBannerBean;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

import static com.blankj.utilcode.util.ScreenUtils.getScreenWidth;

/**
 * 自定我的页面banner布局
 */
public class MineBannerAdapter extends BannerAdapter<ClientMineBean.AdsListBean, MineBannerAdapter.BannerViewHolder> {

    private Context mContext;

    public MineBannerAdapter(Context context, List<ClientMineBean.AdsListBean> mDatas) {
        //设置数据，也可以调用banner提供的方法,或者自己在adapter中实现
        super(mDatas);
        mContext = context;
    }

    //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.module_include_home_page_banner, new LinearLayout(mContext), false);
        return new BannerViewHolder((LinearLayout) view);
    }


    @Override
    public void onBindView(BannerViewHolder holder, ClientMineBean.AdsListBean data, int position, int size) {

        Glide.with(mContext)
                .load(data.getImage())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(6)))
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @androidx.annotation.Nullable Transition<? super Drawable> transition) {
                        int width = resource.getIntrinsicWidth();
                        int height = resource.getIntrinsicHeight();

                        int newheight = getScreenWidth() * height / width;

                        holder.imageView.setImageDrawable(resource);
                        holder.imageView.getLayoutParams().height = newheight;
                    }
                });

    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public BannerViewHolder(@NonNull LinearLayout view) {
            super(view);
            this.imageView = view.findViewById(R.id.imgView);
        }
    }
}