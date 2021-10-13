package com.kikis.commnlibrary.view;

import android.content.Context;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kikis.commnlibrary.R;
import com.kikis.commnlibrary.biz.BaseOnConfirmListener;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.interfaces.OnCancelListener;

/**
 * @author: Kikis
 * @date: 2021/1/28
 * @page: 按需求自定义的 ConfirmPopupView
 */
public class BaseInputConfirmPopupView extends CenterPopupView implements View.OnClickListener {


    OnCancelListener cancelListener;
    BaseOnConfirmListener confirmListener;
    TextView tv_title, tv_content, tv_cancel, tv_confirm;
    CharSequence title, content,inputContent, hint, cancelText, confirmText;
    EditText et_input;
    View divider1, divider2;
    public boolean isHideCancel = false;

    /**
     * @param context
     * @param bindLayoutId layoutId 要求布局中必须包含的TextView以及id有：tv_title，tv_content，tv_cancel，tv_confirm
     */
    public BaseInputConfirmPopupView(@NonNull Context context, int bindLayoutId) {
        super(context);
        this.bindLayoutId = bindLayoutId;
        addInnerContent();
    }

    /**
     * @param context
     */
    public BaseInputConfirmPopupView(@NonNull Context context, CharSequence title, CharSequence edcontent, CharSequence hint, BaseOnConfirmListener confirmListener) {
        super(context);
        this.confirmListener = confirmListener;
        this.title = title;
        this.hint = hint;
        this.inputContent = edcontent;
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return bindLayoutId != 0 ? bindLayoutId : R.layout.module_dialog_base_xpopup_confirm;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        tv_title = findViewById(R.id.tv_title);
        tv_content = findViewById(R.id.tv_content);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_confirm = findViewById(R.id.tv_confirm);
        tv_content.setMovementMethod(LinkMovementMethod.getInstance());
        et_input = findViewById(R.id.et_input);
        divider1 = findViewById(R.id.xpopup_divider1);
        divider2 = findViewById(R.id.xpopup_divider2);

        tv_cancel.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);

        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }

        if (!TextUtils.isEmpty(content)) {
            tv_content.setText(content);
        } else {
            tv_content.setVisibility(GONE);
        }

        if (!TextUtils.isEmpty(cancelText)) {
            tv_cancel.setText(cancelText);
        }

        if (!TextUtils.isEmpty(confirmText)) {
            tv_confirm.setText(confirmText);
        }

        if (isHideCancel) {
            tv_cancel.setVisibility(GONE);
            if (divider2 != null) divider2.setVisibility(GONE);
        }

        if (!TextUtils.isEmpty(hint)) {
            et_input.setHint(hint);
        }
        if (!TextUtils.isEmpty(inputContent)) {
            et_input.setText(inputContent);
            et_input.setSelection(inputContent.length());
        }
    }


    public TextView getTitleTextView() {
        return findViewById(R.id.tv_title);
    }

    public TextView getContentTextView() {
        return findViewById(R.id.tv_content);
    }

    public TextView getCancelTextView() {
        return findViewById(R.id.tv_cancel);
    }

    public TextView getConfirmTextView() {
        return findViewById(R.id.tv_confirm);
    }

    public EditText getEditText() {
        return et_input;
    }

    public BaseInputConfirmPopupView setListener(BaseOnConfirmListener confirmListener, OnCancelListener cancelListener) {
        this.cancelListener = cancelListener;
        this.confirmListener = confirmListener;
        return this;
    }

    public BaseInputConfirmPopupView setTitleContent(CharSequence title, CharSequence content, CharSequence hint) {
        this.title = title;
        this.content = content;
        this.hint = hint;
        return this;
    }

    public BaseInputConfirmPopupView setCancelText(CharSequence cancelText) {
        this.cancelText = cancelText;
        return this;
    }

    public BaseInputConfirmPopupView setConfirmText(CharSequence confirmText) {
        this.confirmText = confirmText;
        return this;
    }

    @Override
    public void onClick(View v) {
        if (v == tv_cancel) {
            if (cancelListener != null) cancelListener.onCancel();
            dismiss();
        } else if (v == tv_confirm) {
            if (confirmListener != null) confirmListener.onConfirm(et_input.getText().toString());
            if (popupInfo.autoDismiss) dismiss();
        }
    }

}
