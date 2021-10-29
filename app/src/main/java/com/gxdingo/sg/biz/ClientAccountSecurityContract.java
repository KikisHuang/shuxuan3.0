package com.gxdingo.sg.biz;

import com.gxdingo.sg.bean.ClientAccountTransactionBean;
import com.gxdingo.sg.bean.ClientCashInfoBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

import java.util.List;

/**
 * @author: Weaving
 * @date: 2021/10/15
 * @page:
 */
public class ClientAccountSecurityContract {

    public interface ClientAccountSecurityPresenter extends MvpPresenter<BasicsListener,ClientAccountSecurityListener>{

        void getAccountRecord(boolean refresh,int status,String date);

        void sendVerificationCode();

//        void saveStatus();

//        void getUserPhone();

        void getCashInfo();

        void bind(String code,int type);

        void bindAli();

        void bindWechat();

        void unbindThirdParty(int type);

        void cash(String pwd);

        void loginOff();
    }

    public interface ClientAccountSecurityListener{

        void onTransactionResult(boolean refresh,List<ClientAccountTransactionBean.ListBean> transactions);

        void onCashInfoResult(ClientCashInfoBean cashInfoBean);

        String getCashAmount();

        long getBackCardId();

        int getType();

//        void setUserPhone(String phone);
//
//        String getCode();

    }
}
