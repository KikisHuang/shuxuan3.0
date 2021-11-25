package com.gxdingo.sg.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.gxdingo.sg.R;
import com.kikis.commnlibrary.utils.GlideUtils;
import com.kikis.commnlibrary.view.GlideRoundTransform;
import com.lzy.ninegrid.NineGridView;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.ConvertUtils.dp2px;


/**
 * Created by Kikis on 2021/4/25.
 * NineGrid九宫格图片加载器
 */

public class NineGridGlideImageLoader implements NineGridView.ImageLoader {

    @Override
    public void onDisplayImage(Context context, ImageView imageView, String url) {
/*
        Glide.with(context).load(url).apply(GlideUtils.getInstance().getDefaultOptions().placeholder(R.drawable.load_faile_icon))
                .into(imageView);*/


        RequestOptions options = new RequestOptions().transform(new CenterCrop());
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(options)
                .into(new BitmapImageViewTarget(imageView){
                    @Override
                    protected void setResource(Bitmap resource) {
                        super.setResource(resource);
                        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        roundedBitmapDrawable.setCornerRadius(dp2px(6));
                        imageView.setImageDrawable(roundedBitmapDrawable);
                    }
                });

    }

    @Override
    public Bitmap getCacheImage(String url) {
        return null;
    }
}
