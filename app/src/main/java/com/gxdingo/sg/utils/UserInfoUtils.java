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
     * ??????????????????
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
            //????????????
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

            //??????IM??????????????????
            if (ImMessageUtils.getInstance().isRunning())
                ImServiceUtils.stopImService();

        }
    }


    /**
     * ??????token
     *
     * @param token
     */
    public void saveUserToken(String token) {
        BaseLogUtils.w("?????????token === " + token);

        SPUtils.getInstance().put(Constant.TOKEN_KEY, token);
    }

    /**
     * ??????storeId
     *
     * @param userId
     */
    public void saveUserId(int userId) {
        BaseLogUtils.w("????????? userId  === " + userId);
        SPUtils.getInstance().put(USER_ID_KEY, userId);
    }


    /**
     * ??????????????????
     *
     * @return
     */
    public void saveUserAvatar(String avatar) {
        SPUtils.getInstance().put(USER_AVATAR_KEY, avatar);
    }

    /**
     * ??????????????????
     *
     * @return
     */
    public void saveUserPhone(String phone) {
        SPUtils.getInstance().put(USER_PHONE_KEY, phone);
    }

    /**
     * ??????Identifier
     *
     * @return
     */
    public void saveIdentifier(String identifier) {
        SPUtils.getInstance().put(USER_IDENTIFIER, identifier);
    }

    /**
     * ??????saveEMAS
     *
     * @return
     */
    public void saveEMAS(String emas) {
        SPUtils.getInstance().put(USER_EMAS, emas);
    }

    /**
     * ??????????????????
     *
     * @return
     */
    public void saveUserNickName(String name) {
        SPUtils.getInstance().put(USER_NICKNAME_KEY, name);
    }

    /**
     * ??????????????????
     *
     * @return
     */
    public void saveWallpaper(String wallpager) {
        SPUtils.getInstance().put(USER_WALLPAGER_KEY, wallpager);
    }

    /**
     * ??????openid
     *
     * @return
     */
    public void saveOpenId(String name) {
        SPUtils.getInstance().put(USER_OPENID_KEY, name);
    }


    /**
     * /**
     * ??????????????????
     */
    public void clearLoginStatus() {
        saveLoginUserInfo(null);
    }

    /**
     * ????????????????????????;
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
     * ??????????????????
     *
     * @param context
     */
    public void goToOauthPage(Context context) {
        goToPage(context, OauthActivity.class, null);
    }

    /**
     * ??????????????????
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
     * ??????????????????
     *
     * @param userBean
     */
    public void saveUserInfo(UserBean userBean) {
        SPUtils.getInstance().put(USER_INFO_KEY, GsonUtil.gsonToStr(userBean));
    }

    /**
     * ??????token
     *
     * @return
     */
    public String getUserToken() {
        String token = SPUtils.getInstance().getString(Constant.TOKEN_KEY, "");
        BaseLogUtils.i("token === " + token);

        return token;
    }

    /**
     * ??????StoreId
     *
     * @return
     */
    public int getUserId() {
        return SPUtils.getInstance().getInt(USER_ID_KEY, 0);
    }


    /**
     * ??????????????????
     *
     * @return
     */
    public String getUserAvatar() {
        return SPUtils.getInstance().getString(USER_AVATAR_KEY);
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    public String getUserPhone() {
        return SPUtils.getInstance().getString(USER_PHONE_KEY, "");
    }

    /**
     * ??????OpenId
     *
     * @return
     */
    public String getUserOpenId() {
        return SPUtils.getInstance().getString(USER_OPENID_KEY);
    }


    /**
     * ??????????????????
     *
     * @return
     */
    public String getUserNickName() {
        return SPUtils.getInstance().getString(USER_NICKNAME_KEY);
    }


    /**
     * ??????????????????
     *
     * @return
     */
    public String getWallpaper() {
        return SPUtils.getInstance().getString(USER_WALLPAGER_KEY);
    }

    /**
     * ??????Identifier
     *
     * @return
     */
    public String getIdentifier() {
        return SPUtils.getInstance().getString(USER_IDENTIFIER);
    }

    /**
     * ??????emas
     *
     * @return
     */
    public String getEMAS() {
        return SPUtils.getInstance().getString(USER_EMAS);
    }

    /**
     * ??????????????????
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
     * ??????????????????
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
