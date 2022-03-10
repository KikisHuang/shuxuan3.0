package com.gxdingo.sg.http;

/**
 * @author: Kikis
 * @date: 2021/5/13
 * @page:
 */
public class Api {

    public static String URL;

    public static final String CLIENT_HDGZ_AGREEMENT_KEY = "hdgz";

    //uat服务器开关 ，***打包上线正式服请关闭此开关***
    public static final boolean isUat = true;

    //正式服测试开关 ，***本地调试正式服打开此开关即可,打开此开关后，isUat开关不生效***
    public static final boolean isOnlineTest = false;

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
    public static String IM_OFFICIAL_URL = "shuxuan.gxdingo.com/exmsgr/";

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
    /**
     * 获取或存放邀请码至服务器
     */
    public static final String INVITATIONCODE = HIERARCHY + "store/save/or/get/invitationCode";
    /**
     * 上传邀请码至服务器
     */
    public static final String UPLOAD_INVITATIONCODE = HIERARCHY + "activity/add/regionCode";

    /**
     * * 消息内容未读状态改成已读
     */
    public static final String MESSAGE_WITHDRAW = HIERARCHY + "mps/message/withdraw";
    /**
     * * 置顶取消置顶
     */
    public static final String CHAT_SETTOP = HIERARCHY + "mps/subscribe/top";
    /**
     * 订阅列表消息删除
     */
    public static final String SUBSCRIBE_DELETE = HIERARCHY + "mps/subscribe/delete";
    /**
     * 消息删除
     */
    public static final String MESSAGE_DELETE = HIERARCHY + "mps/message/delete";

    /**
     * 阿里云身份证时别
     */
    public static final String EXTRA_IDCARDOCR = HIERARCHY + "extra/idCardOCR";

    /**
     * 实名认证
     */
    public static final String EXTRA_CERTIFICATION = HIERARCHY + "extra/certification";

    /**
     * 活体实名认证接口初始化
     */
    public static final String AUTHENTICATION_INIT = HIERARCHY + "extra/living/authentication/init";
    /**
     * 活体实名认证1
     */
    public static final String AUTHENTICATION_VERIFY = HIERARCHY + "extra/living/authentication";
    /**
     * 活体实名认证2
     */
    public static final String AUTHENTICATION_VERIFY2 = HIERARCHY + "extra/living/authentication/verify";
    /**
     * 阿里云 获取认证结果
     */
    public static final String ALICLOUDAPI_VERIFY = HIERARCHY + "https://apprpv.market.alicloudapi.com/verify";
    /**
     * 获取标签
     */
    public static final String CIRCLE_LABEL = HIERARCHY + "circle/label";

    /**
     * 刷新登录信息
     */
    public static final String USER_STATUS = HIERARCHY + "user/status";

    //uat路径
    public static final String UAT_URL = "uat.gxdingo.com/exuser/";

    //正式路径
    public static final String OFFICIAL_URL = "shuxuan.gxdingo.com/exuser/";

    //客户端隐私协议文章
    public static final String CLIENT_PRIVACY_AGREEMENT_KEY = "shuxuankehuxieyi";
    //客户端服务协议文章
    public static final String CLIENT_SERVICE_AGREEMENT_KEY = "yonghufuwuxieyi";

    // 商圈排行榜规则
    public static final String RANKING_RULE = "ranking-circle";

    //商家端隐私协议文章
    public static final String STORE_PRIVACY_AGREEMENT_KEY = "shuxuanshangjinxieyi";
    //商家端服务协议文章
    public static final String STORE_SERVICE_AGREEMENT_KEY = "shangjiafuwuxieyi";

    public static final String STORE_SHOP_AGREEMENT_KEY = "shangjiaxieyi";

    /*    //商家端隐私协议文章
        public static final String STORE_PRIVACY_AGREEMENT_KEY = "shuxuanshangjinxieyi";
        //商家端服务协议文章
        public static final String STORE_SERVICE_AGREEMENT_KEY = "shangjiafuwuxieyi";*/
    //店铺命名规则
    public static final String STORE_NAMING_RULE = "mingmingguize";
    //树选用户及商家隐私协议
    public static final String PRIVACY_AGREEMENT_KEY = "azyhsjyhxy";

    //店铺命名规则
    public static final String STORE_NAMING_RULES = "mingmingguize";


    //测试路径
    public static final String TEST_URL = "192.168.110.248";

    //端口
    public static final String CLIENT_PORT = "8080";


    public static String WEB_URL = "http://192.168.110.248:8181";

    public static final String TEST_WEB_URL = "http://192.168.110.248:8181";

    public static final String UAT_WEB_URL = "http://uat.gxdingo.com/html";

