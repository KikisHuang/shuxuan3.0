package com.gxdingo.sg.presenter;

import android.text.InputType;
import android.view.View;

import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.ChangeBindPhoneContract;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.model.ChangePhoneModel;
import com.gxdingo.sg.model.ClientNetworkModel;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.model.WebSocketModel;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.zhouyou.http.subsciber.BaseSubscriber;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.RegexUtils.isMobileSimple;
import static com.gxdingo.sg.utils.LocalConstant.BIND_NEW_PHONE;
import static com.kikis.commnlibrary.utils.CommonUtils.HideMobile;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;

/**
 * @author: Weaving
 * @date: 2021/10/24
 * @page:
 */
public class ChangeBindPhonePresenter extends BaseMvpPresenter<BasicsListener, ChangeBindPhoneContract.ChangeBindPhoneListener> implements ChangeBindPhoneContract.ChangeBindPhonePresenter, NetWorkListener {

    private ChangePhoneModel changePhoneModel;

    private ClientNetworkModel clientNetworkModel;

    private NetworkModel mNetworkModel;

    private String mNewPhoneNum = "";

    private String phone = UserInfoUtils.getInstance().getUserInfo().getMobile();
//    private String phone = "18878759765";

    public ChangeBindPhonePresenter() {
        changePhoneModel = new ChangePhoneModel();
        clientNetworkModel = new ClientNetworkModel(this);
        mNetworkModel = new NetworkModel(this);
    }

    @Override
    public void getUserPhone() {
        if (isViewAttached() && !isEmpty(phone))
            getV().setUserPhone(phone);
    }

    @Override
    public void sendOldPhoneVerificationCode() {
        if (mNetworkModel != null && isViewAttached()) {
            mNetworkModel.sendSmsCodeResult(getContext(), phone, o -> {
                getV().oldPhoneNumberCountDown();
            });
        }
    }

    @Override
    public void sendNewPhoneVerificationCode(String phone) {
        if (mNetworkModel != null && isViewAttached()) {
            mNetworkModel.sendSmsCodeResult(getContext(), phone, o -> {
                getV().newPhoneNumberCountDown();
            });
        }
    }

    @Override
    public void nextStep(String str) {
        if (changePhoneModel != null && isBViewAttached()) {
            if (isEmpty(str)) {
                getBV().onMessage(changePhoneModel.pageLogic() == 2 ? gets(R.string.phone_number_can_not_null) : gets(R.string.verification_code_can_not_null));
                return;
            }

            switch (changePhoneModel.pageLogic()) {
                case 1:
                    if (mNetworkModel != null)
                        mNetworkModel.checkSMSCode(getContext(), phone, str, o -> {
                            if (isViewAttached()) {
                                getV().setEdittextHint(gets(R.string.input_new_phone_number));
                                getV().changeTitle(gets(R.string.binding_new_phone_number));
                                getV().changeHint("更换手机号后，下次登陆可使用新手机号登陆");
                                getV().countryCodeShow(true);
                                getV().oldPhoneCodeCountdownVisibility(View.GONE);
                                getV().newPhoneCodeCountdownVisibility(View.GONE);
                                getV().bottomHintVisibility(View.GONE);
                                getV().changeNextBtnText(gets(R.string.get_verification_code));
                                getV().setEdittextInputType(InputType.TYPE_CLASS_PHONE);
                                getV().setEdittextContent("");
                                changePhoneModel.next();
                            }
                        });

                    break;

                case 2:


                    if (isViewAttached()) {
                        if (!isMobileSimple(str)) {
                            getBV().onMessage(gets(R.string.please_input_correct_phone_number));
                            return;
                        } else if (getV().getNumberCountDown() > 0) {
                            getBV().onMessage("操作太频繁，请" + getV().getNumberCountDown() + "秒后再操作");
                            return;
                        }
                        mNewPhoneNum = str;
                        getV().setEdittextHint(gets(R.string.input_verification_code));
                        getV().changeTitle(gets(R.string.binding_new_phone_number));
                        getV().changeHint("为确认身份，请输入" + HideMobile(mNewPhoneNum) + "收到的短信验证码");
                        getV().countryCodeShow(false);
                        getV().oldPhoneCodeCountdownVisibility(View.GONE);
                        getV().newPhoneCodeCountdownVisibility(View.VISIBLE);
                        getV().bottomHintVisibility(View.GONE);
                        getV().changeNextBtnText(gets(R.string.done));
                        getV().setEdittextInputType(InputType.TYPE_CLASS_NUMBER);
                        getV().setEdittextContent("");
                        sendNewPhoneVerificationCode(str);
                        changePhoneModel.next();
                    }

                    break;
                case 3:

                    if (clientNetworkModel != null)
                        clientNetworkModel.bindNewPhone(getContext(), mNewPhoneNum, str);

                    break;

            }

        }
    }

