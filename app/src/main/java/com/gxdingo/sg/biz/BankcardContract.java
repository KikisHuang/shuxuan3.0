package com.gxdingo.sg.biz;

import com.gxdingo.sg.bean.BankcardBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

import java.util.List;

/**
 * @author: Weaving
 * @date: 2021/10/22
 * @page:
 */
public class BankcardContract {

    public interface BankcardPresenter extends MvpPresenter<BasicsListener,BankcardListener>{

        void bindCard();

        void getCardList(boolean refresh);

        void getSupportCardList();

        void sendVerificationCode();

        void delete(long bankCardId);

    }

    public interface BankcardListener{

        void onDataResult(List<BankcardBean> bankcardBeans, boolean refresh);

        String getBankType();
        String getPersonOfCard();
        String getIdCard();
        String getName();
        String getNumber();
        String getMobile();
        String getCode();
    }
}
