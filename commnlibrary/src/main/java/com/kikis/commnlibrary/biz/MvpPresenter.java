package com.kikis.commnlibrary.biz;

import android.content.Context;
import android.os.Bundle;

import com.trello.rxlifecycle3.LifecycleProvider;

/**
 * Created by Kikis on 2018/8/20.
 * 定义P层生命周期与V层同步
 */

public interface MvpPresenter<BV, V> {


    void onMvpAttachView(Context context, BV bview, V view);

    void onMvpStart();

    void onMvpResume();

    void onMvpPause();

    void onMvpStop();

    void onMvpSaveInstanceState(Bundle savedInstanceState);

    void onMvpDetachView(boolean retainInstance);

    void onMvpDestroy();

}