    public static final String HTML = "article/html?";

    public static final String OFFICIAL_WEB_URL = "shuxuan.gxdingo.com/h5";

    //代付path
    public static final String NSTEAD_PAYING_URL = "/#/pages/app/daifu?orderId=";

    //平台客服path
    public static final String SERVER_URL = "/#/pages/message/chat";


    /**
     * 获取语音的token
     */
    public static final String VOICE_TOKEN = HIERARCHY + "public/voice/token";
    /**
     * 获取默认地址
     */
    public static final String ADDRESS_DEFAULT = HIERARCHY + "address/default";
    /**
     * 订单生成
     */
    public static final String ORDER_BUILD = HIERARCHY + "order/build";
    /**
     * 追加需求
     */
    public static final String ORDER_EXTRA = HIERARCHY + "order/extra";
    /**
     * 指定商家/随机商家
     */
    public static final String ORDER_STORE = HIERARCHY + "order/store";
    /**
     * 删除订单
     */
    public static final String ORDER_DELETE = HIERARCHY + "order/delete";
    /**
     * 用户端用户信息
     */
    public static final String USER_HOME = HIERARCHY + "user/home";
    /**
     * 用户端修改用户信息
     */
    public static final String USER_EDIT = HIERARCHY + "user/edit";

    /**
     * 添加收货地址
     */
    public static final String ADDRESS_ADD = HIERARCHY + "address/add";
    /**
     * 修改收货地址
     */
    public static final String ADDRESS_UPDATE = HIERARCHY + "address/update";

    /**
     * 删除收货地址
     */
    public static final String ADDRESS_DELETE = HIERARCHY + "address/delete";
    /**
     * 评价列表
     */
    public static final String COMMENT_LIST = HIERARCHY + "comment/list";
    /**
     * 订单列表
     */
    public static final String ORDER_LIST = HIERARCHY + "order/list";
    /**
     * 订单详情
     */
    public static final String ORDER_DETAIL = HIERARCHY + "order/detail";

    /**
     * 取消订单
     */
    public static final String ORDER_CANCEL = HIERARCHY + "order/cancel";
    /**
     * 确认收货
     */
    public static final String ORDER_CONFIRM = HIERARCHY + "order/confirm";
    /**
     * 去评价
     */
    public static final String COMMENT_SUBMIT = HIERARCHY + "comment/submit";
    /**
     * 商家详情
     */
    public static final String STORE_DETAIL = HIERARCHY + "store/detail";
    /**
     * 常用店铺
     */
    public static final String STORE_FAVORITES = HIERARCHY + "store/favorites";
    /**
     * 删除常用店铺
     */
    public static final String FAVORITE_DELETE = HIERARCHY + "favorite/delete";
    /**
     * 设置常用店铺
     */
    public static final String FAVORITE_ADD = HIERARCHY + "favorite/add";


    /**
     * 绑定手机号码修改
     */
    public static final String USER_MOBILE_CHANGE = HIERARCHY + "user/mobile/change";


    /**
     * 绑定第三方提现账号
     */
    public static final String WALLET_BINDING = HIERARCHY + "wallet/binding";

    /**
     * 解绑第三方提现方式
     */
    public static final String WALLET_UNBINDING = HIERARCHY + "wallet/unbound";

    /**
     * 我的主页
     */
    public static final String MINE_HOME = HIERARCHY + "user/home";

    /**
     * 交易记录
     */
    public static final String TRANSACTION_RECORD = HIERARCHY + "wallet/transaction/list";

    /**
     * 提现信息
     */
    public static final String Cash_ACCOUNT_INFO = HIERARCHY + "wallet/bank/list";
    /**
     * 校验支付密码
     */
    public static final String CHECK_PAY_PASSWORD = HIERARCHY + "wallet/authentication/old/password";
    /**
     * 获取地址列表
     */
    public static final String ADDRESS_ADDRESSES = HIERARCHY + "address/addresses";

    /**
     * 填写商家邀请码（领取优惠券）
     */
    public static final String COUPON_RECEIVE = HIERARCHY + "coupon/receive";
    /**
     * 已领取的优惠券列表
     */
    public static final String COUPON_LIST = HIERARCHY + "coupon/list";
    /**
     * 获取文章列表
     */
    public static final String ARTICLE_LIST = HIERARCHY + "article/list";

    /**
     * 获取文章详情
     */
    public static final String ARTICLE_DETAIL = HIERARCHY + "article/detail";

    /**
     * 获取图片文章形式
     */
    public static final String ARTICLE_IMAGE = HIERARCHY + "article/image";

