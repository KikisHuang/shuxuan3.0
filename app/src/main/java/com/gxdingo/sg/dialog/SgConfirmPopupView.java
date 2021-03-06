package com.gxdingo.sg.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ScreenUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.MyConfirmListener;
import com.lxj.xpopup.core.CenterPopupView;

/**
 * @author: Kikis
 * @date: 2021/4/21
 * @page: 树选通用自定义确认弹窗
 */
public class SgConfirmPopupView extends CenterPopupView implements View.OnClickListener {

    private MyConfirmListener confirmListener;
    private TextView tv_title, tv_confirm,tv_hint;
    private ImageView close_img;
    private CharSequence title,hint, confirmText;

    /**
     * @param context
     * @param bindLayoutId layoutId 要求布局中必须包含的TextView以及id有：tv_title，tv_content，tv_cancel，tv_confirm
     */
    public SgConfirmPopupView(@NonNull Context context, int bindLayoutId) {
        super(context);
        this.bindLayoutId = bindLayoutId;
        addInnerContent();
    }

    /**
     * @param context
     */
    public SgConfirmPopupView(@NonNull Context context, CharSequence title, CharSequence confirmText, MyConfirmListener confirmListener) {
        super(context);
        this.title = title;
        this.confirmText = confirmText;
        this.confirmListener = confirmListener;
        addInnerContent();
    }

    /**
     * @param context
     */
    public SgConfirmPopupView(@NonNull Context context, CharSequence title, CharSequence hint, CharSequence confirmText, MyConfirmListener confirmListener) {
        super(context);
        this.title = title;
        this.confirmText = confirmText;
        this.hint = hint;
        this.confirmListener = confirmListener;
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return bindLayoutId != 0 ? bindLayoutId : R.layout.module_dialog_base_sg_xpopup_confirm;
    }

    @Override
    protected int getMaxWidth() {
        return (int) (ScreenUtils.getScreenWidth() * 2.6 / 3);
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        tv_title = findViewById(R.id.tv_title);
        close_img = findViewById(R.id.close_img);
        tv_confirm = findViewById(R.id.tv_confirm);
        tv_hint = findViewById(R.id.hint_tv);

        close_img.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);

        if (!TextUtils.isEmpty(confirmText)) {
            tv_confirm.setText(confirmText);
        }
        if (!TextUtils.isEmpty(hint)) {
            tv_hint.setText(hint);
            tv_hint.setVisibility(VISIBLE);
        }
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == close_img) {
            dismiss();
        } else if (v == tv_confirm) {
            if (confirmListener != null) confirmListener.onConfirm();
            if (popupInfo.autoDismiss) dismiss();
        }
    }

}
