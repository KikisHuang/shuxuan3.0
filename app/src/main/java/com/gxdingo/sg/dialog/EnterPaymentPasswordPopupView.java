package com.gxdingo.sg.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ScreenUtils;
import com.gxdingo.sg.R;
import com.kikis.commnlibrary.utils.SystemUtils;
import com.lxj.xpopup.core.CenterPopupView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 输入支付密码
 *
 * @author: JM
 * @date: 2021/10/24
 */
public class EnterPaymentPasswordPopupView extends CenterPopupView implements View.OnClickListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.close_img)
    ImageView closeImg;
    @BindView(R.id.tv_pass_1)
    TextView tvPass1;
    @BindView(R.id.tv_pass_2)
    TextView tvPass2;
    @BindView(R.id.tv_pass_3)
    TextView tvPass3;
    @BindView(R.id.tv_pass_4)
    TextView tvPass4;
    @BindView(R.id.tv_pass_5)
    TextView tvPass5;
    @BindView(R.id.tv_pass_6)
    TextView tvPass6;
    @BindView(R.id.et_password)
    EditText etPassword;

    TextView[] tvPassArray;


    private OnCallbackPasswordListener mOnCallbackPasswordListener;

    @OnClick({R.id.tv_pass_1, R.id.tv_pass_2, R.id.tv_pass_3, R.id.tv_pass_4, R.id.tv_pass_5, R.id.tv_pass_6})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_pass_1:
            case R.id.tv_pass_2:
            case R.id.tv_pass_3:
            case R.id.tv_pass_4:
            case R.id.tv_pass_5:
            case R.id.tv_pass_6:
                etPassword.requestFocus();
                SystemUtils.showKeyboard(getContext());
                break;
        }
    }


    public interface OnCallbackPasswordListener {
        void getPassword(String pass);
    }

    public EnterPaymentPasswordPopupView(@NonNull Context context, OnCallbackPasswordListener onCallbackPasswordListener) {
        super(context);
        mOnCallbackPasswordListener = onCallbackPasswordListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_enter_payment_password;
    }

    @Override
    protected int getMaxWidth() {
        return (int) (ScreenUtils.getScreenWidth() * 2.8 / 3);
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        ButterKnife.bind(this);

        etPassword.setFocusable(true);
        etPassword.setFocusableInTouchMode(true);
        etPassword.requestFocus();
        etPassword.findFocus();

//        SystemUtils.showKeyboard(getContext());
        tvPassArray = new TextView[]{tvPass1, tvPass2, tvPass3, tvPass4, tvPass5, tvPass6};
        closeImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 6) {
                    for (int i = 0; i < tvPassArray.length; i++) {
                        if (i < s.length())
                            tvPassArray[i].setText(String.valueOf(s.charAt(i)));
                        else
                            tvPassArray[i].setText("");
                    }
                } else if (s.length() == 6){
                    mOnCallbackPasswordListener.getPassword(etPassword.getText().toString());
                    dismiss();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {

    }

}
