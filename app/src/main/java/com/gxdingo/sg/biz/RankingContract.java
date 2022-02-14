package com.gxdingo.sg.biz;

import android.content.Context;

import com.gxdingo.sg.bean.CategoriesBean;
import com.gxdingo.sg.bean.RankListBean;
import com.gxdingo.sg.bean.ShareBean;
import com.gxdingo.sg.bean.StoreListBean;
import com.kikis.commnlibrary.bean.AddressBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class RankingContract {

    public interface RankingPresenter extends MvpPresenter<BasicsListener, RankingListener> {

        void getRankingDataList(String cycle);

    }

    public interface RankingListener {

        void onRankingListResult(RankListBean data);

    }
}
