package com.gxdingo.sg.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;

import com.allen.library.SuperTextView;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientCashInfoBean;
import com.lxj.xpopup.core.BottomPopupView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.CommonUtils.getd;

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

    private ClientCashInfoBean cashInfoBean;

    public ClientCashSelectDialog(@NonNull Context context,ClientCashInfoBean cashInfoBean) {
        super(context);
        this.cashInfoBean = cashInfoBean;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_client_cash_select_account;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
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
                bankcard_stv.setCenterString(cashInfoBean.getBankList().get(0).getName()+cashInfoBean.getBankList().get(0).getNumber());
                bankcard_stv.getCenterTextView().setBackground(getd(R.drawable.module_shape_bg_top_white));
                bankcard_stv.setCenterTextColor(getc(R.color.gray_a9));
                bankcard_stv.setRightString("切换");
            }
        }
    }

    @OnClick({R.id.alipay_stv,R.id.wechaty_stv,R.id.bankcard_stv,R.id.btn_cancel})
    public void OnClickViews(View v){
        switch (v.getId()){
            case R.id.alipay_stv:
                break;
            case R.id.wechaty_stv:
                break;
            case R.id.bankcard_stv:
                break;
            case R.id.btn_cancel:
                this.dismiss();
                break;
        }
    }
}
