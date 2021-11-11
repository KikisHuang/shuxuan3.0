package com.gxdingo.sg.utils;

import android.media.MediaRecorder;

import com.blankj.utilcode.util.LogUtils;
import com.gxdingo.sg.biz.AudioModelListener;
import com.trello.rxlifecycle3.LifecycleProvider;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static com.blankj.utilcode.util.FileUtils.createOrExistsFile;
import static com.kikis.commnlibrary.utils.Constant.isDebug;

/**
 * @author: Kikis
 * @date: 2021/5/8
 * @page:
 */
public class MediaRecorderUtil {

    public static MediaRecorder recorder;

    private static Disposable mDisposable;

    private static int mCountDownTime;

    private static int mMaxTime = 120;

    public static void startRecordering(String filePath, AudioModelListener audioModelListener, LifecycleProvider lifecycle) {

        if (isDebug)
            LogUtils.i("outputPath === " + filePath);

        try {
            if (recorder != null) {
                recorder.release();
                recorder = null;
            }

            if (createOrExistsFile(filePath)) {

                startTimer(lifecycle, audioModelListener);
                recorder = new MediaRecorder();
                // 设置麦克风为音频源
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                // 设置音频文件的编码
                recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
                //配置采样频率，频率越高月接近原始声音，Android所有设备都支持的采样频率为44100
                recorder.setAudioSamplingRate(44100);
                //配置码率，这里一般通用的是96000
                recorder.setAudioEncodingBitRate(96000);
                // 设置输出文件的格式
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);//可以设置成 MediaRecorder.AudioEncoder.AMR_NB

                recorder.setOutputFile(filePath);

                recorder.prepare();
                recorder.start();
            } else {
                audioModelListener.onAudioError("文件创建失败");
            }

        } catch (Exception e) {
            if (audioModelListener != null)
                audioModelListener.onAudioError("");
            LogUtils.e("start recorder error === " + e);

        }
    }

    private static void startTimer(LifecycleProvider lifecycle, AudioModelListener audioModelListener) {
        if (mDisposable != null)
            dispose();

        mCountDownTime = mMaxTime;
        Observable observable = Observable.interval(1000, TimeUnit.MILLISECONDS)
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
                mCountDownTime--;
                LogUtils.i("mMaxTime === " + mCountDownTime);
                if (mCountDownTime <= 0)
                    onComplete();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if (audioModelListener != null)
                    audioModelListener.onAudioError(e.getMessage());

            }

            @Override
            public void onComplete() {
                if (audioModelListener != null)
                    audioModelListener.onRecorderComplete();
            }
        });
    }

    public static void stopRecordering() {
        dispose();
        if (recorder != null) {

            recorder.setOnErrorListener(null);
            recorder.setOnInfoListener(null);
            recorder.setPreviewDisplay(null);
            try {
                recorder.stop();
                recorder.release();
                recorder = null;
            } catch (Exception e) {
                LogUtils.e("stop recorder error === " + e);
            }

        }
    }


    /**
     * 取消订阅
     */
    public static void dispose() {
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
            LogUtils.w("====定时器取消======");
        }
    }
}
