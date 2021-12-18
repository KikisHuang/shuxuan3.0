package com.gxdingo.sg.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.allen.library.SuperTextView;
import com.amap.api.services.core.PoiItem;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.BusinessScopeEvent;
import com.gxdingo.sg.bean.StoreBusinessScopeBean;
import com.gxdingo.sg.biz.StoreCertificationContract;
import com.gxdingo.sg.dialog.SgConfirm2ButtonPopupView;
import com.gxdingo.sg.dialog.SgConfirmPopupView;
import com.gxdingo.sg.presenter.StoreCertificationPresenter;
import com.gxdingo.sg.utils.StoreLocalConstant;
import com.gxdingo.sg.view.RegexEditText;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.bean.ReLoginBean;
import com.kikis.commnlibrary.dialog.BaseActionSheetPopupView;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.utils.GlideUtils;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;
import static com.gxdingo.sg.http.ClientApi.CLIENT_SERVICE_AGREEMENT_KEY;
import static com.gxdingo.sg.http.ClientApi.STORE_NAMING_RULES;
import static com.gxdingo.sg.http.HttpClient.switchGlobalUrl;
import static com.gxdingo.sg.http.StoreApi.STORE_SHOP_AGREEMENT_KEY;
import static com.gxdingo.sg.utils.LocalConstant.LOGIN_WAY;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;
import static com.kikis.commnlibrary.utils.KikisUitls.getContext;

/**
 * 商家认证
 *
 * @author JM
 */
