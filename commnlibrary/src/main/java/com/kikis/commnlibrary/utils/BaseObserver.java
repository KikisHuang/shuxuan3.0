package com.kikis.commnlibrary.utils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.kikis.commnlibrary.utils.CommonUtils.getTAG;

/**
 * Created by Kikis on 2018/7/10.
 */

public class BaseObserver<T> implements Observer<T> {

    private static final String TAG = getTAG(BaseObserver.class);
    protected String errMsg = "";
    protected Disposable disposable;

    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(T t) {
        onComplete();
    }

    @Override
    public void onError(Throwable e) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void onComplete() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
