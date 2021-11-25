package com.gxdingo.sg.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.OnContentListener;
import com.gxdingo.sg.view.RegexEditText;
import com.lxj.xpopup.core.CenterPopupView;

/**
 * @author: Weaving
 * @date: 2021/7/22
 * @page:
 */
public class EditMobilePopupView extends CenterPopupView implements View.OnClickListener {

    private RegexEditText et_mobile;
    private Button btn_cancel;
    private Button btn_confirm;

    private OnContentListener onContentListener;

    public EditMobilePopupView(@NonNull Context context, OnContentListener onContentListener) {
        super(context);
        this.onContentListener = onContentListener;
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_store_edit_shop_mobile;
    }

    @Override
    protected void initPopupContent() {
        et_mobile=findViewById(R.id.et_mobile);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_cancel.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_confirm:
                if (onContentListener!=null)
                    onContentListener.onConfirm(this,et_mobile.getText().toString());
                break;
        }
    }
}
