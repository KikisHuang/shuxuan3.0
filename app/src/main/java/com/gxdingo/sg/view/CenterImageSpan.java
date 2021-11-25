package com.gxdingo.sg.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * reated by Kikis on 2020/3/18.
 * 居中 ImageSpan
 */
public class CenterImageSpan extends ImageSpan {

    public CenterImageSpan(Drawable drawable) {
        super(drawable);

    }

    public CenterImageSpan(Context context, Bitmap bitmap) {
        super(context, bitmap);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom,
                     @NonNull Paint paint) {

        Drawable b = getCachedDrawable();
        canvas.save();


        int transY = 0;
        if (mVerticalAlignment == ALIGN_BASELINE) {
            transY -= paint.getFontMetricsInt().descent;
        } else if (mVerticalAlignment == ALIGN_BOTTOM) {
            transY = bottom - b.getBounds().bottom;
        } else {
            Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();

            // y 是基准线  Ascent是基准线 之上字符最高处的距离   descent 是基准线之下字符最低处的距离
            transY = (y + fontMetricsInt.descent + y + fontMetricsInt.ascent) / 2 - b.getBounds().bottom / 2;

        }

        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();

      /*  Drawable b = getDrawable();
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        int transY = (y + fm.descent + y + fm.ascent) / 2 - b.getBounds().bottom / 2;//计算y方向的位移
        canvas.save();
        canvas.translate(x, transY);//绘制图片位移一段距离
        b.draw(canvas);
        canvas.restore();*/
    }

    private Drawable getCachedDrawable() {
        WeakReference<Drawable> wr = mDrawableRef;
        Drawable d = null;

        if (wr != null)
            d = wr.get();

        if (d == null) {
            d = getDrawable();
            mDrawableRef = new WeakReference<Drawable>(d);
        }

        return d;
    }

    private WeakReference<Drawable> mDrawableRef;
}
