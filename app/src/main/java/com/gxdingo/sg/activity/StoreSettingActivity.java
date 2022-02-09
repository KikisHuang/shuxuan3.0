package com.gxdingo.sg.activity;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.allen.library.SuperTextView;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.DistanceBean;
import com.gxdingo.sg.bean.StoreDetail;
import com.gxdingo.sg.bean.StoreDetailBean;
import com.gxdingo.sg.bean.StoreQRCodeBean;
import com.gxdingo.sg.biz.OnBusinessTimeListener;
import com.gxdingo.sg.biz.OnContentListener;
import com.gxdingo.sg.biz.StoreSettingsContract;
import com.gxdingo.sg.dialog.BusinessTimePopupView;
import com.gxdingo.sg.dialog.DeliverScopePopupView;
import com.gxdingo.sg.dialog.EditMobilePopupView;
import com.gxdingo.sg.dialog.StoreSelectBusinessStatusPopupView;
import com.gxdingo.sg.presenter.StoreSettingsPresenter;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.dialog.BaseActionSheetPopupView;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;
import static com.gxdingo.sg.utils.DateUtils.dealDateFormat;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;

/**
 * @author: Weaving
 * @date: 2022/1/22
 * @page:
 */
public class StoreSettingActivity extends BaseMvpActivity<StoreSettingsContract.StoreSettingsPresenter> implements StoreSettingsContract.StoreSettingsListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.change_nickname_stv)
    public SuperTextView change_nickname_stv;

    @BindView(R.id.change_address_stv)
    public SuperTextView change_address_stv;

    @BindView(R.id.change_mobile_stv)
    public SuperTextView change_mobile_stv;

    @BindView(R.id.business_time_stv)
    public SuperTextView business_time_stv;

    @BindView(R.id.distribution_scope_stv)
    public SuperTextView distribution_scope_stv;

    @BindView(R.id.shop_type_stv)
    public SuperTextView shop_type_stv;

    @BindView(R.id.store_ll)
    public LinearLayout store_ll;

    @BindView(R.id.operating_state_stv)
    public SuperTextView operating_state_stv;

    private String id = "";

    private int ReleaseUserType = -1;

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
        return R.layout.module_activity_store_setting;
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
        title_layout.setTitleText("信息管理");
    }

    @Override
    protected void initData() {
        id = UserInfoUtils.getInstance().getUserInfo().getIdentifier();
        getP().getStoreInfo(id);
    }

    @OnClick({R.id.operating_state_stv, R.id.change_nickname_stv,
            R.id.change_mobile_stv, R.id.business_time_stv, R.id.distribution_scope_stv})
    public void OnClickViews(View v) {
        switch (v.getId()) {

            case R.id.operating_state_stv:
                new XPopup.Builder(reference.get())
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .isDarkTheme(false)
                        .asCustom(new StoreSelectBusinessStatusPopupView(reference.get(), new StoreSelectBusinessStatusPopupView.OnBusinessStatusListener() {
                            @Override
                            public void onStatus(int code, String name) {

                                getP().updateBusinessStatus(code);
                            }
                        }).show());
                break;
            case R.id.change_nickname_stv:
                if (ReleaseUserType == 0)
                    goToPage(this, StoreUpdateNameActivity.class, getIntentMap(new String[]{change_nickname_stv.getLeftTextView().getText().toString()}));
                else if (ReleaseUserType == 1)
                    showChangeDialog("修改昵称");

                break;

            case R.id.change_mobile_stv:
                showChangeDialog("");
                break;
            case R.id.business_time_stv:
                new XPopup.Builder(reference.get())
                        .isDestroyOnDismiss(true)
                        .isDarkTheme(false)
                        .asCustom(new BusinessTimePopupView(reference.get(), new OnBusinessTimeListener() {
                            @Override
                            public void onSelected(BasePopupView popupView, int startHour, int startMinute, int endHour, int endMinute) {
                                String startTime = (startHour < 10 ? "0" : "") + startHour + ":" + (startMinute < 10 ? "0" : "") + startMinute;
                                String endTime = (endHour < 10 ? "0" : "") + endHour + ":" + (endMinute < 10 ? "0" : "") + endMinute;
                                getP().businessTime(startTime, endTime);
//                                business_time_stv.setRightString(startTime+" - "+endTime);
                                popupView.dismiss();
                            }
                        }).show());
                break;
            case R.id.distribution_scope_stv:
                getP().getDistanceList();
                break;
        }
    }

    private void showChangeDialog(String title) {
        new XPopup.Builder(reference.get())
                .isDarkTheme(false)
                .asCustom(new EditMobilePopupView(reference.get(), title, (popupView, content) -> {
                    if (isEmpty(content)) {
                        return;
                    }
                    if (isEmpty(title))
                        getP().changMobile(content);
                    else
                        getP().updateStoreName(content);

                    popupView.dismiss();
                }))
                .show();
    }

    @Override
    public void onSucceed(int type) {
        super.onSucceed(type);
        getP().getStoreInfo(id);
    }

    @Override
    public void onInfoResult(StoreDetail storeDetailBean) {

        ReleaseUserType = storeDetailBean.getReleaseUserType();

        store_ll.setVisibility(storeDetailBean.getReleaseUserType() == 0 ? View.VISIBLE : View.GONE);

        // 用户类型0=商家 1=用户
        if (storeDetailBean.getReleaseUserType() == 0) {

            StringBuffer stringBuffer = new StringBuffer();

            for (int i = 0; i < storeDetailBean.getClassNameList().size(); i++) {
                stringBuffer.append(storeDetailBean.getClassNameList().get(i));
                if (i != storeDetailBean.getClassNameList().size() - 1)
                    stringBuffer.append(" | ");
            }
            shop_type_stv.setRightString(stringBuffer.toString());

            //经营状态  0=未营业  1=营业中
            operating_state_stv.getRightTextView().setText(storeDetailBean.getBusinessStatus() == 1 ? "营业中" : "未营业");

            change_address_stv.setRightString(storeDetailBean.getAddress());
            change_mobile_stv.setRightString(storeDetailBean.getContactNumber());
            business_time_stv.setRightString(storeDetailBean.getOpenTime() + "-" + storeDetailBean.getCloseTime());
            distribution_scope_stv.setRightString(storeDetailBean.getDistance());
        }

        if (!isEmpty(storeDetailBean.getName()))
            change_nickname_stv.setLeftString(storeDetailBean.getName());

    }

    @Override
    public void onQRResult(StoreQRCodeBean qrCodeBean) {

    }

    @Override
    public void onDistanceResult(List<DistanceBean> distanceBeans) {

        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .isDarkTheme(false)
                .asCustom(new DeliverScopePopupView(reference.get(), pos -> {
                    Integer s = distanceBeans.get((Integer) pos).getMeter();
                    getP().deliveryScope(s.toString());
                    distribution_scope_stv.setRightString(distanceBeans.get((Integer) pos).getName());
                }, distanceBeans).show());
    }

    @Override
    public void changeBusinessStatus(int status) {

        //经营状态  0=未营业  1=营业中
        operating_state_stv.getRightTextView().setText(status == 1 ? "营业中" : "未营业");
    }
}
