
package com.kikis.commnlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.widget.ScrollView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static android.os.Environment.DIRECTORY_DCIM;
import static android.os.Environment.getExternalStoragePublicDirectory;

/**
 * 位图工具类
 *
 * @author pjw
 */
public class BitmapUtils {

    public static byte[] bitmapTurnByteArray(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        return baos.toByteArray();
    }

    public static long getBitmapLength(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        return baos.toByteArray().length;
    }

    /**
     * 质量压缩
     */
    public static Bitmap compressBitmap(Bitmap image, int maxkb) {
        //L.showlog(压缩图片);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > maxkb && options > 0) { // 循环判断如果压缩后图片是否大于(maxkb)50kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 压缩位图并保存
     *
     * @param image
     * @param maxkb        压缩后的最大大小
     * @param mStoragePath 保存路径
     * @return
     */
    public static String compressBitmapAndSave(Bitmap image, int maxkb, String mStoragePath) throws IOException {
        if (image == null) {
            return null;
        }
        //L.showlog(压缩图片);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > maxkb && options > 0) { // 循环判断如果压缩后图片是否大于(maxkb)50kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        File tempFile = new File(mStoragePath);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        File file = new File(tempFile, getCompressBitmapName());
        FileOutputStream fos = new FileOutputStream(file);
        int len;
        byte[] bytes = new byte[1024];
        while ((len = isBm.read(bytes)) != -1) {
            fos.write(bytes, 0, len);
        }
        isBm.close();
        fos.close();
        return file.getAbsolutePath();
    }


    /**
     * http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
     * 官网：获取压缩后的图片
     *
     * @param res
     * @param resId
     * @param reqWidth  所需图片压缩尺寸最小宽度
     * @param reqHeight 所需图片压缩尺寸最小高度
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    /**
     * 官网：获取压缩后的图片
     *
     * @param filepath  图片路径
     * @param reqWidth  所需图片压缩尺寸最小宽度
     * @param reqHeight 所需图片压缩尺寸最小高度
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFile(String filepath,
                                                     int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filepath, options);
    }

    public static Bitmap decodeSampledBitmapFromFile(String filepath, int inSampleSize) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, options);

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;
        return BitmapFactory.decodeFile(filepath, options);
    }

    /**
     * 官网：获取压缩后的图片
     *
     * @param bitmap    位图
     * @param reqWidth  所需图片压缩尺寸最小宽度
     * @param reqHeight 所需图片压缩尺寸最小高度
     * @return
     */
    public static Bitmap decodeSampledBitmapFromBitmap(Bitmap bitmap,
                                                       int reqWidth, int reqHeight) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] data = baos.toByteArray();

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    /**
     * 计算压缩比例值(改进版 by touch_ping)
     * <p>
     * 原版2>4>8...倍压缩
     * 当前2>3>4...倍压缩
     *
     * @param options   解析图片的配置信息
     * @param reqWidth  所需图片压缩尺寸最小宽度O
     * @param reqHeight 所需图片压缩尺寸最小高度
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int picheight = options.outHeight;
        final int picwidth = options.outWidth;

        int targetheight = picheight;
        int targetwidth = picwidth;
        int inSampleSize = 1;

        if (targetheight > reqHeight || targetwidth > reqWidth) {
            while (targetheight >= reqHeight
                    && targetwidth >= reqWidth) {
                inSampleSize += 1;
                targetheight = picheight / inSampleSize;
                targetwidth = picwidth / inSampleSize;
            }
        }

        return inSampleSize;
    }

    /**
     * 保存压缩图片
     */
    public static String saveCompressBitmap(Bitmap mBitmap, String mStoragePath) {
        File file = FileUtils.makeFolder(mStoragePath);
        File newFile = new File(file.getAbsolutePath(), getCompressBitmapName());
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(newFile);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFile.getPath();
    }


    /**
     * 将滚动视图转成位图
     */
    public static Bitmap getBitmapByScrollView(ScrollView scrollView) {
        if (scrollView == null) {
            return null;
        }
        int h = 0;
        Bitmap bitmap = null;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
        }
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        scrollView.draw(canvas);
        return bitmap;
    }

    /**
     * 将Activity转成位图
     */
    public static Bitmap getBitmapByActivity(Activity activity) {
        if (activity == null) {
            return null;
        }
        // 获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();
        view.buildDrawingCache();
        // 获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeights = rect.top;
        Display display = activity.getWindowManager().getDefaultDisplay();
        // 获取屏幕宽和高
        int widths = display.getWidth();
        int heights = display.getHeight();
        // 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);
        // 去掉状态栏
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
                statusBarHeights, widths, heights - statusBarHeights);
        // 销毁缓存信息
        view.destroyDrawingCache();
        return bmp;
    }

    /**
     * 将View转成位图
     */
    public static Bitmap getBitmapByView(View view) {
        if (view == null) {
            return null;
        }
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();  //启用DrawingCache并创建位图
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache()); //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        view.setDrawingCacheEnabled(false);  //禁用DrawingCahce否则会影响性能
        return bitmap;
    }


    /**
     * 使用系统当前日期加以调整作为压缩图片的名称
     */
    private synchronized static String getCompressBitmapName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'COMPRESS_IMG'_yyyyMMddHHmmssSS");
        Random random = new Random();
        String name = dateFormat.format(date) + "_" + random.nextInt(1000) + random.nextInt(1000) + ".jpg";
        return name;
    }

    /**
     * 保存图片到多媒体库
     */
    public static String savePictureMultimediaLibrary(Context context, String imagePath) {
        String uri = null;
        try {
            //向媒体库添加缩略图，图片可能保存在Pictures目录下
            uri = MediaStore.Images.Media.insertImage(context.getContentResolver(), imagePath, "", "");
            //扫描SD卡让缩略图能马上看得到
            MediaScannerConnection.scanFile(context, new String[]{imagePath}, null, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return uri;
    }

    /**
     * 保存图片到多媒体库
     */
    public static void savePictureMultimediaLibrary(Context context, Bitmap bitmap) {
        //向媒体库添加缩略图，让微信里能看得到（微信选择照片是获取缩略图的）
        MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "", "");
        //下载图片
        String imgQrPath = saveImagePath(context, bitmap);
        //扫描SD卡让缩略图能马上看得到
        MediaScannerConnection.scanFile(context, new String[]{imgQrPath}, null, null);
    }

    /**
     * 保存位图到相册并返回路径
     */
    private static String saveImagePath(Context context, Bitmap bm) {
        String dateFolder = new SimpleDateFormat("yyyyMMdd", Locale.CHINA)
                .format(new Date());
        try {
            String filePath = getExternalStoragePublicDirectory(DIRECTORY_DCIM) + "/Camera/";
            return FileUtils.saveFile(context, filePath, "hi_" + dateFolder + System.currentTimeMillis() + ".jpg", bm);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 修改Bitmap宽高
     */
    public static Bitmap updateBitmapWidthAndHeight(Bitmap bitmap, int newWidth, int newHeight) {
        // 获得图片的宽高.
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算缩放比例.
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片.
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newBitmap;
    }
}
