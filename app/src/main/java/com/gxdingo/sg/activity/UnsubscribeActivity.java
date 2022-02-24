package com.gxdingo.sg.activity;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ArticleImage;
import com.gxdingo.sg.bean.ClientMineBean;
import com.gxdingo.sg.bean.UserBean;
import com.gxdingo.sg.biz.ClientMineContract;
import com.gxdingo.sg.presenter.ClientMinePresenter;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.dialog.BaseActionSheetPopupView;
import com.kikis.commnlibrary.utils.Constant;
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
 * @author: Kikis
 * @date: 2022/1/25
 * @page:
 */
public class UnsubscribeActivity extends BaseMvpActivity<ClientMineContract.ClientMinePresenter> implements ClientMineContract.ClientMineListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;


    @BindView(R.id.cancel_logoff_tv)
    public TextView cancel_logoff_tv;

    @BindView(R.id.status_ll)
    public LinearLayout status_ll;

    @BindView(R.id.article_img)
    public ImageView article_img;

    @BindView(R.id.status_content_tv)
    public TextView status_content_tv;

    @BindView(R.id.status_hint_tv)
    public TextView status_hint_tv;

    @BindView(R.id.affirmative_cbx)
    public CheckBox affirmative_cbx;

    private int mStatus = 0;

    @Override
    protected ClientMineContract.ClientMinePresenter createPresenter() {
        return new ClientMinePresenter();
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
        return R.layout.module_activity_unsubscribe;
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
        title_layout.setTitleText(gets(R.string.logout));
    }

    @Override
    protected void initData() {


        String article = UserInfoUtils.getInstance().getUserInfo().getRole() == 10 ? "sxyg_user_logoff_instruction" : "sxyg_store_logoff_instruction";
        getP().getArticleImg(article);
        getP().refreshStatus();

    }

    @OnClick({R.id.cancel_logoff_tv, R.id.cancel_tv, R.id.start_logout_tv})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;
        switch (v.getId()) {
            case R.id.start_logout_tv:
                if (affirmative_cbx.isChecked())
                    getP().loginOff(0);
                else
                    onMessage("请先勾选注销须知协议");
                break;
            case R.id.cancel_logoff_tv:
                if (mStatus == 0)
                    getP().loginOff(1);
                else if (mStatus == 2) {
                    status_ll.setVisibility(View.GONE);
                    cancel_logoff_tv.setVisibility(View.GONE);
                }
                break;
            case R.id.cancel_tv:
                finish();
                break;

        }
    }

    @Override
    public void changeAvatar(Object o) {

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

    @Override
    public void onStatusResult(UserBean userBean) {

        mStatus = userBean.logoffAuditStatus;

        status_ll.setVisibility(userBean.logoffAuditStatus == 0 || userBean.logoffAuditStatus == 2 ? View.VISIBLE : View.GONE);
        cancel_logoff_tv.setVisibility(userBean.logoffAuditStatus == 0 || userBean.logoffAuditStatus == 2 ? View.VISIBLE : View.GONE);

        if (userBean.logoffAuditStatus == 0 || userBean.logoffAuditStatus == 2) {
            status_hint_tv.setText(userBean.logoffAuditStatus == 0 ? "您提交的注销申请正在审核中" : "您提交的注销申请审核不通过");
            status_content_tv.setVisibility(userBean.logoffAuditStatus == 2 ? View.VISIBLE : View.GONE);

            if (userBean.logoffAuditStatus == 2)
                status_content_tv.setText(userBean.logoffRejectText);

            cancel_logoff_tv.setText(userBean.logoffAuditStatus == 0 ? "撤销申请" : "返回");
        }

    }

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
        if (object instanceof ArticleImage) {
            ArticleImage articleImage = (ArticleImage) object;
            Glide.with(this).load(articleImage.getImage()).apply(GlideUtils.getInstance().getDefaultOptions().fitCenter()).override(1080,1920).into(article_img);
        }

    }

}
