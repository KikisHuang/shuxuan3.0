package com.gxdingo.sg.http;

import static com.gxdingo.sg.http.Api.HIERARCHY;

/**
 * Created by Kikis on 2018/12/7.
 */

public class ClientApi {


    //uat路径
    public static final String UAT_URL = "uat.gxdingo.com/exuser/";

    //正式路径
    public static final String OFFICIAL_URL = "shuxuan.gxdingo.com/app/";

    //客户端隐私协议文章
    public static final String CLIENT_PRIVACY_AGREEMENT_KEY = "shuxuankehuxieyi";
    //客户端服务协议文章
    public static final String CLIENT_SERVICE_AGREEMENT_KEY = "yonghufuwuxieyi";

    //商家端隐私协议文章
    public static final String STORE_PRIVACY_AGREEMENT_KEY = "shuxuanshangjinxieyi";
    //商家端服务协议文章
    public static final String STORE_SERVICE_AGREEMENT_KEY = "shangjiafuwuxieyi";
    //树选用户及商家隐私协议
    public static final String SERVICE_PRIVACY_AGREEMENT_KEY = "azyhsjyhxy";


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

    public static final String OFFICIAL_WEB_URL = "https://shuxuan.gxdingo.com/h5";

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
}
