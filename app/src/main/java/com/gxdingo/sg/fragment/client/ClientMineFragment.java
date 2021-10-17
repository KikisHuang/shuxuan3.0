package com.gxdingo.sg.fragment.client;

import android.view.View;

import com.allen.library.SuperTextView;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.ChangeBindingPhoneActivity;
import com.gxdingo.sg.activity.ClientAccountRecordActivity;
import com.gxdingo.sg.activity.ClientAccountSecurityActivity;
import com.gxdingo.sg.activity.ClientAddressListActivity;
import com.gxdingo.sg.activity.ClientCashActivity;
import com.gxdingo.sg.activity.ClientPersonalDataActivity;
import com.gxdingo.sg.biz.ClientMineContract;
import com.gxdingo.sg.presenter.ClientMinePresenter;
import com.kikis.commnlibrary.fragment.BaseMvpFragment;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

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
}
