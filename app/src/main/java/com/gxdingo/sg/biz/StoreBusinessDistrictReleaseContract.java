package com.gxdingo.sg.biz;

import com.gxdingo.sg.bean.UpLoadBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

import java.util.List;

/**
 * 商家商圈发布契约类
 *
 * @author JM
 */
public class StoreBusinessDistrictReleaseContract {

    public interface StoreBusinessDistrictReleasePresenter extends MvpPresenter<BasicsListener, StoreBusinessDistrictReleaseListener> {
        /**
         * 添加图片
         *
         * @param num
         */
        void addPhoto(int type, int num);

        /**
         * 提交商圈信息
         */
        void releaseBusinessDistrict(String content, List<String> images);
    }

    public interface StoreBusinessDistrictReleaseListener {
        /**
         * 获取图片数据
         *
         * @param upLoadBean 服务器返回上传图片的ULR
         */
        void getPhotoDataList(UpLoadBean upLoadBean);

        /**
         * 发布商圈信息成功
         */
        void releaseBusinessDistrictSuccess(String msg);
    }
}
