package com.gxdingo.sg.activity;

import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.gxdingo.sg.R;
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
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import butterknife.BindView;
import butterknife.OnClick;

import static com.blankj.utilcode.util.ClipboardUtils.copyText;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;

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


    @BindView(R.id.back_img)
    public ImageView back_img;

    @BindView(R.id.back_check_img)
    public ImageView back_check_img;

    @BindView(R.id.back_upload_ll)
    public LinearLayout back_upload_ll;

    @BindView(R.id.hint3)
    public TextView hint3;


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

        showAuthenticationStatusDialog();
    }


    @OnClick({R.id.front_img, R.id.back_img, R.id.front_upload_ll, R.id.back_upload_ll})
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

    /**
     * 显示认证状态弹窗
     */
    private void showAuthenticationStatusDialog() {
        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .autoDismiss(true)
                .hasShadowBg(true)
                .asCustom(new AuthenticationStatusPopupView(reference.get(), status -> {

                }).show());
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

        hint3.setVisibility(View.VISIBLE);

        Glide.with(reference.get()).load(path).apply(GlideUtils.getInstance().getGlideRoundOptions(6)).into(selectedType == 0 ? front_img : back_img);

    }

}
