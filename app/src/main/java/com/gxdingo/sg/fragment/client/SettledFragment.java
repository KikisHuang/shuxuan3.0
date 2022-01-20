package com.gxdingo.sg.fragment.client;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.ChatActivity;
import com.gxdingo.sg.activity.ClientActivity;
import com.gxdingo.sg.activity.WebActivity;
import com.gxdingo.sg.adapter.StoreHomeIMMessageAdapter;
import com.gxdingo.sg.bean.ArticleImage;
import com.gxdingo.sg.bean.CategoriesBean;
import com.gxdingo.sg.bean.ExitChatEvent;
import com.gxdingo.sg.bean.HelpBean;
import com.gxdingo.sg.bean.ShareBean;
import com.gxdingo.sg.bean.StoreListBean;
import com.gxdingo.sg.biz.ClientHomeContract;
import com.gxdingo.sg.biz.ClientMessageContract;
import com.gxdingo.sg.dialog.InviteFriendsActionSheetPopupView;
import com.gxdingo.sg.presenter.ClientHomePresenter;
import com.gxdingo.sg.presenter.ClientMessagePresenter;
import com.gxdingo.sg.utils.ShareUtils;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.activitiy.BaseActivity;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.kikis.commnlibrary.bean.SubscribesListBean;
import com.kikis.commnlibrary.fragment.BaseMvpFragment;
import com.kikis.commnlibrary.utils.MessageCountManager;
import com.kikis.commnlibrary.utils.RxUtil;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static com.blankj.utilcode.util.ClipboardUtils.copyText;
import static com.gxdingo.sg.http.StoreApi.CLIENT_HDGZ_AGREEMENT_KEY;
import static com.gxdingo.sg.utils.ImServiceUtils.resetImService;
import static com.gxdingo.sg.utils.ImServiceUtils.startImService;
import static com.gxdingo.sg.utils.LocalConstant.CLIENT_LOGIN_SUCCEED;
import static com.gxdingo.sg.utils.LocalConstant.NOTIFY_MSG_LIST_ADAPTER;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.Constant.WEB_SOCKET_URL;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * @author: Kikis
 * @date: 2022/1/20
 * @page:
 */
public class SettledFragment extends BaseMvpFragment<ClientHomeContract.ClientHomePresenter> implements  ClientHomeContract.ClientHomeListener {


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
        return 0;
    }

    @Override
    protected int initContentView() {
        return R.layout.module_activity_client_settle;
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
    protected boolean refreshEnable() {
        return false;
    }

    @Override
    protected boolean loadmoreEnable() {
        return false;
    }


    @Override
    protected void init() {

    }


    @Override
    protected void lazyInit() {
        super.lazyInit();
        getP().getSettleImage();
        getP().getShareUrl();
    }

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
        if (object instanceof ArticleImage) {
            ArticleImage articleImage = (ArticleImage) object;
            Glide.with(this).load(articleImage.getImage()).into(settle_in_iv);
        }

    }

    @OnClick({R.id.btn_become_store, R.id.btn_invitation})
    public void onClickViews(View v) {
        switch (v.getId()) {
            case R.id.btn_become_store:
                if (UserInfoUtils.getInstance().isLogin())
                    getP().convertStore();
                else
                    getP().oauth(reference.get());
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
        }
    }


    @Override
    protected void initData() {

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
    public void onHelpDataResult(HelpBean helpBean) {

    }

    @Override
    public void onShareUrlResult(ShareBean sb) {

        shareBean = sb;
    }
}