public class StoreCertificationActivity extends BaseMvpActivity<StoreCertificationContract.StoreCertificationPresenter>
        implements StoreCertificationContract.StoreCertificationListener {


    @BindView(R.id.stv_avatar)
    public SuperTextView stvAvatar;

    @BindView(R.id.et_store_name_rule)
    public TextView etStoreNameRule;

    @BindView(R.id.et_store_name)
    public RegexEditText etStoreName;

    @BindView(R.id.t_store_name_layout)
    public LinearLayout tStoreNameLayout;

    @BindView(R.id.stv_business_scope)
    public SuperTextView stvBusinessScope;

    @BindView(R.id.stv_select_address)
    public SuperTextView stvSelectAddress;

    @BindView(R.id.et_details_address)
    public RegexEditText etDetailsAddress;

    @BindView(R.id.ll_details_address_layout)
    public LinearLayout llDetailsAddressLayout;

    @BindView(R.id.tv_upload_business_license)
    public TextView tvUploadBusinessLicense;


    @BindView(R.id.rl_business_license_layout)
    public RelativeLayout rlBusinessLicenseLayout;

    @BindView(R.id.ll_business_license_layout)
    public RelativeLayout llBusinessLicenseLayout;

    @BindView(R.id.branch_information_layout)
    public RelativeLayout branch_information_layout;

    @BindView(R.id.upload_branch_information_license)
    public TextView upload_branch_information_license;

    @BindView(R.id.branch_information_license_layout)
    public RelativeLayout branch_information_license_layout;


    @BindView(R.id.iv_branch_information_license)
    public ImageView iv_branch_information_license;

    @BindView(R.id.iv_del_branch_information_license)
    public ImageView iv_del_branch_information_license;

    @BindView(R.id.cb_agreement)
    public CheckBox cbAgreement;

    @BindView(R.id.tv_have_read_agreement)
    public TextView tvHaveReadAgreement;

    @BindView(R.id.btn_submit)
    public Button btnSubmit;

    @BindView(R.id.iv_business_license)
    public ImageView ivBusinessLicense;

    @BindView(R.id.iv_del_business_license)
    public ImageView ivDelBusinessLicense;

    @BindView(R.id.title_layout)
    public TemplateTitle titleLayout;

    @BindView(R.id.tv_right_button)
    public TextView tvRightButton;

    @BindView(R.id.business_layout)
    public ConstraintLayout businessLayout;

    @BindView(R.id.iv_result_status)
    public ImageView ivResultStatus;

    @BindView(R.id.tv_status)
    public TextView tvStatus;

    @BindView(R.id.ll_certification_result_layout)
    public LinearLayout llCertificationResultLayout;

    @BindView(R.id.btn_result_botton)
    public Button btnResultBotton;


    private int mType;//1上传头像，2上传营业执照
    private String mAvatar;//头像
    private String mBusinessLicense;//营业执照
    private String mBranchInformationLicense;//门头照
    private BusinessScopeEvent mBusinessScope;//经营范围事件
    private PoiItem mPoiItem;//地图POI信息

    private boolean isUser = false;

    @Override
    protected StoreCertificationContract.StoreCertificationPresenter createPresenter() {
        return new StoreCertificationPresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return true;
    }

    @Override
    protected int activityTitleLayout() {
        return R.layout.module_include_new_custom_title;
    }

    @Override
    protected boolean ImmersionBar() {
        return true;
    }

    @Override
    protected int StatusBarColors() {
        return R.color.white;
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
        return R.layout.module_activity_store_certification;
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
        titleLayout.setTitleTextSize(16);
        titleLayout.setTitleText("认证信息");
        isUser = getIntent().getBooleanExtra(Constant.SERIALIZABLE + 0, false);
        initAgreementStyle();
    }

    @Override
    protected void initData() {
        //刷新登录信息
        getP().getLoginInfoStatus();

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //监测口令
        if (hasFocus)
            getP().getInvitationCode();
    }

    @Override
    public void onBackPressed() {
        if (!isUser)
            showBackDialog();
        else
            super.onBackPressed();
    }

    /**
     * 退出弹出
     */
    private void showBackDialog() {
        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .isDarkTheme(false)
                .dismissOnTouchOutside(false)
                .asCustom(new SgConfirm2ButtonPopupView(reference.get(), "确定要退出登录吗？", "", () -> {
                    if (!isUser) {
                        getP().logout();//请求退出接口
                        sendEvent(new ReLoginBean());//重新登录全局事件
                    }
                    finish();
                }))
                .show();
    }

    @OnClick({R.id.upload_branch_information_license, R.id.iv_del_branch_information_license, R.id.et_store_name_rule, R.id.title_back, R.id.stv_avatar, R.id.stv_business_scope, R.id.stv_select_address, R.id.tv_upload_business_license, R.id.iv_del_business_license, R.id.btn_submit, R.id.btn_result_botton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back://返回
                if (!isUser)
                    showBackDialog();
                else
                    finish();
                break;
            case R.id.stv_avatar://头像
                mType = 1;
                uploadImage(true);
                break;
            case R.id.et_store_name_rule://店铺名称规则
                goToPagePutSerializable(StoreCertificationActivity.this, WebActivity.class, getIntentEntityMap(new Object[]{true, 0, STORE_NAMING_RULES}));
                break;
            case R.id.stv_business_scope://选择经营范围
                goToPage(this, StoreBusinessScopeActivity.class, null);
                break;
            case R.id.stv_select_address://选择店铺地址
                goToPage(this, SelectAddressActivity.class, null);
                break;
            case R.id.upload_branch_information_license://门头照
                mType = 3;
                uploadImage(false);
                break;
            case R.id.tv_upload_business_license://营业执照
                mType = 2;
                uploadImage(false);
                break;
            case R.id.iv_del_branch_information_license:
                shwoDelPhotoImg(0);
                break;
            case R.id.iv_del_business_license:
                shwoDelPhotoImg(1);
                break;
            case R.id.btn_submit://提交审核
                if (isEmpty(mAvatar)) {
                    onMessage("请上传店铺头像！");
                    return;
                } else if (isEmpty(etStoreName.getText().toString())) {
                    onMessage("请输入店铺名称！");
                    etStoreName.requestFocus();
                    return;
                } else if (mBusinessScope == null) {
                    onMessage("请选择经营范围！");
                    return;
                } else if (mPoiItem == null) {
                    onMessage("请选择店铺地址！");
                    return;
                } else if (isEmpty(etDetailsAddress.getText().toString())) {
                    onMessage("请输入详细地址！");
                    etDetailsAddress.requestFocus();
                    return;
                } else if (isEmpty(mBusinessLicense)) {
                    onMessage("请上传营业执照！");
                    return;
                }
                getP().submitCertification(this, mAvatar, etStoreName.getText().toString()
                        , mBusinessScope.data, mPoiItem.getAdCode(), etDetailsAddress.getText().toString(), mBusinessLicense, mBranchInformationLicense
                        , mPoiItem.getLatLonPoint().getLongitude(), mPoiItem.getLatLonPoint().getLatitude());
                break;
            case R.id.btn_result_botton:
                //刷新登录信息
                getP().getLoginInfoStatus();
                break;
        }
    }

    private void shwoDelPhotoImg(int type) {
        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .isDarkTheme(false)
                .dismissOnTouchOutside(false)
                .asCustom(new SgConfirmPopupView(reference.get(), type == 0 ? gets(R.string.del_store_license_image) : gets(R.string.del_business_license_image), "", () -> {

                    if (type == 0) {
                        mBranchInformationLicense = "";
                        branch_information_license_layout.setVisibility(View.GONE);
                        upload_branch_information_license.setVisibility(View.VISIBLE);

                    } else {
                        mBusinessLicense = "";
                        rlBusinessLicenseLayout.setVisibility(View.GONE);
                        tvUploadBusinessLicense.setVisibility(View.VISIBLE);
                    }

                }))
                .show();
    }

    @Override
    public void onBaseEvent(Object object) {
        super.onBaseEvent(object);
        if (object instanceof BusinessScopeEvent) {
            /**
             * 经营范围
             */
            mBusinessScope = (BusinessScopeEvent) object;
            stvBusinessScope.setRightString(mBusinessScope.selectedScope);
        } else if (object instanceof PoiItem) {
            /**
             * 店铺地址
             */
            mPoiItem = (PoiItem) object;
            stvSelectAddress.setRightString(mPoiItem.getTitle());
        }
    }

    /**
     * 上传图片
     *
     * @param isCrop 是否裁剪
     */
    private void uploadImage(boolean isCrop) {
        new XPopup.Builder(reference.get())
                .isDarkTheme(false)
                .isDestroyOnDismiss(true)
                .asCustom(new BaseActionSheetPopupView(reference.get()).addSheetItem(gets(R.string.photo_album), gets(R.string.photo_graph)).setItemClickListener((itemv, pos) -> {
                    getP().photoItemClick(mType, pos, isCrop);
                })).show();
    }

    /**
     * 初始化协议样式
     */
    private void initAgreementStyle() {
        final SpannableStringBuilder style = new SpannableStringBuilder();
        //设置文字
        style.append(getString(R.string.store_have_read_agreement2));
        //设置部分文字点击事件
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                goToPagePutSerializable(StoreCertificationActivity.this, WebActivity.class, getIntentEntityMap(new Object[]{true, 0, CLIENT_SERVICE_AGREEMENT_KEY}));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        style.setSpan(clickableSpan, 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置部分文字点击事件
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                goToPagePutSerializable(StoreCertificationActivity.this, WebActivity.class, getIntentEntityMap(new Object[]{true, 0, STORE_SHOP_AGREEMENT_KEY}));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        style.setSpan(clickableSpan2, 7, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置部分文字颜色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#74A0EC"));
        style.setSpan(foregroundColorSpan, 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan foregroundColorSpan2 = new ForegroundColorSpan(Color.parseColor("#74A0EC"));
        style.setSpan(foregroundColorSpan2, 7, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvHaveReadAgreement.setMovementMethod(LinkMovementMethod.getInstance());
        tvHaveReadAgreement.setHighlightColor(Color.TRANSPARENT);
        tvHaveReadAgreement.setText(style);
        cbAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btnSubmit.setEnabled(isChecked);
                if (isChecked)
                    btnSubmit.setBackgroundResource(R.drawable.module_btn_theme_button_can_be_clicked);
                else
                    btnSubmit.setBackgroundResource(R.drawable.module_btn_theme_button_not_can_be_clicked);
            }
        });
    }

    /**
     * 上传图片回调
     *
     * @param url 服务器返回上传图片的URL路径
     */
    @Override
    public void uploadImage(String url) {
        if (isEmpty(url))
            return;
        switch (mType) {
            case 1://头像图片
                mAvatar = url;
                Glide.with(reference.get()).load(isEmpty(url) ? R.drawable.module_svg_user_default_avatar : url).apply(GlideUtils.getInstance().getCircleCrop()).into(stvAvatar.getLeftIconIV());
                break;
            case 2://营业执照图片
                mBusinessLicense = url;
                Glide.with(reference.get()).load(url).into(ivBusinessLicense);
                rlBusinessLicenseLayout.setVisibility(View.VISIBLE);
                tvUploadBusinessLicense.setVisibility(View.GONE);
                break;
            case 3://店铺门头照
                mBranchInformationLicense = url;
                Glide.with(reference.get()).load(url).into(iv_branch_information_license);
                branch_information_license_layout.setVisibility(View.VISIBLE);
                upload_branch_information_license.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onBusinessScopeResult(List<StoreBusinessScopeBean.ListBean> businessScopes) {

    }

    @Override
    public void closeBusinessScope(BusinessScopeEvent businessScopeEvent) {

    }

    /**
     * 已认证通过
     */
    @Override
    public void certificationPassed() {
        //如果是用户端入驻，切换全局路径，切换登录方式。
        if (isUser) {
            switchGlobalUrl(!isUser);

            SPUtils.getInstance().put(LOGIN_WAY, !isUser);

            if (StoreActivity.getInstance() == null)
                goToPage(getContext(), StoreActivity.class, null);
        }
        sendEvent(StoreLocalConstant.SOTRE_REVIEW_SUCCEED);
        onMessage("店铺已认证");
        finish();
    }

    /**
     * 在审核
     */
    @Override
    public void onReview() {
        titleLayout.setTitleText("");
        businessLayout.setVisibility(View.GONE);
        llCertificationResultLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 被驳回
     */
    @Override
    public void rejected() {
        onMessage("已被驳回，请重新提交");
        titleLayout.setTitleText("认证信息");
        businessLayout.setVisibility(View.VISIBLE);
        llCertificationResultLayout.setVisibility(View.GONE);
    }

    /**
     * 显示活动类型布局
     *
     * @param type
     */
    @Override
    public void showActivityTypeLayout(int type) {

        branch_information_layout.setVisibility(View.VISIBLE);

    }
}