    @Override
    public void lastStep() {
        if (changePhoneModel != null && isBViewAttached()) {

            switch (changePhoneModel.pageLogic()) {
                case 1:

                    getBV().onFailed();
                    break;

                case 2:
                    if (isViewAttached()) {

                        getV().changeTitle(gets(R.string.change_bind_phone_number));
                        getV().changeHint("为确认身份，请输入" + HideMobile(phone) + "收到的短信验证码");
                        getV().countryCodeShow(false);
                        getV().setEdittextHint(gets(R.string.input_verification_code));
                        getV().oldPhoneCodeCountdownVisibility(View.VISIBLE);
                        getV().newPhoneCodeCountdownVisibility(View.GONE);
                        getV().bottomHintVisibility(View.VISIBLE);
                        getV().changeNextBtnText(gets(R.string.next));
                        getV().setEdittextInputType(InputType.TYPE_CLASS_NUMBER);
                        changePhoneModel.lastStep();
                    }
                    break;
                case 3:
                    if (isViewAttached()) {
                        getV().changeTitle(gets(R.string.binding_new_phone_number));
                        getV().changeHint("更换手机号后，下次登陆可使用新手机号登陆");
                        getV().countryCodeShow(false);
                        getV().setEdittextHint(gets(R.string.input_new_phone_number));
                        getV().oldPhoneCodeCountdownVisibility(View.GONE);
                        getV().newPhoneCodeCountdownVisibility(View.GONE);
                        getV().bottomHintVisibility(View.GONE);
                        getV().changeNextBtnText(gets(R.string.get_verification_code));
                        getV().setEdittextInputType(InputType.TYPE_CLASS_PHONE);
                        getV().setEdittextContent(mNewPhoneNum);
                        changePhoneModel.lastStep();
                    }

                    break;

            }
        }

    }

    @Override
    public void onSucceed(int type) {
        if (type == BIND_NEW_PHONE) {
            if (isBViewAttached()) {
                UserInfoUtils.getInstance().clearLoginStatus();
                UserInfoUtils.getInstance().goToLoginPage(getContext(), gets(R.string.please_relogin));
                getBV().onFailed();
            }

        }
    }

    @Override
    public void onMessage(String msg) {
        if (isBViewAttached())
            getBV().onMessage(msg);
    }

    @Override
    public void noData() {
        if (isBViewAttached())
            getBV().noData();
    }

    @Override
    public void onData(boolean refresh, Object o) {

    }

    @Override
    public void haveData() {
        if (isBViewAttached())
            getBV().haveData();
    }

    @Override
    public void finishLoadmoreWithNoMoreData() {
        if (isBViewAttached())
            getBV().finishLoadmoreWithNoMoreData();
    }

    @Override
    public void finishRefreshWithNoMoreData() {
        if (isBViewAttached())
            getBV().finishRefreshWithNoMoreData();
    }

    @Override
    public void onRequestComplete() {
        if (isBViewAttached())
            getBV().onRequestComplete();
    }

    @Override
    public void resetNoMoreData() {
        if (isBViewAttached())
            getBV().resetNoMoreData();
    }

    @Override
    public void finishRefresh(boolean success) {
        if (isBViewAttached())
            getBV().finishRefresh(success);
    }

    @Override
    public void finishLoadmore(boolean success) {
        if (isBViewAttached())
            getBV().finishLoadmore(success);
    }

    @Override
    public void onAfters() {
        if (isBViewAttached())
            getBV().onAfters();
    }

    @Override
    public void onStarts() {
        if (isBViewAttached())
            getBV().onStarts();
    }

    @Override
    public void onDisposable(BaseSubscriber subscriber) {
        addDisposable(subscriber);
    }
}