    /**
     * 语音获取匹配度最高的店铺信息
     */
    public static final String FIND_STORE = HIERARCHY + "voice/find/store";
    /**
     * 语音获取匹配度最高的分类信息
     */
    public static final String FIND_CATEGORY = HIERARCHY + "voice/find/category";

    /**
     * 分类列表
     */
    public static final String CATEGORY_CATEGORIES = HIERARCHY + "category/categories";

    /**
     * 商家列表/附近商家
     */
    public static final String STORE_LIST = HIERARCHY + "store/list";

    /**
     * 店铺评价列表
     */
    public static final String COMMENT_LIST_STORE = HIERARCHY + "comment/list/store";

    /**
     * 去支付
     */
    public static final String PAYMENT_PAY = HIERARCHY + "payment/pay";

    /**
     * 壁纸列表
     */
    public static final String WALLPAPER_List = HIERARCHY + "wallpaper/list";
    /**
     * 修改壁纸
     */
    public static final String WALLPAPER_CHANGE = HIERARCHY + "wallpaper/change";

    /**
     * 添加自定义壁纸
     */
    public static final String WALLPAPER_ADD = HIERARCHY + "wallpaper/add";
    /**
     * 删除自定义壁纸
     */
    public static final String WALLPAPER_DELETE = HIERARCHY + "wallpaper/delete";
    /**
     * 配置列表
     */
    public static final String CONFIG_LIST = HIERARCHY + "config/list";
    /**
     * 配置列表
     */
    public static final String APPEAL_SUBMIT = HIERARCHY + "appeal/submit";

    /**
     * 商圈评论/回复
     */
    public static final String BUSINESS_DISTRICT_COMMENT_OR_ADD = HIERARCHY + "circle/comment/add";

    /**
     * 邀请好友助力页面详情
     */
    public static final String INVITE_HELP = HIERARCHY + "activity/turntable/help/detail";

    /**
     * 邀请好友助力页面详情
     */
    public static final String HELP_AFTER = HIERARCHY + "activity/turntable/help";

    /**
     * 完成用户大转盘抽奖任务
     */
    public static final String TASK_COMPLETE = HIERARCHY + "activity/task/completed";

    /**
     * 邀请信息
     */
    public static final String INVITESELLER = HIERARCHY + "activity/inviteSeller";


    /**
     * 重置支付密码
     */
    public static final String RESET_PAY_PASSWORD = HIERARCHY + "user/paypassword/set";


    /**
     * 地址列表
     */
    public static final String ADDRESS_LIST = HIERARCHY + "address/list";

    /**
     * 获取分类品类
     */
    public static final String CATEGORY_LIST = HIERARCHY + "category/list";

    /**
     * 商家入驻
     */
    public static final String SETTLE = HIERARCHY + "user/settle";


    /**
     * 首页
     */
    public static final String HOME = HIERARCHY + "user/home";

    /**
     * 店铺详情信息
     */
    public static final String HOME_DETAILS = HIERARCHY + "store/detail";
    /**
     * 店铺信息编辑
     */
    public static final String STORE_UPDATE = HIERARCHY + "user/edit";

    /**
     * 获取配送距离列表
     */
    public static final String DELIVERY_SCOPE = HIERARCHY + "config/delivery";
    /**
     * 店铺获取店铺资质
     */
    public static final String CHECK_QUALIFICATION = HIERARCHY + "store/qualifications";
    /**
     * 更新店铺资质
     */
    public static final String UPDATE_QUALIFICATION = HIERARCHY + "store/update/qualifications";
    /**
     * 忽略订单
     */
    public static final String ORDER_IGNORE = HIERARCHY + "order/ignore";

    /**
     * 首页数据统计
     */
    public static final String STORE_DATA = HIERARCHY + "store/data/statistics";
    /**
     * 曲线图标数据
     */
    public static final String STORE_DIAGRAM = HIERARCHY + "store/diagram";

    /**
     * 评论列表
     */
    public static final String EVALUATION_LIST = HIERARCHY + "comment/list";

    /**
     * 商家回复评论
     */
    public static final String REPLY_EVALUATION = HIERARCHY + "comment/reply";


    /**
     * 订单提醒
     */
    public static final String ORDER_REMIND = HIERARCHY + "order/remind";

    /**
     * 订单拒绝
     */
    public static final String ORDER_REFUSE = HIERARCHY + "order/refuse";

    /**
     * 订单拒绝
     */
    public static final String REASON_LIST = HIERARCHY + "config/list";

    /**
     * 接单
     */
    public static final String ORDER_RECEIVING = HIERARCHY + "order/receiving";
    /**
     * 抢单
     */
    public static final String ORDER_SNATCH = HIERARCHY + "order/receiving";


