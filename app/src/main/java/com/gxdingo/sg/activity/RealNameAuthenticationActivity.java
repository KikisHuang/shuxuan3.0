package com.gxdingo.sg.activity;

import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.IdCardOCRBean;
import com.gxdingo.sg.bean.IdSwitchEvent;
import com.gxdingo.sg.bean.WeChatLoginEvent;
import com.gxdingo.sg.biz.AuthenticationContract;
import com.gxdingo.sg.biz.LoginContract;
import com.gxdingo.sg.dialog.AuthenticationStatusPopupView;
import com.gxdingo.sg.dialog.ChatFunctionDialog;
import com.gxdingo.sg.presenter.AuthenticationPresenter;
import com.gxdingo.sg.presenter.LoginPresenter;
import com.gxdingo.sg.utils.LocalConstant;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.kikis.commnlibrary.dialog.BaseActionSheetPopupView;
import com.kikis.commnlibrary.utils.GlideUtils;
import com.kikis.commnlibrary.utils.ScreenUtils;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import butterknife.BindView;
import butterknife.OnClick;

import static com.blankj.utilcode.util.ClipboardUtils.copyText;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.ScreenUtils.dp2px;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * @author: Kikis
 * @date: 2021/12/30
 * @page:实名认证页
 */
public class RealNameAuthenticationActivity extends BaseMvpActivity<AuthenticationContract.AuthenticationPresenter> implements AuthenticationContract.AuthenticationListener {


    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;


    @BindView(R.id.bottom_cl)
    public ConstraintLayout bottom_cl;

    @BindView(R.id.name_edt)
    public EditText name_edt;

    @BindView(R.id.idcard_edt)
    public EditText idcard_edt;


    @BindView(R.id.submit_bt)
    public Button submit_bt;


    @Override
    protected AuthenticationContract.AuthenticationPresenter createPresenter() {
        return new AuthenticationPresenter();
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
        return R.layout.module_activity_real_name_authentication;
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
        title_layout.setTitleText(gets(R.string.real_name_authentication));
    }

    @Override
    protected void initData() {

        int width = ScreenUtils.getScreenWidth(reference.get()) / 2 - dp2px(44);

    }


    @OnClick({R.id.submit_bt, R.id.upload_video_bt})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;
        switch (v.getId()) {

            case R.id.upload_video_bt:
                showAlbumDialog();
                break;
            case R.id.submit_bt:
                if (submit_bt.isSelected())
                    getP().submitAuthenticationInfo();

                break;

        }
    }

    /**
     * 显示相册、拍照选择弹窗
     *
     */
    private void showAlbumDialog() {
        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true)
                .isDarkTheme(false)
                .asCustom(new BaseActionSheetPopupView(reference.get()).addSheetItem(gets(R.string.photo_album), gets(R.string.photo_graph)).setItemClickListener((itemv, pos) -> {
                    getP().photoItemClick(pos);
                })).show();
    }


    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);

    }

    @Override
    public String getIdCardName() {
        return name_edt.getText().toString();
    }

    @Override
    public String getIdCardNumber() {
        return idcard_edt.getText().toString();
    }




    @Override
    public void onSucceed(int type) {
        super.onSucceed(type);
        sendEvent(LocalConstant.AUTHENTICATION_SUCCEEDS);
        finish();
    }
}
