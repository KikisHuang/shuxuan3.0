package com.gxdingo.sg.biz;

import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientBusinessDistrictContract {

    public interface ClientBusinessPresenter extends MvpPresenter<BasicsListener,ClientBusinessDistrictListener>{

    }

    public interface ClientBusinessDistrictListener{

    }
}
