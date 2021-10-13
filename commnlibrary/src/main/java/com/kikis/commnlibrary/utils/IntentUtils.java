package com.kikis.commnlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kikis.commnlibrary.R;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import cc.shinichi.library.ImagePreview;
import cc.shinichi.library.bean.ImageInfo;
import cc.shinichi.library.view.listener.OnBigImageClickListener;
import cc.shinichi.library.view.listener.OnBigImageLongClickListener;
import cc.shinichi.library.view.listener.OnBigImagePageChangeListener;
import cc.shinichi.library.view.listener.OnOriginProgressListener;

import static com.kikis.commnlibrary.utils.CommonUtils.getPath;
import static com.kikis.commnlibrary.utils.CommonUtils.getTAG;
import static com.kikis.commnlibrary.utils.JsonUtils.getParamsMap;

/**
 * Created by lian on 2017/5/26.
 */
public class IntentUtils {

    private static final String TAG = getTAG(IntentUtils.class);


    /**
     * 跳转页面;
     *
     * @param context   上下文;
     * @param acClass   跳转ac;
     * @param intentMap 参数map;
     */
    public static <T> void goToPage(Context context, Class<? super T> acClass, Map<String, String> intentMap) {
        Intent intent = new Intent(context, acClass);
        if (intentMap != null)
            AddExtra(intent, intentMap);

        startPage(context, intent);

    }


    /**
     * 照片查看器页面;
     *
     * @param context  上下文
     * @param pos      从第几页显示
     * @param download 是否开启下载按钮
     */
    public static ImagePreview getImagePreviewInstance(Activity context, ImagePreview.LoadStrategy strategy, int pos, boolean download) {

        return ImagePreview.getInstance().
                // 上下文，必须是activity，不需要担心内存泄漏，本框架已经处理好
                        setContext(context)
                // 从第几张图片开始，索引从0开始哦~
                .setIndex(pos)
                //=================================================================================================
                // 有三种设置数据集合的方式，根据自己的需求进行三选一：
                // 1：第一步生成的imageInfo List
//                .setImageInfoList(list)
                // 2：直接传url List
                //.setImageList(List<String> imageList)
                // 3：只有一张图片的情况，可以直接传入这张图片的url
                //.setImage(String image)
                //=================================================================================================
                // 加载策略，默认为手动模式
                .setLoadStrategy(strategy)
                // 保存的文件夹名称，会在Picture目录进行文件夹的新建。比如："BigImageView"，会在Picture目录新建BigImageView文件夹)
                .setFolderName(getPath() + File.separator + "BigImageView")
                // 缩放动画时长，单位ms
                .setZoomTransitionDuration(300)
                // 是否显示加载失败的Toast
                .setShowErrorToast(true)
                // 是否启用点击图片关闭。默认启用
                .setEnableClickClose(true)
                // 是否启用下拉关闭。默认不启用
                .setEnableDragClose(true)
                // 是否启用上拉关闭。默认不启用
                .setEnableUpDragClose(true)
                // 是否显示关闭页面按钮，在页面左下角。默认不显示
                .setShowCloseButton(false)
                // 设置关闭按钮图片资源，可不填，默认为库中自带：R.drawable.ic_action_close
                .setCloseIconResId(R.drawable.ic_action_close)
                // 是否显示下载按钮，在页面右下角。默认显示
                .setShowDownButton(download)
                // 设置下载按钮图片资源，可不填，默认为库中自带：R.drawable.icon_download_new
                .setDownIconResId(R.drawable.icon_download_new)
                // 设置是否显示顶部的指示器（1/9）默认显示
                .setShowIndicator(true)
                // 设置顶部指示器背景shape，默认自带灰色圆角shape
                .setIndicatorShapeResId(R.drawable.shape_indicator_bg)
                // 设置失败时的占位图，默认为库中自带R.drawable.load_failed，设置为 0 时不显示
                .setErrorPlaceHolder(R.drawable.load_failed)
                // 点击回调
                .setBigImageClickListener(new OnBigImageClickListener() {
                    @Override
                    public void onClick(Activity activity, View view, int position) {
                        // ...
                        Log.d(TAG, "onClick: ");
                    }
                })
                // 长按回调
                .setBigImageLongClickListener(new OnBigImageLongClickListener() {
                    @Override
                    public boolean onLongClick(Activity activity, View view, int position) {
                        // ...
                        Log.d(TAG, "onLongClick: ");
                        return false;
                    }
                })
                // 页面切换回调
                .setBigImagePageChangeListener(new OnBigImagePageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        Log.d(TAG, "onPageScrolled: ");
                    }

                    @Override
                    public void onPageSelected(int position) {
                        Log.d(TAG, "onPageSelected: ");
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        Log.d(TAG, "onPageScrollStateChanged: ");
                    }
                })

                //=================================================================================================
                // 设置查看原图时的百分比样式：库中带有一个样式：ImagePreview.PROGRESS_THEME_CIRCLE_TEXT，使用如下：
                .setProgressLayoutId(ImagePreview.PROGRESS_THEME_CIRCLE_TEXT, new OnOriginProgressListener() {

                    @Override
                    public void progress(View parentView, int progress) {
                        Log.d(TAG, "progress: " + progress);

                        // 需要找到进度控件并设置百分比，回调中的parentView即传入的布局的根View，可通过parentView找到控件：
                        ProgressBar progressBar = parentView.findViewById(R.id.sh_progress_view);
                        TextView textView = parentView.findViewById(R.id.sh_progress_text);
                        progressBar.setProgress(progress);
                        String progressText = progress + "%";
                        textView.setText(progressText);
                    }

                    @Override
                    public void finish(View parentView) {
                        Log.d(TAG, "finish: ");
                    }
                });

        // 使用自定义百分比样式，传入自己的布局，并设置回调，再根据parentView找到进度控件进行百分比的设置：
        //.setProgressLayoutId(R.layout.image_progress_layout_theme_1, new OnOriginProgressListener() {
        //    @Override public void progress(View parentView, int progress) {
        //        Log.d(TAG, "progress: " + progress);
        //
        //        ProgressBar progressBar = parentView.findViewById(R.id.progress_horizontal);
        //        progressBar.setProgress(progress);
        //    }
        //
        //    @Override public void finish(View parentView) {
        //        Log.d(TAG, "finish: ");
        //    }
        //})
        //=================================================================================================

        // 开启预览
        /*.start()*/


