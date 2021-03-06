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
import com.gxdingo.sg.bean.AliASRBean;
import com.gxdingo.sg.bean.DialogParam;
import com.gxdingo.sg.bean.NlsConfig;
import com.gxdingo.sg.bean.ParamsBean;
import com.gxdingo.sg.biz.AudioModelListener;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.MediaUtils;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.utils.BaseLogUtils;
import com.kikis.commnlibrary.utils.GsonUtil;
import com.kikis.commnlibrary.utils.RxUtil;
import com.luck.picture.lib.entity.LocalMedia;
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
 * @page:?????????model???
 */
public class AudioModel {

    private MediaPlayer mediaPlayer;

    //??????????????????
    private String mRecordPath = "";
    //??????????????????
    private String mPlayerPath = "";
    //???????????????
    private boolean mIsRecording = false;

    private HandlerThread mHanderThread;


    private List<String> task_list = new ArrayList<String>();

    private int MAX_TASKS = 1;

    private Handler mHandler;

    public AudioModel() {

        mHanderThread = new HandlerThread("process_thread");
        mHanderThread.start();
        mHandler = new Handler(mHanderThread.getLooper());
    }


    /**
     * ????????????
     *
     * @param context
     * @param path
     */
    public void audioPlayer(Context context, String path, AudioModelListener audioModelListener) {

        //????????????????????????????????????
        if (mediaPlayer != null && isplay(mediaPlayer)) {
            destroyMediaPlayer();
            //????????????????????????????????????
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
                destroyMediaPlayer();
                return false;
            }, mp -> {
                destroyMediaPlayer()
                ;
            });

//            mediaPlayer.setLooping(false);//????????????

        } catch (Exception e) {
            if (audioModelListener != null) {
                audioModelListener.onAudioMessage(gets(R.string.audio_play_error));
                audioModelListener.onAudioError(e.getMessage());
            }
            LogUtils.i("path === " + path);
            LogUtils.e("audio play error === " + e);
            destroyMediaPlayer();
        }
    }

    public boolean isPlaying() {
        if (mediaPlayer == null)
            return false;

        return isplay(mediaPlayer);
    }

    /**
     * ????????????
     */
    public void audioPause() {
        //????????????????????????????????????
        if (mediaPlayer != null && isplay(mediaPlayer)) {
            pause(mediaPlayer);
            mediaPlayer.reset();
            stopRecordering();
        }
    }

    /**
     * ??????????????????
     *
     * @return
     */
    public int getDuration() {
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(mRecordPath);//????????????????????????
            mediaPlayer.prepare();//??????????????????MediaPlayer

            return mediaPlayer.getDuration() / 1000;
        } catch (IOException e) {
            LogUtils.e("getDuration error === " + e);
        }
        return 0;
    }

    /**
     * ????????????
     */
    public void destroyMediaPlayer() {
        closeMedia(mediaPlayer);
        mediaPlayer = null;
    }

    public void delAudioFile() {

        BaseLogUtils.w("???????????????????????????????????????  === " + deleteAllInDir(getPath() + File.separator + "audio"));
    }


    /**
     * ????????????????????????
     *
     * @return
     */
    public String getRecordPath() {
        return mRecordPath;
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    public String getPlayerPath() {
        return mPlayerPath;
    }

    /**
     * ???????????????
     *
     * @return
     */
    public boolean isRecording() {
        return mIsRecording;
    }


    /**
     * ????????????
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
                    listener.onMessage("???????????????????????????");
                    listener.onSucceed(RECORD_SUCCEED);
                }
            }
        }, lifecycle);
    }

    /**
     * ????????????
     */
    public void stopRecorder() {
        mIsRecording = false;
        stopRecordering();
    }

    public void delRecord() {
        // ????????????
        if (FileUtils.delete(mRecordPath)) {
            LogUtils.i("??????????????????");
            mRecordPath = "";
        } else
            LogUtils.i("??????????????????");
    }

    public void destroy() {

/*        new Thread(() -> {
            //????????????
            for (String taskId : task_list) {
                NativeNui.GetInstance().cancelFileTranscriber(taskId);
            }
        }).start();*/

        if (mHandler != null) {
            mHandler.removeCallbacks(mHanderThread);
        }

        destroyMediaPlayer();

        delRecord();

    }

    /**
     * ?????????aliyun??????
     *
     * @param context
     * @param iNativeFileTransCallback
     */
    public void initAliyunNui(Context context, String token, INativeFileTransCallback iNativeFileTransCallback) {

        //????????????????????????SDK?????????????????????
        if (CommonUtils.copyAssetsData(context)) {
            Log.i(TAG, "copy assets data done");
        } else {
            Log.i(TAG, "copy assets failed");
            return;
        }
        //??????????????????
        String assets_path = CommonUtils.getModelPath(context);
        LogUtils.i("use workspace " + assets_path);

        String debug_path = context.getExternalCacheDir().getAbsolutePath() + "/debug_" + System.currentTimeMillis();
        boolean create = createOrExistsDir(debug_path);

        //?????????SDK????????????????????????Auth.getAliYunTicket???????????????ID????????????????????????
        int ret = NativeNui.GetInstance().initialize(iNativeFileTransCallback, genInitParams(assets_path, "", token), Constants.LogLevel.LOG_LEVEL_VERBOSE);

      /*  if (ret == Constants.NuiResultCode.SUCCESS) {
            mInit = true;
        }*/

        //???????????????????????????????????????API??????
        NativeNui.GetInstance().setParams(genParams());
    }


    private String genInitParams(String workpath, String debugpath, String token) {
        String str = "";

        ParamsBean paramsBean = new ParamsBean(LocalConstant.VOICE_APPKEY, token, "https://nls-gateway.cn-shanghai.aliyuncs.com/stream/v1/FlashRecognizer", getUniqueDeviceId(), workpath, debugpath);

        str = GsonUtil.gsonToStr(paramsBean);

        LogUtils.i("InsideUserContext:" + str);
        return str;
    }

    private String genParams() {
        String params = "";
        try {
            JSONObject nls_config = new JSONObject();

            JSONObject tmp = new JSONObject();
            tmp.put("nls_config", nls_config);
//            ?????????HttpDns??????????????????
//            tmp.put("direct_ip", Utils.getDirectIp());
            params = tmp.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    /**
     * ??????????????????text ??????????????????text
     *
     * @param context
     * @param sentences
     */
    public void getASRText(Context context, List<AliASRBean.FlashResultBean.SentencesBean> sentences, CustomResultListener customResultListener) {

        RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {
            StringBuilder stringBuilder = new StringBuilder();
            for (AliASRBean.FlashResultBean.SentencesBean data : sentences) {
                stringBuilder.append(data.getText());
            }
            e.onNext(stringBuilder.toString());
            e.onComplete();
        }), (LifecycleProvider) context).subscribe(o -> {
            if (customResultListener != null)
                customResultListener.onResult(o);

        });
    }

    /**
     * ?????????????????????
     *
     * @param mVoiceUrl
     */
    public void aliYunStartFileTranscriber(String mVoiceUrl) {

        mHandler.post(() -> {
            synchronized (task_list) {
                task_list.clear();
                for (int i = 0; i < MAX_TASKS; i++) {
                    byte[] task_id = new byte[32];
                    int ret = NativeNui.GetInstance().startFileTranscriber(genDialogParams(mVoiceUrl), task_id);
                    String taskId = new String(task_id);
                    task_list.add(taskId);
                    LogUtils.i("start task id " + taskId + " done with " + ret);
                }
            }
        });
    }


    private String genDialogParams(CharSequence mVoiceUrl) {
        String params = "";

        NlsConfig config = new NlsConfig("aac");

        LogUtils.w("??????????????????  ====  " + mVoiceUrl);

        DialogParam dialogParam = new DialogParam((String) mVoiceUrl, config);
        params = GsonUtil.gsonToStr(dialogParam);
        LogUtils.i("dialog params: " + params);
        return params;
    }
}
