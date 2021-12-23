package com.gxdingo.sg.activity;

import android.os.Bundle;

import com.alibaba.sdk.android.push.AndroidPopupActivity;
import com.blankj.utilcode.util.LogUtils;
import com.gxdingo.sg.bean.PushBean;
import com.kikis.commnlibrary.utils.BaseLogUtils;
import com.kikis.commnlibrary.utils.GsonUtil;

import java.util.Map;

import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

public class AuxiliaryPushActivity extends AndroidPopupActivity {

    static final String TAG = AuxiliaryPushActivity.class.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSysNoticeOpened(String title, String summary, Map<String, String> extraMap) {
        LogUtils.d("AuxiliaryPushActivity, title: " + title + ", content: " + summary + ", extMap: " + extraMap);

        try {
            //未测试，现在辅助弹窗直接跳转SplashActivity 如需修改，找服务端设置
            String json = GsonUtil.gsonToStr(extraMap);

            PushBean pushBean = GsonUtil.GsonToBean(json, PushBean.class);

            if (pushBean.getType() == 0)
                goToPagePutSerializable(this, ChatActivity.class, getIntentEntityMap(new Object[]{pushBean.getShareUuid(), pushBean.getRole()}));

        } catch (Exception e) {
            BaseLogUtils.e("onSysNoticeOpened error == " + e);
        }
    }
}
