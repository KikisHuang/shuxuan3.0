package com.gxdingo.sg.biz;

import com.gxdingo.sg.bean.BankcardBean;
import com.gxdingo.sg.bean.ClientCashInfoBean;
import com.gxdingo.sg.bean.TransactionBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

import java.util.ArrayList;
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

        void unbind(int type);

        void bindAli();

        void bindWechat();

        void unbindThirdParty(int type);

        void cash(String pwd);

        void loginOff();

        void getCardList(boolean b);
    }

    public interface ClientAccountSecurityListener{

        void onTransactionResult(boolean refresh,List<TransactionBean> transactions);

        void onCashInfoResult(ClientCashInfoBean cashInfoBean);

        String getCashAmount();

        long getBackCardId();

        int getType();

        void onDataResult(ArrayList<BankcardBean> list, boolean b);

//        void setUserPhone(String phone);
//
//        String getCode();

    }
}
