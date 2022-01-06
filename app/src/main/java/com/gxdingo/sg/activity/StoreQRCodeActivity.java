package com.gxdingo.sg.activity;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.DistanceBean;
import com.gxdingo.sg.bean.StoreDetailBean;
import com.gxdingo.sg.bean.StoreQRCodeBean;
import com.gxdingo.sg.biz.StoreSettingsContract;
import com.gxdingo.sg.presenter.StoreSettingsPresenter;
import com.gxdingo.sg.utils.QRCodeUtil;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.GlideUtils;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.tencent.smtt.sdk.WebView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.blankj.utilcode.util.ClipboardUtils.copyText;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * @author: Weaving
 * @date: 2021/11/10
 * @page:
 */
public class StoreQRCodeActivity extends BaseMvpActivity<StoreSettingsContract.StoreSettingsPresenter> implements StoreSettingsContract.StoreSettingsListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.coupon_title_tv)
    public TextView coupon_title_tv;

    @BindView(R.id.qr_code_iv)
    public ImageView qr_code_iv;

    @BindView(R.id.invitation_code_tv)
    public TextView invitation_code_tv;

    @BindView(R.id.webView)
    public WebView webView;


    @Override
    protected StoreSettingsContract.StoreSettingsPresenter createPresenter() {
        return new StoreSettingsPresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return false;
    }

    @Override
    protected int activityTitleLayout() {
        return R.layout.module_include_custom_title;
    }

    @Override
    protected boolean ImmersionBar() {
        return true;
    }

    @Override
    protected int StatusBarColors() {
        return R.color.divide_color;
    }

    @Override
    protected int NavigationBarColor() {
        return 0;
    }

    @Override
    protected int activityBottomLayout() {
        return 0;
    }

    @Override
    protected View noDataLayout() {
        return null;
    }

    @Override
    protected View refreshLayout() {
        return null;
    }

    @Override
    protected boolean closeDispatchTouchEvent() {
        return false;
    }

    @Override
    protected int initContentView() {
        return R.layout.module_activity_store_exclusive_qr_code;
    }

    @Override
    protected boolean refreshEnable() {
        return false;
    }

    @Override
    protected boolean loadmoreEnable() {
        return false;
    }

    @Override
    protected void init() {
        title_layout.setBackgroundColor(getc(R.color.divide_color));
    }

    @Override
    protected void initData() {
        getP().getQrCode();
    }

    @OnClick(R.id.btn_copy)
    public void OnClickView() {

        copyText(invitation_code_tv.getText().toString());
        onMessage("已复制到剪切板");
    }

    @Override
    public void onInfoResult(StoreDetailBean storeDetailBean) {

    }

    @Override
    public void onQRResult(StoreQRCodeBean qrCodeBean) {

        coupon_title_tv.setText(qrCodeBean.getStoreName());

        Bitmap qrCodeBitmap = QRCodeUtil.createQRCodeBitmap(qrCodeBean.getActiveCode(), 280, 280);

        Glide.with(reference.get()).asBitmap().load(!isEmpty(UserInfoUtils.getInstance().getUserAvatar()) ? UserInfoUtils.getInstance().getUserAvatar() : R.mipmap.ic_user_default_avatar).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Bitmap LogoQrCodeBtmp = QRCodeUtil.addLogo(qrCodeBitmap, resource, 0.15f);
                qr_code_iv.setImageBitmap(LogoQrCodeBtmp);
            }
        });

        invitation_code_tv.setText(qrCodeBean.getActiveCode());
        if (!isEmpty(qrCodeBean.getExplain()))
            webView.loadDataWithBaseURL(null, qrCodeBean.getExplain(), "text/html", "utf-8", null);
        else
            webView.setVisibility(View.GONE);
    }

    @Override
    public void onDistanceResult(List<DistanceBean> distanceBeans) {

    }
}
