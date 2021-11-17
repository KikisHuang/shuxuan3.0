# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\studiosdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#指定代码的压缩级别
-optimizationpasses 5

#包明不混合大小写
-dontusemixedcaseclassnames

#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses

 #优化  不优化输入的类文件
-dontoptimize

 #预校验
-dontpreverify

 #混淆时是否记录日志
-verbose

 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#保护注解
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*

# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
#如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment
#防止R文件被混淆
-keep class **.R$* {
*;
}

#忽略警告
-ignorewarnings


##记录生成的日志数据,gradle build时在本项目根目录输出##
#apk 包内所有 class 的内部结构
-dump proguard/class_files.txt
#未混淆的类和成员
-printseeds proguard/seeds.txt
#列出从 apk 中删除的代码
-printusage proguard/unused.txt
#混淆前后的映射
-printmapping proguard/mapping.txt
########记录生成的日志数据，gradle build时 在本项目根目录输出-end######

#如果引用了v4或者v7包
-dontwarn android.support.v4.**
-dontwarn android.support.v7.**

####混淆保护自己项目的部分代码以及引用的第三方jar包library-end####

####腾讯内核X5####
#-optimizationpasses 7
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-dontoptimize
-dontusemixedcaseclassnames
-verbose
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontwarn dalvik.**
#-overloadaggressively

#@proguard_debug_start
# ------------------ Keep LineNumbers and properties ---------------- #
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
-renamesourcefileattribute TbsSdkJava,SourceFile
-keepattributes SourceFile,LineNumberTable
#@proguard_debug_end

# --------------------------------------------------------------------------
# Addidional for x5.sdk classes for apps

-dontwarn dalvik.**
-dontwarn com.tencent.smtt.**

-keep class com.tencent.smtt.** {
    *;
}

-keep class com.tencent.tbs.** {
    *;
}

-keep class com.tencent.smtt.export.external.**{
    *;
}

#---------------------------------------------------------------------------


#------------------  下方是android平台自带的排除项，这里不要动         ----------------

-keep public class * extends android.app.Activity{
	public <fields>;
	public <methods>;
}
-keep public class * extends android.app.Application{
	public <fields>;
	public <methods>;
}
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keepclasseswithmembers class * {
	public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
	public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepattributes *Annotation*
-keepclasseswithmembernames class *{
	native <methods>;
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#------------------  下方是共性的排除项目         ----------------
# 方法名中含有“JNI”字符的，认定是Java Native Interface方法，自动排除
# 方法名中含有“JRI”字符的，认定是Java Reflection Interface方法，自动排除
-keepclasseswithmembers class * {
    ... *JNI*(...);
}
-keepclasseswithmembernames class * {
	... *JRI*(...);
}
-keep class **JNI* {*;}
####腾讯内核X5####

#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}
####LogInterceptor####
-dontwarn com.parkingwang.okhttp3.LogInterceptor.formatter.*

# banner 的混淆代码
-keep class com.youth.banner.** {
    *;
 }

####TabLayout####
-keep class android.support.design.widget.TabLayout{*;}
####TabLayout####
####avi####
-keep class com.wang.avi.** { *; }
-keep class com.wang.avi.indicators.** { *; }
####avi####
#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#保持枚举 enum 类不被混淆
-keepclassmembers enum * {
  public static **[] values();
  public static ** valueOf(java.lang.String);
}

-keepclassmembers class * {
    public void *ButtonClicked(android.view.View);
}

#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}


#gsyvideoplayer
-keep class com.shuyu.gsyvideoplayer.video.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.video.**
-keep class com.shuyu.gsyvideoplayer.video.base.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.video.base.**
-keep class com.shuyu.gsyvideoplayer.utils.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.utils.**
-keep class tv.danmaku.ijk.** { *; }
-dontwarn tv.danmaku.ijk.**

-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}


####design####
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }


####bugly####
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

####photoview####
-keep class uk.co.senab.photoview.** { *; }


####RxGalleryFinal####
#1.support-v7-appcompat
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

####ijkplayer####
-keep class tv.danmaku.ijk.media.**{*;}
####ijkplayer####

