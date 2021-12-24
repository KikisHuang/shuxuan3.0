package com.gxdingo.sg.fragment.client;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allen.library.SuperTextView;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.ArticleListActivity;
import com.gxdingo.sg.activity.ChangeBindingPhoneActivity;
import com.gxdingo.sg.activity.ClientAccountRecordActivity;
import com.gxdingo.sg.activity.ClientAccountSecurityActivity;
import com.gxdingo.sg.activity.ClientAddressListActivity;
import com.gxdingo.sg.activity.ClientCashActivity;
import com.gxdingo.sg.activity.ClientCouponDetailsActivity;
import com.gxdingo.sg.activity.ClientFillInvitationCodeActivity;
import com.gxdingo.sg.activity.ClientPersonalDataActivity;
import com.gxdingo.sg.activity.WebActivity;
import com.gxdingo.sg.adapter.ClientCouponAdapter;
import com.gxdingo.sg.bean.ClientCouponBean;
import com.gxdingo.sg.bean.ClientMineBean;
import com.gxdingo.sg.biz.ClientMineContract;
import com.gxdingo.sg.biz.MyConfirmListener;
import com.gxdingo.sg.dialog.SgConfirm2ButtonPopupView;
import com.gxdingo.sg.http.ClientApi;
import com.gxdingo.sg.presenter.ClientMinePresenter;
import com.gxdingo.sg.utils.ClientLocalConstant;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.StatusBarUtils;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.components.SimpleImmersionOwner;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.kikis.commnlibrary.fragment.BaseMvpFragment;
import com.lxj.xpopup.XPopup;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.commonsdk.debug.E;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.listener.OnBannerListener;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.blankj.utilcode.util.StringUtils.isEmpty;
import static com.gxdingo.sg.http.ClientApi.CLIENT_PRIVACY_AGREEMENT_KEY;
import static com.gxdingo.sg.http.ClientApi.WEB_URL;
import static com.kikis.commnlibrary.utils.IntentUtils.ShareAnimaStartPages;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientMineFragment extends BaseMvpFragment<ClientMineContract.ClientMinePresenter> implements ClientMineContract.ClientMineListener, OnItemChildClickListener {


    @BindView(R.id.avatar_cimg)
    public CircleImageView avatar_cimg;

    @BindView(R.id.username_stv)
    public TextView username_stv;

    @BindView(R.id.balance_tv)
    public TextView balance_tv;

    @BindView(R.id.internal_tv)
    public TextView internal_tv;

    @BindView(R.id.fill_invitation_code_stv)
    public SuperTextView fill_invitation_code_stv;

    @BindView(R.id.mine_banner)
    public Banner mine_banner;

    @BindView(R.id.cash_coupon_rv)
    public RecyclerView cash_coupon_rv;

    private ClientCouponAdapter mAdapter;

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
        mAdapter = new ClientCouponAdapter();
        cash_coupon_rv.setAdapter(mAdapter);
        cash_coupon_rv.setLayoutManager(new LinearLayoutManager(reference.get()));
        mAdapter.setOnItemChildClickListener(this);
    }

    @Override
    protected void initData() {

//        mine_banner.setOnBannerListener(new OnBannerListener() {
//            @Override
//            public void OnBannerClick(Object data, int position) {
//                ToastUtils.showLong(position);
//            }
//        });
    }

    @Override
    protected void lazyInit() {
        super.lazyInit();
        if (UserInfoUtils.getInstance().isLogin()){
            getP().getUserInfo();
            checkShowFillCode();
        }

    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type == LocalConstant.CLIENT_LOGIN_SUCCEED
                ||type == LocalConstant.CLIENT_REFRESH_USER_HOME
                ||type ==LocalConstant.CASH_SUCCESSS){
            getP().getUserInfo();
            checkShowFillCode();
        }

        else if (type == ClientLocalConstant.MODIFY_PERSONAL_SUCCESS){
            Glide.with(getContext()).load(UserInfoUtils.getInstance().getUserAvatar()).into(avatar_cimg);
            username_stv.setText(UserInfoUtils.getInstance().getUserNickName());
        }else if (type == ClientLocalConstant.FILL_SUCCESS){
            fill_invitation_code_stv.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
        if (object instanceof ReceiveIMMessageBean){
            ReceiveIMMessageBean messageBean = (ReceiveIMMessageBean) object;
            if (messageBean.getType()==21){
                getP().getUserInfo();
            }
        }
    }

    @OnClick({R.id.avatar_cimg,R.id.username_stv,R.id.check_bill_tv,R.id.btn_cash,R.id.ll_address_manage,R.id.ll_account_security
            ,R.id.ll_contract_server,R.id.ll_about_us,R.id.fill_invitation_code_stv,R.id.private_protocol_stv,R.id.logout_stv})
    public void onClickViews(View v){
        switch (v.getId()){
            case R.id.avatar_cimg:
            case R.id.username_stv:
                ShareAnimaStartPages(getContext(),avatar_cimg,"userAvatar" ,ClientPersonalDataActivity.class,null);
                break;
            case R.id.check_bill_tv:
                goToPage(getContext(), ClientAccountRecordActivity.class,null);
                break;
            case R.id.btn_cash:
                goToPage(getContext(), ClientCashActivity.class,getIntentMap(new String[]{balance_tv.getText().toString()}));
                break;
            case R.id.ll_address_manage:
                goToPage(getContext(), ClientAddressListActivity.class,null);
                break;
            case R.id.ll_account_security:
                goToPage(getContext(), ClientAccountSecurityActivity.class,null);
                break;
            case R.id.ll_contract_server:
                String url = WEB_URL + ClientApi.SERVER_URL;
                goToPagePutSerializable(reference.get(), WebActivity.class, getIntentEntityMap(new Object[]{false, url}));
                break;
            case R.id.ll_about_us:
                goToPagePutSerializable(reference.get(), ArticleListActivity.class, getIntentEntityMap(new Object[]{0, "about our"}));
                break;
            case R.id.fill_invitation_code_stv:
                goToPage(getContext(), ClientFillInvitationCodeActivity.class,null);
                break;
            case R.id.private_protocol_stv:
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
        }
    }

    @Override
    public void changeAvatar(Object o) {

    }

    private void checkShowFillCode(){
        if (UserInfoUtils.getInstance().getUserInfo().getInviterId() !=0)
            fill_invitation_code_stv.setVisibility(View.GONE);
    }

    @Override
    public RxPermissions getPermissions() {
        return null;
    }

    @Override
    public void onMineDataResult(ClientMineBean mineBean) {
        Glide.with(getContext()).load(isEmpty(mineBean.getAvatar()) ? R.drawable.module_svg_client_default_avatar : mineBean.getAvatar()).into(avatar_cimg);
        if (!isEmpty(mineBean.getNickname()))
            username_stv.setText(mineBean.getNickname());
        if (!isEmpty(mineBean.getBalance()))
            balance_tv.setText(mineBean.getBalance());
        if (mineBean.getAdsList()!=null){
            mine_banner.setAdapter(new BannerImageAdapter<ClientMineBean.AdsListBean>(mineBean.getAdsList()) {
                @Override
                public void onBindView(BannerImageHolder holder, ClientMineBean.AdsListBean data, int position, int size) {
                    Glide.with(reference.get())
                            .load(data.getImage())
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(6)))
                            .into(holder.imageView);
                }
            });
            mine_banner.start();
        }
        if (mineBean.getCouponList()!=null)
            mAdapter.setList(mineBean.getCouponList());

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mine_banner!=null)
            mine_banner.stop();
    }

    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        ClientCouponBean item =(ClientCouponBean) adapter.getItem(position);
        goToPagePutSerializable(reference.get(), ClientCouponDetailsActivity.class,getIntentEntityMap(new Object[]{item}));
    }
}
