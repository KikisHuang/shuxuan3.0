package com.gxdingo.sg.fragment.client;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.ArticleListActivity;
import com.gxdingo.sg.activity.ClientAccountRecordActivity;
import com.gxdingo.sg.activity.ClientAccountSecurityActivity;
import com.gxdingo.sg.activity.ClientAddressListActivity;
import com.gxdingo.sg.activity.ClientCashActivity;
import com.gxdingo.sg.activity.CouponQrCodeScanActivity;
import com.gxdingo.sg.activity.CouponListActivity;
import com.gxdingo.sg.activity.ClientFillInvitationCodeActivity;
import com.gxdingo.sg.activity.CustomCaptureActivity;
import com.gxdingo.sg.activity.RankingActivity;
import com.gxdingo.sg.activity.StoreAuthInfoActivity;
import com.gxdingo.sg.activity.StoreQRCodeActivity;
import com.gxdingo.sg.activity.StoreSettingActivity;
import com.gxdingo.sg.activity.WebActivity;
import com.gxdingo.sg.adapter.MineActivityAdapter;
import com.gxdingo.sg.bean.ClientCouponBean;
import com.gxdingo.sg.bean.ClientMineBean;
import com.gxdingo.sg.bean.UserBean;
import com.gxdingo.sg.biz.ClientMineContract;
import com.gxdingo.sg.biz.MyConfirmListener;
import com.gxdingo.sg.dialog.ScanningHintPopupView;
import com.gxdingo.sg.dialog.SgConfirm2ButtonPopupView;
import com.gxdingo.sg.presenter.ClientMinePresenter;
import com.gxdingo.sg.utils.ClientLocalConstant;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.kikis.commnlibrary.dialog.BaseActionSheetPopupView;
import com.kikis.commnlibrary.fragment.BaseMvpFragment;
import com.kikis.commnlibrary.utils.GlideUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.blankj.utilcode.util.StringUtils.isEmpty;
import static com.blankj.utilcode.util.TimeUtils.getNowMills;
import static com.gxdingo.sg.http.Api.SERVER_URL;
import static com.gxdingo.sg.http.Api.WEB_URL;
import static com.gxdingo.sg.utils.SignatureUtils.getAcType;
import static com.gxdingo.sg.utils.SignatureUtils.numberDecode;
import static com.gxdingo.sg.utils.StoreLocalConstant.REQUEST_CODE_SCAN;
import static com.gxdingo.sg.utils.StoreLocalConstant.SOTRE_REVIEW_SUCCEED;
import static com.kikis.commnlibrary.utils.CommonUtils.URLRequest;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;
import static com.kikis.commnlibrary.utils.ScreenUtils.dp2px;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientMineFragment extends BaseMvpFragment<ClientMineContract.ClientMinePresenter> implements ClientMineContract.ClientMineListener, OnItemClickListener, OnItemChildClickListener {


    @BindView(R.id.avatar_cimg)
    public CircleImageView avatar_cimg;

    @BindView(R.id.authentication_img)
    public ImageView authentication_img;

    @BindView(R.id.name_right_arrow_img)
    public ImageView name_right_arrow_img;

    @BindView(R.id.scroll_view)
    public NestedScrollView scroll_view;

    @BindView(R.id.real_name_img)
    public ImageView real_name_img;

    @BindView(R.id.btn_qr_code)
    public ImageView btn_qr_code;

    @BindView(R.id.username_stv)
    public TextView username_stv;

    @BindView(R.id.secondary_tv)
    public TextView secondary_tv;

    @BindView(R.id.balance_tv)
    public TextView balance_tv;

    @BindView(R.id.internal_tv)
    public TextView internal_tv;


    @BindView(R.id.activity_recycler)
    public RecyclerView activity_recycler;

    @BindView(R.id.capital_panel_cl)
    public ConstraintLayout capital_panel_cl;

    @BindView(R.id.fill_invitation_code_cardview)
    public CardView fill_invitation_code_cardview;

    private MineActivityAdapter mAcAdapter;

    private String integralLink = "";

    private BasePopupView mPhotoPopupView;

    private String scanContent = "";

    @Override
    protected ClientMineContract.ClientMinePresenter createPresenter() {
        return new ClientMinePresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return true;
    }

    @Override
    protected int activityTitleLayout() {
        return 0;
    }

    @Override
    protected int initContentView() {
        return R.layout.module_fragment_cilent_mine;
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
    protected boolean refreshEnable() {
        return false;
    }

    @Override
    protected boolean loadmoreEnable() {
        return false;
    }

    @Override
    protected void init() {

        mAcAdapter = new MineActivityAdapter();
        activity_recycler.setAdapter(mAcAdapter);
        activity_recycler.setLayoutManager(new GridLayoutManager(reference.get(), 2));
//        activity_recycler.addItemDecoration(new GridRecyclerDecoration(1, getc(R.color.grayf6)));
        mAcAdapter.setOnItemClickListener(this);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void lazyInit() {
        super.lazyInit();
        if (UserInfoUtils.getInstance().isLogin()) {
            getP().getUserInfo();
            checkShowFillCode();
        }

    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type == LocalConstant.LOGIN_SUCCEED
                || type == LocalConstant.CLIENT_REFRESH_USER_HOME
                || type == LocalConstant.CASH_SUCCESSS || type == SOTRE_REVIEW_SUCCEED) {

            if (type == LocalConstant.LOGIN_SUCCEED) {
                scroll_view.post(() -> {
                    scroll_view.fling(0);
                    scroll_view.smoothScrollTo(0, 0);
                });
            }

            getP().getUserInfo();
            checkShowFillCode();
        } else if (type == ClientLocalConstant.MODIFY_PERSONAL_SUCCESS) {
            Glide.with(getContext()).load(UserInfoUtils.getInstance().getUserAvatar()).into(avatar_cimg);
//            username_stv.setText(UserInfoUtils.getInstance().getUserNickName());
        } else if (type == ClientLocalConstant.FILL_SUCCESS) {

            UserBean userBean = UserInfoUtils.getInstance().getUserInfo();
            userBean.setInviterId(1);
            UserInfoUtils.getInstance().saveUserInfo(userBean);

            fill_invitation_code_cardview.setVisibility(View.GONE);
            getP().getUserInfo();

            goToPage(reference.get(), CouponListActivity.class, null);
        }

    }

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
        if (object instanceof ReceiveIMMessageBean) {
            ReceiveIMMessageBean messageBean = (ReceiveIMMessageBean) object;
            if (messageBean.getType() == 21) {
                getP().getUserInfo();
            }
        }
    }

    @Override
    public void onSucceed(int type) {
        super.onSucceed(type);
        if (type == 1) {
            Intent intent = new Intent(getContext(), CustomCaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SCAN);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                //返回的文本内容
                scanContent = data.getStringExtra("success_result");

                if (!isEmpty(scanContent) && (scanContent.contains("activeCode=") || scanContent.contains("dateContent="))) {
                    //url中获取活动码
                    String activeCode = URLRequest(scanContent).get("activeCode");

                    int mType = getAcType(numberDecode(activeCode));

                    if (mType == 10 && scanContent.contains("activeCode=")) {
                        //客户端扫码，或在手机浏览器少吗后领取两张优惠券，商家余额增加两元收入活动
                        getP().scanCode(activeCode);
                    } else if (mType == 11 && scanContent.contains("dateContent=")) {
                        long date = Long.parseLong(URLRequest(scanContent).get("dateContent"));
                        if (getNowMills() - date <= 40000) {
                            //客户端点击【使用优惠券】商家端扫码核销
                            boolean showDialog = SPUtils.getInstance().getBoolean(LocalConstant.SCANNING_NO_REMIND, false);

                            if (!showDialog) {
                                scanContent = activeCode;
                                getP().getNoRemindContent();
                            } else
                                getP().storeScanCode(activeCode);
                        } else
                            onMessage("二维码已失效，请重新扫码");

                    } else
                        onMessage("无法识别的二维码类型");
                } else if (getAcType(numberDecode(scanContent)) == 11) {
                    //客户端点击【使用优惠券】商家端扫码核销
                    boolean showDialog = SPUtils.getInstance().getBoolean(LocalConstant.SCANNING_NO_REMIND, false);

                    if (!showDialog)
                        getP().getNoRemindContent();
                    else
                        getP().storeScanCode(scanContent);

                } else
                    onMessage("无法识别的二维码类型");


                //返回的BitMap图像
//                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
            }
        }
    }

    @OnClick({R.id.btn_qr_code, R.id.coupon_img, R.id.secondary_tv, R.id.auth_info_stv, R.id.my_balance_text, R.id.btn_scan, R.id.check_internal_stv, R.id.avatar_cimg, R.id.username_stv, R.id.btn_cash, R.id.address_manage_stv, R.id.account_security_stv
            , R.id.contract_server_stv, R.id.about_us_stv, R.id.fill_invitation_code_stv, R.id.settle_protocol_stv, R.id.logout_stv})
    public void onClickViews(View v) {
        switch (v.getId()) {
            case R.id.btn_qr_code:
                goToPage(getContext(), StoreQRCodeActivity.class, null);
                break;
            case R.id.coupon_img:
                goToPage(reference.get(), CouponListActivity.class, null);
                break;
            case R.id.auth_info_stv:
                goToPage(getContext(), StoreAuthInfoActivity.class, null);
                break;
            case R.id.btn_scan:
                getP().scan(getRxPermissions());
                break;
            case R.id.avatar_cimg:
                if (mPhotoPopupView == null) {
                    mPhotoPopupView = new XPopup.Builder(reference.get())
                            .isDarkTheme(false)
                            .asCustom(new BaseActionSheetPopupView(reference.get()).addSheetItem(gets(R.string.photo_album), gets(R.string.photo_graph)).setItemClickListener((itemv, pos) -> {
                                getP().photoItemClick(pos);
                            })).show();
                } else
                    mPhotoPopupView.show();

                break;
            case R.id.secondary_tv:
            case R.id.username_stv:
                goToPage(getContext(), StoreSettingActivity.class, null);
                break;
            case R.id.my_balance_text:
                goToPage(getContext(), ClientAccountRecordActivity.class, null);
                break;
            case R.id.btn_cash:
                goToPage(getContext(), ClientCashActivity.class, getIntentMap(new String[]{balance_tv.getText().toString()}));
                break;
            case R.id.address_manage_stv:
                goToPage(getContext(), ClientAddressListActivity.class, null);
                break;
            case R.id.account_security_stv:
                goToPage(getContext(), ClientAccountSecurityActivity.class, null);
                break;
            case R.id.contract_server_stv:
                if (checkClickInterval(R.id.contract_server_stv)) {
                    LocalConstant.IS_CONTEACT_SERVER = true;
                    String url = WEB_URL + SERVER_URL;
                    goToPagePutSerializable(reference.get(), WebActivity.class, getIntentEntityMap(new Object[]{false, url}));
                }

                break;
            case R.id.about_us_stv:
                goToPagePutSerializable(reference.get(), ArticleListActivity.class, getIntentEntityMap(new Object[]{0, "about our"}));
                break;
            case R.id.fill_invitation_code_stv:
                goToPage(getContext(), ClientFillInvitationCodeActivity.class, null);
                break;
            case R.id.settle_protocol_stv:
                goToPagePutSerializable(reference.get(), ArticleListActivity.class, getIntentEntityMap(new Object[]{0, "shuxuanyonghuxieyi"}));
//                goToPagePutSerializable(reference.get(), WebActivity.class, getIntentEntityMap(new Object[]{true,0, CLIENT_PRIVACY_AGREEMENT_KEY}));
                break;
            case R.id.logout_stv:
                new XPopup.Builder(reference.get())
                        .isDarkTheme(false)
                        .asCustom(new SgConfirm2ButtonPopupView(reference.get(), "确定退出登录？", new MyConfirmListener() {
                            @Override
                            public void onConfirm() {
                                getP().logout();
                            }
                        })).show();
                break;
            case R.id.check_internal_stv:

                if (!isEmpty(integralLink))
                    goToPagePutSerializable(reference.get(), WebActivity.class, getIntentEntityMap(new Object[]{false, integralLink}));

                break;
        }
    }

    @Override
    public void changeAvatar(Object o) {
        String a = (String) o;
        Glide.with(reference.get()).load(TextUtils.isEmpty(a) ? R.drawable.module_svg_client_default_avatar : a).apply(GlideUtils.getInstance().getCircleCrop()).into(avatar_cimg);
    }

    private void checkShowFillCode() {
        if (UserInfoUtils.getInstance().getUserInfo().getInviterId() != 0)
            fill_invitation_code_cardview.setVisibility(View.GONE);
    }

    @Override
    public RxPermissions getPermissions() {
        return null;
    }

    @Override
    public void onMineDataResult(ClientMineBean mineBean) {

        ConstraintLayout.LayoutParams clp = (ConstraintLayout.LayoutParams) capital_panel_cl.getLayoutParams();
        clp.topMargin = dp2px(mineBean.getReleaseUserType() == 1 ? 35 : 15);
        capital_panel_cl.setLayoutParams(clp);

        if (mineBean.getAdsList() != null)
            mAcAdapter.setList(mineBean.getAdsList());

        btn_qr_code.setVisibility(mineBean.getReleaseUserType() == 1 ? View.GONE : View.VISIBLE);

        //发布用户类型。0=商家；1=用户
        if (mineBean.getReleaseUserType() == 1) {

            name_right_arrow_img.setVisibility(View.GONE);
            Drawable drawable = getResources().getDrawable(
                    R.drawable.module_svg_right_arrow_8934);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            secondary_tv.setCompoundDrawables(null, null, drawable, null);
            secondary_tv.setText("信息管理");
        } else {
            name_right_arrow_img.setVisibility(View.VISIBLE);

            secondary_tv.setCompoundDrawables(null, null, null, null);
            StringBuffer sb = new StringBuffer();

            if (mineBean.getCategoryList() != null) {
                for (int i = 0; i < mineBean.getCategoryList().size(); i++) {
                    if (i == 0)
                        sb.append(mineBean.getCategoryList().get(i).getName());
                    else
                        sb.append(" | " + mineBean.getCategoryList().get(i).getName());
                }
                sb.append(" " + mineBean.getOpenTime());
                sb.append("-" + mineBean.getCloseTime());
                secondary_tv.setText(sb);
            }
        }

        if (mineBean.getIconList() != null && mineBean.getIconList().size() > 0) {

            authentication_img.setVisibility(View.VISIBLE);

            Glide.with(reference.get()).load(mineBean.getIconList().get(0)).into(authentication_img);

            if (mineBean.getIconList().size() >= 2) {
                real_name_img.setVisibility(View.VISIBLE);
                Glide.with(reference.get()).load(mineBean.getIconList().get(1)).into(real_name_img);
            }

        } else {
            authentication_img.setVisibility(View.GONE);
            real_name_img.setVisibility(View.GONE);
        }


        Glide.with(getContext()).load(isEmpty(mineBean.getAvatar()) ? R.drawable.module_svg_client_default_avatar : mineBean.getAvatar()).into(avatar_cimg);
        if (!isEmpty(mineBean.getNickname()))
            username_stv.setText(mineBean.getNickname());

        if (!isEmpty(mineBean.getBalance()))
            balance_tv.setText(mineBean.getBalance());


        integralLink = mineBean.getIntegralLink();

        if (!isEmpty(mineBean.getIntegral()))
            internal_tv.setText(mineBean.getIntegral());

    }

    @Override
    public void onRemindResult(String data) {
        ScanningHintPopupView dialog = new ScanningHintPopupView(reference.get(), data, flag -> {
            SPUtils.getInstance().put(LocalConstant.SCANNING_NO_REMIND, (Boolean) flag);
            getP().storeScanCode(scanContent);
        });
        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true)
                .isDarkTheme(false)
                .asCustom(dialog).show();
    }

    @Override
    public void onStatusResult(UserBean userBean) {

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        ClientCouponBean item = (ClientCouponBean) adapter.getItem(position);
        goToPagePutSerializable(reference.get(), CouponQrCodeScanActivity.class, getIntentEntityMap(new Object[]{item, 0}));
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {


        if (mAcAdapter.getData().get(position).getType() == 2 || mAcAdapter.getData().get(position).getType() == 3)
            //类型 0=无跳转 1=APP跳转 2=H5跳转  3=H5跳转（左上角增加返回按钮）
            goToPagePutSerializable(reference.get(), WebActivity.class, getIntentEntityMap(new Object[]{false, mAcAdapter.getData().get(position).getPage(), "", mAcAdapter.getData().get(position).getType() == 3 ? true : false}));
        else if (mAcAdapter.getData().get(position).getType() == 1)
            //排行榜类型
            goToPage(reference.get(), RankingActivity.class, null);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPhotoPopupView != null) {
            mPhotoPopupView.destroy();
            mPhotoPopupView = null;
        }
    }
}