    /**
     * 发送订单
     */
    public static final String ORDER_SEND = HIERARCHY + "order/send";

    /**
     * 订单配送完成
     */
    public static final String ORDER_COMPLETE = HIERARCHY + "order/complete";

    /**
     * 详情
     */
    public static final String ORDER_DETAILS = HIERARCHY + "order/detail";

    /**
     * 钱包首页数据
     */
    public static final String WALLET_HOME = HIERARCHY + "wallet/home";

    /**
     * 扫码核销优惠券
     */
    public static final String STORE_SCAN_CODE = HIERARCHY + "store/scanning/code";
    /**
     * 钱包首页数据
     */
    public static final String CHECK_RECORD = HIERARCHY + "wallet/transaction/list";

    /**
     * 交易记录详情
     */
    public static final String TRANSACTION_DETAILS = HIERARCHY + "wallet/transaction/detail";

    /**
     * 提现
     */
    public static final String BALANCE_CASH = HIERARCHY + "wallet/withdraw";
    /**
     * 添加银行卡
     */
    public static final String ADD_CARD = HIERARCHY + "wallet/binding/card";

    /**
     * 解除绑定的银行卡
     */
    public static final String UNBIND_BANK_CARD = HIERARCHY + "wallet/unbind/card";

    /**
     * 支持绑定的银行卡
     */
    public static final String SUPPORT_CARD_LIST = HIERARCHY + "wallet/card/list";

    /**
     * 我的首页
     */
    public static final String MINE_INFO = HIERARCHY + "user/info";
    /**
     * 我的专属二维码
     */
    public static final String STORE_QR_CODE = HIERARCHY + "store/my/code";

    /**
     * 我的首页
     */
    public static final String STORE_ACCOUNT = HIERARCHY + "user/account";
    /**
     * 检验支付密码
     */
    public static final String UPDATE_WITHDRAWAL_PASSWORD = HIERARCHY + "wallet/withdrawal/password";
    /**
     * 我的首页
     */
    public static final String MINE_INFO_UPDATE = HIERARCHY + "user/update";
    /**
     * 反馈
     */
    public static final String FEEDBACK_ADD = HIERARCHY + "feedback/add";
    /**
     * 申诉列表
     */
    public static final String APPEAL_LIST = HIERARCHY + "order/appeal/list";
    /**
     * 订单申诉—修改
     */
    public static final String APPEAL_UPDATE = HIERARCHY + "order/appeal/update";
    /**
     * 订单申诉—删除
     */
    public static final String APPEAL_DELETE = HIERARCHY + "order/appeal/delete";
    /**
     * 订单申诉—添加
     */
    public static final String APPEAL_ADD = HIERARCHY + "order/appeal/save";
    /**
     * 商圈列表
     */
    public static final String BUSINESS_DISTRICT_LIST = HIERARCHY + "circle/list";
    /**
     * 商圈点赞
     */
    public static final String CIRCLE_LIKEDORUNLIKED = HIERARCHY + "circle/likedOrUnliked";
    /**
     * 发布商圈信息
     */
    public static final String RELEASE_BUSINESS_DISTRICT_INFO = HIERARCHY + "circle/save";
    /**
     * 商圈消息评论列表
     */
    public static final String BUSINESS_DISTRICT_MESSAGE_COMMENT_LIST = HIERARCHY + "circle/comment/unread/list";
    /**
     * 商圈评论/回复
     */
    public static final String BUSINESS_DISTRICT_COMMENT_OR_REPLY = HIERARCHY + "circle/comment/reply";
    /**
     * 获取商圈评论列表（展开评论）
     */
    public static final String BUSINESS_DISTRICT_GET_COMMENT = HIERARCHY + "circle/comment/list";
    /**
     * 获取商圈评论未读数量
     */
    public static final String GET_NUMBER_UNREAD_COMMENTS = HIERARCHY + "circle/unread";
    /**
     * 商家删除商圈动态
     */
    public static final String STORE_DELETE_BUSINESS_DISTRICT_DYNAMICS = HIERARCHY + "circle/delete";
    /**
     * 删除我自己的评论
     */
    public static final String DELETE_MY_OWN_COMMENT = HIERARCHY + "circle/comment/delete";
    /**
     * 获取扫码核销优惠卷弹窗内容
     */
    public static final String CONFIG_SCANNING_REMIND = HIERARCHY + "config/scanning/remind";
    /**
     * 商圈广告/图标/通知
     */
    public static final String CIRCLE_HEADER_ADS = HIERARCHY + "circle/header/ads";

    /**
     * 排行榜列表
     */
    public static final String RANKING_LIST = HIERARCHY + "activity/ranking/list";
}
