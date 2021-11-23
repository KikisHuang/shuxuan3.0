package com.gxdingo.sg.view;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.kikis.commnlibrary.utils.BaseLogUtils;

/**
 * @author: Kikis
 * @date: 2021/4/18
 * @page:
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private int itemCount = 3;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    public SpaceItemDecoration(int space, int itemCount) {
        this.space = space;
        this.itemCount = itemCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        outRect.left = space;
        outRect.bottom = space;

        //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
        if (parent.getChildLayoutPosition(view) % itemCount == 0) {
            BaseLogUtils.i("parent.getChildLayoutPosit  ion(view) " + parent.getChildLayoutPosition(view));
            outRect.left = 0;
        }
    }

}