/*
        Intent intent = new Intent(context, PhotoVieweActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PHOTOLIST, (Serializable) list);
        bundle.putInt(Constant.PHOTOPOS, pos);
        bundle.putInt(Constant.PHOTOFUNCTION, Function);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);*/


    }

    /**
     * 照片查看器页面;
     *
     * @param context  上下文
     * @param list     照片数据
     * @param pos      从第几页显示
     * @param download 是否开启下载按钮
     */
    public static void goPhotoViewerPage(Activity context, ImagePreview.LoadStrategy strategy, List<ImageInfo> list, int pos, boolean download) {

        ImagePreview.getInstance().
                // 上下文，必须是activity，不需要担心内存泄漏，本框架已经处理好
                        setContext(context)
                // 从第几张图片开始，索引从0开始哦~
                .setIndex(pos)
                //=================================================================================================
                // 有三种设置数据集合的方式，根据自己的需求进行三选一：
                // 1：第一步生成的imageInfo List
                .setImageInfoList(list)
                // 2：直接传url List
                //.setImageList(List<String> imageList)
                // 3：只有一张图片的情况，可以直接传入这张图片的url
                //.setImage(String image)
                //=================================================================================================
                // 加载策略，默认为手动模式
                .setLoadStrategy(strategy)
                // 保存的文件夹名称，会在Picture目录进行文件夹的新建。比如："BigImageView"，会在Picture目录新建BigImageView文件夹)
                .setFolderName(getPath() + File.separator + "BigImageView")
                // 缩放动画时长，单位ms
                .setZoomTransitionDuration(300)
                // 是否显示加载失败的Toast
                .setShowErrorToast(true)
                // 是否启用点击图片关闭。默认启用
                .setEnableClickClose(true)
                // 是否启用下拉关闭。默认不启用
                .setEnableDragClose(true)
                // 是否启用上拉关闭。默认不启用
                .setEnableUpDragClose(true)
                // 是否显示关闭页面按钮，在页面左下角。默认不显示
                .setShowCloseButton(false)
                // 设置关闭按钮图片资源，可不填，默认为库中自带：R.drawable.ic_action_close
                .setCloseIconResId(R.drawable.ic_action_close)
                // 是否显示下载按钮，在页面右下角。默认显示
                .setShowDownButton(download)
                // 设置下载按钮图片资源，可不填，默认为库中自带：R.drawable.icon_download_new
                .setDownIconResId(R.drawable.icon_download_new)
                // 设置是否显示顶部的指示器（1/9）默认显示
                .setShowIndicator(false)
                // 设置顶部指示器背景shape，默认自带灰色圆角shape
                .setIndicatorShapeResId(R.drawable.shape_indicator_bg)
                // 设置失败时的占位图，默认为库中自带R.drawable.load_failed，设置为 0 时不显示
                .setErrorPlaceHolder(R.drawable.load_failed)

                // 点击回调
                .setBigImageClickListener(new OnBigImageClickListener() {
                    @Override
                    public void onClick(Activity activity, View view, int position) {
                        // ...
                        Log.d(TAG, "onClick: ");
                    }
                })
                // 长按回调
                .setBigImageLongClickListener(new OnBigImageLongClickListener() {
                    @Override
                    public boolean onLongClick(Activity activity, View view, int position) {
                        // ...
                        Log.d(TAG, "onLongClick: ");
                        return false;
                    }
                })
                // 页面切换回调
                .setBigImagePageChangeListener(new OnBigImagePageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        Log.d(TAG, "onPageScrolled: ");
                    }

                    @Override
                    public void onPageSelected(int position) {
                        Log.d(TAG, "onPageSelected: ");
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        Log.d(TAG, "onPageScrollStateChanged: ");
                    }
                })

                //=================================================================================================
                // 设置查看原图时的百分比样式：库中带有一个样式：ImagePreview.PROGRESS_THEME_CIRCLE_TEXT，使用如下：
                .setProgressLayoutId(ImagePreview.PROGRESS_THEME_CIRCLE_TEXT, new OnOriginProgressListener() {

                    @Override
                    public void progress(View parentView, int progress) {
                        Log.d(TAG, "progress: " + progress);

                        // 需要找到进度控件并设置百分比，回调中的parentView即传入的布局的根View，可通过parentView找到控件：
                        ProgressBar progressBar = parentView.findViewById(R.id.sh_progress_view);
                        TextView textView = parentView.findViewById(R.id.sh_progress_text);
                        progressBar.setProgress(progress);
                        String progressText = progress + "%";
                        textView.setText(progressText);
                    }

                    @Override
                    public void finish(View parentView) {
                        Log.d(TAG, "finish: ");
                    }
                })

                // 使用自定义百分比样式，传入自己的布局，并设置回调，再根据parentView找到进度控件进行百分比的设置：
                //.setProgressLayoutId(R.layout.image_progress_layout_theme_1, new OnOriginProgressListener() {
                //    @Override public void progress(View parentView, int progress) {
                //        Log.d(TAG, "progress: " + progress);
                //
                //        ProgressBar progressBar = parentView.findViewById(R.id.progress_horizontal);
                //        progressBar.setProgress(progress);
                //    }
                //
                //    @Override public void finish(View parentView) {
                //        Log.d(TAG, "finish: ");
                //    }
                //})
                //=================================================================================================

                // 开启预览
                .start();


