package com.gxdingo.sg.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
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
import com.gxdingo.sg.bean.StoreBusinessScopeBean;
import com.gxdingo.sg.biz.StoreCertificationContract;
import com.gxdingo.sg.dialog.SgConfirmPopupView;
import com.gxdingo.sg.presenter.StoreCertificationPresenter;
import com.gxdingo.sg.view.RegexEditText;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.dialog.BaseActionSheetPopupView;
import com.kikis.commnlibrary.utils.GlideUtils;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;

/**
 * 商家认证
 *
 * @author JM
 */
public class StoreCertificationActivity extends BaseMvpActivity<StoreCertificationContract.StoreCertificationPresenter>
        implements StoreCertificationContract.StoreCertificationListener {


    @BindView(R.id.stv_avatar)
    SuperTextView stvAvatar;
    @BindView(R.id.et_store_name_rule)
    TextView etStoreNameRule;
    @BindView(R.id.et_store_name)
    RegexEditText etStoreName;
    @BindView(R.id.t_store_name_layout)
    LinearLayout tStoreNameLayout;
    @BindView(R.id.stv_business_scope)
    SuperTextView stvBusinessScope;
    @BindView(R.id.stv_select_address)
    SuperTextView stvSelectAddress;
    @BindView(R.id.et_details_address)
    RegexEditText etDetailsAddress;
    @BindView(R.id.ll_details_address_layout)
    LinearLayout llDetailsAddressLayout;
    @BindView(R.id.tv_upload_business_license)
    TextView tvUploadBusinessLicense;
    @BindView(R.id.rl_business_license_layout)
    RelativeLayout rlBusinessLicenseLayout;
    @BindView(R.id.ll_business_license_layout)
    RelativeLayout llBusinessLicenseLayout;
    @BindView(R.id.cb_agreement)
    CheckBox cbAgreement;
    @BindView(R.id.tv_have_read_agreement)
    TextView tvHaveReadAgreement;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.layout)
    ConstraintLayout layout;
    @BindView(R.id.iv_business_license)
    ImageView ivBusinessLicense;
    @BindView(R.id.iv_del_business_license)
    ImageView ivDelBusinessLicense;
    @BindView(R.id.title_layout)
    TemplateTitle titleLayout;
    @BindView(R.id.tv_right_button)
    TextView tvRightButton;


    private int mType;//1上传头像，2上传营业执照
    private String mAvatar;//头像
    private String mBusinessLicense;//营业执照
    private BusinessScopeEvent businessScope;
    private PoiItem poiItem;

    @Override
    protected StoreCertificationContract.StoreCertificationPresenter createPresenter() {
        return new StoreCertificationPresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return false;
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
        initAgreementStyle();

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.stv_avatar, R.id.stv_business_scope, R.id.stv_select_address, R.id.tv_upload_business_license, R.id.iv_del_business_license, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.stv_avatar:
                mType = 1;
                uploadImage(true);
                break;
            case R.id.stv_business_scope:
                goToPage(this, StoreBusinessScopeActivity.class, null);
                break;
            case R.id.stv_select_address:
                break;
            case R.id.tv_upload_business_license:
                mType = 2;
                uploadImage(false);
                break;
            case R.id.iv_del_business_license:
                new XPopup.Builder(reference.get())
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .isDarkTheme(false)
                        .dismissOnTouchOutside(false)
                        .asCustom(new SgConfirmPopupView(reference.get(), gets(R.string.del_business_license_image), "", () -> {
                            mBusinessLicense = "";
                            rlBusinessLicenseLayout.setVisibility(View.GONE);
                            tvUploadBusinessLicense.setVisibility(View.VISIBLE);
                        }))
                        .show();
                break;
            case R.id.btn_submit:
                break;
        }
    }

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
        if (object instanceof BusinessScopeEvent) {
            businessScope = (BusinessScopeEvent) object;
            stvBusinessScope.setRightString(businessScope.selectedScope);
        } else if (object instanceof PoiItem) {

            poiItem = (PoiItem) object;

//            String a = poiItem.getProvinceCode();
            String b = poiItem.getAdCode();

//            regionPath = b;
//
//            stv_select_address.setRightString(poiItem.getTitle());
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

//                Intent intent = new Intent(StartActivity.this, ShowContentActivity.class);
//                intent.putExtra("TITLE", "隐私政策");
//                intent.putExtra("CONTENT", content);
//                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        style.setSpan(clickableSpan, 0, 6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //设置部分文字点击事件
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

//                Intent intent = new Intent(StartActivity.this, ShowContentActivity.class);
//                intent.putExtra("TITLE", "用户协议");
//                intent.putExtra("CONTENT", content);
//                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        style.setSpan(clickableSpan2, 7, 13, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //设置部分文字颜色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#74A0EC"));
        style.setSpan(foregroundColorSpan, 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan foregroundColorSpan2 = new ForegroundColorSpan(Color.parseColor("#74A0EC"));
        style.setSpan(foregroundColorSpan2, 7, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
        switch (mType) {
            case 1:
                mAvatar = url;
                Glide.with(reference.get()).load(isEmpty(url) ? R.drawable.module_svg_user_default_avatar : url).apply(GlideUtils.getInstance().getCircleCrop()).into(stvAvatar.getLeftIconIV());
                break;

            case 2:
                if (!isEmpty(url)) {
                    mBusinessLicense = url;
                    Glide.with(reference.get()).load(url).into(ivBusinessLicense);
                    rlBusinessLicenseLayout.setVisibility(View.VISIBLE);
                    tvUploadBusinessLicense.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void onBusinessScopeResult(List<StoreBusinessScopeBean.ListBean> businessScopes) {

    }

    @Override
    public void closeBusinessScope(BusinessScopeEvent businessScopeEvent) {

    }
}
