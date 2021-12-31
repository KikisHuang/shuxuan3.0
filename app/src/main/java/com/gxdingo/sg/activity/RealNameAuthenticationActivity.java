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

    @BindView(R.id.front_img)
    public ImageView front_img;

    @BindView(R.id.front_check_img)
    public ImageView front_check_img;

    @BindView(R.id.front_upload_ll)
    public LinearLayout front_upload_ll;

    @BindView(R.id.bottom_cl)
    public ConstraintLayout bottom_cl;

    @BindView(R.id.name_edt)
    public EditText name_edt;

    @BindView(R.id.idcard_edt)
    public EditText idcard_edt;

    @BindView(R.id.back_img)
    public ImageView back_img;

    @BindView(R.id.back_check_img)
    public ImageView back_check_img;

    @BindView(R.id.back_upload_ll)
    public LinearLayout back_upload_ll;

    @BindView(R.id.hint3)
    public TextView hint3;

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

        front_img.getLayoutParams().height = width * 10 / 16;

        back_img.getLayoutParams().height = width * 10 / 16;
    }


    @OnClick({R.id.submit_bt, R.id.front_img, R.id.back_img, R.id.front_upload_ll, R.id.back_upload_ll})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;
        switch (v.getId()) {

            case R.id.front_img:
                showAlbumDialog(0);
                break;
            case R.id.back_img:
                showAlbumDialog(1);
                break;
            case R.id.front_upload_ll:
                showAlbumDialog(0);
                break;
            case R.id.back_upload_ll:
                showAlbumDialog(1);
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
     * @param type
     */
    private void showAlbumDialog(int type) {
        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true)
                .isDarkTheme(false)
                .asCustom(new BaseActionSheetPopupView(reference.get()).addSheetItem(gets(R.string.photo_album), gets(R.string.photo_graph)).setItemClickListener((itemv, pos) -> {
                    getP().photoItemClick(pos, type);
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
    public void upLoadSucceed(String path, int selectedType) {

        if (selectedType == 0) {
            front_upload_ll.setVisibility(View.GONE);
            front_check_img.setVisibility(View.VISIBLE);
            front_img.setVisibility(View.VISIBLE);

        } else {
            back_upload_ll.setVisibility(View.GONE);
            back_check_img.setVisibility(View.VISIBLE);
            back_img.setVisibility(View.VISIBLE);
        }

        Glide.with(reference.get()).load(path).apply(GlideUtils.getInstance().getGlideRoundOptions(6)).into(selectedType == 0 ? front_img : back_img);

    }

    @Override
    public void onOCRInfoResult(IdCardOCRBean data) {

        hint3.setVisibility(View.VISIBLE);

        if (data != null && data.getFrontResult() != null && !isEmpty(data.getFrontResult().getIdnumber())) {
            bottom_cl.setVisibility(View.VISIBLE);
            if (!isEmpty(data.getFrontResult().getName()))
                name_edt.setText(data.getFrontResult().getName());

            if (!isEmpty(data.getFrontResult().getIdnumber()))
                idcard_edt.setText(data.getFrontResult().getIdnumber());
        }
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
    public void changeButtonStatus() {
        submit_bt.setSelected(true);
    }

    @Override
    public void onOCRFailed(int type) {
        if (type == 0) {
            front_upload_ll.setVisibility(View.VISIBLE);
            front_check_img.setVisibility(View.GONE);
            front_img.setVisibility(View.INVISIBLE);
        } else {
            back_upload_ll.setVisibility(View.VISIBLE);
            back_check_img.setVisibility(View.GONE);
            back_img.setVisibility(View.INVISIBLE);
        }
        if (back_img.getVisibility() == View.INVISIBLE && front_img.getVisibility() == View.INVISIBLE)
            hint3.setVisibility(View.GONE);

    }


    @Override
    public void onSucceed(int type) {
        super.onSucceed(type);
        finish();
    }
}
