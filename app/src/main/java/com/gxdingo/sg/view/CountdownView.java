package com.gxdingo.sg.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gxdingo.sg.R;
import com.kikis.commnlibrary.activitiy.BaseActivity;
import com.kikis.commnlibrary.utils.RxUtil;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static com.blankj.utilcode.util.TimeUtils.getNowMills;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/10/18
 * desc   : 验证码倒计时
 */
public final class CountdownView extends AppCompatTextView {

    /**
     * 倒计时秒数
     */
    private int mTotalSecond = 60;
    /**
     * 秒数单位文本
     */
    private static final String TIME_UNIT = "s";

    /**
     * 当前秒数
     */
    private int mCurrentSecond = 0;
    /**
     * 记录原有的文本
     */
    private CharSequence mRecordText;

    private Disposable mDisposable;

    public CountdownView(Context context) {
        super(context);
    }

    public CountdownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CountdownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置倒计时总秒数
     */
    public void setTotalTime(int totalTime) {
        this.mTotalSecond = totalTime;
    }

    /**
     * 获取当前秒数
     *
     * @return
     */
    public int getCurrentSecond() {
        return mCurrentSecond;
    }


    /**
     * 开始倒计时
     */
    public void start() {
        mRecordText = getText();
        setEnabled(false);
        mCurrentSecond = mTotalSecond;

        if (mDisposable != null)
            destroy();

        setText(mCurrentSecond + " " + TIME_UNIT);

        Observable observable = Observable.interval(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable disposable) {
                mDisposable = disposable;
            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull Long number) {
                if (mCurrentSecond > 0) {
                    mCurrentSecond--;
                    setText(mCurrentSecond + " " + TIME_UNIT);
                } else
                    stop();
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
     * 结束倒计时
     */
    public void stop() {
        setText(mRecordText);
        setEnabled(true);
        destroy();
    }

    public void destroy() {
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 设置点击的属性
        setClickable(true);
    }

}