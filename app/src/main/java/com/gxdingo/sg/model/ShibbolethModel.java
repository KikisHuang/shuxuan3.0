package com.gxdingo.sg.model;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.SPUtils;
import com.gxdingo.sg.activity.WebActivity;
import com.gxdingo.sg.biz.OnCodeListener;
import com.gxdingo.sg.biz.OnContentListener;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.utils.BaseLogUtils;
import com.tencent.bugly.crashreport.CrashReport;


import org.greenrobot.eventbus.EventBus;

import static com.blankj.utilcode.util.ClipboardUtils.copyText;
import static com.blankj.utilcode.util.StringUtils.isEmpty;
import static com.gxdingo.sg.utils.SignatureUtils.getAcType;
import static com.gxdingo.sg.utils.SignatureUtils.isShuXiangShibboleth;
import static com.gxdingo.sg.utils.SignatureUtils.numberDecode;

/**
 * @author: Kikis
 * @date: 2021/11/29
 * @page: 口令model
 */
public class ShibbolethModel {


    /**
     * 检测口令
     */
    public static void checkShibboleth(OnCodeListener listener, int delayTime, boolean clearClipboard) {

        try {
            new Handler().postDelayed(() -> {

                String copyContent = ClipboardUtils.getText().toString();

                //判断是否有这个内容
                if (!isEmpty(copyContent)) {
//                String code = copyContent.substring(0, copyContent.indexOf(" "));
                    //是否口令
                    if (isShuXiangShibboleth(copyContent)) {

                        //获取活动类型
                        int mType = getAcType(numberDecode(copyContent));
                        if (mType <= 0) {
                            BaseLogUtils.i("无效口令，清除");
                            copyText("");
                            return;
                        }

                        if (listener != null)
                            listener.onCode(mType, copyContent);


                        //商家类型口令商家处理认证时再处理
                        if (mType == 30) {
                            if (clearClipboard)
                                clear();
                        } else
                            clear();
                    }
                }
            }, delayTime);//1秒后执行Runnable中的run方法
        } catch (Exception e) {
            CrashReport.postCatchedException(e);  // bugly会将这个throwable上报
        }
    }

    private static void clear() {
        //清空剪贴板
        if (UserInfoUtils.getInstance().isLogin()) {
            copyText("");
        }
    }
}
