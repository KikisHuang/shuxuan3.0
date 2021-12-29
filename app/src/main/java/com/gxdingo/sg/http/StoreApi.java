package com.gxdingo.sg.http;

import static com.gxdingo.sg.http.Api.HIERARCHY;

/**
 * Created by Kikis on 2018/12/7.
 */

public class StoreApi {


    //uat路径
    public static final String UAT_URL = "uat.gxdingo.com/exseller/";

    //正式路径
    public static final String OFFICIAL_URL = "shuxuan.gxdingo.com/exseller/";

    //测试路径
    public static final String TEST_URL = "192.168.110.248";

    //端口
    public static final String STORE_PORT = "8081";

    public static final String STORE_SHOP_AGREEMENT_KEY = "shangjiaxieyi";

    public static final String CLIENT_HDGZ_AGREEMENT_KEY = "hdgz";

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
    public static final String STORE_UPDATE = HIERARCHY + "store/update";

    /**
     * 获取配送距离列表
     */
    public static final String DELIVERY_SCOPE = HIERARCHY + "config/delivery";
    /**
     * 店铺获取店铺资质
     */
    public static final String CHECK_QUALIFICATION = HIERARCHY + "store/qualifications";
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
     * 订单列表
     */
    public static final String ORDER_LIST = HIERARCHY + "order/list";

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
     * 删除订单
     */
    public static final String ORDER_DELETE = HIERARCHY + "order/delete";

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
     * 选择已绑定银行卡
     */
    public static final String SELECT_BANK_CARD = HIERARCHY + "wallet/bank/list";

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
    public static final String CHECK_PAY_PASSWORD = HIERARCHY + "update/withdrawal/password";
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
     * 刷新登录信息
     */
    public static final String USER_STATUS = HIERARCHY + "user/status";
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
}
