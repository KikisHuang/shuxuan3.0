/**
 *
 */
package com.gxdingo.sg.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.gxdingo.sg.utils.emotion.EmotionUtils;
import com.gxdingo.sg.view.CenterImageSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.kikis.commnlibrary.utils.Constant.EMOJISIZE;


/**
 * Created by Kikis on 2019/10/28.
 * 文本中的emojb字符处理为表情图片
 */
public class SpanStringUtils {


    /**
     * 表情的 spannableString 不可变
     * @param emotion_map_type
     * @param context
     * @param source
     * @return
     */
    public static SpannableString getEmotionContent(int emotion_map_type, Context context, String source) {

        //缓存防止加载过多表情造成卡顿的问题。
        CacheDiskUtils cache = CacheDiskUtils.getInstance();
        SpannableString spannableString = new SpannableString(source);
        Resources res = context.getResources();

        String regexEmotion = "\\[([\u4e00-\u9fa5\\w])+\\]";

        Pattern patternEmotion = Pattern.compile(regexEmotion);

        Matcher matcherEmotion = patternEmotion.matcher(spannableString);

        while (matcherEmotion.find()) {

            // 获取匹配到的具体字符
            String key = matcherEmotion.group();

            // 匹配字符串的开始位置
            int start = matcherEmotion.start();
            // 利用表情名字获取到对应的图片
            Integer imgRes = EmotionUtils.getImgByName(emotion_map_type, key);

            if (imgRes == -1)
                return spannableString;

            int size = EMOJISIZE;
            //缓存key取drawable的名字和宽高
            String cacheKey = res.getResourceEntryName(imgRes) + "?w=" + size + "&h=" + size;

            Bitmap cacheBitmap = cache.getBitmap(cacheKey);
            //如果缓存中不存在，则创建bitmap后缓存,否则从缓存中读取.
            if (cacheBitmap == null) {
                if (imgRes != null && imgRes != -1) {
                    // 压缩表情图片
                    Bitmap bitmap = BitmapFactory.decodeResource(res, imgRes);
                    if (bitmap != null) {
                        Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
//                        cache.put(cacheKey, scaleBitmap);
                        cache.put(cacheKey, scaleBitmap);
                        getImgSpan(spannableString, context, scaleBitmap, start, key);
                    }
                }
            } else
                getImgSpan(spannableString, context, cacheBitmap, start, key);
        }

        return spannableString;
    }

    /**
     * 表情的 SpannableStringBuilder 可变。
     * @param spannableString
     * @param emotion_map_type
     * @param context
     * @param source
     * @return
     */
    public static SpannableStringBuilder getEmotionContent(SpannableStringBuilder spannableString, int emotion_map_type, Context context, String source) {

        //缓存防止加载过多表情造成卡顿的问题。
        CacheDiskUtils cache = CacheDiskUtils.getInstance();
        Resources res = context.getResources();

        String regexEmotion = "\\[([\u4e00-\u9fa5\\w])+\\]";
        Pattern patternEmotion = Pattern.compile(regexEmotion);
        Matcher matcherEmotion = patternEmotion.matcher(spannableString);

        while (matcherEmotion.find()) {
            // 获取匹配到的具体字符
            String key = matcherEmotion.group();

            // 匹配字符串的开始位置
            int start = matcherEmotion.start();
            // 利用表情名字获取到对应的图片
            Integer imgRes = EmotionUtils.getImgByName(emotion_map_type, key);
            int size = EMOJISIZE;

            //缓存key取drawable的名字和宽高
            String cacheKey = res.getResourceEntryName(imgRes) + "?w=" + size + "&h=" + size;

            Bitmap cacheBitmap = cache.getBitmap(cacheKey);

            //如果缓存中不存在，则创建bitmap后缓存,否则从缓存中读取.
            if (cacheBitmap == null) {
                if (imgRes != null && imgRes != -1) {
                    // 压缩表情图片
                    Bitmap bitmap = BitmapFactory.decodeResource(res, imgRes);
                    if (bitmap != null) {
                        Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
//                        cache.put(cacheKey, scaleBitmap);
                        cache.put(cacheKey, scaleBitmap);
                        getImgSpan(spannableString, context, scaleBitmap, start, key);
                    }
                }
            } else
                getImgSpan(spannableString, context, cacheBitmap, start, key);
        }

        return spannableString;
    }

    public static boolean hasEmoji(String content) {

        Pattern pattern = Pattern.compile("\\[([\u4e00-\u9fa5\\w])+\\]");
        Matcher matcher = pattern.matcher(content);
        return matcher.find();
    }

    private static void getImgSpan(SpannableStringBuilder spannableString, Context context, Bitmap scaleBitmap, int start, String key) {
        CenterImageSpan span = new CenterImageSpan(context, scaleBitmap);
        spannableString.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private static void getImgSpan(SpannableString spannableString, Context context, Bitmap scaleBitmap, int start, String key) {
        CenterImageSpan span = new CenterImageSpan(context, scaleBitmap);
        spannableString.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

}
