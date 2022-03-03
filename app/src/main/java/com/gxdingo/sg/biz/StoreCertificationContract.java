package com.gxdingo.sg.biz;

import android.content.Context;

import com.gxdingo.sg.bean.BusinessScopeEvent;
import com.gxdingo.sg.bean.StoreBusinessScopeBean;
import com.gxdingo.sg.bean.StoreCategoryBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.biz.MvpPresenter;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;
import java.util.Map;

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
        void confirmBusinessScope(List<StoreBusinessScopeBean.ListBean> businessScopeBeans, List<String> licenceUrls);

        //提交认证信息
        void submitCertification(Context context, String avatar, String name, List<StoreCategoryBean> storeCategory
                , String regionPath, String address, String businessLicence, String storeLicence, double longitude, double latitude);

        //刷新登录信息
        void getLoginInfoStatus();

        //退出登录
        void logout();

        void getInvitationCode();

        //上传特殊分类许可证
        void selectedLicence(CustomResultListener customResultListener);

        //批量上传图片
        void batchUpload(Map<Integer, LocalMedia> tempLicenceMap,CustomResultListener customResultListener);
    }

    public interface StoreCertificationListener {
        //上传图片
        void uploadImage(String url);

        //经营范围
        void onBusinessScopeResult(List<StoreBusinessScopeBean.ListBean> businessScopes);

        //关闭经营范围
        void closeBusinessScope(BusinessScopeEvent businessScopeEvent);

        //已认证通过
        void certificationPassed();

        //正在审核
        void onReview();

        //被驳回
        void rejected(String rejectReason);

        //显示活动类型布局
        void showActivityTypeLayout(int type);
    }
}
