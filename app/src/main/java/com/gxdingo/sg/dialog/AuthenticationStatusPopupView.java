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

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.gxdingo.sg.MyApplication;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.WebActivity;
import com.gxdingo.sg.view.PartTextClickSpan;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.lxj.xpopup.core.CenterPopupView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.blankj.utilcode.util.AppUtils.getAppName;
import static com.gxdingo.sg.http.ClientApi.CLIENT_PRIVACY_AGREEMENT_KEY;
import static com.gxdingo.sg.http.ClientApi.CLIENT_SERVICE_AGREEMENT_KEY;
import static com.gxdingo.sg.utils.LocalConstant.FIRST_LOGIN_KEY;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

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

    public AuthenticationStatusPopupView(@NonNull Context context, CustomResultListener<Integer> listener) {
        super(context);
        this.listener = listener;
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
                if (listener != null) {

                }
                this.dismiss();
                break;
        }
    }

}
