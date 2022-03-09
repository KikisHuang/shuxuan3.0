package com.gxdingo.sg.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.MyConfirmListener;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.utils.GlideUtils;
import com.lxj.xpopup.core.CenterPopupView;

import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * @author: Kikis
 * @date: 2022/3/3
 * @page: 上传食品经营许可证弹窗
 */
public class UpLoadLicencePopupView extends CenterPopupView implements View.OnClickListener {

    private CustomResultListener confirmListener;
    private TextView tv_confirm, upload_tv, tv_title;
    private ImageView close_img, licence_img;
    private String title;

    /**
     * @param context
     */
    public UpLoadLicencePopupView(@NonNull Context context, String title, CustomResultListener confirmListener) {
        super(context);
        this.confirmListener = confirmListener;
        this.title = title;
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_base_sg_xpopup_upload_licence;
    }

    @Override
    protected int getMaxWidth() {
        return (int) (ScreenUtils.getScreenWidth() * 2.6 / 3);
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        close_img = findViewById(R.id.close_img);
        tv_confirm = findViewById(R.id.tv_confirm);
        upload_tv = findViewById(R.id.upload_tv);
        licence_img = findViewById(R.id.licence_img);
        tv_title = findViewById(R.id.tv_title);

        if (!isEmpty(title))
            tv_title.setText(title);

        close_img.setOnClickListener(this);
        licence_img.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
        upload_tv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.close_img) {
            dismiss();
        } else if (v.getId() == R.id.tv_confirm) {
            if (confirmListener != null) confirmListener.onResult(0);
            if (popupInfo.autoDismiss) dismiss();
        } else if (v.getId() == R.id.upload_tv || v.getId() == R.id.licence_img) {
            if (confirmListener != null) confirmListener.onResult(1);

        }
    }

    public void setLicenceImg(String url) {
        Glide.with(getContext()).load(url).apply(new RequestOptions()
                .dontAnimate()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)).into(licence_img);
    }
}