#2.rx
-dontwarn io.reactivex.**
-keep class io.reactivex.** { *; }
-keep interface io.reactivex.** { *; }
-keepclassmembers class io.reactivex.** { *; }

#3.retrolambda
-dontwarn java.lang.invoke.*

#4.support-v4
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }

#5.ucrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

#6.photoview
-keep class uk.co.senab.photoview** { *; }
-keep interface uk.co.senab.photoview** { *; }

####SwipeRecyclerView####
-keepclasseswithmembers class androidx.recyclerview.widget.RecyclerView$ViewHolder {
   public final View *;
}
-dontwarn com.yanzhenjie.recyclerview.swipe.**
-keep class com.yanzhenjie.recyclerview.swipe.** {*;}
####SwipeRecyclerView####


# protobuf（jpush依赖）
-dontwarn com.google.**
-keep class com.google.protobuf.** {*;}


####kikislibrary####

####LogInterceptor####
-dontwarn com.parkingwang.okhttp3.LogInterceptor.formatter.*
####LogInterceptor####

#okhttp
-keep class okhttp3.**{*;}
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**
-keep class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

####EventBus####
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
####EventBus####

####WebSocket####
-keep public class org.java_websocket.** { *; }
####WebSocket####

-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

####Glide####
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-dontwarn com.bumptech.glide.load.resource.bitmap.VideoDecoder
#-keepnames class com.mypackage.MyGlideModule
# or more generally:
#-keep public class * implements com.bumptech.glide.module.GlideModule

####Glide####

####gson####
##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }  ##这里需要改成解析到哪个  javabean

##---------------End: proguard configuration for Gson  ----------

-keep class com.google.gson.stream.** { *; }
-keepattributes EnclosingMethod
-keep class org.xz_sale.entity.**{*;}
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }

#实体类混淆
-keep class com.github.mikephil.charting.**{*;}
-keep class com.kikis.commnlibrary.bean.**{*;}
-keep class com.gxdingo.sg.bean.**{*;}
-keep class com.gxdingo.sg.utils.ThirdPartyMapsGuide{*;}


####ali push####
-keepclasseswithmembernames class ** {
    native <methods>;
}
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.taobao.** {*;}
-keep class com.alibaba.** {*;}
-keep class com.alipay.** {*;}
-keep class com.ut.** {*;}
-keep class com.ta.** {*;}
-keep class anet.**{*;}
-keep class anetwork.**{*;}
-keep class org.android.spdy.**{*;}
-keep class org.android.agoo.**{*;}
-keep class android.os.**{*;}
-keep class org.json.**{*;}
-dontwarn com.taobao.**
-dontwarn com.alibaba.**
-dontwarn com.alipay.**
-dontwarn anet.**
-dontwarn org.android.spdy.**
-dontwarn org.android.agoo.**
-dontwarn anetwork.**
-dontwarn com.ut.**
-dontwarn com.ta.**

####gson####


#PictureSelector 2.0
-keep class com.luck.picture.lib.** { *; }

-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }


#RxEasyHttp
-keep class com.zhouyou.http.model.** {*;}
-keep class com.zhouyou.http.cache.model.** {*;}
-keep class com.zhouyou.http.cache.stategy.**{*;}
-keep class com.zhouyou.http.callback.**{*;}
-keep class com.zhouyou.http.exception.**{*;}
-keep class com.zhouyou.http.*{*;}
#自定义的customapi也要避免混淆
-keep class com.pabei.farm.http.CustomApiResult{*;}


####design####
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

####腾讯地图####
-keep class com.tencent.tencentmap.**{*;}
-keep class com.tencent.map.**{*;}
-keep class com.tencent.beacontmap.**{*;}
-keep class navsns.**{*;}
-dontwarn com.qq.**
-dontwarn com.tencent.**

-keepclassmembers class ** {
    public void on*Event(...);
}

-keep class c.t.**{*;}
-keep class com.tencent.map.geolocation.**{*;}

-keep public class com.tencent.location.**{
    public protected *;
}
-keepclasseswithmembernames class * {
    native <methods>;
}

-dontwarn  org.eclipse.jdt.annotation.**
-dontwarn  c.t.**
####腾讯地图####


 #rxjava
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
 long producerIndex;
 long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Exceptions

# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
###rxandroid-1.2.1
-keepclassmembers class rx.android.**{*;}

###svga
-keep class com.squareup.wire.** { *; }
-keep class com.opensource.svgaplayer.proto.** { *; }

####kikislibrary####

####友盟统计####
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class [com.xk.sq].R$*{
public static final int *;
}

####butterknife####
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

####腾讯地图定位####
-keepclassmembers class ** {
    public void on*Event(...);
}
-keep class c.t.**{*;}
-keep class com.tencent.map.geolocation.**{*;}


-dontwarn  org.eclipse.jdt.annotation.**
-dontwarn  c.t.**


####友盟####
-dontusemixedcaseclassnames
    -dontshrink
    -dontoptimize
    -dontwarn com.google.android.maps.**
    -dontwarn android.webkit.WebView
    -dontwarn com.umeng.**
    -dontwarn com.tencent.weibo.sdk.**
    -dontwarn com.facebook.**
    -keep public class javax.**
    -keep public class android.webkit.**
    -keep class com.umeng.commonsdk.** {*;}

    -dontwarn android.support.v4.**
    -keep enum com.facebook.**
    -keepattributes Exceptions,InnerClasses,Signature

    -keepattributes SourceFile,LineNumberTable

    -keep public interface com.facebook.**
    -keep public interface com.tencent.**
    -keep public interface com.umeng.socialize.**
    -keep public interface com.umeng.socialize.sensor.**
    -keep public interface com.umeng.scrshot.**
    -keep class com.android.dingtalk.share.ddsharemodule.** { *; }
    -keep public class com.umeng.socialize.* {*;}

    -keep class com.facebook.**
    -keep class com.facebook.** { *; }
    -keep class com.umeng.scrshot.**
    -keep public class com.tencent.** {*;}
    -keep class com.umeng.socialize.sensor.**
    -keep class com.umeng.socialize.handler.**
    -keep class com.umeng.socialize.handler.*
    -keep class com.umeng.weixin.handler.**
    -keep class com.umeng.weixin.handler.*
    -keep class com.umeng.qq.handler.**
    -keep class com.umeng.qq.handler.*
    -keep class UMMoreHandler{*;}
    -keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
    -keep class com.tencent.mm.sdk.modelmsg.** implements   com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
    -keep class im.yixin.sdk.api.YXMessage {*;}
    -keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
    -keep class com.tencent.mm.sdk.** {
     *;
    }

    -keep class com.tencent.wxop.** {
    *;
    }
    -keep class com.tencent.mm.opensdk.** {
   *;
    }
    -dontwarn twitter4j.**
    -keep class twitter4j.** { *; }

    -keep class com.tencent.** {*;}
    -dontwarn com.tencent.**
    -keep public class com.umeng.com.umeng.soexample.R$*{
    public static final int *;
    }
    -keep public class com.linkedin.android.mobilesdk.R$*{
    public static final int *;
        }
    -keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    }

    -keep class com.tencent.open.TDialog$*
    -keep class com.tencent.open.TDialog$* {*;}
    -keep class com.tencent.open.PKDialog
    -keep class com.tencent.open.PKDialog {*;}
    -keep class com.tencent.open.PKDialog$*
    -keep class com.tencent.open.PKDialog$* {*;}

    -keep class com.sina.** {*;}
    -dontwarn com.sina.**
    -keep class  com.alipay.share.sdk.** {
       *;
    }
    -keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
    }

    -keep class com.linkedin.** { *; }
    -keepattributes Signature


-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**


-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class org.apache.thrift.** {*;}

-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}

#Flutter Wrapper
-keep class io.flutter.app.** { *; }
-keep class io.flutter.plugin.**  { *; }
-keep class io.flutter.util.**  { *; }
-keep class io.flutter.view.**  { *; }
-keep class io.flutter.**  { *; }
-keep class io.flutter.plugins.**  { *; }
#高德地图SDK配置
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.**{*;}
-keep   class com.amap.api.trace.**{*;}
#高德等位SDK配置
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.loc.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

