package com.gxdingo.sg.dialog;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.AuthenticationBean;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.utils.GlideUtils;
import com.lxj.xpopup.core.CenterPopupView;

/**
 * @author: Kikis
 * @date: 2021/12/31
 * @page:
 */
public class AuthenticationStatusPopupView extends CenterPopupView implements View.OnClickListener {

    private ImageView status_img;

    private ImageView close_img;

    private TextView hint_one_tv;

    private TextView hint_two_tv;

    private Button done_bt;

    private CustomResultListener listener;

    private AuthenticationBean authenticationBean;


    public AuthenticationStatusPopupView(@NonNull Context context, AuthenticationBean authenticationBean, CustomResultListener<Integer> listener) {
        super(context);
        this.listener = listener;
        this.authenticationBean = authenticationBean;

        addInnerContent();
    }

    @Override
    protected void initPopupContent() {
        status_img = findViewById(R.id.status_img);
        close_img = findViewById(R.id.close_img);
        hint_two_tv = findViewById(R.id.hint_two_tv);
        hint_one_tv = findViewById(R.id.hint_one_tv);
        done_bt = findViewById(R.id.done_bt);

        done_bt.setOnClickListener(this);
        close_img.setOnClickListener(this);

        boolean isSucceed = authenticationBean.getAuthenticationStatus() == 1;

        Glide.with(getContext()).load(isSucceed ? R.drawable.ic_id_card_authentication_success : R.drawable.ic_id_card_authentication_failed).apply(GlideUtils.getInstance().getDefaultOptions()).into(status_img);

        String hint1 = "";
        String hint2 = "";

        if (isSucceed) {
            hint1 = "恭喜您，认证成功";
            done_bt.setText("完成");
        } else {
            hint1 = "认证失败";
            done_bt.setText("重新认证");
        }
        hint2 = authenticationBean.getMsg();
        hint_one_tv.setText(hint1);
        hint_two_tv.setText(hint2);

    }


    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_authentication_status;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_img:
                this.dismiss();
                break;
            case R.id.done_bt:
                if (listener != null)
                    listener.onResult( authenticationBean.getAuthenticationStatus());
                this.dismiss();
                break;
        }
    }

}
