package com.gxdingo.sg.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.ActivityEvent;
import com.gxdingo.sg.bean.UserBean;
import com.gxdingo.sg.bean.WebBean;
import com.gxdingo.sg.bean.WheelResultBean;
import com.gxdingo.sg.biz.WebContract;
import com.gxdingo.sg.biz.WebViewLoadingListener;
import com.gxdingo.sg.presenter.WebPresenter;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.ShareUtils;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.view.MyWebChromeClient;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.utils.GsonUtil;
import com.kikis.commnlibrary.utils.IntentUtils;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.blankj.utilcode.util.AppUtils.getAppVersionName;
import static com.blankj.utilcode.util.AppUtils.isAppInstalled;
import static com.blankj.utilcode.util.AppUtils.launchApp;
import static com.blankj.utilcode.util.PermissionUtils.isGranted;
import static com.blankj.utilcode.util.StringUtils.isEmpty;
import static com.kikis.commnlibrary.utils.Constant.failure;
import static com.kikis.commnlibrary.utils.Constant.isDebug;


/**
 * Created by Kikis on 2021/5/21
 * web????????????
 */

public class WebActivity extends BaseMvpActivity<WebContract.WebPresenter> implements WebViewLoadingListener, WebContract.WebListener {

    @BindView(R.id.wb_article)
    public WebView webView;

    //????????????
    private boolean mIsArticle = false;
    //??????????????????
    private boolean mShowTitle = false;

    private String mUrl;

    @Override
    protected boolean eventBusRegister() {
        return true;
    }

    @Override
    protected int activityTitleLayout() {
        mShowTitle = getIntent().getBooleanExtra(Constant.SERIALIZABLE + 3, false);

        if (mShowTitle)
            return R.layout.module_include_custom_title;

        mIsArticle = getIntent().getBooleanExtra(Constant.SERIALIZABLE + 0, false);
        return mIsArticle ? R.layout.module_include_custom_title : 0;
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
        return R.layout.module_activity_article_web;
    }

    @Override
    protected WebContract.WebPresenter createPresenter() {
        return new WebPresenter();
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
        webInit();
    }


