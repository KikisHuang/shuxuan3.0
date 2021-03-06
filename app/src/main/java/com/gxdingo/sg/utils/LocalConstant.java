package com.gxdingo.sg.utils;

public class LocalConstant {

    //聊天订阅id
    public static String CHAT_UUID = "";

    public static String SHARE_BUSINESS_DISTRICT_URL = "https://dgkjmm.oss-cn-shenzhen.aliyuncs.com/log/shuxuan.jpg";

    public static final int REFRESH_BUSINESS_DISTRICT_TIME = 900000;

    public static final int REFRESH_LOCATION = 41456;

    public static final String GET_CONTINUOUS_LOGIN_AWARD = "get_continuous_login_award";


    public static final String LAST_VIEW_TIME = "last_view_time";


    //聊天常量
    //自己发送的文本类型
    public static final int SelfText = 7776;
    //自己发送的图片类型
    public static final int SelfImage = 7777;
    //自己发送的商品信息
    public static final int SelfGoodsInfo = 77756;
    //自己发送的音频类型
    public static final int SelfAudio = 7778;
    //自己发送的视频类型
    public static final int SelfVideo = 7779;
    //自己发送的文件类型
    public static final int SelfFile = 7780;
    //自己发送的转账类型
    public static final int SelfTransfer = 7781;

    //自己发送的物流信息类型
    public static final int SelfLogistics = 7782;
    //自己发送的地址信息类型
    public static final int SelfAddressInfo = 7783;
    //自己撤回消息类型
    public static final int SelfRevocation = 7784;
    //被删除的地址信息类型
    public static final int AddressInfoDel = 77777;
    //自己发送的定位地图信息类型
    public static final int SelfLocationMapInfo = 7785;

    //他人发送的文字类型
    public static final int OtherText = 77781;
    //他人发送的图片类型
    public static final int OtherImage = 77782;
    //他人发送的商品信息
    public static final int OtherGoodsInfo = 777754;
    //他人发送的音频类型
    public static final int OtherAudio = 77783;
    //他人发送的视频类型
    public static final int OtherVideo = 77784;
    //他人发送的文件类型
    public static final int OtherFile = 77785;
    //他人发送的转账类型
    public static final int OtherTransfer = 77786;

    //他人发送的物流信息类型
    public static final int OtherLogistics = 77787;

    //他人发送的地址信息类型
    public static final int OtherAddressInfo = 77788;

    //他人撤回消息类型
    public static final int OtherRevocation = 77789;

    //他人发送的定位地图信息类型
    public static final int OtherLocationMapInfo = 77790;

    //未知类型
    public static final int UNKNOWN = 666666;

    public static String AUTH_SECRET = "";
//    public static String AUTH_SECRET = "WSSxEQegYUPwpv0HmGtL5ANDCfCHPqmvK4EHzd66XfcbmSksf4kMaT8IcKQnElSwlZF6V1tgwCx80ODP6LMQlQJbVTBwP5R6ZUfXQAtorPWE0b67pXmd1DrHL7SPUHx6lsLhYoSAZwocE75UNgpw/Iv5HztGAymo4sBxoMvPTIl9b8Cd30jONC1A3XJRvNwqss0fz2ME0nqujWgtEjlZQEy60L6FdWAcGwvDuqKo02AyAyZ2vvWQeqCZm/whkGe4XKnWH+Tj29Kl04VbZLajystSEHRPGCm0PbcuoE7EOL46aev0A2qN4g==";

    public static final int CODE_SEND = 10;
    public static final int LOGIN_SUCCEED = 20;
    public static final int ALIPAY_LOGIN_EVENT = 254;
    public static final int WECHAT_LOGIN_EVENT = 934946;

    public static final int NOTIFY_MSG_LIST_ADAPTER = 4545;
    public static final int CLIENT_REFRESH_BANKCARD_LIST = 38;
    public static final int DELETE_MESSAGE_CONTENT = 7775;
    public static final int CLIENT_REFRESH_USER_HOME = 40;
    public static final int QUITLOGINPAGE = 404;
    //显示商圈未读消息dot
    public static final int SHOW_BUSINESS_DISTRICT_UN_READ_DOT = 1242;

    public static final int BACK_TOP_BUSINESS_DISTRICT = 999;
    public static final int BACK_TOP_SHOP = 998;
    public static final int BACK_TOP_MESSAGE_LIST = 997;
    //确认收货
    public static final int CLIENT_CONFIRM_RECEIPT_ORDER = 36;
    public static final int LOGOUT_SUCCEED = 22;
    public static final int BIND_PHONE = 30;
    public static final int BIND_NEW_PHONE = 31;
    //投诉成功
    public static final int COMPLAINT_SUCCEED = 32;

    //实名认证成功
    public static final int AUTHENTICATION_SUCCEEDS = 33;

    public final static int EMOTION_LAYOUT_IS_SHOWING = 1848;

    public final static int CONSENT_AGREEMENT = 11111;

    //登录or绑定微信获取code
    public static boolean isLogin = false;
    //是否在联系客服页
    public static boolean IS_CONTEACT_SERVER = false;

    //前后台标识符
    public static boolean isBackground = false;

