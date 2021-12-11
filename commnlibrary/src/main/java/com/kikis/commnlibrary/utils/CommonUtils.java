package com.kikis.commnlibrary.utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.kikis.commnlibrary.R;

import java.io.File;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.AppUtils.getAppName;
import static com.blankj.utilcode.util.ConvertUtils.dp2px;


/**
 * Created by lian on 2018/3/1.
 */
public class CommonUtils {
    private static final String TAG = getTAG(CommonUtils.class);

    private static String[] Sex = {"女", "男"};


    /**
     * 获取String 通用方法;
     *
     * @param res
     * @return
     */
    @NonNull
    public static String gets(int res) {
        return KikisUitls.getContext().getResources().getString(res);
    }

    /**
     * 获取项目默认本地保存地址
     * 没有这个目录则先创建
     *
     * @return
     */
    public static String getPath() {
        String storePath;
        /**
         * 创建路径的时候一定要用[/],不能使用[\],但是创建文件夹加文件的时候可以使用[\].
         * [/]符号是Linux系统路径分隔符,而[\]是windows系统路径分隔符 Android内核是Linux.
         */
 /*       if (isHasSdcard())// 判断是否插入SD卡
            storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "shugou"; // 保存到SD卡路径下
        else*/
            storePath = KikisUitls.getContext().getExternalCacheDir().getAbsolutePath() + File.separator + "shugou"; // 保存到app的包名路径下

        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        return storePath;
    }

    /**
     * @Description 判断是否插入SD卡
     */
    public static boolean isHasSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取Layout通用方法;
     */
    @NonNull
    public static XmlResourceParser getlt(int id) {
        return KikisUitls.getContext().getResources().getLayout(id);
    }

    /**
     * @param url
     * @return 从下载连接中解析出文件名
     */
    @NonNull
    public static String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * 获取Colors通用方法;
     */
    @NonNull
    public static int getc(int id) {

        return KikisUitls.getContext().getResources().getColor(id);
    }

    /**
     * 获取Drawable通用方法;
     */
    @NonNull
    public static Drawable getd(int id) {
        return KikisUitls.getContext().getResources().getDrawable(id);
    }

    /**
     * 生成一个随机数
     *
     * @param minlimit 最小限制
     * @param maxlimit 最大限制
     * @return
     */
    public static int getRandomNum(int minlimit, int maxlimit) {

        int rand = new Random().nextInt(maxlimit);

        if (rand < minlimit && rand + minlimit < maxlimit)
            rand += minlimit;
        else if (rand < minlimit) {
            int x = minlimit - rand;

            int y = maxlimit - x;

            rand += x;

            int randy = new Random().nextInt(y);

            rand += randy;
        }

        return rand;
    }

