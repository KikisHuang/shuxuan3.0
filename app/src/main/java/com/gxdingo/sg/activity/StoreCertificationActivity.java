package com.gxdingo.sg.activity;

import android.graphics.Color;
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
import com.bumptech.glide.Glide;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.BusinessScopeEvent;
import com.gxdingo.sg.bean.SelectAddressEvent;
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

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;
import static com.gxdingo.sg.http.Api.CLIENT_SERVICE_AGREEMENT_KEY;
import static com.gxdingo.sg.http.Api.STORE_NAMING_RULES;
import static com.gxdingo.sg.http.Api.STORE_SHOP_AGREEMENT_KEY;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.CommonUtils.getd;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;
import static com.kikis.commnlibrary.utils.KikisUitls.getContext;

/**
 * ????????????
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

    @BindView(R.id.tv_status)
    public TextView tv_status;

    @BindView(R.id.business_layout)
    public ConstraintLayout businessLayout;

    @BindView(R.id.iv_result_status)
    public ImageView ivResultStatus;

    @BindView(R.id.ll_certification_result_layout)
    public LinearLayout llCertificationResultLayout;

    @BindView(R.id.btn_result_botton)
    public Button btnResultBotton;

    private int mType;//1???????????????2??????????????????

    private String mAvatar;//??????

    private String mBusinessLicense;//????????????

    private String mBranchInformationLicense;//?????????

    private BusinessScopeEvent mBusinessScope;//??????????????????

    private PoiItem mPoiItem;//??????POI??????

    private boolean isCheck = false;

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
    protected void onBaseCreate() {
        super.onBaseCreate();
    }

    @Override
    protected void init() {
        titleLayout.setTitleTextSize(16);
        titleLayout.setTitleText("????????????");
        initAgreementStyle();
    }

    @Override
    protected void initData() {
        //??????????????????
        getP().getLoginInfoStatus();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //????????????
        if (hasFocus && !isCheck) {
            isCheck = true;
            getP().getInvitationCode();
        }
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }


    @OnClick({R.id.upload_branch_information_license, R.id.iv_del_branch_information_license, R.id.et_store_name_rule, R.id.title_back, R.id.stv_avatar, R.id.stv_business_scope, R.id.stv_select_address, R.id.tv_upload_business_license, R.id.iv_del_business_license, R.id.btn_submit, R.id.btn_result_botton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back://??????
                    finish();
                break;
            case R.id.stv_avatar://??????
                mType = 1;
                uploadImage(true);
                break;
            case R.id.et_store_name_rule://??????????????????
                goToPagePutSerializable(StoreCertificationActivity.this, WebActivity.class, getIntentEntityMap(new Object[]{true, 0, STORE_NAMING_RULES}));
                break;
            case R.id.stv_business_scope://??????????????????
                goToPage(this, StoreBusinessScopeActivity.class, null);
                break;
            case R.id.stv_select_address://??????????????????
                goToPagePutSerializable(this, SelectAddressActivity.class, getIntentEntityMap(new Object[]{false}));
                break;
            case R.id.upload_branch_information_license://?????????
                mType = 3;
                uploadImage(false);
                break;
            case R.id.tv_upload_business_license://????????????
                mType = 2;
                uploadImage(false);
                break;
            case R.id.iv_del_branch_information_license:
                shwoDelPhotoImg(0);
                break;
            case R.id.iv_del_business_license:
                shwoDelPhotoImg(1);
                break;
            case R.id.btn_submit://????????????
                if (isEmpty(mAvatar)) {
                    onMessage("????????????????????????");
                    return;
                } else if (isEmpty(etStoreName.getText().toString())) {
                    onMessage("????????????????????????");
                    etStoreName.requestFocus();
                    return;
                } else if (mBusinessScope == null) {
                    onMessage("????????????????????????");
                    return;
                } else if (mPoiItem == null) {
                    onMessage("????????????????????????");
                    return;
                } else if (isEmpty(etDetailsAddress.getText().toString())) {
                    onMessage("????????????????????????");
                    etDetailsAddress.requestFocus();
                    return;
                } else if (isEmpty(mBusinessLicense)) {
                    onMessage("????????????????????????");
                    return;
                }
                getP().submitCertification(this, mAvatar, etStoreName.getText().toString()
                        , mBusinessScope.data, mPoiItem.getAdCode(), etDetailsAddress.getText().toString(), mBusinessLicense, mBranchInformationLicense
                        , mPoiItem.getLatLonPoint().getLongitude(), mPoiItem.getLatLonPoint().getLatitude());
                break;
            case R.id.btn_result_botton:

                if (btnResultBotton.getText().equals("????????????")) {
                    businessLayout.setVisibility(View.VISIBLE);
                    llCertificationResultLayout.setVisibility(View.GONE);
                } else
                    //??????????????????
                    getP().getLoginInfoStatus();
                break;
        }
    }

    private void shwoDelPhotoImg(int type) {
        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true) //???????????????????????????????????????????????????
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
             * ????????????
             */
            mBusinessScope = (BusinessScopeEvent) object;
            stvBusinessScope.setRightString(mBusinessScope.selectedScope);
        } else if (object instanceof SelectAddressEvent) {
            /**
             * ????????????
             */
            SelectAddressEvent event = (SelectAddressEvent) object;

            mPoiItem = (PoiItem) event.poiItem;
            stvSelectAddress.setRightString(mPoiItem.getTitle());
        }
    }

    /**
     * ????????????
     *
     * @param isCrop ????????????
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
     * ?????????????????????
     */
    private void initAgreementStyle() {
        final SpannableStringBuilder style = new SpannableStringBuilder();
        //????????????
        style.append(getString(R.string.store_have_read_agreement2));
        //??????????????????????????????
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
        //??????????????????????????????
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
        //????????????????????????
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
     * ??????????????????
     *
     * @param url ??????????????????????????????URL??????
     */
    @Override
    public void uploadImage(String url) {
        if (isEmpty(url))
            return;
        switch (mType) {
            case 1://????????????
                mAvatar = url;
                Glide.with(reference.get()).load(isEmpty(url) ? R.drawable.module_svg_user_default_avatar : url).apply(GlideUtils.getInstance().getCircleCrop()).into(stvAvatar.getLeftIconIV());
                break;
            case 2://??????????????????
                mBusinessLicense = url;
                Glide.with(reference.get()).load(url).into(ivBusinessLicense);
                rlBusinessLicenseLayout.setVisibility(View.VISIBLE);
                tvUploadBusinessLicense.setVisibility(View.GONE);
                break;
            case 3://???????????????
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
     * ???????????????
     */
    @Override
    public void certificationPassed() {
        sendEvent(StoreLocalConstant.SOTRE_REVIEW_SUCCEED);
        onMessage("???????????????");
        finish();
    }

    /**
     * ?????????
     */
    @Override
    public void onReview() {
        titleLayout.setTitleText("");
        businessLayout.setVisibility(View.GONE);
        llCertificationResultLayout.setVisibility(View.VISIBLE);

        tv_status.setText("????????????");
        tv_status.setTextColor(getc(R.color.green_dominant_tone));

        ivResultStatus.setImageResource(R.drawable.module_svg_store_certification_audit_status);

        btnResultBotton.setText("????????????");

    }

    /**
     * ?????????
     *
     * @param rejectReason
     */
    @Override
    public void rejected(String rejectReason) {

        businessLayout.setVisibility(View.GONE);
        llCertificationResultLayout.setVisibility(View.VISIBLE);


        onMessage("??????????????????????????????");
        titleLayout.setTitleText("????????????");

        if (!isEmpty(rejectReason)) {
            tv_status.setText(rejectReason);
            tv_status.setTextColor(getc(R.color.red_dominant_tone));
        }
        ivResultStatus.setImageDrawable(getd(R.drawable.module_svg_store_certification_audit_failed_status));

        btnResultBotton.setText("????????????");

    }

    /**
     * ????????????????????????
     *
     * @param type
     */
    @Override
    public void showActivityTypeLayout(int type) {

        branch_information_layout.setVisibility(View.VISIBLE);

    }

    @Override
    public void setOssSpecialQualificationsImg(int position, String path) {

    }
}
