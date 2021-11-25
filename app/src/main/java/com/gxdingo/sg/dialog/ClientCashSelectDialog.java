package com.gxdingo.sg.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;

import com.allen.library.SuperTextView;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.BankcardListActivity;
import com.gxdingo.sg.bean.BankcardBean;
import com.gxdingo.sg.bean.ClientCashInfoBean;
import com.gxdingo.sg.bean.ThirdPartyBean;
import com.gxdingo.sg.biz.OnAccountSelectListener;
import com.lxj.xpopup.core.BottomPopupView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.CommonUtils.getd;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * @author: Weaving
 * @date: 2021/10/19
 * @page:
 */
public class ClientCashSelectDialog extends BottomPopupView {

    @BindView(R.id.alipay_stv)
    public SuperTextView alipay_stv;

    @BindView(R.id.wechaty_stv)
    public SuperTextView wechaty_stv;

    @BindView(R.id.bankcard_stv)
    public SuperTextView bankcard_stv;

    private OnAccountSelectListener listener;

    private ClientCashInfoBean cashInfoBean;

    private int bankCardId;

    public ClientCashSelectDialog(@NonNull Context context,ClientCashInfoBean cashInfoBean,OnAccountSelectListener listener) {
        super(context);
        this.cashInfoBean = cashInfoBean;
        this.listener = listener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_client_cash_select_account;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        EventBus.getDefault().register(this);
        ButterKnife.bind(this, bottomPopupContainer);

        if (cashInfoBean.getIsShowAlipay()==0)
            alipay_stv.setVisibility(GONE);
        else {
            if (!isEmpty(cashInfoBean.getAlipay())){
                alipay_stv.getCenterTextView().setVisibility(GONE);
                alipay_stv.getRightTextView().setVisibility(GONE);
                alipay_stv.getRightIconIV().setVisibility(GONE);
            }
        }

        if (cashInfoBean.getIsShowWechat()==0)
            wechaty_stv.setVisibility(GONE);
        else {
            if (!isEmpty(cashInfoBean.getWechat())){
                wechaty_stv.getCenterTextView().setVisibility(GONE);
                wechaty_stv.getRightTextView().setVisibility(GONE);
                wechaty_stv.getRightIconIV().setVisibility(GONE);
            }
        }
        if (cashInfoBean.getIsShowBank()==0)
            bankcard_stv.setVisibility(GONE);
        else {
            if (cashInfoBean.getBankList()!=null&&cashInfoBean.getBankList().size()>0){
                BankcardBean bankcardBean = cashInfoBean.getBankList().get(0);
                bankCardId = bankcardBean.getId();
                bankcard_stv.setCenterString(bankcardBean.getName()+bankcardBean.getNumber());
                bankcard_stv.getCenterTextView().setBackground(getd(R.drawable.module_shape_bg_top_white));
                bankcard_stv.setCenterTextColor(getc(R.color.gray_a9));
                bankcard_stv.setRightString("切换");
            }
        }
        bankcard_stv.setRightTextGroupClickListener(new SuperTextView.OnRightTextGroupClickListener() {
            @Override
            public void onClickListener(View view) {
                goToPagePutSerializable(getContext(),BankcardListActivity.class,getIntentEntityMap(new Object[]{true}));
            }
        });
    }

    @OnClick({R.id.alipay_stv,R.id.wechaty_stv,R.id.bankcard_stv,R.id.btn_cancel})
    public void OnClickViews(View v){
        switch (v.getId()){
            case R.id.alipay_stv:
                if (listener!=null)
                    listener.onSelected(this,cashInfoBean.getAlipay(),0,-1);
                break;
            case R.id.wechaty_stv:
                if (listener!=null)
                    listener.onSelected(this,cashInfoBean.getWechat(),1,-1);
                break;
            case R.id.bankcard_stv:
                if (listener!=null && bankCardId>0)
                    listener.onSelected(this,"",2,bankCardId);
                break;
            case R.id.btn_cancel:
                this.dismiss();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBIndEvent(Object object){
        if (object instanceof ThirdPartyBean){
            ThirdPartyBean thirdPartyBean =(ThirdPartyBean) object;
            if (thirdPartyBean.type == 0 && !isEmpty(thirdPartyBean.getNickname())){
                cashInfoBean.setAlipay(thirdPartyBean.getNickname());
                alipay_stv.getCenterTextView().setVisibility(GONE);
                alipay_stv.getRightTextView().setVisibility(GONE);
                alipay_stv.getRightIconIV().setVisibility(GONE);
            }else if (thirdPartyBean.type == 1 && !isEmpty(thirdPartyBean.getNickname())){
                cashInfoBean.setWechat(thirdPartyBean.getNickname());
                wechaty_stv.getCenterTextView().setVisibility(GONE);
                wechaty_stv.getRightTextView().setVisibility(GONE);
                wechaty_stv.getRightIconIV().setVisibility(GONE);
            }
        }else if (object instanceof BankcardBean){
            BankcardBean bankcardBean = (BankcardBean)object;
            bankCardId=bankcardBean.getId();
            bankcard_stv.setCenterString(bankcardBean.getName()+bankcardBean.getNumber());
            bankcard_stv.getCenterTextView().setBackground(getd(R.drawable.module_shape_bg_top_white));
            bankcard_stv.setCenterTextColor(getc(R.color.gray_a9));
            bankcard_stv.setRightString("切换");
        }

    }
}
