package com.gxdingo.sg.biz;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gxdingo.sg.bean.NumberUnreadCommentsBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

import java.util.List;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientMainContract {

    public interface ClientMainPresenter extends MvpPresenter<BasicsListener, ClientMainListener> {

        void persenterInit();

        void checkTab(int tab);

        void oneKeyLogin(String code );

        void aliLogin();

        void getWechatAuth();

        void wechatLogin(String code);

        void goLogin();

        void getSocketUrl();

        void destroySocket();

        void getAliKey();

        void getUnreadMessageNum();

        void checkNotifications();
    }

    public interface ClientMainListener {

        List<Fragment> getFragmentList();

        FragmentTransaction getFragmentTransaction();

        void showFragment(FragmentTransaction fragmentTransaction, int index);

        void hideFragment(int index);

        void onSeleted(int checkTab, int oldTab);

        /**
         * 设置底部菜单未读消息
         *
         * @param data
         */
        void setUnreadMsgNum(int data);

        /**
         * 设置商圈消息未读
         *
         * @param bean
         */
        void setBusinessUnreadMsgNum(NumberUnreadCommentsBean bean);

        void showNotifyDialog();
    }

    public interface ClientMainModelListener {

        void fmResult(int index);

    }
}
