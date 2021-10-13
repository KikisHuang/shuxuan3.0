package com.kikis.commnlibrary.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kikis.commnlibrary.R;

import static com.blankj.utilcode.util.ObjectUtils.isEmpty;
import static com.kikis.commnlibrary.utils.CommonUtils.getTAG;

/**
 * Created by lian on 2020/8/24.
 */
public class DownLoadProgress extends Dialog {

    private static final String TAG = getTAG(DownLoadProgress.class);
    private static DownLoadProgress dialog;
    private static ProgressBar pb;
    private static TextView hint_tv;

    public DownLoadProgress(Context context) {
        super(context);
    }

    public DownLoadProgress(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 弹出自定义ProgressDialog
     *
     * @return
     */
    public static void downLoadProgressShow(Activity activity, String hint, View.OnClickListener clickListener) {
        if (activity.isFinishing()) {
            upDateProgressCancle();
            return;
        }
        if (dialog == null) {
            dialog = new DownLoadProgress(activity, R.style.Custom_Progress);
            dialog.setTitle("");
            dialog.setContentView(R.layout.module_dialog_download_progress_layout);
            pb = (ProgressBar) dialog.findViewById(R.id.progressbar);
            final LinearLayout bottom_layout = (LinearLayout) dialog.findViewById(R.id.bottom_layout);

            hint_tv = (TextView) dialog.findViewById(R.id.hint_tv);
            final TextView update_bt = (TextView) dialog.findViewById(R.id.update_bt);
            final TextView cancel_bt = (TextView) dialog.findViewById(R.id.cancel_bt);


            if (!isEmpty(hint))
                hint_tv.setText(hint);

            cancel_bt.setOnClickListener(clickListener);

            dialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    upDateProgressCancle();
                }
            });
            // 按返回键是否取消
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            // 设置居中
            dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            // 设置背景层透明度
            lp.dimAmount = 0.2f;
            dialog.getWindow().setAttributes(lp);
            // dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
            dialog.show();
            Log.i(TAG, "dialog show!!!!");
        } else
            dialog.show();

    }

    public static void setUpDateProgress(int pro) {
        if (dialog != null && pb != null && pb.getVisibility() == View.VISIBLE && pb.getProgress() < pro)
            pb.setProgress(pro);
    }

    public static void setHint(String hint) {
        if (dialog != null && hint_tv != null && hint_tv.getVisibility() == View.VISIBLE && hint_tv.getText().toString().isEmpty())
            hint_tv.setText(hint);
    }

    public static void upDateProgressCancle() {
        if (dialog != null) {
            Log.i(TAG, "dialog Cancle!!!!");
            dialog.cancel();
            pb = null;
            dialog = null;
        }
    }
}