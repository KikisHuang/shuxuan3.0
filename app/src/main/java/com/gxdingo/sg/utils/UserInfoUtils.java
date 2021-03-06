package com.gxdingo.sg.utils;

import android.content.Context;

//import com.alibaba.sdk.android.push.CloudPushService;
//import com.alibaba.sdk.android.push.CommonCallback;
//import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.gxdingo.sg.R;
//import com.gxdingo.sg.activity.LoginActivity;
import com.gxdingo.sg.activity.LoginActivity;
import com.gxdingo.sg.activity.OauthActivity;
import com.gxdingo.sg.bean.UserBean;
import com.kikis.commnlibrary.utils.BaseLogUtils;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.utils.GsonUtil;
import com.kikis.commnlibrary.utils.MessageCountManager;

import org.greenrobot.eventbus.EventBus;

import static android.text.TextUtils.isEmpty;
import static com.gxdingo.sg.utils.ClientLocalConstant.USER_AVATAR_KEY;
import static com.gxdingo.sg.utils.ClientLocalConstant.USER_EMAS;
import static com.gxdingo.sg.utils.ClientLocalConstant.USER_IDENTIFIER;
import static com.gxdingo.sg.utils.ClientLocalConstant.USER_ID_KEY;
import static com.gxdingo.sg.utils.ClientLocalConstant.USER_INFO_KEY;
import static com.gxdingo.sg.utils.ClientLocalConstant.USER_NICKNAME_KEY;
import static com.gxdingo.sg.utils.ClientLocalConstant.USER_OPENID_KEY;
import static com.gxdingo.sg.utils.ClientLocalConstant.USER_PHONE_KEY;
import static com.gxdingo.sg.utils.ClientLocalConstant.USER_WALLPAGER_KEY;
import static com.gxdingo.sg.utils.LocalConstant.ADDRESS_CACHE;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.Constant.LOGOUT;
import static com.kikis.commnlibrary.utils.Constant.isDebug;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPage;
import static com.kikis.commnlibrary.utils.MyToastUtils.customToast;

public class UserInfoUtils {

    public static UserInfoUtils instance;


    public static UserInfoUtils getInstance() {
        if (instance == null) {
            synchronized (UserInfoUtils.class) {
                instance = new UserInfoUtils();
            }
        }
        return instance;
    }

    /**
     * 保存用户信息
     */
    public void saveLoginUserInfo(UserBean userBean) {
        if (userBean != null) {

            if (!isEmpty(userBean.getToken()))
                saveUserToken(userBean.getToken());

//            saveUserId(userBean.getUserId());
            saveUserAvatar(userBean.getAvatar());
            saveUserPhone(userBean.getMobile());
            saveUserNickName(userBean.getNickname());
            saveIdentifier(userBean.getIdentifier());
            saveOpenId(userBean.getOpenid());
            saveEMAS(userBean.getEmasId());
            SPUtils.getInstance().put(USER_INFO_KEY, GsonUtil.gsonToStr(userBean));
            bindPushAccount();
        } else {
            //退出登录
            unbindPushAccount();
            SPUtils.getInstance().put(USER_INFO_KEY, "");
            saveUserId(0);
            saveUserAvatar("");
            saveUserNickName("");
            saveUserToken("");
            saveIdentifier("");
            saveEMAS("");
            saveWallpaper("");
            saveOpenId("");
            MessageCountManager.getInstance().setUnreadMessageNum(0);

            SPUtils.getInstance().put(ADDRESS_CACHE, "");
            EventBus.getDefault().post(LOGOUT);

            //停止IM消息接收服务
            if (ImMessageUtils.getInstance().isRunning())
                ImServiceUtils.stopImService();

        }
    }


    /**
     * 保存token
     *
     * @param token
     */
    public void saveUserToken(String token) {
        BaseLogUtils.w("保存的token === " + token);

        SPUtils.getInstance().put(Constant.TOKEN_KEY, token);
    }

    /**
     * 保存storeId
     *
     * @param userId
     */
    public void saveUserId(int userId) {
        BaseLogUtils.w("保存的 userId  === " + userId);
        SPUtils.getInstance().put(USER_ID_KEY, userId);
    }


    /**
     * 保存用户头像
     *
     * @return
     */
    public void saveUserAvatar(String avatar) {
        SPUtils.getInstance().put(USER_AVATAR_KEY, avatar);
    }

    /**
     * 保存用户手机
     *
     * @return
     */
    public void saveUserPhone(String phone) {
        SPUtils.getInstance().put(USER_PHONE_KEY, phone);
    }

    /**
     * 保存Identifier
     *
     * @return
     */
    public void saveIdentifier(String identifier) {
        SPUtils.getInstance().put(USER_IDENTIFIER, identifier);
    }

    /**
     * 保存saveEMAS
     *
     * @return
     */
    public void saveEMAS(String emas) {
        SPUtils.getInstance().put(USER_EMAS, emas);
    }