    private void webInit() {


        webView.getSettings().setBlockNetworkImage(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        // ??????ua??????web???????????????
        String ua = webView.getSettings().getUserAgentString();

        webView.getSettings().setUserAgentString(ua + "ShuGou/" + getAppVersionName());

        webView.getSettings().setDefaultTextEncodingName("utf-8");

        webView.getSettings().setDomStorageEnabled(true);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.addJavascriptInterface(this, "callAndroid");

        MyWebChromeClient myWebChromeClient = new MyWebChromeClient();

        myWebChromeClient.setListener(this);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); // ????????????js?????????
        webView.getSettings().setAllowFileAccess(true); // ????????????????????????
        webView.getSettings().setSupportMultipleWindows(false); // ????????????????????????
        webView.setWebChromeClient(myWebChromeClient);
        webView.getSettings().setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler handler, SslError error) {
                //????????????
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                //?????????????????????????????????http???https, ??????????????????tel://????????????????????????
                try {
                    if (!url.startsWith("http:") || !url.startsWith("https:")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) {
                    return false;
                }

                webView.loadUrl(url);
                return true;
            }
        });


        if (!mIsArticle) {

            mUrl = getIntent().getStringExtra(Constant.SERIALIZABLE + 1);

            if (isDebug)
                LogUtils.w("Web url === " + mUrl);

            webView.loadUrl(mUrl);
        }
    }

    @Override
    protected void initData() {
        if (mIsArticle) {
            int articleId = getIntent().getIntExtra(Constant.SERIALIZABLE + 1, 0);

            String identifier = getIntent().getStringExtra(Constant.SERIALIZABLE + 2);
            getP().getArticleDetail(articleId, identifier);
        }
    }


    @OnClick({})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;
        switch (v.getId()) {

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (webView != null)
            webView.reload();

    }

    @Override
    public void onSucceed(int type) {
        super.onSucceed(type);

    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);

    }

    @Override
    public void onStarts() {
        super.onStarts();
    }


    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);

    }

    @Override
    public void onLoading(int progress) {
        if (progress < 100)
            onStarts();
        else {
            onAfters();
            if (UserInfoUtils.getInstance().isLogin() && UserInfoUtils.getInstance().getUserInfo().getRole() == 10)
                getP().upLoadRegionCode(LocalConstant.AdCode);
        }
    }

    @Override
    public void onShowFileChooser(ValueCallback<Uri[]> valueCallback, int mode) {
        getP().openPhoto(valueCallback, mode);
    }

    @Override
    public void loadWebUrl(WebBean webBean) {
        if (!isEmpty(webBean.getContent())) {
            // webView.loadUrl(webBean.getContent());
            webView.loadDataWithBaseURL(null, webBean.getContent(), "text/html", "utf-8", null);
        } else {
            onMessage("???????????????????????????");
            finish();
        }

    }

    @Override
    public void onArticleListResult(List<WebBean> webBeans) {

    }

    @Override
    public void uploadImage(ValueCallback<Uri[]> valueCallback, Uri uri) {
        if (valueCallback != null) {
            if (uri != null) {
                if (uri != null) {
                    valueCallback.onReceiveValue(new Uri[]{uri});
                } else {
                    valueCallback.onReceiveValue(null);
                }
            } else
                valueCallback.onReceiveValue(null);

            valueCallback = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(reference.get()).onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        if (LocalConstant.IS_CONTEACT_SERVER)
            LocalConstant.IS_CONTEACT_SERVER = false;
        if (webView != null) {
            callJsBye();
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearCache(true);
            // ????????????????????????????????????????????????????????????????????????????????????
            webView.getSettings().setJavaScriptEnabled(false);
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.clearHistory();
            webView.clearView();
            webView.destroy();
        }
        getP().clearCache();
        super.onDestroy();
    }

    /**
     * ########################### js??????Android?????? ###########################
     */


    /**
     * ????????????
     *
     * @param url         ????????????
     * @param title       ??????
     * @param description ??????
     * @param thumb       ?????????
     */
    @JavascriptInterface
    public void sharePayment(String url, String title, String description, String thumb) {
        getP().sharePayment(url, title, description, thumb);
    }

    /**
     * ????????????app
     *
     * @return
     */
    @JavascriptInterface
    public void goToApp(String packname) {

        if (isAppInstalled(packname))
            launchApp(packname);
        else
            ToastUtils.showShort("??????????????????");
    }

    /**
     * ??????
     *
     * @return
     */
    @JavascriptInterface
    public void goToPage(String type) {
//        if (type.equals(ClientLocalConstant.COMPLAIN))
//            IntentUtils.goToPage(reference.get(), ClientComplainActivity.class, null);
    }


    /**
     * ??????token
     *
     * @return
     */
    @JavascriptInterface
    public String getSXYGLoginInfo20211021() {
        UserBean userBean = UserInfoUtils.getInstance().getUserInfo();

        String userInfo = GsonUtil.gsonToStr(userBean);
        if (userBean == null || isEmpty(userInfo)) {
            UserInfoUtils.getInstance().goToOauthPage(reference.get());
            finish();
        }
        return userInfo;
    }

    /**
     * ??????app??????????????????
     *
     * @return
     */
    @JavascriptInterface
    public String getRegionCode() {
        return getP().getUserLocationInfo();
    }

    @JavascriptInterface
    public void backToApp(String restultData) {

        if (!checkClickInterval(100))
            return;

        LogUtils.d("h5?????????" + restultData);
        WheelResultBean wheelResultBean = GsonUtil.GsonToBean(restultData, WheelResultBean.class);
        if (wheelResultBean == null) return;

        if (wheelResultBean.type == 20 || wheelResultBean.type == 21) {

            if (wheelResultBean.jumpType == 30) {
                ShareUtils.UmShare(this, new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        LogUtils.d("onStart:" + share_media);
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        LogUtils.d("onResult:" + share_media);

                        getP().completeTask(wheelResultBean.activityIdentifier);
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        LogUtils.d("onError:" + share_media);
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        LogUtils.d("onCancel:" + share_media);
                    }
                }, wheelResultBean.url, wheelResultBean.title, wheelResultBean.describe, R.mipmap.ic_app_logo, SHARE_MEDIA.WEIXIN_CIRCLE);
            } else if (wheelResultBean.jumpType == 20) {
                ShareUtils.UmShare(this, new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {

                    }
                }, wheelResultBean.url, wheelResultBean.title, wheelResultBean.describe, R.mipmap.ic_app_logo, SHARE_MEDIA.WEIXIN);

            } else if (wheelResultBean.jumpType == 10) {
                sendEvent(new ActivityEvent(wheelResultBean.activityIdentifier, wheelResultBean.type));
                finish();
            }
        } else if (wheelResultBean.type == 30) {
            IntentUtils.goToPage(reference.get(), ClientSettleActivity.class, null);
            finish();
        }


    }

    /**
     * ????????????
     */
    @JavascriptInterface
    public void goBack() {
        finish();
    }


    @JavascriptInterface
    public void callJsBye() {
        webView.evaluateJavascript("javascript:byeShuGou()", s -> LogUtils.i("onReceiveValue ==== " + s));
    }

}