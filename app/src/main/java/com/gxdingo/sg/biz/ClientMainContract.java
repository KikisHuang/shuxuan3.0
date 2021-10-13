package com.gxdingo.sg.biz;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gxdingo.sg.presenter.ClientMainPresenter;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

import java.util.List;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientMainContract {

    public interface ClientMainPresenter extends MvpPresenter<BasicsListener,ClientMainListener>{

        void persenterInit();

        void checkTab(int tab);

        void getSocketUrl();

        void destroySocket();
    }

    public interface ClientMainListener{

        List<Fragment> getFragmentList();

        FragmentTransaction getFragmentTransaction();

        void showFragment(FragmentTransaction fragmentTransaction, int index);

        void hideFragment(int index);

        void onSeleted(int checkTab, int oldTab);
    }

    public interface ClientMainModelListener {

        void fmResult(int index);

    }
}
