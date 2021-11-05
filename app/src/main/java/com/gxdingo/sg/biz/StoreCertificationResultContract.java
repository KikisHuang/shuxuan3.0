package com.gxdingo.sg.biz;

import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

/**
 * 商家认证结果契约类
 *
 * @author JM
 */
public class StoreCertificationResultContract {

    public interface StoreCertificationResultPresenter extends MvpPresenter<BasicsListener, StoreCertificationResultContract.StoreCertificationResultListener> {

    }

    public interface StoreCertificationResultListener {

    }
}
