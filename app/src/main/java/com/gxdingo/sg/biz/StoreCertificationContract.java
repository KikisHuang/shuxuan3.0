package com.gxdingo.sg.biz;

import com.gxdingo.sg.bean.BusinessScopeEvent;
import com.gxdingo.sg.bean.StoreBusinessScopeBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.MvpPresenter;

import java.util.List;

/**
 * 商家认证契约类
 *
 * @author JM
 */
public class StoreCertificationContract {

    public interface StoreCertificationPresenter extends MvpPresenter<BasicsListener, StoreCertificationListener> {
        //相册/相机点击
        void photoItemClick(int type, int pos, boolean isCrop);

        //获取分类（业务范围）
        void getCategory();

        //确定经营范围
        void confirmBusinessScope(List<StoreBusinessScopeBean.ListBean> businessScopeBeans);
    }

    public interface StoreCertificationListener {
        //上传图片
        void uploadImage(String url);

        //经营范围
        void onBusinessScopeResult(List<StoreBusinessScopeBean.ListBean> businessScopes);

        //关闭经营范围
        void closeBusinessScope(BusinessScopeEvent businessScopeEvent);
    }
}
