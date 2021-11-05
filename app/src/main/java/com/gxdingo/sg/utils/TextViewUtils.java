package com.gxdingo.sg.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import com.gxdingo.sg.R;
import com.kikis.commnlibrary.utils.BitmapUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextViewUtils {

    /**
     * 内容转换，让其能显示表情图片
     *
     * @param content
     * @return
     */
    public static SpannableStringBuilder contentConversion(Context context,String content) {
        //通过正则表达式将消息内容里面的表情标签转换成图片
        CharSequence text = content;
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        String rexgString = "(\\[(.*?)])";//表示[]中括号里面任意内容的都视为表情，含中括号
        Pattern pattern = Pattern.compile(rexgString);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            int emotionResId = EmotionsUtils.getValue(matcher.group());
            if (emotionResId != 0) {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), emotionResId);
                int bitmapWH = (int) context.getResources().getDimension(R.dimen.emoji_size);
                Bitmap newBitmap = BitmapUtils.updateBitmapWidthAndHeight(bitmap, bitmapWH, bitmapWH);
                builder.setSpan(new ImageSpan(context, newBitmap), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return builder;
    }
}
