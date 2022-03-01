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
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.view.PartTextClickSpan;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.utils.KikisUitls;
import com.lxj.xpopup.core.CenterPopupView;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.model.HttpHeaders;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gxdingo.sg.http.Api.CLIENT_PRIVACY_AGREEMENT_KEY;
import static com.gxdingo.sg.http.Api.CLIENT_SERVICE_AGREEMENT_KEY;
import static com.gxdingo.sg.utils.LocalConstant.FIRST_LOGIN_KEY;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * @author: Weaving
 * @date: 2021/8/6
 * @page:
 */
public class ProtocolPopupView extends CenterPopupView implements View.OnClickListener {

    private TextView protocol_tv;

    private TextView cancel_tv;

    private TextView consent_tv;

    private CustomResultListener listener;

    public ProtocolPopupView(@NonNull Context context, CustomResultListener listener) {
        super(context);
        addInnerContent();
        this.listener = listener;
    }

    @Override
    protected void initPopupContent() {
        protocol_tv = findViewById(R.id.protocol_tv);
        cancel_tv = findViewById(R.id.cancel_tv);
        consent_tv = findViewById(R.id.consent_tv);
        cancel_tv.setOnClickListener(this);
        consent_tv.setOnClickListener(this);
        setTextHighLightWithClick(protocol_tv.getText().toString(), new String[]{"《服务协议》", "《隐私政策》"});
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_protocol;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_tv:
                listener.onResult(0);
                this.dismiss();
                break;
            case R.id.consent_tv:
                SPUtils.getInstance().put(FIRST_LOGIN_KEY, false);
                listener.onResult(1);
                this.dismiss();
                break;
        }
    }


    /**
     * 设置文字高亮及点击事件
     *
     * @param text
     * @param keyWord
     */
    private void setTextHighLightWithClick(String text, String[] keyWord) {

        protocol_tv.setClickable(true);

        protocol_tv.setHighlightColor(Color.TRANSPARENT);

        protocol_tv.setMovementMethod(LinkMovementMethod.getInstance());


        SpannableString s = new SpannableString(text);

        for (int i = 0; i < keyWord.length; i++) {
            Pattern p = Pattern.compile(keyWord[i]);
            Matcher m = p.matcher(s);

            while (m.find()) {

                int start = m.start();

                int end = m.end();

                int finalI = i;

                s.setSpan(new PartTextClickSpan(getc(R.color.deepskyblue), false, new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (finalI == 0)
                            goToPagePutSerializable(getContext(), WebActivity.class, getIntentEntityMap(new Object[]{true, 0, CLIENT_SERVICE_AGREEMENT_KEY}));
                        else
                            goToPagePutSerializable(getContext(), WebActivity.class, getIntentEntityMap(new Object[]{true, 0, (CLIENT_PRIVACY_AGREEMENT_KEY)}));
                    }
                }), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        protocol_tv.setText(s);

    }
}