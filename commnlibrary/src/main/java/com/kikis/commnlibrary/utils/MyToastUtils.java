package com.kikis.commnlibrary.utils;

import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.kikis.commnlibrary.R;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.ToastUtils.make;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;

public class MyToastUtils {

    /**
     * 全局style 风格toast
     *
     * @param msg
     */
    public static void customToast(String msg) {
        if (msg == null || msg.isEmpty() || msg.length() <= 0)
            return;
        Toast.makeText(KikisUitls.getContext(), msg, Toast.LENGTH_SHORT).show();

    //make().setNotUseSystemToast().setBgColor(getc(R.color.white)).setTextColor(getc(R.color.gray202)).setTextSize(13).show(msg);
    }

    /**
     * 全局style 风格toast
     *
     * @param msg
     */
    public static void customLongToast(String msg) {
        make().setBgColor(getc(R.color.white)).setTextColor(getc(R.color.gray202)).setTextSize(13).setDurationIsLong(true).show(msg);
    }

}
