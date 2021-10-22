package com.gxdingo.sg.http;

/**
 * @author: Kikis
 * @date: 2021/5/13
 * @page:
 */
public class Api {

    public static String URL;
    //uat服务器开关
    public static final boolean isUat = true;
    
    public static final String SM = ":";

    public static final String L = "/";

    public static final String HTTPS = "https://";

    public static final String HTTP = "http://";

    public static final String HIERARCHY = "";

    //测试oss上传路径
    public static final String TEST_OSS_UPLOAD_URL = "192.168.110.248:8080/";
    //正式oss上传路径
    public static final String OFFICIAL_OSS_UPLOAD_URL = "shuxuan.gxdingo.com/app/";

    //uat oss上传路径
    public static final String UAT_OSS_UPLOAD_URL = "uat.gxdingo.com/public/";

    //im聊天url
    public static  String IM_URL = HTTP + "192.168.110.248:8080/";

    //oss路径
    public static String OSS_URL = "192.168.110.248:8080/";

    //获取上传文件到oss服务器路径
    public static String getUpLoadImage() {
        return OSS_URL + "public/oss/upload";
    }

    //获取批量上传文件oss服务器路径
    public static String getBatchUpLoadImage() {
        return OSS_URL + "public/oss/batch-upload";
    }

    /**
     * 发送验证码
     */
    public static final String SEND_SMS = HIERARCHY + "sms/send";

    /**
     * 检测验证码
     */
    public static final String CHECK_CODE_SMS = HIERARCHY + "sms/checkcode";

    /**
     * 支付宝获取infoStr
     */
    public static final String PAYMENT_ALIPAY_AUTHINFO = HIERARCHY + "payment/alipay/auth";


    /**
     * 一键登录
     */
    public static final String ONE_CLICK_LOGIN = HIERARCHY + "user/login";
    /**
     * 登录
     */
    public static final String USER_LOGIN = HIERARCHY + "user/login";
    /**
     * 绑定手机
     */
    public static final String USER_MOBILE_BIND = HIERARCHY + "user/mobile/bind";
    /**
     * 登出
     */
    public static final String USER_LOGOUT = HIERARCHY + "user/logout";
    /**
     * 注销
     */
    public static final String USER_LOGOFF = HIERARCHY + "user/logoff";
    /**
     * 第三方登录接口
     */
    public static final String USER_OPEN_LOGIN = HIERARCHY + "user/login/open";

    /**
     * 验证码检测
     */
    public static final String CHECKCODE = HIERARCHY + "sms/checkcode";
    /**
     * 发送一条消息
     */
    public static final String MESSAGE_SEND = HIERARCHY + "mps/message/send";

    /**
     * 消息订阅列表
     */
    public static final String MESSAGE_SUBSCRIBES = HIERARCHY + "mps/message/subscribes";
    /**
     * 消息订阅详情
     */
    public static final String MESSAGE_DETAILS = HIERARCHY + "mps/message/details";
    /**
     * 消息历史
     */
    public static final String MESSAGE_HISTORY = HIERARCHY + "mps/message/history";

    /**
     * 清除未读消息
     */
    public static final String MESSAGE_CLEAR_ALL = HIERARCHY + "mps/message/clearall";

    /**
     * 获取2点距离
     */
    public static final String OTHER_DISTANCE = HIERARCHY + "other/distance";


}
