package com.gxdingo.sg.biz;

import com.gxdingo.sg.bean.UpLoadBean;
import com.gxdingo.sg.bean.WebBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;
import com.kikis.commnlibrary.view.GridPictureEditing;

import java.util.ArrayList;
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

        /**
         * 获取文章列表
         *
         * @param identifier
         */
        void getDataList(String identifier);

        //投诉
        void complaint(String reason, String toString, ArrayList<GridPictureEditing.PictureValue> values, String sendIdentifier, int roleId, String uuid);
    }

    public interface IMComplaintListener {
        /**
         * 获取投诉照片数据
         *
         * @param upLoadBean 服务器返回上传图片的ULR
         */
        void getPhotoDataList(UpLoadBean upLoadBean);

        /**
         * 投诉列表回调
         *
         * @param list
         */
        void onArticleListResult(List<WebBean> list);
    }
}
