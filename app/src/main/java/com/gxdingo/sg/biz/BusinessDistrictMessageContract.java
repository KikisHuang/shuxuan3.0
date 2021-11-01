package com.gxdingo.sg.biz;

import com.gxdingo.sg.bean.UpLoadBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

/**
 * 商圈消息契约类
 *
 * @author JM
 */
public class BusinessDistrictMessageContract {

    public interface BusinessDistrictMessagePresenter extends MvpPresenter<BasicsListener, BusinessDistrictMessageListener> {


    }

    public interface BusinessDistrictMessageListener {


    }
}
