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
    public static String IM_URL = HTTP + "192.168.110.248:8080/";

    //im uat
    public static String IM_UAT_URL = "uat.gxdingo.com/exmsg/";
    //im 测试
    public static final String IM_TEST_URL = "192.168.110.236:8083/";
    //im 正式
    public static String IM_OFFICIAL_URL = "shuxuan.gxdingo.com/exmsg/";


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
    public static final String ONE_CLICK_LOGIN = HIERARCHY + "user/login/click";

    /**
     * 获取调用阿里云手机号码一键登录认证key
     */
    public static final String GET_MOBILE_KEY = HIERARCHY + "public/ali/mobile/key";

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
     * 获取聊天记录列表
     */
    public static final String GET_CHAT_HISTORY_LIST = HIERARCHY + "mps/message/list";

    /**
     * 消息历史
     */
    public static final String MESSAGE_HISTORY = HIERARCHY + "mps/message/history";

    /**
     * 获取未读消息数
     */
    public static final String SUM_UNREAD = HIERARCHY + "mps/message/sum/unread";


    /**
     * 清除未读消息
     */
    public static final String MESSAGE_CLEAR_ALL = HIERARCHY + "mps/message/clearall";
    /**
     * 消息内容未读状态改成已读
     */
    public static final String MESSAGE_READ = HIERARCHY + "mps/message/read";

    /**
     * 获取2点距离
     */
    public static final String OTHER_DISTANCE = HIERARCHY + "other/distance";
    /**
     * 发起转账
     */
    public static final String TRANSFER = HIERARCHY + "transfer/accounts/send";
    /**
     * 领取转账
     */
    public static final String GET_TRANSFER = HIERARCHY + "transfer/accounts/receive";
    /**
     * 投诉
     */
    public static final String COMPLAINT_MSG = HIERARCHY + "complaint/msg";

}
