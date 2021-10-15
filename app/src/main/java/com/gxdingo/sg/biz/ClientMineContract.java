package com.gxdingo.sg.biz;

import android.widget.EditText;

import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;
import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientMineContract {

    public interface ClientMinePresenter extends MvpPresenter<BasicsListener,ClientMineListener>{
        void editsetInit(EditText nick_name_edt, int limit);

        void photoItemClick(int pos);

        void getUserInfo();

        void modityNickName(String toString);
    }

    public interface ClientMineListener<T>{
        void changeAvatar(T t);

        RxPermissions getPermissions();
    }
}