    /**
     * 保存用户昵称
     *
     * @return
     */
    public void saveUserNickName(String name) {
        SPUtils.getInstance().put(USER_NICKNAME_KEY, name);
    }

    /**
     * 保存用户壁纸
     *
     * @return
     */
    public void saveWallpaper(String wallpager) {
        SPUtils.getInstance().put(USER_WALLPAGER_KEY, wallpager);
    }

    /**
     * 保存openid
     *
     * @return
     */
    public void saveOpenId(String name) {
        SPUtils.getInstance().put(USER_OPENID_KEY, name);
    }


    /**
     * /**
     * 清除登录状态
     */
    public void clearLoginStatus() {
        saveLoginUserInfo(null);
    }

    /**
     * 登录判断通用方法;
     */
    public boolean isLogin() {
        if (getUserToken() == null || getUserToken().equals("") || getUserInfo() == null || isEmpty(getUserInfo().getCrossToken()))
            return false;
        else
            return true;

    }

    public void goToLoginPage(Context context, String errormsg) {
        goToPage(context, LoginActivity.class, null);
        if (!isEmpty(errormsg))
            customToast(errormsg);
        else
            customToast(gets(R.string.please_login));

    }

    /**
     * 跳转一键登录
     *
     * @param context
     */
    public void goToOauthPage(Context context) {
        goToPage(context, OauthActivity.class, null);
    }

    /**
     * 获取店铺信息
     *
     * @return
     */
    public UserBean getUserInfo() {
        UserBean userBean;
        try {
            String json = SPUtils.getInstance().getString(USER_INFO_KEY);
            userBean = GsonUtil.GsonToBean(json, UserBean.class);
        } catch (Exception e) {
            BaseLogUtils.e("get shop info Error === " + e);
            return null;
        }
        return userBean;
    }

    /**
     * 保存用户信息
     *
     * @param userBean
     */
    public void saveUserInfo(UserBean userBean) {
        SPUtils.getInstance().put(USER_INFO_KEY, GsonUtil.gsonToStr(userBean));
    }

    /**
     * 获取token
     *
     * @return
     */
    public String getUserToken() {
        String token = SPUtils.getInstance().getString(Constant.TOKEN_KEY, "");
        BaseLogUtils.i("token === " + token);

        return token;
    }

    /**
     * 获取StoreId
     *
     * @return
     */
    public int getUserId() {
        return SPUtils.getInstance().getInt(USER_ID_KEY, 0);
    }


    /**
     * 获取用户头像
     *
     * @return
     */
    public String getUserAvatar() {
        return SPUtils.getInstance().getString(USER_AVATAR_KEY);
    }

    /**
     * 获取用户手机号
     *
     * @return
     */
    public String getUserPhone() {
        return SPUtils.getInstance().getString(USER_PHONE_KEY, "");
    }

    /**
     * 获取OpenId
     *
     * @return
     */
    public String getUserOpenId() {
        return SPUtils.getInstance().getString(USER_OPENID_KEY);
    }


    /**
     * 获取用户昵称
     *
     * @return
     */
    public String getUserNickName() {
        return SPUtils.getInstance().getString(USER_NICKNAME_KEY);
    }


    /**
     * 获取用户壁纸
     *
     * @return
     */
    public String getWallpaper() {
        return SPUtils.getInstance().getString(USER_WALLPAGER_KEY);
    }

    /**
     * 获取Identifier
     *
     * @return
     */
    public String getIdentifier() {
        return SPUtils.getInstance().getString(USER_IDENTIFIER);
    }

    /**
     * 获取emas
     *
     * @return
     */
    public String getEMAS() {
        return SPUtils.getInstance().getString(USER_EMAS);
    }

    /**
     * 绑定推送账号
     */
    public void bindPushAccount() {

        if (UserInfoUtils.getInstance().isLogin() && !isEmpty(UserInfoUtils.getInstance().getIdentifier())) {

            CloudPushService pushService = PushServiceFactory.getCloudPushService();
            if (pushService == null) {
                LogUtils.w("pushService is null ");
                return;
            }
            PushServiceFactory.getCloudPushService().bindAccount(UserInfoUtils.getInstance().getEMAS(), new CommonCallback() {
                @Override
                public void onSuccess(String s) {
                    if (isDebug)
                        LogUtils.i("bind account success account == " + UserInfoUtils.getInstance().getEMAS());
                }

                @Override
                public void onFailed(String s, String s1) {
                    if (isDebug)
                        LogUtils.e("bind account onfailed ");
                }
            });
        }

    }

    /**
     * 解绑推送账号
     */
    public void unbindPushAccount() {

        CloudPushService pushService = PushServiceFactory.getCloudPushService();

        pushService.unbindAccount(new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                LogUtils.d("unbind Account success ");
            }

            @Override
            public void onFailed(String s, String s1) {
                LogUtils.d("unbind Account onfailed ");
            }
        });
    }
}