    //分享的口令
    public static final String SHARE_SHIBBOLETH = "shuxuan_share_shibboleth_key";
    public static final String SCANNING_NO_REMIND = "shuxuan_scanning_no_remind";
    public static final int GO_SETTLED = 1013;
    //返回店铺列表页
    public static final int GO_STORE_LIST_PAGE = 1014;

    public static String AdCode = "";
    public static String AoiName = "";
    public static double lat = 0;
    public static double lon = 0;

    //第一次登录标识符
    public static final String FIRST_LOGIN_KEY = "shugou_first_login_key";
    //第一次进入用户端标识符 用于显示输入邀请码弹框
    public static final String FIRST_INTER_KEY = "shugou_first_login_key";
    //sp final
    public static final String CASH_SELECTED_ID_KEY = "cash_selected_id_key";
    //通知栏管理
    public static final String NOTIFICATION_MANAGER_KEY = "shugou_notification_manager_key";
    //默认地址缓存
    public static final String ADDRESS_CACHE = "address_cache_key";

    //http params
    public static final String TAG = "tag";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE1 = "latitude1";
    public static final String LONGITUDE1 = "longitude1";
    public static final String LATITUDE2 = "latitude2";
    public static final String LONGITUDE2 = "longitude2";
    public static final String CERTFLAG = "certFlag";

    public static final String LABELS = "labels";

    public static final String CONTENTID = "contentId";

    public static String locationSelected = "";

    public static final String REASON = "reason";

    public static final String STATUS = "status";
    public static final String CURRENT = "current";
    public static final String SIZE = "size";
    public static final String ORDERID = "orderId";

    public static final String SCORE = "score";
    public static final String ISGOOD = "isGood";
    public static final String IMAGES = "images";
    public static final String IMAGE_NAME = "imageName";
    public static final String ARTICLEID = "articleId";
    public static final String IDENTIFIER = "identifier";
    public static final String STOREID = "storeId";
    public static final String SUBSCRIBEID = "subscribeId";
    public static final String LASTMSGTIME = "lastMsgTime";
    public static final String VOICE_APPKEY = "hy2ITS9TkPbaguBd";
    public static final String URL = "url";
    public static final String TOID = "toId";

    public static final String BANK_TYPE = "bankType";
    public static final String PERSON_OF_CARD = "personOfCard";
    public static final String ID_CARD = "idCard";
    public static final String NUMBER = "number";
    public static final String SMS_CODE = "smsCode";


    public static String SIGN = "sign";

    public static String TIMESTAMP = "timestamp";

    //test oss key
    public static final String TEST_OSS_KEY = "f4e2e52034348f86b67cde581c0f9eb5";
    //uat oss签名
    public static final String UAT_OSS_KEY = "79f895661d9fe3c2bf048578493a37a1";
    //oss 签名  9e8252612f954ee42a50b3096827fc33
    public static final String OSS_KEY = "298fb7d237c3cd377ed95dc0075e7f35";
    //活体实名认证appcode
    public static final String APPCODE = "0529cdbf24174f839acd89d6eafbf99c";

    //客户端测试 http 签名
    public static final String TEST_HTTP_KEY = "42368a38a5a1a0d8bc6160513b032e15";

    //客户端uat http 签名
    public static final String CLIENT_UAT_HTTP_KEY = "79f895661d9fe3c2bf048578493a37a1";

    //客户端official http 签名
    public static final String CLIENT_OFFICIAL_HTTP_KEY = "298fb7d237c3cd377ed95dc0075e7f35";

    //im uat http 签名
    public static final String IM_UAT_HTTP_KEY = "4ee5515ac41d433cabfa7b741c07f55f";

    //im official http
    public static final String IM_OFFICIAL_HTTP_KEY = "1b4c26cf72b24e47afb4d9d033714fc1";

    //http param
    public static final String APPNAME = "appName";


    //全局key
    public static String GLOBAL_SIGN = "";

    public static String IM_SIGN = "";

    //上传oss的key
    public static String OSS_SIGN_KEY = "";

    //websocket 开发环境key
    public static String WEB_SOCKET_TEST_KEY = "d4a02249b24eeb795ac47b1c09b3b810";

    public static String WEB_SOCKET_KEY = "6669ec3d6f724252b32d773adc1a2fca";

    public static String WEB_SOCKET_UAT_KEY = "f0438ff6a8792aada17a04e0541db8b3";

    public static String AAC = ".aac";

    //websocket类型
    public static final String LOGIN = "login";
    public static final String PING = "ping";
    public static final String ERROR = "error";
    public static final String SAY = "say";
    public static final String MORE = "more";
    public static final String TYPE = "type";
    public static final String REPLY = "reply";
    public static final String CROSSTOKEN = "crossToken";

    public static final String NEWS = "news";
    public static final String LAST_ID = "last_id";
    public static final String EXEC = "exec";
    public static final String FINISH_APP = "finish_app";
    public static final int SOCKET_HEART_TIME = 45000;
    public static final int CASH_SUCCESSS = 1001010;

    public static final int SDK_PAY_FLAG = 12113;
    public static final int SDK_AUTH_FLAG = 12315;

    public static final int IMG = 1123;
    public static final int ADD = 1124;


}
