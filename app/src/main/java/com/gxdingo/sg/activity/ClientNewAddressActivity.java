package com.gxdingo.sg.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ItemDistanceBean;
import com.gxdingo.sg.bean.SelectAddressEvent;
import com.gxdingo.sg.biz.MyConfirmListener;
import com.gxdingo.sg.dialog.SgConfirm2ButtonPopupView;
import com.gxdingo.sg.dialog.SgConfirmPopupView;
import com.kikis.commnlibrary.bean.AddressBean;
import com.gxdingo.sg.biz.AddressContract;
import com.gxdingo.sg.presenter.AddressPresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.CommonUtils.getd;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;

/**
 * @author: kikis
 * @date: 2022/1/24
 * @page:
 */
public class ClientNewAddressActivity extends BaseMvpActivity<AddressContract.AddressPresenter> implements AddressContract.AddressListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.tv_receive_address)
    public TextView tv_receive_address;

    @BindView(R.id.et_house_number)
    public TextView et_house_number;

    @BindView(R.id.et_contacts)
    public TextView et_contacts;

    @BindView(R.id.et_mobile)
    public TextView et_mobile;

    @BindView(R.id.save_tv)
    public TextView save_tv;

    @BindView(R.id.del_tv)
    public TextView del_tv;

    private AddressBean addressBean;

    private PoiItem poiItem;

    //类型 1新增 2修改
    private boolean isAdd = false;

    private String regionPath = "";

    private LatLonPoint mPoint;

    @Override
    protected AddressContract.AddressPresenter createPresenter() {
        return new AddressPresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return true;
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
        return R.layout.module_activity_client_new_address;
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

        isAdd = getIntent().getBooleanExtra(Constant.SERIALIZABLE + 0, true);

        addressBean = (AddressBean) getIntent().getSerializableExtra(Constant.SERIALIZABLE + 1);

        if (!isAdd && addressBean == null) {
            onMessage(getString(R.string.not_get_address_info));
            finish();
        } else if (addressBean == null)
            addressBean = new AddressBean();

        if (!isAdd) {
            regionPath = addressBean.getRegionPath();
            mPoint = new LatLonPoint(addressBean.getLatitude(), addressBean.getLongitude());
        }
        title_layout.setTitleText(isAdd ? getString(R.string.new_address) : getString(R.string.edit_receiving_address));

        del_tv.setVisibility(isAdd ? View.GONE : View.VISIBLE);


        et_mobile.addTextChangedListener(Watcher);
        et_house_number.addTextChangedListener(Watcher);
        et_contacts.addTextChangedListener(Watcher);
    }

    @Override
    protected void initData() {
        if (!isAdd && addressBean != null)
            setAddressData(addressBean);

        getP().checkCompileInfo();
    }

    @OnClick({ R.id.del_tv, R.id.save_tv, R.id.title_back, R.id.cl_receive_address,})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;
        switch (v.getId()) {
            case R.id.del_tv:
                showDelDialog();
                break;
            case R.id.save_tv:
                getP().compileOrAdd(isAdd);
                break;
            case R.id.title_back:
                finish();
                break;
            case R.id.cl_receive_address:

                goToPage(this, SelectAddressActivity.class, null);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);

        //收货地址实体类回调
        if (object instanceof SelectAddressEvent) {

            SelectAddressEvent event = (SelectAddressEvent) object;

            //上传地图截屏
            getP().upLoadLocationImage(event.fliepath);

            poiItem = (PoiItem) event.poiItem;

            String a = poiItem.getProvinceCode();
            String b = poiItem.getAdCode();

            mPoint = poiItem.getLatLonPoint();
            //regionPath = a + "/" + b;
            regionPath = b;

            addressBean.setStreet(poiItem.getTitle());

            tv_receive_address.setText(poiItem.getTitle());
            getP().checkCompileInfo();
        }
    }

    TextWatcher Watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            getP().checkCompileInfo();
        }
    };

    @Override
    public void onDataResult(boolean refresh, List<AddressBean> addressBeans) {

    }

    @Override
    public void setAddressData(AddressBean bean) {
        if (!isEmpty(bean.getStreet()))
            tv_receive_address.setText(bean.getStreet());

        if (!isEmpty(bean.getDoorplate()))
            et_house_number.setText(bean.getDoorplate());

        if (!isEmpty(bean.getName()))
            et_contacts.setText(bean.getName());


        if (!isEmpty(bean.getMobile()))
            et_mobile.setText(bean.getMobile());

    }

    @Override
    public void saveBtnEnable(boolean en) {
        save_tv.setEnabled(en);
        save_tv.setTextColor(en ? getc(R.color.white) : getc(R.color.gray));
        save_tv.setBackground(en ? getd(R.drawable.module_bg_main_color_round6) : getd(R.drawable.module_shape_bg_gray_6r));
        save_tv.setSelected(en);
    }

    @Override
    public int getAddressId() {
        return addressBean != null ? addressBean.getId() : 0;
    }

    @Override
    public String getAddress() {
        return tv_receive_address.getText().toString();
    }

    @Override
    public String getAddressDetail() {
        if (!isEmpty(addressBean.getStreet()))
            return addressBean.getStreet();

        return "";
    }

    @Override
    public String getContact() {
        return et_contacts.getText().toString();
    }

    @Override
    public String getMobile() {
        return et_mobile.getText().toString();
    }

    @Override
    public String getRegionPath() {
        return regionPath;
    }


    @Override
    public LatLonPoint getPoint() {
        return mPoint;
    }

    @Override
    public void showDelDialog() {

        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .isDarkTheme(false)
                .asCustom(new SgConfirm2ButtonPopupView(reference.get(), gets(R.string.confirm_del_address), () -> getP().delAddress(addressBean.getId()))).show();
    }

    @Override
    public String getDoorplate() {
        return et_house_number.getText().toString();
    }

    @Override
    public void setCityName(String cityName) {

    }

    @Override
    public void searchResult(boolean refresh, List<PoiItem> poiItems, boolean isSearch) {

    }


    @Override
    public AMap getAMap() {
        return null;
    }

    @Override
    public void onDistanceResult(ItemDistanceBean bean) {

    }


    @Override
    public void onSucceed(int type) {
        super.onSucceed(type);
        finish();
    }
}
