package com.gxdingo.sg.utils;

import android.app.Activity;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import static android.text.TextUtils.isEmpty;
import static com.luck.picture.lib.config.PictureMimeType.ofImage;

/**
 * @author: Kikis
 * @date: 2020/10/14
 * @page:
 */
public class PhotoUtils {


    /**
     * 相册单选通用无裁剪
     */
    public static void Photo(Activity activity, OnResultCallbackListener listener) {
        PictureSelector.create(activity)
                .openGallery(ofImage())
                .selectionMode(PictureConfig.SINGLE)
                .loadImageEngine(GlideEngine.createGlideEngine())// 图片加载引擎 需要 implements ImageEngine接口
                .enableCrop(false)//是否裁剪
                .compress(true)//是否压缩
                .minimumCompressSize(1024)//小于1024kb不压缩
                .synOrAsy(true)//开启同步or异步压缩
                .forResult(listener);
    }

    /**
     * 相册单选通用可开启裁剪
     */
    public static void Photo(Activity activity, OnResultCallbackListener listener, boolean enableCrop) {
        PictureSelector.create(activity)
                .openGallery(ofImage())
                .selectionMode(PictureConfig.SINGLE)
                .loadImageEngine(GlideEngine.createGlideEngine())// 图片加载引擎 需要 implements ImageEngine接口
                .enableCrop(enableCrop)//是否裁剪
                .withAspectRatio(1, 1)
                .rotateEnabled(false)//裁剪是否可旋转图片
                .scaleEnabled(true)//裁剪是否可放大缩小图片
                .compress(true)//是否压缩
                .minimumCompressSize(1024)//小于1024kb不压缩
                .synOrAsy(true)//开启同步or异步压缩
                .forResult(listener);
    }

    /**
     * 拍照单选通用无裁剪
     */
    public static void TakePhoto(Activity activity, OnResultCallbackListener listener) {
        PictureSelector.create(activity)
                .openCamera(ofImage())
                .selectionMode(PictureConfig.SINGLE)
                .loadImageEngine(GlideEngine.createGlideEngine())// 图片加载引擎 需要 implements ImageEngine接口
                .enableCrop(false)//是否裁剪
                .compress(true)//是否压缩
                .minimumCompressSize(1024)//小于1024kb不压缩
                .synOrAsy(true)//开启同步or异步压缩
                .forResult(listener);
    }

    /**
     * 相册多选通用无裁剪
     */
    public static void PhotoMultiple(Activity activity, int maxnum, OnResultCallbackListener listener) {
        PictureSelector.create(activity)
                .openGallery(ofImage())
                .selectionMode(PictureConfig.MULTIPLE)
                .loadImageEngine(GlideEngine.createGlideEngine())// 图片加载引擎 需要 implements ImageEngine接口
                .enableCrop(false)//是否裁剪
                .compress(true)//是否压缩
                .isCamera(true)//是否允许拍照
                .maxSelectNum(maxnum)
                .minimumCompressSize(300)//小于1024kb不压缩
                .synOrAsy(true)//开启同步or异步压缩
                .forResult(listener);
    }

    /**
     * 相册多选通用有裁剪
     * 比例x
     * 比例y
     */
    public static void PhotoMultipleCut(Activity activity, int maxnum, OnResultCallbackListener listener, int x, int y) {
        PictureSelector.create(activity)
                .openGallery(ofImage())
                .selectionMode(PictureConfig.MULTIPLE)
                .loadImageEngine(GlideEngine.createGlideEngine())// 图片加载引擎 需要 implements ImageEngine接口
                .enableCrop(true)//是否裁剪
                .withAspectRatio(x, y)
                .isMultipleSkipCrop(true)//多图裁剪是否支持跳过
                .compress(true)//是否压缩
                .isCamera(false)
                .showCropGrid(false)//是否显示裁剪矩形网格 圆形裁剪时建议设为fals
                .rotateEnabled(false)//裁剪是否可旋转图片)
                .maxSelectNum(maxnum)
                .minimumCompressSize(1024)//小于1024kb不压缩
                .synOrAsy(true)//开启同步or异步压缩
                .forResult(listener);
    }

    /**
     * 获取图片路径，优先获取压缩路径
     *
     * @param localMedia
     * @return
     */
    public static String getPhotoUrl(LocalMedia localMedia) {

        String url = "";
        //优先压缩路径CompressPath
        if (!isEmpty(localMedia.getCompressPath()))
            url = localMedia.getCompressPath();
        else if (!isEmpty(localMedia.getPath()))
            url = localMedia.getPath();
        else if (!isEmpty(localMedia.getRealPath()))
            url = localMedia.getRealPath();
        else {
            return url;
        }
        return url;
    }


}
