package com.kikis.commnlibrary.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.opengl.Visibility;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kikis.commnlibrary.R;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.SizeUtils.dp2px;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.CommonUtils.getd;


/**
 * Created by Kikis on 2018/3/22.
 * 标题控件
 */
public class TemplateTitle extends RelativeLayout {

    private String titleText;
    private boolean canBack;
    private String backText;
    private String moreText;
    private int moreImg;
    private int titleBackGroundColor;
    private TextView tvMore;
    private LinearLayout backBtn, btn_more;
    private RelativeLayout title_layout;
    private int backImg;
    private int TextColor;


    @SuppressLint("ResourceAsColor")
    public TemplateTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.module_include_title, this);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TemplateTitle, 0, 0);
        try {
            titleText = ta.getString(R.styleable.TemplateTitle_titleText);
            canBack = ta.getBoolean(R.styleable.TemplateTitle_canBack, false);
            backText = ta.getString(R.styleable.TemplateTitle_backText);
            moreImg = ta.getResourceId(R.styleable.TemplateTitle_moreImg, 0);
            backImg = ta.getResourceId(R.styleable.TemplateTitle_backImg, R.drawable.module_svg_return_white);
            moreText = ta.getString(R.styleable.TemplateTitle_moreText);
            titleBackGroundColor = ta.getColor(R.styleable.TemplateTitle_layoutBackColor, getc(R.color.white));
            TextColor = ta.getColor(R.styleable.TemplateTitle_TitleTextColor, 0);
            setUpView();
        } finally {
            ta.recycle();
        }
    }

    private void setUpView() {

        title_layout = findViewById(R.id.title_layout);
        title_layout.setBackgroundColor(titleBackGroundColor);
        TextView tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText(titleText);
        if (TextColor != 0)
            tvTitle.setTextColor(TextColor);

        backBtn = (LinearLayout) findViewById(R.id.title_back);
        backBtn.setVisibility(canBack || backText != null || !backText.isEmpty() ? VISIBLE : INVISIBLE);

        if (canBack || backText != null || !backText.isEmpty()) {
            TextView tvBack = (TextView) findViewById(R.id.txt_back);
            tvBack.setText(backText);
            if (TextColor != 0)
                tvBack.setTextColor(TextColor);
            backBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) getContext()).finish();
                }
            });

        }
        if (moreImg != 0) {
            ImageView moreImgView = (ImageView) findViewById(R.id.img_more);
            moreImgView.setImageDrawable(getContext().getResources().getDrawable(moreImg));
            moreImgView.setVisibility(VISIBLE);
        }

        ImageView backImgView = (ImageView) findViewById(R.id.img_back);
        backImgView.setImageDrawable(getContext().getResources().getDrawable(backImg));
        backImgView.setVisibility(canBack ? VISIBLE : GONE);


        tvMore = (TextView) findViewById(R.id.txt_more);
        tvMore.setText(moreText);
        if (!isEmpty(moreText))
            tvMore.setVisibility(VISIBLE);
        if (TextColor != 0)
            tvMore.setTextColor(TextColor);

        btn_more = (LinearLayout) findViewById(R.id.btn_more);
    }

    /**
     * 设置返回图标
     */
    public void setBackImg(int res) {
        ImageView backImgView = (ImageView) findViewById(R.id.img_back);
        backImgView.setImageDrawable(getd(res));
    }

    /**
     * 标题控件
     *
     * @param titleText 设置标题文案
     */
    public void setTitleText(String titleText) {
        this.titleText = titleText;
        TextView tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText(titleText);
        if (TextColor != 0)
            tvTitle.setTextColor(TextColor);
    }

    /**
     * 标题控件
     *
     * @param dp 设置标题大小
     */
    public void setTitleTextSize(int dp) {

        TextView tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, dp);
        if (TextColor != 0)
            tvTitle.setTextColor(TextColor);
    }

    /**
     * 设置标题背景色
     *
     * @param color
     */
    public void setTitleBackGroundColor(int color) {
        if (title_layout == null)
            title_layout = findViewById(R.id.title_layout);
        title_layout.setBackgroundColor(color);
    }

    /**
     * 返回text
     *
     * @param str 设置标题文案
     */
    public void setBackText(String str) {
        backText = str;
        TextView tvBack = (TextView) findViewById(R.id.txt_back);
        tvBack.setText(backText);
    }

    /**
     * 返回可见
     *
     * @param visible 设置标题文案
     */
    public void setBackVisible(boolean visible) {
        backBtn.setVisibility(visible ? VISIBLE : GONE);
    }

    /**
     * 更多
     *
     * @param visible 设置标题文案
     */
    public void setMoreVisible(boolean visible) {
        btn_more.setVisibility(visible ? VISIBLE : GONE);
    }

    /**
     * 获取返回textview的控件
     */
    public TextView getBackTextView() {
        TextView tvBack = (TextView) findViewById(R.id.txt_back);
        return tvBack;
    }

    /**
     * 返回text
     *
     * @param color 设置标题文字颜色
     */
    public void setBackTextColor(int color) {
        TextView tvBack = (TextView) findViewById(R.id.txt_back);
        tvBack.setTextColor(color);
    }

    /**
     * 标题更多按钮
     *
     * @param img 设置更多按钮
     */
    public void setMoreImg(int img) {
        moreImg = img;
        ImageView moreImgView = (ImageView) findViewById(R.id.img_more);
        moreImgView.setImageDrawable(getContext().getResources().getDrawable(moreImg));

        moreImgView.setVisibility(VISIBLE);
    }


    /**
     * 设置更多按钮事件
     *
     * @param listener 事件监听
     */
    public void setMoreImgAction(OnClickListener listener) {
        ImageView moreImgView = (ImageView) findViewById(R.id.img_more);
        moreImgView.setOnClickListener(listener);
    }


    /**
     * 设置更多按钮事件
     *
     * @param listener 事件监听
     */
    public void setMoreTextAction(OnClickListener listener) {
        tvMore.setOnClickListener(listener);
    }


    /**
     * 自定返回按钮特殊事件监听
     *
     * @param canbackListener listener回调
     */
    public void setGobackListener(OnClickListener canbackListener) {
        backBtn.setOnClickListener(canbackListener);
    }

    /**
     * 设置更多文字内容
     *
     * @param text 更多文本
     */
    public void setMoreText(String text) {
        tvMore.setVisibility(VISIBLE);
        tvMore.setText(text);
    }

    /**
     * 设置更多文字内容颜色
     *
     * @param color 颜色
     */
    public void setMoreTextColor(int color) {
        tvMore.setTextColor(color);
    }

    /**
     * 获取更多的view控件
     */
    public TextView getMoreTextView() {
        return tvMore;
    }

    /**
     * 设置返回按钮事件
     *
     * @param listener 事件监听
     */
    public void setBackListener(OnClickListener listener) {
        if (canBack) {
            LinearLayout backBtn = (LinearLayout) findViewById(R.id.title_back);
            backBtn.setOnClickListener(listener);
        }
    }

}
