package com.kikis.commnlibrary.utils;


import android.Manifest;

import static com.blankj.utilcode.util.AppUtils.getAppName;
import static com.blankj.utilcode.util.ConvertUtils.sp2px;

/**
 * Created by lian on 2018/3/1.
 */
public class Constant {


    //身份证号英文和数字正则
    public static final String IDCARD_REGULAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
    //支付密码正则规则
    public static final String PAY_PASS_WORD_PATTERN = "(?:(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){5}|(?:9(?=8)|8(?=7)|7(?=6)|6(?=5)|5(?=4)|4(?=3)|3(?=2)|2(?=1)|1(?=0)){5}|([\\d])\\1{2,})\\d";
    //英文正则
    public static final String AZ_PATTERN = "[a-zA-Z]";

    public static final Object COMMODITYDETAILSRICHTEXT = "commodity_details_richtext";

    public static final String HTML_LOADING_SUCCEED = "html_loading_succeed";
    //是否debug环境
    public static boolean isDebug = false;

    //聊天id
    public static String CHAT_IDENTIFIER = "";

    /**
     * 分享类型
     */
    public static final int SHARE_QQ = 123;

    public static final int SHARE_WECHAT = 223;

    public static final int SHARE_WECHAT_CIRCLE = 323;

    public static long SMS_CODE_INTERVAL = 60;

    public static String APK = "apk";

    public static String DOWNLOAD_PATH = "download_path";

    public static String STORAGE_PATH = "storage_path";

    public static String FILE_NAME = "file_name";

    public static String DOWNLOAD = "download";

    public static String AUTOMOUNT = "automount";

    public static String TEMPNAME = getAppName() + " temp";

    public static String TEAMPSN = "";

    //bugly appid 9c912b9d6d
    public static String BUGLYAPPID = "9c912b9d6d";

    //小程序原始id
    public static String MINI_ID = "gh_f194a10139ee";

    //QQappid
    public static String QQ_APPID = "101807113";
    //微信appid
    public static String WECHAT_APPID = "wx71be9c923a6c1d7f";
    //阿里appid
    public static String ALI_APPID = "2021002138680668";
    //小米appid
    public static String MI_APPID = "2882303761518396560";

    //oppo appkey
    public static String OPPO_APPKEY = "75f9cb3df46b420d8b69c5cfd417dffd";
    public static String OPPO_MASTERSECRET = "b8cc6e5ba95e4a46b47a030a6a8039bf";

    //qq secret
    public static String QQ_APP_SECRET = "d5eed81fe85eae9bcddb14ae9a78fdac";
    //微信 secret
    public static String WECHAT_APP_SECRET = "35cbfd3c687cf8592c7d726949fc5e9b";


    //小米 appkey
    public static String MI_APP_KEY = "5911839687560";

    public static String UMENG_APP_KEY = "5da3dfb44ca3577f71000cd4";
    public static String DOUYIN_CLIENT_KEY = "awriamz58963bh44";
    public static String DOUYIN_CLIENT_SECRET = "6b9baa0b94b360be4aa4825d1f0ddc45";


    public static final String HTMLTESTDATA = "<p><img src=\"https://cdn.pabei.cn/commodity/image/21/C252F37EA13F9AB765DF0C7F51156CF4393217.jpg\">奶油生菜是莴苣的一个变种，味甘甜微苦，颜色翠绿，口感脆嫩清香，因适宜生吃而得名。属菊科\t莴苣属口里你\t，为一年生草本作物。所含\t膳食纤维较白菜多，可消除多余脂肪。</p><p><br></p><h2>生长期</h2><p>60天</p><p><br></p><h2>食用价值</h2><p>食用部分含水分高达94%-96%，故生食清脆爽口，特别鲜嫩。每100克食用部分还含\t蛋白质\t1-1.4克、碳水化合咯【评论物1.8-3.2克、\t维生素C\t10-15毫克及一些矿物质。进口量就生菜还含有\t莴苣\t素，具清热、消炎、\t催眠\t作用。</p><p><br></p><h2>菜谱</h2><h3>奶油生菜沙司</h3><p>原料：</p><p>生莱250克，鸡蛋2个，黄瓜100克，奶油40克，盐、胡椒粉各适量。</p><p>制作：</p><p>1、将鸡蛋煮熟，剥去壳，蛋白与蛋黄分开，蛋白切成丁，蛋黄压成泥后加入奶油、盐、胡椒粉，搅拌成沙司。</p><p>2、生菜洗净，控干水分，切成小块，与蛋白丁拌匀，黄瓜洗净切圆片。</p><p>3、将黄瓜片摆放盘边，生菜蛋白放在盘中，浇上调好的蛋黄沙司即成。</p><p>特点：生菜的食用方法主要是生食，为西餐蔬菜沙拉的当家菜。&nbsp;</p><p><br></p><h3>奶油生菜丝浆汤</h3><p>原料：青生菜100克，生鸡蛋黄2只，白塔油50克，奶油浓汤2000克。</p><p>制作：青生菜切丝，下沸水中烫一下捞出，滤干水分，分装在10只汤盆里。临吃时，再将生鸡蛋黄、白塔油放在一起打起泡，投入奶油汤内搅匀，置火上烧沸后，分装在汤盆里即成。</p><p>特点：奶绿色，浓香清口。&nbsp;</p><p><br></p><h3>奶油生菜沙拉</h3><p>原料：鸡蛋3个，生菜250克。</p><p>辅料：黄瓜100克，奶油40克，盐3克，胡椒粉少许。</p><p>做法：</p><p>1、将鸡蛋煮熟，剥去壳，蛋白与蛋黄分开，把蛋白切成小丁，蛋黄放小碗内，用匙压成泥状，加奶油、盐、胡椒粉搅拌成沙司；生菜洗净、控干，切成小块，与蛋丁拌匀；黄瓜洗净，切成圆片。</p><p>2、取一干净圆盘，将黄瓜片围着盘边码一圈，生菜蛋丁堆放在中央，上面浇上调好的奶油蛋黄沙拉。</p><p>特点：色泽黄绿相间，口味清淡宜人。&nbsp;</p>";

