package com.kikis.commnlibrary.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.kikis.commnlibrary.R;

/**
 * 实现圆角image
 * 可以改变四个角任意一个角的圆度
 * @author JM
 *
 */
@SuppressLint("AppCompatCustomView")
public class RoundAngleImageView extends ImageView {

    private Paint paint;

    private int leftTop;//左上角圆度
    private int rightTop;//右上角圆度
    private int leftBottom;//左下角圆度
    private int rightBottom;//右下角圆度
    private Paint paint2;

    public RoundAngleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public RoundAngleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundAngleImageView(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundAngleImageView1);
            leftTop = a.getDimensionPixelSize(R.styleable.RoundAngleImageView1_leftTop, leftTop);
            rightTop = a.getDimensionPixelSize(R.styleable.RoundAngleImageView1_rightTop, rightTop);
            leftBottom = a.getDimensionPixelSize(R.styleable.RoundAngleImageView1_leftBottom, leftBottom);
            rightBottom = a.getDimensionPixelSize(R.styleable.RoundAngleImageView1_rightBottom, rightBottom);
        } else {
            float density = context.getResources().getDisplayMetrics().density;
            leftTop = (int) (leftTop * density);
            rightTop = (int) (rightTop * density);
            leftBottom = (int) (leftBottom * density);
            rightBottom = (int) (rightBottom * density);
        }

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        paint2 = new Paint();
        paint2.setXfermode(null);
    }

    @Override
    public void draw(Canvas canvas) {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bitmap);
        super.draw(canvas2);
        drawLeftTop(canvas2);
        drawLeftBottom(canvas2);
        drawRightTop(canvas2);
        drawRightBottom(canvas2);

        canvas.drawBitmap(bitmap, 0, 0, paint2);
        bitmap.recycle();
    }

    private void drawLeftTop(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, leftTop);
        path.lineTo(0, 0);
        path.lineTo(leftTop, 0);
        path.arcTo(new RectF(0, 0, leftTop * 2, leftTop * 2), -90, -90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawLeftBottom(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, getHeight() - leftBottom);
        path.lineTo(0, getHeight());
        path.lineTo(leftBottom, getHeight());
        path.arcTo(new RectF(0, getHeight() - leftBottom * 2, leftBottom * 2, getHeight()), 90, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawRightBottom(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getWidth() - rightBottom, getHeight());
        path.lineTo(getWidth(), getHeight());
        path.lineTo(getWidth(), getHeight() - rightBottom);
        path.arcTo(new RectF(getWidth() - rightBottom * 2, getHeight() - rightBottom * 2, getWidth(), getHeight()), -0, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawRightTop(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getWidth(), rightTop);
        path.lineTo(getWidth(), 0);
        path.lineTo(getWidth() - rightTop, 0);
        path.arcTo(new RectF(getWidth() - rightTop * 2, 0, getWidth(), 0 + rightTop * 2), -90, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    //设置左上角圆度
    public void setLeftTop(int leftTop){
        this.leftTop=leftTop;
        invalidate();
    }

    //设置左下角圆度
    public void  setLeftBottom(int leftBottom){
        this.leftBottom=leftBottom;
        invalidate();
    }

    //设置右上角圆度
    public void  setRightTop(int rightTop){
        this.rightTop=rightTop;
        invalidate();
    }

    //设置右下角圆度
    public void  setRightBottom(int rightBottom){
        this.rightBottom=rightBottom;
        invalidate();
    }

}