package com.gxdingo.sg.fragment.client;

import android.view.View;

import com.allen.library.SuperTextView;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.ChangeBindingPhoneActivity;
import com.gxdingo.sg.activity.ClientAccountRecordActivity;
import com.gxdingo.sg.activity.ClientAccountSecurityActivity;
import com.gxdingo.sg.activity.ClientAddressListActivity;
import com.gxdingo.sg.activity.ClientCashActivity;
import com.gxdingo.sg.activity.ClientPersonalDataActivity;
import com.gxdingo.sg.bean.ClientMineBean;
import com.gxdingo.sg.biz.ClientMineContract;
import com.gxdingo.sg.presenter.ClientMinePresenter;
import com.gxdingo.sg.utils.StatusBarUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.components.SimpleImmersionOwner;
import com.kikis.commnlibrary.fragment.BaseMvpFragment;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.listener.OnBannerListener;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.blankj.utilcode.util.StringUtils.isEmpty;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientMineFragment extends BaseMvpFragment<ClientMineContract.ClientMinePresenter> implements ClientMineContract.ClientMineListener {


    @BindView(R.id.avatar_cimg)
    public CircleImageView avatar_cimg;

    @BindView(R.id.username_stv)
    public SuperTextView username_stv;

    @BindView(R.id.mine_banner)
    public Banner mine_banner;

    @Override
    protected ClientMineContract.ClientMinePresenter createPresenter() {
        return new ClientMinePresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return false;
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

    }

    @Override
    protected void initData() {
        getP().getUserInfo();
        mine_banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(Object data, int position) {
                ToastUtils.showLong(position);
            }
        });
    }

    @OnClick({R.id.avatar_cimg,R.id.username_stv,R.id.check_bill_tv,R.id.btn_cash,R.id.ll_address_manage,R.id.ll_account_security
            ,R.id.ll_contract_server,R.id.ll_about_us,R.id.fill_invitation_code_stv,R.id.private_protocol_stv,R.id.logout_stv})
    public void onClickViews(View v){
        switch (v.getId()){
            case R.id.avatar_cimg:
            case R.id.username_stv:
                goToPage(getContext(), ClientPersonalDataActivity.class,null);
                break;
            case R.id.check_bill_tv:
                goToPage(getContext(), ClientAccountRecordActivity.class,null);
                break;
            case R.id.btn_cash:
                goToPage(getContext(), ClientCashActivity.class,null);
                break;
            case R.id.ll_address_manage:
                goToPage(getContext(), ClientAddressListActivity.class,null);
                break;
            case R.id.ll_account_security:
                goToPage(getContext(), ClientAccountSecurityActivity.class,null);
                break;
            case R.id.ll_contract_server:
                break;
            case R.id.ll_about_us:
                break;
            case R.id.fill_invitation_code_stv:
                break;
            case R.id.private_protocol_stv:
                break;
            case R.id.logout_stv:
                break;
        }
    }

    @Override
    public void changeAvatar(Object o) {

    }

    @Override
    public RxPermissions getPermissions() {
        return null;
    }

    @Override
    public void onMineDataResult(ClientMineBean mineBean) {
        Glide.with(getContext()).load(isEmpty(mineBean.getAvatar()) ? R.drawable.module_svg_client_default_avatar : mineBean.getAvatar()).into(avatar_cimg);
        if (!isEmpty(mineBean.getNickname()))
            username_stv.setLeftString(mineBean.getNickname());
        if (mineBean.getAdsList()!=null){
            mine_banner.setAdapter(new BannerImageAdapter<ClientMineBean.AdsListBean>(mineBean.getAdsList()) {
                @Override
                public void onBindView(BannerImageHolder holder, ClientMineBean.AdsListBean data, int position, int size) {
                    Glide.with(getContext())
                            .load(data.getImage())
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(6)))
                            .into(holder.imageView);
                }
            });
        }

    }
}
