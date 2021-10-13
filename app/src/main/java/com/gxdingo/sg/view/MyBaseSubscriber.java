package com.gxdingo.sg.view;

import android.content.Context;

import com.gxdingo.sg.model.WebSocketModel;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.bean.ReLoginBean;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.subsciber.BaseSubscriber;

import org.greenrobot.eventbus.EventBus;


/**
 * 作者： zhouyou<br>
 * 日期： 2016/12/19 16:46<br>
 * 修改者：kikis<br>
 * 日期： 2019/12/20
 * 修改内容：自定义全局请求code事件处理
 * 版本： v2.0<br>
 */

public class MyBaseSubscriber<T> extends BaseSubscriber<T> {


    /**
     * 默认不显示弹出框，不可以取消
     *
     * @param context 上下文
     */
    public MyBaseSubscriber(Context context) {
        super(context);
    }


    public void onDisposed() {
        if (!isDisposed()) {
            dispose();
        }
    }

    @Override
    public void onError(ApiException e) {
        int errCode = e.getCode();

        switch (errCode) {
            case 401:
            case 2:
                UserInfoUtils.getInstance().clearLoginStatus();
                WebSocketModel.getInstance(contextWeakReference.get()).setUnReadMessageNum(0);
                EventBus.getDefault().post(new ReLoginBean(0, ""));
                UserInfoUtils.getInstance().goToLoginPage(contextWeakReference.get(), "");
                break;

        }
    }

}