    public static final int EMOJISIZE = (int) ((int) sp2px(12) * 13 / 9);

    public static final String VIDEO = "video";
    public static final String PHOTO = "photo";
    public static final String COMPRESS = "compress";

    //因为chatid群聊跟私聊会重复，所以为了区分群聊 = id + group
    public static final int group = 1000000;

    //error params
    public static final String SOCKET_CLOSED = "java.net.SocketException: Socket closed";
    public static final String IMSOCKET_CLOSED = "Im_Socket_Closed";

    public static final String IO_CANCELED = "java.io.IOException: Canceled";

    public static final String ERRORFILENAME = "Error";

    public static final String ERROMSG = "erroMsg";
    public static final String error = "error";

    //port params
    public static final String PARAMAS = "Paramas";
    public static final String SERIALIZABLE = "serializable";
    public static final String CLOSECHATPAGE = "close_chat_page";
    public static final String CLOSEGROUPCHATPAGE = "close_group_chat_page";

    public static final String ID = "id";


    public static final String CONTENT = "content";
    public static final String SESSION = "session";
    public static final String PACKAGE = "package";
    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";
    public static final String KEY = "key";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String GENDER = "gender";
    public static final String PAGE = "page";
    public static final String LIMIT = "limit";
    public static final String AVATAR = "avatar";
    public static final String NICKNAME = "nickname";
    public static final String FILE = "file";
    public static final String FILES = "files";
    public static final String STRING = "string";
    public static final String BOOLEAN = "boolean";
    public static final String TEXTAREA = "textarea";
    public static final String WSID = "wsid";
    public static final String SOURCE = "source";

    public static final String SEARCH_CONTENT = "searchContent";
    public static final String CURRENT = "current";
    public static final String SIZE = "size";


    public static final String ONCONNECTION = "onConnection";

    public static final String MIME_TYPE = "video/avc"; // H.264 Advanced Video
    public static final String JPG = "jpg"; // H.264 Advanced Video
    public static final String MP4 = "mp4"; //
    public static final String MIMETYPE_MP4 = "video/mp4"; //

    public static final String MIMETYPE_JPG = "image/jpeg"; //
    public static final String MIMETYPE_PNG = "image/png"; //

    //event bus paramas
    public static final String BACK = "back";
    //禁止发送验证码
    public static final String DISABLEVCODE = "disable_verification_code";
    public static final String LOGINDISABLEVCODE = "login_disable_verification_code";
    public static final int CLOSE_ALL_ACTIVITY = 10086;
    public static final String CONSENT_PROTOCOL = "consent_protocol";

    public static final String ENTER_ANIMATION = "enter_animation";

    public static final String START_ANIMA_ALLEVALUATE = "start_all_evaluate_fragment";
    public static final String END_ANIMA_ALLEVALUATE = "end_all_evaluate_fragment";

    //支付成功
    public static final int PAYMENT_SUCCESS = 51733;

    //开通地块成功
    public static final String OPEN_LAND_SUCCEED = "open_land_succeed";

    //开通地块成功
    public static final String RENEWED_LAND_SUCCEED = "renewed_land_succeed";

    //支付失败
    public static final String PAYMENT_FAILED = "payment_failed";
    //微信支付成功
    public static final String WECHAT_PAYMENT_SUCCESS = "wechat_payment_success";
    //微信支付失败
    public static final String WECHAT_PAYMENT_FAILED = "wechat_payment_failed";
    //支付宝支付成功
    public static final String ALIPAY_PAYMENT_SUCCESS = "alipay_payment_success";
    //支付宝支付失败
    public static final String ALIPAY_PAYMENT_FAILED = "alipay_payment_failed";
    //重新支付
    public static final String RETRY_PAYMENT = "retry_payment";

    //登入成功
    public static final String LOGINSUCCEED = "login_succeed";

