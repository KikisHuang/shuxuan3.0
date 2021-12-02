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
import com.gxdingo.sg.bean.UserBean;
import com.gxdingo.sg.bean.WebBean;
import com.gxdingo.sg.biz.WebContract;
import com.gxdingo.sg.biz.WebViewLoadingListener;
import com.gxdingo.sg.presenter.WebPresenter;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.gxdingo.sg.view.MyWebChromeClient;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.utils.GsonUtil;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.socialize.UMShareAPI;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.blankj.utilcode.util.AppUtils.getAppVersionName;
import static com.blankj.utilcode.util.AppUtils.isAppInstalled;
import static com.blankj.utilcode.util.AppUtils.launchApp;
import static com.blankj.utilcode.util.StringUtils.isEmpty;
import static com.kikis.commnlibrary.utils.Constant.isDebug;


/**
 * Created by Kikis on 2021/5/21
 * web文章页面
 */

public class WebActivity extends BaseMvpActivity<WebContract.WebPresenter> implements WebViewLoadingListener, WebContract.WebListener {

    @BindView(R.id.wb_article)
    public WebView webView;

    //是否文章
    private boolean mIsArticle = false;

    private String mUrl;

    @Override
    protected boolean eventBusRegister() {
        return true;
    }

    @Override
    protected int activityTitleLayout() {
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

        // 修改ua使得web端正确判断
        String ua = webView.getSettings().getUserAgentString();

        webView.getSettings().setUserAgentString(ua + "ShuGou/" + getAppVersionName());

        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setDomStorageEnabled(true);


        webView.getSettings().setJavaScriptEnabled(true);

        webView.addJavascriptInterface(this, "callAndroid");

        MyWebChromeClient myWebChromeClient = new MyWebChromeClient();

        myWebChromeClient.setListener(this);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); // 设置支持js的弹窗
        webView.getSettings().setAllowFileAccess(true); // 设置可以访问文件
        webView.getSettings().setSupportMultipleWindows(false); // 设置可以访问文件
        webView.setWebChromeClient(myWebChromeClient);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                //修复正式版默认只能识别http和https, 连系统自带的tel://都无法识别的问题
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


    /**
     * 分享支付
     *
     * @param url         分享路径
     * @param title       标题
     * @param description 描述
     * @param thumb       缩略图
     */
    @JavascriptInterface
    public void sharePayment(String url, String title, String description, String thumb) {
        getP().sharePayment(url, title, description, thumb);
    }

    /**
     * 跳转其他app
     *
     * @return
     */
    @JavascriptInterface
    public void goToApp(String packname) {

        if (isAppInstalled(packname))
            launchApp(packname);
        else
            ToastUtils.showShort("未安装该应用");
    }

    /**
     * 投诉
     *
     * @return
     */
    @JavascriptInterface
    public void goToPage(String type) {
//        if (type.equals(ClientLocalConstant.COMPLAIN))
//            IntentUtils.goToPage(reference.get(), ClientComplainActivity.class, null);
    }


    /**
     * 获取token
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
     * 关闭页面
     */
    @JavascriptInterface
    public void goBack() {
        finish();
    }

    @OnClick({})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;
        switch (v.getId()) {

        }

    }


    @JavascriptInterface
    public void callJsBye() {
        webView.evaluateJavascript("javascript:byeShuGou()", s -> LogUtils.e("onReceiveValue ==== " + s));
    }


    @Override
    protected void onStart() {
        super.onStart();


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
        else
            onAfters();
    }

    @Override
    public void onShowFileChooser(ValueCallback<Uri[]> valueCallback, int mode) {
        getP().openPhoto(valueCallback, mode);
    }

    @Override
    public void loadWebUrl(WebBean webBean) {
        if (!isEmpty(webBean.getContent())) {
//            webView.loadUrl(webBean.getContent());
            webView.loadDataWithBaseURL(null, webBean.getContent(), "text/html", "utf-8", null);
        } else {
            onMessage("没有获取当文章详情");
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
        super.onDestroy();
        if (webView != null) {
            callJsBye();
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearCache(true);
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webView.getSettings().setJavaScriptEnabled(false);
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.clearHistory();
            webView.clearView();
            webView.destroy();
        }
    }
}