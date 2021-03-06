package com.gxdingo.sg.fragment.store;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.ArticleListActivity;
import com.gxdingo.sg.activity.ClientAccountSecurityActivity;
import com.gxdingo.sg.activity.ClientAddressListActivity;
import com.gxdingo.sg.activity.StoreAuthInfoActivity;
import com.gxdingo.sg.activity.StoreQRCodeActivity;
import com.gxdingo.sg.activity.StoreSettingActivity;
import com.gxdingo.sg.activity.WebActivity;
import com.gxdingo.sg.bean.StoreMineBean;
import com.gxdingo.sg.bean.UserBean;
import com.gxdingo.sg.biz.MyConfirmListener;
import com.gxdingo.sg.biz.StoreMyContract;
import com.gxdingo.sg.dialog.SgConfirm2ButtonPopupView;
import com.gxdingo.sg.presenter.StoreMyPresenter;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.fragment.BaseMvpFragment;
import com.lxj.xpopup.XPopup;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.blankj.utilcode.util.StringUtils.isEmpty;
import static com.gxdingo.sg.http.Api.SERVER_URL;
import static com.gxdingo.sg.http.Api.WEB_URL;
import static com.gxdingo.sg.utils.DateUtils.dealDateFormat;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * 商家端我的
 *
 * @author Kikis
 */
public class StoreMyFragment extends BaseMvpFragment<StoreMyContract.StoreMyPresenter> implements StoreMyContract.StoreMyListener {

    @BindView(R.id.store_avatar_iv)
    public CircleImageView store_avatar_iv;

    @BindView(R.id.store_name_tv)
    public TextView store_name_tv;

    @BindView(R.id.type_and_open_time_tv)
    public TextView type_and_open_time_tv;

    @BindView(R.id.store_mine_banner)
    public Banner store_mine_banner;


    @Override
    protected StoreMyContract.StoreMyPresenter createPresenter() {
        return new StoreMyPresenter();
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
        return R.layout.module_fragment_store_my;
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

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void lazyInit() {
        super.lazyInit();
        if (UserInfoUtils.getInstance().isLogin())
            getP().getInfo();
    }

    @OnClick({R.id.address_manage_stv, R.id.store_avatar_iv, R.id.btn_setting, R.id.btn_qr_code, R.id.account_security_stv, R.id.auth_info_stv
            , R.id.settle_protocol_stv, R.id.contract_server_stv, R.id.about_us_stv, R.id.logout_stv})
    public void OnClickViews(View v) {
        switch (v.getId()) {
            case R.id.address_manage_stv:
                goToPage(getContext(), ClientAddressListActivity.class,null);
                break;
            case R.id.store_avatar_iv:
            case R.id.btn_setting:
                goToPage(getContext(), StoreSettingActivity.class, null);
                break;
            case R.id.btn_qr_code:
                goToPage(getContext(), StoreQRCodeActivity.class, null);
                break;
            case R.id.account_security_stv:
                goToPage(getContext(), ClientAccountSecurityActivity.class, null);
                break;
            case R.id.auth_info_stv:
                goToPage(getContext(), StoreAuthInfoActivity.class, null);
                break;
            case R.id.settle_protocol_stv:
                goToPagePutSerializable(reference.get(), ArticleListActivity.class, getIntentEntityMap(new Object[]{0, "shuxuanshangjiaxieyi"}));
                break;
            case R.id.contract_server_stv:
                String url = WEB_URL + SERVER_URL;
                goToPagePutSerializable(reference.get(), WebActivity.class, getIntentEntityMap(new Object[]{false, url}));
                break;
            case R.id.about_us_stv:
                goToPagePutSerializable(reference.get(), ArticleListActivity.class, getIntentEntityMap(new Object[]{0, "about us"}));
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
        }
    }

    @Override
    public void onDataResult(StoreMineBean mineBean) {
        Glide.with(getContext()).load(isEmpty(mineBean.getAvatar()) ? R.drawable.module_svg_client_default_avatar : mineBean.getAvatar()).into(store_avatar_iv);
        store_name_tv.setText(mineBean.getName());

        if (UserInfoUtils.getInstance().getUserInfo() != null && UserInfoUtils.getInstance().getUserInfo().getStore() != null) {
            UserBean userBean = UserInfoUtils.getInstance().getUserInfo();
            if (!isEmpty(mineBean.getCloseTime()))
                userBean.getStore().setCloseTime(mineBean.getCloseTime());

            if (!isEmpty(mineBean.getOpenTime()))
                userBean.getStore().setOpenTime(mineBean.getOpenTime());

            if (!isEmpty(mineBean.getAvatar()))
                userBean.getStore().setAvatar(mineBean.getAvatar());

            if (!isEmpty(mineBean.getName()))
                userBean.getStore().setName(mineBean.getName());

            UserInfoUtils.getInstance().saveUserInfo(userBean);
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (mineBean.getCategoryList() != null && mineBean.getCategoryList().size() > 0) {
            for (int i = 0; i < mineBean.getCategoryList().size(); i++) {
                stringBuilder.append(mineBean.getCategoryList().get(i).getName());
                if (i != mineBean.getCategoryList().size() - 1)
                    stringBuilder.append("、");
            }
        }
        type_and_open_time_tv.setText(stringBuilder.toString() + " | " + dealDateFormat(mineBean.getOpenTime(), "HH:mm") + " - " + dealDateFormat(mineBean.getCloseTime(), "HH:mm"));
        if (mineBean.getAdsList() != null && mineBean.getAdsList().size() > 0) {
            store_mine_banner.setAdapter(new BannerImageAdapter<StoreMineBean.AdsListBean>(mineBean.getAdsList()) {
                @Override
                public void onBindView(BannerImageHolder holder, StoreMineBean.AdsListBean data, int position, int size) {
                    Glide.with(getContext())
                            .load(data.getImage())
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(6)))
                            .into(holder.imageView);
                }
            });
            store_mine_banner.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (store_mine_banner != null)
            store_mine_banner.stop();
    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);

    }
}