####抖音混淆规则####
-keep class com.bytedance.** { *;}
-keep class com.bytedance.sdk.open.aweme** {*;}
-keep class com.bytedance.sdk.open.douyin** {*;}
-keep class com.bytedance.sdk.open.douyin.ui** {*;}


-keep class com.pabei.farm.douyinapi.DouYinEntryActivity {*;}
#自定义的DouyinCustomApiResult也要避免混淆
-keep class com.pabei.farm.http.DouyinCustomApiResult{*;}

####一键登录混淆规则####
##若开启资源混淆，需要配置whiteList 1 "R.drawable.authsdk*", 2 "R.layout.authsdk*", 3 "R.anim.authsdk*", 4 "R.id.authsdk*", 5 "R.string.authsdk*", 6 "R.style.authsdk*",
-keep class cn.com.chinatelecom.gateway.lib.** {*;}
-keep class com.unicom.xiaowo.login.** {*;}
-keep class com.cmic.sso.sdk.** {*;}
-keep class com.mobile.auth.gatewayauth.** {*;}
-keep class android.support.v4.** { *;}
-keep class org.json.**{*;}
-keep class com.alibaba.fastjson.** {*;}


-keepclassmembers class ** {
     @com.squareup.otto.Subscribe public *;
     @com.squareup.otto.Produce public *;
}

-keep class com.alipay.mobile.android.verify.** { *; }

####AndroidUtilCode####
-keep class com.blankj.utilcode.** {*;}

####ImmersionBar####
 -keep class com.gyf.immersionbar.* {*;}
 -dontwarn com.gyf.immersionbar.**



####阿里移动推送####
 -keepclasseswithmembernames class ** {
        native <methods>;
    }
    -keepattributes Signature
    -keep class sun.misc.Unsafe { *; }
    -keep class com.taobao.** {*;}
    -keep class com.alibaba.** {*;}
    -keep class com.alipay.** {*;}
    -keep class com.ut.** {*;}
    -keep class com.ta.** {*;}
    -keep class anet.**{*;}
    -keep class anetwork.**{*;}
    -keep class org.android.spdy.**{*;}
    -keep class org.android.agoo.**{*;}
    -keep class android.os.**{*;}
    -dontwarn com.taobao.**
    -dontwarn com.alibaba.**
    -dontwarn com.alipay.**
    -dontwarn anet.**
    -dontwarn org.android.spdy.**
    -dontwarn org.android.agoo.**
    -dontwarn anetwork.**
    -dontwarn com.ut.**
    -dontwarn com.ta.**

#推送辅助通道
# 小米通道
-keep class com.xiaomi.** {*;}
-dontwarn com.xiaomi.**
# 华为通道
-keep class com.huawei.** {*;}
-dontwarn com.huawei.**
# GCM/FCM通道
-keep class com.google.firebase.**{*;}
-dontwarn com.google.firebase.**
# OPPO通道
-keep public class * extends android.app.Service
# VIVO通道
-keep class com.vivo.** {*;}
-dontwarn com.vivo.**
# 魅族通道
-keep class com.meizu.cloud.** {*;}
-dontwarn com.meizu.cloud.**


####支付宝混淆规则####
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.sdk.app.H5PayCallback {
    <fields>;
    <methods>;
}
-keep class com.alipay.android.phone.mrpc.core.** { *; }
-keep class com.alipay.apmobilesecuritysdk.** { *; }
-keep class com.alipay.mobile.framework.service.annotation.** { *; }
-keep class com.alipay.mobilesecuritysdk.face.** { *; }
-keep class com.alipay.tscenter.biz.rpc.** { *; }
-keep class org.json.alipay.** { *; }
-keep class com.alipay.tscenter.** { *; }
-keep class com.ta.utdid2.** { *;}
-keep class com.ut.device.** { *;}

#避免混淆泛型 如果混淆报错建议关掉
#-keepattributes Signature

#移除Log类打印各个等级日志的代码，打正式包的时候可以做为禁log使用，这里可以作为禁止log打印的功能使用，另外的一种实现方案是通过BuildConfig.DEBUG的变量来控制
-assumenosideeffects class android.util.Log {
    public static *** v(...);
    public static *** i(...);
    public static *** d(...);
    public static *** w(...);
    public static *** e(...);
}