/*
        Intent intent = new Intent(context, PhotoVieweActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PHOTOLIST, (Serializable) list);
        bundle.putInt(Constant.PHOTOPOS, pos);
        bundle.putInt(Constant.PHOTOFUNCTION, Function);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);*/


    }


    /**
     * 跳转页面传递实体类;
     *
     * @param context   上下文;
     * @param acClass   跳转ac;
     * @param intentMap 实体类map;
     */
    public static <T> void goToPagePutSerializable(Context context, Class<? super T> acClass, Map<String, Object> intentMap) {
        Intent intent = new Intent(context, acClass);
        if (intentMap != null)
            intent.putExtras(AddSerExtra(intentMap));
        startPage(context, intent);
    }

    /**
     * 跳转页面传递实体类;
     *
     * @param context   上下文;
     * @param acClass   跳转ac;
     * @param intentMap 实体类map;
     */
    public static <T> void goToPagePutParcelable(Context context, Class<? super T> acClass, Map<String, Object> intentMap) {
        Intent intent = new Intent(context, acClass);
        if (intentMap != null)
            intent.putExtras(AddParcelableExtra(intentMap));
        startPage(context, intent);
    }


    /**
     * 单控件共享转场动画
     *
     * @param context
     * @param view
     * @param str
     * @param clas
     */
    public static void ShareAnimaStartPages(Context context, View view, String str, Class clas, Map<String, Object> intentMap) {
        ActivityOptionsCompat compat =
                ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                        view, str);
        Intent intent = new Intent(context, clas);
        if (intentMap != null)
            intent.putExtras(AddSerExtra(intentMap));
        ActivityCompat.startActivity(context, intent, compat.toBundle());
    }

    /**
     * 多控件共享转场动画
     *
     * @param context
     * @param pairs
     * @param clas
     */
    public static void ShareMultiAnimaStartPages(Context context, Class clas, Map<String, Object> intentMap, Pair... pairs) {
        ActivityOptionsCompat compat =
                ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, pairs);
        Intent intent = new Intent(context, clas);
        if (intentMap != null)
            intent.putExtras(AddSerExtra(intentMap));
        ActivityCompat.startActivity(context, intent, compat.toBundle());
    }


    /**
     * 单控件共享转场动画
     *
     * @param context
     * @param view
     * @param str
     * @param clas
     */
    public static void ShareAnimaStartPage(Context context, View view, String str, Class clas, Map<String, String> intentMap) {
        ActivityOptionsCompat compat =
                ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                        view, str);
        Intent intent = new Intent(context, clas);
        if (intentMap != null)
            AddExtra(intent, intentMap);
        ActivityCompat.startActivity(context, intent, compat.toBundle());
    }

    /**
     * 遍历map添加传递的参数;
     *
     * @param intent
     * @param intentMap
     */
    private static void AddExtra(Intent intent, Map<String, String> intentMap) {
        for (String key : intentMap.keySet()) {
            intent.putExtra(key, intentMap.get(key));
        }
    }

    /**
     * 遍历map添加传递的参数;
     *
     * @param intentMap
     */
    private static Bundle AddSerExtra(Map<String, Object> intentMap) {
        Bundle mBundle = new Bundle();
        for (String key : intentMap.keySet()) {
            mBundle.putSerializable(key, (Serializable) intentMap.get(key));
        }
        return mBundle;
    }

    /**
     * 遍历map添加传递的参数;
     *
     * @param intentMap
     */
    private static Bundle AddParcelableExtra(Map<String, Object> intentMap) {
        Bundle mBundle = new Bundle();
        for (String key : intentMap.keySet()) {
            mBundle.putParcelable(key, (Parcelable) intentMap.get(key));
        }
        return mBundle;
    }

    /**
     * 跳转通用方法1;
     *
     * @param context
     * @param intent
     */
    private static void startPage(Context context, Intent intent) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
            context.startActivity(intent);
        else
            context.startActivity(intent);
    }


    /**
     * 获Intent 跳转 map方法;
     *
     * @return
     */
    public static Map<String, String> getIntentMap(String[] strings) {
        Map<String, String> map = getParamsMap();
        for (int i = 0; i < strings.length; i++) {
            map.put(Constant.PARAMAS + i, strings[i]);
        }
        return map;
    }

    /**
     * 获Intent 跳转传递实体类 map方法;
     *
     * @return
     */
    public static Map<String, Object> getIntentEntityMap(Object[] strings) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < strings.length; i++) {
            map.put(Constant.SERIALIZABLE + i, strings[i]);
        }
        return map;
    }
}
