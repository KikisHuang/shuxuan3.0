package com.gxdingo.sg.biz;

import com.gxdingo.sg.bean.ClientCouponBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

import java.util.List;

/**
 * @author: Weaving
 * @date: 2021/10/29
 * @page:
 */
public class ClientCouponContract {

    public interface ClientCouponPresenter extends MvpPresenter<BasicsListener,ClientCouponListener>{
        void receive();

        void getCoupons(boolean refresh);
    }

    public interface ClientCouponListener{
        String getCode();

        void onCouponsResult(boolean refresh,List<ClientCouponBean> couponBeans);
    }
}
