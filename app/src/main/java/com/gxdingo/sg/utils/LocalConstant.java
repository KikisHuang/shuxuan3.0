package com.gxdingo.sg.utils;

public class LocalConstant {

    //聊天id
    public static String CHAT_IDENTIFIER = "";
    //聊天订阅id
    public static String CHAT_UUID = "";

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
    //未知类型
    public static final int UNKNOWN = 666666;

    public static String AUTH_SECRET = "";
//    public static String AUTH_SECRET = "WSSxEQegYUPwpv0HmGtL5ANDCfCHPqmvK4EHzd66XfcbmSksf4kMaT8IcKQnElSwlZF6V1tgwCx80ODP6LMQlQJbVTBwP5R6ZUfXQAtorPWE0b67pXmd1DrHL7SPUHx6lsLhYoSAZwocE75UNgpw/Iv5HztGAymo4sBxoMvPTIl9b8Cd30jONC1A3XJRvNwqss0fz2ME0nqujWgtEjlZQEy60L6FdWAcGwvDuqKo02AyAyZ2vvWQeqCZm/whkGe4XKnWH+Tj29Kl04VbZLajystSEHRPGCm0PbcuoE7EOL46aev0A2qN4g==";

    public static final int CODE_SEND = 10;
    public static final int CLIENT_LOGIN_SUCCEED = 20;
    public static final int ALIPAY_LOGIN_EVENT = 254;
    public static final int WECHAT_LOGIN_EVENT = 934946;
    public static final int CLIENT_REFRESH_BANKCARD_LIST = 38;
    public static final int CLIENT_REFRESH_USER_HOME = 40;
    public static final int QUITLOGINPAGE = 404;
    //确认收货
    public static final int CLIENT_CONFIRM_RECEIPT_ORDER = 36;
    public static final int STORE_LOGIN_SUCCEED = 21;
    public static final int LOGOUT_SUCCEED = 22;
    public static final int BIND_PHONE = 30;
    public static final int BIND_NEW_PHONE = 31;
    //投诉成功
    public static final int COMPLAINT_SUCCEED = 32;

    public final static int EMOTION_LAYOUT_IS_SHOWING = 1848;

    public final static int CONSENT_AGREEMENT = 11111;

    //登录or绑定微信获取code
    public static boolean isLogin = false;

    //前后台标识符
    public static boolean isBackground = false;

    public static String adCode = "";

    //第一次登录标识符
    public static final String FIRST_LOGIN_KEY = "shugou_first_login_key";
    //sp final
    //登陆方式 true 用户 false商家端
    public static final String LOGIN_WAY = "shugou_login_way_key";
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

    //客户端测试 http 签名
    public static final String TEST_HTTP_KEY = "42368a38a5a1a0d8bc6160513b032e15";

    //客户端uat http 签名
    public static final String CLIENT_UAT_HTTP_KEY = "79f895661d9fe3c2bf048578493a37a1";
    //商家端uat http 签名
    public static final String STORE_UAT_HTTP_KEY = "0ad003666103496c6d17ceab55795764";

    //客户端official http 签名
    public static final String CLIENT_OFFICIAL_HTTP_KEY = "298fb7d237c3cd377ed95dc0075e7f35";
    //商家端official http 签名
    public static final String STORE_OFFICIAL_HTTP_KEY = "4b2412de3657b3fcdc6b1f7d1518b3df";

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

    //d4a02249b24eeb795ac47b1c09b3b810
    public static String WEB_SOCKET_KEY = "9e8252612f954ee42a50b3096827fc33";

    public static String WEB_SOCKET_UAT_KEY = "f0438ff6a8792aada17a04e0541db8b3";

    public static String TEMPTOKEN = "";

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
