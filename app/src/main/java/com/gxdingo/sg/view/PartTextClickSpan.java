package com.gxdingo.sg.view;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * textview部分字点击事件ClickableSpan
 * Created by Kikis on 2020/8/6
 */
public class PartTextClickSpan extends ClickableSpan {

    private int mHighLightColor = Color.RED;

    private boolean mUnderLine = false;

    private View.OnClickListener mClickListener;

    public PartTextClickSpan(View.OnClickListener listener) {

        this.mClickListener = listener;

    }


    public PartTextClickSpan(int color, boolean underline, View.OnClickListener listener) {

        this.mHighLightColor = color;

        this.mUnderLine = underline;

        this.mClickListener = listener;

    }


    @Override

    public void onClick(View widget) {

        if (mClickListener != null)

            mClickListener.onClick(widget);

    }


    @Override

    public void updateDrawState(TextPaint ds) {

        ds.setColor(mHighLightColor);

        ds.setUnderlineText(mUnderLine);

    }
}
