package com.kikis.commnlibrary.presenter;

import android.content.Context;
import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;
import com.trello.rxlifecycle3.LifecycleProvider;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * Created by Kikis on 2018/8/20.
 * Presenter生命周期包装、View的绑定和解除，P层实现的基类
 * BV 基础v
 * v 当前模块v
 */

public class BaseMvpPresenter<BV, V> implements MvpPresenter<BV, V> {


    private WeakReference<V> viewRef;

    private WeakReference<BV> bviewRef;

    private Context mContext;

    private CompositeDisposable mCompositeDisposable;

    protected V getV() {
        try {
            return viewRef.get();
        } catch (Exception e) {
            LogUtils.e("BaseMvpPresenter contextWeak error == " + e);
            return null;
        }
    }


    protected BV getBV() {
        try {
            return bviewRef.get();
        } catch (Exception e) {
            LogUtils.e("BaseMvpPresenter contextWeak error == " + e);
            return null;
        }
    }


    protected Context getContext() {
        return mContext;
    }


    protected boolean isBViewAttached() {
        return bviewRef != null && bviewRef.get() != null;
    }

    protected boolean isViewAttached() {
        return viewRef != null && viewRef.get() != null;
    }

    protected void _attach(Context context, V view, BV bview) {
        this.mContext = context;
        viewRef = new WeakReference<V>(view);
        bviewRef = new WeakReference<BV>(bview);
    }

    @Override
    public void onMvpAttachView(Context context, BV bview, V view) {
        _attach(context, view, bview);
    }

    @Override
    public void onMvpStart() {

    }

    @Override
    public void onMvpResume() {

    }

    @Override
    public void onMvpPause() {

    }

    @Override
    public void onMvpStop() {

    }

    @Override
    public void onMvpSaveInstanceState(Bundle savedInstanceState) {

    }


    /**
     * 添加  Disposable
     *
     * @param disposable
     */
    protected void addDisposable(Disposable disposable) {
        if (this.mCompositeDisposable == null) {
            this.mCompositeDisposable = new CompositeDisposable();
        }
        this.mCompositeDisposable.add(disposable);

    }

    /**
     * 清除disposable
     */
    protected void clearDisposable() {
        if (this.mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            this.mCompositeDisposable.clear();
            mCompositeDisposable = null;
        }
    }


    private void _detach(boolean retainInstance) {
        if (viewRef != null) {

            viewRef.clear();
            viewRef = null;
        }
        if (bviewRef != null) {

            bviewRef.clear();
            bviewRef = null;
        }
        clearDisposable();
    }

    @Override
    public void onMvpDetachView(boolean retainInstance) {
        _detach(retainInstance);
    }

    @Override
    public void onMvpDestroy() {

    }

    protected void onBack() {

    }
}
