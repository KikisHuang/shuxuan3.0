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


import static com.blankj.utilcode.util.ClipboardUtils.copyText;
import static com.blankj.utilcode.util.StringUtils.isEmpty;
import static com.gxdingo.sg.utils.SignatureUtils.getAcType;
import static com.gxdingo.sg.utils.SignatureUtils.isShuXiangShibboleth;
import static com.gxdingo.sg.utils.SignatureUtils.numberDecode;
import static com.kikis.commnlibrary.utils.Constant.isDebug;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * @author: Kikis
 * @date: 2021/11/29
 * @page: 口令model
 */
public class ShibbolethModel {


    /**
     * 检测口令
     */
    public static void checkShibboleth(OnCodeListener listener) {

        new Handler().postDelayed(() -> {

            String copyContent = ClipboardUtils.getText().toString();

            //判断是否有这个内容
            if (!isEmpty(copyContent)) {

//                String code = copyContent.substring(0, copyContent.indexOf(" "));

                //是否口令
                if (isShuXiangShibboleth(copyContent)) {

                    //获取活动类型
                    int mType = getAcType(numberDecode(copyContent));

                    if (mType != 30) {
                        if (listener != null)
                            listener.onCode(mType, copyContent);
                    }


                    String url = "";


        /*            String tempUrl = "";

                    if (!isDebug)
                        tempUrl = Api.H5_URL;
                    else if (isDebug && !isUat)
                        tempUrl = Api.TEST_H5_URL;
                    else if (isDebug && isUat)
                        tempUrl = Api.URL.replace("/user/", "");

                    switch (mType) {
                        //全民领钱
                        case 50:
                            //邀请新用户注册
                        case 10:
                            url = tempUrl + AC1URL + INVITATIONCODE + QT + code;
                            break;

                        //一分拿
                        case 51:
                            url = tempUrl + AC2URL + INVITATIONCODE + QT + code;
                            break;
                    }*/
//                    goToPagePutSerializable((Activity) context, WebActivity.class, getIntentEntityMap(new Object[]{false,url}));

                    //清空剪贴板
                    copyText("");
                }
            }
        }, 1000);//1秒后执行Runnable中的run方法

    }

}
