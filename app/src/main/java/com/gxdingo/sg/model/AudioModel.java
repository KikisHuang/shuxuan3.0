package com.gxdingo.sg.model;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
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
import com.gxdingo.sg.utils.MediaUtils;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.utils.BaseLogUtils;
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


import static android.text.TextUtils.isEmpty;
import static anetwork.channel.monitor.Monitor.stop;
import static com.blankj.utilcode.util.DeviceUtils.getUniqueDeviceId;
import static com.blankj.utilcode.util.FileUtils.createOrExistsDir;
import static com.blankj.utilcode.util.FileUtils.deleteAllInDir;
import static com.gxdingo.sg.utils.ClientLocalConstant.RECORD_SUCCEED;
import static com.gxdingo.sg.utils.LocalConstant.AAC;
import static com.gxdingo.sg.utils.MediaRecorderUtil.startRecordering;
import static com.gxdingo.sg.utils.MediaRecorderUtil.stopRecordering;
import static com.gxdingo.sg.utils.MediaUtils.closeMedia;
import static com.gxdingo.sg.utils.MediaUtils.createMedia;
import static com.gxdingo.sg.utils.MediaUtils.isplay;
import static com.gxdingo.sg.utils.MediaUtils.pause;
import static com.gxdingo.sg.utils.MediaUtils.play;
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
    //是否录音中
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
     * @param context
     * @param path
     */
    public void audioPlayer(Context context, String path, AudioModelListener audioModelListener) {

        //如果再播放中，就取消播放
        if (mediaPlayer != null && isplay(mediaPlayer)) {
            destroy();
            //相同的播放路径，取消播放
            if (mPlayerPath.equals(path))
                return;
        }

        try {
            if (isEmpty(path)) {
                if (audioModelListener != null)
                    audioModelListener.onAudioMessage(gets(R.string.not_get_voice_player_url));
                return;
            }

            mediaPlayer = createMedia(context, path);

            if (mediaPlayer != null)
                MediaUtils.stop(mediaPlayer);

            mPlayerPath = path;

            MediaUtils.prepareMedia(mediaPlayer, mp -> {
                play(mediaPlayer);
            }, (mp, what, extra) -> {
                destroy();
                return false;
            }, mp -> {
                destroy()
                ;
            });

//            mediaPlayer.setLooping(false);//循环播放

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

        return isplay(mediaPlayer);
    }

    /**
     * 音频暂停
     */
    public void audioPause() {
        //如果在播放中，立刻暂停。
        if (mediaPlayer != null && isplay(mediaPlayer)) {
            pause(mediaPlayer);
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
        closeMedia(mediaPlayer);
        mediaPlayer = null;
    }

    public void delAudioFile() {

        BaseLogUtils.w("删除音频文件夹内的音频文件  === " + deleteAllInDir(getPath() + File.separator + "audio"));
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
