package com.gxdingo.sg.biz;

import android.widget.EditText;

import com.gxdingo.sg.bean.ClientMineBean;
import com.gxdingo.sg.bean.UserBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;
import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientMineContract {

    public interface ClientMinePresenter extends MvpPresenter<BasicsListener, ClientMineListener> {

        void editsetInit(EditText nick_name_edt, int limit);

        void photoItemClick(int pos);

        void getUserInfo();

        void modityNickName(String toString);

        void logout();

        void loginOff(int cancel);

        void scan(RxPermissions rxPermissions);

        void scanCode(String content);

        void getNoRemindContent();

        void storeScanCode(String scanContent);

        void refreshStatus();

        void getArticleImg(String article);
    }

    public interface ClientMineListener<T> {
        void changeAvatar(T t);

        RxPermissions getPermissions();

        void onMineDataResult(ClientMineBean mineBean);

        void onRemindResult(String remindValue);

        void onStatusResult(UserBean userBean);

        //是否需要上传特殊许可证状态
        void onQualification(Object v);
    }
}
