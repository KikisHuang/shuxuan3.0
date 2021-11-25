package com.gxdingo.sg.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gxdingo.sg.R;
import com.kikis.commnlibrary.utils.GlideUtils;
import com.kikis.commnlibrary.view.GlideRoundTransform;
import com.lzy.ninegrid.NineGridView;

import static android.text.TextUtils.isEmpty;


/**
 * Created by Kikis on 2021/4/25.
 * NineGrid九宫格图片加载器
 */

public class NineGridGlideImageLoader implements NineGridView.ImageLoader {

    @Override
    public void onDisplayImage(Context context, ImageView imageView, String url) {

        Glide.with(context).load(url).apply(GlideUtils.getInstance().getDefaultOptions().placeholder(R.drawable.load_faile_icon))
                .into(imageView);

    }

    @Override
    public Bitmap getCacheImage(String url) {
        return null;
    }
}
