package com.gxdingo.sg.activity;

import android.view.View;

import com.allen.library.SuperTextView;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.DistanceBean;
import com.gxdingo.sg.bean.StoreDetailBean;
import com.gxdingo.sg.bean.StoreQRCodeBean;
import com.gxdingo.sg.biz.OnBusinessTimeListener;
import com.gxdingo.sg.biz.OnContentListener;
import com.gxdingo.sg.biz.StoreSettingsContract;
import com.gxdingo.sg.dialog.BusinessTimePopupView;
import com.gxdingo.sg.dialog.DeliverScopePopupView;
import com.gxdingo.sg.dialog.EditMobilePopupView;
import com.gxdingo.sg.presenter.StoreSettingsPresenter;
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
 * @date: 2021/11/10
 * @page:
 */
public class StoreSettingActivity extends BaseMvpActivity<StoreSettingsContract.StoreSettingsPresenter> implements StoreSettingsContract.StoreSettingsListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.change_avatar_stv)
    public SuperTextView change_avatar_stv;

    @BindView(R.id.change_nickname_stv)
    public SuperTextView change_nickname_stv;

    @BindView(R.id.change_address_stv)
    public SuperTextView change_address_stv;

    @BindView(R.id.change_mobile_stv)
    public SuperTextView change_mobile_stv;

    @BindView(R.id.business_time_stv)
    public SuperTextView business_time_stv;

    @BindView(R.id.business_scope_stv)
    public SuperTextView business_scope_stv;

    private BasePopupView mPhotoPopupView;

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
        title_layout.setTitleText("店铺设置");
    }

    @Override
    protected void initData() {
        getP().getStoreInfo();
    }

    @OnClick({R.id.change_avatar_stv,R.id.change_nickname_stv,
            R.id.change_mobile_stv,R.id.business_time_stv,R.id.business_scope_stv})
    public void OnClickViews(View v){
        switch (v.getId()){
            case R.id.change_avatar_stv:
                if (mPhotoPopupView == null) {
                    mPhotoPopupView = new XPopup.Builder(reference.get())
                            .isDarkTheme(false)
                            .asCustom(new BaseActionSheetPopupView(reference.get()).addSheetItem(gets(R.string.photo_album), gets(R.string.photo_graph)).setItemClickListener((itemv, pos) -> {
                                getP().photoItemClick(pos);
                            })).show();
                } else
                    mPhotoPopupView.show();
                break;
            case R.id.change_nickname_stv:
                goToPage(this,StoreUpdateNameActivity.class,getIntentMap(new String[]{change_nickname_stv.getLeftTextView().getText().toString()}));
                break;

            case R.id.change_mobile_stv:
                new XPopup.Builder(reference.get())
                        .isDarkTheme(false)
                        .asCustom(new EditMobilePopupView(reference.get(), new OnContentListener() {
                            @Override
                            public void onConfirm(BasePopupView popupView, String content) {
                                if (isEmpty(content)){
                                    return;
                                }
                                getP().changMobile(content);
                                popupView.dismiss();
                            }
                        }))
                        .show();
                break;
            case R.id.business_time_stv:
               new XPopup.Builder(reference.get())
                        .isDestroyOnDismiss(true)
                        .isDarkTheme(false)
                        .asCustom(new BusinessTimePopupView(reference.get(), new OnBusinessTimeListener() {
                            @Override
                            public void onSelected(BasePopupView popupView, int startHour, int startMinute, int endHour, int endMinute) {
                                String startTime = (startHour < 10 ? "0" : "")+startHour+":"+(startMinute < 10 ? "0" : "")+startMinute;
                                String endTime = (endHour < 10 ? "0" : "")+endHour+":"+(endMinute < 10 ? "0" : "")+endMinute;
                                getP().businessTime(startTime,endTime);
                                business_time_stv.setRightString(startTime+" - "+endTime);
                                popupView.dismiss();
                            }
                        }).show());
                break;
            case R.id.business_scope_stv:
                getP().getDistanceList();
                break;
        }
    }

    @Override
    public void onSucceed(int type) {
        super.onSucceed(type);
        getP().getStoreInfo();
    }

    @Override
    public void onInfoResult(StoreDetailBean storeDetailBean) {
        Glide.with(this).
                load(StringUtils.isEmpty(storeDetailBean.getAvatar()) ? R.drawable.module_svg_client_default_avatar : storeDetailBean.getAvatar())
                .apply(RequestOptions.circleCropTransform())
                .into(change_avatar_stv.getLeftIconIV());
        change_nickname_stv.setLeftString(storeDetailBean.getName());
        change_address_stv.setRightString(storeDetailBean.getAddress());
        change_mobile_stv.setRightString(storeDetailBean.getContactNumber());
        business_time_stv.setRightString(dealDateFormat(storeDetailBean.getOpenTime(),"HH:mm")+"-"+dealDateFormat(storeDetailBean.getCloseTime(),"HH:mm"));
        business_scope_stv.setRightString(storeDetailBean.getMaxDistance());
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
                    business_scope_stv.setRightString(distanceBeans.get((Integer) pos).getName());
                }, distanceBeans).show());
    }
}
