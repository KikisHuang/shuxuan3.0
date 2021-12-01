package com.gxdingo.sg.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.IMChatHistoryListBean;
import com.gxdingo.sg.bean.PayBean;
import com.gxdingo.sg.biz.IMTransferAccountsPayContract;
import com.gxdingo.sg.dialog.EnterPaymentPasswordPopupView;
import com.gxdingo.sg.presenter.IMTransferAccountsPayPresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.KeyboardHeightObserver;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.utils.GlideUtils;
import com.kikis.commnlibrary.view.NiceImageView;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.Constant.PAYMENT_SUCCESS;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * IM-转账支付
 *
 * @author JM
 */
public class IMTransferAccountsPayActivity extends BaseMvpActivity<IMTransferAccountsPayContract.IMTransferAccountsPayPresenter> implements IMTransferAccountsPayContract.IMTransferAccountsPayListener, KeyboardHeightObserver {
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

    private String mShareUuid = "";

    private int mPayType = 30;

    private IMChatHistoryListBean.OtherAvatarInfo otherInfo;

    private PayBean.TransferAccountsDTO transferAccounts;

    @Override
    protected IMTransferAccountsPayContract.IMTransferAccountsPayPresenter createPresenter() {
        return new IMTransferAccountsPayPresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return true;
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
        mShareUuid = getIntent().getStringExtra(Constant.SERIALIZABLE + 0);
        mPayType = getIntent().getIntExtra(Constant.SERIALIZABLE + 1, 30);
        otherInfo = (IMChatHistoryListBean.OtherAvatarInfo) getIntent().getSerializableExtra(Constant.SERIALIZABLE + 2);


        if (otherInfo != null) {
            if (!isEmpty(otherInfo.getSendNickname()))
                tvPayeeName.setText(otherInfo.getSendNickname());

            if (!isEmpty(otherInfo.getSendAvatar()))
            Glide.with(reference.get()).load(otherInfo.getSendAvatar()).apply(GlideUtils.getInstance().getCircleCrop()).into(nivAvatar);
        }


        if (isEmpty(mShareUuid)) {
            onMessage("没有获取到 shareUuid ");
            finish();
        }
        etAmount.addTextChangedListener(textWatcher);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onKeyboardHeightChanged(int height, int orientation) {

    }

    private TextWatcher textWatcher = new TextWatcher() {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0,
                            s.toString().indexOf(".") + 3);
                    etAmount.setText(s);
                    etAmount.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                etAmount.setText(s);
                etAmount.setSelection(2);
            }

            if (s.toString().startsWith("0")
                    && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    etAmount.setText(s.subSequence(0, 1));
                    etAmount.setSelection(1);
                    return;
                }
            }

        }

        @Override
        public void afterTextChanged(Editable s) {


        }
    };


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
                //余额支付密码弹窗
                if (mPayType == 30) {
                    new XPopup.Builder(reference.get())
                            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                            .isDarkTheme(false)
                            .dismissOnTouchOutside(false)
                            .asCustom(new EnterPaymentPasswordPopupView(reference.get(), new EnterPaymentPasswordPopupView.OnCallbackPasswordListener() {
                                @Override
                                public void getPassword(String pass) {
                                    getP().transfer(mShareUuid, mPayType, pass, etAmount.getText().toString());
                                }
                            })).show();
                } else
                    getP().transfer(mShareUuid, mPayType, "", etAmount.getText().toString());

                break;
        }
    }

    @Override
    public void onSetTransferAccounts(PayBean.TransferAccountsDTO transferAccounts) {
        this.transferAccounts = transferAccounts;
    }

    @Override
    public void onSucceed(int type) {
        super.onSucceed(type);
        onMessage(gets(R.string.payment_succeed));
        sendEvent(transferAccounts);
        finish();
    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type == PAYMENT_SUCCESS && transferAccounts != null) {
            onSucceed(0);
        }

    }
}