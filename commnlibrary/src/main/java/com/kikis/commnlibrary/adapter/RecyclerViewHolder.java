package com.kikis.commnlibrary.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by lian on 2018/3/1.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    /**
     * 集合类，layout里包含的View,以view的id作为key，value是view对象
     */
    private SparseArray<View> mViews;
    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 构造方法
     *
     * @param ctx
     * @param itemView
     */
    public RecyclerViewHolder(Context ctx, View itemView) {
        super(itemView);
        this.mContext = ctx;
        mViews = new SparseArray<View>();
    }

    /**
     * 存放xml页面方法
     *
     * @param viewId
     * @param <T>
     * @return
     */
    private <T extends View> T findViewById(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getView(int viewId) {
        return findViewById(viewId);
    }

    /**
     * 存放文本的id
     *
     * @param viewId
     * @return
     */
    public TextView getTextView(int viewId) {
        return (TextView) getView(viewId);
    }

    /**
     * 存放button的id
     *
     * @param viewId
     * @return
     */
    public Button getButton(int viewId) {
        return (Button) getView(viewId);
    }

    /**
     * 存放图片的id
     *
     * @param viewId
     * @return
     */
    public ImageView getImageView(int viewId) {
        return (ImageView) getView(viewId);
    }

    public LinearLayout getLinearLayout(int viewId) {
        return (LinearLayout) getView(viewId);
    }

    public ProgressBar getProgressBar(int viewId) {
        return (ProgressBar) getView(viewId);
    }

    /**
     * 存放图片按钮的id
     *
     * @param viewId
     * @return
     */
    public ImageButton getImageButton(int viewId) {
        return (ImageButton) getView(viewId);
    }

    /**
     * 存放输入框的id
     *
     * @param viewId
     * @return
     */
    public EditText getEditText(int viewId) {
        return (EditText) getView(viewId);
    }

    /**
     * 存放文本xml中的id并且可以赋值数据的方法
     *
     * @param viewId
     * @param value
     * @return
     */
    public RecyclerViewHolder setText(int viewId, String value) {
        TextView view = findViewById(viewId);
        if (view != null)
            view.setText(value);
        return null;
    }

    /**
     * 存放图片xml中的id并且可以赋值数据的方法
     *
     * @param viewId
     * @param resId
     * @return
     */
    public RecyclerViewHolder setBackground(int viewId, int resId) {
        View view = findViewById(viewId);
        if (view != null)
            view.setBackgroundColor(resId);
        return null;
    }

    /**
     * 存放点击事件监听
     *
     * @param viewId
     * @param listener
     * @return
     */
    public RecyclerViewHolder setClickListener(int viewId, View.OnClickListener listener) {
        View view = findViewById(viewId);
        if (view != null)
            view.setOnClickListener(listener);
        return null;
    }
}
