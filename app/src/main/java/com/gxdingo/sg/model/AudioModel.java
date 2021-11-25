package com.gxdingo.sg.model;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.alibaba.idst.nui.CommonUtils;
import com.alibaba.idst.nui.Constants;
import com.alibaba.idst.nui.INativeFileTransCallback;
import com.alibaba.idst.nui.NativeNui;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.AudioModelListener;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.utils.LocalConstant;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.utils.GsonUtil;
import com.kikis.commnlibrary.utils.RxUtil;
import com.trello.rxlifecycle3.LifecycleProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.DeviceUtils.getUniqueDeviceId;
import static com.blankj.utilcode.util.FileUtils.createOrExistsDir;
import static com.blankj.utilcode.util.FileUtils.deleteAllInDir;
import static com.gxdingo.sg.utils.ClientLocalConstant.RECORD_SUCCEED;
import static com.gxdingo.sg.utils.LocalConstant.AAC;
import static com.gxdingo.sg.utils.MediaRecorderUtil.startRecordering;
import static com.gxdingo.sg.utils.MediaRecorderUtil.stopRecordering;
import static com.kikis.commnlibrary.utils.CommonUtils.getPath;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.PermissionUtils.TAG;

/**
 * @author: Kikis
 * @date: 2021/4/20
 * @page:音频的model类
 */
public class AudioModel {

    private MediaPlayer mediaPlayer;

    //当前录音路径
    private String mRecordPath = "";
    //当前播放路径
    private String mPlayerPath = "";

    private boolean mIsRecording = false;

    private HandlerThread mHanderThread;

    public static AudioModel instance;

    private AudioModel() {
        mHanderThread = new HandlerThread("process_thread");
        mHanderThread.start();
    }

    public static AudioModel getInstance() {
        if (instance == null) {
            synchronized (AudioModel.class) {
                if (instance == null) {
                    instance = new AudioModel();
                }
            }
        }
        return instance;
    }


    /**
     * 音频播放
     *
     * @param path
     */
    public void audioPlayer(String path, AudioModelListener audioModelListener) {



        //如果再播放中，就取消播放
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            destroy();
            //相同的播放路径，取消播放
            if (mPlayerPath.equals(path))
                return;
        }

        mediaPlayer = new MediaPlayer();

        mPlayerPath = path;
        try {

            if (isEmpty(path)) {
                if (audioModelListener != null)
                    audioModelListener.onAudioMessage(gets(R.string.not_get_voice_player_url));
                return;
            }

            mediaPlayer.setDataSource(path);//指定音频文件路径
            mediaPlayer.setLooping(false);//循环播放
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();//初始化播放器MediaPlayer
//            mediaPlayer.start();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                destroy();
                return false;
            });

            mediaPlayer.setOnCompletionListener(mp -> destroy());

        } catch (Exception e) {
            if (audioModelListener != null) {
                audioModelListener.onAudioMessage(gets(R.string.audio_play_error));
                audioModelListener.onAudioError(e.getMessage());
            }
            LogUtils.i("path === " + path);
            LogUtils.e("audio play error === " + e);
            destroy();
        }
    }

    public boolean isPlaying() {
        if (mediaPlayer == null)
            return false;

        return mediaPlayer.isPlaying();
    }

    /**
     * 音频暂停
     */
    public void audioPause() {
        //如果在播放中，立刻暂停。
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.reset();
            stopRecordering();
        }
    }

    /**
     * 获取音频时长
     *
     * @return
     */
    public int getDuration() {
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(mRecordPath);//指定音频文件路径
            mediaPlayer.prepare();//初始化播放器MediaPlayer

            return mediaPlayer.getDuration() / 1000;
        } catch (IOException e) {
            LogUtils.e("getDuration error === " + e);
        }
        return 0;
    }

    /**
     * 音频销毁
     */
    public void destroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void delAudioFile() {
        LogUtils.w("删除音频文件夹内的音频文件  === " + deleteAllInDir(getPath() + File.separator + "audio"));
    }


    /**
     * 获取当前录音路径
     *
     * @return
     */
    public String getRecordPath() {
        return mRecordPath;
    }

    /**
     * 获取当前播放路径
     *
     * @return
     */
    public String getPlayerPath() {
        return mPlayerPath;
    }

    /**
     * 是否在录音
     *
     * @return
     */
    public boolean isRecording() {
        return mIsRecording;
    }


    /**
     * 开始录音
     */
    public void startRecorder(NetWorkListener listener, LifecycleProvider lifecycle) {

        mIsRecording = true;

        mRecordPath = getPath() + File.separator + "audio" + File.separator + UUID.randomUUID().toString() + AAC;

        startRecordering(mRecordPath, new AudioModelListener() {
            @Override
            public void onAudioMessage(String msg) {

            }

            @Override
            public void onAudioError(String ermsg) {
                if (!isEmpty(ermsg))
                    listener.onMessage(ermsg);
                stopRecordering();
                mIsRecording = false;
            }

            @Override
            public void onRecorderComplete() {
                if (listener != null) {
                    listener.onMessage("录制已超过最大时间");
                    listener.onSucceed(RECORD_SUCCEED);
                }
            }
        }, lifecycle);
    }

    /**
     * 停止录音
     */
    public void stopRecorder() {
        mIsRecording = false;
        stopRecordering();
    }


    /**
     * 删除录音
     */
    public void removeRecord() {
        if (FileUtils.delete(mRecordPath)) {
            LogUtils.i("删除录音成功");
            mRecordPath = "";
        } else
            LogUtils.i("删除录音失败");
    }

}
