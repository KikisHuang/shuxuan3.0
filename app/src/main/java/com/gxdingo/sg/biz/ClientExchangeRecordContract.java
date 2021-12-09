package com.gxdingo.sg.biz;

import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

/**
 * @author: Weaving
 * @date: 2021/12/9
 * @page:
 */
public class ClientExchangeRecordContract {

    public interface  ClientExchangeRecordPresenter extends MvpPresenter<BasicsListener,ClientExchangeRecordListener>{

    }

    public interface ClientExchangeRecordListener{}
}
