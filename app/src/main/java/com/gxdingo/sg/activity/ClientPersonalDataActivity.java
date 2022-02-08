package com.gxdingo.sg.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ClientMineBean;
import com.gxdingo.sg.biz.ClientMineContract;
import com.gxdingo.sg.presenter.ClientMinePresenter;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.dialog.BaseActionSheetPopupView;
import com.kikis.commnlibrary.utils.GlideUtils;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;
import static com.kikis.commnlibrary.utils.CommonUtils.finishac;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;

/**
 * @author: Weaving
 * @date: 2021/10/14
 * @page:
 */
public class ClientPersonalDataActivity extends BaseMvpActivity<ClientMineContract.ClientMinePresenter> implements ClientMineContract.ClientMineListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.avatar_stv)
    public SuperTextView avatar_stv;

    @BindView(R.id.save_tv)
    public TextView save_tv;

    @BindView(R.id.nick_name_edt)
    public EditText nick_name_edt;

    private BasePopupView mPhotoPopupView;


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
        return R.layout.module_activity_client_personal_data;
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
        avatar_stv.getLeftIconIV().setTransitionName(gets(R.string.my_catransition));

        avatar_stv.getLeftIconIV().setTransitionName("userAvatar");
        String avatar = UserInfoUtils.getInstance().getUserAvatar();
        String nickname = UserInfoUtils.getInstance().getUserNickName();

        changeAvatar(avatar);

        if (!isEmpty(nickname))
            nick_name_edt.setText(nickname);

        title_layout.setTitleText(gets(R.string.edit_profile));
        title_layout.setMoreText(gets(R.string.done));
        getP().editsetInit(nick_name_edt, 15);
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.title_back, R.id.avatar_stv, R.id.txt_more, R.id.save_tv})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;
        switch (v.getId()) {
            case R.id.avatar_stv:
                if (mPhotoPopupView == null) {
                    mPhotoPopupView = new XPopup.Builder(reference.get())
                            .isDarkTheme(false)
                            .asCustom(new BaseActionSheetPopupView(reference.get()).addSheetItem(gets(R.string.photo_album), gets(R.string.photo_graph)).setItemClickListener((itemv, pos) -> {
                                getP().photoItemClick(pos);
                            })).show();
                } else
                    mPhotoPopupView.show();
                break;
            case R.id.save_tv:
                getP().modityNickName(nick_name_edt.getText().toString());
                break;
            case R.id.title_back:
                finishac(reference.get());
                break;
            case R.id.txt_more:
                getP().modityNickName(nick_name_edt.getText().toString());
                finish();
                break;

        }

    }

    @Override
    public void changeAvatar(Object o) {
        String a = (String) o;
        Glide.with(reference.get()).load(isEmpty(a) ? R.drawable.module_svg_client_default_avatar : a).apply(GlideUtils.getInstance().getCircleCrop()).into(avatar_stv.getLeftIconIV());
    }

    @Override
    public RxPermissions getPermissions() {
        return getRxPermissions();
    }

    @Override
    public void onMineDataResult(ClientMineBean mineBean) {

    }

    @Override
    public void onRemindResult(String remindValue) {

    }
}
