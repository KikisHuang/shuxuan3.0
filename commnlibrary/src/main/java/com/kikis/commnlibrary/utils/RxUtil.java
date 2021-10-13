package com.kikis.commnlibrary.utils;

import android.util.Log;

import com.kikis.commnlibrary.biz.IRxNextListener;
import com.trello.rxlifecycle3.LifecycleProvider;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Kikis on 2018/3/8.
 */

public class RxUtil {
    private static final String TAG = "RxUtil";
    private static Disposable mDisposable;

    /**
     * milliseconds毫秒后执行next操作
     *
     * @param milliseconds
     * @param next
     */
    public static void timer(long milliseconds, final IRxNextListener next, LifecycleProvider lifecycle) {

        Observable observable = Observable.timer(milliseconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread());
        if (lifecycle != null)
            observable.compose(lifecycle.bindToLifecycle());

        observable.subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {
                mDisposable = disposable;
            }

            @Override
            public void onNext(@NonNull Long number) {
                if (next != null) {
                    next.doNext(number);
                    onComplete();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                //取消订阅
                cancel();
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "====延时定时器取消======");
                //取消订阅
                cancel();
            }
        });
    }

    /**
     * 每隔milliseconds毫秒后执行next操作
     *
     * @param milliseconds
     * @param next
     */
    public static void intervals(long milliseconds, final IRxNextListener next, LifecycleProvider lifecycle) {

        Observable observable = Observable.interval(milliseconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread());
        if (lifecycle != null)
            observable.compose(lifecycle.bindToLifecycle());

        observable.subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {
                mDisposable = disposable;
            }

            @Override
            public void onNext(@NonNull Long number) {
                if (next != null) {
                    next.doNext(number);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    /**
     * 将一些重复的操作提出来
     * 网络请求Scheduler使用Schedulers.io() 、线程操作使用Schedulers.newThread()方法
     * <p>
     * 网络请求使用Schedulers.io()的好处在于它使用线程池的事实，避免了重复创建浪费的资源，而Schedulers.newThread()是新线程。
     *
     * @param <T>
     * @param observable
     * @return Observable
     * <p>
     * RxUtil.observe(Observable.create(new ObservableOnSubscribe<Object>() {
     * @Override public void subscribe(ObservableEmitter<Object> e) throws Exception {
     * <p>
     * }
     * }), (InitActivity) getActivity()).subscribe(new Consumer<Object>() {
     * @Override public void accept(Object object) throws Exception {
     * <p>
     * }
     * });
     */
    public static <T> Observable<T> observe(Scheduler scheduler, Observable<T> observable, LifecycleProvider lifecycle) {

        Observable<T> obs = observable;
        if (lifecycle != null)
            obs.compose(lifecycle.bindToLifecycle());
        obs.subscribeOn(scheduler)
                .unsubscribeOn(scheduler)
                .observeOn(AndroidSchedulers.mainThread());
        return obs;
    }

    /**
     * 背压版
     * <p>
     * RxUtil.observe(Schedulers.newThread(),Flowable.create(new FlowableOnSubscribe<Object>() {
     *
     * @param scheduler
     * @param observable
     * @param lifecycle
     * @param <T>
     * @return
     * @Override public void subscribe(FlowableEmitter<Object> emitter) {
     * <p>
     * }
     * },BackpressureStrategy.ERROR),reference.get()).subscribe(new Consumer<Object>() {
     * @Override public void accept(Object o) {
     * <p>
     * }
     * });
     */
    public static <T> Flowable<T> observe(Scheduler scheduler, Flowable<T> observable, LifecycleProvider lifecycle) {

        Flowable<T> obs = observable;
        if (lifecycle != null)
            obs.compose(lifecycle.bindToLifecycle());
        obs.subscribeOn(scheduler)
                .unsubscribeOn(scheduler)
                .observeOn(AndroidSchedulers.mainThread());
        return obs;
    }

    /**
     * 取消订阅
     */
    public static void cancel() {
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
            Log.e(TAG, "====定时器取消======");
        }
    }
}
