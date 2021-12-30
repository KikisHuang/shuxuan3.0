package com.gxdingo.sg.biz;

import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

/**
 * @author: Kikis
 * @date: 2021/12/30
 * @page:
 */
public class AuthenticationContract {

    public interface AuthenticationPresenter extends MvpPresenter<BasicsListener, AuthenticationListener> {

        void photoItemClick(int pos, int type);
    }

    public interface AuthenticationListener {

        void upLoadSucceed(String path, int selectedType);
    }
}
