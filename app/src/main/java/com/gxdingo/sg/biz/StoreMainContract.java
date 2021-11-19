package com.gxdingo.sg.biz;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

import java.util.List;

/**
 * Created by Kikis on 2020/3/16.
 */

public class StoreMainContract {

    public interface StoreMainPresenter extends MvpPresenter<BasicsListener, StoreMainListener> {

        void persenterInit();

        void checkTab(int tab);

        void play();

        void release();

        void logout();

        void login();

        void getAliKey();

        void getUnreadMessageNum();
    }

    public interface StoreMainListener {

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
         * @param num
         */
        void setBusinessUnreadMsgNum(int num);
    }

    public interface StoreMainModelListener {

        void fmResult(int index);

    }
}