package com.kikis.commnlibrary.utils;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.LogUtils;

import static com.blankj.utilcode.util.AppUtils.getAppPackageName;
import static com.blankj.utilcode.util.AppUtils.isAppDebug;
import static com.blankj.utilcode.util.LogUtils.getConfig;


/**
 * Created by lian on 2019/11/15.
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //    ┃　　　┃   神兽保佑
 * //    ┃　　　┃   代码无BUG！
 * //    ┃　　　┗━━━┓
 * //    ┃　　　　　　　┣┓
 * //    ┃　　　　　　　┏┛
 * //    ┗┓┓┏━┳┓┏┛
 * //      ┃┫┫　┃┫┫
 * //      ┗┻┛　┗┻┛
 */

public class KikisUitls {

    private static Application context;

    public static void Init(Application app) {
        context = app;
        AppStatuejudgement();
        LogInit();
    }

    /**
     * LogUtils  配置 初始化
     * <p>
     * getConfig                : 获取 log 配置
     * Config.setLogSwitch      : 设置 log 总开关
     * Config.setConsoleSwitch  : 设置 log 控制台开关
     * Config.setGlobalTag      : 设置 log 全局 tag
     * Config.setLogHeadSwitch  : 设置 log 头部信息开关
     * Config.setLog2FileSwitch : 设置 log 文件开关
     * Config.setDir            : 设置 log 文件存储目录
     * Config.setFilePrefix     : 设置 log 文件前缀
     * Config.setBorderSwitch   : 设置 log 边框开关
     * Config.setSingleTagSwitch: 设置 log 单一 tag 开关（为美化 AS 3.1 的 Logcat）
     * Config.setConsoleFilter  : 设置 log 控制台过滤器
     * Config.setFileFilter     : 设置 log 文件过滤器
     * Config.setStackDeep      : 设置 log 栈深度
     * Config.setStackOffset    : 设置 log 栈偏移
     * Config.setSaveDays       : 设置 log 可保留天数
     */
    private static void LogInit() {
        LogUtils.Config config = getConfig();
        //debug自动开启日志
        config.setLogSwitch(Constant.isDebug);
    }

    /**
     * 根据App状态自动修改debug release标识符;
     */
    private static void AppStatuejudgement() {
        Constant.isDebug = isAppDebug(getAppPackageName());
        LogUtils.i("app is debug  == " + Constant.isDebug);
    }


    public static Context getContext() {
        return context.getApplicationContext();
    }

}
