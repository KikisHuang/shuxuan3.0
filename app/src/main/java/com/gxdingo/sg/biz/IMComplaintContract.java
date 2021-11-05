package com.gxdingo.sg.biz;

import com.gxdingo.sg.bean.UpLoadBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * IM-投诉契约类
 *
 * @author JM
 */
public class IMComplaintContract {

    public interface IMComplaintPresenter extends MvpPresenter<BasicsListener, IMComplaintListener> {
        /**
         * 添加投诉图片
         *
         * @param num 可选照片数
         */
        void addPhoto(int num);

    }

    public interface IMComplaintListener {
        /**
         * 获取投诉照片数据
         *
         * @param upLoadBean 服务器返回上传图片的ULR
         */
        void getPhotoDataList(UpLoadBean upLoadBean);

    }
}
