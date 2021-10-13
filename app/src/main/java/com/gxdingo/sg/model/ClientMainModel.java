package com.gxdingo.sg.model;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.ClientMainContract;
import com.kikis.commnlibrary.activitiy.BaseActivity;
import com.kikis.commnlibrary.utils.RxUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientMainModel {


    private int mOldTab = 0;

    public ClientMainModel() {
    }


    /**
     * fragment初始化
     *
     * @param fragmentTransaction
     * @param mFragmentList
     */
    public void fragmentInit(FragmentTransaction fragmentTransaction, List<Fragment> mFragmentList) {
        if (fragmentTransaction == null || mFragmentList == null || mFragmentList.size() < 3)
            return;

        if (!mFragmentList.get(0).isAdded()) {
            fragmentTransaction.add(R.id.main_ll, mFragmentList.get(0));
            fragmentTransaction.hide(mFragmentList.get(0));
        }
        if (!mFragmentList.get(1).isAdded()) {
            fragmentTransaction.add(R.id.main_ll, mFragmentList.get(1));
            fragmentTransaction.hide(mFragmentList.get(1));
        }
        if (!mFragmentList.get(2).isAdded()) {
            fragmentTransaction.add(R.id.main_ll, mFragmentList.get(2));
            fragmentTransaction.hide(mFragmentList.get(2));
        }
        if (!mFragmentList.get(3).isAdded()) {
            fragmentTransaction.add(R.id.main_ll, mFragmentList.get(3));
            fragmentTransaction.hide(mFragmentList.get(3));
        }

    }

    /**
     * 隐藏fragment
     *
     * @param size
     * @param context
     * @param mainModelListener
     */
    public void hideAllFragment(int size, Context context, ClientMainContract.ClientMainModelListener mainModelListener) {
        RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {
            for (int i = 0; i < size; i++) {
                e.onNext(i);
            }
            e.onComplete();
        }), (BaseActivity) context).subscribe(o -> {
            if (mainModelListener != null)
                mainModelListener.fmResult((int) o);
        });
    }

    /**
     * 记录tab
     *
     * @param tab
     */
    public void recordTab(int tab) {

        mOldTab = tab;
    }

    /**
     * 获取记录tab
     */
    public int getOldTab() {

        return mOldTab;
    }
}
