package com.gxdingo.sg;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.net.http.HttpResponseCache;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.multidex.MultiDex;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.amap.api.location.AMapLocationClient;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
//import com.gxdingo.sg.activity.ClientActivity;
import com.gxdingo.sg.http.Api;
import com.gxdingo.sg.http.ClientApi;
import com.gxdingo.sg.http.StoreApi;
import com.gxdingo.sg.utils.ClientLocalConstant;
import com.gxdingo.sg.utils.LocalConstant;
//import com.gxdingo.sg.view.NineGridGlideImageLoader;
import com.kikis.commnlibrary.utils.KikisUitls;
import com.kikis.commnlibrary.utils.ScreenUtils;
import com.lxj.xpopup.XPopup;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.ui.UILifecycleListener;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.converter.SerializableDiskConverter;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.cookie.CookieManger;
import com.zhouyou.http.model.HttpHeaders;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;

import io.reactivex.plugins.RxJavaPlugins;

import static cc.shinichi.library.tool.file.FileUtil.createOrExistsDir;
import static com.blankj.utilcode.util.AppUtils.getAppName;
import static com.blankj.utilcode.util.DeviceUtils.getUniqueDeviceId;
import static com.gxdingo.sg.http.Api.HTTP;
import static com.gxdingo.sg.http.Api.HTTPS;
import static com.gxdingo.sg.http.Api.IM_OFFICIAL_URL;
import static com.gxdingo.sg.http.Api.IM_TEST_URL;
import static com.gxdingo.sg.http.Api.IM_UAT_URL;
import static com.gxdingo.sg.http.Api.L;
import static com.gxdingo.sg.http.Api.OFFICIAL_OSS_UPLOAD_URL;
import static com.gxdingo.sg.http.Api.SM;
import static com.gxdingo.sg.http.Api.TEST_OSS_UPLOAD_URL;
import static com.gxdingo.sg.http.Api.isUat;
import static com.gxdingo.sg.http.ClientApi.CLIENT_PORT;
import static com.gxdingo.sg.http.ClientApi.OFFICIAL_WEB_URL;
import static com.gxdingo.sg.http.ClientApi.TEST_WEB_URL;
import static com.gxdingo.sg.http.ClientApi.UAT_URL;
import static com.gxdingo.sg.http.ClientApi.UAT_WEB_URL;
import static com.gxdingo.sg.http.StoreApi.STORE_PORT;
import static com.gxdingo.sg.utils.ClientLocalConstant.APP;
import static com.gxdingo.sg.utils.ClientLocalConstant.DEVICE;
import static com.gxdingo.sg.utils.ClientLocalConstant.YI_TARGET;
import static com.gxdingo.sg.utils.ClientLocalConstant.YI_VERSION;
import static com.gxdingo.sg.utils.ClientLocalConstant.YI_VERSION_NUMBER;
import static com.gxdingo.sg.utils.LocalConstant.CLIENT_OFFICIAL_HTTP_KEY;
import static com.gxdingo.sg.utils.LocalConstant.CLIENT_UAT_HTTP_KEY;
import static com.gxdingo.sg.utils.LocalConstant.IM_OFFICIAL_HTTP_KEY;
import static com.gxdingo.sg.utils.LocalConstant.IM_UAT_HTTP_KEY;
import static com.gxdingo.sg.utils.LocalConstant.LOGIN_WAY;
import static com.gxdingo.sg.utils.LocalConstant.OSS_KEY;
import static com.gxdingo.sg.utils.LocalConstant.STORE_OFFICIAL_HTTP_KEY;
import static com.gxdingo.sg.utils.LocalConstant.STORE_UAT_HTTP_KEY;
import static com.gxdingo.sg.utils.LocalConstant.TEST_HTTP_KEY;
import static com.gxdingo.sg.utils.LocalConstant.TEST_OSS_KEY;
import static com.gxdingo.sg.utils.LocalConstant.UAT_OSS_KEY;
import static com.kikis.commnlibrary.utils.CommonUtils.getPath;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.Constant.BUGLYAPPID;
import static com.kikis.commnlibrary.utils.Constant.isDebug;
import static com.kikis.commnlibrary.utils.KikisUitls.getContext;

