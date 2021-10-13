package com.kikis.commnlibrary.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import com.blankj.utilcode.util.LogUtils;
import com.kikis.commnlibrary.fragment.dialog.EditDialogFragment;

import io.reactivex.annotations.Nullable;

import static android.text.TextUtils.isEmpty;
import static com.kikis.commnlibrary.utils.MyToastUtils.customToast;

public class DialogUtils {


    /**
     * 用户拒绝权限弹窗，并且选择不再提示,可以引导用户进入权限设置界面开启权限
     *
     * @param context
     */
    public static void rejectPermissionsDialog(@Nullable final Activity context) {
        if (context != null) {
            new AlertDialog.Builder(context)
                    .setCancelable(false)
                    .setTitle("权限要求")
                    .setMessage("如果没有请求的权限，此应用程序可能无法正常工作，打开app设置界面修改app权限。")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                            intent.setData(uri);
                            context.startActivity(intent);
                            context.finish();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create()
                    .show();
        }
    }


    /**
     * edittext fragment dialog通用方法
     *
     * @param tag 0 key
     *            1 title
     *            2 content
     *            3 index
     *            4 input type
     * @return
     */
    public static EditDialogFragment showEdDialog(String... tag) {

        Bundle bundle = new Bundle();

        if (tag.length >= 1 && !isEmpty(tag[0]))
            bundle.putString(EditDialogFragment.EDIT_TAG, tag[0]);

        if (tag.length >= 2 && !isEmpty(tag[1]))
            bundle.putString(EditDialogFragment.EDIT_TITLE, tag[1]);


        if (tag.length >= 3 && isEmpty(tag[2]))
            bundle.putString(EditDialogFragment.EDIT_HINT, "请输入");
        else
            bundle.putString(EditDialogFragment.EDIT_CONTENT, tag[2]);

        try {
            if (tag.length >= 4 && !isEmpty(tag[3]))
                bundle.putInt(EditDialogFragment.EDIT_INDEX, Integer.parseInt(tag[3]));

            if (tag.length >= 5 && !isEmpty(tag[4]))
                bundle.putInt(EditDialogFragment.EDIT_INPUT_TYPE, Integer.parseInt(tag[4]));
        } catch (Exception e) {
            customToast(e.getMessage());
            LogUtils.e(e);
        }


        EditDialogFragment fragment = EditDialogFragment.newInstance(EditDialogFragment.class, bundle);

        return fragment;
    }
}