    /**
     * 判断 用户是否安装QQ客户端
     */
    @NonNull
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                Log.i(TAG, "qq pn = " + pn);
                if (pn.equalsIgnoreCase("com.tencent.qqlite") || pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断 用户是否安装支付宝
     *
     * @param context
     * @return
     */
    public static boolean isAliPayInstalled(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

    /**
     * 判断 用户是否安装微信客户端
     */
    @NonNull
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                Log.i(TAG, "wechat pn = " + pn);
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 获取图标 bitmap
     *
     * @param context
     */
    @NonNull
    public static synchronized Bitmap getBitmap(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext()
                    .getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        Drawable d = packageManager.getApplicationIcon(applicationInfo); //xxx根据自己的情况获取drawable
        BitmapDrawable bd = (BitmapDrawable) d;
        Bitmap bm = bd.getBitmap();
        return bm;
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     * @param mContext
     */
    public static void backgroundAlpha(float bgAlpha, Context mContext) {
        WindowManager.LayoutParams layoutParams = ((Activity) mContext).getWindow().getAttributes();
        layoutParams.alpha = bgAlpha;
        if (layoutParams.alpha == 1) {
            //不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            //此行代码主要是解决在华为手机上半透明效果无效的bug
            ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        ((Activity) mContext).getWindow().setAttributes(layoutParams);
    }

    /**
     * 获取包名.类名 通用方法;
     *
     * @param Class 类
     * @return
     */
    public static String getTAG(Class Class) {
        return Class.getName();
    }

    /**
     * 过千、过万简略显示方法;
     *
     * @param num
     * @return
     */
    public static String KswitchWay(double num) {

        DecimalFormat df1 = new DecimalFormat("######0");

        String a = "";
        if (num > 1000 && num < 10000) {
            DecimalFormat df = new DecimalFormat("######0.0");
            double c = num / 1000;
            a = String.valueOf(df.format(c)) + "k";
        } else if (num >= 10000) {
            DecimalFormat df = new DecimalFormat("#.0000");
            double value = num / 10000;
            String result = df.format(value);
            // 截取第一位
            String index = result.substring(0, 1);

            if (".".equals(index)) {
                result = "0" + result;
            }
            // 获取小数 . 号第一次出现的位置
            int inde = firstIndexOf(result, ".");
            // 字符串截断
            a = result.substring(0, inde + 2) + "w";
        } else
            a = String.valueOf(df1.format(num));

        return a;
    }


    /**
     * 过万简略显示方法;
     * 不四舍五入
     *
     * @param num
     * @return
     */
    public static String WswitchWay(double num) {
        DecimalFormat df = new DecimalFormat("#.0000");

        DecimalFormat df1 = new DecimalFormat("######0");
        String a = "";
        if (num > 10000) {

            double value = num / 10000;
            String result = df.format(value);

            // 截取第一位
            String index = result.substring(0, 1);

            if (".".equals(index)) {
                result = "0" + result;
            }
            // 获取小数 . 号第一次出现的位置
            int inde = firstIndexOf(result, ".");

            // 字符串截断
            return result.substring(0, inde + 2) + "w";
        } else
            a = String.valueOf(num);

        return a;

    }

    public static String oneDecimal(float scale) {
        DecimalFormat fnum = new DecimalFormat("##0.0");
        String dd = fnum.format(scale);
        return dd;
    }

    /**
     * 过万简略显示方法;
     * 不四舍五入
     *
     * @param num
     * @return
     */
    public static String WswitchWayChines(int num) {
        DecimalFormat df = new DecimalFormat("#.0000");

        DecimalFormat df1 = new DecimalFormat("######0");
        String a = "";
        if (num > 10000) {

            double value = num / 10000;
            String result = df.format(value);

            // 截取第一位
            String index = result.substring(0, 1);

            if (".".equals(index)) {
                result = "0" + result;
            }
            // 获取小数 . 号第一次出现的位置
            int inde = firstIndexOf(result, ".");

            // 字符串截断
            return result.substring(0, inde + 2) + "万";
        } else
            a = String.valueOf(num);

        return a;

    }

    /**
     * 过万简略显示方法;
     * 不四舍五入
     *
     * @param num
     * @return
     */
    public static String WswitchWayChines(float num) {
        DecimalFormat df = new DecimalFormat("#.0000");

        DecimalFormat df1 = new DecimalFormat("######0");
        String a = "";
        if (num > 10000) {

            double value = num / 10000;
            String result = df.format(value);

            // 截取第一位
            String index = result.substring(0, 1);

            if (".".equals(index)) {
                result = "0" + result;
            }
            // 获取小数 . 号第一次出现的位置
            int inde = firstIndexOf(result, ".");

            // 字符串截断
            return result.substring(0, inde + 2) + "万";
        } else
            a = String.valueOf(num);

        return a;

    }

    public static SpannableString changTVsize(String value) {
        SpannableString spannableString = new SpannableString(value);
        if (value.contains(".")) {
            spannableString.setSpan(new RelativeSizeSpan(0.4f), value.indexOf("."), value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }


    /**
     * 查找字符串pattern在str中第一次出现的位置
     *
     * @param str
     * @param pattern
     * @return
     */
    public static int firstIndexOf(String str, String pattern) {
        for (int i = 0; i < (str.length() - pattern.length()); i++) {
            int j = 0;
            while (j < pattern.length()) {
                if (str.charAt(i + j) != pattern.charAt(j))
                    break;
                j++;
            }
            if (j == pattern.length())
                return i;
        }
        return -1;
    }

    /**
     * int判断通用方法;
     *
     * @param str
     * @return
     */
    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 字母判断;
     */
    public static boolean isChinesePunctuation(String str) {
        if (str.matches("^[A-Za-z]+$"))
            return true;
        else
            return false;
    }


    public static void finishac(Activity ac) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            ac.finishAfterTransition();
        else {
            ac.finish();
            ac.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    /**
     * 数据交换
     *
     * @param olddata
     * @param newdata
     * @param <T>
     */
    public static <T> void assignmentData(List<T> olddata, List<T> newdata) {
        for (T t : olddata) {
            newdata.add(t);
        }
    }

    /**
     * 将手机号中间四位数隐藏
     *
     * @return
     */
    public static String getUserPhone(String phone) {
        if (phone == null || phone.isEmpty())
            return "";
        StringBuilder sb = new StringBuilder();
        if (phone.length() > 6) {

            for (int i = 0; i < phone.length(); i++) {
                char c = phone.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
        }
        return String.valueOf(sb);
    }


    /**
     * 隐藏虚导航栏，并且全屏
     */
    public static void hideBottomUIMenu(View v) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            v.setSystemUiVisibility(uiOptions);

        }
    }


    /**
     * 判断string是否包含 某一字符
     *
     * @param string  完整字符串
     * @param include 是否包含的字符串
     * @return
     */
    public static boolean isInclude(String string, String include) {
        if (string.indexOf(include) != -1) {
            return true;
        }
        return false;

    }

    /**
     * 判断String 内容是否为int类型
     *
     * @param str
     * @return
     */
    public static boolean canParseInt(String str) {

        if (str == null) { //验证是否为空
            return false;

        }
        return str.matches("\\d+");
    }


    /**
     * 复制到剪贴板
     *
     * @param context
     * @param text
     */
    public static void CopyToClipboard(Context context, String text) {
        ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //clip.getText(); // 粘贴
        clip.setText(text); // 复制
    }

    /**
     * 点击窗体外部判断方法
     *
     * @param context
     * @param event
     * @return
     */
    public static boolean isOutOfBounds(Activity context, MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        final View decorView = context.getWindow().getDecorView();
        return (x < -slop) || (y < -slop) || (x > (decorView.getWidth() + slop)) || (y > (decorView.getHeight() + slop));
    }


    /**
     * 隐藏手机号中间四位
     *
     * @param mobile
     * @return
     */
    public static String HideMobile(String mobile) {
        StringBuilder sb = new StringBuilder();
        if (!isEmpty(mobile) && mobile.length() > 6) {

            for (int i = 0; i < mobile.length(); i++) {
                char c = mobile.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }

        }
        return sb.toString();
    }

    /**
     * 设置Clicklistener通用方法
     *
     * @param clickListener
     * @param views
     */
    public static void setClickListener(View.OnClickListener clickListener, View... views) {
        for (int i = 0; i < views.length; i++) {
            views[i].setOnClickListener(clickListener);
        }
    }


    /**
     * 保存图片到picture 目录，Android Q适配，最简单的做法就是保存到公共目录，不用SAF存储
     *
     * @param context
     * @param bitmap
     * @param fileName
     */
    public static boolean addPictureToAlbum(Context context, Bitmap bitmap, String fileName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, fileName);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        OutputStream outputStream = null;
        try {
            outputStream = context.getContentResolver().openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 调用系统分享
     *
     * @param context
     * @param bitmap
     */
    public static void systemShare(Context context, Bitmap bitmap) {
        if (bitmap == null) {
            LogUtils.e(" ====== bitmap is null ======");
            return;
        }
        //将mipmap中图片转换成Uri
        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, null, null));

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        //其中imgUri为图片的标识符
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        //切记需要使用Intent.createChooser，否则会出现别样的应用选择框，您可以试试
        shareIntent = Intent.createChooser(shareIntent, "分享至");
        context.startActivity(shareIntent);
    }


    /**
     * 判断Activity是否Destroy
     *
     * @param mActivity
     * @return
     */
    public static boolean isDestroy(Activity mActivity) {
        if (mActivity == null || mActivity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 字节转kb、mb、gb
     *
     * @param size
     * @return
     */
    public static String getPrintSize(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
        }
    }

    /**
     * 按下标拆分为集合
     *
     * @param str
     * @param separator
     * @return
     */
    public static ArrayList<String> StringToArrayList(String str, String separator) {
        ArrayList<String> arr = new ArrayList<String>();
        if ((str == null) || (separator == null)) {
            return arr;
        }
        StringTokenizer st = new StringTokenizer(str, separator);
        while (st.hasMoreTokens()) {
            arr.add(st.nextToken());
        }
        return arr;
    }


    /**
     * 按下表拆分为集合
     *
     * @param array
     * @param separator
     * @return
     */
    public static String ArrayListToString(List<String> array, String separator) {
        ArrayList<String> arr = new ArrayList<String>();
        if ((array == null) || (array.size() <= 0)) {
            return "";
        }
        String content = "";

        for (int i = 0; i < array.size(); i++) {
            if (i == 0)
                content = array.get(i);
            else
                content = content + separator + array.get(i);
        }

        return content;
    }

    /**
     * 通用空数据判断方法
     *
     * @param content
     * @return
     */
    public static String getText(String content) {
        if (content == null || isEmpty(content))
            return "";
        else
            return content;

    }

    /**
     * 获取边界view
     *
     * @param height dp
     * @return
     */
    public static View getBorder(int height, Context context) {
        //设置底部间距
        LinearLayout layout = new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(height));
        layout.setLayoutParams(lp);
        return layout;
    }

    /**
     * 获取本地验证码间隔时间
     *
     * @return
     */
    public static int getSmsCodeTime() {

        long value = System.currentTimeMillis() - SPUtils.getInstance().getLong(Constant.SMS_CODE_KEY, 0);

        int interval = (int) (value / 1000);

        if (value < 1000)
            return 60;

        if (interval > 0 && interval <= Constant.SMS_CODE_INTERVAL) {
            int time = 60 - interval;
            return time;
        }
        return 0;
    }

    /**
     * 判断通知权限是否开启
     *
     * @param context
     * @return
     */
    public static boolean isNotificationEnabled(Context context) {
        boolean isOpened = false;
        try {
            isOpened = NotificationManagerCompat.from(context).areNotificationsEnabled();
        } catch (Exception e) {
            e.printStackTrace();
            isOpened = false;
        }
        return isOpened;

    }

    /**
     * 去应用通知设置页面
     *
     * @param context
     */
    public static void goNotifySetting(Context context) {

        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 26) {
            // android 8.0引导
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
        } else if (Build.VERSION.SDK_INT >= 21) {
            // android 5.0-7.0
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", context.getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else {
            // 其他
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    /**
     * 判断是否包含字母、数字
     *
     * @param str
     * @return
     */
    public static boolean isLetterDigitOrChinese(String str) {
        String regex = ".*[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]";
        return str.matches(regex);
    }



}
