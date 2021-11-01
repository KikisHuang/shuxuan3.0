package com.gxdingo.sg.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.IMTransferAccountsPayContract;
import com.gxdingo.sg.dialog.EnterPaymentPasswordPopupView;
import com.gxdingo.sg.presenter.IMTransferAccountsPayPresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.KeyboardHeightObserver;
import com.kikis.commnlibrary.view.NiceImageView;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * IM-转账支付
 * @author JM
 */
public class IMTransferAccountsPayActivity extends BaseMvpActivity<IMTransferAccountsPayContract.IMTransferAccountsPayPresenter> implements IMTransferAccountsPayContract.IMTransferAccountsPayPresenter, KeyboardHeightObserver {
    @BindView(R.id.title_layout)
    TemplateTitle titleLayout;
    @BindView(R.id.tv_go)
    TextView tvGo;
    @BindView(R.id.niv_avatar)
    NiceImageView nivAvatar;
    @BindView(R.id.tv_payee_name)
    TextView tvPayeeName;
    @BindView(R.id.tv_transfer_amount_title)
    TextView tvTransferAmountTitle;
    @BindView(R.id.tv_transfer_amount)
    LinearLayout tvTransferAmount;
    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.tv_3)
    TextView tv3;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_4)
    TextView tv4;
    @BindView(R.id.tv_5)
    TextView tv5;
    @BindView(R.id.tv_6)
    TextView tv6;
    @BindView(R.id.tv_7)
    TextView tv7;
    @BindView(R.id.tv_8)
    TextView tv8;
    @BindView(R.id.tv_9)
    TextView tv9;
    @BindView(R.id.tv_0)
    TextView tv0;
    @BindView(R.id.tv_point)
    TextView tvPoint;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.et_amount)
    EditText etAmount;

    @Override
    protected IMTransferAccountsPayContract.IMTransferAccountsPayPresenter createPresenter() {
        return new IMTransferAccountsPayPresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return false;
    }

    @Override
    protected int activityTitleLayout() {
        return R.layout.module_include_new_custom_title;
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
        return R.layout.module_activity_im_transfer_accounts_pay;
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
        titleLayout.setTitleText("转账支付");
        etAmount.setInputType(InputType.TYPE_NULL);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onKeyboardHeightChanged(int height, int orientation) {

    }

    @Override
    public void onMvpAttachView(Context context, BasicsListener bview, IMTransferAccountsPayContract.IMTransferAccountsPayListener view) {

    }

    @Override
    public void onMvpStart() {

    }

    @Override
    public void onMvpResume() {

    }

    @Override
    public void onMvpPause() {

    }

    @Override
    public void onMvpStop() {

    }

    @Override
    public void onMvpSaveInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onMvpDetachView(boolean retainInstance) {

    }

    @Override
    public void onMvpDestroy() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_1, R.id.tv_2, R.id.tv_3, R.id.rl_back, R.id.tv_4, R.id.tv_5, R.id.tv_6, R.id.tv_7, R.id.tv_8, R.id.tv_9, R.id.tv_0, R.id.tv_point, R.id.tv_submit})
    public void onViewClicked(View view) {
        String amountStr = etAmount.getText().toString();
        switch (view.getId()) {
            case R.id.tv_1:
                //如果数字排在第一个并且不包含有小数点则去掉0,
                if (amountStr.indexOf("0") == 0 && !amountStr.contains(".")) {
                    etAmount.setText("");
                }
                etAmount.append("1");
                break;
            case R.id.tv_2:
                //如果数字排在第一个并且不包含有小数点则去掉0,
                if (amountStr.indexOf("0") == 0 && !amountStr.contains(".")) {
                    etAmount.setText("");
                }
                etAmount.append("2");
                break;
            case R.id.tv_3:
                //如果数字排在第一个并且不包含有小数点则去掉0,
                if (amountStr.indexOf("0") == 0 && !amountStr.contains(".")) {
                    etAmount.setText("");
                }
                etAmount.append("3");
                break;
            case R.id.rl_back:
                if (amountStr != null && amountStr.length() > 0) {
                    amountStr = amountStr.substring(0, amountStr.length() - 1);
                    etAmount.setText(amountStr);
                }
                break;
            case R.id.tv_4:
                //如果数字排在第一个并且不包含有小数点则去掉0,
                if (amountStr.indexOf("0") == 0 && !amountStr.contains(".")) {
                    etAmount.setText("");
                }
                etAmount.append("4");
                break;
            case R.id.tv_5:
                //如果数字排在第一个并且不包含有小数点则去掉0,
                if (amountStr.indexOf("0") == 0 && !amountStr.contains(".")) {
                    etAmount.setText("");
                }
                etAmount.append("5");
                break;
            case R.id.tv_6:
                //如果数字排在第一个并且不包含有小数点则去掉0,
                if (amountStr.indexOf("0") == 0 && !amountStr.contains(".")) {
                    etAmount.setText("");
                }
                etAmount.append("6");
                break;
            case R.id.tv_7:
                //如果数字排在第一个并且不包含有小数点则去掉0,
                if (amountStr.indexOf("0") == 0 && !amountStr.contains(".")) {
                    etAmount.setText("");
                }
                etAmount.append("7");
                break;
            case R.id.tv_8:
                //如果数字排在第一个并且不包含有小数点则去掉0,
                if (amountStr.indexOf("0") == 0 && !amountStr.contains(".")) {
                    etAmount.setText("");
                }
                etAmount.append("8");
                break;
            case R.id.tv_9:
                //如果数字排在第一个并且不包含有小数点则去掉0,
                if (amountStr.indexOf("0") == 0 && !amountStr.contains(".")) {
                    etAmount.setText("");
                }
                etAmount.append("9");
                break;
            case R.id.tv_0:
                //如果数字排在第一个并且不包含有小数点则去掉0,
                if (amountStr.indexOf("0") == 0 && !amountStr.contains(".")) {
                    etAmount.setText("");
                }
                etAmount.append("0");
                break;
            case R.id.tv_point:
                //只能输入一个小数点
                if (!amountStr.contains(".")) {
                    if (amountStr.equals("")) {
                        etAmount.append("0.");
                    } else {
                        etAmount.append(".");
                    }
                }
                break;
            case R.id.tv_submit:
                new XPopup.Builder(reference.get())
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .isDarkTheme(false)
                        .dismissOnTouchOutside(false)
                        .asCustom(new EnterPaymentPasswordPopupView(reference.get(), new EnterPaymentPasswordPopupView.OnCallbackPasswordListener() {
                            @Override
                            public void getPassword(String pass) {

                            }
                        })).show();
                break;
        }
    }
}
