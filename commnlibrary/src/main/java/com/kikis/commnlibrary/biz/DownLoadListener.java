package com.kikis.commnlibrary.biz;

import java.io.File;

/**
 * Created by Kikis on 2018/3/19.
 */

public interface DownLoadListener {

    void DownLoadSucceed(File file);

    void DownLoadFailed();

    void DownLoadProgress(int pro);

}
