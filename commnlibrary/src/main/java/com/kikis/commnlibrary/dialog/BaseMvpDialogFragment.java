package com.kikis.commnlibrary.dialog;

import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.kikis.commnlibrary.activitiy.BaseActivity;
import com.kikis.commnlibrary.biz.MvpPresenter;
import com.kikis.commnlibrary.fragment.BaseFragment;


/**
 * Created by Kikis on 2018/8/20.
 * 纯粹的 MVP 包装，不要增加任何View层基础功能
 * 如果要添加基类功能，请在{@link BaseFragment} 中添加
 */

public abstract class BaseMvpDialogFragment<P extends MvpPresenter> extends BaseFragmentDialog {


    private P presenter;


    protected abstract P createPresenter();

    protected void registerPresenter() {
        presenter = createPresenter();
        if (presenter == null) {
            throw new NullPointerException("Presenter is null! Do you return null in createPresenter()?");
        }

        presenter.onMvpAttachView(reference.get(),this,this);


    }

    public P getP() {
        return presenter;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.onMvpStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.onMvpResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (presenter != null) {
            presenter.onMvpPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.onMvpStop();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        try {
            super.onSaveInstanceState(outState);
        } catch (Exception e) {
            LogUtils.e(" onSaveInstanceState error === " + e);
        }
        if (presenter != null) {
            presenter.onMvpSaveInstanceState(outState);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onMvpDetachView(false);
            presenter.onMvpDestroy();
        }
    }


}
