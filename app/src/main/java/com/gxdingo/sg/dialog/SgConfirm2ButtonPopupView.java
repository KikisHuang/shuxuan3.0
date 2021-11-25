package com.gxdingo.sg.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.MyConfirmListener;
import com.lxj.xpopup.core.CenterPopupView;

import static android.text.TextUtils.isEmpty;

/**
 * @author: Weaving
 * @date: 2021/5/11
 * @page:
 */
public class SgConfirm2ButtonPopupView extends CenterPopupView implements View.OnClickListener {

    private MyConfirmListener confirmListener;

    private TextView tvTitle,tvContent,btnCancel,btnConfirm;

    private CharSequence mTitle,mContent;

    public SgConfirm2ButtonPopupView(@NonNull Context context, MyConfirmListener confirmListener) {
        super(context);
        this.confirmListener = confirmListener;
        addInnerContent();
    }

    public SgConfirm2ButtonPopupView(@NonNull Context context, String title, MyConfirmListener confirmListener) {
        super(context);
        this.mTitle = title;
        this.confirmListener = confirmListener;
        addInnerContent();
    }

    public SgConfirm2ButtonPopupView(@NonNull Context context, String title, String content, MyConfirmListener confirmListener) {
        super(context);
        this.mTitle = title;
        this.mContent = content;
        this.confirmListener = confirmListener;
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return  R.layout.module_dialog_base_sg_xpopup_2button_confirm;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        tvTitle=findViewById(R.id.tv_title);
        tvContent=findViewById(R.id.tv_content);
        btnCancel=findViewById(R.id.btn_cancel);
        btnConfirm=findViewById(R.id.btn_confirm);

        if (!isEmpty(mTitle))
            tvTitle.setText(mTitle);

        if (!isEmpty(mContent)){
            tvContent.setVisibility(VISIBLE);
            tvContent.setText(mContent);
        }

        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_confirm:
                if (confirmListener!=null){
                    confirmListener.onConfirm();
                    dismiss();
                }
                break;
        }
    }
}
