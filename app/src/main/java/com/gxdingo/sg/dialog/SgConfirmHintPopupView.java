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
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.lxj.xpopup.core.CenterPopupView;

import static com.kikis.commnlibrary.utils.CommonUtils.gets;

/**
 * @author: Kikis
 * @date: 2022/3/1
 * @page: 树选通用自定义确认弹窗
 */
public class SgConfirmHintPopupView extends CenterPopupView implements View.OnClickListener {

    private TextView tv_title, tv_confirm, tv_hint;
    private ImageView close_img;
    private CharSequence title, hint, confirmText;
    private CustomResultListener customResultListener;

    /**
     * @param context
     */
    public SgConfirmHintPopupView(@NonNull Context context, CharSequence title, CharSequence hint, CharSequence confirmText, CustomResultListener customResultListener) {
        super(context);
        this.title = title;
        this.confirmText = confirmText;
        this.hint = hint;
        this.customResultListener = customResultListener;
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_base_sg_xpopup_hint_confirm;
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
        if (v.getId() == R.id.close_img) {
            dismiss();
        } else if (v.getId() == R.id.tv_confirm) {
            if (popupInfo.autoDismiss) {
                if (customResultListener != null)
                    customResultListener.onResult("");
                dismiss();
            }
        }
    }

}
