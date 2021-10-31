package com.gxdingo.sg.utils;

public class ClientLocalConstant {

    //sp final
    public static final String USER_INFO_KEY = "shugou_user_info_key";
    public static final String USER_TOKEN_KEY = "shugou_user_token_key";
    public static final String USER_ID_KEY = "shugou_user_id_key";
    public static final String USER_AVATAR_KEY = "shugou_user_avatar_key";
    public static final String USER_PHONE_KEY = "shugou_user_phone_key";
    public static final String USER_NICKNAME_KEY = "shugou_user_nickname_key";
    public static final String USER_OPENID_KEY = "shugou_user_openid_key";
    public static final String USER_WALLPAGER_KEY = "shugou_user_wallpager_key";
    public static final String USER_IDENTIFIER = "shugou_user_identifier_key";

    public static final String MESSAGE_NOTICE_KEY = "shugou_message_notice_key";

    public static final String LOCATION_LATITUDE_KEY = "shugou_user_latitude_key";
    public static final String LOCATION_LONGITUDE_KEY = "shugou_user_longitude_key";

    //heads params
    public static final String APP = "APP";
    public static final String DEVICE = "device";
    public static final String YI_TARGET = "YI-Target";
    public static final String YI_VERSION = "YI-Version";
    public static final String YI_VERSION_NUMBER = "2.0";

    //http params
    public static final String REGIONPATH = "regionPath";
    public static final String STREET = "street";
    public static final String DOORPLATE = "doorplate";
    public static final String ADDRESSID = "addressId";
    public static final String DEMAND = "demand";
    public static final String WALLPAPER = "wallpaper";
    public static final String CATEGORYID = "categoryId";
    public static final String KEY = "key";
    public static final String VOICE = "voice";
    public static final String TRADENO = "tradeNo";
    public static final String REASON = "reason";
    public static final String EMAIL = "email";
    public static final String VOICESIZE = "voiceSize";
    public static final String PAYTYPE = "payType";
    public static final String MAXSTATUS = "maxStatus";
    //js page type
    public static final String COMPLAIN = "complain";

    //event bus
    public static final int REFRESH_ADDRESS_LIST = 14;
    //添加地址成功
    public static final int ADDADDRESS_SUCCEED = 11;
    //编辑地址成功
    public static final int COMPILEADDRESS_SUCCEED = 12;
    //删除地址成功
    public static final int DELADDRESS_SUCCEED = 13;
    //创建订单成功
    public static final int CREATE_ORDER_SUCCEED = 14;
    //取消订单成功
    public static final int CANCEL_ORDER_SUCCEED = 16;
    //删除订单成功
    public static final int DEL_ORDER_SUCCEED = 98;
    //修改壁纸成功
    public static final int CHANGE_WALLPAGER_SUCCEED = 99;
    //发送订单成功（随机商家\指定商家）
    public static final int SEND_ORDER_SUCCEED = 17;
    //获取定位成功
    public static final int GET_LOCATION_SUCCEED = 18;
    //录音成功
    public static final int RECORD_SUCCEED = 15;
    //录音成功
    public static final int MODIFY_PERSONAL_SUCCESS = 115;

    //清除未读消息成功
    public static final int CLEAR_UNREAD_SUCCEED = 28;


    //店铺详情店址选择回调
    public static final int STORE_DETAIL_ADDRESS_RESULT = 151;

    //首页址选择回调
    public static final int MAIN_ADDRESS_RESULT = 152;
    //发送订单地址选择回调
    public static final int SEND_ORDER_ADDRESS_RESULT = 156;
    //首页址选择回调
    public static final int MAIN_SEND_DIALOG_ADDRESS_RESULT = 154;
    //发送订单选择地址回调
    public static final int SEND_ORDER_RESULT = 153;
    //订单详情刷新时间
    public static final int ORDER_DETAIL_REFRESH_TIME = 30000;

    //umeng app key
    public static final String UMENG_APP_KEY = "6073de2b18b72d2d244f05cf";
    //login type
    public static final String WECHAT = "wechat";
    public static final String ALIPAY = "alipay";

    public static final int COMMONLY_USED_STORE_HORIZONTAL = 1;

    public static final int COMMONLY_USED_STORE_VERTICAL = 2;

    public static final int VOICE_ORDER_TYPE = 3;

    public static final int TEXT_ORDER_TYPE = 4;

    public static final int WAITING_EVALUATION = 5;

    public static final int HAVE_EVALUATION = 6;


    public static final int ITEMS1 = 7;
    public static final int ITEMS2 = 8;
    public static final int ITEMS3 = 9;

    public static final String STATUS = "status";
    public static final String DATE = "date";

    public static final String WITHDRAWAL_PASSWORD = "withdrawalPassword";
    public static final String AMOUNT = "amount";
    public static final String BANK_CARD_ID = "bankCardId";

    public static final String ACTIVE_CODE = "activeCode";

    public static final String UPDATE_SUCCESS = "update_success";


}