/**
 * Created by Kikis on 2021/3/16.
 */

public class MyApplication extends Application {


    private static MyApplication instance;

    public static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        instance = this;
        //自用lib的初始化
        KikisUitls.Init(this);
        okHttpInit();
        keyInt();
        ScreenUtils.init(this);

        ZXingLibrary.initDisplayOpinion(this);

        //todo 需要增加隐私政策弹窗
        AMapLocationClient.updatePrivacyShow(this, true, true);
        AMapLocationClient.updatePrivacyAgree(this, true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onApplicationEvent(Integer type) {
        if (type == LocalConstant.CONSENT_AGREEMENT) {
            initGreenDao();
            xPopupInit();
            nineGridInit();
            rxInit();
            umengInit();
            buglyInit();
            tntX5Init();

            svgaCacheInit();
        }
    }

    /**
     * svga缓存
     */
    private void svgaCacheInit() {

        File cacheDir = new File(getPath() + "svga_cache");
        createOrExistsDir(cacheDir);
        try {
            HttpResponseCache.install(cacheDir, 1024 * 1024 * 128);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 九宫格控件初始化
     */
    private void nineGridInit() {

//        NineGridView.setImageLoader(new NineGridGlideImageLoader());
    }


    private void xPopupInit() {
        XPopup.setPrimaryColor(getc(R.color.pink_dominant_tone));
    }

    /**
     * 所需key初始化
     */
    private void keyInt() {

        boolean isUser = SPUtils.getInstance().getBoolean(LOGIN_WAY, true);

        //全局url初始化
        if (isUser) {
            //客户端
            LocalConstant.GLOBAL_SIGN = isUat ? CLIENT_UAT_HTTP_KEY : !isDebug ? CLIENT_OFFICIAL_HTTP_KEY : TEST_HTTP_KEY;
        } else {
            //商家端
            LocalConstant.GLOBAL_SIGN = isUat ? STORE_UAT_HTTP_KEY : !isDebug ? STORE_OFFICIAL_HTTP_KEY : TEST_HTTP_KEY;
        }

        Log.i("key", "keyInt: " + LocalConstant.GLOBAL_SIGN);
//
//        if (isUser) {
//            //客户端
//            LocalConstant.GLOBAL_SIGN = isUat ? CLIENT_UAT_HTTP_KEY : !isDebug ? CLIENT_UAT_HTTP_KEY : TEST_HTTP_KEY;
//        } else {
//            //商家端
//            LocalConstant.GLOBAL_SIGN = isUat ? STORE_UAT_HTTP_KEY : !isDebug ? STORE_UAT_HTTP_KEY : TEST_HTTP_KEY;
//        }
        LocalConstant.IM_SIGN = isUat ? IM_UAT_HTTP_KEY : !isDebug ? IM_OFFICIAL_HTTP_KEY : TEST_HTTP_KEY;

        LocalConstant.OSS_SIGN_KEY = isUat ? UAT_OSS_KEY : !isDebug ? OSS_KEY : TEST_OSS_KEY;

    }

    /**
     * tbs 腾讯x5内核
     */
    private void tntX5Init() {
        //非wifi情况下，主动下载x5内核
        QbSdk.setDownloadWithoutWifi(true);
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                LogUtils.d("开启TBS===X5加速成功");
            }

            @Override
            public void onCoreInitFinished() {
                LogUtils.d("开启TBS===X5加速失败");

            }
        };

        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }


    /**
     * greendao数据库框架初始化
     */
    private void initGreenDao() {
  /*      DaoManager mManager = DaoManager.getInstance();
        mManager.init(this);*/
    }


    /**
     * 注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调
     * 用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，
     * UMConfigure.init调用中appkey和channel参数请置为null）。
     */
    private void umengInit() {

        UMConfigure.init(getContext(), ClientLocalConstant.UMENG_APP_KEY, AnalyticsConfig.getChannel(getContext()), UMConfigure.DEVICE_TYPE_PHONE, null);
        //友盟相关平台配置。注意友盟官方新文档中没有这项配置，但是如果不配置会吊不起来相关平台的授权界面
//        PlatformConfig.setWeixin(Constant.WECHAT_APPID, Constant.WECHAT_APP_SECRET);//微信APPID和AppSecret
//        PlatformConfig.setAlipay(Constant.ALI_APPID);//ALIAPPID


        // 选用AUTO页面采集模式，如果是在AUTO页面采集模式下，则需要注意，所有Activity中都不能调用MobclickAgent.onResume和onPause方法
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

        //umeng日志输出
        UMConfigure.setLogEnabled(isDebug);

    }

    /**
     * Bugly异常统计初始化
     */
    private void buglyInit() {

        //使用异常上报功能的初始化。
//        CrashReport.initCrashReport(getApplicationContext(), BUGLYAPPID, !isDebug);
//        CrashReport.setAppChannel(this,AnalyticsConfig.getChannel(this));

       /* CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);

        strategy.setAppChannel(AnalyticsConfig.getChannel(this));*/

//        Beta.upgradeDialogLayoutId = R.layout.module_dialog_upgrade;

        /**
         *  如果想监听升级对话框的生命周期事件，可以通过设置OnUILifecycleListener接口
         *  回调参数解释：
         *  context - 当前弹窗上下文对象
         *  view - 升级对话框的根布局视图，可通过这个对象查找指定view控件
         *  upgradeInfo - 升级信息
         */
        Beta.upgradeDialogLifecycleListener = new UILifecycleListener<UpgradeInfo>() {
            @Override
            public void onCreate(Context context, View view, UpgradeInfo upgradeInfo) {

            }

            @Override
            public void onStart(Context context, View view, UpgradeInfo upgradeInfo) {
            }

            @Override
            public void onResume(Context context, View view, UpgradeInfo upgradeInfo) {
            }

            @Override
            public void onPause(Context context, View view, UpgradeInfo upgradeInfo) {
            }

            @Override
            public void onStop(Context context, View view, UpgradeInfo upgradeInfo) {
            }

            @Override
            public void onDestroy(Context context, View view, UpgradeInfo upgradeInfo) {
            }
        };
        //统一初始化方法。
        Bugly.init(getApplicationContext(), BUGLYAPPID, isDebug);

        /**
         * true表示app启动自动初始化升级模块; false不会自动初始化;
         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false，
         * 在后面某个时刻手动调用Beta.init(getApplicationContext(),false);
         */
        Beta.autoInit = true;
        /**
         * true表示初始化时自动检查升级; false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
         */
        Beta.autoCheckUpgrade = true;


//        Beta.tipsDialogLayoutId = R.layout.module_dialog_tips;

        /**
         * 设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);
         */
        Beta.upgradeCheckPeriod = 60 * 1000;

        /**
         * 设置启动延时为1s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
         */
        Beta.initDelay = 1 * 1000;

        /**
         * 已经确认过的弹窗在APP下次启动自动检查更新时会再次显示;
         */
        Beta.showInterruptedStrategy = true;

        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗; 不设置会默认所有activity都可以显示弹窗;
         */
//        Beta.canShowUpgradeActs.add(ClientActivity.class);

    }

    /**
     * 全局rxjava异常捕获
     */
    private void rxInit() {
        if (!isDebug) {
            RxJavaPlugins.setErrorHandler(throwable -> {
                LogUtils.e("RxJava Error === " + throwable);
                CrashReport.postCatchedException(throwable);
            });
        }
    }


    /**
     * RxHttpUtils 2.x 版本 初始化
     */
    private void okHttpInit() {

        boolean isUser = SPUtils.getInstance().getBoolean(LOGIN_WAY, true);
        //全局url初始化
        if (isUser) {
            //客户端
            Api.URL = isUat ? HTTP + UAT_URL : !isDebug ? HTTP + ClientApi.OFFICIAL_URL : HTTP + ClientApi.TEST_URL + SM + CLIENT_PORT + L;
//            Api.URL = HTTPS + ClientApi.OFFICIAL_URL;
            Api.OSS_URL = isUat ? HTTP + UAT_URL : !isDebug ? HTTP + OFFICIAL_OSS_UPLOAD_URL : HTTP + TEST_OSS_UPLOAD_URL;
//            Api.OSS_URL = HTTPS + OFFICIAL_OSS_UPLOAD_URL;
        } else {
            //商家端
            Api.URL = isUat ? HTTP + StoreApi.UAT_URL : !isDebug ? HTTP + StoreApi.OFFICIAL_URL : HTTP + StoreApi.TEST_URL + SM + STORE_PORT + L;
            Api.OSS_URL = isUat ? HTTP + ClientApi.UAT_URL : !isDebug ? HTTP + OFFICIAL_OSS_UPLOAD_URL : HTTP + TEST_OSS_UPLOAD_URL;
        }

//        if (isUat)
//            Api.IM_URL = HTTP + UAT_URL;
        Api.IM_URL = isUat ? HTTP + IM_UAT_URL : !isDebug ? HTTP + IM_OFFICIAL_URL : HTTP + IM_TEST_URL;

        //H5客服
        ClientApi.WEB_URL = isUat ? UAT_WEB_URL : !isDebug ? OFFICIAL_WEB_URL : TEST_WEB_URL;

        EasyHttp.init(this);//默认初始化

        // 打开该调试开关并设置TAG,不需要就不要加入该行
        // 最后的true表示是否打印内部异常，一般打开方便调试错误
        EasyHttp.getInstance();

        //全局设置请求头
        HttpHeaders headers = new HttpHeaders();

        headers.put(YI_VERSION, YI_VERSION_NUMBER);
        headers.put(YI_TARGET, APP);
        headers.put(DEVICE, getUniqueDeviceId());

/*        headers.put("User-Agent", SystemInfoUtils.getUserAgent(this, AppConstant.APPID));
        //全局设置请求参数A
        HttpParams params = new HttpParams();
        params.put("appId", AppConstant.APPID);*/

        //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
        EasyHttp.getInstance()
                //可以全局统一设置全局URL
                .setBaseUrl(Api.URL)//设置全局URL  url只能是域名 或者域名+端口号
                // 打开该调试开关并设置TAG,不需要就不要加入该行
                // 最后的true表示是否打印内部异常，一般打开方便调试错误
                .debug("Http - Logcat", isUat ? true : isDebug)
                //如果使用默认的60秒,以下三行也不需要设置
                .setReadTimeOut(60 * 1000)
                .setWriteTimeOut(60 * 100)
                .setConnectTimeout(60 * 100)
                //可以设置https的证书,以下几种方案根据需要自己设置
                //可以全局统一设置超时重连次数,默认为3次,那么最差的情况会请求4次(一次原始请求,三次重连请求),
                //不需要可以设置为0
                .setRetryCount(1)//网络不好自动重试1次
                //可以全局统一设置超时重试间隔时间,默认为500ms,不需要可以设置为0
                .setRetryDelay(500)//每次延时500ms重试
                //可以全局统一设置超时重试间隔叠加时间,默认为0ms不叠加
                .setRetryIncreaseDelay(500)//每次延时叠加500ms
                .setCookieStore(new CookieManger(getContext()))
                //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体请看CacheMode
                .setCacheMode(CacheMode.NO_CACHE)
                //可以全局统一设置缓存时间,默认永不过期
                .setCacheTime(-1)//-1表示永久缓存,单位:秒 ，Okhttp和自定义RxCache缓存都起作用
                //全局设置自定义缓存保存转换器，主要针对自定义RxCache缓存
                .setCacheDiskConverter(new SerializableDiskConverter())//默认缓存使用序列化转化
                //全局设置自定义缓存大小，默认50M
                .setCacheMaxSize(100 * 1024 * 1024)//设置缓存大小为100M
                //设置缓存版本，如果缓存有变化，修改版本后，缓存就不会被加载。特别是用于版本重大升级时缓存不能使用的情况
                .setCacheVersion(1)//缓存版本为1
                //.setHttpCache(new Cache())//设置Okhttp缓存，在缓存模式为DEFAULT才起作用
                //可以设置https的证书,以下几种方案根据需要自己设置
                .setCertificates()//方法一：信任所有证书,不安全有风险
                .addCommonHeaders(headers)
        //.setCertificates(new SafeTrustManager())            //方法二：自定义信任规则，校验服务端证书
        //配置https的域名匹配规则，不需要就不要加入，使用不当会导致https握手失败
        //.setHostnameVerifier(new SafeHostnameVerifier())
        //.addConverterFactory(GsonConverterFactory.create(gson))//本框架没有采用Retrofit的Gson转化，所以不用配置
        ;//设置全局公共头
//                .addCommonParams(params)//设置全局公共参数
        //.addNetworkInterceptor(new NoCacheInterceptor())//设置网络拦截器
        //.setCallFactory()//局设置Retrofit对象Factory
        //.setCookieStore()//设置cookie
        //.setOkproxy()//设置全局代理
        //.setOkconnectionPool()//设置请求连接池
        //.setCallbackExecutor()//全局设置Retrofit callbackExecutor
        //可以添加全局拦截器，不需要就不要加入，错误写法直接导致任何回调不执行
        //.addInterceptor(new GzipRequestInterceptor())//开启post数据进行gzip后发送给服务器
//                .addInterceptor(new CustomSignInterceptor());//添加参数签名拦截器

    }


    /**
     * 初始化云推送通道
     *
     * @param applicationContext
     */
    private void initCloudChannel(MyApplication applicationContext) {

        try {

            createNotificationChannel();

            PushServiceFactory.init(applicationContext);
            CloudPushService pushService = PushServiceFactory.getCloudPushService();

            if (pushService == null)
                return;

            pushService.register(applicationContext, new CommonCallback() {
                @Override
                public void onSuccess(String response) {
                    String deviceId = PushServiceFactory.getCloudPushService().getDeviceId();
                    LogUtils.w("init cloudchannel success deviceId ==== " + deviceId);
                }

                @Override
                public void onFailed(String errorCode, String errorMessage) {
                    LogUtils.w("init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
                }
            });

            auxiliaryChannelInit();

        } catch (Exception e) {
            LogUtils.e("initCloudChannel error  == " + e);
        }
    }

    /**
     * 8.0以上弹窗
     */
    private void createNotificationChannel() {

        //8.0及其以上的设配设置NotificaitonChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // 通知渠道的id
            String id = "1";//这个是与后台约定好的，要不收不到，该方法主要是适配Android 8.0以上，避免接收不到通知
            // 用户可以看到的通知渠道的名字.
            CharSequence name = getAppName();
            // 用户可以看到的通知渠道的描述
            String description = "通知";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // 配置通知渠道的属性
            mChannel.setDescription(description);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            mChannel.enableLights(false);
            mChannel.setLightColor(Color.RED);

            //取消震动
            mChannel.enableVibration(false);
            mChannel.setVibrationPattern(new long[]{0});
            // 设置通知出现时的震动（如果 android 设备支持的话）
//            mChannel.enableVibration(true);
//            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            //最后在notificationmanager中创建该通知渠道
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    /**
     * 辅助通道初始化
     */
    private void auxiliaryChannelInit() {

    /*    //小米辅助推送通道注册（如不支持会跳过）
        MiPushRegister.register(this, MI_APPID, MI_APP_KEY);
        //华为辅助推送通道注册（如不支持会跳过）
        HuaWeiRegister.register(this);
        // OPPO辅助通道注册
        OppoRegister.register(this, OPPO_APPKEY, OPPO_MASTERSECRET); // appKey/appSecret在OPPO开发者平台获取*/

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