    //表情布局显示
    public static final String SHOWEMOTION = "show_emotion";
    //刷新会话列表
    public static final String REFRESHCHATLIST = "refresh_chatlist";
    //表情布局添加按钮
    public static final String EMOJIADDBUTTON = "emoji_add_button";


    //fragment bundle params
    public static final String DEVICEDATA = "DeviceData";
    public static final String GROUPCONTROLDEVICEDATA = "GroupControlDeviceData";
    public static final String MYMODULEDATA = "MyModuleData";
    public static final String POSITION = "position";
    public static final String TABLIST = "TabList";

    //Bundle params
    public static final String PHOTOLIST = "PHOTO_LIST";
    public static final String PHOTOPOS = "PHOTO_POS";
    public static final String PHOTOFUNCTION = "PHOTO_FUNCTION";

    /**
     * AES常量
     */
    // key
    public static byte[] AESKEY = null;
    //偏移量
    public static byte[] IVPARAMETER = null;

    public static final String CIPHERMODEPADDING = "AES/CBC/PKCS5Padding";// AES/CBC/PKCS7Padding

    /**
     * socket response type
     */
    //初始化
    public static final String init = "init";
    //登录
    public static final String login = "login";
    public static final String register = "register";
    public static final String other = "other";
    //更新用户信息
    public static final String UpdateUserInfo = "UpdateUserInfo";
    //修改密码
    public static final String UpdateUserAccount = "UpdateUserAccount";
    //用户信息
    public static final String UserInfo = "UserInfo";
    //好友列表
    public static final String friendList = "friendList";
    //ack
    public static final String ack = "ack";

    //私聊(8.0通知栏渠道id)
    public static final String chat = "chat";
    //群聊
    public static final String roomChat = "roomChat";
    //聊天记录
    public static final String chatrecord = "chatrecord";
    //单聊已读、未读
    public static final String readed = "readed";
    //群聊已读、未读
    public static final String roomReaded = "roomReaded";
    //群里列表
    public static final String roomList = "roomList";
    //群信息
    public static final String roomInfo = "roomInfo";

    public static final int LOGOUT = -424;

    public static final String success = "success";
    public static final String failure = "failure";
    public static final String chatList = "chatList";

    /**
     * SharedPreferences常量
     */
    // 登录发送验证码时间
    public static final String VERIFICATIONCODETIME = "fram_verification_code_time";

    //当前被选中底部tab
    public static final String CURRENT_POSITION_FLAG = "FARM_CURRENT_POSITION_FLAG";

    public static final String SOFT_INPUT_HEIGHT = "farm_soft_input_height";
    //第一次启动标识符
    public static final String FIRST_START = "first_start";


    //key
    public static final String TOKEN_KEY = "pabei_farm_token";
    public static final String LOGIN_STATUS = "login_status";
    public static final String SMS_CODE_KEY = "sms_code_key";

    /**
     * http params
     */
    public static final String ACCESS_TOKEN = "access_token";
    public static final String OPENID = "openid";
    public static final String TYPE = "type";
    public static final String TOKEN = "token";
    public static final String TIME = "time";


    public static final String PHONE = "phone";
    public static final String VCODE = "vcode";
    public static final String MOBILE = "mobile";
    public static final String NEWPASSWORD = "newPassword";
    public static final String OLDPASSWORD = "oldPassword";
    public static final String SMSCODE = "smsCode";
    public static final String CAPTCHA = "captcha";
    public static final String CLIENT_TYPE = "client_type";


    public static final String GRANT_TYPE = "grant_type";
    public static final String CLIENT_SECRET = "client_secret";
    public static final String CLIENT_KEY = "client_key";


    public static final int REQUEST1 = 248851;
    public static final int REQUEST2 = 248852;
    public static final int REQUEST3 = 248853;
    public static final int REQUEST4 = 248854;
    public static final int REQUEST5 = 248855;
    public static final int REQUEST6 = 248856;
    public static final int REQUEST7 = 248857;
    public static final int REQUEST8 = 248858;
    public static final int REQUEST9 = 248859;
    public static final int REQUEST10 = 248810;
    public static final int REQUEST11 = 248811;
    public static final int REQUEST12 = 248812;
    public static final int REQUEST13 = 248813;
    public static final int REQUEST14 = 248814;
    public static final int REQUEST15 = 248815;
    public static final int REQUEST16 = 248816;
    public static final int REQUEST17 = 248817;
    public static final int REQUEST18 = 248818;


    public static final int ACRESULET2 = 3121;
    public static final int ACRESULET3 = 3211;
    public static final int ACRESULET4 = 3214;
    public static final int ACRESULET5 = 3215;

    public static final int SEARCH = (3 >> 2);
    public static final int MYLEARNING = (4 >> 1);
    public static final int LECTURERCENTER = (12 >> 1);
    public static final int MESSAGECENTER = (6 >> 1);
    public static final int MYORDER = (8 >> 1);
    public static final int COURSEDETAILTAB = (3 >> 1);

    public static final String WEB_SOCKET_URL = "websocketUrl";
}
