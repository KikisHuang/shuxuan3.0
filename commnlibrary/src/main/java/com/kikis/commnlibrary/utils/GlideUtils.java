package com.kikis.commnlibrary.utils;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.kikis.commnlibrary.R;
import com.kikis.commnlibrary.view.CenterCropRoundCornerTransform;
import com.kikis.commnlibrary.view.GlideRoundedCornersTransform;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.blankj.utilcode.util.ActivityUtils.isActivityAlive;
import static com.blankj.utilcode.util.SizeUtils.dp2px;

/**
 * Created by Kikis on 2018/4/17.
 */

public class GlideUtils {
    //单例
    private static GlideUtils instance = null;
    private DrawableCrossFadeFactory drawableCrossFadeFactory = null;

    //单例模式，私有构造方法
    private GlideUtils() {
    }

    //获取单例
    public static GlideUtils getInstance() {
        if (null == instance) {
            synchronized (GlideUtils.class) {
                if (null == instance) {
                    instance = new GlideUtils();
                }
            }
        }
        return instance;
    }

    public RequestOptions getDefaultOptions() {

        return new RequestOptions()
                .error(R.drawable.load_faile_icon)
                .dontAnimate()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    /**
     * 渐现动画
     *
     * @return
     */
    public DrawableCrossFadeFactory getCrossFade() {
        if (drawableCrossFadeFactory == null)
            drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();
        return drawableCrossFadeFactory;
    }

    /**
     * 圆角
     *
     * @param dp
     * @return
     */
    public RequestOptions getRoundedOptions(int dp) {

        RequestOptions options = getDefaultOptions().bitmapTransform(new MultiTransformation(
                new RoundedCornersTransformation(dp, 0, RoundedCornersTransformation.CornerType.ALL)));

        return options;
    }

    /**
     * 顶部圆角
     *
     * @param dp
     * @return
     */
    public RequestOptions getTopRoundedOptions(int dp) {

        RequestOptions options = getDefaultOptions().optionalTransform(new GlideRoundedCornersTransform(dp, GlideRoundedCornersTransform.CornerType.TOP));

        return options;
    }

    /**
     * 圆角
     *
     * @return
     */
    public RequestOptions getGlideRoundOptions(int dp) {

        RequestOptions options = getDefaultOptions().bitmapTransform(new CenterCropRoundCornerTransform(dp2px(dp)));

        return options;
    }

    /**
     * 圆形
     *
     * @return
     */
    public RequestOptions getCircleCrop() {
        RequestOptions options = getDefaultOptions().bitmapTransform(new CircleCrop());
        return options;
    }


    /**
     * 高斯模糊
     *
     * @param radius   设置模糊度 默认”25"(在0.0到25.0之间)
     * @param sampling 图片缩放比例,默认“1”
     * @return
     */
    public RequestOptions getBlurringOptions(int radius, int sampling) {
        return getDefaultOptions()
                .bitmapTransform(new BlurTransformation(radius, sampling));
    }

    public BitmapImageViewTarget getRoundedBmp(final Context context, final ImageView img) {

        return new BitmapImageViewTarget(img) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                roundedBitmapDrawable.setCircular(true);
                img.setImageDrawable(roundedBitmapDrawable);
            }
        };
    }


    /**
     * 通用加载方法
     *
     * @param context
     * @param data
     * @param imageView
     * @param optType   : 0 centerCrop  1 fitCenter
     * @param rounded
     * @param thum
     * @return
     */
    public void GlideWith(Activity context, Object data, ImageView imageView, int optType, boolean rounded, float thum) {
        //判断Activity是否销毁
        if (context == null || isActivityAlive(context)) {

            RequestManager requestManager = Glide.with(context == null ? KikisUitls.getContext() : context);

            RequestOptions opt = GlideUtils.getInstance().getDefaultOptions();
            switch (optType) {
                case 0:
                    opt.centerCrop();
                    break;
                case 1:
                    opt.fitCenter();
                    break;
            }
            RequestBuilder requestBuilder;
            if (rounded) {
                requestBuilder = requestManager.asBitmap().load(data).apply(opt);

                if (thum > 0)
                    requestBuilder.thumbnail(thum);

                requestBuilder.into(GlideUtils.getInstance().getRoundedBmp(context == null ? KikisUitls.getContext() : context, imageView));

            } else {
                requestBuilder = requestManager.load(data).apply(opt);

                if (thum > 0)
                    requestBuilder.thumbnail(thum);

                requestBuilder.into(imageView);
            }

        }
    }

    /**
     * 通用加载方法 回调RequestOptions
     *
     * @param context
     * @param data
     * @param imageView
     * @param optType   : 0 centerCrop  1 fitCenter 2 default
     * @param rounded
     * @param thum
     * @return
     */
    public RequestOptions GlideWithOnBack(Activity context, Object data, ImageView imageView, int optType, boolean rounded, float thum) {

        //判断Activity是否销毁
        if (context == null || isActivityAlive(context)) {

            RequestManager requestManager = Glide.with(context == null ? KikisUitls.getContext() : context);

            RequestOptions opt = GlideUtils.getInstance().getDefaultOptions();

            switch (optType) {
                case 0:
                    opt.centerCrop();
                    break;
                case 1:
                    opt.fitCenter();
                    break;
            }

            RequestBuilder requestBuilder = null;


            if (rounded) {
                requestBuilder = requestManager.asBitmap().load(data).apply(opt);
                if (thum > 0)
                    requestBuilder.thumbnail(thum);
                requestBuilder.into(GlideUtils.getInstance().getRoundedBmp(context == null ? KikisUitls.getContext() : context, imageView));

            } else {

                requestBuilder = requestManager.load(data).apply(opt);

                if (thum > 0)
                    requestBuilder.thumbnail(thum);

                requestBuilder.into(imageView);
            }

            return opt;
        }
        return null;
    }


}
