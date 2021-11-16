package com.gxdingo.sg.adapter;

import android.content.Context;
import android.view.View;

import com.gxdingo.sg.biz.NineClickListener;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.NineGridViewAdapter;

import java.util.List;


/**
 * Created by Kikis on 2019/10/25.
 * 自定义九宫格照片查看器跳转;
 */

public class MyNineGridViewClickAdapter extends NineGridViewAdapter {

    private NineClickListener nineGridClickListener;
    private int mAdapterPos;

    public MyNineGridViewClickAdapter(Context context, List<ImageInfo> imageInfo, int pos, NineClickListener nineGridClickListener) {
        super(context, imageInfo);
        this.nineGridClickListener = nineGridClickListener;
        this.mAdapterPos = pos;
    }

    @Override
    protected void onImageItemClick(Context context, NineGridView nineGridView, int index, List<ImageInfo> imageInfo) {
        View imageView = null;
        if (index <= nineGridView.getMaxSize()) {
            imageView = nineGridView.getChildAt(index);

        }
        if (imageView != null && nineGridClickListener != null)
            nineGridClickListener.onNineGridViewClick(mAdapterPos,index);
    }
}