package com.gxdingo.sg.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientCouponBean;
import com.gxdingo.sg.biz.ClientCouponContract;
import com.gxdingo.sg.presenter.ClientCouponPresenter;
import com.gxdingo.sg.utils.DateUtils;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * @author: Weaving
 * @date: 2021/10/31
 * @page:
 */
public class ClientCouponDetailsActivity extends BaseMvpActivity<ClientCouponContract.ClientCouponPresenter> {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.coupon_title_tv)
    public TextView coupon_title_tv;

    @BindView(R.id.qr_code_iv)
    public ImageView qr_code_iv;

    @BindView(R.id.valid_date_tv)
    public TextView valid_date_tv;

    private ClientCouponBean mCouponBean;

    @Override
    protected ClientCouponContract.ClientCouponPresenter createPresenter() {
        return new ClientCouponPresenter();
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
        return R.layout.module_activity_client_coupon_details;
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
        title_layout.setBackgroundColor(getc(R.color.transparent));
        mCouponBean = (ClientCouponBean) getIntent().getSerializableExtra(Constant.SERIALIZABLE + 0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mCouponBean = (ClientCouponBean) intent.getSerializableExtra(Constant.SERIALIZABLE + 0);
        initData();
    }

    @Override
    protected void initData() {
        if (mCouponBean == null) {
            onMessage("未获取到优惠劵信息!");
            finish();
            return;
        }
        coupon_title_tv.setText("扫码立减" + mCouponBean.getCouponAmount() + "元");
//        Bitmap qrCodeBitmap = QRCodeUtil.createQRCodeBitmap(mCouponBean.getCouponIdentifier(), 200, 200);

        Glide.with(reference.get()).asBitmap().load(!isEmpty(mCouponBean.storeAvatar) ? mCouponBean.storeAvatar : R.mipmap.ic_user_default_avatar).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                Bitmap qrCodeBitmap = CodeUtils.createImage(mCouponBean.getCouponIdentifier(), 280, 280, resource);

                qr_code_iv.setImageBitmap(qrCodeBitmap);
            }
        });

        valid_date_tv.setText("有效期至：" + mCouponBean.getExpireTime());
    }

    @OnClick(R.id.other_coupon_stv)
    public void OnClickViews(View v) {
        switch (v.getId()) {
            case R.id.other_coupon_stv:
                goToPage(this, ClientCouponListActivity.class, null);
                break;
        }
    }
}
