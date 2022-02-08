package com.gxdingo.sg.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ArticleImage;
import com.gxdingo.sg.bean.CategoriesBean;
import com.gxdingo.sg.bean.HelpBean;
import com.gxdingo.sg.bean.HomeBannerBean;
import com.gxdingo.sg.bean.ShareBean;
import com.gxdingo.sg.bean.StoreListBean;
import com.gxdingo.sg.bean.changeLocationEvent;
import com.gxdingo.sg.biz.ClientHomeContract;
import com.gxdingo.sg.dialog.InviteFriendsActionSheetPopupView;
import com.gxdingo.sg.presenter.ClientHomePresenter;
import com.gxdingo.sg.utils.ShareUtils;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.adapter.BaseRecyclerAdapter;
import com.kikis.commnlibrary.bean.AddressBean;
import com.kikis.commnlibrary.dialog.BaseActionSheetPopupView;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.blankj.utilcode.util.ClipboardUtils.copyText;
import static com.gxdingo.sg.http.Api.CLIENT_HDGZ_AGREEMENT_KEY;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;


/**
 * @author: Weaving
 * @date: 2021/11/15
 * @page:
 */
public class ClientSettleActivity extends BaseMvpActivity<ClientHomeContract.ClientHomePresenter> implements ClientHomeContract.ClientHomeListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.settle_in_iv)
    public ImageView settle_in_iv;

    private ShareBean shareBean;

    @Override
    protected ClientHomeContract.ClientHomePresenter createPresenter() {
        return new ClientHomePresenter(false);
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
        return R.layout.module_activity_client_settle;
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
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
        if (object instanceof ArticleImage) {
            ArticleImage articleImage = (ArticleImage) object;
            Glide.with(this).load(articleImage.getImage()).into(settle_in_iv);
        }

    }

    @OnClick({R.id.btn_become_store, R.id.btn_invitation, R.id.btn_more})
    public void onClickViews(View v) {
        switch (v.getId()) {
            case R.id.btn_become_store:
                if (UserInfoUtils.getInstance().isLogin())
                    getP().convertStore();
                else
                    getP().oauth(this);
                break;
            case R.id.btn_invitation:
                if (shareBean != null) {
                    new XPopup.Builder(reference.get())
                            .isDarkTheme(false)
                            .asCustom(new InviteFriendsActionSheetPopupView(reference.get(), view -> {
                                if (view.getId() == R.id.share_wechat_ll)
                                    ShareUtils.UmShare(reference.get(), null, shareBean.getUrl(), shareBean.getTitle(), shareBean.getDescribe(), R.mipmap.ic_app_logo, SHARE_MEDIA.WEIXIN);
                                else if (view.getId() == R.id.copy_invite_friends_ll) {
                                    copyText(shareBean.getInviteCode());
                                    onMessage("已复制到剪切板");
                                }

                            }).show());

                } else
                    onMessage("没有获取到分享连接");

                break;
            case R.id.btn_more:
                goToPagePutSerializable(reference.get(), WebActivity.class, getIntentEntityMap(new Object[]{true, 0, CLIENT_HDGZ_AGREEMENT_KEY}));

                break;
        }
    }

    @Override
    protected void init() {
        title_layout.setMoreText("活动规则");

        title_layout.getMoreTextView().setTextColor(getc(R.color.green_dominant_tone));
        title_layout.setTitleText("入驻平台");
    }

    @Override
    protected void initData() {
        getP().getSettleImage();
        getP().getShareUrl();
    }

    @Override
    public void setDistrict(String district) {

    }

    @Override
    public void onCategoryResult(List<CategoriesBean> categories) {
    }

    @Override
    public void onStoresResult(boolean refresh, boolean search, List<StoreListBean.StoreBean> storeBeans) {

    }


    @Override
    public void onHistoryResult(List<String> searchHistories) {

    }

    @Override
    public void onShareUrlResult(ShareBean sb) {

        shareBean = sb;
    }
}
