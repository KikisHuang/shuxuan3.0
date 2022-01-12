package com.gxdingo.sg.dialog;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ScreenUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.MyConfirmListener;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.lxj.xpopup.core.CenterPopupView;

import static android.text.TextUtils.isEmpty;

/**
 * @author: Kikis
 * @date: 2022/1/12
 * @page:
 */
public class ScanningHintPopupView extends CenterPopupView implements View.OnClickListener {

    private CustomResultListener confirmListener;

    private OnClickListener cancelCilcikListener;

    private TextView tvTitle, btnCancel, btnConfirm;

    private CheckBox not_remind_ckb;

    private CharSequence mTitle;

    private boolean check;

    public ScanningHintPopupView(@NonNull Context context, String text, CustomResultListener confirmListener) {
        super(context);
        this.confirmListener = confirmListener;
        mTitle = text;
        addInnerContent();
    }


    @Override
    protected int getMaxWidth() {
        return ScreenUtils.getScreenWidth() * 9 / 10;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_base_scanning_hint;
    }

    public void setCancelCilcikListener(OnClickListener cancelCilcikListener) {
        this.cancelCilcikListener = cancelCilcikListener;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        tvTitle = findViewById(R.id.tv_title);
        btnCancel = findViewById(R.id.btn_cancel);
        btnConfirm = findViewById(R.id.btn_confirm);
        not_remind_ckb = findViewById(R.id.not_remind_ckb);

        not_remind_ckb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            check = isChecked;
        });
        if (!isEmpty(mTitle))
            tvTitle.setText(mTitle);


        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                if (cancelCilcikListener != null)
                    cancelCilcikListener.onClick(v);

                dismiss();
                break;
            case R.id.btn_confirm:
                if (confirmListener != null) {
                    confirmListener.onResult(check);
                    dismiss();
                }
                break;
        }
    }
}
