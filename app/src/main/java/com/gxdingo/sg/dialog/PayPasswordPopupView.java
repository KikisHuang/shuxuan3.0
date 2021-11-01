package com.gxdingo.sg.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.ClientSettingPayPwd1Activity;
import com.gxdingo.sg.activity.ClientUpdatePayPwdActivity;
import com.gxdingo.sg.bean.UserBean;
import com.gxdingo.sg.biz.PayPasswordListener;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.view.PasswordLayout;
import com.lxj.xpopup.core.CenterPopupView;

import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;

/**
 * @author: Weaving
 * @date: 2021/5/10
 * @page:
 */
public class PayPasswordPopupView extends CenterPopupView implements View.OnClickListener {

    private PasswordLayout passwordLayout;

    private ImageView btnClose;

    private TextView btnForget;

    private  PayPasswordListener mPayPasswordListener;



    public PayPasswordPopupView(@NonNull Context context) {
        super(context);
        addInnerContent();
    }

    public PayPasswordPopupView(@NonNull Context context, PayPasswordListener payPasswordListener) {
        super(context);
        this.mPayPasswordListener=payPasswordListener;
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_input_pay_passwrod;
    }

    @Override
    protected void initPopupContent() {
        passwordLayout = findViewById(R.id.password_layout);
        btnClose=findViewById(R.id.iv_btn_close);
        btnForget = findViewById(R.id.btn_forget_password);
        btnClose.setOnClickListener(this);
        btnForget.setOnClickListener(this);
        passwordLayout.setPwdChangeListener(new PasswordLayout.pwdChangeListener() {
            @Override
            public void onChange(String pwd) {

            }

            @Override
            public void onNull() {

            }

            @Override
            public void onFinished(String pwd) {
                if (mPayPasswordListener!=null)
                    mPayPasswordListener.finished(PayPasswordPopupView.this,passwordLayout,pwd);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_btn_close:
                dismiss();
                break;
            case R.id.btn_forget_password:
                UserBean userInfo = UserInfoUtils.getInstance().getUserInfo();
                if (userInfo.getIsSetPassword())
                    goToPage(getContext(), ClientUpdatePayPwdActivity.class,null);
                else
                    goToPage(getContext(), ClientSettingPayPwd1Activity.class,null);
                break;
        }
    }
}
