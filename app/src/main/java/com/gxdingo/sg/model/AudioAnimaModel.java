package com.gxdingo.sg.model;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;

import com.alibaba.idst.nui.NativeNui;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.AudioModelListener;
import com.gxdingo.sg.biz.AudioStatusListener;
import com.gxdingo.sg.biz.NetWorkListener;
import com.trello.rxlifecycle3.LifecycleProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.FileUtils.deleteAllInDir;
import static com.gxdingo.sg.utils.ClientLocalConstant.RECORD_SUCCEED;
import static com.gxdingo.sg.utils.LocalConstant.AAC;
import static com.gxdingo.sg.utils.MediaRecorderUtil.startRecordering;
import static com.gxdingo.sg.utils.MediaRecorderUtil.stopRecordering;
import static com.kikis.commnlibrary.utils.CommonUtils.getPath;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;

/**
 * @author: Kikis
 * @date: 2021/4/20
 * @page:音频动画的model类
 */
public class AudioAnimaModel {

    private AnimationDrawable animationDrawable;

    public static AudioAnimaModel instance;

    private Disposable mDisposable;

    private AudioAnimaModel() {
    }

    public static AudioAnimaModel getInstance() {
        if (instance == null) {
            synchronized (AudioAnimaModel.class) {
                if (instance == null) {
                    instance = new AudioAnimaModel();
                }
            }
        }
        return instance;
    }

}
