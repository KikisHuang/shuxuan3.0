package com.gxdingo.sg.utils.emotion;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.gxdingo.sg.R;

import java.util.ArrayList;

import static com.blankj.utilcode.util.ConvertUtils.dp2px;

/**
 * @author: Kikis
 * @date: 2020/12/29
 * @page: 自定义表情底部指示器
 */
public class EmojiIndicatorView extends LinearLayout {

    private Context mContext;
    private ArrayList<View> mImageViews;//所有指示器集合
    private int size = 6;
    private int marginSize = 15;
    private int pointSize;//指示器的大小
    private int marginLeft;//间距

    public EmojiIndicatorView(Context context) {
        this(context, null);
    }

    public EmojiIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmojiIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        pointSize = dp2px(size);
        marginLeft = dp2px(marginSize);
    }

    /**
     * 初始化指示器
     *
     * @param count 指示器的数量
     */
    public void initIndicator(int count) {
        mImageViews = new ArrayList<>();
        this.removeAllViews();
        LayoutParams lp;
        for (int i = 0; i < count; i++) {
            View v = new View(mContext);
            lp = new LayoutParams(pointSize, pointSize);
            if (i != 0)
                lp.leftMargin = marginLeft;
            v.setLayoutParams(lp);
            if (i == 0) {
                v.setBackgroundResource(R.drawable.module_shape_bg_indicator_point_select);
            } else {
                v.setBackgroundResource(R.drawable.module_shape_bg_indicator_point_nomal);
            }
            mImageViews.add(v);
            this.addView(v);
        }
    }

    /**
     * 页面移动时切换指示器
     */
    public void playByStartPointToNext(int startPosition, int nextPosition) {
        if (startPosition < 0 || nextPosition < 0 || nextPosition == startPosition) {
            startPosition = nextPosition = 0;
        }
        final View ViewStrat = mImageViews.get(startPosition);
        final View ViewNext = mImageViews.get(nextPosition);
        ViewNext.setBackgroundResource(R.drawable.module_shape_bg_indicator_point_select);
        ViewStrat.setBackgroundResource(R.drawable.module_shape_bg_indicator_point_nomal);
    }
}
