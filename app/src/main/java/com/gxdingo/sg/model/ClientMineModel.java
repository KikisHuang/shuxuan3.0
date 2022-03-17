package com.gxdingo.sg.model;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.gxdingo.sg.bean.StoreAuthInfoBean;
import com.kikis.commnlibrary.activitiy.BaseActivity;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.utils.RxUtil;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static com.gxdingo.sg.utils.LocalConstant.ADD;

/**
 * @author: Kikis
 * @date: 2021/3/31
 * @page:
 */
public class ClientMineModel {


    public ClientMineModel() {

    }


    public void edittextFocusChangeListener(EditText edittext, View.OnFocusChangeListener listener) {
        edittext.setOnFocusChangeListener(listener);
    }

    /**
     * 判断商家资质信息是否缺少
     *
     * @param context
     * @param data
     * @param customResultListener
     */
    public void getQualification(Context context, List<StoreAuthInfoBean.CategoryListBean> data, CustomResultListener customResultListener) {

        RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {
            boolean isNoQualif = false;

            for (StoreAuthInfoBean.CategoryListBean clb : data) {
                if (clb.getType() == 1 && clb.getProveStatus() != 1) {
                    isNoQualif = true;
                    break;
                }
            }


            e.onNext(isNoQualif);
            e.onComplete();
        }), (BaseActivity) context).subscribe(o -> {
            if (customResultListener != null)
                customResultListener.onResult(o);
        });

    }

    /**
     * 获取商家资质状态
     *
     * @param context
     * @param data
     * @param customResultListener
     */
    public void getQualificationStatus(Context context, List<StoreAuthInfoBean.CategoryListBean> data, CustomResultListener customResultListener) {

        RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {

            for (StoreAuthInfoBean.CategoryListBean clb : data) {
                if (clb.getType() == 1 && clb.getProveStatus() != 1) {
                    e.onNext(clb);
                    break;
                }
            }
            e.onComplete();
        }), (BaseActivity) context).subscribe(o -> {
            if (customResultListener != null)
                customResultListener.onResult(o);
        });

    }

}
