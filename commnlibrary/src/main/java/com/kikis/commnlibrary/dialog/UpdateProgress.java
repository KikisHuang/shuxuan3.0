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
import com.kikis.commnlibrary.biz.UpDateListener;

import static com.blankj.utilcode.util.ObjectUtils.isEmpty;
import static com.kikis.commnlibrary.utils.CommonUtils.getTAG;

/**
 * Created by lian on 2017/6/19.
 */
public class UpdateProgress extends Dialog {
    private static final String TAG = getTAG(UpdateProgress.class);
    private static UpdateProgress dialog;
    private static ProgressBar pb;

    public UpdateProgress(Context context) {
        super(context);
    }

    public UpdateProgress(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 弹出自定义ProgressDialog
     *
     * @param isCompulsive 是否强制更新
     * @return
     */
    public static void upDateProgressShow(Activity activity, boolean isCompulsive, String hint, final UpDateListener upDateListener) {
        if (activity.isFinishing()) {
            upDateProgressCancle();
            return;
        }
        if (dialog == null) {
            dialog = new UpdateProgress(activity, R.style.Custom_Progress);
            dialog.setTitle("");
            dialog.setContentView(R.layout.module_dialog_update_progress_layout);
            pb = (ProgressBar) dialog.findViewById(R.id.progressbar);
            LinearLayout bottom_layout = (LinearLayout) dialog.findViewById(R.id.bottom_layout);

            TextView hint_tv = (TextView) dialog.findViewById(R.id.hint_tv);
            final TextView update_bt = (TextView) dialog.findViewById(R.id.update_bt);
            final TextView cancel_bt = (TextView) dialog.findViewById(R.id.cancel_bt);

            if (!isEmpty(hint))
                hint_tv.setText(hint);

            pb.setVisibility(!isCompulsive ? View.INVISIBLE : View.VISIBLE);

            bottom_layout.setVisibility(!isCompulsive ? View.VISIBLE : View.GONE);

            cancel_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    upDateProgressCancle();
                }
            });

            if (upDateListener != null) {

                update_bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        upDateListener.onUpDate();
//                        pb.setVisibility(View.VISIBLE);
                        cancel_bt.setVisibility(View.GONE);
                        update_bt.setVisibility(View.GONE);
                    }
                });
            }
            dialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    upDateProgressCancle();
                }
            });
            // 按返回键是否取消
            dialog.setCancelable(true);
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
        if (dialog != null && pb != null && pb.getVisibility() == View.VISIBLE)
            pb.setProgress(pro);
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